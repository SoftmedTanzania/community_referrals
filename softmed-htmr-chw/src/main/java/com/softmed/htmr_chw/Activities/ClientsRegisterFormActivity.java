package com.softmed.htmr_chw.Activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Client;
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
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.repository.IndicatorRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.softmed.htmr_chw.util.Utils.generateRandomUUIDString;
import static org.ei.opensrp.AllConstants.ENGLISH_LOCALE;

/**
 * Created by coze on 11/17/17.
 */

public class ClientsRegisterFormActivity extends SecuredNativeSmartRegisterActivity {

    private Toolbar toolbar;
    private static final String TAG = ClientsRegisterFormActivity.class.getSimpleName();
    public static EditText editTextfName, editTextmName, editTextlName, editTextVillageLeader, editTextCTCNumber,
            editTextDiscountId, editTextKijiji, editTextReferralReason,helperName,helperPhoneNumber;
    public static Button button;
    public static MaterialSpinner spinnerGender;
    private EditText textPhone;
    private List<String> facilityList = new ArrayList<String>();
    private List<String> serviceList = new ArrayList<String>();
    public String message = "";
    public static Context context;
    public static int genderSelection = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String formName = "client_referral_form";
    private String wardId = "";
    private Client client;
    private Gson gson = new Gson();
    private JSONObject fieldOverides = new JSONObject();
    private CommonRepository commonRepository;
    private IndicatorRepository indicatorRepository;
    private Cursor cursor;
    private MaterialEditText dobTextView;
    private List<ReferralServiceObject> referralServiceList;
    private List<FacilityObject> facilitiesList;
    public Dialog referalDialogue;
    private String preferredLocale;
    private Typeface robotoBold, robotoCondenced;
    long dob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        preferredLocale = allSharedPreferences.fetchLanguagePreference();

        robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        robotoCondenced = Typeface.createFromAsset(getAssets(), "roboto_condensed.ttf");

        setLanguage();

        setContentView(com.softmed.htmr_chw.R.layout.activity_client_registration_form);
        setReferralServiceList();
        setFacilistList();
        setupviews();

        indicatorRepository = context().indicatorRepository();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = (Toolbar) findViewById(com.softmed.htmr_chw.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            wardId = null;
        } else {
            wardId = bundle.getString("selectedLocation");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormSubmissionOk()) {
                    Log.d(TAG,"button clicked");

                    //setting default values
                    client = getClient();
                    client.setStatus(0);

                    // convert to json
                    String gsonReferral = gson.toJson(client);
                    saveFormSubmission(gsonReferral, generateRandomUUIDString(), formName, getFormFieldsOverrides());
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("status", true);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();

                    Intent intent1 = new Intent(ClientsRegisterFormActivity.this, ClientDetails.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("client", client);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });
    }


    @Override
    public void saveFormSubmission(String formSubmission, final String id, String formName, JSONObject fieldOverrides) {
        // save the form
        final Client client = gson.fromJson(formSubmission, Client.class);
        client.setId(id);
        ContentValues values = new ClientRepository().createValuesFor(client);
        Log.d(TAG, "values = " + gson.toJson(values));

        commonRepository = context().commonrepository("client");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();

        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));
        formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        FormData formData;
        FormInstance formInstance;
        FormSubmission submission;

        for (String key : c.getDetails().keySet()) {
            Log.d(TAG, "key = " + key);
            if (key.equals("facility_id")) {
                FormField f = new FormField(key, c.getDetails().get(key), "facility.id");
                formFields.add(f);
            } else {
                FormField f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
                formFields.add(f);
            }
        }
        Log.d(TAG, "form field = " + new Gson().toJson(formFields));
        Log.d(TAG, "am in tb");
        formData = new FormData("client", "/model/instance/client_registration_form/", formFields, null);
        formInstance = new FormInstance(formData, "1");
        submission = new FormSubmission(generateRandomUUIDString(), id, "client_registration_form", new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
        context().formDataRepository().saveFormSubmission(submission);

        new com.softmed.htmr_chw.util.AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!client.getPhone_number().equals(""))
                    Utils.sendRegistrationAlert(client.getPhone_number());
                return null;
            }
        }.execute();


    }

    private void setupviews() {

        ((TextView) findViewById(com.softmed.htmr_chw.R.id.form_heading)).setTypeface(robotoBold);
        ((TextView) findViewById(com.softmed.htmr_chw.R.id.initial_information_title)).setTypeface(robotoBold);

        textPhone = (EditText) findViewById(com.softmed.htmr_chw.R.id.edittextPhone);
        dobTextView = (MaterialEditText) findViewById(com.softmed.htmr_chw.R.id.reg_dob);

        editTextfName = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextfName);
        editTextmName = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextmName);
        editTextlName = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextlName);
        editTextReferralReason = (EditText) findViewById(com.softmed.htmr_chw.R.id.reason_for_referral);
        editTextVillageLeader = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextVillageLeader);

        helperName = (EditText) findViewById(R.id.helper_name);
        helperPhoneNumber = (EditText) findViewById(R.id.helper_phone_number);


        Log.d(TAG, "username" + ((BoreshaAfyaApplication) getApplication()).getUsername());
        Log.d(TAG, "team name " + ((BoreshaAfyaApplication) getApplication()).getTeam_name());

        editTextDiscountId = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextDiscountId);
        editTextKijiji = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextKijiji);
        editTextCTCNumber = (EditText) findViewById(com.softmed.htmr_chw.R.id.editTextOthers);

        button = (Button) findViewById(com.softmed.htmr_chw.R.id.referal_button);


        String[] ITEMS = getResources().getStringArray(com.softmed.htmr_chw.R.array.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender = (MaterialSpinner) findViewById(com.softmed.htmr_chw.R.id.spinnerGender);
        spinnerGender.setAdapter(adapter);


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

        spinnerGender.setSelection(genderSelection);

        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(com.softmed.htmr_chw.R.id.reg_dob);
            }
        });

    }

    private void setReferralServiceList() {
        commonRepository = context().commonrepository("referral_service");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service WHERE category <> 'other' ");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "serviceList = " + gson.toJson(commonPersonObjectList));

        this.referralServiceList = Utils.convertToServiceObjectList(commonPersonObjectList);
        int size = referralServiceList.size();

        for (int i = 0; size > i; i++) {
            Log.d(TAG, "service category : " + referralServiceList.get(i).getCategory());
            if (preferredLocale.equals(ENGLISH_LOCALE))
                serviceList.add(referralServiceList.get(i).getName());
            else {
                serviceList.add(referralServiceList.get(i).getNameSw());
            }
        }
        Log.d(TAG, "service list" + serviceList.toString());
    }


    private void setFacilistList() {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility");

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Log.d(TAG, "facility Name = " + cursor.getString(cursor.getColumnIndex("name")));
        }

        List<CommonPersonObject> commonPersonObjectList2 = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList2));

        this.facilitiesList = Utils.convertToFacilityObjectList(commonPersonObjectList2);
        int size2 = facilitiesList.size();

        for (int i = 0; size2 > i; i++) {
            facilityList.add(facilitiesList.get(i).getName());
        }
        Log.d(TAG, "facility list" + facilityList.toString());
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
                if (id == com.softmed.htmr_chw.R.id.reg_dob) {
                    dob = pickedDate.getTimeInMillis();
                    dobTextView.setText(dateFormat.format(pickedDate.getTimeInMillis()));
                }
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
        Log.d(TAG, "selected gender position = " + spinnerGender.getSelectedItemPosition());
        Log.d(TAG, "valid phone number  = " + validCellPhone(textPhone.getText().toString()));


        if (TextUtils.isEmpty(editTextfName.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.unfilled_information);
            editTextfName.setError(message);
            makeToast();
            return false;
        } else if (TextUtils.isEmpty(editTextlName.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.unfilled_information);
            editTextlName.setError(message);
            makeToast();
            return false;
        } else if (spinnerGender.getSelectedItemPosition() == 0) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_gender);
            spinnerGender.setError(message);
            makeToast();
            return false;

        } else if (TextUtils.isEmpty(editTextKijiji.getText())) {
            message = getResources().getString(com.softmed.htmr_chw.R.string.missing_physical_address);
            editTextKijiji.setError(message);
            makeToast();
            return false;
        }  else if (!TextUtils.isEmpty(textPhone.getText()) && textPhone.getText().toString().length() < 10) {
            message = getResources().getString(R.string.incorrect_phone_number);
            textPhone.setError(message);
            makeToast();
            return false;
        } else if (!TextUtils.isEmpty(textPhone.getText()) && !validCellPhone(textPhone.getText().toString())) {
            message = getResources().getString(R.string.incorrect_phone_number);
            textPhone.setError(message);
            makeToast();
            return false;
        } else
            return true;
    }

    public boolean validCellPhone(String number) {
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();

    }

    public Client getClient() {
        Client client = new Client();
        client.setCommunity_based_hiv_service(editTextDiscountId.getText().toString());
        client.setFirst_name(editTextfName.getText().toString());
        client.setMiddle_name(editTextmName.getText().toString());
        client.setSurname(editTextlName.getText().toString());
        client.setVillage(editTextKijiji.getText().toString());
        client.setIs_valid("true");
        client.setPhone_number(textPhone.getText().toString());

        //TODO fix this with CHW health facility
        client.setFacility_id("1");
        if (spinnerGender.getSelectedItem().toString().equalsIgnoreCase("female") || spinnerGender.getSelectedItem().toString().contains("ke"))
            client.setGender("Female");
        else
            client.setGender("Male");
        client.setVillage_leader(editTextVillageLeader.getText().toString());
        client.setHelper_name(helperName.getText().toString());
        client.setHelper_phone_number(helperPhoneNumber.getText().toString());
        client.setWard(wardId);
        client.setCtc_number(editTextCTCNumber.getText().toString());

        //client.setService_provider_uiid(((BoreshaAfyaApplication) getApplication()).getCurrentUserID());
        return client;
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public String getFacilityId(String name) {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where name ='" + name + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));
        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    private void setLanguage() {
        android.util.Log.d(TAG, "set Locale : " + preferredLocale);

        Resources res = org.ei.opensrp.Context.getInstance().applicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }

}
