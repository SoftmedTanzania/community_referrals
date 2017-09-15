package org.ei.opensrp.drishti.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ei.opensrp.drishti.Fragments.CHWFollowUpFragment;
import org.ei.opensrp.drishti.Fragments.CHWRegistrationFragment;


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
                return new CHWRegistrationFragment();
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
                return "usaili wa awali";

            default:
                return String.valueOf(position + 1);
        }
    }
}
