package com.softmed.htmr_chw.Service;

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
import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;

import org.ei.opensrp.domain.ClientFollowup;
import org.ei.opensrp.view.activity.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

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
        String message = "successful referral feedback";
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "data bundle: " + data.toString());
        Log.d(TAG, "data size: " + data.size());

        org.ei.opensrp.Context context = org.ei.opensrp.Context.getInstance().updateApplicationContext(this.getApplicationContext());
        context.userService().isValidLocalLogin(context.allSharedPreferences().fetchRegisteredANM(), context.allSettings().fetchANMPassword());

        String type= data.getString("type");
        if(type.equals("PatientReferral")){
            Log.d(TAG,"patientsDTO = "+data.getString("patientsDTO"));

            try {
                JSONObject object = new JSONObject(data.getString("patientsDTO"));

                ClientFollowup clientFollowup = new ClientFollowup();
                clientFollowup.setId(object.getString("patientId"));
                clientFollowup.setFirst_name(object.getString("firstName"));
                clientFollowup.setMiddle_name( object.getString("middle_name"));
                clientFollowup.setSurname( object.getString("surname"));
                clientFollowup.setGender( object.getString("gender"));
                clientFollowup.setPhone_number( object.getString("phone_number"));
                clientFollowup.setCommunity_based_hiv_service( object.getString("community_based_hiv_service"));
                clientFollowup.setMap_cue( object.getString("map_cue"));
                clientFollowup.setWard( object.getString("ward"));
                clientFollowup.setReferral_reason( object.getString("referral_reason"));
                clientFollowup.setCare_taker_name( object.getString("care_taker_name"));
                clientFollowup.setCare_taker_name_phone_number( object.getString("care_taker_name"));
                clientFollowup.setCare_taker_relationship( object.getString("care_taker_relationship"));
                clientFollowup.setCtc_number(  object.getString("ctc_number"));
                clientFollowup.setFacility_id(  object.getString("facility_id"));
                clientFollowup.setReferral_status( object.getString("referral_status"));
                clientFollowup.setService_provider_uiid( object.getString("service_provider_uiid"));

                clientFollowup.setReferral_date(Long.valueOf(object.getString("referral_date")));
                clientFollowup.setVisit_date(Long.valueOf(object.getString("visit_date")));
                clientFollowup.setDate_of_birth(Long.valueOf(object.getString("date_of_birth")));


                ((BoreshaAfyaApplication)getApplication()).insertFollowup(clientFollowup);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(type.equals("ReferralFeedback") || type.equals("FailedReferrals")){
            String client_id= data.getString("referralUUID");
            String feedback= data.getString("otherNotes");
            String serviceGivenToPatient= data.getString("serviceGivenToPatient");
            boolean testResult= Boolean.parseBoolean(data.getString("testResults"));
            String referralStatus= data.getString("referralStatus");

            ((BoreshaAfyaApplication)getApplication()).updateReferralStatus(client_id,feedback,serviceGivenToPatient,testResult,referralStatus);


        }

        sendNotification(message);
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    public void onMessageSent(String s) {


    }

    /**
     * Method called on receiving a deleted message
     * */
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
     * */
    @Override
    public void onSendError(String s1, String errorId) {


    }
    public void handleIntent(Intent intent) { /* compiled code */ }


    // [END receive_message]

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