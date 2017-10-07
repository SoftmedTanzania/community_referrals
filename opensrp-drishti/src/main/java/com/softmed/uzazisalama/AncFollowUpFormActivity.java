package com.softmed.uzazisalama;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import com.softmed.uzazisalama.DataModels.FollowUpReport;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.util.DatesHelper;

import org.ei.opensrp.drishti.R;

public class AncFollowUpFormActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AncFollowUpFormActivity.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CheckBox checkBoxPressure, checkboxHb, chechboxAlbumini, checkboxSugar, checkboxUmriWaMimba,
            checkboxChildDeath, chechkboxMlaloWaMtoto, checkboxKimo;
    private String pressure, hb, albumini, sugar, umriWaMimba, childDeath, mlaloWaMtoto, kimo;
    private String formName;
    private EditText editTextFacilityName;

    private PregnantMom pregnantMom;
    private Gson gson = new Gson();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anc_follow_up_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Mahudhurio Ya Marudio");


        String gsonMom = getIntent().getStringExtra("mom");
        Log.d(TAG, "mom=" + gsonMom);

        pregnantMom = gsonMom != null ? gson.fromJson(gsonMom, PregnantMom.class) : null;

        findViews();
        setListeners();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews() {
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

        findViewById(R.id.fabSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            pressure = "true";
//                        } else {
//                            pressure = "false";
//                        }
//
//                    }
//                });
//
//                chechboxAlbumini.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (chechboxAlbumini.isChecked()) {
//                            albumini = "true";
//                        } else {
//                            albumini = "false";
//                        }
//                    }
//                });
//
//                checkboxHb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                        if (isChecked) {
//                            hb = "true";
//                        } else {
//                            hb = "false";
//                        }
//                    }
//                });
//                chechkboxMlaloWaMtoto.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (chechkboxMlaloWaMtoto.isChecked()) {
//                            mlaloWaMtoto = "true";
//                        } else {
//                            mlaloWaMtoto = "false";
//                        }
//                    }
//                });
//                checkboxChildDeath.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (checkboxChildDeath.isChecked()) {
//                            childDeath = "true";
//                        } else {
//                            childDeath = "false";
//                        }
//                    }
//                });
//                checkboxKimo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            kimo = "true";
//                        } else {
//                            kimo = "false";
//                        }
//                    }
//                });
//                checkboxSugar.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (checkboxSugar.isChecked()) {
//                            sugar = "true";
//                        } else {
//                            sugar = "false";
//                        }
//                    }
//                });
//                checkboxUmriWaMimba.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (checkboxUmriWaMimba.isEnabled()) {
//                            umriWaMimba = "true";
//                        } else {
//                            umriWaMimba = "false";
//                        }
//                    }
//                });
//
//                HashMap<String, String> followHash = new HashMap<String, String>();
//                followHash.put("facility_name", editTextFacilityName.toString());
//                followHash.put("pressure", pressure);
//                followHash.put("hb", hb);
//                followHash.put("albumin", albumini);
//                followHash.put("sugar", sugar);
//                followHash.put("mlaloWaMtoto", mlaloWaMtoto);
//                followHash.put("childDeath", childDeath);
//                followHash.put("umriWaMimba", umriWaMimba);
//                followHash.put("kimo", kimo);
//
//
//                String trial_one = followHash.get(pressure);
//                String trial_two = followHash.get(hb);
//                String trial_three = followHash.get(umriWaMimba);
//                Log.d(TAG, "pressure = " + trial_one);
//                Log.d(TAG, "pressure_1 = " + trial_two);
//                Log.d(TAG, "pressure_2 = " + trial_three);


                // TODO: 10/2/17 submit follow up report
                FollowUpReport report = getFollowUpReport();
            }
        });
    }


    private FollowUpReport getFollowUpReport() {
        FollowUpReport report = new FollowUpReport();
        long today = System.currentTimeMillis();
        report.setDate(today);
        report.setMotherId(pregnantMom.getId());
        report.setFacilityName(editTextFacilityName.getText().toString());

        report.setAlbumin(chechboxAlbumini.isChecked());
        report.setChildDealth(checkboxChildDeath.isChecked());
        report.setOver40WeeksPregnancy(checkboxUmriWaMimba.isChecked());
        report.setHighBloodPressure(checkBoxPressure.isChecked());
        report.setBadChildPosition(chechkboxMlaloWaMtoto.isChecked());
        report.setHighSugar(checkboxSugar.isChecked());
        report.setHbBelow60(checkboxHb.isChecked());
        report.setUnproportionalPregnancyHeight(checkboxKimo.isChecked());

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
        return report;
    }


}
