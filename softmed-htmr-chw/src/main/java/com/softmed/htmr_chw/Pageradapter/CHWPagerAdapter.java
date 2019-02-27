package com.softmed.htmr_chw.Pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.softmed.htmr_chw.Fragments.ReferredClientsFragment;
import com.softmed.htmr_chw.Fragments.FollowupClientsFragment;
import com.softmed.htmr_chw.Fragments.ReportFragment;


/**
 * Created by martha on 8/22/17.
 */

public class CHWPagerAdapter extends FragmentPagerAdapter {

    public CHWPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ReferredClientsFragment() ;

            case 1:
                return new FollowupClientsFragment();

            case 2:
                return new ReportFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "wakufuatilia";

            case 1:
                return "rufaa ya awali";

            case 2:
                return "reports";

            default:
                return String.valueOf(position + 1);
        }
    }
}
