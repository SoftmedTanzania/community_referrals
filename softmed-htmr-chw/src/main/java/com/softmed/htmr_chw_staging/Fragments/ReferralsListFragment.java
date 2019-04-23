package com.softmed.htmr_chw_staging.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw_staging.Activities.ChwSmartRegisterActivity;
import com.softmed.htmr_chw_staging.Adapters.ReferredClientsListAdapter;
import com.softmed.htmr_chw_staging.Domain.ClientReferral;
import com.softmed.htmr_chw_staging.Domain.LocationSelectorDialogFragment;
import com.softmed.htmr_chw_staging.R;
import com.softmed.htmr_chw_staging.util.AsyncTask;
import com.softmed.htmr_chw_staging.util.DividerItemDecoration;
import com.softmed.htmr_chw_staging.util.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.repository.ReferralRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class ReferralsListFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = ReferralsListFragment.class.getSimpleName();
    private CommonRepository commonRepository;
    private List<ClientReferral> clientReferrals = new ArrayList<>();
    private Cursor cursor;
    private String locationDialogTAG = "locationDialogTAG";
    private long startDate = 0, endDate = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private EditText fname, othername, ctc_number, textStartDate, textEndDate;
    private String message = "";
    private MaterialSpinner spinnerType;
    private RecyclerView recyclerView;
    private ReferredClientsListAdapter clientsListAdapter;
    private FloatingActionButton fab;
    private View filter;
    private Typeface robotoRegular, sansBold;

    public ReferralsListFragment() {
    }

    public static ReferralsListFragment newInstance() {
        ReferralsListFragment fragment = new ReferralsListFragment();

        return fragment;
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_refered_clients, container, false);

        setupviews(v);

        commonRepository = context().commonrepository(ReferralRepository.TABLE_NAME);
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM " + ReferralRepository.TABLE_NAME +
                " INNER JOIN " + ClientRepository.TABLE_NAME + " ON " + ReferralRepository.TABLE_NAME + "." + ReferralRepository.CLIENT_ID + " = " + ClientRepository.TABLE_NAME + "." + ClientRepository.CLIENT_ID +
                " WHERE " + ReferralRepository.REFERRAL_TYPE + " = 1");
        clientReferrals = Utils.convertToClientReferralObjectList(cursor);

        clientsListAdapter = new ReferredClientsListAdapter(getActivity(), this.clientReferrals, commonRepository);

        textStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(0);
            }
        });
        textEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(1);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));

        recyclerView.setAdapter(clientsListAdapter);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isQueryInitializationOk()) {
                    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
                    queryBuilder.append(ReferralRepository.TABLE_NAME +
                            " INNER JOIN " + ClientRepository.TABLE_NAME + " ON " + ReferralRepository.TABLE_NAME + "." + ReferralRepository.CLIENT_ID + " = " + ClientRepository.TABLE_NAME + "." + ClientRepository.CLIENT_ID +
                            " WHERE " + ReferralRepository.REFERRAL_TYPE + " = 1");

                    new QueryTask().execute(
                            queryBuilder.toString(),
                            ReferralRepository.TABLE_NAME,
                            getFname(), getOthername(), getCTCNumber(), getFromDate(), getToDate(), isDateRangeSet());

                } else {
                    Log.d(TAG, "am in false else");
                    cursor = commonRepository.RawCustomQueryForAdapter("select * FROM " + ReferralRepository.TABLE_NAME +
                            " INNER JOIN " + ClientRepository.TABLE_NAME + " ON " + ReferralRepository.TABLE_NAME + "." + ReferralRepository.CLIENT_ID + " = " + ClientRepository.TABLE_NAME + "." + ClientRepository.CLIENT_ID +
                            " WHERE " + ReferralRepository.REFERRAL_TYPE + " = 1");

                    clientReferrals = Utils.convertToClientReferralObjectList(cursor);
                    clientsListAdapter = new ReferredClientsListAdapter(getActivity(),clientReferrals, commonRepository);
                    recyclerView.setAdapter(clientsListAdapter);
                }
            }
        });

        return v;
    }

    private String getFname() {
        String value = "";
        if (!(fname.getText().toString()).isEmpty()) {
            value = fname.getText().toString();
        }
        return value;
    }

    private String getOthername() {
        String value = "";
        if (!(othername.getText().toString()).isEmpty()) {
            value = othername.getText().toString();
        }
        return value;
    }

    private String getCTCNumber() {
        String value = "";
        if (!(ctc_number.getText().toString()).isEmpty()) {
            value = ctc_number.getText().toString();
        }
        return value;
    }

    private String getFromDate() {
        String value = "";
        if (!(textStartDate.getText().toString()).isEmpty()) {
            value = textStartDate.getText().toString();
        }
        return value;
    }

    private String getToDate() {
        String value = "";
        if (!(textEndDate.getText().toString()).isEmpty()) {
            value = textEndDate.getText().toString();
        }
        return value;
    }

    private void makeToast() {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private boolean isQueryInitializationOk() {
        if ((fname.getText().toString()).isEmpty() && (othername.getText().toString()).isEmpty() && (ctc_number.getText().toString()).isEmpty() && (textStartDate.getText().toString()).isEmpty() && (textEndDate.getText().toString()).isEmpty()) {
            // date range not defined properly
            message = "hujachagua kitu cha kutafuta";
            makeToast();
            return false;
        }

        return true;
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
    public void startRegistration() {
        Log.d(TAG, "starting registrations");
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag(locationDialogTAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        LocationSelectorDialogFragment
                .newInstance((ChwSmartRegisterActivity) getActivity(), null, context().anmLocationController().get(),
                        "pregnant_mothers_pre_registration")
                .show(ft, locationDialogTAG);
    }

    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    //TODO fix this
    public void populateData() {
//        commonRepository = context().commonrepository("client_referral");
//        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM " + TABLE_NAME);
//        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
//
//        clientReferrals = Utils.convertToClientReferralObjectList(commonPersonObjectList);
//        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + clientReferrals.size());
//        ReferredClientsListAdapter pager = new ReferredClientsListAdapter(getActivity(), clientReferrals, commonRepository);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), null));
//        recyclerView.setAdapter(pager);
    }

    private String isDateRangeSet() {
        if ((startDate == 0 && endDate == 0))
            return "no";
        else
            return "yes";
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
                    Log.d(TAG, "chosen date" + startDate);
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

        datePickerDialog.setOkColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        // show dialog
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    private void setupviews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.clients_recycler);
        TextView clientName = v.findViewById(R.id.client_name);
        TextView referralReasons = v.findViewById(R.id.referral_reasons);
        TextView refDate = v.findViewById(R.id.ref_date);
        TextView referralService = v.findViewById(R.id.referral_service);
        TextView status = v.findViewById(R.id.status);
        fname = (EditText) v.findViewById(R.id.client_name_et);
        othername = (EditText) v.findViewById(R.id.client_last_name_et);
        ctc_number = (EditText) v.findViewById(R.id.client_ctc_number_et);
        textStartDate = (EditText) v.findViewById(R.id.from_date);
        textEndDate = (EditText) v.findViewById(R.id.to_date);
        spinnerType = v.findViewById(R.id.spin_status);
        fab = v.findViewById(R.id.fab);
        filter = v.findViewById(R.id.filter_button);

        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(getActivity().getAssets(), "google_sans_bold.ttf");

        clientName.setTypeface(sansBold);
        referralReasons.setTypeface(sansBold);
        refDate.setTypeface(sansBold);
        referralService.setTypeface(sansBold);
        status.setTypeface(sansBold);


    }

    private class QueryTask extends AsyncTask<String, Void, List<ClientReferral>> {

        @Override
        protected List<ClientReferral> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            String fName = params[2];
            String other_name = params[3];
            String ctc_number = params[4];
            String sdate = params[5];
            String edate = params[6];
            String daterange = params[7];

            Log.d(TAG, "query = " + query);
            Log.d(TAG, "tableName = " + tableName + ", parameter = " + fName + ", rangeisset = " + daterange);

            Cursor cursor = commonRepository.RawCustomQueryForAdapter(query);
            clientReferrals = Utils.convertToClientReferralObjectList(cursor);
            cursor.close();

            // obtains client from result
            List<ClientReferral> referrals = new ArrayList<>();

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.setLenient(false);
            try {
                for (ClientReferral clientReferral : clientReferrals) {
                        if (!fName.isEmpty()) {
                            if ((clientReferral.getFirst_name().toLowerCase()).contains((fname.getText().toString()).toLowerCase()))
                                referrals.add(clientReferral);
                        } else if (!other_name.isEmpty()) {
                            if ((clientReferral.getMiddle_name().toLowerCase()).contains(other_name.toLowerCase())||(clientReferral.getSurname().toLowerCase()).contains(other_name.toLowerCase()))
                                referrals.add(clientReferral);
                        } else if (!ctc_number.isEmpty()) {
                            if ((clientReferral.getCtc_number().toLowerCase()).contains(ctc_number.toLowerCase()))
                                referrals.add(clientReferral);
                        } else if (!sdate.isEmpty()) {
                            if (startDate <= clientReferral.getReferral_date())
                                referrals.add(clientReferral);
                        } else if (!edate.isEmpty()) {
                            Log.d(TAG, "am in enddate");
                            if (endDate >= clientReferral.getReferral_date())
                                referrals.add(clientReferral);
                        }
                }

                Log.d(TAG, "result client referral size = " + referrals.size());
                // check date range
                if (daterange.equals("yes")) {
                    Log.d(TAG, "am in the date range");
                    for (ClientReferral clients : referrals) {
                        if (clients.getReferral_date() < startDate || clients.getReferral_date() > endDate)
                            referrals.remove(clients); // remove client referral
                        Log.d(TAG,"Removing client");
                    }
                }

            } catch (Exception e) {
                Log.d(TAG, "error: " + e.getMessage());
                return null;

            } finally {
                cursor.close();
            }

            return referrals;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<ClientReferral> resultList) {
            super.onPostExecute(resultList);
            if (resultList.size() > 0) {
                clientReferrals = resultList;
                ReferredClientsListAdapter pager = new ReferredClientsListAdapter(getActivity(), clientReferrals, commonRepository);
                recyclerView.setAdapter(pager);


            }else {
                Log.d(TAG, "Query result is empty!");
                message = getResources().getString(R.string.no_clients_found);
                makeToast();
                clientReferrals = resultList;
                ReferredClientsListAdapter pager = new ReferredClientsListAdapter(getActivity(), clientReferrals, commonRepository);
                recyclerView.setAdapter(pager);

            }
        }
    }
}
