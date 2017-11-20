package org.ei.opensrp.drishti.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.util.DatesHelper;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class CHWPreRegisterFormFragment extends Fragment {
    private static final String TAG = AncRegisterFormFragment.class.getSimpleName();
    public static TextView textDate, textPhone;
    LinearLayout layoutDatePick, layoutEditPhone;
    CardView cardDatePickLNMP;
    public static EditText editTextClientName,editTextClinicName, editTextVillageLeader, editTextAge, editTextChildrenCount,
            editTextDiscountId, editTextKataAddress,editTextKijiji,editTextKijitongoji,editTextReferralReason,editTextReferralFacility;
    public static TextView textviewReferralProviderSupportGroup,textviewReferralProvider;
    public static Button button;
    public static RadioGroup radioGroupGender;
    public static MaterialSpinner spinnerService;
    private ArrayAdapter<String>  serviceAdapter;

    private Calendar today;
    private List<String> educationList = new ArrayList<>();
    private List<String> serviceList = new ArrayList<>();
    public String message = "";
    public static Context context;
    public static int clientEduSelection = -1, clientServiceSelection = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String formName = "client_hiv_referral_form";
    private String recordId;
    private ClientReferral clientReferral;
    private Gson gson = new Gson();
    private JSONObject fieldOverides = new JSONObject();

    public CHWPreRegisterFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        today = Calendar.getInstance();


        serviceList.add("Ushauri nasaha na kupima");
        serviceList.add("Rufaa kwenda kliniki ya TB na Matunzo (CTC)");
        serviceList.add("Rufaa kwenda kituo cha kutoa huduma za afya kutokana na magonjwa nyemelezi");
        serviceList.add("Kliniki ya kutibu kifua kikuu");
        serviceList.add("Huduma za kuzuia maambukizi toka kwa mama kwenda mtoto");
        serviceList.add("Huduma ya afya ya uzazi na mtoto (RCH) ");
        serviceList.add("Huduma ya Tohara (VMMC)");
        serviceList.add("Msaada wa kisheria");
        serviceList.add("Huduma za kuzuia ukatili wa kijinsia(DAwati la jinsia)");
        serviceList.add("Huduma za kuzuia maambukizi toka kwa mama kwenda mtoto");

        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.activity_chwedit_pre_registration, container, false);


        textDate = (TextView) fragmentView.findViewById(R.id.textDate);
        textPhone = (TextView) fragmentView.findViewById(R.id.textPhone);
        layoutDatePick = (LinearLayout) fragmentView.findViewById(R.id.layoutDatePick);
        layoutEditPhone = (LinearLayout) fragmentView.findViewById(R.id.layoutEditPhone);
        cardDatePickLNMP = (CardView) fragmentView.findViewById(R.id.cardPickDateLNMP);


        editTextClinicName = (EditText) fragmentView.findViewById(R.id.editTextClinicName);
        editTextClientName = (EditText) fragmentView.findViewById(R.id.editTextClientName);
        editTextAge = (EditText) fragmentView.findViewById(R.id.editTextMotherAge);
        editTextReferralReason = (EditText) fragmentView.findViewById(R.id.reason_for_referral);
        editTextVillageLeader = (EditText) fragmentView.findViewById(R.id.editTextVillageLeader);
        textviewReferralProvider = (TextView) fragmentView.findViewById(R.id.provider_name);
        textviewReferralProviderSupportGroup = (TextView) fragmentView.findViewById(R.id.provider_support_group);
        editTextDiscountId = (EditText) fragmentView.findViewById(R.id.editTextDiscountId);
        editTextKataAddress = (EditText) fragmentView.findViewById(R.id.editTextKataAddress);
        editTextKijiji = (EditText) fragmentView.findViewById(R.id.editTextKijiji);
        editTextKijitongoji = (EditText) fragmentView.findViewById(R.id.editTextKijitongoji);

        button = (Button) fragmentView.findViewById(R.id.save);

        serviceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, serviceList);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService = (MaterialSpinner) fragmentView.findViewById(R.id.spinnerService);
        spinnerService.setAdapter(serviceAdapter);


        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerService.setFloatingLabelText("Aina za Huduma");
                    clientServiceSelection = i;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerService.setSelection(clientServiceSelection);


        radioGroupGender = (RadioGroup) fragmentView.findViewById(R.id.radioGroupGender);

        // initialize date to today's date
        textDate.setText(dateFormat.format(today.getTimeInMillis()));

        layoutDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(R.id.textDate);
            }
        });

        layoutEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show edit phone dialog
                showEditPhoneDialog();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormSubmissionOk()) {
                    //setting default values
                    clientReferral = getClientReferral();
                    

                    // convert to json
                    String gsonReferral = gson.toJson(clientReferral);
                    Log.d(TAG, "referral = " + gsonReferral);

                    // todo start form submission

                    ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(gsonReferral, recordId, formName, getFormFieldsOverrides());
                    getActivity().finish();
                }

            }
        });

        return fragmentView;
    }


    private void pickDate(final int id) {
        // listener
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                // get picked date
                // update view
                GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                if (id == R.id.textDate)
                    textDate.setText(dateFormat.format(pickedDate.getTimeInMillis()));


            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getContext(), R.color.primary));

        // show dialog
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }


    public void showEditPhoneDialog() {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_edit_phone, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        final EditText editTextPhone = (EditText) dialogView.findViewById(R.id.editTextLocation);
        // get previously entered location
        if (textPhone.getText() != null)
            editTextPhone.setText(textPhone.getText());

        // positive button
        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = editTextPhone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    editTextPhone.setError("Please enter a valid phone number.");
                    return;
                }

                // update view
                textPhone.setText(phone);

                // close dialog
                dialog.dismiss();
            }
        });

        // negative button
        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close dialog
                dialog.dismiss();
            }
        });
    }

    public boolean isFormSubmissionOk() {
        if (
                TextUtils.isEmpty(editTextClinicName.getText())
                || TextUtils.isEmpty(editTextClientName.getText())
                || TextUtils.isEmpty(editTextKataAddress.getText())
                || TextUtils.isEmpty(editTextKijitongoji.getText())
                || TextUtils.isEmpty(editTextKijiji.getText())
                || TextUtils.isEmpty(editTextReferralReason.getText())
                || TextUtils.isEmpty(editTextVillageLeader.getText())
                || TextUtils.isEmpty(textPhone.getText())
                || TextUtils.isEmpty(editTextDiscountId.getText())
                ) {

            message = "Tafadhali jaza taarifa zote muhimu";
            makeToast();

            return false;

        } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            // no radio checked
            message = "Tafadhali chagua umri wa ujauzito.";
            makeToast();
            return false;

        } else if (spinnerService.getSelectedItemPosition() < 0) {

            message = "Tafadhali chagua aina ya huduma";
            makeToast();
            return false;

        }  else
            // all good
            return true;
    }

    public ClientReferral getClientReferral() {
        ClientReferral referral = new ClientReferral();

        referral.setReferralDate(textDate.getText().toString());
        referral.setCBHS(editTextDiscountId.getText().toString());
        referral.setClientName(editTextClientName.getText().toString());
        if(radioGroupGender.getCheckedRadioButtonId() == R.id.male)
            referral.setGender("me");
        else
            referral.setGender("fe");
        referral.setKata(editTextKataAddress.getText().toString());
        referral.setKijiji(editTextKijiji.getText().toString());
        referral.setKijitongoji(editTextKijitongoji.getText().toString());
        referral.setIsValid("true");
        referral.setPhoneNumber(textPhone.getText().toString());
        referral.setReferralFacility(editTextClinicName.getText().toString());
        referral.setVillageLeader(editTextDiscountId.getText().toString());
        referral.setReferralService(spinnerService.getSelectedItem().toString());
        referral.setServiceProviderName(textviewReferralProvider.getText().toString());
        referral.setServiceProviderGroup(textviewReferralProviderSupportGroup.getText().toString());

        Log.d(TAG, "referral 1 ="+ new Gson().toJson(referral));
        return referral;
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public void setRecordId(String recordId) {

        Log.d("TAG","record id = "+recordId);
        this.recordId = recordId;
    }

    private void makeToast() {
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG).show();
    }

    //TODO martha Implement this method to initialize a form data
    public void setFormData(String data) {
        Log.d(TAG, "Setting form data");
//        ((SecuredNativeSmartRegisterActivity) getActivity()).saveFormSubmission(data, recordId, formName, getFormFieldsOverrides());
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
}
