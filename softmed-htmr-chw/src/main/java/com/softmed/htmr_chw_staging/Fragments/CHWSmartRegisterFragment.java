package com.softmed.htmr_chw_staging.Fragments;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.softmed.htmr_chw_staging.Activities.ChwSmartRegisterActivity;
import com.softmed.htmr_chw_staging.Activities.LoginActivity;
import com.softmed.htmr_chw_staging.Application.BoreshaAfyaApplication;
import com.softmed.htmr_chw_staging.Domain.LocationSelectorDialogFragment;
import com.softmed.htmr_chw_staging.R;
import com.softmed.htmr_chw_staging.util.FitDoughnut;
import com.softmed.htmr_chw_staging.util.NavigationController;

import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import static android.os.Looper.getMainLooper;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.valueOf;
import static org.ei.opensrp.event.Event.ACTION_HANDLED;
import static org.ei.opensrp.event.Event.FORM_SUBMITTED;
import static org.ei.opensrp.event.Event.SYNC_COMPLETED;
import static org.ei.opensrp.event.Event.SYNC_STARTED;

public class CHWSmartRegisterFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = CHWSmartRegisterFragment.class.getSimpleName();
    private static boolean isOnTheMainMenu;
    private String locationDialogTAG = "locationDialogTAG";
    private TabLayout tabs;
    private ImageButton imageButton;
    private Toolbar toolbar;
    private View v;
    private TextView pending;
    private TextView successView, unsuccessView;
    private FitDoughnut donutChart;
    private MenuItem updateMenuItem;
    private PendingFormSubmissionService pendingFormSubmissionService;
    private RelativeLayout pendingForm;
    private ScrollView mainMenu;
    private View fragmentsView;
    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            updateRemainingFormsToSyncCount();
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(null);
            }
            updateRegisterCounts();
            refreshListView();
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
    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(R.layout.progress);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navigationController = new NavigationController(getActivity(), anmController);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        v = inflater.inflate(R.layout.activity_chwregister, container, false);
        onInitialization();
        setHasOptionsMenu(true);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        imageButton = (ImageButton) v.findViewById(R.id.register_client);


        isOnTheMainMenu = true;
        mainMenu = (ScrollView) v.findViewById(R.id.main_menu);
        fragmentsView = v.findViewById(R.id.fragments);
        View referralRegistration = mainMenu.findViewById(R.id.referral_registration_card);
        View issuedReferrals = mainMenu.findViewById(R.id.issued_referral_list_card);
        View receivedReferralList = mainMenu.findViewById(R.id.received_referrals_list_card);
        View reports = mainMenu.findViewById(R.id.reports);

        referralRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });
        issuedReferrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isOnTheMainMenu = false;
                ReferralsListFragment newFragment = new ReferralsListFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, newFragment, "tag");
                transaction.addToBackStack(null);
                transaction.commit();

                mainMenu.setVisibility(View.GONE);
                fragmentsView.setVisibility(View.VISIBLE);
            }
        });

        receivedReferralList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isOnTheMainMenu = false;

                FollowupReferralsFragment newFragment = new FollowupReferralsFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragments, newFragment);
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

        tabs = (TabLayout) v.findViewById(R.id.tabs);


        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

        TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
        tabContent.setText(R.string.received_referrals_label);
        tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_down_white_24dp, 0, 0, 0);


        LinearLayout tabLinearLayout2 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabContent2 = (TextView) tabLinearLayout2.findViewById(R.id.tabContent);
        tabContent2.setText(R.string.sent_referrals_label);
        tabContent2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_up_white_24dp, 0, 0, 0);


        LinearLayout tabLinearLayout3 = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        TextView tabContent3 = (TextView) tabLinearLayout3.findViewById(R.id.tabContent);
        tabContent3.setText(R.string.reports_label);
        tabContent3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_note_white_24dp, 0, 0, 0);


        final int colorWhite = ContextCompat.getColor(getActivity(), android.R.color.white);
        final int colorPrimaryLight = ContextCompat.getColor(getActivity(), R.color.primary_light);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getIcon() != null)
                    tab.getIcon().setColorFilter(colorPrimaryLight, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);


        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case com.softmed.htmr_chw_staging.R.id.updateMenuItem:
                                onOptionsItemSelected(item);
                                return true;
                            case com.softmed.htmr_chw_staging.R.id.switchLanguageMenuItem:
                                String newLanguagePreference = LoginActivity.switchLanguagePreference();
                                LoginActivity.setLanguage();
                                Toast.makeText(getActivity(), "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
                                getActivity().recreate();
                                return true;
                            default:
                                return onOptionsItemSelected(item);
                        }
                    }
                });

        TextView username = (TextView) v.findViewById(R.id.toolbar_user_name);
        pendingForm = (RelativeLayout) v.findViewById(R.id.key_three);
        pending = (TextView) v.findViewById(R.id.count_three);
        username.setText(getResources().getString(R.string.logged_user) + " " + ((BoreshaAfyaApplication) getActivity().getApplication()).getUsername());
        successView = (TextView) v.findViewById(R.id.count_one);
        unsuccessView = (TextView) v.findViewById(R.id.count_two);

        donutChart = (FitDoughnut) v.findViewById(R.id.donutChart);
        donutChart.startAnimateLoading();
        updateFromServer();

        return v;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return null;
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.NavBarOptionsProvider getNavBarOptionsProvider() {
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

    protected void onResumption() {
        updateRegisterCounts();
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
        refreshListView();
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
                        successView = (TextView) v.findViewById(R.id.count_one);
                        unsuccessView = (TextView) v.findViewById(R.id.count_two);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "am in preparationOptionMenu");
        super.onPrepareOptionsMenu(menu);
        updateMenuItem = menu.findItem(R.id.updateMenuItem);
        updateRemainingFormsToSyncCount();

    }

    public void updateFromServer() {
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                getActivity(), context().actionService(), context().formSubmissionSyncService(),
                new SyncProgressIndicator(), context().allFormVersionSyncService());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());

        updateRemainingFormsToSyncCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SYNC_STARTED.removeListener(onSyncStartListener);
        SYNC_COMPLETED.removeListener(onSyncCompleteListener);
        FORM_SUBMITTED.removeListener(onFormSubmittedListener);
        ACTION_HANDLED.removeListener(updateANMDetailsListener);
    }

    private void updateSyncIndicator() {

        if (updateMenuItem != null) {
            if (context().allSharedPreferences().fetchIsSyncInProgress()) {
                updateMenuItem.setActionView(R.layout.progress);
            } else
                updateMenuItem.setActionView(null);
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

    @Override
    public void startRegistration() {
        Log.d(TAG, "starting registrations");
        android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance((ChwSmartRegisterActivity) getActivity(), null, context().anmLocationController().get(),
                        "referral_registration")
                .show(ft, locationDialogTAG);
    }


    @Override
    protected void onCreation() {

    }

    @Override
    public void refreshListView() {
        try {
            ReferralsListFragment referralsListFragment = (ReferralsListFragment) getFragmentManager().findFragmentByTag("tag");
            ;
            referralsListFragment.populateData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FollowupReferralsFragment followupReferralsFragment = (FollowupReferralsFragment) getFragmentManager().findFragmentByTag("tag");
            followupReferralsFragment.populateData();

        } catch (Exception e) {
            e.printStackTrace();
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
                ((BoreshaAfyaApplication) getActivity().getApplication()).logoutCurrentUser();

                return true;
            case R.id.switchLanguageMenuItem:
                String newLanguagePreference = context().userService().switchLanguagePreference();

                LoginActivity.setLanguage();
                Toast.makeText(getActivity(), "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onBackPressed() {
        Log.d(TAG, "BackPressed");
        if (!isOnTheMainMenu) {
            Log.d(TAG, "BackPressed true");
            v.findViewById(R.id.main_menu).setVisibility(View.VISIBLE);
            mainMenu.setVisibility(View.GONE);
            fragmentsView.setVisibility(View.GONE);
            return false;
        }
        return true;
    }


}
