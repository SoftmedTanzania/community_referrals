package com.softmed.uzazisalama.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.softmed.uzazisalama.Fragments.CHWFollowUpFragment;
import com.softmed.uzazisalama.Fragments.CHWPreRegistrationFragment;


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
                return new CHWFollowUpFragment() ;

            case 1:
                return new CHWPreRegistrationFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mama wajawazito";

            case 1:
                return "Usajili wa awali";

            default:
                return String.valueOf(position + 1);
        }
    }
}
