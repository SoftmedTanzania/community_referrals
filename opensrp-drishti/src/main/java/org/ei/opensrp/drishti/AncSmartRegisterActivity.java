package org.ei.opensrp.drishti;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import org.ei.opensrp.drishti.DataModels.ChwFollowUpMother;
import org.ei.opensrp.drishti.DataModels.PreRegisteredMother;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.Fragments.AncRegisterFormFragment;
import org.ei.opensrp.drishti.Fragments.AncSmartRegisterFragment;
import org.ei.opensrp.drishti.Fragments.CHWPreRegisterFormFragment;
import org.ei.opensrp.drishti.Fragments.CHWPreRegistrationFragment;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;
import org.ei.opensrp.drishti.pageradapter.BaseRegisterActivityPagerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.DatesHelper;
import org.ei.opensrp.drishti.util.OrientationHelper;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.util.FormUtils;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.controller.VillageController;
import org.ei.opensrp.view.dialog.DialogOption;
import org.ei.opensrp.view.dialog.DialogOptionMapper;
import org.ei.opensrp.view.dialog.DialogOptionModel;
import org.ei.opensrp.view.dialog.EditOption;
import org.ei.opensrp.view.dialog.OpenFormOption;
import org.ei.opensrp.view.viewpager.OpenSRPViewPager;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.ei.opensrp.drishti.util.Utils.generateRandomUUIDString;

public class AncSmartRegisterActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    private static final String TAG = AncSmartRegisterActivity.class.getSimpleName();

    private SmartRegisterClientsProvider clientProvider = null;
    private CommonPersonObjectController controller;
    private VillageController villageController;
    private DialogOptionMapper dialogOptionMapper;

    @Bind(R.id.view_pager)
    OpenSRPViewPager mPager;
    private FragmentPagerAdapter mPagerAdapter;
    private int currentPage;

    private String[] formNames = new String[]{};
    private Fragment mBaseFragment;
    private Gson gson = new Gson();
    private CommonRepository commonRepository;
    private android.content.Context appContext;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();
    private Cursor cursor;
    private static final String TABLE_NAME = "wazazi_salama_mother";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // orientation
        OrientationHelper.setProperOrientationForDevice(AncSmartRegisterActivity.this);

        formNames = this.buildFormNameList();
        mBaseFragment = new AncSmartRegisterFragment();

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
        //TODO this is hacking should be changed depending with the usertype
        mPager.setCurrentItem(0);
        currentPage = 0;

    }

    public void showPreRegistrationDetailsDialog(MotherPersonObject mother) {
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails().substring(1, mother.getDetails().length() - 1));
        Log.d(TAG, "gsonMom = " + gsonMom);
        PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AncSmartRegisterActivity.this);
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

        long lnmp = pregnantMom.getDateLNMP();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String edd = dateFormat.format(DatesHelper.calculateEDDFromLNMP(lnmp));
        String reg_date = dateFormat.format(pregnantMom.getDateReg());

        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);
        TextView textSpouseName = (TextView) dialogView.findViewById(R.id.spouseName);
        TextView textSpousetel = (TextView) dialogView.findViewById(R.id.spouseTel);
        TextView textvillage = (TextView) dialogView.findViewById(R.id.village);
        TextView textfacility = (TextView) dialogView.findViewById(R.id.facility);
        TextView textvisit = (TextView) dialogView.findViewById(R.id.visit);
        TextView textrisk = (TextView) dialogView.findViewById(R.id.risk);
        TextView textedd = (TextView) dialogView.findViewById(R.id.EDD);
        TextView textlnmp = (TextView) dialogView.findViewById(R.id.lnmp);
        TextView textPregnancyAge = (TextView) dialogView.findViewById(R.id.age);


        textName.setText(mother.getMOTHERS_FIRST_NAME() +" "+mother.getMOTHERS_LAST_NAME());
        textAge.setText(String.valueOf(pregnantMom.getAge())+" years");
        textSpouseName.setText(pregnantMom.getHusbandName()+"["+ pregnantMom.getHusbandOccupation() +"]");
        textSpousetel.setText(pregnantMom.getPhone());
        textvillage.setText(pregnantMom.getPhysicalAddress());
        textfacility.setText(pregnantMom.getFacilityId());
        textvisit.setText(reg_date);
        //Todo to check the risk indicators if checked to display high or low or moderate
        textrisk.setText("high");
        textedd.setText(edd);
        textlnmp.setText(mother.getMOTHERS_LAST_MENSTRUATION_DATE());
        if(pregnantMom.isAbove20WeeksPregnant()){
            textPregnancyAge.setText("greater than 20");
        }
        else     {
            textPregnancyAge.setText("less than 20");
        }
    }

    public void showPreRegistrationVisitDialog(final MotherPersonObject mother) {

        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
        final PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwregistration_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AncSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        Button button_yes = (Button) dialogView.findViewById(R.id.button_yes);
        Button button_no = (Button) dialogView.findViewById(R.id.button_no);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pregnantMom.setDateLastVisited(Calendar.getInstance().getTimeInMillis());

                mother.setDetails(new Gson().toJson(pregnantMom));
                updateFormSubmission(mother,mother.getId());
                Toast.makeText(AncSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + mother.getMOTHERS_FIRST_NAME() +" "+mother.getMOTHERS_LAST_NAME(), Toast.LENGTH_SHORT).show();
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
        textName.setText(mother.getMOTHERS_FIRST_NAME()+" "+ mother.getMOTHERS_LAST_NAME());
    }

    public void showFollowUpDetailsDialog(MotherPersonObject mother) {

        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d(TAG, "gsonMom = " + gsonMom);
        PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AncSmartRegisterActivity.this);
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


        long lnmp = pregnantMom.getDateLNMP();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String edd = dateFormat.format(DatesHelper.calculateEDDFromLNMP(lnmp));
        String reg_date = dateFormat.format(pregnantMom.getDateReg());

        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);
        TextView textSpouseName = (TextView) dialogView.findViewById(R.id.spouseName);
        TextView textSpousetel = (TextView) dialogView.findViewById(R.id.spouseTel);
        TextView textvillage = (TextView) dialogView.findViewById(R.id.village);
        TextView textfacility = (TextView) dialogView.findViewById(R.id.facility);
        TextView textvisit = (TextView) dialogView.findViewById(R.id.visit);
        TextView textrisk = (TextView) dialogView.findViewById(R.id.risk);
        TextView textedd = (TextView) dialogView.findViewById(R.id.EDD);
        TextView textlnmp = (TextView) dialogView.findViewById(R.id.lnmp);
        TextView textPregnancyAge = (TextView) dialogView.findViewById(R.id.age);


        textName.setText(mother.getMOTHERS_FIRST_NAME() +" "+mother.getMOTHERS_LAST_NAME());
        textAge.setText(String.valueOf(pregnantMom.getAge()) + " years");
        textSpouseName.setText(pregnantMom.getHusbandName()+"["+ pregnantMom.getHusbandOccupation() +"]");
        textSpousetel.setText(pregnantMom.getPhone());
        textvillage.setText(pregnantMom.getPhysicalAddress());
        textfacility.setText(pregnantMom.getFacilityId());
        textvisit.setText(reg_date);
        //Todo to check the risk indicators if checked to display high or low or moderate
        textrisk.setText("high");
        textedd.setText(edd);
        textlnmp.setText(mother.getMOTHERS_LAST_MENSTRUATION_DATE());
        if(pregnantMom.isAbove20WeeksPregnant()){
            textPregnancyAge.setText("greater than 20");
        }
        else     {
            textPregnancyAge.setText("less than 20");
        }
    }

    public void showFollowUpFormDialog(final MotherPersonObject mother) {

        String gsonMom = Utils.convertStandardJSONString(mother.getDetails().substring(1, mother.getDetails().length() - 1));
        Log.d(TAG, "gsonMom = " + gsonMom);
        PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);

        final View dialogView = getLayoutInflater().inflate(R.layout.fragment_chwfollow_visit_details, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AncSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.visit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (selectedId == R.id.visit_yes) {
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.VISIBLE);
                }
                if (selectedId == R.id.visit_no) {
                    RelativeLayout info = (RelativeLayout) dialogView.findViewById(R.id.information);
                    info.setVisibility(View.GONE);
                }
            }
        });


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
                Toast.makeText(AncSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + mother.getMOTHERS_FIRST_NAME(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        TextView textAge = (TextView) dialogView.findViewById(R.id.mom_age);

        textName.setText(mother.getMOTHERS_FIRST_NAME() +" "+ mother.getMOTHERS_LAST_NAME());
        textAge.setText(String.valueOf(pregnantMom.getAge())+" years");


    }

    public void confirmDelete() {
        final View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_confirm_delete, null);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AncSmartRegisterActivity.this);
        dialogBuilder.setView(dialogView)
                .setCancelable(false);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();


        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: delete mother


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
        Log.d(TAG,"Location selected");
        mPager.setCurrentItem(2);
        currentPage = 2;
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
                if (formName.equals("pregnant_mothers_registration")) {
                    AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) getDisplayFormFragmentAtIndex(formIndex);
                    if (displayFormFragment != null) {
                        Log.d(TAG, "form data = " + data);
                        displayFormFragment.setFormData(data);
                        displayFormFragment.setRecordId(entityId);
                        displayFormFragment.setFieldOverides(metaData);
                    }
                } else if(formName.equals("pregnant_mothers_pre_registration")){
                    CHWPreRegisterFormFragment displayFormFragment = (CHWPreRegisterFormFragment) getDisplayFormFragmentAtIndex(formIndex);
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
    public void saveFormSubmission(String formSubmission, String id, String formName, JSONObject fieldOverrides) {
        // save the form
        PregnantMom pregnantMom = gson.fromJson(formSubmission, PregnantMom.class);

        // todo fix NullPointerException on getWritableDatabase

//        MotherPersonObject motherPersonObject = new MotherPersonObject(id, id, pregnantMom);
//        CustomContext customContext = new CustomContext();
//        CustomMotherRepository motherRepository = customContext.getCustomMotherRepo("wazazi_salama_mother");
//        motherRepository.add(motherPersonObject);

        MotherPersonObject motherPersonObject = new MotherPersonObject(id, null, pregnantMom);
        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);
        Log.d(TAG, "motherPersonObject = " + gson.toJson(motherPersonObject));
        Log.d(TAG, "values = " + gson.toJson(values));

        CommonRepository commonRepository = context().commonrepository("wazazi_salama_mother");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();
        for ( String key : c.getDetails().keySet() ) {
            FormField f = null;
            if(!key.equals("FACILITY_ID")) {
                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
            }else{
                f = new FormField(key, c.getDetails().get(key), "facility.id");
            }
            formFields.add(f);
        }

        FormData formData = new FormData("wazazi_salama_mother","/model/instance/Wazazi_Salama_ANC_Registration/",formFields,null);
        FormInstance formInstance = new FormInstance(formData,"1");
        FormSubmission submission = new FormSubmission(generateRandomUUIDString(),id,"wazazi_salama_pregnant_mothers_registration",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
        context().formDataRepository().saveFormSubmission(submission);
    }

    public void updateFormSubmission(MotherPersonObject motherPersonObject, String id){


        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);

        CommonRepository commonRepository = context().commonrepository("wazazi_salama_mother");
        commonRepository.customUpdate(values,id);
    }

    public void switchToBaseFragment(final String data) {
        Log.v("we are here", "switchtobasegragment");
        final int prevPageIndex = currentPage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 9/17/17 this is a hack


                if(currentPage==1) {//for supervisors
                    AncSmartRegisterFragment registerFragment = (AncSmartRegisterFragment) findFragmentByPosition(currentPage);
                    if (registerFragment != null) {
                        registerFragment.refreshListView();
                    }
                    mPager.setCurrentItem(0, true);
                }else   if(currentPage==2) {//for chws
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (currentPage != 0) {
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
            try {
                AncSmartRegisterFragment formFragment = (AncSmartRegisterFragment) getDisplayFormFragmentAtIndex(currentPage);
                formFragment.saveCurrentFormData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean currentActivityIsShowingForm() {
        return currentPage != 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        retrieveAndSaveUnsubmittedFormData();
    }


}
