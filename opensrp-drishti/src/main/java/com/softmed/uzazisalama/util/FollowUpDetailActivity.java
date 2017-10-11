package com.softmed.uzazisalama.util;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.FollowUpReport;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import org.ei.opensrp.drishti.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FollowUpDetailActivity extends AppCompatActivity {

    private TextView textToolbarTitle, textMotherName, textMotherId, textFacility, textDate;

    private PregnantMom pregnantMom;
    private FollowUpReport followUpReport;
    private Gson gson = new Gson();

    private static final String TAG = FollowUpDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        ((CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar)).setTitle("");

        getIntentData();
        findViews();
        setValues();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getIntentData() {
        // follow up report
        String gsonFollowUp = getIntent().getStringExtra("followUp");
        followUpReport = gsonFollowUp != null ? gson.fromJson(gsonFollowUp, FollowUpReport.class) : new FollowUpReport();
        // mother
        String gsonMom = getIntent().getStringExtra("mom");
        pregnantMom = gsonMom != null ? gson.fromJson(gsonMom, PregnantMom.class) : new PregnantMom();
    }


    private void findViews() {
        textToolbarTitle = (TextView) findViewById(R.id.textToolbarTitle);
        textMotherName = (TextView) findViewById(R.id.textMotherName);
        textMotherId = (TextView) findViewById(R.id.textMotherId);
        textDate = (TextView) findViewById(R.id.textDate);
        textFacility = (TextView) findViewById(R.id.textFacility);
    }


    private void setValues() {
        // todo set values to views from intent data

        if (followUpReport.getFollowUpNumber() != 0)
            textToolbarTitle.append(" " + followUpReport.getFollowUpNumber());
        else
            textToolbarTitle.setText("Mahurio Ya Mapema Kwa Mama Aliye Hatarini");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        textDate.setText(dateFormat.format(followUpReport.getDate()));
        textMotherName.setText(pregnantMom.getName());
        textMotherId.setText(pregnantMom.getId());
        textFacility.setText(followUpReport.getFacilityName());

    }
}
