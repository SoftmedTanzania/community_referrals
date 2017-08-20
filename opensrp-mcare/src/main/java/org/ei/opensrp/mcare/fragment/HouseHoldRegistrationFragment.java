package org.ei.opensrp.mcare.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HouseHoldRegistrationFragment extends Fragment {
    private static final String TAG = HouseHoldRegistrationFragment.class.getSimpleName();
    private EditText editTextHeadName, editTextGoBHHID,
            editTextNumberPeople, editTextWomanName, editTextWomanDOB;

    private RadioGroup radioGroupGenderHeadHH, radioGroupLiveWithHusband;

    private LinearLayout layoutWomanRegistration, layoutHusbandAlive, layoutLiveWithHusband;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private long today;
    private String recordId;
    private String formName;
    private JSONObject fieldOverides = new JSONObject();

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

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
        editTextGoBHHID = (EditText) view.findViewById(R.id.editTextGoBHHID);
        editTextHeadName = (EditText) view.findViewById(R.id.editTextHeadName);
        editTextNumberPeople = (EditText) view.findViewById(R.id.editTextNumberPeople);
        editTextWomanName = (EditText) view.findViewById(R.id.editTextWomanName);
        editTextWomanDOB = (EditText) view.findViewById(R.id.editTextWomanDOB);

        radioGroupGenderHeadHH = (RadioGroup) view.findViewById(R.id.radioGroupHeadGender);
        radioGroupLiveWithHusband = (RadioGroup) view.findViewById(R.id.radioGroupWLiveWithHusband);

        layoutWomanRegistration = (LinearLayout) view.findViewById(R.id.layoutWomanRegistration);
        // by default it's hidden until we know there's a woman in household
        layoutWomanRegistration.setVisibility(View.GONE);

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

    // override this on tha child classes to override specific fields
    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public JSONObject getFieldOverides() {
        return fieldOverides;
    }

    public void setFieldOverides(String overrides) {
        try {
            //get the field overrides map
            if (overrides != null) {
                JSONObject json = new JSONObject(overrides);
                String overridesStr = json.getString("fieldOverrides");
                this.fieldOverides = new JSONObject(overridesStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void processFormSubmission(String formSubmission) {
        Log.d(TAG, "submitted data = " + formSubmission);
        ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(formSubmission, recordId, formName, getFormFieldsOverrides());
    }

    public void savePartialFormData(String partialData) {
        ((SecuredNativeSmartRegisterActivity) getActivity()).savePartialFormData(partialData, recordId, formName, getFormFieldsOverrides());
    }
}
