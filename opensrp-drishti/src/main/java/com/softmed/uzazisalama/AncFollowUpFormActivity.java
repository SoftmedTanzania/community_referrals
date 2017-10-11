package com.softmed.uzazisalama;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import com.softmed.uzazisalama.Application.UzaziSalamaApplication;
import com.softmed.uzazisalama.DataModels.FollowUpReport;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Repository.CustomFollowUpRepository;
import com.softmed.uzazisalama.Repository.FollowUpReportObject;
import com.softmed.uzazisalama.util.DatesHelper;
import com.softmed.uzazisalama.util.Utils;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.softmed.uzazisalama.util.Utils.generateRandomUUIDString;

public class AncFollowUpFormActivity extends SecuredNativeSmartRegisterActivity {
    private static final String TAG = AncFollowUpFormActivity.class.getSimpleName();

    static String id;
    private CheckBox checkBoxPressure, checkboxHb, chechboxAlbumini, checkboxSugar, checkboxUmriWaMimba,
            checkboxChildDeath, chechkboxMlaloWaMtoto, checkboxKimo;
    private String formName = "anc_follow_up_report";
    private EditText editTextFacilityName;

    private static PregnantMom pregnantMom;
    private Gson gson = new Gson();


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anc_follow_up_form);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
//
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("Mahudhurio Ya Marudio");


        String gsonMom = getIntent().getStringExtra("mom");
        id = getIntent().getStringExtra("id");
        Log.d(TAG, "mom=" + gsonMom);
        Log.d(TAG, "id=" + id);

        pregnantMom = gsonMom != null ? gson.fromJson(gsonMom, PregnantMom.class) : null;
        Log.d(TAG, "preganantMom  =" + gsonMom);


        findViews();
        setListeners();


    }


    private void findViews() {
        Log.d(TAG, "am in findview");
        editTextFacilityName = (EditText) findViewById(R.id.facility);
        checkBoxPressure = (CheckBox) findViewById(R.id.checkbox_pressure);
        chechboxAlbumini = (CheckBox) findViewById(R.id.checkbox_albumin);
        checkboxHb = (CheckBox) findViewById(R.id.checkbox_hb_below_60);
        chechkboxMlaloWaMtoto = (CheckBox) findViewById(R.id.checkbox_mlalo_wa_mtotos);
        checkboxChildDeath = (CheckBox) findViewById(R.id.checkbox_baby_death);
        checkboxKimo = (CheckBox) findViewById(R.id.checkbox_kimo);
        checkboxSugar = (CheckBox) findViewById(R.id.checkbox_sugar_level);
        checkboxUmriWaMimba = (CheckBox) findViewById(R.id.checkbox_umri_wa_mimba);
    }

    private void setListeners() {
        Log.d(TAG, "am in setListerners");
        findViewById(R.id.fabSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 10/2/17 get id and fieldOverrides for follow up report submission
                saveFormSubmission(getFollowUpReport(), UUID.randomUUID().toString(), id, null);
                onBackPressed();

            }
        });


        // home as up button
        findViewById(R.id.buttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "am in setListerners2");

                onBackPressed();
            }
        });
    }


    private String getFollowUpReport() {
        FollowUpReport report = new FollowUpReport();
        long today = System.currentTimeMillis();
        report.setDate(today);
        report.setMotherId(id);
        report.setFacilityName(editTextFacilityName.getText().toString());

        report.setAlbumin(chechboxAlbumini.isChecked());
        report.setChildDealth(checkboxChildDeath.isChecked());
        report.setOver40WeeksPregnancy(checkboxUmriWaMimba.isChecked());
        report.setHighBloodPressure(checkBoxPressure.isChecked());
        report.setBadChildPosition(chechkboxMlaloWaMtoto.isChecked());
        report.setHighSugar(checkboxSugar.isChecked());
        report.setHbBelow60(checkboxHb.isChecked());
        report.setUnproportionalPregnancyHeight(checkboxKimo.isChecked());
        report.setCreatedBy(((UzaziSalamaApplication) getApplication()).getCurrentUserID());
        report.setModifyBy(((UzaziSalamaApplication) getApplication()).getCurrentUserID());

        // automate follow up number
        long lnmp = pregnantMom.getDateLNMP();
        long firstVisit = DatesHelper.calculate1stVisitFromLNMP(lnmp);
        long secondVisit = DatesHelper.calculate2ndVisitFromLNMP(lnmp);
        long thirdVisit = DatesHelper.calculate3rdVisitFromLNMP(lnmp);
        long fourthVisit = DatesHelper.calculate4thVisitFromLNMP(lnmp);
        long earlyVisit = 0;
        if (pregnantMom.isOnRisk())
            earlyVisit = DatesHelper.calculateEarlyVisitFromLNMP(lnmp);

        if (today > fourthVisit)
            // 4th follow up
            report.setFollowUpNumber(4);

        else if (today > thirdVisit)
            // 3rd follow up
            report.setFollowUpNumber(3);

        else if (today > secondVisit)
            // 2nd visit
            report.setFollowUpNumber(2);

        else if (today > firstVisit)
            // 1st visit
            report.setFollowUpNumber(1);

        else if ((int) earlyVisit != 0 && today > earlyVisit)
            // early visit for mother on risk
            report.setFollowUpNumber(0);


        // log report object
        Log.d(TAG, "report=" + gson.toJson(report));
        return gson.toJson(report);
    }


    @Override
    public void saveFormSubmission(String formSubmission, String id, String relId, JSONObject fieldOverrides) {
        // TODO: 10/7/17 complete this implementation to save report to database
        Log.d(TAG, "am in save");
        Log.d(TAG, "formsubmission =" + formSubmission);
        final FollowUpReport followUpReport = gson.fromJson(formSubmission, FollowUpReport.class);

        FollowUpReportObject followUpReportObject = new FollowUpReportObject(id, relId, followUpReport);
        ContentValues values = new CustomFollowUpRepository().createValuesFor(followUpReportObject);
        Log.d(TAG, "followUpReportObject = " + gson.toJson(followUpReportObject));
        Log.d(TAG, "values = " + gson.toJson(values));

        CommonRepository commonRepository = context().commonrepository("uzazi_salama_follow_up_report");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();


        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


        formFields.add(new FormField("relationalid", c.getRelationalId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        for (String key : c.getDetails().keySet()) {
            Log.d(TAG, "key = " + key);
            FormField f = null;
            f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);

            formFields.add(f);
        }

        for (String key : c.getColumnmaps().keySet()) {
            Log.d(TAG, "key = " + key);
            FormField f = null;
            f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);


            formFields.add(f);
        }
        Log.d(TAG, "form field = " + gson.toJson(formFields));

// // TODO: 08/10/2017 coze finishing up saving in the form submission
        FormData formData = new FormData("uzazi_salama_follow_up_report", "/model/instance/Wazazi_Salama_ANC_Followup/", formFields, null);
        FormInstance formInstance = new FormInstance(formData, "1");
        FormSubmission submission = new FormSubmission(generateRandomUUIDString(), id, "wazazi_salama_pregnant_mothers_follow_up", gson.toJson(formInstance), "4", SyncStatus.PENDING, "4");
        context().formDataRepository().saveFormSubmission(submission);

        Log.d(TAG, "submission content = " + gson.toJson(submission));


//        TODO finish this better implementation for saving data to the database
//        FormSubmission formSubmission1 = null;
//        try {
//            formSubmission1 = FormUtils.getInstance(getApplicationContext()).generateFormSubmisionFromJSONString(id,new Gson().toJson(pregnantMom),"wazazi_salama_pregnant_mothers_registration",fieldOverrides);
//            Log.d(TAG,"form submission generated successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            context().ziggyService().saveForm(getParams(formSubmission1), formSubmission1.instance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        new com.softmed.uzazisalama.util.AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!pregnantMom.getPhone().equals(""))
                    Utils.sendRegistrationAlert(pregnantMom.getPhone());
                return null;
            }
        }.execute();


    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void onResumption() {

    }
}
