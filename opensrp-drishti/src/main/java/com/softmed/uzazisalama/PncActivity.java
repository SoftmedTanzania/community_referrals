package com.softmed.uzazisalama;

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
import com.softmed.uzazisalama.Application.UzaziSalamaApplication;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Fragments.AncRegisterFormFragment;
import com.softmed.uzazisalama.Fragments.AncSmartRegisterFragment;
import com.softmed.uzazisalama.Fragments.CHWPreRegisterFormFragment;
import com.softmed.uzazisalama.Fragments.CHWPreRegistrationFragment;
import com.softmed.uzazisalama.Fragments.CHWSmartRegisterFragment;
import com.softmed.uzazisalama.Repository.CustomMotherRepository;
import com.softmed.uzazisalama.Repository.LocationSelectorDialogFragment;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.pageradapter.BaseRegisterActivityPagerAdapter;
import com.softmed.uzazisalama.util.DatesHelper;
import com.softmed.uzazisalama.util.OrientationHelper;
import com.softmed.uzazisalama.util.Utils;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.SyncStatus;
import org.ei.opensrp.domain.form.FormData;
import org.ei.opensrp.domain.form.FormField;
import org.ei.opensrp.domain.form.FormInstance;
import org.ei.opensrp.domain.form.FormSubmission;
import org.ei.opensrp.drishti.R;
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
import static com.softmed.uzazisalama.util.Utils.generateRandomUUIDString;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PncActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    private static final String TAG = PncActivity.class.getSimpleName();

    private SmartRegisterClientsProvider clientProvider = null;
    private CommonPersonObjectController controller;
    private VillageController villageController;
    private DialogOptionMapper dialogOptionMapper;

    @Bind(R.id.view_pager)
    public    OpenSRPViewPager mPager;
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
        OrientationHelper.setProperOrientationForDevice(PncActivity.this);

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

        mPager.setCurrentItem(4);
        currentPage = 4;

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
        Log.d(TAG,"Location selected ="+locationSelected);
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
                if (formName.equals("pregnant_mothers_registration")) {
                    AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) getDisplayFormFragmentAtIndex(formIndex);
                    if (displayFormFragment != null) {
                        Log.d(TAG, "form data = " + data);
                        displayFormFragment.setFormData(data);
                        displayFormFragment.setRecordId(entityId);
                        displayFormFragment.setFieldOverides(metaData);

                        if((displayFormFragment.getRegistrationType()).equals("2")){
                            displayFormFragment.setEmptyDetails();
                        }
                    }
                } else if(formName.equals("pregnant_mothers_pre_registration")){
                    CHWPreRegisterFormFragment displayFormFragment = (CHWPreRegisterFormFragment) getDisplayFormFragmentAtIndex(formIndex);
                    if (displayFormFragment != null) {
                        Log.d(TAG, "form data = " + data);
                        displayFormFragment.setFormData(data);
                        displayFormFragment.setRecordId(entityId);
                        displayFormFragment.setFieldOverides(metaData);
                    }
                    displayFormFragment.setEmptyDetails();
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
        final PregnantMom pregnantMom = gson.fromJson(formSubmission, PregnantMom.class);

        MotherPersonObject motherPersonObject = new MotherPersonObject(id, null, pregnantMom);
        ContentValues values = new CustomMotherRepository().createValuesFor(motherPersonObject);
        Log.d(TAG, "motherPersonObject = " + gson.toJson(motherPersonObject));
        Log.d(TAG, "values = " + gson.toJson(values));

        CommonRepository commonRepository = context().commonrepository("wazazi_salama_mother");
        commonRepository.customInsert(values);

        CommonPersonObject c = commonRepository.findByCaseID(id);
        List<FormField> formFields = new ArrayList<>();


        formFields.add(new FormField("id", c.getCaseId(), commonRepository.TABLE_NAME + "." + "id"));


        formFields.add(new FormField("relationalid", c.getCaseId(), commonRepository.TABLE_NAME + "." + "relationalid"));

        for ( String key : c.getDetails().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            if(!key.equals("facilityId")) {
                f = new FormField(key, c.getDetails().get(key), commonRepository.TABLE_NAME + "." + key);
            }else{
                f = new FormField(key, c.getDetails().get(key), "facility.id");
            }
            formFields.add(f);
        }

        for ( String key : c.getColumnmaps().keySet() ) {
            Log.d(TAG,"key = "+key);
            FormField f = null;
            if(!key.equals("facilityId")) {
                f = new FormField(key, c.getColumnmaps().get(key), commonRepository.TABLE_NAME + "." + key);
            }else{
                f = new FormField(key, c.getColumnmaps().get(key), "facility.id");
            }

            formFields.add(f);


        }

        Log.d(TAG,"form field = "+ new Gson().toJson(formFields));

        FormData formData = new FormData("wazazi_salama_mother","/model/instance/Wazazi_Salama_ANC_Registration/",formFields,null);
        FormInstance formInstance = new FormInstance(formData,"1");
        FormSubmission submission = new FormSubmission(generateRandomUUIDString(),id,"wazazi_salama_pregnant_mothers_registration",new Gson().toJson(formInstance),"4", SyncStatus.PENDING,"4");
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


    public void switchToBaseFragment(final String data) {
        Log.v(TAG, "switchtobasegragment");
        final int prevPageIndex = currentPage;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(((UzaziSalamaApplication)getApplication()).getUserType()==0) {//for chws
                    CHWSmartRegisterFragment chwSmartRegisterFragment = (CHWSmartRegisterFragment) findFragmentByPosition(3);
                    if (chwSmartRegisterFragment != null) {
                        chwSmartRegisterFragment.refreshListView();
                    }
                    mPager.setCurrentItem(3, true);
                }else {//for supervisors
                    AncSmartRegisterFragment registerFragment = (AncSmartRegisterFragment) findFragmentByPosition(0);
                    if (registerFragment != null) {
                        registerFragment.refreshListView();
                    }
                    mPager.setCurrentItem(0, true);
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
//        retrieveAndSaveUnsubmittedFormData();
    }

    public int getFormIndex(String formName){
        return FormUtils.getIndexForFormName(formName, formNames) + 1;
    }
    public void switchToPage(int pageNumber){
        mPager.setCurrentItem(pageNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
