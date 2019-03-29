package com.softmed.htmr_chw.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Activities.ChwSmartRegisterActivity;
import com.softmed.htmr_chw.Adapters.CBHSClientsListAdapter;
import com.softmed.htmr_chw.Adapters.ReferralClientsListAdapter;
import com.softmed.htmr_chw.Domain.LocationSelectorDialogFragment;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.util.AsyncTask;
import com.softmed.htmr_chw.util.DividerItemDecoration;

import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.domain.Client;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class CBHSClientsListFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = CBHSClientsListFragment.class.getSimpleName();
    public String message = "";
    private ClientRepository clientRepository;
    private List<Client> clients = new ArrayList<>();
    private String locationDialogTAG = "locationDialogTAG";
    private long startDate = 0, endDate = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private EditText fname, othername, phoneNumber;
    private MaterialSpinner spinnerType;
    private RecyclerView recyclerView;
    private CBHSClientsListAdapter cbhsClientsListAdapter;
    private Typeface robotoRegular, sansBold;
    private String preferredLocale;

    public CBHSClientsListFragment() {
    }

    public static CBHSClientsListFragment newInstance() {
        CBHSClientsListFragment fragment = new CBHSClientsListFragment();

        return fragment;
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_registered_cbhs_clients, container, false);
        setupviews(v);

        clientRepository = context().clientRepository();

        List<Client> clients = clientRepository.RawCustomQueryForAdapter("select * FROM " + ClientRepository.TABLE_NAME+
                " WHERE "+ ClientRepository.CBHS+" LIKE '"+context().allSharedPreferences().fetchCBHS()+"%'");

        cbhsClientsListAdapter = new CBHSClientsListAdapter(getActivity(), clients);
        Log.d(TAG, "repo count = " + clients.size());

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        View filter = v.findViewById(R.id.filter_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistration();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isQueryInitializationOk()) {
                    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");
                    queryBuilder.append(ClientRepository.TABLE_NAME);
                    queryBuilder.append(" WHERE "+ClientRepository.CBHS+" LIKE '"+context().allSharedPreferences().fetchCBHS()+"%'");
                    new QueryTask().execute(queryBuilder.toString(),
                            ClientRepository.TABLE_NAME,
                            getFname(),
                            getOthername(),
                            getPhoneNumber(),
                            isDateRangeSet());

                } else {
                    Log.d(TAG, "am in false else");
                    List<Client> clients = clientRepository.RawCustomQueryForAdapter("select * FROM " +
                            ClientRepository.TABLE_NAME+
                            " WHERE "+
                            ClientRepository.CBHS+"  LIKE '"+context().allSharedPreferences().fetchCBHS()+"%'");

                    cbhsClientsListAdapter = new CBHSClientsListAdapter(getActivity(), clients);
                    recyclerView.setAdapter(cbhsClientsListAdapter);
                }
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));

        recyclerView.setAdapter(cbhsClientsListAdapter);

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

    private String getPhoneNumber() {
        String value = "";
        if (!(phoneNumber.getText().toString()).isEmpty()) {
            value = phoneNumber.getText().toString();
        }
        Log.d(TAG,"Received phone number = "+value);
        return value;
    }

    private void makeToast() {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_LONG).show();
    }

    private boolean isQueryInitializationOk() {
        if ((fname.getText().toString()).isEmpty() && (othername.getText().toString()).isEmpty() && (phoneNumber.getText().toString()).isEmpty()) {
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

    private String isDateRangeSet() {
        if ((startDate == 0 && endDate == 0))
            return "no";
        else
            return "yes";
    }

    private void setupviews(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.clients_recycler);
        fname = (EditText) v.findViewById(R.id.client_name_et);
        othername = (EditText) v.findViewById(R.id.client_last_name_et);
        phoneNumber = (EditText) v.findViewById(R.id.client_phone_number);
        spinnerType = (MaterialSpinner) v.findViewById(R.id.spin_status);

        TextView clientName = v.findViewById(R.id.client_name);
        TextView gender = v.findViewById(R.id.gender);
        TextView phoneNumber = v.findViewById(R.id.phone_number);
        TextView village = v.findViewById(R.id.village);


        robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(getActivity().getAssets(), "google_sans_bold.ttf");

        clientName.setTypeface(sansBold);
        gender.setTypeface(sansBold);
        phoneNumber.setTypeface(sansBold);
        village.setTypeface(sansBold);


    }

    private class QueryTask extends AsyncTask<String, Void, List<Client>> {

        @Override
        protected List<Client> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            String fName = params[2];
            String otherName = params[3];
            String phoneNumber = params[4];
            Log.d(TAG, "query = " + query);

            List<Client> commonPersonObjectList = clientRepository.RawCustomQueryForAdapter(query);


            // obtains client from result
            List<Client> receivedClients = new ArrayList<>();

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.setLenient(false);

            try {
                for (Client client : commonPersonObjectList) {
                    // convert and add to list
                    if (!fName.isEmpty()) {
                        if ((client.getFirst_name().toLowerCase()).contains((fname.getText().toString()).toLowerCase()))
                            receivedClients.add(client);
                    } else if (!otherName.isEmpty()) {
                        if ((client.getMiddle_name().toLowerCase()).contains(otherName.toLowerCase()) || (client.getSurname().toLowerCase()).contains(otherName.toLowerCase()))
                            receivedClients.add(client);
                    } else if (!phoneNumber.isEmpty()) {
                        if ((client.getCtc_number().toLowerCase()).contains(phoneNumber.toLowerCase()))
                            receivedClients.add(client);
                    }
                }

                Log.d(TAG, "result clients size" + receivedClients.size());


            } catch (Exception e) {
                Log.d(TAG, "error: " + e.getMessage());
                return null;
            }

            return receivedClients;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Client> resultList) {
            super.onPostExecute(resultList);

            if (resultList == null) {
                Log.d(TAG, "Query failed!");
            } else if (resultList.size() > 0) {
                Log.d(TAG, "resultList " + resultList.size() + "items");
                Log.d(TAG, "resultList " + new Gson().toJson(resultList));

                ReferralClientsListAdapter pager = new ReferralClientsListAdapter(getActivity(), resultList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getActivity(), null));
                recyclerView.setAdapter(pager);


            } else {
                Log.d(TAG, "Query result is empty!");
                message = getString(R.string.no_clients_found);
                makeToast();

                ReferralClientsListAdapter pager = new ReferralClientsListAdapter(getActivity(), resultList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getActivity(), null));
                recyclerView.setAdapter(pager);

            }
        }
    }
}
