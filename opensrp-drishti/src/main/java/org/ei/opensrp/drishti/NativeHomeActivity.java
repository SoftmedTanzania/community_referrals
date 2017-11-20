package org.ei.opensrp.drishti;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.commonregistry.ControllerFilterMap;
import org.ei.opensrp.cursoradapter.SmartRegisterQueryBuilder;
import org.ei.opensrp.drishti.Anc.Anc1handler;
import org.ei.opensrp.drishti.Anc.Anc2handler;
import org.ei.opensrp.drishti.Anc.Anc3handler;
import org.ei.opensrp.drishti.Anc.Anc4handler;
import org.ei.opensrp.drishti.Application.UzaziSalamaApplication;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;
import org.ei.opensrp.drishti.util.CustomContext;
import org.ei.opensrp.drishti.util.OrientationHelper;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.service.PendingFormSubmissionService;
import org.ei.opensrp.sync.SyncAfterFetchListener;
import org.ei.opensrp.sync.SyncProgressIndicator;
import org.ei.opensrp.sync.UpdateActionsTask;
import org.ei.opensrp.view.activity.SecuredActivity;
import org.ei.opensrp.view.contract.HomeContext;
import org.ei.opensrp.view.controller.NativeAfterANMDetailsFetchListener;
import org.ei.opensrp.view.controller.NativeUpdateANMDetailsTask;
import org.ei.opensrp.view.fragment.DisplayFormFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.String.valueOf;
import static org.ei.opensrp.event.Event.ACTION_HANDLED;
import static org.ei.opensrp.event.Event.FORM_SUBMITTED;
import static org.ei.opensrp.event.Event.SYNC_COMPLETED;
import static org.ei.opensrp.event.Event.SYNC_STARTED;

public class NativeHomeActivity extends SecuredActivity {
    private NavigationController navigationController1;
    private MenuItem updateMenuItem;
    private MenuItem remainingFormsToSyncMenuItem;
    private PendingFormSubmissionService pendingFormSubmissionService;
    static final String DATABASE_NAME = "drishti.db";

    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            if (updateMenuItem != null) {
                updateMenuItem.setActionView(R.layout.progress);
            }
        }
    };

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


    public static int hhcount;
    private int elcocount;
    private int anccount;
    private int pnccount;
    private int childcount;

    @Override
    protected void onCreation() {
        setContentView(R.layout.smart_registers_home);
        navigationController = new NavigationController(this, anmController);
        navigationController1 = new NavigationController(this, anmController);
        setupViews();
        initialize();
        DisplayFormFragment.formInputErrorMessage = getResources().getString(R.string.forminputerror);
        DisplayFormFragment.okMessage = getResources().getString(R.string.okforminputerror);

        context().formSubmissionRouter().getHandlerMap().put("anc_reminder_visit_1",
                new Anc1handler());
        context().formSubmissionRouter().getHandlerMap().put("anc_reminder_visit_2",
                new Anc2handler());
        context().formSubmissionRouter().getHandlerMap().put("anc_reminder_visit_3",
                new Anc3handler());
        context().formSubmissionRouter().getHandlerMap().put("anc_reminder_visit_4",
                new Anc4handler());

        // orientation
        OrientationHelper.setProperOrientationForDevice(NativeHomeActivity.this);


        String userDetailsString = context().allSettings().settingsRepository.querySetting("userInformation","");
        JSONObject userSettings = null;
        try {
            userSettings = new JSONObject(userDetailsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray roles = null;
        try {
            roles = userSettings.getJSONArray("roles");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int count = roles.length();
        for (int i =0 ; i<count ; i++){
            try {
                if(roles.getString(i).equals("Organizational: Health Facility User")){
                    ((UzaziSalamaApplication)getApplication()).setUserType(0);
                }else if (roles.getString(i).equals("Organizational: CHW")){
                    ((UzaziSalamaApplication)getApplication()).setUserType(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setupViews() {
//        findViewById(R.id.btn_chw_register).setOnClickListener(onRegisterStartListener);
//        findViewById(R.id.btn_pnc_register).setOnClickListener(onRegisterStartListener);
        findViewById(R.id.btn_anc_register).setOnClickListener(onRegisterStartListener);
        findViewById(R.id.btn_pnc_register).setOnClickListener(onRegisterStartListener);
        findViewById(R.id.btn_reporting).setOnClickListener(onRegisterStartListener);
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

    @Override
    protected void onResumption() {
        LoginActivity.setLanguage();
        updateRegisterCounts();
        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
    }

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

    private void updateRegisterCounts(HomeContext homeContext) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder();


//                MotherRepository commonRepository = context().commonrepository("wazazi_salama_mother");
//                if(commonRepository!=null) {
//                    Cursor anccountcursor = context().commonrepository("wazazi_salama_mother").RawCustomQueryForAdapter(sqb.queryForCountOnRegisters("wazazi_salama_mother", "(wazazi_salama_mother.Is_PNC is null or wazazi_salama_mother.Is_PNC = '0') and wazazi_salama_mother.MOTHERS_NAME is not NUll  AND wazazi_salama_mother.MOTHERS_NAME != \"\"  AND wazazi_salama_mother.details  LIKE '%\"IS_VALID\":\"1\"%'"));
//                    anccountcursor.moveToFirst();
//                    anccount = anccountcursor.getInt(0);
//                    anccountcursor.close();


//                Cursor pnccountcursor = context().commonrepository("mcaremother").RawCustomQueryForAdapter(sqb.queryForCountOnRegisters("mcaremother","mcaremother.Is_PNC = '1' and mcaremother.FWWOMFNAME is not NUll  AND mcaremother.FWWOMFNAME != \"\"      AND mcaremother.details  LIKE '%\"FWWOMVALID\":\"1\"%'"));
//                pnccountcursor.moveToFirst();
//                pnccount= pnccountcursor.getInt(0);
//                pnccountcursor.close();


                    Handler mainHandler = new Handler(getMainLooper());

                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
//                        pncRegisterClientCountView.setText(valueOf(pnccount));
//                        ecRegisterClientCountView.setText(valueOf(hhcount));
//                        ancRegisterClientCountView.setText(valueOf(anccount));
//                        fpRegisterClientCountView.setText(valueOf(elcocount));
//                        childRegisterClientCountView.setText(valueOf(childcount));
                        }
                    };
                    mainHandler.post(myRunnable);
//                } else {
//
//                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        attachLogoutMenuItem(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateMenuItem = menu.findItem(R.id.updateMenuItem);
        remainingFormsToSyncMenuItem = menu.findItem(R.id.remainingFormsToSyncMenuItem);

        updateSyncIndicator();
        updateRemainingFormsToSyncCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateMenuItem:
                updateFromServer();
                return true;
            case R.id.switchLanguageMenuItem:
                String newLanguagePreference = LoginActivity.switchLanguagePreference();
                LoginActivity.setLanguage();
                Toast.makeText(this, "Language preference set to " + newLanguagePreference + ". Please restart the application.", LENGTH_SHORT).show();
                this.recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateFromServer() {
        UpdateActionsTask updateActionsTask = new UpdateActionsTask(
                this, context().actionService(), context().formSubmissionSyncService(),
                new SyncProgressIndicator(), context().allFormVersionSyncService());
        updateActionsTask.updateFromServer(new SyncAfterFetchListener());
    }

    @Override
    protected void onDestroy() {
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

    private View.OnClickListener onRegisterStartListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btn_anc_register:
                    navigationController.startANCSmartRegistry();
                    break;

                case R.id.btn_pnc_register:
                    navigationController.startPNCSmartRegistry();
                    break;

//                case R.id.btn_chw_register:
//                    navigationController1.startCHWSmartRegistry();
//                    break;

//                case R.id.btn_reporting:
//                    navigationController.startFPSmartRegistry();
//                    break;
            }
        }
    };

    private View.OnClickListener onButtonsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reporting:
//                    navigationController.startReports();
                    break;

//                case R.id.btn_videos:
//                    navigationController.startVideos();
//                    break;
            }
        }
    };

    class pncControllerfiltermap extends ControllerFilterMap {

        @Override
        public boolean filtermapLogic(CommonPersonObject commonPersonObject) {
            boolean returnvalue = false;
            if (commonPersonObject.getDetails().get("IS_VALID") != null) {
                if (commonPersonObject.getDetails().get("IS_VALID").equalsIgnoreCase("1")) {
                    returnvalue = true;
                    if (commonPersonObject.getDetails().get("Is_PNC") != null) {
                        if (commonPersonObject.getDetails().get("Is_PNC").equalsIgnoreCase("1")) {
                            returnvalue = true;
                        }

                    } else {
                        returnvalue = false;
                    }
                }
            }
            Log.v("the filter", "" + returnvalue);
            return returnvalue;
        }
    }

    class ancControllerfiltermap extends ControllerFilterMap {

        @Override
        public boolean filtermapLogic(CommonPersonObject commonPersonObject) {
            boolean returnvalue = false;
            if (commonPersonObject.getDetails().get("IS_VALID") != null) {
                if (commonPersonObject.getDetails().get("IS_VALID").equalsIgnoreCase("1")) {
                    returnvalue = true;
                    if (commonPersonObject.getDetails().get("Is_PNC") != null) {
                        if (commonPersonObject.getDetails().get("Is_PNC").equalsIgnoreCase("1")) {
                            returnvalue = false;
                        }

                    }
                }
            }
            Log.v("the filter", "" + returnvalue);
            return returnvalue;
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
}
