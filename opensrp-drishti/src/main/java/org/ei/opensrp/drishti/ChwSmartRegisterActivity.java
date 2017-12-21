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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.DataModels.FollowUp;
import org.ei.opensrp.drishti.Fragments.FollowupClientsFragment;
import org.ei.opensrp.drishti.Fragments.ReferredClientsFragment;
import org.ei.opensrp.drishti.Fragments.ClientRegistrationFormFragment;
import org.ei.opensrp.drishti.Fragments.CHWSmartRegisterFragment;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.Repository.ClientReferralRepository;
import org.ei.opensrp.drishti.Repository.FollowUpPersonObject;
import org.ei.opensrp.drishti.Repository.FollowUpRepository;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.pageradapter.BaseRegisterActivityPagerAdapter;
import org.ei.opensrp.drishti.util.OrientationHelper;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.util.FormUtils;
import org.ei.opensrp.view.activity.*;
import org.ei.opensrp.view.contract.HomeContext;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.controller.NativeAfterANMDetailsFetchListener;
import org.ei.opensrp.view.controller.NativeUpdateANMDetailsTask;
import org.ei.opensrp.view.controller.VillageController;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionMapper;
import org.ei.opensrp.view.dialog.DialogOptionModel;
import org.ei.opensrp.view.dialog.EditOption;
import org.ei.opensrp.view.dialog.OpenFormOption;
import org.ei.opensrp.view.viewpager.OpenSRPViewPager;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.ei.opensrp.drishti.util.Utils.generateRandomUUIDString;
import static org.ei.opensrp.event.Event.ACTION_HANDLED;
import static org.ei.opensrp.event.Event.FORM_SUBMITTED;
import static org.ei.opensrp.event.Event.SYNC_COMPLETED;
import static org.ei.opensrp.event.Event.SYNC_STARTED;

public class ChwSmartRegisterActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    private static final String TAG = ChwSmartRegisterActivity.class.getSimpleName();

    private SmartRegisterClientsProvider clientProvider = null;
    private CommonPersonObjectController controller;
    private VillageController villageController;
    private DialogOptionMapper dialogOptionMapper;
    private JSONObject fieldOverides = new JSONObject();
    @Bind(R.id.view_pager)
    public    OpenSRPViewPager mPager;
    private FragmentPagerAdapter mPagerAdapter;
    private int currentPage;
    private String[] formNames = new String[]{};
    private Fragment mBaseFragment;
    private Gson gson = new Gson();
    private CommonRepository commonRepository;
    private android.content.Context appContext;
    private Cursor cursor;
    private static final String TABLE_NAME = "client_referral";
    private PendingFormSubmissionService pendingFormSubmissionService;
    private MenuItem updateMenuItem;
    private MenuItem remainingFormsToSyncMenuItem;
    static final String DATABASE_NAME = "drishti.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // orientation
        OrientationHelper.setProperOrientationForDevice(ChwSmartRegisterActivity.this);

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

//        if(((BoreshaAfyaApplication)getApplication()).getUserType()==0) {
//            mPager.setCurrentItem(3);
//            currentPage = 3;
//        }
        mPager.setCurrentItem(3);
        currentPage = 3;

        Log.d(TAG, "table columns ="+new Gson().toJson(context().commonrepository("referral_service").common_TABLE_COLUMNS));


    }

    public void returnToBaseFragment(){
//        mPager.setCurrentItem(0);
//        AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) getDisplayFormFragmentAtIndex(1);
//        displayFormFragment.reloadValues();


    }

    public void showPreRegistrationDetailsDialog(ClientReferralPersonObject mother) {
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
        ClientReferral clientReferral = new Gson().fromJson(gsonMom,ClientReferral.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

//        long lnmp = pregnantMom.getDateLNMP();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        String edd = dateFormat.format(DatesHelper.calculateEDDFromLNMP(lnmp));
//        String reg_date = dateFormat.format(pregnantMom.getDateReg());

        Calendar today = Calendar.getInstance();
        int Cyear = today.get(Calendar.YEAR);
        int year = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd mmm yyyy");
            Date d = sdf.parse(clientReferral.getDate_of_birth());
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            year = cal.get(Calendar.YEAR);



        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);
        TextView textSpouseName = (TextView) dialogView.findViewById(R.id.spouseName);
        TextView textSpousetel = (TextView) dialogView.findViewById(R.id.spouseTel);
        TextView textvillage = (TextView) dialogView.findViewById(R.id.village);
        TextView textfacility = (TextView) dialogView.findViewById(R.id.facility);
        TextView textvisit = (TextView) dialogView.findViewById(R.id.visit);
        TextView textReason = (TextView) dialogView.findViewById(R.id .rufaa);
        TextView textDate = (TextView) dialogView.findViewById(R.id.rufaaDate);
        TextView textcbhs = (TextView) dialogView.findViewById(R.id.cbhs);
        TextView textPregnancyAge = (TextView) dialogView.findViewById(R.id.age);


        textName.setText(mother.getFirst_name() +" "+mother.getSurname());
        textAge.setText((Cyear-year)+" years");
//        textSpouseName.setText(pregnantMom.getHusbandName()+"["+ pregnantMom.getHusbandOccupation() +"]");
//        textSpousetel.setText(pregnantMom.getPhone());
//        textvillage.setText(pregnantMom.getPhysicalAddress());
//        textfacility.setText(pregnantMom.getFacility_id());
//        textvisit.setText(reg_date);
        //Todo to check the risk indicators if checked to display high or low or moderate
//        textrisk.setText("high");
//        textedd.setText(edd);
//        textlnmp.setText(mother.getReferral_date());
//        if(pregnantMom.isAbove20WeeksPregnant()){
//            textPregnancyAge.setText("greater than 20");
//        }
//        else     {
//            textPregnancyAge.setText("less than 20");
//        }
    }

    public void showPreRegistrationVisitDialog(final ClientReferralPersonObject mother) {

        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
//        final PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        Button button_yes = (Button) dialogView.findViewById(R.id.button_yes);
        Button button_no = (Button) dialogView.findViewById(R.id.button_no);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                pregnantMom.setDateLastVisited(Calendar.getInstance().getTimeInMillis());

//                mother.setDetails(new Gson().toJson(pregnantMom));
//                updateFormSubmission(mother,mother.getId());
//todo how to refresh the  pre registartion  fragment after updating
//                mBaseFragment = new FollowupClientsFragment();
//                // Instantiate a ViewPager and a PagerAdapter.
//                mPagerAdapter = new BaseRegisterActivityPagerAdapter(getSupportFragmentManager(), formNames, mBaseFragment);
//                mPager.setOffscreenPageLimit(formNames.length);
//                mPager.setAdapter(mPagerAdapter);
//                mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//                    @Override
//                    public void onPageSelected(int position) {
//                        currentPage = position;
//                        // onPageChanged(position);
//                    }
//                });
//
//                mPager.setCurrentItem(1);
//                currentPage = 1;
                FollowupClientsFragment.newInstance();
                ReferredClientsFragment.newInstance();
                Toast.makeText(ChwSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + mother.getFirst_name() +" "+mother.getSurname(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getFirst_name()+" "+ mother.getSurname());
    }

    public void showFollowUpDetailsDialog(ClientReferralPersonObject clientReferralPersonObject) {

        String gsonMom = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
        ClientReferral clientReferral = new Gson().fromJson(gsonMom,ClientReferral.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        ImageView cancel = (ImageView) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        String reg_date = clientReferral.getReferral_date();
        String ageS="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date d = sdf.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);

            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);

            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();



        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);
//        TextView textSpouseName = (TextView) dialogView.findViewById(R.id.spouseName);
//        TextView textSpousetel = (TextView) dialogView.findViewById(R.id.spouseTel);
//        TextView textvillage = (TextView) dialogView.findViewById(R.id.village);
//        TextView textfacility = (TextView) dialogView.findViewById(R.id.facility);
//        TextView textvisit = (TextView) dialogView.findViewById(R.id.visit);
//        TextView textrisk = (TextView) dialogView.findViewById(R.id.risk);
//        TextView textedd = (TextView) dialogView.findViewById(R.id.EDD);
//        TextView textlnmp = (TextView) dialogView.findViewById(R.id.lnmp);
//        TextView textPregnancyAge = (TextView) dialogView.findViewById(R.id.age);


        textName.setText(clientReferral.getFirst_name() +" "+clientReferral.getMiddle_name()+" "+clientReferral.getSurname());

        textAge.setText(ageS + " years");
//        textSpouseName.setText(pregnantMom.getHusbandName()+"["+ pregnantMom.getHusbandOccupation() +"]");
//        textSpousetel.setText(pregnantMom.getPhone());
//        textvillage.setText(clientReferral.getVillage());
//        textfacility.setText(clientReferral.getFacility_id());
//        textvisit.setText(reg_date);
//        //Todo to check the risk indicators if checked to display high or low or moderate
//        textrisk.setText("high");
//        if((clientReferral.getGender()).equals("fe")){
//            textPregnancyAge.setText("Female");
//        }
//        else     {
//            textPregnancyAge.setText("Male");
//        }
    }

    public void showFollowUpFormDialog(final ClientReferralPersonObject clientperson) {

        String gsonClient = Utils.convertStandardJSONString(clientperson.getDetails());
        Log.d(TAG, "gsonMom = " + gsonClient);

        final FollowUp referral = new Gson().fromJson(gsonClient,FollowUp.class);

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChwSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        final String[] met = new String[1];

        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.visit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == R.id.visit_yes) {
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.VISIBLE);
                    met[0] = "yes";
                }
                if (selectedId == R.id.visit_no) {
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.GONE);
                }
            }
        });

        final EditText comment = (EditText) dialogView.findViewById(R.id.observation);
        final EditText Token = (EditText) dialogView.findViewById(R.id.token);
        final TextView date = (TextView) dialogView.findViewById(R.id.textDate);

        Button cancel = (Button) dialogView.findViewById(R.id.cancel_action);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button save = (Button) dialogView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(met[0] == "yes"){
                    referral.setFollow_up_date(date.getText().toString());
                    referral.setComment(comment.getText().toString());
                    referral.setToken(Token.getText().toString());
                    referral.setIsValid("true");

                    String gsonReferral = gson.toJson(referral);
                    Log.d(TAG, "referral = " + gsonReferral);

                    // todo start form submission
                    saveFormSubmission(gsonReferral,clientperson.getId() , "follow_up_form", fieldOverides);



                    Toast.makeText(ChwSmartRegisterActivity.this, "Asante kwa kumtembelea " + clientperson.getFirst_name() +" "+clientperson.getSurname(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(ChwSmartRegisterActivity.this, "Tafadhali hakikisha unamtafuta tena kumjulia hali " + clientperson.getFirst_name() +" "+clientperson.getSurname(), Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);

        String reg_date = clientperson.getReferral_date();
        String ageS="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd mmm yyyy");
            Date d = sdf.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);

            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);

            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();



        } catch (Exception e) {
            e.printStackTrace();
        }
        textName.setText(clientperson.getFirst_name() +" "+ clientperson.getMiddle_name()+" "+clientperson.getSurname());
        textAge.setText(ageS+" years");


    }

    public void confirmDelete(final ClientReferralPersonObject mother) {
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
//        final PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
//        pregnantMom.setIs_valid("false");

        //todo martha how to set is_valid in the pregnant mother for this passed motherpersonal object
//        JSONArray arr = new JSONArray(gsonMom);
//        JSONObject jObj = arr.getJSONObject(0);
//        String date = jObj.getString("is_valid");

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
//                mother.setIs_valid("false");
//                pregnantMom.setIs_valid("false");
//                this.motherData = new Gson().fromJson(gsonMom,PregnantMom.class);
//                Log.d(TAG, "gsonMomafter Changes = " + pregnantMom);

//                mother.setDetails(new Gson().toJson(pregnantMom));
//                updateFormSubmission(mother,mother.getId());

//                mBaseFragment = new FollowupClientsFragment();
                // Instantiate a ViewPager and a PagerAdapter.
//                mPagerAdapter = new BaseRegisterActivityPagerAdapter(getSupportFragmentManager(), formNames, mBaseFragment);
//                mPager.setOffscreenPageLimit(formNames.length);
//                mPager.setAdapter(mPagerAdapter);
//                mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//                    @Override
//                    public void onPageSelected(int position) {
//                        currentPage = position;
//                        // onPageChanged(position);
//                    }
//                });
//
//                mPager.setCurrentItem(1);
//                currentPage = 1;
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
        updateRegisterCounts();
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
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
        startActivity(intent);
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

    private class EditDialogOptionModelForANC implements DialogOptionModel {
        String ancvisittext;
        String ancvisitstatus;

        public EditDialogOptionModelForANC(String text, String status) {
            ancvisittext = text;
            ancvisitstatus = status;
        }

        @Override
        public DialogOption[] getDialogOptions() {
            return getEditOptionsforanc(ancvisittext, ancvisitstatus);
        }

        @Override
        public void onDialogOptionSelection(DialogOption option, Object tag) {
            onEditSelection((EditOption) option, (SmartRegisterClient) tag);
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
                 if(formName.equals("pregnant_mothers_pre_registration")){
                    ClientRegistrationFormFragment displayFormFragment = (ClientRegistrationFormFragment) getDisplayFormFragmentAtIndex(formIndex);
                    if (displayFormFragment != null) {
                        Log.d(TAG, "form data = " + data);
                        displayFormFragment.setFormData(data);
                        displayFormFragment.setRecordId(entityId);
                        displayFormFragment.setFieldOverides(metaData);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void updateSearchView() {
        getSearchView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(final CharSequence cs, int start, int before, int count) {
                (new AsyncTask() {
                    SmartRegisterClients filteredClients;

                    @Override
                    protected Object doInBackground(Object[] params) {
//                        currentSearchFilter = new ElcoSearchOption(cs.toString());
//                        setCurrentSearchFilter(new ElcoSearchOption(cs.toString()));
                        filteredClients = getClientsAdapter().getListItemProvider()
                                .updateClients(getCurrentVillageFilter(), getCurrentServiceModeOption(),
                                        getCurrentSearchFilter(), getCurrentSortOption());


                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
//                        clientsAdapter
//                                .refreshList(currentVillageFilter, currentServiceModeOption,
//                                        currentSearchFilter, currentSortOption);
                        getClientsAdapter().refreshClients(filteredClients);
                        getClientsAdapter().notifyDataSetChanged();
                        getSearchCancelView().setVisibility(isEmpty(cs) ? INVISIBLE : VISIBLE);
                        super.onPostExecute(o);
                    }
                }).execute();
//                currentSearchFilter = new HHSearchOption(cs.toString());
//                clientsAdapter
//                        .refreshList(currentVillageFilter, currentServiceModeOption,
//                                currentSearchFilter, currentSortOption);
//
//                searchCancelView.setVisibility(isEmpty(cs) ? INVISIBLE : VISIBLE);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void saveFormSubmission(String formSubmission, final String id, String formName, JSONObject fieldOverrides) {
        // save the form

        if(formName.equals("client_referral_form")){
            final ClientReferral clientReferral = gson.fromJson(formSubmission, ClientReferral.class);

            ClientReferralPersonObject clientReferralPersonObject = new ClientReferralPersonObject(id, null, clientReferral);
            ContentValues values = new ClientReferralRepository().createValuesFor(clientReferralPersonObject);
            Log.d(TAG, "clientReferralPersonObject = " + gson.toJson(clientReferralPersonObject));
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


//    public void updateFormSubmission(MotherPersonObject motherPersonObject, String id){
//
//
//        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);
//        Log.d(TAG,"values to be updated ="+ new Gson().toJson(values));
//        Log.d(TAG," mother id to be updated ="+ id);
//        Log.d(TAG," mother id to be updated ="+ motherPersonObject.getId());
//        CommonRepository commonRepository = context().commonrepository("wazazi_salama_mother");
//        commonRepository.customUpdateTable("wazazi_salama_mother",values,motherPersonObject.getId());
//
//        CommonRepository cRepository = context().commonrepository("wazazi_salama_mother");
//
//        CommonPersonObject c = cRepository.findByCaseID(id);
//        List<FormField> formFields = new ArrayList<>();
//
//
//        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));
//
//
//        formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));
//
//        for ( String key : c.getDetails().keySet() ) {
//            Log.d(TAG,"key = "+key);
//            FormField f = null;
//            if(!key.equals("FACILITY_ID")) {
//                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
//            }else{
//                f = new FormField(key, c.getDetails().get(key), "facility.id");
//            }
//            formFields.add(f);
//        }
//
//        for ( String key : c.getColumnmaps().keySet() ) {
//            Log.d(TAG,"key = "+key);
//            FormField f = null;
//            if(!key.equals("FACILITY_ID")) {
//                f = new FormField(key, c.getColumnmaps().get(key), commonRepository.TABLE_NAME + "." + key);
//            }else{
//                f = new FormField(key, c.getColumnmaps().get(key), "facility.id");
//            }
//
//            formFields.add(f);
//
//
//        }
//        Log.d(TAG,"fieldes = "+ new Gson().toJson(formFields));
//
//        FormData formData = new FormData("wazazi_salama_mother","/model/instance/Wazazi_Salama_ANC_Registration/",formFields,null);
//        FormInstance formInstance = new FormInstance(formData,"1");
//        FormSubmission submission = context().formDataRepository().fetchFromSubmissionByEntity(motherPersonObject.getId());
//
//        Log.d(TAG,"submission content = "+ new Gson().toJson(submission));
//
//        FormSubmission updatedSubmission = new FormSubmission(submission.instanceId(), submission.entityId(), submission.formName(), new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
//        context().formDataRepository().updateFormSubmission(updatedSubmission);
//
//        Log.d(TAG,"submission content = "+ new Gson().toJson(updatedSubmission));
//    }

    public void switchToBaseFragment(final String data) {
        Log.v("we are here", "switchtobasegragment");
        final int prevPageIndex = currentPage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 9/17/17 this is a hack

//
//                if(currentPage==1) {//for supervisors
//                    AncSmartRegisterFragment registerFragment = (AncSmartRegisterFragment) findFragmentByPosition(0);
//                    if (registerFragment != null) {
//                        registerFragment.refreshListView();
//                    }
//                    mPager.setCurrentItem(0, true);
//                }else
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
//            try {
//                AncSmartRegisterFragment formFragment = (AncSmartRegisterFragment) getDisplayFormFragmentAtIndex(currentPage);
//                formFragment.saveCurrentFormData();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
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


    private void updateSyncIndicator() {
        if (updateMenuItem != null) {
            if (context().allSharedPreferences().fetchIsSyncInProgress()) {
                updateMenuItem.setActionView(R.layout.progress);
            } else
                updateMenuItem.setActionView(null);
        }
    }

    private void updateRemainingFormsToSyncCount() {
        if (remainingFormsToSyncMenuItem == null) {
            return;
        }

        long size = pendingFormSubmissionService.pendingFormSubmissionCount();
        if (size > 0) {
            remainingFormsToSyncMenuItem.setTitle(valueOf(size) + " " + getString(R.string.unsynced_forms_count_message));
            remainingFormsToSyncMenuItem.setVisible(true);
        } else {
            remainingFormsToSyncMenuItem.setVisible(false);
        }
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

    private void initialize() {
        pendingFormSubmissionService = context().pendingFormSubmissionService();
        SYNC_STARTED.addListener(onSyncStartListener);
        SYNC_COMPLETED.addListener(onSyncCompleteListener);
        FORM_SUBMITTED.addListener(onFormSubmittedListener);
        ACTION_HANDLED.addListener(updateANMDetailsListener);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("HTM-Referrals");
        LoginActivity.setLanguage();
    }

    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            //#TODO: RemainingFormsToSyncCount cannot be updated from a back ground thread!!
            updateRemainingFormsToSyncCount();
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(null);
            }
            updateRegisterCounts();
        }
    };

    private void updateRegisterCounts() {
        NativeUpdateANMDetailsTask task = new NativeUpdateANMDetailsTask(Context.getInstance().anmController());
        task.fetch(new NativeAfterANMDetailsFetchListener() {
            @Override
            public void afterFetch(HomeContext anmDetails) {
                // TODO: 9/14/17 update counts after fetch
                // updateRegisterCounts(anmDetails);
            }
        });
    }

    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(R.layout.progress);
            }
        }
    };

    private Listener<String> onFormSubmittedListener = new Listener<String>() {
        @Override
        public void onEvent(String instanceId) {
            updateRegisterCounts();
        }
    };



    private Listener<String> updateANMDetailsListener = new Listener<String>() {
        @Override
        public void onEvent(String data) {
            updateRegisterCounts();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        attachLogoutMenuItem(menu);
        return true;
    }

}
