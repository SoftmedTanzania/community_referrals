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

package com.softmed.htmr_chw.util;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import com.softmed.htmr_chw.Domain.ClientReferral;
import com.softmed.htmr_chw.Domain.FacilityObject;
import com.softmed.htmr_chw.Domain.ReferralServiceObject;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.repository.ReferralRepository;
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

    public static ClientReferral convertToClientPersonObject(Cursor cursor) {
        try {
            ClientReferral clientReferral =  new ClientReferral();
            clientReferral.setClient_id(cursor.getString(cursor.getColumnIndex(ClientRepository.CLIENT_ID)));
            clientReferral.setFirst_name(cursor.getString(cursor.getColumnIndex(ClientRepository.FIRST_NAME)));
            clientReferral.setMiddle_name(cursor.getString(cursor.getColumnIndex(ClientRepository.MIDDLE_NAME)));
            clientReferral.setSurname(cursor.getString(cursor.getColumnIndex(ClientRepository.SURNAME)));
            clientReferral.setGender(cursor.getString(cursor.getColumnIndex(ClientRepository.GENDER)));
            clientReferral.setDate_of_birth(cursor.getLong(cursor.getColumnIndex(ClientRepository.DOB)));

            clientReferral.setFacility_id(cursor.getString(5));

            clientReferral.setCommunity_based_hiv_service(cursor.getString(cursor.getColumnIndex(ClientRepository.CBHS)));
            clientReferral.setCtc_number(cursor.getString(cursor.getColumnIndex(ClientRepository.CTC_NUMBER)));
            clientReferral.setHelper_name(cursor.getString(cursor.getColumnIndex(ClientRepository.HELPER_NAME)));
            clientReferral.setHelper_phone_number(cursor.getString(cursor.getColumnIndex(ClientRepository.HELPER_PHONE_NUMBER)));
            clientReferral.setPhone_number(cursor.getString(cursor.getColumnIndex(ClientRepository.PHONE_NUMBER)));

            clientReferral.setReferral_date(cursor.getLong(cursor.getColumnIndex( ReferralRepository.ReferralDate)));
            clientReferral.setAppointment_date(cursor.getLong(cursor.getColumnIndex( ReferralRepository.AppointmentDate)));
            clientReferral.setReferral_service_id(cursor.getString(cursor.getColumnIndex(ReferralRepository.Service)));
            clientReferral.setReferral_status(cursor.getString(cursor.getColumnIndex(ReferralRepository.ReferralStatus)));
            clientReferral.setReferral_reason(cursor.getString(cursor.getColumnIndex(ReferralRepository.ReferralReason)));
            clientReferral.setIndicator_ids(cursor.getString(cursor.getColumnIndex(ReferralRepository.INDICATOR_IDS)));


            clientReferral.setOther_notes(cursor.getString(cursor.getColumnIndex(ReferralRepository.OTHER_NOTES)));
            clientReferral.setServices_given_to_patient(cursor.getString(cursor.getColumnIndex(ReferralRepository.SERVICES_GIVEN_TO_PATIENTS)));
            clientReferral.setVillage(cursor.getString(cursor.getColumnIndex(ClientRepository.VILLAGE)));
            clientReferral.setWard(cursor.getString(cursor.getColumnIndex(ClientRepository.WARD)));


            return clientReferral;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static FacilityObject convertToFacilityObject(CommonPersonObject commonPersonObject) {
        try {
            return new FacilityObject(
                    commonPersonObject.getColumnmaps().get("id"),
                    commonPersonObject.getColumnmaps().get("name")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } public static Indicator convertToIndicatorObject(CommonPersonObject commonPersonObject) {
        try {
            return new Indicator(
                    commonPersonObject.getColumnmaps().get("id"),
                    commonPersonObject.getColumnmaps().get("relationalid"),
                    commonPersonObject.getColumnmaps().get("indicatorName"),
                    commonPersonObject.getColumnmaps().get("indicatorNameSw"),
                    commonPersonObject.getColumnmaps().get("isActive")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ReferralServiceObject convertToServiceObject(CommonPersonObject commonPersonObject) {
        try {
            Log.d(TAG,"swahili names : "+commonPersonObject.getColumnmaps().get("name_sw"));
            return new ReferralServiceObject(
                    commonPersonObject.getColumnmaps().get("id"),
                    commonPersonObject.getColumnmaps().get("name"),
                    commonPersonObject.getColumnmaps().get("name_sw"),
                    commonPersonObject.getColumnmaps().get("category")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<ClientReferral> convertToClientReferralObjectList(Cursor cursor) {

        int cursorSize = cursor.getCount();
        cursor.moveToFirst();
        Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));


        List<ClientReferral> clientReferrals = new ArrayList<>();
        for(int i=0;i<cursorSize;i++){
            cursor.moveToPosition(i);

            ClientReferral clientReferral = convertToClientPersonObject(cursor);
            clientReferrals.add(clientReferral);

        }

        return clientReferrals;
    }



    public static List<FacilityObject> convertToFacilityObjectList(List<CommonPersonObject> commonPersonObjectsList) {
        List<FacilityObject> facilityObjects = new ArrayList<>();
        for (CommonPersonObject common : commonPersonObjectsList) {
            facilityObjects.add(convertToFacilityObject(common));
        }


        return facilityObjects;
    }
    public static List<Indicator> convertToIndicatorList(List<CommonPersonObject> commonPersonObjectsList) {
        List<Indicator> indicatorObjects = new ArrayList<>();
        for (CommonPersonObject common : commonPersonObjectsList) {
            indicatorObjects.add(convertToIndicatorObject(common));
        }


        return indicatorObjects;
    }
    public static List<ReferralServiceObject> convertToServiceObjectList(List<CommonPersonObject> commonPersonObjectsList) {
        List<ReferralServiceObject> facilityObjects = new ArrayList<>();
        for (CommonPersonObject common : commonPersonObjectsList) {
            facilityObjects.add(convertToServiceObject(common));
        }


        return facilityObjects;
    }
    public static String convertStandardJSONString(String data_json) {

        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("\\", "");
        if(data_json.contains("indicator_ids")){
            int start = data_json.indexOf("[");
            int end = data_json.indexOf("]");
            String indicator = data_json.substring(start,end);
            String indicator1 = data_json.substring(start,end);
            Log.d(TAG,"indicator b4 "+indicator);
            indicator1 = indicator1.replace('\"','*');
            indicator1 = indicator1.replace("*","(+");
            String indicator2 = indicator1.replace('+','"');
            indicator2 = indicator2.replace('(','\\');
            Log.d(TAG,"indicator after "+indicator2);
            data_json = data_json.replace(indicator,indicator2);
        }
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
            url = new URL("http://45.56.90.103:8080/api/v2/flow_starts.json");
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
