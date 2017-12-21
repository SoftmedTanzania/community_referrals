package org.ei.opensrp.drishti;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Facility;
import org.ei.opensrp.domain.LoginResponse;
import org.ei.opensrp.domain.Response;
import org.ei.opensrp.domain.ResponseStatus;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;

import org.ei.opensrp.domain.ReferralServiceDataModel;

import org.ei.opensrp.drishti.Application.Config;
import org.ei.opensrp.drishti.Service.RegistrationIntentService;
import org.ei.opensrp.drishti.util.AsyncTask;
import org.ei.opensrp.drishti.util.SmallDiagonalCutPathDrawable;
import org.ei.opensrp.repository.FacilityRepository;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.drishti.Service.FacilityService;
import org.ei.opensrp.event.Listener;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.sync.DrishtiSyncScheduler;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;
import org.ei.opensrp.view.activity.SettingsActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static org.ei.opensrp.domain.LoginResponse.NO_INTERNET_CONNECTIVITY;
import static org.ei.opensrp.domain.LoginResponse.SUCCESS;
import static org.ei.opensrp.domain.LoginResponse.UNAUTHORIZED;
import static org.ei.opensrp.domain.LoginResponse.UNKNOWN_RESPONSE;
import static org.ei.opensrp.util.Log.logError;
import static org.ei.opensrp.util.Log.logVerbose;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressDialog progressDialog;
    private FacilityService facilityService;
    private List<ReferralServiceDataModel> serviceData;
    private ReferralServiceRepository serviceRepository;
    private FacilityRepository facilityRepository;
    private CommonRepository commonRepository;
    public static final String ENGLISH_LOCALE = "en";
    public static final String KANNADA_LOCALE = "kn";
    public static final String BENGALI_LOCALE = "bn";
    public static final String ENGLISH_LANGUAGE = "English";
    public static final String KANNADA_LANGUAGE = "Kannada";
    public static final String Bengali_LANGUAGE = "Bengali";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    AsyncTask<Void, Void, Void> mRegisterTask;
    private ArrayList<Facility> referralList;
    private ArrayList<ReferralServiceDataModel> serviceDataModelArrayList;

    Intent intent ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logVerbose("Initializing ...");
        try {
            AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(this));
            Gson gson = new Gson();
            android.util.Log.d(TAG, "sharedPreference = " + gson.toJson(allSharedPreferences));

            String preferredLocale = allSharedPreferences.fetchLanguagePreference();
            Resources res = Context.getInstance().applicationContext().getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale(preferredLocale);
             res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);


        context = Context.getInstance().updateApplicationContext(this.getApplicationContext());
        initializeLoginFields();
        setDoneActionHandlerOnPasswordField();
        initializeProgressDialog();

//        findViewById(R.id.credential_card).setBackground(new SmallDiagonalCutPathDrawable());

        ImageView v = (ImageView)findViewById(R.id.background);
//        Glide.with(getApplicationContext()).load(R.drawable.clint_adair).into(v);



        setLanguage();

    }
    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add("Settings");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase("Settings")) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeBuildDetails() {
        TextView buildDetailsTextView = (TextView) findViewById(org.ei.opensrp.R.id.login_build);
        try {
            buildDetailsTextView.setText("Version " + getVersion() + ", Built on: " + getBuildDate());
        } catch (Exception e) {
            logError("Error fetching build details: " + e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!context.IsUserLoggedOut()) {
            goToHome();
        }

        fillUserIfExists();
        registerReceiver();
    }
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {

        if (Config.YOUR_SERVER_URL == null
                || Config.GOOGLE_SENDER_ID == null
                || Config.YOUR_SERVER_URL.length() == 0
                || Config.GOOGLE_SENDER_ID.length() == 0) {

            // GCM sernder id / server url is missing
            ((BoreshaAfyaApplication)getApplication()).showAlertDialog(context, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);

            // stop executing code by return
            return false;
        }
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.logDebug( "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public void login(final View view) {
//        hideKeyboard();
        view.setClickable(false);


        final String userName = userNameEditText.getText().toString();

        final String password = passwordEditText.getText().toString();


        if (context.userService().hasARegisteredUser()) {
            localLogin(view, userName, password);
        } else {
            remoteLogin(view, userName, password);
        }
    }

    private void initializeLoginFields() {

        userNameEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.buttonSignIn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check input fields first
                if (isLoginInitiateOk())
                    login(loginButton);
            }
        });
    }

    private void setDoneActionHandlerOnPasswordField() {
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // check input fields first
                    if (isLoginInitiateOk())
                        login(loginButton);
                }
                return false;
            }
        });
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(org.ei.opensrp.R.string.loggin_in_dialog_title));
        progressDialog.setMessage(getString(org.ei.opensrp.R.string.loggin_in_dialog_message));
    }

    private void localLogin(View view, String userName, String password) {
        if (context.userService().isValidLocalLogin(userName, password)) {
            localLoginWith(userName, password);
        } else {
            android.util.Log.d("login","am in local login");
            showErrorDialog(getString(org.ei.opensrp.R.string.login_failed_dialog_message));
            view.setClickable(true);
        }
    }

    private void remoteLogin(final View view, final String userName, final String password) {
        tryRemoteLogin(userName, password, new Listener<LoginResponse>() {
            public void onEvent(LoginResponse loginResponse) {
                android.util.Log.d("login","am in remote login");
                if (loginResponse == SUCCESS) {
                    remoteLoginWith(userName, password, loginResponse.payload());

                } else {
                    if (loginResponse == null) {
                        showErrorDialog("Login failed. Unknown reason. Try Again");
                    } else {
                        if (loginResponse == NO_INTERNET_CONNECTIVITY) {
                            showErrorDialog(getResources().getString(R.string.no_internet_connectivity));
                        } else if (loginResponse == UNKNOWN_RESPONSE) {
                            showErrorDialog(getResources().getString(R.string.unknown_response));
                        } else if (loginResponse == UNAUTHORIZED) {
                            showErrorDialog(getResources().getString(R.string.unauthorized));
                        }
//                        showErrorDialog(loginResponse.message());
                    }
                    view.setClickable(true);
                }
            }
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_failed_dialog_title))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();
    }

    private void getLocation() {
        tryGetLocation(new Listener<Response<String>>() {
            @Override
            public void onEvent(Response<String> data) {
                if (data.status() == ResponseStatus.success) {
                    context.userService().saveAnmLocation(data.payload());
                }
            }
        });
    }

    private void tryGetLocation(final Listener<Response<String>> afterGet) {
        LockingBackgroundTask task = new LockingBackgroundTask(new ProgressIndicator() {
            @Override
            public void setVisible() {
            }

            @Override
            public void setInvisible() {
                Log.logInfo("Successfully get location");
            }
        });

        task.doActionInBackground(new BackgroundAction<Response<String>>() {
            @Override
            public Response<String> actionToDoInBackgroundThread() {
                return context.userService().getLocationInformation();
            }

            @Override
            public void postExecuteInUIThread(Response<String> result) {
                afterGet.onEvent(result);
            }
        });
    }

    private void tryRemoteLogin(final String userName, final String password, final Listener<LoginResponse> afterLoginCheck) {
        LockingBackgroundTask task = new LockingBackgroundTask(new ProgressIndicator() {
            @Override
            public void setVisible() {
                progressDialog.show();
            }

            @Override
            public void setInvisible() {
                progressDialog.dismiss();
            }
        });

        task.doActionInBackground(new BackgroundAction<LoginResponse>() {
            public LoginResponse actionToDoInBackgroundThread() {
                return context.userService().isValidRemoteLogin(userName, password);
            }

            public void postExecuteInUIThread(LoginResponse result) {
                afterLoginCheck.onEvent(result);
            }
        });
    }

    private void fillUserIfExists() {
        if (context.userService().hasARegisteredUser()) {
            userNameEditText.setText(context.allSharedPreferences().fetchRegisteredANM());
            // remove disable edit username
            //  userNameEditText.setEnabled(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
    }

    private void localLoginWith(String userName, String password) {
        context.userService().localLogin(userName, password);
        goToHome();
        DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
    }

    private void remoteLoginWith(String userName, String password, String userInfo) {
        context.userService().remoteLogin(userName, password, userInfo);
        goToHome();
        DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
    }

    private void goToHome() {
        BoreshaAfyaApplication.setCrashlyticsUser(context);

        // Registering BroadcastReceiver
        registerReceiver();
        startActivity(new Intent(this, NativeHomeActivity.class));

        final String regId =  ((BoreshaAfyaApplication)getApplication()).getRegistration_id();
        if(((BoreshaAfyaApplication)getApplication()).isHasFacility()) {
            android.util.Log.d(TAG,"has the list of facility already");
        }else{
            ((BoreshaAfyaApplication)getApplication()).setFacilityService();
        }

        if(((BoreshaAfyaApplication)getApplication()).isHasService()) {
            android.util.Log.d(TAG,"has the list of service already");
        }else{

            ((BoreshaAfyaApplication)getApplication()).setReferralService();
        }
        // Check if regid already presents
        if (regId.equals("")) {

            // Register with GCM

            intent = new Intent(this, RegistrationIntentService.class);
            mRegisterTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    // Register on our server
                    // On server creates a new user
                    if (checkPlayServices()) {
                        // Start IntentService to register this application with GCM.
                        startService(intent);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mRegisterTask = null;
                }

            };

            // execute AsyncTask
            mRegisterTask.execute(null, null, null);

        } else {


            // Skips registration of the mobile to the server.
            Toast.makeText(getApplicationContext(),
                    "Already registered with GCM Server",
                    Toast.LENGTH_LONG).
                    show();


        }
        finish();
    }

    private String getVersion() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;
    }

    private String getBuildDate() throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
        ZipFile zf = new ZipFile(applicationInfo.sourceDir);
        ZipEntry ze = zf.getEntry("classes.dex");
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new java.util.Date(ze.getTime()));
    }

    public static void setLanguage() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(Context.getInstance().applicationContext()));
        String preferredLocale = allSharedPreferences.fetchLanguagePreference();
        Resources res = Context.getInstance().applicationContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(preferredLocale);
        res.updateConfiguration(conf, dm);

    }

    public static String switchLanguagePreference() {
        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(Context.getInstance().applicationContext()));

        String preferredLocale = allSharedPreferences.fetchLanguagePreference();
        if (ENGLISH_LOCALE.equals(preferredLocale)) {
            allSharedPreferences.saveLanguagePreference(BENGALI_LOCALE);
            Resources res = Context.getInstance().applicationContext().getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale(BENGALI_LOCALE);
            res.updateConfiguration(conf, dm);
            return Bengali_LANGUAGE;
        } else {
            allSharedPreferences.saveLanguagePreference(ENGLISH_LOCALE);
            Resources res = Context.getInstance().applicationContext().getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale(ENGLISH_LOCALE);
            res.updateConfiguration(conf, dm);
            return ENGLISH_LANGUAGE;
        }
    }

    private boolean isLoginInitiateOk() {
        if (TextUtils.isEmpty(userNameEditText.getText())
                || TextUtils.isEmpty(passwordEditText.getText())) {
            // tell user to enter username and pwd
            Snackbar.make(
                    findViewById(R.id.coordinatorLogin),
                    R.string.provide_username_password,
                    Snackbar.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}
