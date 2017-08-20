package org.ei.opensrp.mcare.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ei.opensrp.mcare.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
public class HouseHoldRegistrationFragment extends Fragment {
    EditText editTextHeadName, editTextInterviewDate, editTextGoBHHID,
            editTextLatitude, editTextLongitude,
            editTextNumberPeople, editTextWomanName, editTextWomanDOB;
    View spaceBottom;

    RadioGroup radioGroupGenderHeadHH, radioGroupStillMenstr,
            radioGroupAnyWomanBtn1349, radioGroupWSterilized, radioGroupLiveWithHusband;

    LinearLayout layoutWomanRegistration, layoutWSterilized, layoutHusbandAlive, layoutLiveWithHusband;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private long today;

    public HouseHoldRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        today = System.currentTimeMillis();
        View v = inflater.inflate(R.layout.activity_house_hold_register_form, container, false);
        findViews(v);
        setRadioListeners();
        return v;
    }

    private void findViews(View view) {
        spaceBottom = view.findViewById(R.id.spaceBottom);
        editTextInterviewDate = (EditText) view.findViewById(R.id.editTextInterviewDate);
        editTextGoBHHID = (EditText) view.findViewById(R.id.editTextGoBHHID);
        editTextHeadName = (EditText) view.findViewById(R.id.editTextHeadName);
        editTextLatitude = (EditText) view.findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) view.findViewById(R.id.editTextLongitude);
        editTextNumberPeople = (EditText) view.findViewById(R.id.editTextNumberPeople);
        editTextWomanName = (EditText) view.findViewById(R.id.editTextWomanName);
        editTextWomanDOB = (EditText) view.findViewById(R.id.editTextWomanDOB);

        // default value for interview date
        editTextInterviewDate.setText(dateFormat.format(today));

        radioGroupGenderHeadHH = (RadioGroup) view.findViewById(R.id.radioGroupHeadGender);
        radioGroupAnyWomanBtn1349 = (RadioGroup) view.findViewById(R.id.radioGroupAnyWomanBtn1349);
        radioGroupStillMenstr = (RadioGroup) view.findViewById(R.id.radioGroupMenstr);
        radioGroupWSterilized = (RadioGroup) view.findViewById(R.id.radioGroupWomanSterilized);
        radioGroupLiveWithHusband = (RadioGroup) view.findViewById(R.id.radioGroupWLiveWithHusband);


        layoutWomanRegistration = (LinearLayout) view.findViewById(R.id.layoutWomanRegistration);
        // by default it's hidden until we know there's a woman in household
        layoutWomanRegistration.setVisibility(View.GONE);

        layoutWSterilized = (LinearLayout) view.findViewById(R.id.layoutWSterilized);
        layoutHusbandAlive = (LinearLayout) view.findViewById(R.id.layoutHusbandAlive);
        layoutLiveWithHusband = (LinearLayout) view.findViewById(R.id.layoutWLiveWithHusband);


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
