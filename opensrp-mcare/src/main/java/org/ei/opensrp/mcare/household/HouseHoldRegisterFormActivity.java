package org.ei.opensrp.mcare.household;

import android.support.annotation.IdRes;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ei.opensrp.mcare.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HouseHoldRegisterFormActivity extends ActionBarActivity {


    EditText editTextHeadName, editTextGoBHHID,
            editTextNumberPeople, editTextWomanName, editTextWomanDOB;

    RadioGroup radioGroupGenderHeadHH,  radioGroupLiveWithHusband;

    LinearLayout layoutWomanRegistration,  layoutHusbandAlive, layoutLiveWithHusband;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private static final String TAG = HouseHoldRegisterFormActivity.class.getSimpleName();
    private long today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_hold_register_form);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        today = System.currentTimeMillis();
        // find views
        findViews();

        // set radio listeners
        setRadioListeners();
    }


    private void findViews() {
        editTextGoBHHID = (EditText) findViewById(R.id.editTextGoBHHID);
//        editTextJvitaHHID = (EditText) findViewById(R.id.editTextJivitaHHID);
        editTextHeadName = (EditText) findViewById(R.id.editTextHeadName);
        editTextNumberPeople = (EditText) findViewById(R.id.editTextNumberPeople);
        editTextWomanName = (EditText) findViewById(R.id.editTextWomanName);
        editTextWomanDOB = (EditText) findViewById(R.id.editTextWomanDOB);


        radioGroupGenderHeadHH = (RadioGroup) findViewById(R.id.radioGroupHeadGender);
        radioGroupLiveWithHusband = (RadioGroup) findViewById(R.id.radioGroupWLiveWithHusband);


        layoutWomanRegistration = (LinearLayout) findViewById(R.id.layoutWomanRegistration);
        // by default it's hidden until we know there's a woman in household
        layoutWomanRegistration.setVisibility(View.GONE);

        layoutHusbandAlive = (LinearLayout) findViewById(R.id.layoutHusbandAlive);
        layoutLiveWithHusband = (LinearLayout) findViewById(R.id.layoutWLiveWithHusband);


    }

    private void setRadioListeners() {

        radioGroupGenderHeadHH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                // R.id.radioMale || R.id.radioFemale
            }
        });

        layoutWomanRegistration.setVisibility(View.VISIBLE);

        radioGroupLiveWithHusband.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radioYesLiveWithHusband) {
                    // show husband registration
                    layoutHusbandAlive.setVisibility(View.GONE);
                } else {
                    // ask if the husband is alive
                    layoutHusbandAlive.setVisibility(View.VISIBLE);
                }

            }
        });
    }

}
