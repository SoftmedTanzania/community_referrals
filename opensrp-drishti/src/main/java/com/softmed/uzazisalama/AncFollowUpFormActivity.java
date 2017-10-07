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


    private static final String TAG = AncFollowUpFormActivity.class.getSimpleName();


    private CheckBox checkBoxPressure, checkboxHb, chechboxAlbumini, checkboxSugar, checkboxUmriWaMimba,
            checkboxChildDeath, chechkboxMlaloWaMtoto, checkboxKimo;

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
