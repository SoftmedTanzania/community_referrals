package org.ei.opensrp.drishti.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * Created by ali on 9/14/17.
 */

public class OrientationHelper {

    public static void lockOrientationPortrait(Activity activity){
     activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public static void lockOrientationLandscape(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
