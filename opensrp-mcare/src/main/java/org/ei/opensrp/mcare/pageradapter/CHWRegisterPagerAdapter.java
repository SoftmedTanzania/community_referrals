package org.ei.opensrp.mcare.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ei.opensrp.mcare.fragment.AncRegister1stFragment;
import org.ei.opensrp.mcare.fragment.AncRegister2ndFragment;
import org.ei.opensrp.mcare.fragment.CHWFollowUpFragment;
import org.ei.opensrp.mcare.fragment.CHWRegistrationFragment;


/**
 * Created by ali on 8/22/17.
 */

public class CHWRegisterPagerAdapter extends FragmentPagerAdapter {

    public CHWRegisterPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CHWRegistrationFragment();

            case 1:
                return new CHWFollowUpFragment();
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
