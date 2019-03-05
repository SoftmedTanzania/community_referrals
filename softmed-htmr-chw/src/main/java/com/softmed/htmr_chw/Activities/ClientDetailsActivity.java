package com.softmed.htmr_chw.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
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
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.domain.Referral;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.IndicatorRepository;
import org.ei.opensrp.repository.ReferralRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientDetailsActivity extends SecuredNativeSmartRegisterActivity {
    public static final int SUCCESSFULLY_REFERED_A_CLIENT = 192;
    private static final String TAG = ClientDetailsActivity.class.getSimpleName();
    private ReferralRepository referralRepository;
    private CommonRepository commonRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Client client;
    private Typeface robotoBold;
    private MaterialEditText gender, regDob, village, villageLeader, cbhs, ctcNumber, phone, helperName, helperPhoneNumber;
    private TextView clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        setupviews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        client = (Client) bundle.getSerializable("client");
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
                Intent intent = new Intent(ClientDetailsActivity.this, ReferralRegistrationFormActivity.class);
                intent.putExtra("clientName", client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());
                intent.putExtra("clientId", client.getClient_id());
                startActivityForResult(intent, SUCCESSFULLY_REFERED_A_CLIENT);
            }
        });

        referralRepository = context().referralRepository();

        Log.d(TAG, "Client ID = " + client.getClient_id());
        setReferralHistory();

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

    public void setReferralHistory() {
        List<Referral> referrals = referralRepository.findByClientId(client.getClient_id());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.referral_history);
        linearLayout.removeAllViews();

        for (Referral referral : referrals) {
            View v = getLayoutInflater().inflate(R.layout.view_referral_history, null);
            TextView referralDate = (TextView) v.findViewById(R.id.referral_date);
            TextView service = (TextView) v.findViewById(R.id.service);
            TextView service_label = (TextView) v.findViewById(R.id.service_label);
            TextView facility = (TextView) v.findViewById(R.id.facility_name);
            TextView reasonForReferral = (TextView) v.findViewById(R.id.reason_for_referral);
            MaterialEditText appointmentDate = (MaterialEditText) v.findViewById(R.id.appointment_date);

            LinearLayout indicatorsLayout = (LinearLayout) v.findViewById(R.id.indicators);
            setIndicators(indicatorsLayout, referral.getIndicator_ids());

            try {
                service_label.setText(getReferralServiceName(referral.getReferral_service_id()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            referralDate.setText(dateFormat.format(referral.getReferral_date()));
            appointmentDate.setText(dateFormat.format(referral.getAppointment_date()));
            service_label.setTypeface(robotoBold);

            reasonForReferral.setText(referral.getReferral_reason());
            facility.setText(getFacilityName(referral.getFacility_id()));
            linearLayout.addView(v);
        }
    }

    public String getFacilityName(String id) {
        Log.d(TAG, "Facility Id = " + id);
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
                View v = getLayoutInflater().inflate(R.layout.indicator_item, null);
                TextView indicatorName = v.findViewById(R.id.indicator_name);
                indicatorName.setText(getIndicatorName(indicatorsArray.getString(m)));
                indicatorName.setPadding(0, 10, 10, 0);
                view.addView(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIndicatorName(String id) {
        try {
            Log.d(TAG, "indicatorId = " + id);
            IndicatorRepository indicatorRepository = context().indicatorRepository();


            List<Indicator> indicators = indicatorRepository.findServiceByCaseIds(id);

            return indicators.get(0).getIndicatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            setReferralHistory();
        }

    }

    private void setupviews() {
        clientName = (TextView) findViewById(R.id.client_name);
        gender = (MaterialEditText) findViewById(R.id.gender);
        regDob = (MaterialEditText) findViewById(R.id.reg_dob);
        village = (MaterialEditText) findViewById(R.id.editTextKijiji);
        villageLeader = (MaterialEditText) findViewById(R.id.editTextVillageLeader);
        cbhs = (MaterialEditText) findViewById(R.id.cbhs);
        ctcNumber = (MaterialEditText) findViewById(R.id.ctc_number);
        phone = (MaterialEditText) findViewById(R.id.edittextPhone);
        helperName = (MaterialEditText) findViewById(R.id.helper_name);
        helperPhoneNumber = (MaterialEditText) findViewById(R.id.helper_phone_number);

        robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        ((TextView) findViewById(R.id.client_name)).setTypeface(robotoBold);

    }
}
