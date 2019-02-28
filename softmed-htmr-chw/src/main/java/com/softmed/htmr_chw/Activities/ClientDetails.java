package com.softmed.htmr_chw.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Client;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientReferralRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientDetails extends SecuredNativeSmartRegisterActivity {
    private static final String TAG = ClientDetails.class.getSimpleName();
    private ClientReferralRepository clientReferralRepository;
    private CommonRepository commonRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        final Client client = (Client) bundle.getSerializable("client");

        TextView clientName = (TextView) findViewById(R.id.client_name);
        MaterialEditText gender = (MaterialEditText) findViewById(R.id.gender);
        MaterialEditText regDob = (MaterialEditText) findViewById(R.id.reg_dob);
        MaterialEditText village = (MaterialEditText) findViewById(R.id.editTextKijiji);
        MaterialEditText villageLeader = (MaterialEditText) findViewById(R.id.editTextVillageLeader);
        MaterialEditText cbhs = (MaterialEditText) findViewById(R.id.cbhs);
        MaterialEditText ctcNumber = (MaterialEditText) findViewById(R.id.ctc_number);
        MaterialEditText phone = (MaterialEditText) findViewById(R.id.edittextPhone);
        MaterialEditText helperName = (MaterialEditText) findViewById(R.id.helper_name);
        MaterialEditText helperPhoneNumber = (MaterialEditText) findViewById(R.id.helper_phone_number);

        Typeface robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");

        ((TextView) findViewById(R.id.client_name)).setTypeface(robotoBold);
        clientName.setText(client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());

        Date date = new Date();
        date.setTime(client.getDate_of_birth());
        gender.setText(client.getGender());
        village.setText(client.getVillage());
        villageLeader.setText(client.getVillage_leader());
        cbhs.setText(client.getCommunity_based_hiv_service());
        ctcNumber.setText(client.getCtc_number());
        phone.setText(client.getPhone_number());
        regDob.setText(date.toString());
        helperName.setText(client.getHelper_name());
        helperPhoneNumber.setText(client.getPhone_number());

        Button fab = (Button) findViewById(R.id.referal_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientDetails.this, ReferralFormRegisterActivity.class);
                intent.putExtra("clientName", client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());
                intent.putExtra("clientId", client.getClient_id());
                startActivityForResult(intent, 90);
            }
        });

        clientReferralRepository = context().clientReferralRepository();

        Log.d(TAG,"Client ID = "+client.getClient_id());

        List<ClientReferral> clientReferrals = clientReferralRepository.findByClientId(client.getClient_id());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.referral_history);

        for (ClientReferral clientReferral : clientReferrals) {
            View v = getLayoutInflater().inflate(R.layout.view_referral_history, null);
            TextView referralDate = (TextView) v.findViewById(R.id.referral_date);
            TextView service = (TextView) v.findViewById(R.id.service);
            TextView facility = (TextView) v.findViewById(R.id.facility_name);
            TextView reasonForReferral = (TextView) v.findViewById(R.id.reason_for_referral);
            MaterialEditText appointmentDate = (MaterialEditText) v.findViewById(R.id.appointment_date);

            LinearLayout indicatorsLayout = (LinearLayout) v.findViewById(R.id.indicators);
            setIndicators(indicatorsLayout,clientReferral.getIndicator_ids());

            try {
                service.setText(getReferralServiceName(clientReferral.getReferral_service_id()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            referralDate.setText(dateFormat.format(clientReferral.getReferral_date()));
            appointmentDate.setText(dateFormat.format(clientReferral.getAppointment_date()));

            reasonForReferral.setText(clientReferral.getReferral_reason());
            facility.setText(getFacilityName(clientReferral.getFacility_id()));
            linearLayout.addView(v);
        }


    }

    @Override
    protected DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    @Override
    public void startRegistration() {

    }

    public String getFacilityName(String id) {
        commonRepository = context().commonrepository("facility");
        Cursor cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='" + id + "'");
        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    public String getReferralServiceName(String id) {
        commonRepository = context().commonrepository("referral_service");
        Cursor cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where id ='" + id + "'");
        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    public void setIndicators(LinearLayout view, String object) {
        try {
            JSONArray indicatorsArray = new JSONArray(object);
            view.removeAllViewsInLayout();

            for (int m = 0; m < indicatorsArray.length(); m++) {
                final TextView rowTextView = new TextView(this);

                rowTextView.setText(" - "+getIndicatorName(indicatorsArray.getString(m)));
                rowTextView.setPadding(0, 10, 10, 0);
                view.addView(rowTextView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIndicatorName(String id) {
        try {
            Log.d(TAG, "indicatorId = " + id);
            commonRepository = context().commonrepository("indicator");
            Cursor cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where referralServiceIndicatorId ='" + id + "'");
            List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
            return commonPersonObjectList.get(0).getColumnmaps().get("indicatorName");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
