package org.ei.opensrp.drishti;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;
import org.ei.opensrp.drishti.DataModels.FollowUp;
import org.ei.opensrp.drishti.Fragments.FollowupClientsFragment;
import org.ei.opensrp.drishti.Fragments.CHWSmartRegisterFragment;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.Repository.FollowUpPersonObject;
import org.ei.opensrp.drishti.Repository.FollowUpRepository;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.pageradapter.BaseRegisterActivityPagerAdapter;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ClientReferralRepository;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.util.FormUtils;
import org.ei.opensrp.view.activity.*;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.controller.VillageController;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionMapper;
import org.ei.opensrp.view.dialog.DialogOptionModel;
import org.ei.opensrp.view.dialog.EditOption;
import org.ei.opensrp.view.dialog.OpenFormOption;
import org.ei.opensrp.view.viewpager.OpenSRPViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.ei.opensrp.drishti.util.Utils.generateRandomUUIDString;

public class ChwSmartRegisterActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    private static final String TAG = ChwSmartRegisterActivity.class.getSimpleName();
    private JSONObject fieldOverides = new JSONObject();
    @Bind(R.id.view_pager)
    public    OpenSRPViewPager mPager;
    private FragmentPagerAdapter mPagerAdapter;
    private int currentPage;
    private String[] formNames = new String[]{};
    private Fragment mBaseFragment;
    private Gson gson = new Gson();
    private CommonRepository commonRepository;
    private Cursor cursor;
    private static final String TABLE_NAME = "client_referral";
    private String wardId =null;
    public static MaterialSpinner spinnerReason,spinnerClientAvailable;
    public static int availableSelection = -1,reasonSelection = -1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    static final String DATABASE_NAME = "drishti.db";
    private SecuredActivity securedActivity;
    private LinearLayout flags_layout;
    String message ="";
    Calendar today = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // orientation
//        OrientationHelper.setProperOrientationForDevice(ChwSmartRegisterActivity.this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        securedActivity = new SecuredActivity() {
            @Override
            protected void onCreation() {

            }

            @Override
            protected void onResumption() {

            }
        };
        formNames = this.buildFormNameList();
        mBaseFragment = new CHWSmartRegisterFragment();

        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new BaseRegisterActivityPagerAdapter(getSupportFragmentManager(), formNames, mBaseFragment);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                // onPageChanged(position);
            }
        });
        mPager.setOffscreenPageLimit(formNames.length);

        mPager.setCurrentItem(3);
        currentPage = 3;
        initialize();
        setValuesInBoreshaAfya();
        Log.d(TAG, "table columns ="+new Gson().toJson(context().commonrepository("referral_service").common_TABLE_COLUMNS));


    }

    private void initialize() {

        LoginActivity.setLanguage();
    }


    public void showPreRegistrationDetailsDialog(ClientReferralPersonObject clientReferralPersonObject) {

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_details, null);
        String gsonClient = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        ClientReferral clientReferral = new Gson().fromJson(gsonClient,ClientReferral.class);

        Log.d(TAG,"clientobject ="+ gson.toJson(clientReferralPersonObject));
        Log.d(TAG,"clientobject 2="+gsonClient);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(800,600);

        Button cancel = (Button) dialogView.findViewById(R.id.ok_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        String reg_date = dateFormat.format(clientReferral.getDate_of_birth());
        String ageS="";
        try {
            Date d = dateFormat.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);
            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();



        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textName = (TextView) dialogView.findViewById(R.id.client_name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.agevalue);
        TextView cbhs = (TextView) dialogView.findViewById(R.id.cbhs_number_value);
        TextView referral_service = (TextView) dialogView.findViewById(R.id.viewService);
        TextView facility = (TextView) dialogView.findViewById(R.id.viewFacility);
        TextView ctc_number = (TextView) dialogView.findViewById(R.id.ctc_number);
        TextView referral_reason = (TextView) dialogView.findViewById(R.id.reason_for_referral);
        TextView gender = (TextView) dialogView.findViewById(R.id.gendervalue);
        TextView phoneNumber = (TextView) dialogView.findViewById(R.id.viewPhone);
        TextView physicalAddress = (TextView) dialogView.findViewById(R.id.editTextKijiji);
        TextView villageleader = (TextView) dialogView.findViewById(R.id.viewVillageLeader);


        textName.setText(clientReferralPersonObject.getFirst_name() +" "+clientReferralPersonObject.getMiddle_name()+" "+clientReferralPersonObject.getSurname());
//
        textAge.setText(ageS + " years");
        cbhs.setText(clientReferral.getCommunity_based_hiv_service());
        referral_service.setText(getReferralServiceName(clientReferralPersonObject.getReferral_service_id()));
        facility.setText(getFacilityName(clientReferralPersonObject.getFacility_id()));
        if(!clientReferralPersonObject.getCtc_number().isEmpty())
            ctc_number.setText(clientReferralPersonObject.getCtc_number());
        else
            ctc_number.setText("-");
        referral_reason.setText(clientReferralPersonObject.getReferral_reason());
        phoneNumber.setText(clientReferral.getPhone_number());
        villageleader.setText(clientReferral.getVillage_leader());
        physicalAddress.setText(clientReferral.getVillage());
        if((clientReferral.getGender()).equals("1")){
            gender.setText(getResources().getString(R.string.female));
        }
        else     {
            gender.setText(getResources().getString(R.string.male));
        }
        setIndicators(dialogView,gsonClient);
    }

    public void showPreRegistrationVisitDialog(final ClientReferralPersonObject clientReferralPersonObject)
    {

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_visit_details, null);
        String[] ITEMS = {"Amehama", "Amefariki","Ameenda kituo kingine","Sababu nyinginezo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason = (MaterialSpinner) dialogView.findViewById(R.id.spinnerReason);
        spinnerReason.setAdapter(adapter);

        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerReason.setFloatingLabelText("Chagua sababu ya kutokwenda kliniki");
                    reasonSelection = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        spinnerReason.setSelection(reasonSelection);

        String gsonClient = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        Log.d(TAG, "gsonMom = " + gsonClient);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        Button button_yes = (Button) dialogView.findViewById(R.id.tuma_button);
        Button button_no = (Button) dialogView.findViewById(R.id.cancel_button);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerReason.getSelectedItemPosition() <=0) {
                    // no radio checked
                    message = "Tafadhali chagua sababu ya mteja kutokwenda kliniki";
                    makeToast();
                }else {

                    ClientReferral clientReferral = new Gson().fromJson(clientReferralPersonObject.getDetails(), ClientReferral.class);

                    if (spinnerReason.getSelectedItem().toString().equals("Amehama") || spinnerReason.getSelectedItem().toString().equals("Amefariki"))
                        clientReferral.setIs_valid("false");

                    clientReferral.setReferral_feedback(spinnerReason.getSelectedItem().toString());
                    Toast.makeText(ChwSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + clientReferralPersonObject.getFirst_name() + " " + clientReferralPersonObject.getMiddle_name() + " " + clientReferralPersonObject.getSurname(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView textName = (TextView) dialogView.findViewById(R.id.patient_name);
        textName.setText(clientReferralPersonObject.getFirst_name()+" "+clientReferralPersonObject.getMiddle_name()+" "+ clientReferralPersonObject.getSurname());

        TextView facility = (TextView) dialog.findViewById(R.id.textview_facility);
        facility.setText(getFacilityName(clientReferralPersonObject.getFacility_id()));

        TextView service = (TextView) dialog.findViewById(R.id.textview_referral);
        service.setText(getReferralServiceName(clientReferralPersonObject.getReferral_service_id()));

        TextView referral_reason = (TextView) dialog.findViewById(R.id.textview_service);
        referral_reason.setText(clientReferralPersonObject.getReferral_reason());
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    public String getFacilityName(String id){

        commonRepository = context().commonrepository("facility");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='"+ id +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    public String getReferralServiceName(String id){

        commonRepository = context().commonrepository("referral_service");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where id ='"+ id +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    public void setIndicators(View view,String object) {
        try{


            JSONObject jsonObj = new JSONObject(object);
            Log.d(TAG,"jason indicators "+jsonObj.get("indicator_ids"));
            String list = jsonObj.getString("indicator_ids").toString();
            Log.d(TAG,"list"+list);
            list = list.replace("[","");
            list = list.replace("\"","");
            list = list.replace("]","");
            Log.d(TAG,"list"+list);
            List<String> myList = new ArrayList<String>(Arrays.asList(list.split(",")));
            Log.d(TAG,"inidcators list "+myList.get(0)+"size "+myList.size());
            flags_layout = (LinearLayout) view.findViewById(R.id.flags_layout);
            flags_layout.removeAllViewsInLayout();
            for(int m =0; m< myList.size(); m++){
                final TextView rowTextView = new TextView(this);

                // set some properties of rowTextView or something
                rowTextView.setText(getIndicatorName(myList.get(m)));
                rowTextView.setPadding(10,10,10,0);
                // add the textview to the linearlayout
                flags_layout.addView(rowTextView);
            }
        }catch (JSONException e){

        }

    }

    public String getIndicatorName(String id){
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where referralServiceIndicatorId ='"+ id +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("indicatorName");
    }

    public void showFollowUpFormDialog(final ClientReferralPersonObject clientperson) {

        String gsonClient = Utils.convertStandardJSONString(clientperson.getDetails());
        Log.d(TAG, "gsonMom = " + gsonClient);

        final FollowUp referral = new Gson().fromJson(gsonClient,FollowUp.class);

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_visit_details, null);
        final View view =  dialogView.findViewById(R.id.textview2);
        final EditText client_condition = (EditText)dialogView.findViewById(R.id.client_status);
        String[] ITEMS = {"Amehama mji", "Amefariki","Ameenda kituo kingine","Sababu nyinginezo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason = (MaterialSpinner) dialogView.findViewById(R.id.spinnerReason);
        spinnerReason.setAdapter(adapter);

        String[] options = {"ndio", "hapana"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientAvailable = (MaterialSpinner) dialogView.findViewById(R.id.spinnerClientAvailable);
        spinnerClientAvailable.setAdapter(adapter1);

        spinnerClientAvailable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerClientAvailable.setFloatingLabelText("Umemkutana na  mteja?");
                    availableSelection = i;
                }

                if(spinnerClientAvailable.getSelectedItem().toString().equals("ndio")){
                    spinnerReason.setVisibility(VISIBLE);
                    client_condition.setVisibility(VISIBLE);
                    view.setVisibility(View.GONE);
                }else{
                    spinnerReason.setVisibility(View.GONE);
                    client_condition.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    spinnerReason.setFloatingLabelText("Chagua sababu ya kutokwenda kliniki");
                    reasonSelection = i;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        spinnerReason.setSelection(reasonSelection);
        spinnerClientAvailable.setSelection(availableSelection);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button cancel = (Button) dialogView.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button save = (Button) dialogView.findViewById(R.id.tuma_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerClientAvailable.getSelectedItem().toString().equals("ndio")){
                    if (spinnerReason.getSelectedItemPosition() <=0) {
                        // no radio checked
                        message = "Tafadhali chagua sababu ya mteja kutokwenda kliniki";
                        makeToast();
                    }else {
                        referral.setFollow_up_date(today.getTimeInMillis());
                        referral.setComment(client_condition.getText().toString());
                        referral.setFollow_up_reason(spinnerReason.getSelectedItem().toString());
                        referral.setIsValid("true");

                        String gsonReferral = gson.toJson(referral);
                        Log.d(TAG, "referral = " + gsonReferral);

                        // todo start form submission
                        saveFormSubmission(gsonReferral, clientperson.getId(), "follow_up_form", fieldOverides);


                        Toast.makeText(ChwSmartRegisterActivity.this, "Asante kwa kumtembelea " + clientperson.getFirst_name() + " " + clientperson.getSurname(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(ChwSmartRegisterActivity.this, "Tafadhali hakikisha unamtafuta tena kumjulia hali " + clientperson.getFirst_name() +" "+clientperson.getSurname(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        TextView textName = (TextView) dialogView.findViewById(R.id.patient_name);
        textName.setText(clientperson.getFirst_name()+" "+clientperson.getMiddle_name()+" "+ clientperson.getSurname());

        TextView facility = (TextView) dialog.findViewById(R.id.textview_facility);
        facility.setText(getFacilityName(clientperson.getFacility_id()));

        TextView service = (TextView) dialog.findViewById(R.id.textview_feedback);

        TextView referral_reason = (TextView) dialog.findViewById(R.id.textview_followupreason);

    }

    public void confirmDelete(final ClientReferralPersonObject mother) {
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);

        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_confirm_delete, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();


        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: delete mother

                FollowupClientsFragment preRegisterFragment = (FollowupClientsFragment) findFragmentByPosition(currentPage);
                preRegisterFragment.refreshListView();
                Toast.makeText(ChwSmartRegisterActivity.this, "umemfuta " + mother.getFirst_name() +" "+mother.getSurname(), Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private String[] buildFormNameList() {
        List<String> formNames = new ArrayList<String>();
        formNames.add("pregnant_mothers_registration");
        formNames.add("pregnant_mothers_pre_registration");
        formNames.add("anc_reminder_visit_1");
        formNames.add("anc_reminder_visit_2");
        formNames.add("anc_reminder_visit_3");
        formNames.add("anc_reminder_visit_4");
        formNames.add("birthnotificationpregnancystatusfollowup");


        DialogOption[] options = getEditOptions();
        for (int i = 0; i < options.length; i++){
            formNames.add(((OpenFormOption) options[i]).getFormName());
        }
        return formNames.toArray(new String[formNames.size()]);
    }

    public void onPageChanged(int page) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected SmartRegisterPaginatedAdapter adapter() {
        return new SmartRegisterPaginatedAdapter(clientsProvider());
    }

    @Override
    protected DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected void setupViews() {
    }

    @Override
    protected void onResumption() {
        LoginActivity.setLanguage();
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

    @Override
    public void showFragmentDialog(DialogOptionModel dialogOptionModel, Object tag) {
        try {
            LoginActivity.setLanguage();
        } catch (Exception e) {

        }
        super.showFragmentDialog(dialogOptionModel, tag);
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

    @Override
    public void OnLocationSelected(String locationSelected) {
        // set registration fragment
        Log.d(TAG,"Location selected"+locationSelected);

        Intent intent = new Intent(this, ClientsFormRegisterActivity.class);
        intent.putExtra("selectedLocation",locationSelected);
        startActivityForResult(intent,90);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.d(TAG,"am here after result");
        if (requestCode == 90) {
            if (resultCode == RESULT_OK) {
                UpdateContent();
            }
        }
    }

    private class EditDialogOptionModelfornbnf implements DialogOptionModel {
        @Override
        public DialogOption[] getDialogOptions() {
            return getEditOptions();
        }

        @Override
        public void onDialogOptionSelection(DialogOption option, Object tag) {
            onEditSelection((EditOption) option, (SmartRegisterClient) tag);
        }
    }

    private void setValuesInBoreshaAfya(){

        String userDetailsString = context().allSettings().settingsRepository.querySetting("userInformation","");
        String teamDetailsString = context().allSettings().settingsRepository.querySetting("teamInformation","");
        android.util.Log.d(TAG,"team details "+teamDetailsString);
        JSONObject teamSettings = null;
        try {
            teamSettings = new JSONObject(teamDetailsString);


            JSONObject team_details = null;
            try {
                android.util.Log.d(TAG,"teamSettings = "+teamSettings.toString());
                team_details = teamSettings.getJSONObject("team");
                android.util.Log.d(TAG,"team jason "+team_details.get("uuid").toString()+" "+team_details.get("teamName").toString());
                ((BoreshaAfyaApplication)getApplication()).setTeam_uuid(team_details.get("uuid").toString());
                ((BoreshaAfyaApplication)getApplication()).setTeam_name(team_details.get("teamName").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject userLocationSettings = null;
            try {
                userLocationSettings = team_details.getJSONObject("location");
                android.util.Log.d(TAG,"teamSettings location id= "+userLocationSettings.get("uuid").toString());
                ((BoreshaAfyaApplication)getApplication()).setTeam_location_id(userLocationSettings.get("uuid").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject userSettings = null;
        try {
            userSettings = new JSONObject(userDetailsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray roles = null;
        try {
//            android.util.Log.d(TAG,"usersettings = "+userSettings.toString());
            roles = userSettings.getJSONArray("roles");
            int count = roles.length();
            for (int i =0 ; i<count ; i++){
                try {
                    if(roles.getString(i).equals("Organizational: Health Facility User")){
                        ((BoreshaAfyaApplication)getApplication()).setUserType(0);
                    }else if (roles.getString(i).equals("Organizational: CHW")){
                        ((BoreshaAfyaApplication)getApplication()).setUserType(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject attributes = null;
        try {
            attributes = userSettings.getJSONObject("attributes");

            ((BoreshaAfyaApplication)getApplication()).setCurrentUserID(attributes.get("_PERSON_UUID").toString());
            ((BoreshaAfyaApplication)getApplication()).setUsername(userSettings.get("username").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        Log.d(TAG, "starting form = "+formName);
        Log.d(TAG, "recordId form = "+entityId);

        int formIndex = FormUtils.getIndexForFormName(formName, formNames) + 1; // add the offset
        Log.d(TAG, "starting form index = "+formIndex);
        mPager.setCurrentItem(formIndex, true);
        try {
            if (entityId != null || metaData != null) {
                String data = null;
                //check if there is previously saved data for the form
                data = getPreviouslySavedDataForForm(formName, metaData, entityId);
                if (data == null) {
                    data = FormUtils.getInstance(getApplicationContext()).generateXMLInputForFormWithEntityId(entityId, formName, metaData);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveFormSubmission(String formSubmission, final String id, String formName, JSONObject fieldOverrides) {
        // save the form
        if(formName.equals("follow_up_form"))
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


    public void switchToBaseFragment(final String data) {
        Log.v("we are here", "switchtobasegragment");
        final int prevPageIndex = currentPage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 9/17/17 this is a hack

          if(currentPage==2) {//for chws
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (currentPage != 0 && currentPage != 3) {
            retrieveAndSaveUnsubmittedFormData();
            String BENGALI_LOCALE = "bn";
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(Context.getInstance().applicationContext()));

            String preferredLocale = allSharedPreferences.fetchLanguagePreference();
            if (BENGALI_LOCALE.equals(preferredLocale)) {
                new AlertDialog.Builder(this)
                        .setMessage("আপনি কি নিশ্চিত যে আপনি ফর্ম থেকে বের হয়ে যেতে চান? ")
                        .setTitle("ফর্ম বন্ধ নিশ্চিত করুন ")
                        .setCancelable(false)
                        .setPositiveButton("হাঁ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        switchToBaseFragment(null);
                                    }
                                })
                        .setNegativeButton("না",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                })
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.mcareform_back_confirm_dialog_message)
                        .setTitle(R.string.mcareform_back_confirm_dialog_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.mcareyes_button_label,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        switchToBaseFragment(null);
                                    }
                                })
                        .setNegativeButton(R.string.mcareno_button_label,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                })
                        .show();
            }

        } else if (currentPage == 0 || currentPage == 3) {
            securedActivity = new SecuredActivity() {
                @Override
                protected void onCreation() {

                }

                @Override
                protected void onResumption() {

                }
            };
            super.onBackPressed(); // allow back key only if we are
        }
    }

    public Fragment findFragmentByPosition(int position) {
        FragmentPagerAdapter fragmentPagerAdapter = mPagerAdapter;
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + mPager.getId() + ":" + fragmentPagerAdapter.getItemId(position));
    }

    public Fragment getDisplayFormFragmentAtIndex(int index) {

        try {
            return findFragmentByPosition(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return findFragmentByPosition(index);
    }

    public void retrieveAndSaveUnsubmittedFormData() {
        if (currentActivityIsShowingForm()) {
        }
    }

    private boolean currentActivityIsShowingForm() {
        return currentPage != 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        retrieveAndSaveUnsubmittedFormData();
    }

    public int getFormIndex(String formName){
        return FormUtils.getIndexForFormName(formName, formNames) + 1;
    }

    public void switchToPage(int pageNumber){
        mPager.setCurrentItem(pageNumber);
    }


    public void updateFromServer() {
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                this, context().actionService(), context().formSubmissionSyncService(),
                new SyncProgressIndicator(), context().allFormVersionSyncService());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());
    }

    public void UpdateContent(){
        CHWSmartRegisterFragment registerFragment = (CHWSmartRegisterFragment) getDisplayFormFragmentAtIndex(0);
        if (registerFragment != null) {
            registerFragment.refreshListView();
        }
        registerFragment.updateRegisterCounts();
        registerFragment.updateRemainingFormsToSyncCount();
    }

    public boolean backUpDataBase() {
        boolean result = true;

        // Source path in the application database folder
        String appDbPath = "/data/data/com.my.application/databases/" + DATABASE_NAME;

        // Destination Path to the sdcard app folder
        String sdFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DATABASE_NAME;


        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            //Open your local db as the input stream
            myInput = new FileInputStream(appDbPath);
            //Open the empty db as the output stream
            myOutput = new FileOutputStream(sdFolder);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            try {
                //Close the streams
                if (myOutput != null) {
                    myOutput.flush();
                    myOutput.close();
                }
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException e) {
            }
        }

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        attachLogoutMenuItem(menu);
        return true;
    }

}
