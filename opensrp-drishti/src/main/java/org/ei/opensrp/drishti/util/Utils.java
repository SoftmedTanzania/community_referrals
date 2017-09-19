/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ei.opensrp.drishti.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Class containing some static utility methods.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    ;


    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }


    public static MotherPersonObject convertToMotherPersonObject(CommonPersonObject commonPersonObject) {
        String details = commonPersonObject.getColumnmaps().get("details");
        Log.d(TAG, "details string = " + convertStandardJSONString(details.substring(1, details.length() - 1)));
        try {
            return new MotherPersonObject(
                    commonPersonObject.getColumnmaps().get("id"),
                    commonPersonObject.getColumnmaps().get("relationalid"),
                    commonPersonObject.getColumnmaps().get("MOTHERS_FIRST_NAME"),
                    commonPersonObject.getColumnmaps().get("MOTHERS_LAST_NAME"),
                    commonPersonObject.getColumnmaps().get("MOTHERS_ID"),
                    commonPersonObject.getColumnmaps().get("MOTHERS_SORTVALUE"),
                    commonPersonObject.getColumnmaps().get("EXPECTED_DELIVERY_DATE"),
                    commonPersonObject.getColumnmaps().get("MOTHERS_LAST_MENSTRUATION_DATE"),
                    commonPersonObject.getColumnmaps().get("FACILITY_ID"),
                    commonPersonObject.getColumnmaps().get("IS_PNC"),
                    commonPersonObject.getColumnmaps().get("IS_VALID"),
                     commonPersonObject.getColumnmaps().get("details"),
                    commonPersonObject.getColumnmaps().get("type")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<MotherPersonObject> convertToMotherPersonObjectList(List<CommonPersonObject> commonPersonObjectsList) {
        List<MotherPersonObject> mothers = new ArrayList<>();
        for (CommonPersonObject common : commonPersonObjectsList) {
            mothers.add(convertToMotherPersonObject(common));
        }


        return mothers;
    }

    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("\\", "");
        return data_json;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String generateRandomUUIDString(){
        return UUID.randomUUID().toString();
    }

}
