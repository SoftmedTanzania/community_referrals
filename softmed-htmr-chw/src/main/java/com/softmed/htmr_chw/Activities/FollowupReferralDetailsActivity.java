package com.softmed.htmr_chw.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Domain.ClientReferral;
import com.softmed.htmr_chw.Fragments.FollowupClientDetailFragment;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class FollowupReferralDetailsActivity extends SecuredNativeSmartRegisterActivity {
    private static final String TAG = FollowupReferralDetailsActivity.class.getSimpleName();
    private static final String CLIENT_FOLLOWUP = "item_id";
    private ClientReferral clientReferral;
    private CommonRepository commonRepository;
    private Cursor cursor;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private TextView name, age, veo, ward, mapCue, village, refererName, gender, phoneNumber, feedback, otherInformation, referedReason, helperName, referedDate, helperPhoneNumber;
    private MaterialSpinner spinnerReason;
    private Typeface robotoRegular, sansBold;
    private int reasonSelection = -1;
    private Button save;

    public static void setLanguage() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(Context.getInstance().applicationContext()));
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();

        android.util.Log.d(TAG, "set Locale : " + preferredLocale);

        // Change locale settings in the app.
        Resources res = Context.getInstance().applicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.softmed.htmr_chw.R.layout.activity_item_detail);
        setupviews();
        Toolbar toolbar = (Toolbar) findViewById(com.softmed.htmr_chw.R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.softmed.htmr_chw.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(CLIENT_FOLLOWUP,
                    getIntent().getStringExtra(CLIENT_FOLLOWUP));
            FollowupClientDetailFragment fragment = new FollowupClientDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(com.softmed.htmr_chw.R.id.item_detail_container, fragment)
                    .commit();
        }

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        clientReferral = (ClientReferral) bundle.getSerializable("client_followup");

        setDetails(clientReferral);

        setLanguage();
    }

    public String getFacilityName(String id) {

        commonRepository = context().commonrepository("facility");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
//            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void setupviews() {
        TextView heading = (TextView) findViewById(R.id.heading);
        TextView referralDateTitle = (TextView) findViewById(R.id.referral_date_title);
        TextView clientAgeTitle = (TextView) findViewById(R.id.client_age_title);
        TextView veoNameTitle = (TextView) findViewById(R.id.veo_name_title);
        TextView clientWardTitle = (TextView) findViewById(R.id.client_ward_title);
        TextView clientVillageTitle = (TextView) findViewById(R.id.client_village_title);
        TextView refererNameTitle = (TextView) findViewById(R.id.referer_name_title);
        TextView client_kitongoji_title = (TextView) findViewById(R.id.client_kitongoji_title);
        TextView refererTitle = (TextView) findViewById(R.id.referer_title);
        TextView referralReasonTitle = (TextView) findViewById(R.id.sababu_ya_rufaa_title);
        TextView clinicalInformationTitle = (TextView) findViewById(R.id.clinical_information_title);
        TextView feedbackTitle = (TextView) findViewById(R.id.feedback_title);
        TextView helperPhoneNumberTitle = (TextView) findViewById(R.id.helper_phone_number_title);
        TextView helperNameTitle = (TextView) findViewById(R.id.helper_name_title);
        TextView service_offered_title = (TextView) findViewById(R.id.service_offered_title);
        TextView service_advice_title = (TextView) findViewById(R.id.service_advice_title);
        save = (Button) findViewById(R.id.save_button);
        name = (TextView) findViewById(R.id.client_name);
        phoneNumber = (TextView) findViewById(R.id.phone_number);
        village = (TextView) findViewById(R.id.client_village_value);
        age = (TextView) findViewById(R.id.client_age_value);
        veo = (TextView) findViewById(R.id.veo_name_value);
        ward = (TextView) findViewById(R.id.client_ward_value);
        refererName = (TextView) findViewById(R.id.referer_name_value);
        mapCue = (TextView) findViewById(R.id.client_kitongoji_value);
        helperName = (TextView) findViewById(R.id.helper_name_value);
        helperPhoneNumber = (TextView) findViewById(R.id.helper_phone_number_value);
        gender = (TextView) findViewById(R.id.gender);
        otherInformation = (TextView) findViewById(R.id.other_clinical_inforamtion_value);
        referedDate = (TextView) findViewById(R.id.referral_date);
        referedReason = (TextView) findViewById(R.id.followUp_reason);
        feedback = (TextView) findViewById(R.id.client_condition);
        spinnerReason = (MaterialSpinner) findViewById(R.id.spinnerClientAvailable);

        robotoRegular = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(getAssets(), "google_sans_bold.ttf");

        heading.setTypeface(sansBold);
        refererTitle.setTypeface(sansBold);
        helperPhoneNumberTitle.setTypeface(sansBold);
        helperNameTitle.setTypeface(sansBold);
        clientVillageTitle.setTypeface(sansBold);
        feedbackTitle.setTypeface(sansBold);
        referralReasonTitle.setTypeface(sansBold);
        clinicalInformationTitle.setTypeface(sansBold);
        referralDateTitle.setTypeface(sansBold);
        client_kitongoji_title.setTypeface(sansBold);
        refererNameTitle.setTypeface(sansBold);
        clientAgeTitle.setTypeface(sansBold);
        veoNameTitle.setTypeface(sansBold);
        clientWardTitle.setTypeface(sansBold);
        name.setTypeface(sansBold);
        referedDate.setTypeface(sansBold);

        service_offered_title.setTypeface(robotoRegular);
        service_advice_title.setTypeface(robotoRegular);
        age.setTypeface(robotoRegular);
        veo.setTypeface(robotoRegular);
        ward.setTypeface(robotoRegular);
        village.setTypeface(robotoRegular);
        refererName.setTypeface(robotoRegular);
        mapCue.setTypeface(robotoRegular);
        referedReason.setTypeface(robotoRegular);
        otherInformation.setTypeface(robotoRegular);
    }


    private void setDetails(final ClientReferral clientReferral) {

        String reg_date = dateFormat.format(clientReferral.getDate_of_birth());
        Log.d(TAG, "Date of Birth : " + clientReferral.getDate_of_birth());
        String ageS = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date d = dateFormat.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);

            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((clientReferral.getGender()).equalsIgnoreCase(getResources().getString(R.string.female))) {
            gender.setText(getResources().getString(R.string.female));
        } else {
            gender.setText(getResources().getString(R.string.male));
        }
        age.setText(ageS + " years");
        name.setText(clientReferral.getFirst_name() + " " + clientReferral.getMiddle_name() + ", " + clientReferral.getSurname());
        phoneNumber.setText(clientReferral.getPhone_number());
        referedReason.setText(clientReferral.getReferral_reason());
        referedDate.setText(dateFormat.format(clientReferral.getReferral_date()));


        try {
            village.setText(clientReferral.getVillage());
            ward.setText(clientReferral.getWard());
            mapCue.setText(clientReferral.getKijitongoji());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            helperName.setText(clientReferral.getHelper_name());
            helperPhoneNumber.setText(clientReferral.getHelper_phone_number());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            feedback.setText(clientReferral.getReferral_feedback());
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String[] ITEMS = {getString(R.string.followup_feedback_patient_moved), getString(R.string.followup_feedback_patient_died), getString(R.string.followup_feedback_other_reasons)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FollowupReferralDetailsActivity.this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason.setAdapter(adapter);

        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerReason.setFloatingLabelText(getString(R.string.followup_qn_reasons_for_not_visiting_clinic));
                    reasonSelection = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerReason.getSelectedItemPosition() <= 0) {
                    Toast.makeText(FollowupReferralDetailsActivity.this, getString(R.string.toast_message_select_reasons_for_missing_appointment), Toast.LENGTH_SHORT).show();

                } else {
                    clientReferral.setReferral_feedback(ITEMS[reasonSelection] + "/n/n" + feedback.getText().toString());

//                        context().followupClientRepository().update(followup);

                    //TODO finish up sending of referral feedbacks of the followup

//                        final String uuid = generateRandomUUIDString();
//                        context().referralRepository().update(clientReferral);
//                        List<FormField> formFields = new ArrayList<>();
//
//
//                        formFields.add(new FormField("id", followup.getId(), commonRepository.TABLE_NAME + "." + "id"));
//                        formFields.add(new FormField("relationalid", followup.getId(), commonRepository.TABLE_NAME + "." + "relationalid"));
//
//                        for (String key : followup.getDetails().keySet()) {
//                            Log.d(TAG, "key = " + key);
//                            FormField f = null;
//                            if (!key.equals("facility_id")) {
//                                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
//                            } else {
//                                f = new FormField(key, c.getDetails().get(key), "facility.id");
//                            }
//                            formFields.add(f);
//                        }
//
//
//                        Log.d(TAG, "form field = " + new Gson().toJson(formFields));
//
//                        FormData formData = new FormData("follow_up", "/model/instance/Follow_Up_Form/", formFields, null);
//                        FormInstance formInstance = new FormInstance(formData, "1");
//                        FormSubmission submission = new FormSubmission(generateRandomUUIDString(), uuid, "client_referral", new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
//                        context().formDataRepository().saveFormSubmission(submission);
//
//                        Log.d(TAG, "submission content = " + new Gson().toJson(submission));
                    Toast.makeText(FollowupReferralDetailsActivity.this, getString(R.string.followup_thankyou_note_part_one) + clientReferral.getFirst_name() + " " + clientReferral.getSurname(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
