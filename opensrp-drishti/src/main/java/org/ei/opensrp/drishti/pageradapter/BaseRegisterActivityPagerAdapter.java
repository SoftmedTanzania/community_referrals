package org.ei.opensrp.drishti.pageradapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.ei.opensrp.drishti.Fragments.AncRegisterFormFragment;
import org.ei.opensrp.view.fragment.DisplayFormFragment;

/**
 * Created by koros on 11/2/15.
 */
public class BaseRegisterActivityPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = BaseRegisterActivityPagerAdapter.class.getSimpleName();
    public static final String ARG_PAGE = "page";
    String[] dialogOptions;
    Fragment mBaseFragment;
    Fragment mProfileFragment;
    public int offset = 0;

    public BaseRegisterActivityPagerAdapter(FragmentManager fragmentManager, String[] dialogOptions, Fragment baseFragment) {
        super(fragmentManager);
        this.dialogOptions = dialogOptions;
        this.mBaseFragment = baseFragment;
        offset += 1;
    }
    public BaseRegisterActivityPagerAdapter(FragmentManager fragmentManager, String[] dialogOptions, Fragment baseFragment, Fragment mProfileFragment) {
        super(fragmentManager);
        this.dialogOptions = dialogOptions;
        this.mBaseFragment = baseFragment;
        this.mProfileFragment = mProfileFragment;
        offset += 2;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mBaseFragment;
                break;
            case 1:
                Log.d(TAG,"setting AncRegisterFormFragment");
                fragment = new AncRegisterFormFragment();
                break;
            default:
                String formName = dialogOptions[position - offset]; // account for the base fragment
                Log.d(TAG,"Form name  = "+formName);
                DisplayFormFragment f = new DisplayFormFragment();
                f.setFormName(formName);
                fragment = f;
                break;
        }

        Log.d(TAG,"After setting house hold registration Fragment");
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        if(fragment!=null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return dialogOptions.length + offset; // index 0 is always occupied by the base fragment
    }

    public int offset() {
        return offset;
    }

}
