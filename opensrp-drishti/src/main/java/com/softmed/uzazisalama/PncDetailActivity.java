package com.softmed.uzazisalama;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ei.opensrp.drishti.R;

public class PncDetailActivity extends AppCompatActivity {

    private TextView textName, textId, textAge, textGravida, textPara,
            textDeliveryMethod, textProblemDuringDelivery, textMotherHealth,
            textBabyGender, textBabyWeight, textApgarScore, textBabyProblems, textBabyHealth;
    private LinearLayout layoutBBA;
    private CardView cardBabyDetails;

    private static final String TAG = PncDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnc_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle("");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        if (appBarLayout.getVisibility() != View.VISIBLE)
            appBarLayout.setVisibility(View.VISIBLE);


        textName = (TextView) findViewById(R.id.textName);
        textAge = (TextView) findViewById(R.id.textAge);
        textId = (TextView) findViewById(R.id.textId);
        textDeliveryMethod = (TextView) findViewById(R.id.textDeliveryMethod);
        textPara = (TextView) findViewById(R.id.textPara);
        textGravida = (TextView) findViewById(R.id.textGravida);
        textProblemDuringDelivery = (TextView) findViewById(R.id.textProblemDuringDelivery);
        textMotherHealth = (TextView) findViewById(R.id.textMotherHealthOnPermission);
        textBabyGender = (TextView) findViewById(R.id.textBabyGender);
        textBabyWeight = (TextView) findViewById(R.id.textBabyWeight);
        textApgarScore = (TextView) findViewById(R.id.textApgarScore);
        textBabyProblems = (TextView) findViewById(R.id.textBabyProblems);
        textBabyHealth = (TextView) findViewById(R.id.textBabyHealthOnPermission);
        cardBabyDetails = (CardView) findViewById(R.id.cardBabyDetails);
        layoutBBA = (LinearLayout) findViewById(R.id.layoutBBA);


        setDetails();

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


    private void setDetails() {
        // todo get from intent and set mother & baby details

        boolean isBBA = false; // check from mother details if mimba imehibika or not
        if (isBBA) {
            cardBabyDetails.setVisibility(View.INVISIBLE);
            layoutBBA.setVisibility(View.VISIBLE);
        } else {
            cardBabyDetails.setVisibility(View.VISIBLE);
            layoutBBA.setVisibility(View.GONE);
        }
    }


}
