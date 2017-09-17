package org.ei.opensrp.drishti;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.drishti.DataModels.ChwFollowUpMother;
import org.ei.opensrp.drishti.DataModels.PreRegisteredMother;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.Fragments.AncRegisterFormFragment;
import org.ei.opensrp.drishti.Fragments.AncSmartRegisterFragment;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;
import org.ei.opensrp.drishti.pageradapter.BaseRegisterActivityPagerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.OrientationHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
        mPager.setOffscreenPageLimit(formNames.length);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                // onPageChanged(position);
            }
        });

        //TODO this is hacking should be changed depending with the usertype
        mPager.setCurrentItem(2);
        currentPage = 2;

    }

    public void showPreRegistrationDetailsDialog(PreRegisteredMother mother) {

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

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void showPreRegistrationVisitDialog(final PreRegisteredMother mother) {

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
                Toast.makeText(AncSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + mother.getName(), Toast.LENGTH_SHORT).show();
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
        textName.setText(mother.getName());
    }

    public void showFollowUpDetailsDialog(ChwFollowUpMother mother) {

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

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
    }

    public void showFollowUpFormDialog(final ChwFollowUpMother mother) {

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
                Toast.makeText(AncSmartRegisterActivity.this, "Asante kwa kumtembelea tena " + mother.getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // TODO: findviewbyid that are on the dialog layout
        // example
        TextView textName = (TextView) dialogView.findViewById(R.id.name);
        textName.setText(mother.getName());
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


//        DialogOption[] options = getEditOptions();
//        for (int i = 0; i < options.length; i++){
//            formNames.add(((OpenFormOption) options[i]).getFormName());
//        }
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

    }

    public void StartRegistrationFragment(String formName, String entityId, String metaData){
        if(formName.equals("pregnant_mothers_registration")){
            mPager.setCurrentItem(1, true);
        }
        if(formName.equals("pregnant_mothers_pre_registration")){
            Log.d(TAG,"pregnant_mothers_pre_registration is selected");
            mPager.setCurrentItem(3, true);
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
        Log.d(TAG, "startFormActivity");
        try {
            int formIndex = FormUtils.getIndexForFormName(formName, formNames) + 1; // add the offset
            if (entityId != null || metaData != null) {
                String data = null;
                //check if there is previously saved data for the form
                data = getPreviouslySavedDataForForm(formName, metaData, entityId);
                if (data == null) {
                    data = FormUtils.getInstance(getApplicationContext()).generateXMLInputForFormWithEntityId(entityId, formName, metaData);
                }

                AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) getDisplayFormFragmentAtIndex(formIndex);
                if (displayFormFragment != null) {
                    Log.d(TAG, "form data = " + data);
                    displayFormFragment.setFormData(data);
                    displayFormFragment.setRecordId(entityId);
                    displayFormFragment.setFieldOverides(metaData);
                }
            }

            mPager.setCurrentItem(2, false); //Don't animate the view on orientation change the view disappears

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

        MotherPersonObject motherPersonObject = new MotherPersonObject(id, id, pregnantMom);
        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);
        Log.d(TAG, "motherPersonObject = " + gson.toJson(motherPersonObject));
        Log.d(TAG, "values = " + gson.toJson(values));

        CommonRepository commonRepository = context().commonrepository("wazazi_salama_mother");
        commonRepository.customInsert(values);

//        Map<String, String> personDetails1 = create("Is_PNC", "0").map();
//        personDetails1.put("FWWOMVALID","1");
//        personDetails1.put("FWBNFGEN", "2");
//        personDetails1.put("FWWOMFNAME", pregnantMom.getName());
//        personDetails1.put("GOBHHID", pregnantMom.getId());
//        personDetails1.put("FWWOMLNAME", pregnantMom.getName());
//        personDetails1.put("FWPSRLMP", sdf.format(new Date(pregnantMom.getDateLNMP())));
//        personDetails1.put("FWPSRPREGTWYRS", pregnantMom.getPregnancyCount()+"");
//        personDetails1.put("FWPSRPRSB", pregnantMom.isHasBabyDeath()?"Has had still birth":"");
//        personDetails1.put("FWPSRTOTBIRTH", pregnantMom.getChildrenCount()+"");
//        personDetails1.put("FWPSRPREGTWYRS", pregnantMom.isHas2orMoreBBA()?"Has had no pregnancies in the last 2 years":"");
//        personDetails1.put("FWWOMAGE", pregnantMom.getAge()+"");
//        personDetails1.put("HEIGHT", pregnantMom.getHeight()+"");
//        personDetails1.put("user_type", "1");

//        String[] columns = {"MOTHERS_FIRST_NAME",
//                "MOTHERS_LAST_NAME",
//                "MOTHERS_LAST_MENSTRUATION_DATE",
//                "MOTHERS_SORTVALUE",
//                "MOTHERS_ID",
//                "PNC_STATUS",
//                "EXPECTED_DELIVERY_DATE",
//                "IS_VALID",
//                "Is_PNC",
//                "FACILITY_ID"};

//        CustomMotherRepository motherRepository = new CustomMotherRepository("wazazi_salama_mother", columns);
//        motherRepository.add(motherPersonObject);

//        CommonPersonObject cpo2 = new CommonPersonObject(id,id,personDetails1,"mcaremother");
//        context().commonrepository("mcaremother").add(cpo2);
//
//        try {
//            FormUtils formUtils = FormUtils.getInstance(getApplicationContext());
//            FormSubmission submission = formUtils.generateFormSubmisionFromXMLString(id, formSubmission, formName, fieldOverrides);
//
//            org.ei.opensrp.Context context = org.ei.opensrp.Context.getInstance();
//            ZiggyService ziggyService = context.ziggyService();
//            ziggyService.saveForm(getParams(submission), submission.instance());
//
//            FormSubmissionService formSubmissionService = context.formSubmissionService();
//            formSubmissionService.updateFTSsearch(submission);
//
//            Log.v("we are here", "hhregister");
//            //switch to forms list fragmentstregi
//            switchToBaseFragment(formSubmission); // Unnecessary!! passing on data
//
//        } catch (Exception e) {
//            AncSmartRegisterFragment displayFormFragment =(AncSmartRegisterFragment) getDisplayFormFragmentAtIndex(currentPage);
//            if (displayFormFragment != null) {
////                displayFormFragment.hideTranslucentProgressDialog();
//            }
//            e.printStackTrace();
//        }
    }

    public void switchToBaseFragment(final String data) {
        Log.v("we are here", "switchtobasegragment");
        final int prevPageIndex = currentPage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: 9/17/17 this is a hack
                mPager.setCurrentItem(2, true);


                SecuredNativeSmartRegisterCursorAdapterFragment registerFragment = (SecuredNativeSmartRegisterCursorAdapterFragment) findFragmentByPosition(currentPage);
                if (registerFragment != null && data != null) {
                    registerFragment.refreshListView();
                }

                try {
                    AncSmartRegisterFragment displayFormFragment = (AncSmartRegisterFragment) getDisplayFormFragmentAtIndex(prevPageIndex);
                    if (displayFormFragment != null) {
//                        displayFormFragment.setFormData(null);
                    }

                    displayFormFragment.setRecordId(null);
                } catch (Exception e) {
                    e.printStackTrace();
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

        } else if (currentPage == 0) {
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
