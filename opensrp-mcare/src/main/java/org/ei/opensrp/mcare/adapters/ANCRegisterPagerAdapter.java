package org.ei.opensrp.mcare.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ei.opensrp.mcare.fragment.ANCRegister1stFragment;
import org.ei.opensrp.mcare.fragment.ANCRegister2ndFragment;


/**
 * Created by ali on 8/22/17.
 */

public class ANCRegisterPagerAdapter extends FragmentPagerAdapter {

    public ANCRegisterPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ANCRegister1stFragment();

            case 1:
                return new ANCRegister2ndFragment();

//            case 2:
//                return new ANCRegister3rdFragment();

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
                return "Kuhusu mama";

            case 1:
                return "Vidokezo";

//            case 2:
//                return new ANCRegister3rdFragment();

            default:
                return String.valueOf(position + 1);
        }
    }
}
