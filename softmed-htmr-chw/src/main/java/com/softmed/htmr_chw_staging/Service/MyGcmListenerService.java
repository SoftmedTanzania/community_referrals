package com.softmed.htmr_chw_staging.Service;

/**
 * Created by kency on 12/11/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.softmed.htmr_chw_staging.Application.BoreshaAfyaApplication;

import org.ei.opensrp.domain.Client;
import org.ei.opensrp.domain.Referral;
import org.ei.opensrp.view.activity.LoginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public MyGcmListenerService() {
    }


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = "";
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "data bundle: " + data.toString());
        Log.d(TAG, "data size: " + data.size());

        org.ei.opensrp.Context context = org.ei.opensrp.Context.getInstance().updateApplicationContext(this.getApplicationContext());

        Log.d(TAG, "username = " + context.allSharedPreferences().fetchRegisteredANM());
        Log.d(TAG, "password = " + context.allSharedPreferences().fetchRegisteredANMPassword());

        context.userService().isValidLocalLogin(context.allSharedPreferences().fetchRegisteredANM(), context.allSettings().fetchANMPassword());

        String type = data.getString("type");
        if (type.equals("PatientReferral")) {
            message = "New Followup Client Referral Received";
            Log.d(TAG, "patientsDTO = " + data.getString("patientsDTO"));
            String clientId = "";

            Client client = new Client();

            try {
                JSONObject object = new JSONObject(data.getString("patientsDTO"));

                clientId = object.getString("patientId");
                client.setClient_id(clientId);
                client.setFirst_name(object.getString("firstName"));
                client.setMiddle_name(object.getString("middleName"));
                client.setSurname(object.getString("surname"));
                client.setGender(object.getString("gender"));
                client.setPhone_number(object.getString("phoneNumber"));

                try {
                    client.setVillage(object.getString("village"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    client.setWard(object.getString("ward"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    client.setCare_taker_name(object.getString("careTakerName"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    client.setCare_taker_phone_number(object.getString("careTakerPhoneNumber"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    client.setCtc_number(object.getString("ctcNumber"));
                } catch (Exception e) {
                    e.printStackTrace();
                    client.setCtc_number("");
                }

                try {
                    client.setDate_of_birth(Long.valueOf(object.getString("dateOfBirth")));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    client.setCommunity_based_hiv_service(object.getString("communityBasedHivService"));
                } catch (Exception e) {
                    e.printStackTrace();
                    client.setCommunity_based_hiv_service("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            List<Referral> referralList = new ArrayList<>();
            JSONObject referralObject = null;
            try {
                referralObject = new JSONArray(data.getString("patientReferralsList")).getJSONObject(0);
                Referral referral = new Referral();

                referral.setId(referralObject.getString("referralId"));
                referral.setFacility_id(referralObject.getString("fromFacilityId"));
                referral.setReferral_status(referralObject.getString("referralStatus"));
                referral.setReferral_reason(referralObject.getString("referralReason"));
                referral.setOther_notes(referralObject.getString("otherNotes"));
                referral.setReferral_uuid(referralObject.getString("referralUUID"));
                referral.setService_provider_uiid(referralObject.getString("serviceProviderUIID"));
                referral.setReferral_date(Long.valueOf(referralObject.getString("referralDate")));
                referral.setReferral_type(4);
                referral.setClient_id(clientId);

                referralList.add(referral);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ((BoreshaAfyaApplication) getApplication()).insertFollowup(client, referralList);

        } else if (type.equals("ReferralFeedback") || type.equals("FailedReferrals")) {
            message = "successful referral feedback";
            String client_id = data.getString("referralUUID");
            String feedback = data.getString("otherNotes");
            String feedbackId = data.getString("serviceGivenToPatient");
            String testResult = data.getString("testResults");
            String referralStatus = data.getString("referralStatus");

            ((BoreshaAfyaApplication) getApplication()).updateReferralStatus(client_id, feedback, feedbackId, testResult, referralStatus);


        } else if (type.equals("UpdateClientId")) {

            JSONObject object = null;
            try {
                object = new JSONObject(data.getString("map"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            message = "";
            String client_id = null;
            String temp_id = null;
            try {
                client_id = object.getString("GENERATED_CLIENT_ID");
                temp_id = object.getString("TEMP_CLIENT_ID");

                Log.d(TAG,"ClientId = "+client_id);
                Log.d(TAG,"Temp Id = "+temp_id);
                ((BoreshaAfyaApplication) getApplication()).updateClientId(client_id, temp_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        sendNotification(message);
    }

    /**
     * Method called on Receiving a new message from GCM server
     */
    @Override
    public void onMessageSent(String s) {


    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    public void onDeletedMessages() {


        Log.i(TAG, "Received deleted messages notification");
//        String message = getString(R.string.gcm_deleted, total);
//        aController.displayMessageOnScreen(context, message);
//        // notifies user
//        sendNotification( message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onSendError(String s1, String errorId) {

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(org.ei.opensrp.R.drawable.ic_htmr)
                .setContentTitle("TRCMIS Notification")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}