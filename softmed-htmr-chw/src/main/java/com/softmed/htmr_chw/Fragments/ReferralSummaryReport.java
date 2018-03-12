package com.softmed.htmr_chw.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.softmed.htmr_chw.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by issy on 12/03/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class ReferralSummaryReport extends Fragment {

    TableLayout servicesTable;
    LinearLayout dateFromLayout, dateToLayout;
    Button applyDateRangeButton;
    TextView dateRangeFromText, dateRangeToText;
    DatePickerDialog fromDatePickerDialog = new DatePickerDialog();
    DatePickerDialog toDatePickerDialog = new DatePickerDialog();

    LayoutInflater layoutInflater;

    Calendar cal;
    long dateFromInMillis, dateToInMillis;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reports, container, false);
        layoutInflater = inflater;
        setUpViews(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateFromLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null){
                    fromDatePickerDialog.show(getActivity().getFragmentManager(),"fromDateRange");
                }
                fromDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateRangeFromText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        dateFromInMillis = cal.getTimeInMillis();
                    }

                });
            }
        });


        dateToLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null){
                    toDatePickerDialog.show(getActivity().getFragmentManager(),"toDateRange");
                }
                toDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        dateRangeToText.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1) + "-" + year);
                        cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        dateToInMillis = cal.getTimeInMillis();
                    }

                });
            }
        });

        applyDateRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadReportData();
            }
        });

    }

    private void loadReportData(){

        new AsyncTask<Void, Void, Void>(){

//            List<ReferralServiceIndicators> listOfIndicators = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {

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

    private void setUpViews(View view){
//        servicesTable = (TableLayout) view.findViewById(R.id.services_table);
        dateFromLayout = (LinearLayout) view.findViewById(R.id.range_from);
        dateToLayout = (LinearLayout) view.findViewById(R.id.range_to);
        applyDateRangeButton = (Button) view.findViewById(R.id.apply_date_range_button);
        dateRangeFromText = (TextView) view.findViewById(R.id.date_range_from);
        dateRangeToText = (TextView) view.findViewById(R.id.date_range_to);
    }

}
