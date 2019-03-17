package com.softmed.htmr_chw.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softmed.htmr_chw.Activities.ChwSmartRegisterActivity;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;


public class ClientsFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = ClientsFragment.class.getSimpleName();
    private ViewPager viewPager;
    private Typeface robotoRegular, sansBold;

    public ClientsFragment() {
    }

    public static ClientsFragment newInstance() {
        ClientsFragment fragment = new ClientsFragment();

        return fragment;
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_clients, container, false);
        setupviews(v);

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
    public void startRegistration() {

    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    private void setupviews(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);


        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(getActivity().getAssets(), "google_sans_bold.ttf");

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), new String[]{getString(R.string.cbhs_clients_label),getString(R.string.referral_clients_label)});
        viewPager.setAdapter(myPagerAdapter);
        ChwSmartRegisterActivity.myTabLayout.setupWithViewPager(viewPager);

        LinearLayout tabLinearLayout2 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabContent2 = tabLinearLayout2.findViewById(R.id.tabContent);
        ImageView tabImage = tabLinearLayout2.findViewById(R.id.tabImage);
        tabContent2.setText(R.string.cbhs_clients_label);
        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_white_48dp));
        try {
            ChwSmartRegisterActivity.myTabLayout.getTabAt(0).setCustomView(tabLinearLayout2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout tabLinearLayout3 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabContent3 = (TextView) tabLinearLayout3.findViewById(R.id.tabContent);
        ImageView tabImage1 = tabLinearLayout3.findViewById(R.id.tabImage);
        tabContent3.setText(R.string.referral_clients_label);
        tabImage1.setImageDrawable(getResources().getDrawable(R.drawable.baseline_person_white_48dp));
        try {
            ChwSmartRegisterActivity.myTabLayout.getTabAt(1).setCustomView(tabLinearLayout3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private static String [] pageNames;

        public MyPagerAdapter(FragmentManager fragmentManager, String [] pageNames) {
            super(fragmentManager);
            this.pageNames =pageNames;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new CBHSClientsListFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new ReferralsListFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageNames[position];
        }

    }
}
