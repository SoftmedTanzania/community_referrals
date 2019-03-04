package com.softmed.htmr_chw.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
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
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.util.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.ei.opensrp.domain.Client;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fr.ganfra.materialspinner.MaterialSpinner;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.softmed.htmr_chw.util.Utils.generateRandomUUIDString;

/**
 * Created by coze on 11/17/17.
 */

public class ClientRegistrationFormActivity extends SecuredNativeSmartRegisterActivity {

    private static final String TAG = ClientRegistrationFormActivity.class.getSimpleName();
    public EditText editTextfName, editTextmName, editTextlName, editTextVillageLeader, editTextCTCNumber,
            editTextDiscountId, editTextKijiji, editTextReferralReason, helperName, helperPhoneNumber;
    public Button button;
    public MaterialSpinner spinnerGender;
    public String message = "";
    public Context context;
    public int genderSelection = -1;
    public Dialog referalDialogue;
    private long dob;
    private Toolbar toolbar;
    private EditText textPhone;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String formName = "client_registration_form";
    private String wardId = "";
    private Client client;
    private Gson gson = new Gson();
    private JSONObject fieldOverides = new JSONObject();
    private ClientRepository clientRepository;
    private MaterialEditText dobTextView;
    private String preferredLocale;
    private Typeface robotoBold, robotoCondenced;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        preferredLocale = allSharedPreferences.fetchLanguagePreference();

        robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        robotoCondenced = Typeface.createFromAsset(getAssets(), "roboto_condensed.ttf");

        setLanguage();

        setContentView(com.softmed.htmr_chw.R.layout.activity_client_registration_form);
        setupviews();

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

        clientRepository = context().clientRepository();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFormSubmissionOk()) {
                    //setting default values
                    client = getClient();
                    client.setStatus(0);
                    client.setClient_id(generateRandomUUIDString());

                    // convert to json
                    String gsonReferral = gson.toJson(client);
                    saveFormSubmission(gsonReferral, client.getClient_id(), formName, getFormFieldsOverrides());
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("status", true);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();

                    Intent intent1 = new Intent(ClientRegistrationFormActivity.this, ClientDetailsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("client", client);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    finish();
                }
            }
        });
    }


    @Override
    public void saveFormSubmission(String formSubmission, final String id, String formName, JSONObject fieldOverrides) {
        // save the form
        final Client client = gson.fromJson(formSubmission, Client.class);
        client.setId(id);
        Log.d(TAG, "values = " + gson.toJson(client));

        clientRepository.add(client);

        Client c = clientRepository.find(id);
        List<FormField> formFields = new ArrayList<>();
        formFields.add(new FormField("id", c.getId(), ClientRepository.TABLE_NAME + "." + "id"));
        formFields.add(new FormField("relationalid", c.getId(), ClientRepository.TABLE_NAME + "." + "relationalid"));

        FormData formData;
        FormInstance formInstance;
        FormSubmission submission;

        Map<String, String> details = new Gson().<Map<String, String>>fromJson(c.getDetails(), new TypeToken<Map<String, String>>() {
        }.getType());

        for (String key : details.keySet()) {
            Log.d(TAG, "key = " + key);
            if (key.equals("facility_id")) {
                FormField f = new FormField(key, details.get(key), "facility.id");
                formFields.add(f);
            } else {
                FormField f = new FormField(key, details.get(key), ClientRepository.TABLE_NAME + "." + key);
                formFields.add(f);
            }
        }
        Log.d(TAG, "form field = " + new Gson().toJson(formFields));
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
        } else if (!TextUtils.isEmpty(textPhone.getText()) && textPhone.getText().toString().length() < 10) {
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

        client.setFacility_id(context().allSettings().fetchANMLocation());
        if (spinnerGender.getSelectedItem().toString().equalsIgnoreCase("female") || spinnerGender.getSelectedItem().toString().contains("ke"))
            client.setGender("Female");
        else
            client.setGender("Male");
        client.setVillage_leader(editTextVillageLeader.getText().toString());
        client.setHelper_name(helperName.getText().toString());
        client.setHelper_phone_number(helperPhoneNumber.getText().toString());
        client.setWard(wardId);
        client.setCtc_number(editTextCTCNumber.getText().toString());

        client.setService_provider_uiid(((BoreshaAfyaApplication) getApplication()).getCurrentUserID());
        return client;
    }

    public JSONObject getFormFieldsOverrides() {
        return fieldOverides;
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
