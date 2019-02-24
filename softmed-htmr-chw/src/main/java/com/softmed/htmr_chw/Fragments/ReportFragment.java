package com.softmed.htmr_chw.Fragments;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.ClientFollowupRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static org.ei.opensrp.AllConstants.ENGLISH_LOCALE;

/**
 * Created by coze on 06/03/18.
 */
public class ReportFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    static final String TAG = ReportFragment.class.getSimpleName(), TABLE_NAME = "client_referral";
    ;
    private static final String ARG_POSITION = "position";
    private TableLayout defaultersTable;
    private LinearLayout dataView;
    private View metDOBFrom, metDOBTo;
    private TextView dateRangeFromText, dateRangeToText;
    private long toDateTimestamp, fromDateTimestamp;
    final DatePickerDialog fromDatePicker = new DatePickerDialog();
    final DatePickerDialog toDatePicker = new DatePickerDialog();
    private LinearLayout dateFromLayout, dateToLayout;
    private Button applyDateRangeButton;
    private CommonRepository commonRepository;

    private ClientFollowupRepository clientFollowupRepository;
    private TableLayout servicesTable;
    private PieChart mChart1;
    private BarChart mChart2;
    private String preferredLocale;

    public ReportFragment() {
    }

    @Override
    protected void onCreation() {
        toDatePicker.setMaxDate(Calendar.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rowview = inflater.inflate(R.layout.fragment_summary_reports, null);

        AllSharedPreferences allSharedPreferences = new AllSharedPreferences(getDefaultSharedPreferences(org.ei.opensrp.Context.getInstance().applicationContext()));
        preferredLocale = allSharedPreferences.fetchLanguagePreference();


        clientFollowupRepository = context().followupClientRepository();
        commonRepository = context().commonrepository(TABLE_NAME);

        dateRangeFromText = (TextView) rowview.findViewById(R.id.date_range_from);
        dateRangeToText = (TextView) rowview.findViewById(R.id.date_range_to);
        metDOBTo = rowview.findViewById(R.id.range_to);


        //Styling date picker dialogues
        fromDatePicker.setOkColor(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_light));
        fromDatePicker.setCancelColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        fromDatePicker.setVersion(DatePickerDialog.Version.VERSION_1);
        fromDatePicker.setAccentColor(ContextCompat.getColor(getActivity(), com.softmed.htmr_chw.R.color.colorPrimary));


        toDatePicker.setOkColor(ContextCompat.getColor(getActivity(), android.R.color.holo_blue_light));
        toDatePicker.setCancelColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        toDatePicker.setVersion(DatePickerDialog.Version.VERSION_1);
        toDatePicker.setAccentColor(ContextCompat.getColor(getActivity(), com.softmed.htmr_chw.R.color.colorPrimary));


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
                        toDateTimestamp = toCalendar.getTimeInMillis();
                        if (fromDateTimestamp == 0) {
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
                        fromDateTimestamp = fromCalendar.getTimeInMillis();
                        if (toDateTimestamp == 0) {
                            final Snackbar snackbar = Snackbar.make(rowview, "Please select an end date to view the report", Snackbar.LENGTH_LONG);
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
                Log.d(TAG, "applying date range");
                Log.d(TAG, "from time stamp = " + fromDateTimestamp);
                Log.d(TAG, "to time stamp = " + toDateTimestamp);
                loadReportData(fromDateTimestamp, toDateTimestamp);
            }
        });
        servicesTable = (TableLayout) rowview.findViewById(R.id.services_table);

        //Pie chart configurations
        mChart1 = (PieChart) rowview.findViewById(R.id.chart1);
        mChart1.setUsePercentValues(true);
        mChart1.getDescription().setEnabled(false);
        mChart1.setExtraOffsets(5, 10, 5, 5);

        mChart1.setDragDecelerationFrictionCoef(0.95f);

        mChart1.setCenterText(generateCenterSpannableText());

        mChart1.setDrawHoleEnabled(true);
        mChart1.setHoleColor(Color.WHITE);

        mChart1.setTransparentCircleColor(Color.WHITE);
        mChart1.setTransparentCircleAlpha(110);

        mChart1.setHoleRadius(58f);
        mChart1.setTransparentCircleRadius(61f);

        mChart1.setDrawCenterText(true);

        mChart1.setRotationAngle(0);
        mChart1.setRotationEnabled(true);
        mChart1.setHighlightPerTapEnabled(true);

        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        Legend l = mChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(7f);

        // entry label styling
        mChart1.setEntryLabelColor(Color.BLACK);
        mChart1.setEntryLabelTextSize(12f);


        //Bar chart configurations
        mChart2 = (BarChart) rowview.findViewById(R.id.chart2);
        mChart2.setDrawBarShadow(false);
        mChart2.setDrawValueAboveBar(true);

        mChart2.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart2.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(false);

        mChart2.setDrawGridBackground(false);
        // mChart2.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart2.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart2.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l2 = mChart2.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setDrawInside(false);
        l2.setForm(Legend.LegendForm.SQUARE);
        l2.setFormSize(9f);
        l2.setTextSize(11f);
        l2.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        loadReportData(0, 0);

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


    private void loadReportData(final long fromDateTimestamp, final long toDateTimestamp) {

        new AsyncTask<Void, String, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                categoryNames.clear();
                sizes.clear();

                Cursor receivedMaleReferralsServicesCursor = null;
                Cursor receivedFemaleReferralsServicesCursor = null;

                Cursor providedReferralsServicesCursor = clientFollowupRepository.RawCustomQueryForAdapter("select * FROM referral_service INNER JOIN client_referral ON  referral_service.id = client_referral.referral_service_id " +
                        (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                        (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : "") +
                        "  GROUP BY referral_service.id");

                try {
                    receivedMaleReferralsServicesCursor = clientFollowupRepository.RawCustomQueryForAdapter("select * FROM followup_client WHERE gender = 'Male'  " +
                            (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                            (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : ""));
                    receivedFemaleReferralsServicesCursor = clientFollowupRepository.RawCustomQueryForAdapter("select * FROM followup_client WHERE gender = 'Female'  " +
                            (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                            (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                JSONObject receivedReferrals = new JSONObject();
                try {
                    receivedReferrals.put("maleCount", receivedMaleReferralsServicesCursor.getCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    receivedReferrals.put("femaleCount", receivedFemaleReferralsServicesCursor.getCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                receivedMaleReferralsServicesCursor.close();
                receivedFemaleReferralsServicesCursor.close();

                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < providedReferralsServicesCursor.getCount(); i++) {
                    providedReferralsServicesCursor.moveToPosition(i);

                    if (preferredLocale.equals(ENGLISH_LOCALE))
                        categoryNames.add(providedReferralsServicesCursor.getString(providedReferralsServicesCursor.getColumnIndex("name")));
                    else
                        categoryNames.add(providedReferralsServicesCursor.getString(providedReferralsServicesCursor.getColumnIndex("name_sw")));

                    int serviveId = providedReferralsServicesCursor.getInt(0);

                    JSONObject serviceDetails = new JSONObject();
                    try {
                        if (preferredLocale.equals(ENGLISH_LOCALE))
                            serviceDetails.put("Service", providedReferralsServicesCursor.getString(providedReferralsServicesCursor.getColumnIndex("name")));
                        else
                            serviceDetails.put("Service", providedReferralsServicesCursor.getString(providedReferralsServicesCursor.getColumnIndex("name_sw")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Cursor healthFacilityCursor = clientFollowupRepository.RawCustomQueryForAdapter("select client_referral.facility_id,facility.name FROM client_referral " +
                            "INNER JOIN referral_service ON  client_referral.referral_service_id = referral_service.id " +
                            "INNER JOIN facility ON  client_referral.facility_id = facility.id " +
                            (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                            (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : "") +
                            "  GROUP BY client_referral.facility_id,facility.name ");

                    JSONArray facilityReferrals = new JSONArray();
                    int totalReferrals = 0;
                    for (int j = 0; j < healthFacilityCursor.getCount(); j++) {
                        healthFacilityCursor.moveToPosition(j);

                        Cursor cursorMaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) as c FROM " + TABLE_NAME + " WHERE facility_id = '" + healthFacilityCursor.getString(0) + "' AND referral_service_id = " + serviveId + " AND  referral_status<>2 AND  gender = 'Male' " +
                                (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                                (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : ""));
                        cursorMaleCount.moveToFirst();
                        Cursor cursorFemaleCount = commonRepository.RawCustomQueryForAdapter("select count(*) as c FROM " + TABLE_NAME + " WHERE facility_id = '" + healthFacilityCursor.getString(0) + "' AND referral_service_id = " + serviveId + " AND  referral_status<>2 AND gender = 'Female' " +
                                (fromDateTimestamp != 0 ? " AND referral_date > " + fromDateTimestamp : "") +
                                (toDateTimestamp != 0 ? " AND referral_date < " + toDateTimestamp : ""));
                        cursorFemaleCount.moveToFirst();

                        JSONObject facilityReferralDetails = new JSONObject();
                        try {
                            facilityReferralDetails.put("FacilityName", healthFacilityCursor.getString(1));
                            facilityReferralDetails.put("Male", cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                            facilityReferralDetails.put("Female", cursorFemaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                            facilityReferralDetails.put("Total", cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c")) + cursorFemaleCount.getInt(cursorMaleCount.getColumnIndex("c")));

                            totalReferrals += (cursorMaleCount.getInt(cursorMaleCount.getColumnIndex("c")) + cursorFemaleCount.getInt(cursorMaleCount.getColumnIndex("c")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            facilityReferrals.put(facilityReferralDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        cursorMaleCount.close();
                        cursorFemaleCount.close();
                    }
                    sizes.add(totalReferrals);

                    try {
                        serviceDetails.put("facilityReferrals", facilityReferrals);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(serviceDetails);
                    healthFacilityCursor.close();
                }


                JSONObject chartsData = new JSONObject();
                try {
                    chartsData.put("chart1", jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    chartsData.put("chart2", receivedReferrals);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                providedReferralsServicesCursor.close();

                return chartsData.toString();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                setData();
                mChart1.highlightValues(null);
                mChart1.invalidate();

                JSONObject chartsData = null;
                try {
                    chartsData = new JSONObject(aVoid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONArray services = new JSONArray();
                try {
                    services = chartsData.getJSONArray("chart1");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < services.length(); i++) {

                    JSONObject object = null;
                    try {
                        object = services.getJSONObject(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    View tableView = getActivity().getLayoutInflater().inflate(R.layout.view_summary_item, null);

                    TextView sn = (TextView) tableView.findViewById(R.id.sn_title);
                    TextView serviceName = (TextView) tableView.findViewById(R.id.service_name);

                    sn.setText((i + 1) + "");
                    try {
                        serviceName.setText(object.getString("Service"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray facilityReferralsSummary = null;
                    try {
                        facilityReferralsSummary = object.getJSONArray("facilityReferrals");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int j = 0; j < facilityReferralsSummary.length(); j++) {
                        View facilityReferralsSumamryItem = getActivity().getLayoutInflater().inflate(R.layout.view_facility_referrals_summary_item, null);

                        TextView facilityName = (TextView) facilityReferralsSumamryItem.findViewById(R.id.facility_name);
                        TextView maleTotal = (TextView) facilityReferralsSumamryItem.findViewById(R.id.male_count);
                        TextView femaleTotal = (TextView) facilityReferralsSumamryItem.findViewById(R.id.female_count);
                        TextView total = (TextView) facilityReferralsSumamryItem.findViewById(R.id.total);

                        JSONObject facilityObject = null;
                        try {
                            facilityObject = facilityReferralsSummary.getJSONObject(j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {
                            facilityName.setText(facilityObject.getString("FacilityName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            maleTotal.setText(facilityObject.getString("Male"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            femaleTotal.setText(facilityObject.getString("Female"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            total.setText(facilityObject.getString("Total"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ((LinearLayout) tableView.findViewById(R.id.facility)).addView(facilityReferralsSumamryItem);
                        tableView.invalidate();
                    }

                    servicesTable.addView(tableView);

                }


                JSONObject followupPatients = new JSONObject();
                try {
                    followupPatients = chartsData.getJSONObject("chart2");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int maleCount = 0;
                try {
                    maleCount = followupPatients.getInt("maleCount");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int femaleCount = 0;
                try {
                    femaleCount = followupPatients.getInt("femaleCount");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                yVals1.add(new BarEntry(1, maleCount));
                yVals1.add(new BarEntry(2, femaleCount));

                BarDataSet set1 = new BarDataSet(yVals1, getString(R.string.followup_referrals_chart_key_label));

                set1.setDrawIcons(false);

                set1.setColors(ColorTemplate.MATERIAL_COLORS);

                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setBarWidth(0.9f);

                mChart2.setData(data);

                mChart2.highlightValues(null);
                mChart2.invalidate();


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private SpannableString generateCenterSpannableText() {

//        SpannableString s = new SpannableString("Successful Referrals\nreferred by "+((BoreshaAfyaApplication)getActivity().getApplication()).getUsername());
        SpannableString s = new SpannableString(getString(R.string.completed_referrals_label));
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 20, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 32, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 20, 32, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 20, 32, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), 20, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 32, s.length(), 0);
        return s;
    }

    private List<String> categoryNames = new ArrayList<>();
    private List<Integer> sizes = new ArrayList<>();

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < sizes.size(); i++) {
            entries.add(new PieEntry((float) (sizes.get(i)),
                    categoryNames.get(i),
                    getResources().getDrawable(R.drawable.ic_account_box)));
        }

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.referral_services));

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart1.setData(data);

        // undo all highlights
        mChart1.highlightValues(null);

        mChart1.invalidate();
    }


    class MyAxisValueFormatter implements IAxisValueFormatter {

        public MyAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            int v = (int) value;
            return v + "";
        }
    }

    class XAxisValueFormatter implements IAxisValueFormatter {
        public XAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value == 1)
                return getString(R.string.male);
            else
                return getString(R.string.female);
        }
    }
}
