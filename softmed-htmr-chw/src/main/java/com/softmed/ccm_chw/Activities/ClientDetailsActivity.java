package com.softmed.ccm_chw.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.ccm_chw.R;

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
import java.util.List;
import java.util.Locale;

public class ClientDetailsActivity extends SecuredNativeSmartRegisterActivity {
    public static final int SUCCESSFULLY_REFERED_A_CLIENT = 192;
    private static final String TAG = ClientDetailsActivity.class.getSimpleName();
    private ReferralRepository referralRepository;
    private CommonRepository commonRepository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Client client;
    private Typeface robotoBold, sansBold;
    private MaterialEditText gender, regDob, village, villageLeader, cbhs, ctcNumber, phone, helperName, helperPhoneNumber;
    private TextView clientName;
    private final int REQUEST_PERMISSION_PHONE_STATE=1;

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
        String type = bundle.getString("type");

        if (type.equals("REFERRAL")) {
            cbhs.setVisibility(View.INVISIBLE);
        }

        clientName.setText(client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());

        String dobDateString = dateFormat.format(client.getDate_of_birth());

        gender.setText(client.getGender());
        village.setText(client.getVillage());
        villageLeader.setText(client.getVeo());
        cbhs.setText(client.getCommunity_based_hiv_service());
        ctcNumber.setText(client.getCtc_number());
        phone.setText(client.getPhone_number());

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!client.getPhone_number().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + client.getPhone_number()));
                    if (ActivityCompat.checkSelfPermission(ClientDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        showPhonePermission();
                        return;
                    }
                    startActivity(intent);
                }
            }
        });

        regDob.setText(dobDateString);
        helperName.setText(client.getCare_taker_name());
        helperPhoneNumber.setText(client.getCare_taker_phone_number());
        helperPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!client.getCare_taker_phone_number().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + client.getCare_taker_phone_number()));
                    if (ActivityCompat.checkSelfPermission(ClientDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        showPhonePermission();
                        return;
                    }
                    startActivity(intent);
                }
            }
        });

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
            View v = null;
            if (referral.getReferral_type() == 1) {
                v = getLayoutInflater().inflate(R.layout.view_referral_history, null);
                MaterialEditText appointmentDate = (MaterialEditText) v.findViewById(R.id.appointment_date);
                TextView serviceLabel = (TextView) v.findViewById(R.id.service_label);
                TextView serviceBackground = (TextView) v.findViewById(R.id.service_background);
                serviceLabel.setTypeface(sansBold);
                serviceBackground.setTypeface(sansBold);

                LinearLayout indicatorsLayout = (LinearLayout) v.findViewById(R.id.indicators);
                try {
                    setIndicators(indicatorsLayout, referral.getIndicator_ids());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    serviceLabel.setText(getReferralServiceName(referral.getReferral_service_id()));
                    serviceBackground.setText(getReferralServiceName(referral.getReferral_service_id()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                appointmentDate.setText(dateFormat.format(referral.getAppointment_date()));
                serviceLabel.setTypeface(robotoBold);

            } else {
                v = getLayoutInflater().inflate(R.layout.view_followup_referral_history, null);
                TextView otherInfo = (TextView) v.findViewById(R.id.other_info);
                otherInfo.setText(referral.getServices_given_to_patient());
                ((TextView) v.findViewById(R.id.service_label)).setTypeface(sansBold);
            }

            TextView referralDate = (TextView) v.findViewById(R.id.referral_date);
            TextView facility = (TextView) v.findViewById(R.id.facility_name);
            TextView reasonForReferral = (TextView) v.findViewById(R.id.reason_for_referral);

            ((TextView) v.findViewById(R.id.facility_titleview)).setTypeface(sansBold);
            ((TextView) v.findViewById(R.id.clinical_information_title)).setTypeface(sansBold);
            ((TextView) v.findViewById(R.id.flags_title)).setTypeface(sansBold);

            referralDate.setText(dateFormat.format(referral.getReferral_date()));
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
        sansBold = Typeface.createFromAsset(getAssets(), "google_sans_bold.ttf");
        ((TextView) findViewById(R.id.client_name)).setTypeface(sansBold);
        ((TextView) findViewById(R.id.initial_information_title)).setTypeface(sansBold);
        ((TextView) findViewById(R.id.referral_details_heading)).setTypeface(sansBold);

    }

    private void showPhonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            showExplanation("Permission Needed", "Rationale", Manifest.permission.CALL_PHONE, REQUEST_PERMISSION_PHONE_STATE);
        } else {
            requestPermission(Manifest.permission.CALL_PHONE, REQUEST_PERMISSION_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ClientDetailsActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClientDetailsActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
