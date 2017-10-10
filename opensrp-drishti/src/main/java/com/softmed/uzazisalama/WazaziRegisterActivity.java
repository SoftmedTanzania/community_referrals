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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.uzazisalama.Application.UzaziSalamaApplication;
import com.softmed.uzazisalama.DataModels.Child;
import com.softmed.uzazisalama.DataModels.PncMother;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Repository.ChildPersonObject;
import com.softmed.uzazisalama.Repository.CustomChildRepository;
import com.softmed.uzazisalama.Repository.CustomMotherRepository;
import com.softmed.uzazisalama.Repository.CustomPncRepository;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.Repository.PncPersonObject;
import com.softmed.uzazisalama.util.DatesHelper;
import com.softmed.uzazisalama.util.Utils;
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
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionModel;
import org.ei.opensrp.view.dialog.OpenFormOption;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.softmed.uzazisalama.util.Utils.generateRandomUUIDString;

public class WazaziRegisterActivity extends SecuredNativeSmartRegisterActivity {
    private static final String TAG = WazaziRegisterActivity.class.getSimpleName();
    private String id;
    public static long addmissionDate, deliveryDate;
    private static String childId;
    private PregnantMom pregnantMom;
    private TextView textName, textId,
            textAge, textDeliveryDate, textDateKulazwa;
    private CardView cardPickCheckInDate, cardPickDeliveryDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private EditText editTextGravida, editTextMotherStatus, editTextPara, editTextNjiaYaKujifungua,
            editTextBba, editTextDeliveryProblems, editTextChildWeight, editTextApgar, editTextChildProblems;

    private RadioGroup childStatusRadioGroup, typeOfDeadChildRadioGroup;
    private Spinner genderSpinner;
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
    @Override
    protected void onInitialization() {
    }
    @Override
    public void startRegistration() {
    }

    @Override
    public void showFragmentDialog(DialogOptionModel dialogOptionModel, Object tag) {
        try {
            LoginActivity.setLanguage();
        } catch (Exception e) {

        }
        super.showFragmentDialog(dialogOptionModel, tag);
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
    protected void setupViews() {
    }

    @Override
    protected void onResumption() {
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }


    public DialogOption[] getEditOptions() {
        return new DialogOption[]{

                new OpenFormOption(getResources().getString(R.string.nbnf), "birthnotificationpregnancystatusfollowup", formController)
        };
    }

    public DialogOption[] getEditOptionsforanc(String visittext, String alertstatus) {
        String ancvisittext = "Not Synced";
        String ancalertstatus = alertstatus;
        ancvisittext = visittext;

        HashMap<String, String> overridemap = new HashMap<String, String>();


        if (ancvisittext.contains("ANC4")) {
            overridemap.put("ANC4_current_formStatus", alertstatus);
            return new DialogOption[]{new OpenFormOption(getResources().getString(R.string.anc4form), "anc_reminder_visit_4", formController, overridemap, OpenFormOption.ByColumnAndByDetails.bydefault)};
        } else if (ancvisittext.contains("ANC3")) {
            Log.v("anc3 form status", alertstatus);
            overridemap.put("ANC3_current_formStatus", alertstatus);
            return new DialogOption[]{new OpenFormOption(getResources().getString(R.string.anc3form), "anc_reminder_visit_3", formController, overridemap, OpenFormOption.ByColumnAndByDetails.bydefault)};
        } else if (ancvisittext.contains("ANC2")) {
            overridemap.put("ANC2_current_formStatus", alertstatus);
            return new DialogOption[]{new OpenFormOption(getResources().getString(R.string.anc2form), "anc_reminder_visit_2", formController, overridemap, OpenFormOption.ByColumnAndByDetails.bydefault)};
        } else if (ancvisittext.contains("ANC1")) {
            Log.v("anc1 form status", alertstatus);
            overridemap.put("anc1_current_formStatus", alertstatus);
            return new DialogOption[]{new OpenFormOption(getResources().getString(R.string.anc1form), "anc_reminder_visit_1", formController, overridemap, OpenFormOption.ByColumnAndByDetails.bydefault)};
        } else {
            return new DialogOption[]{};
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
        genderSpinner = (Spinner) findViewById(R.id.gender);
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
        typeOfDeadChildRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    private String getChild(){
        Child child = new Child();
        child.setCreatedBy(((UzaziSalamaApplication)getApplication()).getCurrentUserID());
        child.setGender(genderSpinner.getSelectedItem().toString());
        child.setProblem(editTextChildProblems.getText().toString());
        child.setApgarScore(editTextApgar.getText().toString());
        if(childStatus == 1)
            child.setStatus("alive");
        else
            child.setStatus("dead");
        child.setWeight(editTextChildWeight.getText().toString());
        String gsonChild = new Gson().toJson(child);
        Log.d(TAG, "new child =" + gsonChild);
        return gsonChild;
    }

    private String getPncMother(){
        PncMother pncMother = new PncMother();
        pncMother.setCreatedBy(((UzaziSalamaApplication)getApplication()).getCurrentUserID());
        pncMother.setAdmissionDate(addmissionDate);
        pncMother.setDeliveryDate(deliveryDate);
        pncMother.setDeliveryComplication(editTextDeliveryProblems.getText().toString());
        pncMother.setDeliveryType(editTextNjiaYaKujifungua.getText().toString());
        pncMother.setBba(editTextBba.getText().toString());
        pncMother.setPara(editTextPara.getText().toString());
        pncMother.setGravida(editTextGravida.getText().toString());
        pncMother.setMother_status(editTextMotherStatus.getText().toString());

        String gsonPncMother = new Gson().toJson(pncMother);
        Log.d(TAG, "new Pnc Mother =" + gsonPncMother);
        return gsonPncMother;
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
                if (id == R.id.textDeliveryDate) {
                    textDeliveryDate.setText(dateFormat.format(pickedDate.getTimeInMillis()));
                    addmissionDate = pickedDate.getTimeInMillis();
                }
                else if (id == R.id.textDateKulazwa) {
                    textDateKulazwa.setText(dateFormat.format(pickedDate.getTimeInMillis()));
                    deliveryDate = pickedDate.getTimeInMillis();
                }


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
        Log.d(TAG, "pnc mother id =" + id);

        //updating mother information
        MotherPersonObject motherPersonObject = new MotherPersonObject(id, null, pregnantMom);
        updateFormSubmission(motherPersonObject, id);



        //creating a new born child
        childId = UUID.randomUUID().toString();
        //todo martha fix error in saving child details
//        saveChildFormSubmission(getChild(),childId );
        //registering delivery information about a mother
        savePNCFormSubmission(getPncMother(),UUID.randomUUID().toString(),childId,id );





    }

    public void saveChildFormSubmission(String formSubmission, String id) {
        // save the form
        final Child child = new Gson().fromJson(formSubmission, Child.class);

        ChildPersonObject childPersonObject = new ChildPersonObject(id, id, child );
        ContentValues values = new CustomChildRepository().createValuesFor(childPersonObject);
        Log.d(TAG, "childPersonObject = " + new Gson().toJson(childPersonObject));
        Log.d(TAG, "values = " + new Gson().toJson(values));

        CommonRepository commonRepository = context().commonrepository("child");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();


        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


        formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        for ( String key : c.getDetails().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);

            formFields.add(f);
        }

        for ( String key : c.getColumnmaps().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);

            formFields.add(f);

        }

        Log.d(TAG,"form field = "+ new Gson().toJson(formFields));

        FormData formData = new FormData("child","/model/instance/Child/",formFields,null);
        FormInstance formInstance = new FormInstance(formData,"1");
        FormSubmission submission = new FormSubmission(generateRandomUUIDString(),id,"child",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
        context().formDataRepository().saveFormSubmission(submission);

        Log.d(TAG,"submission content = "+ new Gson().toJson(submission));

//        TODO finish this better implementation for saving data to the database
//        FormSubmission formSubmission1 = null;
//        try {
//            formSubmission1 = FormUtils.getInstance(getApplicationContext()).generateFormSubmisionFromJSONString(id,new Gson().toJson(pregnantMom),"wazazi_salama_pregnant_mothers_registration",fieldOverrides);
//            Log.d(TAG,"form submission generated successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            context().ziggyService().saveForm(getParams(formSubmission1), formSubmission1.instance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void savePNCFormSubmission(String formSubmission, String id, String childId, String motherid) {
        // save the form
        final PncMother pncMother = new Gson().fromJson(formSubmission, PncMother.class);

        PncPersonObject pncPersonObject = new PncPersonObject(id,childId, motherid, pncMother);
        ContentValues values = new CustomPncRepository().createValuesFor(pncPersonObject);
        Log.d(TAG, "pncPersonObject = " + new Gson().toJson(pncPersonObject));
        Log.d(TAG, "values = " + new Gson().toJson(values));

        CommonRepository commonRepository = context().commonrepository("uzazi_salama_pnc");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();


        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


        formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        for ( String key : c.getDetails().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            if(key.equals("childCaseId")) {
                f = new FormField(key, c.getDetails().get(key), "child.id");
            }else if(key.equals("motherCaseId")) {
                f = new FormField(key, c.getDetails().get(key), "wazazi_salama_mother.id");
            }else{
                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
            }
            formFields.add(f);
        }

        for ( String key : c.getColumnmaps().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            if(key.equals("childCaseId")) {
                f = new FormField(key, c.getDetails().get(key), "child.id");
            }else if(key.equals("motherCaseId")) {
                f = new FormField(key, c.getDetails().get(key), "wazazi_salama_mother.id");
            }else{
                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
            }

            formFields.add(f);


        }

        Log.d(TAG,"form field = "+ new Gson().toJson(formFields));

        FormData formData = new FormData("uzazi_salama_pnc","/model/instance/Wazazi_Salama_PNC_Registration/",formFields,null);
        FormInstance formInstance = new FormInstance(formData,"1");
        FormSubmission submission = new FormSubmission(generateRandomUUIDString(),id,"uzazi_salama_pnc",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
        context().formDataRepository().saveFormSubmission(submission);

        Log.d(TAG,"submission content = "+ new Gson().toJson(submission));


//        TODO finish this better implementation for saving data to the database
//        FormSubmission formSubmission1 = null;
//        try {
//            formSubmission1 = FormUtils.getInstance(getApplicationContext()).generateFormSubmisionFromJSONString(id,new Gson().toJson(pregnantMom),"wazazi_salama_pregnant_mothers_registration",fieldOverrides);
//            Log.d(TAG,"form submission generated successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            context().ziggyService().saveForm(getParams(formSubmission1), formSubmission1.instance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        new  com.softmed.uzazisalama.util.AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                if(!pregnantMom.getPhone().equals(""))
                    Utils.sendRegistrationAlert(pregnantMom.getPhone());
                return null;
            }
        }.execute();

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
