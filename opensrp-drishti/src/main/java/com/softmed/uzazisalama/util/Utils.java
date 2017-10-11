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

package com.softmed.uzazisalama.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
        Log.d(TAG, "details string = " + convertStandardJSONString(details));
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
                    commonPersonObject.getColumnmaps().get("CATCHMENT_AREA"),
                    commonPersonObject.getColumnmaps().get("CreatedBy"),
                    commonPersonObject.getColumnmaps().get("ModifyBy"),
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

    public static void sendRegistrationAlert(String phoneNumber){
        Log.d(TAG,"sending registration alerts to rapid pro server");
        JSONObject object = new JSONObject();
        try {
            object.put("urns",new JSONArray().put("tel:"+phoneNumber));
            object.put("flow","c342cb9e-67a6-4a11-b063-1ac04fbbc26f");
            Log.d(TAG,"payload = "+object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        URL url = null;
        try {
            url = new URL("http://45.56.90.103:8000/api/v2/flow_starts.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Token 7dae598d311c91fb3907687fdf6ed900fecb1a05");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(object.toString().getBytes("UTF-8"));
            os.flush();
            os.close();


            int responseCode = conn.getResponseCode();
            Log.d(TAG,"POST Response Code :: " + responseCode);

            // read the response
            InputStream in  = conn.getInputStream();

            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            JSONObject jsonObject = new JSONObject(result);

            Log.d(TAG,"response is "+jsonObject.toString());

            in.close();
            os.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
