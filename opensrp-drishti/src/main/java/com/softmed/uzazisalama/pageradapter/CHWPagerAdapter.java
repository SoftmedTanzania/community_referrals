package org.ei.opensrp.drishti.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ei.opensrp.drishti.Fragments.CHWFollowUpFragment;
import org.ei.opensrp.drishti.Fragments.CHWPreRegistrationFragment;


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
                return "wakufuatilia";

            case 1:
                return "rufaa ya awali";

            default:
                return String.valueOf(position + 1);
        }
    }
}
