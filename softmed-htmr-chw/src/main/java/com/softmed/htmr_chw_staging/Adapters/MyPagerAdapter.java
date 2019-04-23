package com.softmed.htmr_chw_staging.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.softmed.htmr_chw_staging.Fragments.CBHSClientsListFragment;
import com.softmed.htmr_chw_staging.Fragments.ReferralClientsListFragment;

public  class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private static String[] pageNames;

        public MyPagerAdapter(FragmentManager fragmentManager, String[] pageNames) {
            super(fragmentManager);
            this.pageNames = pageNames;
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
                    return CBHSClientsListFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ReferralClientsListFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageNames[position];
        }

    }