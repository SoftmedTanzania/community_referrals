package org.ei.opensrp.drishti;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.domain.Facility;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;
import org.ei.opensrp.drishti.DataModels.FollowUp;
import org.ei.opensrp.drishti.Fragments.ClientRegistrationFormFragment;
import org.ei.opensrp.drishti.Repository.FacilityObject;
import org.ei.opensrp.drishti.Repository.FollowUpPersonObject;
import org.ei.opensrp.drishti.Repository.FollowUpRepository;
import org.ei.opensrp.drishti.Repository.ReferralServiceObject;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientReferralRepository;
import org.ei.opensrp.repository.FacilityRepository;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

import static java.lang.String.valueOf;
import static org.ei.opensrp.drishti.util.Utils.generateRandomUUIDString;

/**
 * Created by issy on 11/17/17.
 */

public class ClientsFormRegisterActivity extends SecuredNativeSmartRegisterActivity {

    private Toolbar toolbar;
    private static final String TAG = ClientRegistrationFormFragment.class.getSimpleName();
    public static TextView textDate;
    public static EditText editTextfName,editTextmName,editTextlName,editTextVillageLeader, editTextAge, editTextCTCNumber,
            editTextDiscountId,editTextKijiji,editTextReferralReason;
    public static Button button;
    public static MaterialSpinner spinnerService, spinnerFacility,spinnerGender;
    private ArrayAdapter<String> serviceAdapter;
    private ArrayAdapter<String>  facilityAdapter;
    private ArrayList<Facility> facilities;

    private Calendar today;
    private static CheckBox checkBoxAreasonOne, checkBoxreasonTwo, checkBoxreasonThree,
            checkBoxreasonFour, checkBoxresonFive, checkBoxreasonSix;
    private long dob;
    private LinearLayout tbLayout;
    private EditText CTCLayout,textPhone;
    private List<String> facilityList = new ArrayList<String>();
    private List<String> serviceList = new ArrayList<String>();
    public String message = "";
    public static Context context;
    public static int clientServiceSelection = -1,genderSelection = -1,facilitySelection = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String  formName = "client_referral_form";
    private String recordId,wardId="",fName ="";
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
    public ChwSmartRegisterActivity chwSmartRegisterActivity = new ChwSmartRegisterActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        setReferralServiceList();
        setFacilistList();
        setupviews();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                    clientReferral.setStatus("0");

                    // convert to json
                    String gsonReferral = gson.toJson(clientReferral);
                    Log.d(TAG, "referral = " + gsonReferral);

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

        if(formName.equals("client_referral_form")){
            final ClientReferral clientReferral = gson.fromJson(formSubmission, ClientReferral.class);
            clientReferral.setId(id);
            ContentValues values = new ClientReferralRepository().createValuesFor(clientReferral);
            Log.d(TAG, "values = " + gson.toJson(values));

            CommonRepository commonRepository = context().commonrepository("client_referral");
            commonRepository.customInsert(values);

            CommonPersonObject c = commonRepository.findByCaseID(id);
            List<FormField> formFields = new ArrayList<>();


            formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


            formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));



            FormData formData;
            FormInstance formInstance;
            FormSubmission submission;
            if(clientReferral.getReferral_service_id().equals("Kliniki ya kutibu kifua kikuu")){
                for ( String key : c.getDetails().keySet() ) {
                    Log.d(TAG,"key = "+key);
                    FormField f = null;
                    f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                    if(key.equals("facility_id")){
                        f = new FormField(key, c.getDetails().get(key), "facility.id");
                    }
                    formFields.add(f);
                }


                Log.d(TAG,"form field = "+ new Gson().toJson(formFields));
                Log.d(TAG,"am in tb");
                formData = new FormData("client_referral","/model/instance/client_tb_referral_form/",formFields,null);
                formInstance  = new FormInstance(formData,"1");
                submission= new FormSubmission(generateRandomUUIDString(),id,"client_tb_referral_form",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");

            }else if(clientReferral.getReferral_service_id().equals("Rufaa kwenda kliniki ya TB na Matunzo (CTC)")){
                Log.d(TAG,"am in hiv");
                for ( String key : c.getDetails().keySet() ) {
                    Log.d(TAG,"key = "+key);

                    if(key.equals("has2WeekCough")||key.equals("hasFever")||key.equals("hadWeightLoss")||key.equals("hasSevereSweating")||key.equals("hasBloodCough")||key.equals("isIs_lost_follow_up")){

                    }else if(key.equals("facility_id")){
                        FormField f = new FormField(key, c.getDetails().get(key), "facility.id");
                        formFields.add(f);
                    }else{
                        FormField f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                        formFields.add(f);
                    }

                }
                Log.d(TAG,"form field = "+ new Gson().toJson(formFields));
                formData = new FormData("client_referral","/model/instance/client_hiv_referral_form/",formFields,null);
                formInstance  = new FormInstance(formData,"1");
                submission= new FormSubmission(generateRandomUUIDString(),id,"client_hiv_referral_form",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");

            }else{
                Log.d(TAG,"am in general");
                for ( String key : c.getDetails().keySet() ) {
                    Log.d(TAG,"key = "+key);
                    if(key.equals("has2WeekCough")||key.equals("CTCNumber")||key.equals("hasFever")||key.equals("hadWeightLoss")||key.equals("hasSevereSweating")||key.equals("hasBloodCough")||key.equals("isIs_lost_follow_up")){

                    }else if(key.equals("facility_id")){
                        FormField f = new FormField(key, c.getDetails().get(key), "facility.id");
                        formFields.add(f);
                    }else{
                        FormField f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                        formFields.add(f);
                    }
                }
                Log.d(TAG,"form field = "+ new Gson().toJson(formFields));
                formData = new FormData("client_referral","/model/instance/client_referral_form/",formFields,null);
                formInstance  = new FormInstance(formData,"1");
                submission= new FormSubmission(generateRandomUUIDString(),id,"client_referral_form",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
            }

            context().formDataRepository().saveFormSubmission(submission);

            Log.d(TAG,"submission content = "+ new Gson().toJson(submission));



            new  org.ei.opensrp.drishti.util.AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    if(!clientReferral.getPhone_number().equals(""))
                        Utils.sendRegistrationAlert(clientReferral.getPhone_number());
                    return null;
                }
            }.execute();


        }
        else if(formName.equals("follow_up_form"))
        {

            final FollowUp followUp = gson.fromJson(formSubmission, FollowUp.class);

            final String uuid = generateRandomUUIDString();
            FollowUpPersonObject followUpPersonObject = new FollowUpPersonObject(uuid, id, followUp);
            ContentValues values = new FollowUpRepository().createValuesFor(followUpPersonObject);
            Log.d(TAG, "motherPersonObject = " + gson.toJson(followUpPersonObject));
            Log.d(TAG, "values = " + gson.toJson(values));

            CommonRepository commonRepository = context().commonrepository("follow_up");
            commonRepository.customInsert(values);

            CommonPersonObject c = commonRepository.findByCaseID(uuid);
            List<FormField> formFields = new ArrayList<>();


            formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


            formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

            for ( String key : c.getDetails().keySet() ) {
                Log.d(TAG,"key = "+key);
                FormField f = null;
                if(!key.equals("facility_id")) {
                    f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                }else{
                    f = new FormField(key, c.getDetails().get(key), "facility.id");
                }
                formFields.add(f);
            }


            Log.d(TAG,"form field = "+ new Gson().toJson(formFields));

            FormData formData = new FormData("follow_up","/model/instance/Follow_Up_Form/",formFields,null);
            FormInstance formInstance = new FormInstance(formData,"1");
            FormSubmission submission = new FormSubmission(generateRandomUUIDString(),uuid,"client_referral",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
            context().formDataRepository().saveFormSubmission(submission);

            Log.d(TAG,"submission content = "+ new Gson().toJson(submission));



            new  org.ei.opensrp.drishti.util.AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    CommonRepository commonRepository = context().commonrepository("client_referral");
                    CommonPersonObject c = commonRepository.findByCaseID(id);
                    if(!c.getDetails().get("PhoneNumber").equals(""))
                        Utils.sendRegistrationAlert(c.getDetails().get("PhoneNumber"));
                    return null;
                }
            }.execute();

        }
    }
    private void setupSearchView() {

    }

    private void setupviews(){

        textPhone = (EditText)   findViewById(R.id.edittextPhone);
        dobTextView = (MaterialEditText)   findViewById(R.id.reg_dob);
        CTCLayout = (EditText)  findViewById(R.id.editTextOthers);


        editTextfName = (EditText)   findViewById(R.id.editTextfName);
        editTextmName = (EditText)   findViewById(R.id.editTextmName);
        editTextlName = (EditText)   findViewById(R.id.editTextlName);
        //todo martha implement on age data entry
//        editTextAge = (EditText)   findViewById(R.id.editTextMotherAge);
        editTextReferralReason = (EditText)   findViewById(R.id.reason_for_referral);
        editTextVillageLeader = (EditText)   findViewById(R.id.editTextVillageLeader);


        Log.d(TAG, "username"+((BoreshaAfyaApplication)getApplication()).getUsername());
        Log.d(TAG, "team name "+((BoreshaAfyaApplication)getApplication()).getTeam_name());

        editTextDiscountId = (EditText)   findViewById(R.id.editTextDiscountId);
        editTextKijiji = (EditText)   findViewById(R.id.editTextKijiji);
        editTextCTCNumber = (EditText)   findViewById(R.id.editTextOthers);

        button = (Button)   findViewById(R.id.referal_button);

        serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceList);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService = (MaterialSpinner)   findViewById(R.id.spinnerService);
        spinnerService.setAdapter(serviceAdapter);

        facilityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facilityList);
        facilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFacility = (MaterialSpinner)   findViewById(R.id.spinnerFacility);
        spinnerFacility.setAdapter(facilityAdapter);



        String[] ITEMS = {"ME", "KE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender = (MaterialSpinner) findViewById(R.id.spinnerGender);
        spinnerGender.setAdapter(adapter);


        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerService.setFloatingLabelText("Aina za Huduma");
                    clientServiceSelection = i;
                }


                if((spinnerService.getSelectedItem().toString()).equals("Rufaa kwenda kliniki ya kutibu kifua kikuu")){


                    findViewById(R.id.checkboxis_at_hot_spot).setVisibility(View.GONE);
                    findViewById(R.id.checkboxHasHeadache).setVisibility(View.GONE);
                    findViewById(R.id.checkboxIsVomiting).setVisibility(View.GONE);
                    findViewById(R.id.checkboxis_at_hot_spot).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_affected_partner).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_symptomps_for_associative_diseases).setVisibility(View.GONE);
                    checkBoxAreasonOne = (CheckBox)   findViewById(R.id.checkbox2weekCough);
                    checkBoxreasonTwo = (CheckBox)   findViewById(R.id.checkboxfever);
                    checkBoxreasonThree = (CheckBox)   findViewById(R.id.checkboxWeightLoss);
                    checkBoxreasonFour = (CheckBox)   findViewById(R.id.checkboxSevereSweating);
                    checkBoxresonFive = (CheckBox)   findViewById(R.id.checkboxBloodCough);
                    checkBoxreasonSix = (CheckBox)   findViewById(R.id.checkboxLostFollowup);
                    checkBoxAreasonOne.setVisibility(View.VISIBLE);
                    checkBoxreasonTwo.setVisibility(View.VISIBLE);
                    checkBoxreasonThree.setVisibility(View.VISIBLE);
                    checkBoxreasonFour.setVisibility(View.VISIBLE);
                    checkBoxresonFive.setVisibility(View.VISIBLE);
                    checkBoxreasonSix.setVisibility(View.VISIBLE);
//                    tbLayout.setVisibility(View.VISIBLE);
                    CTCLayout.setVisibility(View.VISIBLE);
                    fName = "client_tb_referral_form";

                }else if((spinnerService.getSelectedItem().toString()).equals("Rufaa kwenda kliniki ya TB na Matunzo (CTC)")||(spinnerService.getSelectedItem().toString()).equals("Rufaa kwenda kupata huduma za kuzuia maambukizi toka kwa mama kwenda kwa mtoto") ){


                    findViewById(R.id.checkboxHasHeadache).setVisibility(View.GONE);
                    findViewById(R.id.checkboxWeightLoss).setVisibility(View.GONE);
                    findViewById(R.id.checkboxSevereSweating).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_loss_of_appetite).setVisibility(View.GONE);
                    findViewById(R.id.checkboxIsVomiting).setVisibility(View.GONE);
                    checkBoxAreasonOne = (CheckBox)   findViewById(R.id.checkboxis_at_hot_spot);
                    checkBoxreasonTwo = (CheckBox)   findViewById(R.id.checkboxfever);
                    checkBoxreasonThree = (CheckBox)   findViewById(R.id.checkboxWeightLoss);
                    checkBoxreasonFour = (CheckBox)   findViewById(R.id.checkboxhas_symptomps_for_associative_diseases);
                    checkBoxresonFive = (CheckBox)   findViewById(R.id.checkboxhas_affected_partner);
                    checkBoxreasonSix = (CheckBox)   findViewById(R.id.checkboxLostFollowup);
                    checkBoxAreasonOne.setVisibility(View.VISIBLE);
                    checkBoxreasonTwo.setVisibility(View.VISIBLE);
                    checkBoxreasonThree.setVisibility(View.VISIBLE);
                    checkBoxreasonFour.setVisibility(View.VISIBLE);
                    checkBoxresonFive.setVisibility(View.VISIBLE);
                    checkBoxreasonSix.setVisibility(View.VISIBLE);

//                    tbLayout.setVisibility(View.GONE);
                    CTCLayout.setVisibility(View.VISIBLE);
                    fName = "client_hiv_referral_form";

                }else if((spinnerService.getSelectedItem().toString()).equals("Rufaa kwenda kliniki kutibiwa malaria") ){

                    findViewById(R.id.checkboxis_at_hot_spot).setVisibility(View.GONE);
                    findViewById(R.id.checkboxWeightLoss).setVisibility(View.GONE);
                    findViewById(R.id.checkboxBloodCough).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_symptomps_for_associative_diseases).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_affected_partner).setVisibility(View.GONE);
                    findViewById(R.id.checkboxLostFollowup).setVisibility(View.GONE);
                    checkBoxAreasonOne = (CheckBox)   findViewById(R.id.checkboxfever);
                    checkBoxreasonTwo = (CheckBox)   findViewById(R.id.checkboxHasHeadache);
                    checkBoxreasonThree = (CheckBox)   findViewById(R.id.checkboxSevereSweating);
                    checkBoxreasonFour = (CheckBox)   findViewById(R.id.checkboxIsVomiting);
                    checkBoxresonFive = (CheckBox)   findViewById(R.id.checkboxhas_loss_of_appetite);
                    checkBoxAreasonOne.setVisibility(View.VISIBLE);
                    checkBoxreasonTwo.setVisibility(View.VISIBLE);
                    checkBoxreasonThree.setVisibility(View.VISIBLE);
                    checkBoxreasonFour.setVisibility(View.VISIBLE);
                    checkBoxresonFive.setVisibility(View.VISIBLE);
//                    tbLayout.setVisibility(View.GONE);
                    CTCLayout.setVisibility(View.GONE);
                    fName = "client_malaria_referral_form";

                }else{
                    findViewById(R.id.checkboxis_at_hot_spot).setVisibility(View.GONE);
                    findViewById(R.id.checkboxIsVomiting).setVisibility(View.GONE);
                    findViewById(R.id.checkboxHasHeadache).setVisibility(View.GONE);
                    findViewById(R.id.checkboxWeightLoss).setVisibility(View.GONE);
                    findViewById(R.id.checkbox2weekCough).setVisibility(View.GONE);
                    findViewById(R.id.checkboxfever).setVisibility(View.GONE);
                    findViewById(R.id.checkboxBloodCough).setVisibility(View.GONE);
                    findViewById(R.id.checkboxSevereSweating).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_loss_of_appetite).setVisibility(View.GONE);
                    findViewById(R.id.checkboxIsVomiting).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_symptomps_for_associative_diseases).setVisibility(View.GONE);
                    findViewById(R.id.checkboxhas_affected_partner).setVisibility(View.GONE);
                    findViewById(R.id.checkboxLostFollowup).setVisibility(View.GONE);
//                    tbLayout.setVisibility(View.GONE);
                    CTCLayout.setVisibility(View.GONE);
                    fName = " ";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerFacility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerFacility.setFloatingLabelText("Jina la kliniki aliyoshauriwa kwenda");
                    facilitySelection = i;
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
        spinnerFacility.setSelection(facilitySelection);
        spinnerGender.setSelection(genderSelection);

        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(R.id.reg_dob);
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
                // get picked date
                // update view
                GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
//                if (id == R.id.textDate)
//                    textDate.setText(dateFormat.format(pickedDate.getTimeInMillis()));
                if (id == R.id.reg_dob)
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
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));

        // show dialog
        datePickerDialog.show(this.getFragmentManager(), "DatePickerDialog");
    }


    public boolean isFormSubmissionOk() {
        if (     TextUtils.isEmpty(editTextfName.getText())
                || TextUtils.isEmpty(editTextlName.getText())
                || TextUtils.isEmpty(editTextVillageLeader.getText())
                || TextUtils.isEmpty(editTextDiscountId.getText())
                ) {

            message = "Tafadhali jaza taarifa zote muhimu";
            makeToast();

            return false;

        } else if (spinnerGender.getSelectedItemPosition() <=0) {
            // no radio checked
            message = "Tafadhali chagua jinsia ya mteja";
            makeToast();
            return false;

        } else if (spinnerFacility.getSelectedItemPosition() <= 0) {

            message = "Tafadhali chagua kituo cha kwenda kupewa huduma";
            makeToast();
            return false;

        }else if (spinnerService.getSelectedItemPosition() <= 0 ) {

            message = "Tafadhali chagua aina ya huduma";
            makeToast();
            return false;

        } else if (TextUtils.isEmpty(textPhone.getText())) {
            message = "Tafadhali andika namba ya simu sahihi";
            makeToast();
            return false;
        } else if (TextUtils.isEmpty(editTextKijiji.getText())) {
            message = "Tafadhali jaza mahali anapoishi";
            makeToast();
            return false;
        } else  if (TextUtils.isEmpty(editTextReferralReason.getText())) {
            message = "Tafadhali andika sababu ya rufaa ya mteja";
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
        referral.setFacility_id(getFacilityId(spinnerFacility.getSelectedItem().toString()));
        referral.setGender(spinnerGender.getSelectedItem().toString());
        referral.setVillage_leader(editTextVillageLeader.getText().toString());
        referral.setReferral_reason(editTextReferralReason.getText().toString());
        referral.setReferral_service_id(getReferralServiceId(spinnerService.getSelectedItem().toString()));
//        referral.setProviderMobileNumber(textviewReferralNumber.getText().toString());
        referral.setWard(wardId);
        referral.setService_provider_uiid(((BoreshaAfyaApplication)getApplication()).getCurrentUserID());
        referral.setService_provider_group(((BoreshaAfyaApplication)getApplication()).getTeam_uuid());

        //tb referral
        if(fName.equals("client_tb_referral_form")){
            referral.setCtc_number(editTextCTCNumber.getText().toString());
            referral.setHas_2Week_cough(checkBoxAreasonOne.isChecked());
            referral.setHas_fever(checkBoxreasonTwo.isChecked());
            referral.setHad_weight_loss(checkBoxreasonThree.isChecked());
            referral.setHas_severe_sweating(checkBoxreasonFour.isChecked());
            referral.setHas_blood_cough(checkBoxresonFive.isChecked());
            referral.setIs_lost_follow_up(checkBoxreasonSix.isChecked());
        }

        //for malaria referral
        if(fName.equals("client_malaria_referral_form")){
            referral.setCtc_number(editTextCTCNumber.getText().toString());
            referral.setHas_fever(checkBoxAreasonOne.isChecked());
            referral.setHas_headache(checkBoxreasonTwo.isChecked());
            referral.setHas_severe_sweating(checkBoxreasonThree.isChecked());
            referral.setIs_vomiting(checkBoxreasonFour.isChecked());
            referral.setHas_loss_of_appetite(checkBoxresonFive.isChecked());
//            referral.setIs_lost_follow_up(checkBoxreasonSix.isChecked());
        }

        //for hiv referral
        if(fName.equals("client_hiv_referral_form")){
            referral.setCtc_number(editTextCTCNumber.getText().toString());
            referral.setIs_at_hot_spot(checkBoxAreasonOne.isChecked());
            referral.setHas_fever(checkBoxreasonTwo.isChecked());
            referral.setHad_weight_loss(checkBoxreasonThree.isChecked());
            referral.setHas_symptomps_for_associative_diseases(checkBoxreasonFour.isChecked());
            referral.setHas_affected_partner(checkBoxresonFive.isChecked());
            referral.setIs_lost_follow_up(checkBoxreasonSix.isChecked());
            referral.setCtc_number(editTextCTCNumber.getText().toString());
            referral.setLast_ctc_date(dobTextView.getText().toString());
        }


        Log.d(TAG, "referral 1 ="+ new Gson().toJson(referral));
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

    public String getReferralServiceId(String name){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where name ='"+ name +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

}
