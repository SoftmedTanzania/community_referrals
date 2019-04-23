package com.softmed.htmr_chw_staging.Activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw_staging.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw_staging.R;
import com.softmed.htmr_chw_staging.Domain.FacilityObject;
import com.softmed.htmr_chw_staging.Domain.ReferralServiceObject;
import com.softmed.htmr_chw_staging.util.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Referral;
import org.ei.opensrp.domain.Indicator;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ReferralRepository;
import org.ei.opensrp.repository.IndicatorRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.softmed.htmr_chw_staging.util.Utils.generateRandomUUIDString;
import static org.ei.opensrp.AllConstants.ENGLISH_LOCALE;

/**
 * Created by coze on 11/17/17.
 */

public class ReferralRegistrationFormActivity extends SecuredNativeSmartRegisterActivity {

    private static final String TAG = ReferralRegistrationFormActivity.class.getSimpleName();
    public AutoCompleteTextView facilitytextView;
    public EditText editTextReferralReason;
    public Button button;
    public MaterialSpinner spinnerService;
    public int clientServiceSelection = -1;
    public String message = "";
    public Dialog referalDialogue;
    public String categoryValue;
    private Toolbar toolbar;
    private ArrayAdapter<String> serviceAdapter;
    private ArrayAdapter<String> facilityAdapter;
    private Calendar today;
    private long appointmentDate, defaultAppointmentDate;
    private LinearLayout parentLayout;
    private List<String> facilityList = new ArrayList<String>();
    private List<String> serviceList = new ArrayList<String>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String formName = "referral_form";
    private Referral referral;
    private Gson gson = new Gson();
    private JSONObject fieldOverides = new JSONObject();
    private ReferralRepository referralRepository;
    private IndicatorRepository indicatorRepository;
    private Cursor cursor;
    private MaterialEditText appointmentDateTextView;
    private List<ReferralServiceObject> referralServiceList;
    private List<FacilityObject> facilitiesList;
    private String preferredLocale;
    private Typeface robotoBold;
    private TextView clientName;
    private boolean is_emergency = false;
    private CommonRepository commonRepository;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getIntent().getExtras();
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        preferredLocale = allSharedPreferences.fetchLanguagePreference();

        robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");

        setLanguage();

        setContentView(R.layout.activity_referral_registration_form);
        setReferralServiceList();
        setFacilistList();
        setupviews();

        clientName.setText(bundle.getString("clientName"));

        indicatorRepository = context().indicatorRepository();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referalDialogue = new Dialog(this);
        referalDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);

        today = Calendar.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormSubmissionOk()) {
                    //setting default values
                    referral = getReferral();
                    referral.setReferral_status("0");

                    // convert to json
                    String gsonReferral = gson.toJson(referral);

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
        final Referral referral = gson.fromJson(formSubmission, Referral.class);
        referral.setId(id);

        referralRepository = context().referralRepository();
        referralRepository.add(referral);


        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("id", referral.getId(), ReferralRepository.TABLE_NAME + "." + "id"));
        formFields.add(new FormField("relationalid", referral.getClient_id(), ReferralRepository.TABLE_NAME + "." + "relationalid"));

        FormData formData;
        FormInstance formInstance;
        FormSubmission submission;

        String detailsString = new Gson().toJson(referral);

        Map<String, String> details = new Gson().<Map<String, String>>fromJson(detailsString, new TypeToken<Map<String, String>>() {
        }.getType());

        for (String key : details.keySet()) {
            Log.d(TAG, "key = " + key);
            FormField f = new FormField(key, details.get(key), ReferralRepository.TABLE_NAME + "." + key);
            formFields.add(f);
        }

        Log.d(TAG, "form field = " + new Gson().toJson(formFields));
        Log.d(TAG, "am in tb");
        formData = new FormData("referral", "/model/instance/referral_form/", formFields, null);
        formInstance = new FormInstance(formData, "1");
        submission = new FormSubmission(generateRandomUUIDString(), id, "referral_form", new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
        context().formDataRepository().saveFormSubmission(submission);


    }

    private void setupviews() {
        ((TextView) findViewById(R.id.client_name)).setTypeface(robotoBold);
        ((TextView) findViewById(R.id.referer_title)).setTypeface(robotoBold);
        ((TextView) findViewById(R.id.flags_title)).setTypeface(robotoBold);
        ((TextView) findViewById(R.id.facility_titleview)).setTypeface(robotoBold);

        Switch aSwitch = (Switch) findViewById(R.id.emergency_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_emergency = b;
                if (b) {
                    findViewById(R.id.appointment_date).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.appointment_date).setVisibility(View.VISIBLE);
                }
            }
        });

        clientName = (TextView) findViewById(R.id.client_name);
        appointmentDateTextView = (MaterialEditText) findViewById(R.id.appointment_date);
        editTextReferralReason = (EditText) findViewById(R.id.reason_for_referral);
        button = (Button) findViewById(R.id.referal_button);
        serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceList);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService = (MaterialSpinner) findViewById(R.id.spinnerService);
        spinnerService.setAdapter(serviceAdapter);
        facilityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, facilityList);
        facilitytextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_facility);
        facilitytextView.setThreshold(1);
        facilitytextView.setAdapter(facilityAdapter);

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerService.setFloatingLabelText("Aina za Huduma");
                    clientServiceSelection = i;
                }
                String service = "";
                try {
                    service = spinnerService.getSelectedItem().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Indicator> indicator = new ArrayList<Indicator>();
                if (service.equals(getResources().getString(R.string.referral_services))) {

                } else if (!service.equals("")) {
                    parentLayout = (LinearLayout) findViewById(R.id.check_add_layout);
                    categoryValue = getCategory(service);
                    if (categoryValue.equalsIgnoreCase("malaria")) {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        defaultAppointmentDate = c.getTimeInMillis();
                        editTextReferralReason.setVisibility(View.VISIBLE);
                    } else {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, 3);
                        defaultAppointmentDate = c.getTimeInMillis();
                        editTextReferralReason.setVisibility(View.VISIBLE);
                    }
                    indicator = getIndicator(getReferralServiceId(service));
                    parentLayout.removeAllViewsInLayout();
                }

                int size = indicator.size();
                for (int k = 0; k < size; k++) {
                    CheckBox checkBox = new CheckBox(getApplicationContext());
                    checkBox.setId(k);
                    checkBox.setPadding(0, 0, 0, 0);
                    checkBox.setTextColor(getResources().getColor(R.color.secondary_text));
                    if (preferredLocale.equals(ENGLISH_LOCALE))
                        checkBox.setText(indicator.get(k).getIndicatorName());
                    else
                        checkBox.setText(indicator.get(k).getIndicatorNameSw());

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

        spinnerService.setSelection(clientServiceSelection);
        appointmentDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(R.id.appointment_date);
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
                if (id == R.id.appointment_date) {
                    appointmentDate = pickedDate.getTimeInMillis();
                    appointmentDateTextView.setText(dateFormat.format(pickedDate.getTimeInMillis()));
                }
            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        // show dialog
        datePickerDialog.show(this.getFragmentManager(), "DatePickerDialog");
    }


    public boolean isFormSubmissionOk() {

        if (TextUtils.isEmpty(facilitytextView.getText())) {
            message = getResources().getString(R.string.missing_facility);
            facilitytextView.setError(message);
            makeToast();
            return false;

        } else if (spinnerService.getSelectedItemPosition() == 0) {
            message = getResources().getString(R.string.missing_services);
            spinnerService.setError(message);
            makeToast();
            return false;

        } else if (TextUtils.isEmpty(editTextReferralReason.getText())) {
            message = getResources().getString(R.string.missing_missing_referral_reason);
            makeToast();
            return false;
        } else if (!TextUtils.isEmpty(facilitytextView.getText())) {
            String facilityName = facilitytextView.getText().toString();
            Log.d(TAG, "facility name = " + facilityName);
            facilityName = facilityName.trim();

            int index = -1;
            for (int i = 0; i < facilityList.size(); i++) {
                Log.d(TAG, "facility name at index   " + i + " = " + facilityList.get(i));
                if (facilityList.get(i).equalsIgnoreCase(facilityName))
                    index = i;
            }

            Log.d(TAG, "facility name index = " + index);
            if (index < 0) {
                message = getResources().getString(R.string.wrong_facility);
                facilitytextView.setError(message);
                makeToast();
                return false;
            } else {
                return true;
            }

        } else
            return true;
    }


    public Referral getReferral() {
        Referral referral = new Referral();
        referral.setReferral_date(today.getTimeInMillis());

        if (is_emergency) {
            referral.setAppointment_date(today.getTimeInMillis());
        } else if (appointmentDate == 0) {
            referral.setAppointment_date(defaultAppointmentDate);
        } else {
            Log.d(TAG,"Setting custom appointmentDate");
            referral.setAppointment_date(appointmentDate);
        }
        referral.setIs_emergency(is_emergency+"");
        referral.setIs_valid("true");
        referral.setFacility_id(getFacilityId(facilitytextView.getText().toString()));
        referral.setReferral_reason(editTextReferralReason.getText().toString());
        referral.setReferral_service_id(getReferralServiceId(spinnerService.getSelectedItem().toString()));
        referral.setServices_given_to_patient("");
        referral.setOther_notes("");
        referral.setClient_id(bundle.getString("clientId"));
        referral.setService_provider_uiid(((BoreshaAfyaApplication)getApplication()).getCurrentUserID());
        referral.setReferral_type(1);

        List<String> indicatorIds = new ArrayList<String>();
        for(int i=0; i<parentLayout.getChildCount(); i++) {
            CheckBox nextChild = (CheckBox) parentLayout.getChildAt(i);
            if (nextChild.isChecked()) {
                CheckBox check = nextChild;
                if (check.isChecked()) {
                    indicatorIds.add(getIndicatorId(check.getText().toString()));
                }
            }
        }
        referral.setIndicator_ids(new Gson().toJson(indicatorIds));
        return referral;
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
    }

    public String getFacilityId(String name) {

        cursor = commonRepository.customQuery("select * FROM facility where name = ?",new String[] { name });
//        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where name ='" + name + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));
        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    public List<Indicator> getIndicator(String id) {
        cursor = indicatorRepository.RawCustomQueryForAdapter("select * FROM indicator where referralIndicatorId ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "indicator list = " + gson.toJson(commonPersonObjectList));
        List<Indicator> indicator = Utils.convertToIndicatorList(commonPersonObjectList);
        return indicator;
    }

    public String getIndicatorId(String name) {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where indicatorName ='" + name + "' OR indicatorNameSw = '" + name + "' ");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("referralServiceIndicatorId");
    }

    public String getReferralServiceId(String name) {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where name ='" + name + "' OR name_sw ='" + name + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("id");
    }

    public String getCategory(String name) {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where name ='" + name + "' OR name_sw ='" + name + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("category");
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    private void setLanguage() {
        Log.d(TAG, "set Locale : " + preferredLocale);

        Resources res = org.ei.opensrp.Context.getInstance().applicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }

}
