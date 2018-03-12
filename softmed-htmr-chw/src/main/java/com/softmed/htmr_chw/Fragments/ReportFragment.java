package com.softmed.htmr_chw.Fragments;


import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by coze on 06/03/18.
 */
public class ReportFragment  extends SecuredNativeSmartRegisterCursorAdapterFragment {
     static final String TAG = ReportFragment.class.getSimpleName(),TABLE_NAME = "client_referral";;
    private static final String ARG_POSITION = "position";
    private TableLayout defaultersTable;
    private LinearLayout dataView;
    private View metDOBFrom, metDOBTo;
    private TextView dateRangeFromText,dateRangeToText;
    private String toDateString="",fromDateString="";
    final DatePickerDialog fromDatePicker = new DatePickerDialog();
    final DatePickerDialog toDatePicker = new DatePickerDialog();
    private LinearLayout dateFromLayout, dateToLayout;
    private Button applyDateRangeButton;
    private CommonRepository commonRepository;
    private TableLayout servicesTable;

    public ReportFragment() {
    }

    @Override
    protected void onCreation() {
        toDatePicker.setMaxDate(Calendar.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rowview = inflater.inflate(R.layout.fragment_summary_reports, null);


        commonRepository = context().commonrepository(TABLE_NAME);

        dateRangeFromText = (TextView) rowview.findViewById(R.id.date_range_from);
        dateRangeToText = (TextView) rowview.findViewById(R.id.date_range_to);
        metDOBTo = rowview.findViewById(R.id.range_to);

        metDOBTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePicker.show(((Activity) getActivity()).getFragmentManager(), "DatePickerDialogue");
                toDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateRangeToText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-"
                                + year);

                        Calendar toCalendar = Calendar.getInstance();
                        toCalendar.set(year, monthOfYear, dayOfMonth);
                        fromDatePicker.setMaxDate(toCalendar);
                        toDateString = (toCalendar.getTimeInMillis() / 1000) + "";
                        if (fromDateString.equals("")) {
                            final Snackbar snackbar = Snackbar.make(rowview, "Please select a start date to view the chart", Snackbar.LENGTH_LONG);
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
            }
        });

        metDOBFrom = rowview.findViewById(R.id.range_from);
        metDOBFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePicker.show(((Activity) getActivity()).getFragmentManager(), "DatePickerDialogue");
                fromDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateRangeFromText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-"
                                + year);

                        Calendar fromCalendar = Calendar.getInstance();
                        fromCalendar.set(year, monthOfYear, dayOfMonth);
                        toDatePicker.setMinDate(fromCalendar);
                        fromDateString = ((fromCalendar.getTimeInMillis() - 24*60*60*1000) / 1000) + "";
                        if(!toDateString.equals("")){
                            final Snackbar snackbar= Snackbar.make(rowview,"Please select an end date to view the report", Snackbar.LENGTH_LONG);
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
            }
        });

        applyDateRangeButton = (Button) rowview.findViewById(R.id.apply_date_range_button);
        applyDateRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicesTable.removeAllViews();
                loadReportData();
            }
        });
        servicesTable = (TableLayout)rowview.findViewById(R.id.services_table);


        return rowview;
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

    }

    @Override
    protected void startRegistration() {

    }


    private void loadReportData(){

        new AsyncTask<Void, String, String>(){

            @Override
            protected String doInBackground(Void... voids) {

                Cursor servicesCursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service INNER JOIN client_referral ON  referral_service.id = client_referral.referral_service_id GROUP BY referral_service.id" );

                Log.d(TAG," services count count :=  "+servicesCursor.getCount());

                JSONArray jsonArray = new JSONArray();
                for(int i=0;i<servicesCursor.getCount();i++){
                    servicesCursor.moveToPosition(i);
                    Log.d(TAG," services name :=  "+servicesCursor.getString(servicesCursor.getColumnIndex("name")));

                    int serviveId = servicesCursor.getInt(0);
                    Log.d(TAG," services id :=  "+serviveId);

                    Cursor cursorMaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) as c FROM "+TABLE_NAME+ " WHERE referral_service_id = "+serviveId+" AND  referral_status<>2 AND  gender = 'Male'" );
                    cursorMaleCount.moveToFirst();
                    Cursor cursorFemaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) as c FROM "+TABLE_NAME+ " WHERE referral_service_id = "+serviveId+" AND  referral_status<>2 AND gender = 'Female'" );
                    cursorFemaleCount.moveToFirst();

                    JSONObject serviceDetails = new JSONObject();
                    try {
                        serviceDetails.put("Male",cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                        serviceDetails.put("Female",cursorFemaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                        serviceDetails.put("Service",servicesCursor.getString(servicesCursor.getColumnIndex("name")));
                        serviceDetails.put("Total",cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c"))+cursorFemaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    Log.d(TAG," male count   :=  "+cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                    Log.d(TAG," female count :=  "+cursorFemaleCount.getInt(cursorFemaleCount.getColumnIndex("c")));

                    jsonArray.put(serviceDetails);


                }



                return jsonArray.toString();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                JSONArray services = null;
                try {
                    services = new JSONArray(aVoid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i=0;i<services.length();i++){

                    JSONObject object = null;
                    try {
                        object = services.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    View tableView = getActivity().getLayoutInflater().inflate(R.layout.view_summary_item, null);

                    TextView sn = (TextView) tableView.findViewById(R.id.sn_title);
                    TextView serviceName = (TextView) tableView.findViewById(R.id.service_name);
                    TextView maleTotal  = (TextView) tableView.findViewById(R.id.male_count);
                    TextView femaleTotal = (TextView) tableView.findViewById(R.id.female_count);
                    TextView total = (TextView) tableView.findViewById(R.id.total);

                    sn.setText((i+1)+"");
                    try {
                        serviceName.setText(object.getString("Service"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        maleTotal.setText(object.getString("Male"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        femaleTotal.setText(object.getString("Female"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        total.setText(object.getString("Total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    servicesTable.addView(tableView);

                }

            }
        }.execute();

    }


}
