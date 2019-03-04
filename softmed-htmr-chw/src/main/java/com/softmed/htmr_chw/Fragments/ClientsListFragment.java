package com.softmed.htmr_chw.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Activities.ChwSmartRegisterActivity;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.Domain.LocationSelectorDialogFragment;
import com.softmed.htmr_chw.Adapters.ClientsListAdapter;
import com.softmed.htmr_chw.Adapters.SecuredNativeSmartRegisterCursorAdapterFragment;
import com.softmed.htmr_chw.util.AsyncTask;
import com.softmed.htmr_chw.util.DividerItemDecoration;

import org.ei.opensrp.domain.Client;
import org.ei.opensrp.domain.Referral;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class ClientsListFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private ClientRepository clientRepository;
    private List<Client> clients = new ArrayList<>();
    private Cursor cursor;
    private String locationDialogTAG = "locationDialogTAG";
    private static final String TAG = ClientsListFragment.class.getSimpleName(),
            TABLE_NAME = "client";
    private long startDate = 0, endDate = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private Gson gson = new Gson();
    private EditText fname, othername, phoneNumber;
    public String message = "";
    private MaterialSpinner spinnerType;
    private RecyclerView recyclerView;
    private ClientsListAdapter clientsListAdapter;

    public ClientsListFragment() {
    }

    public static ClientsListFragment newInstance() {
        ClientsListFragment fragment = new ClientsListFragment();

        return fragment;
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_registered_clients, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.clients_recycler);

        clientRepository = context().clientRepository();
        clients = clientRepository.all();



        clientsListAdapter = new ClientsListAdapter(getActivity(), clients);
        Log.d(TAG, "repo count = " + clients.size());

        spinnerType = (MaterialSpinner) v.findViewById(R.id.spin_status);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        Button filter = (Button) v.findViewById(R.id.filter_button);
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
                    queryBuilder.append(TABLE_NAME);
                    new QueryTask().execute(
                            queryBuilder.toString(),
                            TABLE_NAME,
                            getFname(), getOthername(), getPhoneNumber(), isDateRangeSet());

                } else {
                    Log.d(TAG, "am in false else");
                    List<Client> clients = clientRepository.RawCustomQueryForAdapter("select * FROM " + ClientRepository.TABLE_NAME + " where is_valid ='true'");

                    ClientsListAdapter pager = new ClientsListAdapter(getActivity(), clients);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(
                            new DividerItemDecoration(getActivity(), null));
                    recyclerView.setAdapter(pager);
                }
            }
        });

        fname = (EditText) v.findViewById(R.id.client_name_et);
        othername = (EditText) v.findViewById(R.id.client_last_name_et);
        phoneNumber = (EditText) v.findViewById(R.id.client_ctc_number_et);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));

        recyclerView.setAdapter(clientsListAdapter);

        populateData();

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

    public void populateData() {

        Log.d(TAG,"am in refresh list view");

        List<Client> clients = clientRepository.all();

        ClientsListAdapter pager = new ClientsListAdapter(getActivity(), clients);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), null));
        recyclerView.setAdapter(pager);
    }

    private String isDateRangeSet() {
        if ((startDate == 0 && endDate == 0))
            return "no";
        else
            return "yes";
    }

    private class QueryTask extends AsyncTask<String, Void, List<Client>> {

        @Override
        protected List<Client> doInBackground(String... params) {
            publishProgress();
            String query = params[0];
            String tableName = params[1];
            String fName = params[2];
            String other_name = params[3];
            String phone_number = params[4];
            String daterange = params[7];
            Log.d(TAG, "query = " + query);

            List<Client> commonPersonObjectList = clientRepository.RawCustomQueryForAdapter("select * FROM "+tableName);;


            // obtains client from result
            Referral client = null;
            List<Client> receivedClients = new ArrayList<>();

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.setLenient(false);

            try {
                for (Client commonPersonObject : commonPersonObjectList) {

                        // convert and add to list
                        if (!fName.isEmpty()) {
                            if ((commonPersonObject.getFirst_name().toLowerCase()).contains((fname.getText().toString()).toLowerCase()))
                                receivedClients.add(commonPersonObject);
                        } else if (!other_name.isEmpty()) {
                            if ((commonPersonObject.getMiddle_name().toLowerCase()).contains(other_name.toLowerCase())||(commonPersonObject.getSurname().toLowerCase()).contains(other_name.toLowerCase()))
                                receivedClients.add(commonPersonObject);
                        } else if (!phone_number.isEmpty()) {
                            if ((commonPersonObject.getCtc_number().toLowerCase()).contains(phone_number.toLowerCase()))
                                receivedClients.add(commonPersonObject);
                        }

                        cursor.moveToNext();
                    }

                    Log.d(TAG, "result clients size" + receivedClients.size());


            } catch (Exception e) {
                Log.d(TAG, "error: " + e.getMessage());
                return null;

            } finally {
                cursor.close();
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

                ClientsListAdapter pager = new ClientsListAdapter(getActivity(), resultList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.addItemDecoration(
                        new DividerItemDecoration(getActivity(), null));
                recyclerView.setAdapter(pager);


            } else {
                Log.d(TAG, "Query result is empty!");
                message = "hakuna taarifa yoyote";
                makeToast();

                ClientsListAdapter pager = new ClientsListAdapter(getActivity(), resultList);
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
