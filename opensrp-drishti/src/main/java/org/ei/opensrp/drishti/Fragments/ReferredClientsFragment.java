package org.ei.opensrp.drishti.Fragments;

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

import com.google.gson.Gson;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.ChwSmartRegisterActivity;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.Repository.LocationSelectorDialogFragment;
import org.ei.opensrp.drishti.pageradapter.ReferredClientsPagerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.DividerItemDecoration;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;


public class ReferredClientsFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CommonRepository commonRepository;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<ClientReferralPersonObject> clientReferralPersonObjectList = new ArrayList<>();
    private Cursor cursor,cursor2;
    private String locationDialogTAG = "locationDialogTAG";
    private static final String TAG = ReferredClientsFragment.class.getSimpleName(),
            TABLE_NAME = "client_referral";

    public ReferredClientsFragment() {
        // Required empty public constructor
    }

    public static ReferredClientsFragment newInstance() {
        ReferredClientsFragment fragment = new ReferredClientsFragment();

        return fragment;
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_refered_clients, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.clients_recycler);
        //todo need to select all mothers with usertype id similar to the logged chw user
        commonRepository = context().commonrepository("client_referral");
        //todo martha edit the query
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME );
//        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME+" where IS_VALID='true'" );

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.clientReferralPersonObjectList = Utils.convertToClientReferralPersonObjectList(commonPersonObjectList);
        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + clientReferralPersonObjectList.size());

        ReferredClientsPagerAdapter pager = new ReferredClientsPagerAdapter(getActivity(), clientReferralPersonObjectList,commonRepository);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
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


        recyclerView.setAdapter(pager);

        return v;
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

    protected void populateData() {

    }


}
