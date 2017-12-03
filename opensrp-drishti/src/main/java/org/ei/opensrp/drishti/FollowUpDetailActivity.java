package org.ei.opensrp.drishti;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import org.ei.opensrp.drishti.DataModels.FollowUpReport;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.DataModels.RiskIndicator;
import org.ei.opensrp.drishti.pageradapter.IndicatorsAdapter;

import org.ei.opensrp.drishti.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.indicatorsRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FollowUpDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        IndicatorsAdapter indicatorAdapter = new IndicatorsAdapter(getRiskIndicators(), FollowUpDetailActivity.this);
        recyclerView.setAdapter(indicatorAdapter);
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


    private List<RiskIndicator> getRiskIndicators() {
        List<RiskIndicator> indicators = new ArrayList<>();
        RiskIndicator risk = new RiskIndicator();

        if (followUpReport.isAlbumin()) {
            risk.setDetected(true);
            risk.setTitle("Albumin katika mkojo.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Hakuna albumin katika mkojo.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isChildDealth()) {
            risk.setDetected(true);
            risk.setTitle("Kufariki mtoto akiwa tumboni");
        } else {
            risk.setDetected(false);
            risk.setTitle("Mtoto mzima akiwa tumboni.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isOver40WeeksPregnancy()) {
            risk.setDetected(true);
            risk.setTitle("Umri wa mimba wiki 40 au zaidi.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Umri wa mimba chini ya wiki 40.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isHighBloodPressure()) {
            risk.setDetected(true);
            risk.setTitle("Presha ya damu zaidi ya 140/90.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Presha ya damu chini ya 140/90.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isHighSugar()) {
            risk.setDetected(true);
            risk.setTitle("Sukari katika mkojo.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Hakuna sukari katika mkojo.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isBadChildPosition()) {
            risk.setDetected(true);
            risk.setTitle("Mlalo mbaya wa mtoto tumboni.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Mlalo sahihi wa mtoto tumboni.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isHbBelow60()) {
            risk.setDetected(true);
            risk.setTitle("HB chini ya 60%.");
        } else {
            risk.setDetected(false);
            risk.setTitle("HB juu ya 60%.");
        }
        indicators.add(risk);
        risk = new RiskIndicator();


        if (followUpReport.isUnproportionalPregnancyHeight()) {
            risk.setDetected(true);
            risk.setTitle("Kimo cha mimba kisicho sahihi.");
        } else {
            risk.setDetected(false);
            risk.setTitle("Kimo sahihi cha mimba.");
        }
        indicators.add(risk);


        Log.d(TAG, "indicators " + indicators.size());
        return indicators;
    }
}
