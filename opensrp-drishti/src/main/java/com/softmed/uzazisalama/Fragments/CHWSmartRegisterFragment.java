package com.softmed.uzazisalama.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.softmed.uzazisalama.AncSmartRegisterActivity;

import org.ei.opensrp.drishti.R;
import com.softmed.uzazisalama.Repository.LocationSelectorDialogFragment;
import com.softmed.uzazisalama.pageradapter.CHWPagerAdapter;
import com.softmed.uzazisalama.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

public class CHWSmartRegisterFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = CHWSmartRegisterFragment.class.getSimpleName();
    private String locationDialogTAG = "locationDialogTAG";
    private JSONObject fieldOverides = new JSONObject();
    private TabLayout tabs;
    private LayoutInflater inflater;
    private ImageButton imageButton;
    private CHWPagerAdapter adapter;
    private ViewPager feeds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        View v = inflater.inflate(R.layout.activity_chwregister, container, false);
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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();

            }
        });

        tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(feeds);

//        ((TextView) v.findViewById(R.id.txt_title_label)).setText("Community HW");
        // tabs icons
        tabs.getTabAt(0).setIcon(R.drawable.ic_account_circle);
        tabs.getTabAt(1).setIcon(R.drawable.ic_message_bulleted);

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
                .newInstance((AncSmartRegisterActivity) getActivity(), null, context().anmLocationController().get(),
                        "pregnant_mothers_pre_registration")
                .show(ft, locationDialogTAG);
    }


    @Override
    protected void onCreation() {

    }

    @Override
    public void refreshListView() {

        CHWFollowUpFragment chwFollowUpFragment = (CHWFollowUpFragment) findFragmentByPosition(0);
        if (chwFollowUpFragment != null) {
            chwFollowUpFragment.populateData();
        }

        CHWPreRegistrationFragment chwPreRegistrationFragment = (CHWPreRegistrationFragment) findFragmentByPosition(1);
        if (chwPreRegistrationFragment != null) {
            chwPreRegistrationFragment.populateData();
        }

    }

    public android.support.v4.app.Fragment findFragmentByPosition(int position) {
        FragmentPagerAdapter fragmentPagerAdapter = adapter;
        return getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + feeds.getId() + ":" + fragmentPagerAdapter.getItemId(position));
    }
}
