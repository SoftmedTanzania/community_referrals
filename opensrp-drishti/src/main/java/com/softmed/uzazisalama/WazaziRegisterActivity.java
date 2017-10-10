package com.softmed.uzazisalama;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Repository.CustomMotherRepository;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.util.DatesHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.drishti.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.softmed.uzazisalama.util.Utils.generateRandomUUIDString;

public class WazaziRegisterActivity extends AppCompatActivity {
    private static final String TAG = WazaziRegisterActivity.class.getSimpleName();
    private String id;
    private PregnantMom pregnantMom;
    private TextView textName, textId,
            textAge, textDeliveryDate, textDateKulazwa;
    private CardView cardPickCheckInDate, cardPickDeliveryDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private EditText editTextGravida, editTextMotherStatus, editTextPara, editTextNjiaYaKujifungua,
            editTextBba, editTextDeliveryProblems, editTextChildWeight, editTextApgar, editTextChildProblems;

    private RadioGroup childStatusRadioGroup, typeOfDeadChildRadioGroup;
    private int childStatus = 1, childDeathType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wazazi_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregnantMom.setIs_pnc("true");
                saveData();
            }
        });
        setUpViews();
        final String gsonMom = getIntent().getStringExtra("mom");
        id = getIntent().getStringExtra("id");
        Log.d(TAG, "mom=" + gsonMom);

        if (gsonMom != null) {
            pregnantMom = new Gson().fromJson(gsonMom, PregnantMom.class);
            Log.d(TAG, "id =" + id);
            // set values
            setMotherProfileDetails();
        }
    }

    private void setUpViews() {
        textName = (TextView) findViewById(R.id.textName);
        textAge = (TextView) findViewById(R.id.textAge);
        textId = (TextView) findViewById(R.id.textId);
        textDeliveryDate = (TextView) findViewById(R.id.textDeliveryDate);
        textDateKulazwa = (TextView) findViewById(R.id.textDateKulazwa);
        editTextGravida = (EditText) findViewById(R.id.gravida);
        editTextMotherStatus = (EditText) findViewById(R.id.mothers_status);
        editTextPara = (EditText) findViewById(R.id.para);
        editTextNjiaYaKujifungua = (EditText) findViewById(R.id.njia_ya_kujifungua);
        editTextBba = (EditText) findViewById(R.id.bba);
        editTextDeliveryProblems = (EditText) findViewById(R.id.delivery_problems);
        editTextChildWeight = (EditText) findViewById(R.id.child_weight);
        editTextApgar = (EditText) findViewById(R.id.apgar);
        editTextChildProblems = (EditText) findViewById(R.id.child_problems);
        childStatusRadioGroup = (RadioGroup) findViewById(R.id.child_status_radio_group);
        typeOfDeadChildRadioGroup = (RadioGroup) findViewById(R.id.type_of_dead_child_radio_group);

        cardPickDeliveryDate = (CardView) findViewById(R.id.card_pick_delivery_date);
        cardPickCheckInDate = (CardView) findViewById(R.id.card_pick_check_in_date);

        cardPickDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(R.id.textDeliveryDate);
            }
        });

        cardPickCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(R.id.textDateKulazwa);
            }
        });

        childStatusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.alive) {
                    childStatus = 1;
                    findViewById(R.id.hali_ya_mtoto).setVisibility(View.GONE);
                } else {
                    childStatus = 0;
                    findViewById(R.id.hali_ya_mtoto).setVisibility(View.VISIBLE);
                }
            }
        });
        childStatusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.fbs) {
                    childDeathType = 0;
                } else if (i == R.id.msb) {
                    childDeathType = 1;

                }
            }
        });
    }

    private void setMotherProfileDetails() {
        // todo set all profile details
        textName.setText(pregnantMom.getName());
        textId.setText("Mother ID : " + pregnantMom.getId());
        textAge.setText(String.valueOf(pregnantMom.getAge()) + " years");

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

                ((TextView) findViewById(id)).setText(dateFormat.format(pickedDate.getTimeInMillis()));

            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(this, android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(this, R.color.primary));

        // show dialog
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private void saveData() {
        // save the form

        MotherPersonObject motherPersonObject = new MotherPersonObject(id, null, pregnantMom);
        updateFormSubmission(motherPersonObject, id);


    }

    public void updateFormSubmission(MotherPersonObject motherPersonObject, String id) {


        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);
        Log.d(TAG, "values to be updated =" + new Gson().toJson(values));
        Log.d(TAG, " mother id to be updated =" + id);
        Log.d(TAG, " mother id to be updated =" + motherPersonObject.getId());
        CommonRepository commonRepository = Context.getInstance().updateApplicationContext(this.getApplicationContext()).commonrepository("wazazi_salama_mother");
        commonRepository.customUpdateTable("wazazi_salama_mother", values, motherPersonObject.getId());

        CommonRepository cRepository = Context.getInstance().updateApplicationContext(this.getApplicationContext()).commonrepository("wazazi_salama_mother");

        CommonPersonObject c = cRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();


        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


        formFields.add(new FormField("relationalid", c.getRelationalId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        for (String key : c.getDetails().keySet()) {
            Log.d(TAG, "key = " + key);
            FormField f = null;
            if (!key.equals("FACILITY_ID")) {
                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
            } else {
                f = new FormField(key, c.getDetails().get(key), "facility.id");
            }
            formFields.add(f);
        }

        for (String key : c.getColumnmaps().keySet()) {
            Log.d(TAG, "key = " + key);
            FormField f = null;
            if (!key.equals("FACILITY_ID")) {
                f = new FormField(key, c.getColumnmaps().get(key), commonRepository.TABLE_NAME + "." + key);
            } else {
                f = new FormField(key, c.getColumnmaps().get(key), "facility.id");
            }

            formFields.add(f);


        }
        Log.d(TAG, "fieldes = " + new Gson().toJson(formFields));

        FormData formData = new FormData("wazazi_salama_mother", "/model/instance/Wazazi_Salama_ANC_Registration/", formFields, null);
        FormInstance formInstance = new FormInstance(formData, "1");
        FormSubmission submission = Context.getInstance().updateApplicationContext(this.getApplicationContext()).formDataRepository().fetchFromSubmissionByEntity(motherPersonObject.getId());

        Log.d(TAG, "submission content = " + new Gson().toJson(submission));

        FormSubmission updatedSubmission = new FormSubmission(submission.instanceId(), submission.entityId(), submission.formName(), new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
        Context.getInstance().updateApplicationContext(this.getApplicationContext()).formDataRepository().updateFormSubmission(updatedSubmission);

        Log.d(TAG, "submission content = " + new Gson().toJson(updatedSubmission));
    }
}
