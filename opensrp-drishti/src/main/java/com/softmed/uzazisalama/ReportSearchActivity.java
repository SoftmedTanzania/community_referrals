package com.softmed.uzazisalama;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.PncMother;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Repository.PncPersonObject;
import com.softmed.uzazisalama.util.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class ReportSearchActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    AlertDialog.Builder dialogBuilder;
    MaterialSpinner spinnerType;
    ArrayAdapter<String> typeAdapter;
    RadioGroup radioGroupMotherType, radioGroupRiskStatus, radioGroupDeliveryResult;
    LinearLayout layoutRiskStatus, layoutDeliveryStatus;
    CardView cardPickStartDate, cardPickEndDate;
    TextView textStartDate, textEndDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private String risk,delivery = "n/a";
    private final static String TAG = ReportSearchActivity.class.getSimpleName(),
            TABLE_ANC = "wazazi_salama_mother",
            TABLE_PNC = "uzazi_salama_pnc";
    private String tableName;
    private long startDate = 0, endDate = 0;
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

        // radioGroupMotherType = (RadioGroup) findViewById(R.id.radioGroupMotherType);
        radioGroupRiskStatus = (RadioGroup) findViewById(R.id.radioGroupRiskStatus);
        radioGroupDeliveryResult = (RadioGroup) findViewById(R.id.radioGroupDeliveryResult);
        cardPickStartDate = (CardView) findViewById(R.id.cardPickStartDate);
        cardPickEndDate = (CardView) findViewById(R.id.cardPickEndDate);
        textStartDate = (TextView) findViewById(R.id.textStartDate);
        textEndDate = (TextView) findViewById(R.id.textEndDate);
        layoutRiskStatus = (LinearLayout) findViewById(R.id.layoutRiskStatus);
        layoutRiskStatus.setVisibility(View.GONE);
        layoutDeliveryStatus = (LinearLayout) findViewById(R.id.layoutDeliveryStatus);
        layoutDeliveryStatus.setVisibility(View.GONE);


//        radioGroupMotherType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
//                if (id == R.id.radioTypeANC) {
//                    layoutRiskStatus.setVisibility(View.VISIBLE);
//                    layoutDeliveryStatus.setVisibility(View.GONE);
//
//                } else if (id == R.id.radioTypePNC) {
//                    layoutDeliveryStatus.setVisibility(View.VISIBLE);
//                    layoutRiskStatus.setVisibility(View.GONE);
//                }
//            }
//        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected index = " + i);

                if (i == 0) {
                    layoutDeliveryStatus.setVisibility(View.GONE);
                    layoutRiskStatus.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    layoutRiskStatus.setVisibility(View.GONE);
                    layoutDeliveryStatus.setVisibility(View.VISIBLE);
                } else {
                    if (layoutRiskStatus.getVisibility() == View.VISIBLE)
                        layoutRiskStatus.setVisibility(View.GONE);

                    if (layoutDeliveryStatus.getVisibility() == View.VISIBLE)
                        layoutDeliveryStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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


        cardPickStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(0);
            }
        });

        cardPickEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(1);
            }
        });

        // fab listener
        findViewById(R.id.fabSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // build query string and  initiate query async task
                Log.d(TAG, "Selected item position = " + spinnerType.getSelectedItemPosition());

                if (isQueryInitializationOk()) {
                    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");

//                    switch (radioGroupMotherType.getCheckedRadioButtonId()) {
//                        case R.id.radioTypeANC:
//                            // anc
//                            tableName = TABLE_ANC;
//                            queryBuilder.append(tableName).append(" WHERE Is_PNC = 'false' ");
//                            // execute query
//                            new QueryAncTask().execute(
//                                    queryBuilder.toString(),
//                                    tableName,
//                                    getRiskStatus());
//                            break;
//
//                        case R.id.radioTypePNC:
//                            // pnc
//                            tableName = TABLE_PNC;
//                            queryBuilder.append(tableName);
//                            // execute query
//                            new QueryPncTask().execute(
//                                    queryBuilder.toString(),
//                                    tableName,
//                                    getDeliveryResult());
//                            break;
//                    }


                    switch (spinnerType.getSelectedItemPosition()) {
                        case 1:
                            // anc
                            tableName = TABLE_ANC;
                            queryBuilder.append(tableName).append(" WHERE Is_PNC = 'false' ");
                            // execute query
                            new QueryAncTask().execute(
                                    queryBuilder.toString(),
                                    tableName,
                                    getRiskStatus(),
                                    isDateRangeSet());
                            break;

                        case 2:
                            // pnc
                            tableName = TABLE_PNC;
                            queryBuilder.append(tableName);
                            // execute query
                            new QueryPncTask().execute(
                                    queryBuilder.toString(),
                                    tableName,
                                    getDeliveryResult(),
                                    isDateRangeSet());
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
        if (spinnerType.getSelectedItemPosition() == 0) {
            // nothing selected
            makeSnackbar("Chagua ripoti unayohitaji kuona.");
            return false;

        } else if ((int) startDate != 0 && (int) endDate == 0) {
            // date range not defined properly
            makeSnackbar("Chagua tarehe ya kuishia.");
            return false;

        } else if ((int) startDate == 0 && (int) endDate != 0) {
            // date range not defined properly
            makeSnackbar("Chagua tarehe ya kuanzia.");
            return false;
        }

        return true;
    }

    private String getRiskStatus() {
        return radioGroupRiskStatus.getCheckedRadioButtonId() == R.id.radioYesOnRisk ? "yes" :
                radioGroupRiskStatus.getCheckedRadioButtonId() == R.id.radioNotOnRisk ? "no" :
                        "n/a";
        // the last one means radio for "all" is checked so will show all mothers
        // without filtering by using their risk status
    }

    private String getDeliveryResult() {
        return radioGroupDeliveryResult.getCheckedRadioButtonId() == R.id.radioSuccessfulDelivery ? "yes"
                : radioGroupDeliveryResult.getCheckedRadioButtonId() == R.id.radioUnsuccessfulDelivery ? "no" :
                "n/a";
        // the last one means radio for "all" is checked so will show all mothers
        // without filtering by using their delivery result
    }

    private String isDateRangeSet() {
        return ((int) startDate != 0 && (int) endDate != 0) ? "yes" : "no";
    }


    private class QueryAncTask extends AsyncTask<String, Void, List<PregnantMom>> {

        @Override
        protected List<PregnantMom> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            String riskStatus = params[2];
            risk = params[2];
            String isDateRangeSet = params[3];
            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName + ", riskStatus = " + riskStatus
                    + ", isDateRangeSet = " + isDateRangeSet);

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
                        if (riskStatus.equals("n/a"))
                            // add all
                            pregnantMoms.add(gson.fromJson(details, PregnantMom.class));

                        else if (riskStatus.equals("yes")) {
                            // add mothers on risk
                            PregnantMom mom = gson.fromJson(details, PregnantMom.class);
                            if (mom.isOnRisk())
                                pregnantMoms.add(mom);

                        } else if (riskStatus.equals("no")) {
                            // add mothers not on risk
                            PregnantMom mom = gson.fromJson(details, PregnantMom.class);
                            if (!mom.isOnRisk())
                                pregnantMoms.add(mom);
                        }

                        cursor.moveToNext();
                    }
                    // check date range
                    if (isDateRangeSet.equals("yes")) {
                        for (PregnantMom mom : pregnantMoms) {
                            if (mom.getDateReg() < startDate || mom.getDateReg() > endDate)
                                pregnantMoms.remove(mom); // remove mother
                        }
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
                //  makeSnackbar("Result: " + resultList.size() + " items.");
                Intent reportIntent = new Intent(ReportSearchActivity.this, UzaziSalamaReport.class);
                reportIntent.putExtra("moms", gson.toJson(resultList));
                reportIntent.putExtra("type", "anc");
                reportIntent.putExtra("risk", risk);
                startActivity(reportIntent);

            } else {
                Log.d(TAG, "Query result is empty!");
                showDialog(getString(R.string.no_results_found));
            }
        }
    }


    private class QueryPncTask extends AsyncTask<String, Void, List<PncPersonObject>> {

        @Override
        protected List<PncPersonObject> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            delivery = params[2];
            String deliveryResult = params[2];
            String isDateRangeSet = params[3];
            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName + ", deliveryResult =  " + deliveryResult
                    + ", isDateRangeSet = " + isDateRangeSet);

            Context context = Context.getInstance().updateApplicationContext(getApplicationContext());
            CommonRepository commonRepository = context.commonrepository(tableName);
            Cursor cursor = commonRepository.RawCustomQueryForAdapter(query);
            List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, tableName);
            Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));


            // obtains mothers from result
            List<PncPersonObject> motherPersonList = new ArrayList<>();
            List<PncMother> pncMoms = new ArrayList<>();
            motherPersonList = Utils.convertToPncPersonObjectList(commonPersonObjectList);
            Log.d(TAG, "repo count = " + gson.toJson(motherPersonList));
//            try {
//                if (cursor.moveToFirst()) {
//                    while (!cursor.isAfterLast()) {
//                        // get anc mothers from query result and add them to list
//                        String details = cursor.getString(cursor.getColumnIndex("details"));
//                        Log.d(TAG, "column details = " + details);
//                        // convert and add to list
//                        if (deliveryResult.equals("n/a"))
//                            // add all
//                            pncMoms.add(gson.fromJson(details, PncMother.class));
//
//                        else if (deliveryResult.equals("yes")) {
//                            // todo add mothers with successful birth
//                            pncMoms.add(gson.fromJson(details, PncMother.class));
//
//                        } else if (deliveryResult.equals("no")) {
//                            //todo add mothers with unsuccessful birth
//                            pncMoms.add(gson.fromJson(details, PncMother.class));
//                        }
//
//                        cursor.moveToNext();
//                    }
//
//
//                }
//            } catch (Exception e) {
//                Log.d(TAG, "error: " + e.getMessage());
//                return null;
//
//            } finally {
//                cursor.close();
//            }
//
//                        else if (deliveryResult.equals("yes")) {
//                            // todo add mothers with successful birth
//                            pncMoms.add(gson.fromJson(details, PncMother.class));
//
//                        } else if (deliveryResult.equals("no")) {
//                            //todo add mothers with unsuccessful birth
//                            pncMoms.add(gson.fromJson(details, PncMother.class));
//                        }
//
//                        cursor.moveToNext();
//                    }
//
//                    // check date range
//                    if (isDateRangeSet.equals("yes")) {
//                        for (PncMother mom : pncMoms) {
//                            if (mom.getDeliveryDate() < startDate || mom.getDeliveryDate() > endDate)
//                                pncMoms.remove(mom); // remove mother
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.d(TAG, "error: " + e.getMessage());
//                return null;
//
//            } finally {
//                cursor.close();
//            }
//
//            return pncMoms;
            return motherPersonList;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // show progress
            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<PncPersonObject> resultList) {
            super.onPostExecute(resultList);
            // hide progress and process the result
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (resultList == null)
                showDialog(getString(R.string.failed_please_try_again));

            else if (resultList.size() > 0) {
                Log.d(TAG, "resultList " + resultList.size());
                Log.d(TAG, "resultList " + gson.toJson(resultList));
                Intent reportIntent = new Intent(ReportSearchActivity.this, UzaziSalamaReport.class);
                reportIntent.putExtra("moms", gson.toJson(resultList));
                reportIntent.putExtra("type", "pnc");
                reportIntent.putExtra("delivery", delivery);
                startActivity(reportIntent);

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


    private void pickDate(final int id) {
        // listener
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                // todo get picked date update view
                GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Log.d(TAG, "pickedDate = " + pickedDate.getTimeInMillis());

                if (id == 0) {
                    // start date
                    startDate = pickedDate.getTimeInMillis();
                    textStartDate.setText(dateFormat.format(startDate));

                } else if (id == 1) {
                    // end date
                    endDate = pickedDate.getTimeInMillis();
                    textEndDate.setText(dateFormat.format(endDate));
                }
            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.primary));

        // show dialog
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }
}
