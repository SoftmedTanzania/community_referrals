package com.softmed.uzazisalama;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.PncMother;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ReportSearchActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    AlertDialog.Builder dialogBuilder;
    MaterialSpinner spinnerType;
    ArrayAdapter<String> typeAdapter;
    RadioGroup radioGroupMotherType;

    private Gson gson = new Gson();

    private final static String TAG = ReportSearchActivity.class.getSimpleName(),
            TABLE_ANC = "wazazi_salama_mother",
            TABLE_PNC = "uzazi_salama_pnc";
    private String tableName;
    private final static String[] MOTHER_TYPES = {"Mama Waja Wazito", "Mama Waliojifungua"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_search);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Report Search");


//        typeAdapter = new ArrayAdapter<>(ReportSearchActivity.this,
//                android.R.layout.simple_spinner_dropdown_item, MOTHER_TYPES);
//        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinnerType = (MaterialSpinner) findViewById(R.id.spinnerType);
//        spinnerType.setAdapter(typeAdapter);

        radioGroupMotherType = (RadioGroup) findViewById(R.id.radioGroupMotherType);


        progressDialog = new ProgressDialog(ReportSearchActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tafadhali subiri....");
        progressDialog.setIndeterminate(true);

        dialogBuilder = new AlertDialog.Builder(ReportSearchActivity.this);
        dialogBuilder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        // fab listener
        findViewById(R.id.fabSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo build query string and  initiate query async task

                if (isQueryInitializationOk()) {
                    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");

                    switch (radioGroupMotherType.getCheckedRadioButtonId()) {
                        case R.id.radioTypeANC:
                            // anc
                            tableName = TABLE_ANC;
                            queryBuilder.append(tableName).append(" WHERE Is_PNC = 'false' ");
                            // execute query
                            new QueryAncTask().execute(queryBuilder.toString(), tableName);
                            break;

                        case R.id.radioTypePNC:
                            // pnc
                            tableName = TABLE_PNC;
                            queryBuilder.append(tableName);
                            // execute query
                            new QueryPncTask().execute(queryBuilder.toString(), tableName);
                            break;
                    }
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean isQueryInitializationOk() {
        if (radioGroupMotherType.getCheckedRadioButtonId() == -1) {
            // nothing selected
            makeSnackbar("Chagua ripoti unayohitaji kuona.");
            return false;
        }

        return true;
    }


    private class QueryAncTask extends AsyncTask<String, Void, List<PregnantMom>> {

        @Override
        protected List<PregnantMom> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName);

            Context context = Context.getInstance().updateApplicationContext(getApplicationContext());
            CommonRepository commonRepository = context.commonrepository(tableName);
            Cursor cursor = commonRepository.RawCustomQueryForAdapter(query);

            // obtains mothers from result
            List<PregnantMom> pregnantMoms = new ArrayList<>();
            try {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        // get anc mothers from query result and add them to list
                        String details = cursor.getString(cursor.getColumnIndex("details"));
                        Log.d(TAG, "column details = " + details);
                        // convert and add to list
                        pregnantMoms.add(gson.fromJson(details, PregnantMom.class));
                        cursor.moveToNext();
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "error: " + e.getMessage());
                return null;

            } finally {
                cursor.close();
            }

            return pregnantMoms;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // show progress
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<PregnantMom> resultList) {
            super.onPostExecute(resultList);
            // hide progress and process the result
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (resultList == null)
                showDialog(getString(R.string.failed_please_try_again));

            else if (resultList.size() > 0) {
                Log.d(TAG, "resultList " + resultList.size());
                makeSnackbar("Result: " + resultList.size() + " items.");

            } else {
                Log.d(TAG, "Query result is empty!");
                showDialog(getString(R.string.no_results_found));
            }
        }
    }


    private class QueryPncTask extends AsyncTask<String, Void, List<PncMother>> {

        @Override
        protected List<PncMother> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName);

            Context context = Context.getInstance().updateApplicationContext(getApplicationContext());
            CommonRepository commonRepository = context.commonrepository(tableName);
            Cursor cursor = commonRepository.RawCustomQueryForAdapter(query);

            // obtains mothers from result
            List<PncMother> pncMoms = new ArrayList<>();
            try {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        // get anc mothers from query result and add them to list
                        String details = cursor.getString(cursor.getColumnIndex("details"));
                        Log.d(TAG, "column details = " + details);
                        // convert and add to list
                        pncMoms.add(gson.fromJson(details, PncMother.class));
                        cursor.moveToNext();
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "error: " + e.getMessage());
                return null;

            } finally {
                cursor.close();
            }

            return pncMoms;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // show progress
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<PncMother> resultList) {
            super.onPostExecute(resultList);
            // hide progress and process the result
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (resultList == null)
                showDialog(getString(R.string.failed_please_try_again));

            else if (resultList.size() > 0) {
                Log.d(TAG, "resultList " + resultList.size());
                makeSnackbar("Result: " + resultList.size() + " items.");

            } else {
                Log.d(TAG, "Query result is empty!");
                showDialog(getString(R.string.no_results_found));
            }
        }
    }


    private void makeSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinatorSearchReport),
                message,
                3000).show();

    }


    private void showDialog(String message) {
        dialogBuilder.setMessage(message).create().show();
    }
}
