package com.softmed.htmr_chw;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw.Repository.FacilityObject;
import com.softmed.htmr_chw.Repository.ReferralServiceObject;
import com.softmed.htmr_chw.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ClientReferralRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.String.valueOf;
import static com.softmed.htmr_chw.util.Utils.generateRandomUUIDString;

/**
 * Created by issy on 11/17/17.
 */

public class ClientsFormRegisterActivity extends SecuredNativeSmartRegisterActivity {

    private Toolbar toolbar;
    private static final String TAG = ClientsFormRegisterActivity.class.getSimpleName();
    public static AutoCompleteTextView facilitytextView;
    public static EditText editTextfName,editTextmName,editTextlName,editTextVillageLeader, editTextAge, editTextCTCNumber,
            editTextDiscountId,editTextKijiji,editTextReferralReason;
    public static Button button;
    public static MaterialSpinner spinnerService, spinnerGender;
    private ArrayAdapter<String> serviceAdapter;
    private ArrayAdapter<String>  facilityAdapter;
    private Calendar today;
    private long dob;
    private LinearLayout parentLayout;
    private EditText textPhone;
    private List<String> facilityList = new ArrayList<String>();
    private List<String> serviceList = new ArrayList<String>();
    private List<String> category = new ArrayList<String>();
    private List<String> AllCheckbox = new ArrayList<String>();
    public String message = "";
    public static Context context;
    public static int clientServiceSelection = -1,genderSelection = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String  formName = "client_referral_form";
    private String recordId,wardId="";
    private ClientReferral clientReferral;
    private Gson gson = new Gson();
    private JSONObject fieldOverides = new JSONObject();
    private CommonRepository commonRepository,commonRepository1;
    private Cursor cursor;
    private MaterialEditText dobTextView;
    private List<ReferralServiceObject> referralServiceList;
    private List<FacilityObject> facilitiesList;
    ArrayList<String> genderList = new ArrayList<String>();
    public Dialog referalDialogue;
    public String categoryValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(com.softmed.htmr_chw.R.layout.activity_client_registration_form);
        setReferralServiceList();
        setFacilistList();
        setupviews();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = (Toolbar) findViewById(com.softmed.htmr_chw.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            wardId= null;
        } else {
            wardId= bundle.getString("selectedLocation");
        }
        today = Calendar.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormSubmissionOk()) {
                    //setting default values
                    clientReferral = getClientReferral();
                    clientReferral.setReferral_status("0");

                    // convert to json
                    String gsonReferral = gson.toJson(clientReferral);
                    Log.d(TAG, "referral = " + gsonReferral);
                    Log.d(TAG, "fname = " + formName);

                    // todo start form submission
                    saveFormSubmission(gsonReferral, generateRandomUUIDString(), formName, getFormFieldsOverrides());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("status", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

    }


    @Override
    public void saveFormSubmission(String formSubmission, final String id, String formName, JSONObject fieldOverrides) {
        // save the form
            final ClientReferral clientReferral = gson.fromJson(formSubmission, ClientReferral.class);
            clientReferral.setId(id);
            ContentValues values = new ClientReferralRepository().createValuesFor(clientReferral);
            Log.d(TAG, "values = " + gson.toJson(values));

            commonRepository = context().commonrepository("client_referral");
            commonRepository.customInsert(values);

            CommonPersonObject c = commonRepository.findByCaseID(id);
            List<FormField> formFields = new ArrayList<>();

            formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));
            formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

            FormData formData;
            FormInstance formInstance;
            FormSubmission submission;

                for ( String key : c.getDetails().keySet() ) {
                    Log.d(TAG,"key = "+key);
                  if(key.equals("facility_id")){
                        FormField f = new FormField(key, c.getDetails().get(key), "facility.id");
                        formFields.add(f);
                    }else{
                        FormField f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                        formFields.add(f);
                    }
                }
                Log.d(TAG,"form field = "+ new Gson().toJson(formFields));
                Log.d(TAG,"am in tb");
                formData = new FormData("client_referral","/model/instance/client_referral_form/",formFields,null);
                formInstance  = new FormInstance(formData,"1");
                submission= new FormSubmission(generateRandomUUIDString(),id,"client_referral_form",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
                context().formDataRepository().saveFormSubmission(submission);

            new  com.softmed.htmr_chw.util.AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    if(!clientReferral.getPhone_number().equals(""))
                        Utils.sendRegistrationAlert(clientReferral.getPhone_number());
                    return null;
                }
            }.execute();


    }

    private void setupviews(){

        textPhone = (EditText)   findViewById(com.softmed.htmr_chw.R.id.edittextPhone);
        dobTextView = (MaterialEditText)   findViewById(com.softmed.htmr_chw.R.id.reg_dob);


        editTextfName = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextfName);
        editTextmName = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextmName);
        editTextlName = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextlName);
        editTextReferralReason = (EditText)   findViewById(com.softmed.htmr_chw.R.id.reason_for_referral);
        editTextVillageLeader = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextVillageLeader);


        Log.d(TAG, "username"+((BoreshaAfyaApplication)getApplication()).getUsername());
        Log.d(TAG, "team name "+((BoreshaAfyaApplication)getApplication()).getTeam_name());

        editTextDiscountId = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextDiscountId);
        editTextKijiji = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextKijiji);
        editTextCTCNumber = (EditText)   findViewById(com.softmed.htmr_chw.R.id.editTextOthers);

        button = (Button)   findViewById(com.softmed.htmr_chw.R.id.referal_button);

        serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceList);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService = (MaterialSpinner)   findViewById(com.softmed.htmr_chw.R.id.spinnerService);
        spinnerService.setAdapter(serviceAdapter);

        facilityAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, facilityList);
         facilitytextView = (AutoCompleteTextView) findViewById(com.softmed.htmr_chw.R.id.autocomplete_facility);
        facilitytextView.setThreshold(1);
        facilitytextView.setAdapter(facilityAdapter);


        String[] ITEMS = getResources().getStringArray(com.softmed.htmr_chw.R.array.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender = (MaterialSpinner) findViewById(com.softmed.htmr_chw.R.id.spinnerGender);
        spinnerGender.setAdapter(adapter);


        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerService.setFloatingLabelText("Aina za Huduma");
                    clientServiceSelection = i;
                }


                String service = spinnerService.getSelectedItem().toString();
                List<Indicator> indicator = new ArrayList<Indicator>();
                if(service.equals(getResources().getString(R.string.referral_services))){

                }else {

                    parentLayout = (LinearLayout) findViewById(com.softmed.htmr_chw.R.id.check_add_layout);
                    Log.d(TAG," Coze Service : "+service);
                    categoryValue = getCategory(service);
                    if(categoryValue.equalsIgnoreCase("malaria")){
                        Log.d(TAG,"category"+categoryValue);
                        editTextReferralReason.setVisibility(View.GONE);
                    }else
                        editTextReferralReason.setVisibility(View.VISIBLE);

                    indicator = getIndicator(getReferralServiceId(service));
                    parentLayout.removeAllViewsInLayout();
                }
                int size = indicator.size();

                for(int k=0; k<size;k++){
                    CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setId(k);
                    checkBox.setPadding(0,0,0,0);
                    checkBox.setTextColor(getResources().getColor(com.softmed.htmr_chw.R.color.secondary_text));
                    checkBox.setText(indicator.get(k).getIndicatorName());
                    checkBox.setTextSize(14);

                    LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                            Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
                    checkParams.setMargins(0, 8, 0, 8);
                    checkParams.gravity = Gravity.START;
                    
                    parentLayout.addView(checkBox, checkParams);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerGender.setFloatingLabelText("Chagua Jinsia");
                    genderSelection = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerService.setSelection(clientServiceSelection);
        spinnerGender.setSelection(genderSelection);

        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(com.softmed.htmr_chw.R.id.reg_dob);
            }
        });

    }
    
    private void setReferralServiceList(){
        commonRepository = context().commonrepository("referral_service");
        //todo martha edit the query
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.referralServiceList = Utils.convertToServiceObjectList(commonPersonObjectList);
        int size = referralServiceList.size();

        for(int i =0; size > i; i++  ){


            serviceList.add(referralServiceList.get(i).getName());
        }
        Log.d(TAG, "service list"+serviceList.toString());
    }


    private void setFacilistList(){
        commonRepository1 = context().commonrepository("facility");
        //todo martha edit the query
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility");

        List<CommonPersonObject> commonPersonObjectList2 = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList2));

        this.facilitiesList = Utils.convertToFacilityObjectList(commonPersonObjectList2);
        int size2 = facilitiesList.size();

        for(int i =0; size2 > i; i++  ){

            facilityList.add(facilitiesList.get(i).getName());
        }
        Log.d(TAG, "facility list"+facilityList.toString());
    }


    @Override
    protected DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    @Override
    public void startRegistration() {

    }

    private void pickDate(final int id) {
        // listener
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                if (id == com.softmed.htmr_chw.R.id.reg_dob)
                    dob = pickedDate.getTimeInMillis();
                    dobTextView.setText(dateFormat.format(pickedDate.getTimeInMillis()));
            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), com.softmed.htmr_chw.R.color.colorPrimary));

        // show dialog
        datePickerDialog.show(this.getFragmentManager(), "DatePickerDialog");
    }


    public boolean isFormSubmissionOk() {
        if (TextUtils.isEmpty(editTextfName.getText())  ||
                TextUtils.isEmpty(editTextlName.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.unfilled_information);
            makeToast();
            return false;
        } else if (TextUtils.isEmpty(facilitytextView.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_facility);
            makeToast();
            return false;

        }else if (!TextUtils.isEmpty(facilitytextView.getText())) {
            String facilityName = facilitytextView.getText().toString();
            Log.d(TAG,"facility name = "+facilityName);
            facilityName = facilityName.trim();
            int index = facilityList.indexOf(facilityName);

            Log.d(TAG,"facility name index = "+facilityName);

            if(index<=0){
                message = getResources().getString(com.softmed.htmr_chw.R.string.wrong_facility);
                makeToast();
                return false;
            }else {
                return true;
            }

        }else if (spinnerGender.getSelectedItemPosition() <=0) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_gender);
            makeToast();
            return false;

        }else if (spinnerService.getSelectedItemPosition() <= 0 ) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_services);
            makeToast();
            return false;

        } else if (TextUtils.isEmpty(editTextKijiji.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_physical_address);
            makeToast();
            return false;
        } else  if (TextUtils.isEmpty(editTextReferralReason.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_missing_referral_reason);
            makeToast();
            return false;
        }else
            // all good
            return true;
    }

    public ClientReferral getClientReferral() {
        ClientReferral referral = new ClientReferral();
        referral.setReferral_date(today.getTimeInMillis());
        referral.setDate_of_birth(dob);
        referral.setCommunity_based_hiv_service(editTextDiscountId.getText().toString());
        referral.setFirst_name(editTextfName.getText().toString());
        referral.setMiddle_name(editTextmName.getText().toString());
        referral.setSurname(editTextlName.getText().toString());
        referral.setVillage(editTextKijiji.getText().toString());
        referral.setIs_valid("true");
        referral.setPhone_number(textPhone.getText().toString());
        referral.setFacility_id(getFacilityId(facilitytextView.getText().toString()));
        if(spinnerGender.getSelectedItem().toString().equalsIgnoreCase("female") || spinnerGender.getSelectedItem().toString().contains("ke"))
            referral.setGender("Female");
        else
            referral.setGender("Male");
        referral.setVillage_leader(editTextVillageLeader.getText().toString());
        referral.setReferral_reason(editTextReferralReason.getText().toString());
        referral.setReferral_service_id(getReferralServiceId(spinnerService.getSelectedItem().toString()));
        referral.setWard(wardId);
        referral.setCtc_number(editTextCTCNumber.getText().toString());
        referral.setTest_results(false);
        referral.setServices_given_to_patient("");
        referral.setOther_notes("");
        referral.setService_provider_uiid(((BoreshaAfyaApplication)getApplication()).getCurrentUserID());
        referral.setService_provider_group(((BoreshaAfyaApplication)getApplication()).getTeam_uuid());
        for(int i=0; i<parentLayout.getChildCount(); i++) {
            CheckBox nextChild = (CheckBox) parentLayout.getChildAt(i);
            if (nextChild.isChecked()) {
                CheckBox check = nextChild;
                if (check.isChecked()) {
                    AllCheckbox.add(getIndicatorId(check.getText().toString()));
                }
            }
        }
        referral.setIndicator_ids(new Gson().toJson(AllCheckbox));


        return referral;
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public String getFacilityId(String name){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where name ='"+ name +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    public List<Indicator> getIndicator(String id){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where referralIndicatorId ='"+ id +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        List<Indicator> indicator = Utils.convertToIndicatorList(commonPersonObjectList);
        return indicator;
    }

    public String getIndicatorId(String name){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where indicatorName ='"+ name +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("referralServiceIndicatorId");
    }

    public String getReferralServiceId(String name){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where name ='"+ name +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    public String getCategory(String name){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where category ='"+ name +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("category");
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    public static void setLanguage() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();


        android.util.Log.d(TAG,"set Locale : "+preferredLocale);

        Resources res = org.ei.opensrp.Context.getInstance().applicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }

}
