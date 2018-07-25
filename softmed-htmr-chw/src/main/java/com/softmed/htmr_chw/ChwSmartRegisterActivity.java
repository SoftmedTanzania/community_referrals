package com.softmed.htmr_chw;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw.Fragments.CHWSmartRegisterFragment;
import com.softmed.htmr_chw.Repository.ClientReferralPersonObject;
import com.softmed.htmr_chw.Repository.LocationSelectorDialogFragment;
import com.softmed.htmr_chw.pageradapter.BaseRegisterActivityPagerAdapter;
import com.softmed.htmr_chw.util.Utils;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientFollowup;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.util.FormUtils;
import org.ei.opensrp.view.activity.SecuredActivity;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionModel;
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
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.VISIBLE;
import static com.softmed.htmr_chw.util.Utils.generateRandomUUIDString;

public class ChwSmartRegisterActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    static final String DATABASE_NAME = "drishti.db";
    private static final String TAG = ChwSmartRegisterActivity.class.getSimpleName();
    public static MaterialSpinner spinnerReason, spinnerClientAvailable;
    public static int availableSelection = -1, reasonSelection = -1;
    @Bind(R.id.view_pager)
    public OpenSRPViewPager mPager;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    String message = "";
    Calendar today = Calendar.getInstance();
    private JSONObject fieldOverides = new JSONObject();
    private FragmentPagerAdapter mPagerAdapter;
    private int currentPage;
    private String[] formNames = new String[]{};
    private Fragment mBaseFragment;
    private Gson gson = new Gson();
    private CommonRepository commonRepository;
    private Cursor cursor;
    private SecuredActivity securedActivity;
    private LinearLayout flags_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        Log.d(TAG, "table columns =" + new Gson().toJson(context().commonrepository("referral_service").common_TABLE_COLUMNS));


    }

    private void initialize() {

        LoginActivity.setLanguage();
    }


    public void showPreRegistrationDetailsDialog(ClientReferralPersonObject clientReferralPersonObject) {

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_details, null);
        String gsonClient = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        ClientReferral clientReferral = new Gson().fromJson(gsonClient, ClientReferral.class);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(true);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 650);


        String reg_date = dateFormat.format(clientReferral.getDate_of_birth());
        String ageS = "";
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

        try {
            if (clientReferralPersonObject.getReferral_status().equals("1") && !clientReferral.getServices_given_to_patient().equals("")) {
                dialogView.findViewById(R.id.referral_feedback_title).setVisibility(VISIBLE);
                dialogView.findViewById(R.id.referral_feedback).setVisibility(VISIBLE);
                dialogView.findViewById(R.id.strip_six).setVisibility(VISIBLE);
                TextView referralFeedback = (TextView) dialogView.findViewById(R.id.referral_feedback);
                referralFeedback.setVisibility(VISIBLE);
                referralFeedback.setText(clientReferral.getServices_given_to_patient());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!clientReferral.getOther_notes().equals("")) {
            dialogView.findViewById(R.id.other_notes_title).setVisibility(VISIBLE);
            dialogView.findViewById(R.id.other_notes).setVisibility(VISIBLE);
            dialogView.findViewById(R.id.strip_seven).setVisibility(VISIBLE);
            TextView otherNotes = (TextView) dialogView.findViewById(R.id.other_notes);
            otherNotes.setVisibility(VISIBLE);
            otherNotes.setText(clientReferral.getOther_notes());
        }


        textName.setText(clientReferralPersonObject.getFirst_name() + " " + clientReferralPersonObject.getMiddle_name() + " " + clientReferralPersonObject.getSurname());

        textAge.setText(ageS + " years");
        cbhs.setText(clientReferral.getCommunity_based_hiv_service());

        try {
            referral_service.setText(getReferralServiceName(clientReferralPersonObject.getReferral_service_id()));
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "facility id = " + clientReferralPersonObject.getFacility_id());

        facility.setText(getFacilityName(clientReferralPersonObject.getFacility_id()));
        if (!clientReferralPersonObject.getCtc_number().isEmpty())
            ctc_number.setText(clientReferralPersonObject.getCtc_number());
        else
            ctc_number.setText("-");
        referral_reason.setText(clientReferralPersonObject.getReferral_reason());
        phoneNumber.setText(clientReferral.getPhone_number());
        villageleader.setText(clientReferral.getVillage_leader());
        physicalAddress.setText(clientReferral.getVillage());

        try {
            if ((clientReferralPersonObject.getGender()).equals(getResources().getString(R.string.female))) {
                gender.setText(getResources().getString(R.string.female));
            } else {
                gender.setText(getResources().getString(R.string.male));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setIndicators(dialogView, gsonClient);
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    public String getFacilityName(String id) {

        commonRepository = context().commonrepository("facility");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    public String getReferralServiceName(String id) {

        commonRepository = context().commonrepository("referral_service");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where id ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }

    //TODO Coze reimplement this
    public void setIndicators(View view, String object) {
        try {
            JSONObject jsonObj = new JSONObject(object);
            Log.d(TAG, "jason indicators " + jsonObj.get("indicator_ids"));
            String list = jsonObj.getString("indicator_ids").toString();
            Log.d(TAG, "list" + list);
            list = list.replace("[", "");
            list = list.replace("\"", "");
            list = list.replace("]", "");
            Log.d(TAG, "list" + list);
            List<String> myList = new ArrayList<String>(Arrays.asList(list.split(",")));
            Log.d(TAG, "inidcators list " + myList.get(0) + "size " + myList.size());
            flags_layout = (LinearLayout) view.findViewById(R.id.flags_layout);
            flags_layout.removeAllViewsInLayout();
            for (int m = 0; m < myList.size(); m++) {
                final TextView rowTextView = new TextView(this);
                rowTextView.setText(getIndicatorName(myList.get(m)));
                rowTextView.setPadding(0, 10, 10, 0);
                flags_layout.addView(rowTextView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getIndicatorName(String id) {
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where referralServiceIndicatorId ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("indicatorName");
    }

    public void showFollowUpFormDialog(final ClientFollowup followup) {



        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_visit_details, null);
        final EditText client_condition = (EditText) dialogView.findViewById(R.id.client_status);

        String[] ITEMS = {getString(R.string.followup_feedback_patient_moved), getString(R.string.followup_feedback_patient_died),getString(R.string.followup_feedback_other_reasons)};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReason = (MaterialSpinner) dialogView.findViewById(R.id.spinnerReason);
        spinnerReason.setAdapter(adapter);

        String[] options = {getResources().getString(R.string.yes_button_label), getResources().getString(R.string.no_button_label)};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientAvailable = (MaterialSpinner) dialogView.findViewById(R.id.spinnerClientAvailable);
        spinnerClientAvailable.setAdapter(adapter1);

        spinnerClientAvailable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    availableSelection = i;
                }

                if (spinnerClientAvailable.getSelectedItem().toString().equals(getString(R.string.yes_button_label))) {
                    spinnerReason.setVisibility(VISIBLE);
                    client_condition.setVisibility(VISIBLE);
//                    view.setVisibility(View.GONE);
                } else {
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
                    spinnerReason.setFloatingLabelText(getString(R.string.followup_qn_reasons_for_not_visiting_clinic));
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
                if (spinnerClientAvailable.getSelectedItem().toString().equals(getString(R.string.yes_button_label))) {
                    if (spinnerReason.getSelectedItemPosition() <= 0) {
                        // no radio checked
                        message = getString(R.string.toast_message_select_reasons_for_missing_appointment);
                        makeToast();
                    } else {
                        followup.setVisit_date(today.getTimeInMillis());
                        followup.setReferral_feedback(client_condition.getText().toString());

                        context().followupClientRepository().update(followup);


                        //TODO finish up sending of referral feedbacks of the followup

//                        final String uuid = generateRandomUUIDString();
//
//
//                        context().followupClientRepository().update(followup);
//
//
//                        List<FormField> formFields = new ArrayList<>();
//
//
//                        formFields.add(new FormField("id", followup.getId(), commonRepository.TABLE_NAME + "." + "id"));
//
//
//                        formFields.add(new FormField("relationalid", followup.getId(), commonRepository.TABLE_NAME + "." + "relationalid"));
//
//                        for (String key : followup.getDetails().keySet()) {
//                            Log.d(TAG, "key = " + key);
//                            FormField f = null;
//                            if (!key.equals("facility_id")) {
//                                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
//                            } else {
//                                f = new FormField(key, c.getDetails().get(key), "facility.id");
//                            }
//                            formFields.add(f);
//                        }
//
//
//                        Log.d(TAG, "form field = " + new Gson().toJson(formFields));
//
//                        FormData formData = new FormData("follow_up", "/model/instance/Follow_Up_Form/", formFields, null);
//                        FormInstance formInstance = new FormInstance(formData, "1");
//                        FormSubmission submission = new FormSubmission(generateRandomUUIDString(), uuid, "client_referral", new Gson().toJson(formInstance), "4", SyncStatus.PENDING, "4");
//                        context().formDataRepository().saveFormSubmission(submission);
//
//                        Log.d(TAG, "submission content = " + new Gson().toJson(submission));
//
//
//                        new AsyncTask<Void, Void, Void>() {
//                            @Override
//                            protected Void doInBackground(Void... params) {
//                                CommonRepository commonRepository = context().commonrepository("client_referral");
//                                CommonPersonObject c = commonRepository.findByCaseID(id);
//                                if (!c.getDetails().get("PhoneNumber").equals(""))
//                                    Utils.sendRegistrationAlert(c.getDetails().get("PhoneNumber"));
//                                return null;
//                            }
//                        }.execute();






                        Toast.makeText(ChwSmartRegisterActivity.this, getString(R.string.followup_thankyou_note_part_one) + followup.getFirst_name() + " " + followup.getSurname(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(ChwSmartRegisterActivity.this, getString(R.string.followup_clint_not_found_responce) + followup.getFirst_name() + " " + followup.getSurname(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        TextView textName = (TextView) dialogView.findViewById(R.id.patient_name);
        textName.setText(followup.getFirst_name() + " " + followup.getMiddle_name() + " " + followup.getSurname());

        TextView facility = (TextView) dialog.findViewById(R.id.textview_facility);
        facility.setText(getFacilityName(followup.getFacility_id()));

        TextView referral_reason = (TextView) dialog.findViewById(R.id.textview_followupreason);
        referral_reason.setText(followup.getReferral_reason());

    }

    private String[] buildFormNameList() {
        List<String> formNames = new ArrayList<String>();
        formNames.add("main_form");

        DialogOption[] options = getEditOptions();
        for (int i = 0; i < options.length; i++) {
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

        };
    }

    @Override
    public void OnLocationSelected(String locationSelected) {
        // set registration fragment
        Log.d(TAG, "Location selected" + locationSelected);

        Intent intent = new Intent(this, ReferralClientsFormRegisterActivity.class);
        intent.putExtra("selectedLocation", locationSelected);
        startActivityForResult(intent, 90);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.d(TAG, "am here after result");
        if (requestCode == 90) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "am here after result2");
                updateFromServer();
            }
        }
    }

    private void setValuesInBoreshaAfya() {

        String userDetailsString = context().allSettings().settingsRepository.querySetting("userInformation", "");
        String teamDetailsString = context().allSettings().settingsRepository.querySetting("teamInformation", "");
        android.util.Log.d(TAG, "team details " + teamDetailsString);
        JSONObject teamSettings = null;
        try {
            teamSettings = new JSONObject(teamDetailsString);


            JSONObject team_details = null;
            try {
                android.util.Log.d(TAG, "teamSettings = " + teamSettings.toString());
                team_details = teamSettings.getJSONObject("team");
                android.util.Log.d(TAG, "team jason " + team_details.get("uuid").toString() + " " + team_details.get("teamName").toString());
                ((BoreshaAfyaApplication) getApplication()).setTeam_uuid(team_details.get("uuid").toString());
                ((BoreshaAfyaApplication) getApplication()).setTeam_name(team_details.get("teamName").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject userLocationSettings = null;
            try {
                userLocationSettings = team_details.getJSONObject("location");
                android.util.Log.d(TAG, "teamSettings location id= " + userLocationSettings.get("uuid").toString());
                ((BoreshaAfyaApplication) getApplication()).setTeam_location_id(userLocationSettings.get("uuid").toString());
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
            for (int i = 0; i < count; i++) {
                try {
                    if (roles.getString(i).equals("Organizational: Health Facility User")) {
                        ((BoreshaAfyaApplication) getApplication()).setUserType(0);
                    } else if (roles.getString(i).equals("Organizational: CHW")) {
                        ((BoreshaAfyaApplication) getApplication()).setUserType(0);
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

            ((BoreshaAfyaApplication) getApplication()).setCurrentUserID(attributes.get("_PERSON_UUID").toString());
            ((BoreshaAfyaApplication) getApplication()).setUsername(userSettings.get("username").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void startFormActivity(String formName, String entityId, String metaData) {
        Log.d(TAG, "starting form = " + formName);
        Log.d(TAG, "recordId form = " + entityId);

        int formIndex = FormUtils.getIndexForFormName(formName, formNames) + 1; // add the offset
        Log.d(TAG, "starting form index = " + formIndex);
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
    public void onBackPressed() {
        boolean backPressed = ((CHWSmartRegisterFragment)mBaseFragment).onBackPressed();
       if( backPressed)
        super.onBackPressed(); // allow back key only if we are
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

    public int getFormIndex(String formName) {
        return FormUtils.getIndexForFormName(formName, formNames) + 1;
    }

    public void switchToPage(int pageNumber) {
        mPager.setCurrentItem(pageNumber);
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

    public void updateFromServer() {
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                ChwSmartRegisterActivity.this, context().actionService(), context().formSubmissionSyncService(),
                new SyncProgressIndicator(), context().allFormVersionSyncService());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());

        ((CHWSmartRegisterFragment) mBaseFragment).updateRemainingFormsToSyncCount();
    }


}
