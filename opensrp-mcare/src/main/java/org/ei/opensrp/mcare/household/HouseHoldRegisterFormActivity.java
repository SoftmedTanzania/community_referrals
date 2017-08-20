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


    EditText editTextHeadName, editTextInterviewDate, editTextGoBHHID,
            editTextJvitaHHID, editTextLatitude, editTextLongitude,
            editTextNumberPeople, editTextWomanName, editTextWomanDOB;
    View spaceBottom;

    RadioGroup radioGroupGenderHeadHH, radioGroupStillMenstr,
            radioGroupAnyWomanBtn1349, radioGroupWSterilized, radioGroupLiveWithHusband;

    LinearLayout layoutWomanRegistration, layoutWSterilized, layoutHusbandAlive, layoutLiveWithHusband;

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
        spaceBottom = findViewById(R.id.spaceBottom);
        editTextInterviewDate = (EditText) findViewById(R.id.editTextInterviewDate);
        editTextGoBHHID = (EditText) findViewById(R.id.editTextGoBHHID);
        editTextJvitaHHID = (EditText) findViewById(R.id.editTextJivitaHHID);
        editTextHeadName = (EditText) findViewById(R.id.editTextHeadName);
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        editTextNumberPeople = (EditText) findViewById(R.id.editTextNumberPeople);
        editTextWomanName = (EditText) findViewById(R.id.editTextWomanName);
        editTextWomanDOB = (EditText) findViewById(R.id.editTextWomanDOB);

        // default value for interview date
        editTextInterviewDate.setText(dateFormat.format(today));

        radioGroupGenderHeadHH = (RadioGroup) findViewById(R.id.radioGroupHeadGender);
        radioGroupAnyWomanBtn1349 = (RadioGroup) findViewById(R.id.radioGroupAnyWomanBtn1349);
        radioGroupStillMenstr = (RadioGroup) findViewById(R.id.radioGroupMenstr);
        radioGroupWSterilized = (RadioGroup) findViewById(R.id.radioGroupWomanSterilized);
        radioGroupLiveWithHusband = (RadioGroup) findViewById(R.id.radioGroupWLiveWithHusband);


        layoutWomanRegistration = (LinearLayout) findViewById(R.id.layoutWomanRegistration);
        // by default it's hidden until we know there's a woman in household
        layoutWomanRegistration.setVisibility(View.GONE);

        layoutWSterilized = (LinearLayout) findViewById(R.id.layoutWSterilized);
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

        radioGroupAnyWomanBtn1349.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radioYesWomanInHH) {
                    // show register woman layout
                    spaceBottom.setVisibility(View.GONE);
                    layoutWomanRegistration.setVisibility(View.VISIBLE);


                } else if (layoutWomanRegistration.getVisibility() == View.VISIBLE) {
                    // if choice is no/ don't know
                    layoutWomanRegistration.setVisibility(View.GONE);
                    spaceBottom.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroupStillMenstr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radioYesStillMenstr:
                        // show additional fields
                        layoutWSterilized.setVisibility(View.VISIBLE);
                        break;

//                    case R.id.radioNoMenstr:
//                        break;
//
//                    case R.id.radioDontKnowMenstr:
//                        break;
                    default:
                        // hide additional fields
                        // registration is done
                        layoutWSterilized.setVisibility(View.GONE);
                }
            }
        });


        radioGroupWSterilized.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radioNoWSterilized) {
                    // show additional fields
                    layoutLiveWithHusband.setVisibility(View.VISIBLE);
                } else {
                    // hide additional fields
                    layoutLiveWithHusband.setVisibility(View.GONE);
                }
            }
        });

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
