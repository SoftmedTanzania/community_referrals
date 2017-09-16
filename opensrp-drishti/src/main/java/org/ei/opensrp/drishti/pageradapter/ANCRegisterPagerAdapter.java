package org.ei.opensrp.drishti.pageradapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.ei.opensrp.drishti.Fragments.AncRegister1stFragment;
import org.ei.opensrp.drishti.Fragments.AncRegister2ndFragment;


/**
 * Created by ali on 8/22/17.
 */

public class ANCRegisterPagerAdapter extends FragmentPagerAdapter {

    private AncRegister1stFragment ancRegister1stFragment;
    private AncRegister2ndFragment ancRegister2ndFragment;

    public ANCRegisterPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (ancRegister1stFragment == null)
                    ancRegister1stFragment = new AncRegister1stFragment();

                return ancRegister1stFragment;

            case 1:
                if (ancRegister2ndFragment == null)
                    ancRegister2ndFragment = new AncRegister2ndFragment();

                return ancRegister2ndFragment;
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

            case 2:
                return "FollowUP";

            default:
                return String.valueOf(position + 1);
        }
    }
}
