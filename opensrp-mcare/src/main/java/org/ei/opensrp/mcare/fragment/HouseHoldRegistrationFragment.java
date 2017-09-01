package org.ei.opensrp.mcare.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class HouseHoldRegistrationFragment extends Fragment {
    private static final String TAG = HouseHoldRegistrationFragment.class.getSimpleName();
    private EditText editTextHeadName, editTextGoBHHID,
            editTextNumberPeople, editTextWomanName, editTextWomanDOB;

    private RadioGroup radioGroupGenderHeadHH, radioGroupLiveWithHusband;

    private LinearLayout layoutWomanRegistration, layoutHusbandAlive, layoutLiveWithHusband;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private String recordId;
    private String formName;
    private Button submitButton;
    private JSONObject fieldOverides = new JSONObject();
    private String currentLocation;
    private Date start;

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

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
        View v = inflater.inflate(R.layout.activity_house_hold_register_form, container, false);
        findViews(v);
        setListeners();
        start =  Calendar.getInstance().getTime();
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

        submitButton = (Button)view.findViewById(R.id.submit);


    }

    private void setListeners() {
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> dataHash = new HashMap<String, String>();
                dataHash.put("id",recordId);
                dataHash.put("existing_location",currentLocation);
                dataHash.put("existing_Country","Tanzania");
                dataHash.put("form_name",formName);
                dataHash.put("today", dateFormat.format(Calendar.getInstance().getTime()));
                dataHash.put("start", dateTimeFormat.format(start));
                dataHash.put("end", dateTimeFormat.format(Calendar.getInstance().getTime()));
                dataHash.put("FWNHREGDATE", dateFormat.format(Calendar.getInstance().getTime()));
                dataHash.put("FWHOHFNAME", editTextHeadName.getText().toString().split(" ")[0]);
                dataHash.put("FWHOHLNAME", editTextHeadName.getText().toString().split(" ")[1]);


//                dataHash.put("FWHOHBIRTHDATE", );
//                dataHash.put("FWHOHGENDER", );
//                dataHash.put("FWNHHMBRNUM", );
//                dataHash.put("FWNHHMWRA", );
//                dataHash.put("ELCO", "1");
//                dataHash.put("user_type", "1");


                dataHash.put("id",editTextHeadName.getText().toString());
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

    //TODO Implement this method to initialize a form data
    public void setFormData(String data){
        Log.d(TAG,"Setting form data");
    }

    //TODO Implement this method to save the current unsubmitted form data for future uses
    public void saveCurrentFormData(){
        Log.d(TAG,"Save the currentform data");
    }
}
