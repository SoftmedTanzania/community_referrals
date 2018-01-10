package org.ei.opensrp.drishti.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;
import org.ei.opensrp.drishti.ChwSmartRegisterActivity;
import org.ei.opensrp.drishti.LoginActivity;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.pageradapter.CHWPagerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.AsyncTask;
import org.ei.opensrp.drishti.util.DonutChart;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.DrishtiApplication;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.valueOf;

public class CHWSmartRegisterFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = CHWSmartRegisterFragment.class.getSimpleName();
    private String locationDialogTAG = "locationDialogTAG";
    private JSONObject fieldOverides = new JSONObject();
    private TabLayout tabs;
    private LayoutInflater inflater;
    private ImageButton imageButton;
    private CHWPagerAdapter adapter;
    private ViewPager feeds;
    private Toolbar toolbar;
    private View v;
    private TextView cardPickStartDate, cardPickEndDate;
    TextView successView, unsuccessView;
    Long success = (long) 0;
    Long unsuccess = (long) 0;
    private DonutChart donutChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.inflater = inflater;
        v = inflater.inflate(R.layout.activity_chwregister, container, false);
        imageButton = (ImageButton) v.findViewById(R.id.register_client);

        adapter = new CHWPagerAdapter(getActivity().getSupportFragmentManager());

        feeds = (ViewPager) v.findViewById(R.id.viewPager);
        feeds.setAdapter(adapter);

        feeds.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        donutChart = (DonutChart)v.findViewById(R.id.donutChart) ;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();

            }
        });

        tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(feeds);


        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

        TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
        tabContent.setText("Wakufuatilia");
        tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_down_white_24dp, 0, 0, 0);



        LinearLayout tabLinearLayout2 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabContent2 = (TextView) tabLinearLayout2.findViewById(R.id.tabContent);
        tabContent2.setText("Rufaa ya awali");
        tabContent2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_up_white_24dp, 0, 0, 0);


        tabs.getTabAt(0).setCustomView(tabContent2);
        tabs.getTabAt(1).setCustomView(tabContent);


        final int colorWhite = ContextCompat.getColor(getActivity(), android.R.color.white);
        final int colorPrimaryLight = ContextCompat.getColor(getActivity(), R.color.primary_light);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorPrimaryLight, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);


        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item click event
                        onOptionsItemSelected(item);
                        return true;
                    }
                });

        successView =  (TextView) v.findViewById(R.id.count_two);
        successView.setText(valueOf(success));
        Log.d(TAG,"setting counts"+success);
        unsuccessView =  (TextView) v.findViewById(R.id.count_one);
        unsuccessView.setText(valueOf(unsuccess));
        Log.d(TAG," setiing unsuccess counts"+unsuccess);



        
        return v;
    }



    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }



    @Override
    public void startRegistration() {
        Log.d(TAG, "starting registrations");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance((ChwSmartRegisterActivity) getActivity(), null, context().anmLocationController().get(),
                        "pregnant_mothers_pre_registration")
                .show(ft, locationDialogTAG);
    }


    @Override
    protected void onCreation() {

    }

    @Override
    public void refreshListView() {
        try {
            ReferredClientsFragment referredClientsFragment = (ReferredClientsFragment) findFragmentByPosition(0);
            if (referredClientsFragment != null) {
                referredClientsFragment.populateData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            FollowupClientsFragment followupClientsFragment = (FollowupClientsFragment) findFragmentByPosition(1);
            if (followupClientsFragment != null) {
                followupClientsFragment.populateData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public android.support.v4.app.Fragment findFragmentByPosition(int position) {
        FragmentPagerAdapter fragmentPagerAdapter = adapter;
        return getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + feeds.getId() + ":" + fragmentPagerAdapter.getItemId(position));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateMenuItem:
                ((ChwSmartRegisterActivity)getActivity()).updateFromServer();
                return true;

            case R.id.help:
                Toast.makeText(getActivity(), "help implementation under construction", LENGTH_SHORT).show();
                return true;

            case R.id.logout:
                ((BoreshaAfyaApplication)getActivity().getApplication()).logoutCurrentUser();

                return true;
            case R.id.switchLanguageMenuItem:
                String newLanguagePreference = context().userService().switchLanguagePreference();
//                String newLanguagePreference = LoginActivity.switchLanguagePreference();

                LoginActivity.setLanguage();
                Toast.makeText(getActivity(), "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
//                getActivity().recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSuccessValue(Long value){
        Log.d(TAG,"succesfull count "+value);
        this.success = value;
        successView.setText(valueOf(success));
    }

    public void setUnSuccessValue(Long value){
        Log.d(TAG,"unsuccesfull count after"+value);
        this.unsuccess = value;
        unsuccessView.setText(valueOf(unsuccess));

    }





}
