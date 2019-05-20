package com.softmed.htmr_chw.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw.BuildConfig;
import com.softmed.htmr_chw.Domain.ClientReferral;
import com.softmed.htmr_chw.Domain.LocationSelectorDialogFragment;
import com.softmed.htmr_chw.Fragments.CBHSClientsListFragment;
import com.softmed.htmr_chw.Fragments.CHWSmartRegisterFragment;
import com.softmed.htmr_chw.Fragments.FollowupReferralsFragment;
import com.softmed.htmr_chw.Fragments.ReferralClientsListFragment;
import com.softmed.htmr_chw.Fragments.ReferralsListFragment;
import com.softmed.htmr_chw.Fragments.ReportFragment;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.util.FitDoughnut;
import com.softmed.htmr_chw.util.NavigationController;

import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ReferralFeedback;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.util.FormUtils;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import fr.ganfra.materialspinner.MaterialSpinner;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.softmed.htmr_chw.Activities.LoginActivity.ENGLISH_LOCALE;
import static java.lang.String.valueOf;
import static org.ei.opensrp.event.Event.ACTION_HANDLED;
import static org.ei.opensrp.event.Event.FORM_SUBMITTED;
import static org.ei.opensrp.event.Event.SYNC_COMPLETED;
import static org.ei.opensrp.event.Event.SYNC_STARTED;

public class ChwSmartRegisterActivity extends SecuredNativeSmartRegisterActivity implements LocationSelectorDialogFragment.OnLocationSelectedListener {
    static final String DATABASE_NAME = "drishti.db";
    private static final String TAG = ChwSmartRegisterActivity.class.getSimpleName();
    public static MaterialSpinner spinnerReason, spinnerClientAvailable;
    public static int availableSelection = -1, reasonSelection = -1;
    private static boolean isOnTheMainMenu;
    public TabLayout myTabLayout;
    @Bind(R.id.view_pager)
    public OpenSRPViewPager mPager;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private String message = "";
    private RelativeLayout pendingForm;
    private TextView successView, unsuccessView;
    private String[] formNames = new String[]{};
    private Fragment mBaseFragment;
    private Gson gson = new Gson();
    private CommonRepository commonRepository;
    private Cursor cursor;
    private Toolbar toolbar;
    private ScrollView mainMenu;
    private View fragmentsView;
    private TextView pending;
    private FitDoughnut donutChart;
    private MenuItem updateMenuItem;
    private PendingFormSubmissionService pendingFormSubmissionService;
    private LinearLayout tabsLayout;
    private Typeface robotoBold, sansBold;
    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            //#TODO: RemainingFormsToSyncCount cannot be updated from a back ground thread!!
            updateRemainingFormsToSyncCount();
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(null);
            }
            updateRegisterCounts();
            refreshListView();
        }
    };
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chwregister);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String cbhsNumber = context().allSharedPreferences().fetchCBHS();
        if (cbhsNumber == null || cbhsNumber.equals("")) {
            startActivity(new Intent(ChwSmartRegisterActivity.this, SettingsActivity.class));
        }


        robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        sansBold = Typeface.createFromAsset(getAssets(), "google_sans_bold.ttf");

        setValuesInBoreshaAfya();
        formNames = this.buildFormNameList();
        mBaseFragment = new CHWSmartRegisterFragment();

        navigationController = new NavigationController(this, anmController);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onInitialization();

        ImageButton imageButton = (ImageButton) findViewById(R.id.register_client);
        tabsLayout = (LinearLayout) findViewById(R.id.tabs);

        isOnTheMainMenu = true;
        mainMenu = (ScrollView) findViewById(R.id.main_menu);
        fragmentsView = findViewById(R.id.fragments);
        View clientRegistration = mainMenu.findViewById(R.id.referral_registration_card);
        View clientList = mainMenu.findViewById(R.id.referral_clients_list_card);
        View cbhsClientsList = mainMenu.findViewById(R.id.cbhs_clients_list_card);
        View issuedReferrals = mainMenu.findViewById(R.id.issued_referral_list_card);
        View receivedReferralList = mainMenu.findViewById(R.id.received_referrals_list_card);
        View reports = mainMenu.findViewById(R.id.reports);

        Typeface robotoBold = Typeface.createFromAsset(getAssets(), "roboto_bold.ttf");
        Typeface rosarioRegular = Typeface.createFromAsset(getAssets(), "rosario_regular.ttf");

        TextView registerClientTitle = (TextView) findViewById(R.id.register_client_title);
        TextView clientListTitle = (TextView) findViewById(R.id.client_list_title);
        TextView referralclientListTitle = (TextView) findViewById(R.id.referral_client_list_title);
        TextView referalListTitle = (TextView) findViewById(R.id.referal_list_title);
        TextView referralsListTitle = (TextView) findViewById(R.id.referrals_list_title);
        TextView referedClientsTitle = (TextView) findViewById(R.id.refered_clients_title);


        TextView registerClientDesc = (TextView) findViewById(R.id.register_client_desc);
        TextView clientListDesc = (TextView) findViewById(R.id.client_list_desc);
        TextView referralClientListDesc = (TextView) findViewById(R.id.referral_client_list_desc);
        TextView referalListDesc = (TextView) findViewById(R.id.referal_list_desc);
        TextView referralsListDesc = (TextView) findViewById(R.id.referrals_list_desc);
        TextView referedClientsDesc = (TextView) findViewById(R.id.refered_clients_desc);

        registerClientTitle.setTypeface(robotoBold);
        clientListTitle.setTypeface(robotoBold);
        referralclientListTitle.setTypeface(robotoBold);
        referalListTitle.setTypeface(robotoBold);
        referralsListTitle.setTypeface(robotoBold);
        referedClientsTitle.setTypeface(robotoBold);

        registerClientDesc.setTypeface(rosarioRegular);
        clientListDesc.setTypeface(rosarioRegular);
        referalListDesc.setTypeface(rosarioRegular);
        referralClientListDesc.setTypeface(rosarioRegular);
        referralsListDesc.setTypeface(rosarioRegular);
        referedClientsDesc.setTypeface(rosarioRegular);



        try {
            Log.d(TAG, "Setting build date");
            Date buildDate = BuildConfig.BUILD_TIME;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            TextView buildDateTextView = (TextView) findViewById(R.id.build_date);
            buildDateTextView.setText(getString(R.string.build_on_label) + dateFormat.format(buildDate));


            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView versionTextView = (TextView) findViewById(R.id.version);
            versionTextView.setText(getString(R.string.version_label) + version);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final FragmentManager fragmentManager = this.getSupportFragmentManager();
        clientRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });

        cbhsClientsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabsLayout.removeAllViews();

                View tabs = getLayoutInflater().inflate(R.layout.tab_layout, null);
                myTabLayout = tabs.findViewById(R.id.tabs);
                tabsLayout.addView(tabs);

                isOnTheMainMenu = false;
                CBHSClientsListFragment clientsFragment = new CBHSClientsListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, clientsFragment, "tag");
                transaction.addToBackStack(null);
                transaction.commit();


                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });

        clientList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabsLayout.removeAllViews();

                View tabs = getLayoutInflater().inflate(R.layout.tab_layout, null);
                myTabLayout = tabs.findViewById(R.id.tabs);
                tabsLayout.addView(tabs);

                isOnTheMainMenu = false;
                ReferralClientsListFragment clientsFragment = new ReferralClientsListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, clientsFragment, "tag");
                transaction.addToBackStack(null);
                transaction.commit();


                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });


        issuedReferrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabsLayout.removeAllViews();
                LinearLayout tabLinearLayout2 = (LinearLayout) LayoutInflater.from(ChwSmartRegisterActivity.this).inflate(R.layout.custom_tab, null);
                TextView tabContent2 = (TextView) tabLinearLayout2.findViewById(R.id.tabContent);
                ImageView tabImage = tabLinearLayout2.findViewById(R.id.tabImage);
                tabContent2.setText(R.string.sent_referrals_label);
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_up_white_24dp));
                tabsLayout.addView(tabLinearLayout2);

                isOnTheMainMenu = false;
                ReferralsListFragment newFragment = new ReferralsListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, newFragment, "tag1");
                transaction.addToBackStack(null);
                transaction.commit();

                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });

        receivedReferralList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabsLayout.removeAllViews();
                LinearLayout tabLinearLayout1 = (LinearLayout) LayoutInflater.from(ChwSmartRegisterActivity.this).inflate(R.layout.custom_tab, null);
                TextView tabContent1 = (TextView) tabLinearLayout1.findViewById(R.id.tabContent);
                ImageView tabImage = tabLinearLayout1.findViewById(R.id.tabImage);
                tabContent1.setText(R.string.received_referrals_label);
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_trending_down_white_24dp));
                tabsLayout.addView(tabLinearLayout1);

                isOnTheMainMenu = false;

                FollowupReferralsFragment newFragment = new FollowupReferralsFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, newFragment, "followup_fragment");
                transaction.addToBackStack(null);
                transaction.commit();

                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOnTheMainMenu = false;


                tabsLayout.removeAllViews();
                LinearLayout tabLinearLayout3 = (LinearLayout) LayoutInflater.from(ChwSmartRegisterActivity.this).inflate(R.layout.custom_tab, null);
                TextView tabContent3 = (TextView) tabLinearLayout3.findViewById(R.id.tabContent);
                ImageView tabImage = tabLinearLayout3.findViewById(R.id.tabImage);
                tabContent3.setText(R.string.reports_label);
                tabImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_event_note_white_24dp));
                tabsLayout.addView(tabLinearLayout3);

                ReportFragment newFragment = new ReportFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();

            }
        });

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView tabImage = tabLinearLayout.findViewById(R.id.tabImage);
        TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
        tabContent.setText(getResources().getString(R.string.main_menu_label));

        tabImage.setImageDrawable(getResources().getDrawable(R.drawable.sharp_menu_white_48dp));
        tabsLayout.addView(tabLinearLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case com.softmed.htmr_chw.R.id.updateMenuItem:
                                onOptionsItemSelected(item);
                                return true;
                            case com.softmed.htmr_chw.R.id.switchLanguageMenuItem:
                                String newLanguagePreference = LoginActivity.switchLanguagePreference();
                                LoginActivity.setLanguage();
                                Toast.makeText(ChwSmartRegisterActivity.this, "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
                                recreate();
                                return true;
                            case com.softmed.htmr_chw.R.id.setCBHSnumberMenuItem:
                                startActivity(new Intent(ChwSmartRegisterActivity.this, SettingsActivity.class));
                                return true;
                            default:
                                return onOptionsItemSelected(item);
                        }
                    }
                });

        TextView username = (TextView) findViewById(R.id.toolbar_user_name);
        pendingForm = (RelativeLayout) findViewById(R.id.key_three);
        pending = (TextView) findViewById(R.id.count_three);
        username.setText(getResources().getString(R.string.logged_user) + " " + ((BoreshaAfyaApplication) this.getApplication()).getUsername());
        successView = (TextView) findViewById(R.id.count_one);
        unsuccessView = (TextView) findViewById(R.id.count_two);

        donutChart = (FitDoughnut) findViewById(R.id.donutChart);
        donutChart.startAnimateLoading();
        updateFromServer();


    }

    private void initialize() {

        LoginActivity.setLanguage();
    }

    protected void onResumption() {
        LoginActivity.setLanguage();
        updateRegisterCounts();
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
        refreshListView();
    }

    public void refreshListView() {
        try {
            ReferralsListFragment referralsListFragment = (ReferralsListFragment) getSupportFragmentManager().findFragmentByTag("tag");
            ;
            referralsListFragment.populateData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FollowupReferralsFragment followupReferralsFragment = (FollowupReferralsFragment) getSupportFragmentManager().findFragmentByTag("tag");
            followupReferralsFragment.populateData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateRemainingFormsToSyncCount() {

        try {
            long size = pendingFormSubmissionService.pendingFormSubmissionCount();
            Log.d(TAG, "pending from submission =" + size);
            if (size > 0) {
                pending.setText(valueOf(size) + " " + getString(R.string.unsynced_forms_count_message));
                pendingForm.setVisibility(View.VISIBLE);
            } else {
                pendingForm.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSyncIndicator() {

        if (updateMenuItem != null) {
            if (context().allSharedPreferences().fetchIsSyncInProgress()) {
                updateMenuItem.setActionView(R.layout.progress);
            } else
                updateMenuItem.setActionView(null);
        }

    }

    public void updateRegisterCounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final long successfullCount = context().allBeneficiaries().successCount();
                final long unsuccessfullCount = context().allBeneficiaries().unsuccessCount();

                Handler mainHandler = new Handler(getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        successView = (TextView) findViewById(R.id.count_one);
                        unsuccessView = (TextView) findViewById(R.id.count_two);
                        successView.setText(valueOf(successfullCount));
                        unsuccessView.setText(valueOf(unsuccessfullCount));


                        float v = 0.0f;
                        try {
                            v = (successfullCount * 1.0f) / (successfullCount + unsuccessfullCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "donutchart value = " + v);
                        donutChart.stopAnimateLoading(v * 100);
                    }
                };
                mainHandler.post(myRunnable);

            }
        }).start();
    }

    public void showPreRegistrationDetailsDialog(ClientReferral clientReferral) {

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();

        final View dialogView = getLayoutInflater().inflate(R.layout.view_referral_summary, null);

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
            Integer ageInt = age;
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
        TextView testStatus = (TextView) dialogView.findViewById(R.id.testStatus);
        testStatus.setTypeface(sansBold);

        //TODO remove this hardcoding of services
        if (clientReferral.getReferral_service_id().equals("1") || clientReferral.getReferral_service_id().equals("2"))
            testStatus.setVisibility(VISIBLE);

        if (clientReferral.getTest_results() != null && !clientReferral.getTest_results().equals("")) {
            if (clientReferral.getTest_results().equals("1")) {
                testStatus.setText(R.string.test_results_positive);
                testStatus.setTextColor(getResources().getColor(R.color.green_500));
            } else {
                testStatus.setText(R.string.test_results_negative);
                testStatus.setTextColor(getResources().getColor(R.color.red_500));
            }
        }

        Log.d(TAG, "referral test results = " + clientReferral.getTest_results());
        try {
            if (clientReferral.getReferral_status().equals("1") && !clientReferral.getReferral_feedback().equals("")) {
                dialogView.findViewById(R.id.referral_feedback_title).setVisibility(VISIBLE);
                dialogView.findViewById(R.id.referral_feedback).setVisibility(VISIBLE);
                dialogView.findViewById(R.id.strip_six).setVisibility(VISIBLE);
                TextView referralFeedback = (TextView) dialogView.findViewById(R.id.referral_feedback);
                referralFeedback.setVisibility(VISIBLE);

                ReferralFeedback feedback = context().referralFeedbackRepository().find(clientReferral.getReferral_feedback());

                if (feedback != null) {
                    if (ENGLISH_LOCALE.equals(preferredLocale)) {
                        referralFeedback.setText(feedback.getDesc());
                    } else {
                        referralFeedback.setText(feedback.getDescSw());
                    }
                }
            }
        } catch (Exception e) {
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


        textName.setText(clientReferral.getFirst_name() + " " + clientReferral.getMiddle_name() + " " + clientReferral.getSurname());

        textAge.setText(ageS + " years");
        cbhs.setText(clientReferral.getCommunity_based_hiv_service());

        try {
            referral_service.setText(getReferralServiceName(clientReferral.getReferral_service_id()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "facility id = " + clientReferral.getFacility_id());

        facility.setText(getFacilityName(clientReferral.getFacility_id()));

        try {
            if (!clientReferral.getCtc_number().isEmpty())
                ctc_number.setText(clientReferral.getCtc_number());
            else
                ctc_number.setText("-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        referral_reason.setText(clientReferral.getReferral_reason());
        phoneNumber.setText(clientReferral.getPhone_number());
        villageleader.setText(clientReferral.getVillage_leader());
        physicalAddress.setText(clientReferral.getVillage());

        try {
            if ((clientReferral.getGender()).equalsIgnoreCase("female")) {
                gender.setText(getResources().getString(R.string.female));
            } else {
                gender.setText(getResources().getString(R.string.male));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setIndicators(((LinearLayout) dialogView.findViewById(R.id.flags_layout)), clientReferral.getIndicator_ids());
    }

    private void makeToast() {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG).show();
    }

    public String getFacilityName(String id) {

        Log.d(TAG, "Facility Id = " + id);
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

    public void setIndicators(LinearLayout view, String object) {
        try {
            JSONArray indicatorsArray = new JSONArray(object);
            view.removeAllViewsInLayout();

            for (int m = 0; m < indicatorsArray.length(); m++) {
                View v = getLayoutInflater().inflate(R.layout.indicator_item, null);
                TextView indicatorName = v.findViewById(R.id.indicator_name);
                indicatorName.setText(getIndicatorName(indicatorsArray.getString(m)));
                indicatorName.setPadding(0, 10, 10, 0);
                view.addView(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getIndicatorName(String id) {

        Log.d(TAG, "indicatorId = " + id);
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM indicator where referralServiceIndicatorId ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "indicator");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        return commonPersonObjectList.get(0).getColumnmaps().get("indicatorName");
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
    protected NavBarOptionsProvider getNavBarOptionsProvider() {
        return null;
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {
        pendingFormSubmissionService = context().pendingFormSubmissionService();
        SYNC_STARTED.addListener(onSyncStartListener);
        SYNC_COMPLETED.addListener(onSyncCompleteListener);
        FORM_SUBMITTED.addListener(onFormSubmittedListener);
        ACTION_HANDLED.addListener(updateANMDetailsListener);
        updateRegisterCounts();
    }

    @Override
    public void startRegistration() {
        Log.d(TAG, "starting registrations");
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        String locationDialogTAG = "locationDialogTAG";
        android.app.Fragment prev = getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance(this, null, context().anmLocationController().get(),
                        "referral_registration")
                .show(ft, locationDialogTAG);
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

        Intent intent = new Intent(this, ClientRegistrationFormActivity.class);
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
        Log.d(TAG, "BackPressed");


        if (!isOnTheMainMenu) {

            tabsLayout.removeAllViews();
            LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            ImageView tabImage = tabLinearLayout.findViewById(R.id.tabImage);
            TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
            tabContent.setText(getResources().getString(R.string.main_menu_label));

            tabImage.setImageDrawable(getResources().getDrawable(R.drawable.sharp_menu_white_48dp));
            tabsLayout.addView(tabLinearLayout);


            Log.d(TAG, "BackPressed true");
            mainMenu.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.fragments)).commit();

            fragmentsView.setVisibility(View.GONE);

        } else {
            super.onBackPressed(); // allow back key only if we are
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateMenuItem:
                donutChart.startAnimateLoading();
                updateMenuItem = item;
                updateFromServer();
                if (context().allSharedPreferences().fetchIsSyncInProgress()) {
                    Log.d(TAG, "am in sync progress");
                    item.setActionView(R.layout.progress);
                } else {
                    item.setActionView(null);
                    Log.d(TAG, "am in sync progress after");
                }

                return true;

//            case R.id.help:
//                Toast.makeText(getActivity(), "help implementation under construction", LENGTH_SHORT).show();
//                return true;

            case R.id.logout:
                ((BoreshaAfyaApplication) getApplication()).logoutCurrentUser();

                return true;
            case R.id.switchLanguageMenuItem:
                String newLanguagePreference = context().userService().switchLanguagePreference();

                LoginActivity.setLanguage();
                Toast.makeText(ChwSmartRegisterActivity.this, "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
