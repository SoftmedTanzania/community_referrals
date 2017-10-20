package com.softmed.uzazisalama;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ReportSearchActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    MaterialSpinner spinnerType;
    ArrayAdapter<String> typeAdapter;

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


        typeAdapter = new ArrayAdapter<>(ReportSearchActivity.this,
                android.R.layout.simple_spinner_dropdown_item, MOTHER_TYPES);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType = (MaterialSpinner) findViewById(R.id.spinnerType);
        spinnerType.setAdapter(typeAdapter);


        progressDialog = new ProgressDialog(ReportSearchActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tafadhali subiri....");
        progressDialog.setIndeterminate(true);


        // fab listener
        findViewById(R.id.fabSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo build query string and  initiate query async task

                if (isQueryInitializationOk()) {
                    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");

                    if (spinnerType.getSelectedItemPosition() == 1) {
                        // anc
                        tableName = TABLE_ANC;
                        queryBuilder.append(tableName).append(" WHERE Is_PNC = 'false' ");
                    } else {
                        // pnc
                        tableName = TABLE_PNC;
                        queryBuilder.append(tableName);
                    }


                    // execute task
                    new QueryTask().execute(queryBuilder.toString(), tableName);
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
        if (spinnerType.getSelectedItemPosition() == -1) {
            // nothing selected
            return false;
        }

        return true;
    }


    private class QueryTask extends AsyncTask<String, Void, List<PregnantMom>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<PregnantMom> doInBackground(String... params) {
            // todo run query
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName);

            Context context = Context.getInstance().updateApplicationContext(getApplicationContext());
            CommonRepository commonRepository = context.commonrepository(tableName);
            Cursor cursor = commonRepository.RawCustomQueryForAdapter(query);


            return processCursorResult(cursor);
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
            // todo hide progress and process the result
            Log.d(TAG, "resultList " + resultList.size());

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            makeSnackbar("Result: " + resultList.size() + " items.");
        }
    }


    private List<PregnantMom> processCursorResult(Cursor cursor) {

        if (cursor.getCount() == 0) {
            Log.d(TAG, "Query result is empty!");
            return new ArrayList<>();
        }
        Log.d(TAG, "Result = " + cursor.getCount() + " rows.");

        try {
            cursor.moveToFirst();
            switch (tableName) {
                case TABLE_ANC:
                    List<PregnantMom> pregnantMoms = new ArrayList<>();
                    while (!cursor.isAfterLast()) {
                        // get anc mothers from query result and add them to list
                        String details = cursor.getString(cursor.getColumnIndex("details"));
                        Log.d(TAG, "column details = " + details);
                        // add
                        pregnantMoms.add(gson.fromJson(details, PregnantMom.class));
                        cursor.moveToNext();
                    }
                    return pregnantMoms;


                case TABLE_PNC:
                    while (!cursor.isAfterLast()) {
                        // todo get pnc mothers from query result and add them to list
                        String details = cursor.getString(cursor.getColumnIndex("details"));
                        Log.d(TAG, "column details = " + details);
                        cursor.moveToNext();
                    }
                    break;
            }

        } catch (Exception e) {
            Log.d(TAG, "error: " + e.getMessage());

        } finally {
            cursor.close();
        }


        return new ArrayList<>();
    }


    private void makeSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinatorSearchReport),
                message,
                3000).show();

    }
}
