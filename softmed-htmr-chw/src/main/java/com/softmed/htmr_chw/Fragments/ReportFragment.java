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
                loadReportData();
            }
        });


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

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                Cursor servicesCursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service INNER JOIN client_referral ON  referral_service.id = client_referral.referral_service_id GROUP BY referral_service.id" );

                Log.d(TAG," services count count :=  "+servicesCursor.getCount());

                for(int i=0;i<servicesCursor.getCount();i++){
                    servicesCursor.moveToPosition(i);
                    Log.d(TAG," services name :=  "+servicesCursor.getString(servicesCursor.getColumnIndex("name")));

                    int serviveId = servicesCursor.getInt(servicesCursor.getColumnIndex("id"));
                    Log.d(TAG," services id :=  "+serviveId);

                    Cursor cursorMaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) FROM "+TABLE_NAME+ " WHERE referral_service_id = "+serviveId+" AND  referral_status= 'true' AND gender = 'Male'" );
                    Cursor cursorFemaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) FROM "+TABLE_NAME+ " WHERE referral_service_id = "+serviveId+" AND  referral_status= 'true' AND gender = 'Female'" );



                }



//                listOfIndicators = BaseActivity.baseDatabase.referralServiceIndicatorsDao().getAllServices();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//
//                for (ReferralServiceIndicators service : listOfIndicators){
//                    View tableView = layoutInflater.inflate(R.layout.single_service_report_table_item, null);
//
//                    TextView serviceName = (TextView) tableView.findViewById(R.id.service_name);
//
//                    TextView maleTotal  = (TextView) tableView.findViewById(R.id.male_total);
//                    TextView femaleTotal = (TextView) tableView.findViewById(R.id.female_total);
//                    TextView total = (TextView) tableView.findViewById(R.id.total);
//
//                    TextView maleAttendedCount = (TextView) tableView.findViewById(R.id.male_attended);
//                    TextView femaleAttendedCount = (TextView) tableView.findViewById(R.id.female_attended);
//                    TextView totalAttended = (TextView) tableView.findViewById(R.id.total_attended);
//
//                    TextView maleUnattended = (TextView) tableView.findViewById(R.id.male_pending);
//                    TextView femaleUnattended = (TextView) tableView.findViewById(R.id.female_pending);
//                    TextView totalUnattended = (TextView) tableView.findViewById(R.id.total_pending);
//
//                    serviceName.setText(service.getServiceName());
//
//                    servicesTable.addView(tableView);
//
//                }

            }
        }.execute();

    }


}
