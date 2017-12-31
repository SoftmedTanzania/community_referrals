package org.ei.opensrp.drishti.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.pageradapter.CHWRegisterRecyclerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;


public class FollowupClientsFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private CommonRepository commonRepository;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<ClientReferralPersonObject> clientReferralPersonObjectList = new ArrayList<>();
    private Cursor cursor;
    private boolean mTwoPane;
    private static final String TAG = FollowupClientsFragment.class.getSimpleName(),
            TABLE_NAME = "client_referral";


    public FollowupClientsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ReferredClientsFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        View v= inflater.inflate(R.layout.fragment_chwregistration, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.item_list);
        commonRepository = context().commonrepository("client_referral");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME );

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.clientReferralPersonObjectList = Utils.convertToClientReferralPersonObjectList(commonPersonObjectList);
        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + clientReferralPersonObjectList.size());

        CHWRegisterRecyclerAdapter chwRegisterRecyclerAdapter = new CHWRegisterRecyclerAdapter(getActivity(),clientReferralPersonObjectList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp) or in landscape.
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            chwRegisterRecyclerAdapter.setIsInTwoPane(mTwoPane);
            chwRegisterRecyclerAdapter.notifyDataSetChanged();

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }else{
            int numberOfColumns = 3;
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        recyclerView.setAdapter(chwRegisterRecyclerAdapter);

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
    protected SmartRegisterClientsProvider clientsProvider() {
        return null;
    }

    @Override
    protected void onInitialization() {

    }

    @Override
    protected void startRegistration() {

    }
    protected void populateData() {
        commonRepository = context().commonrepository("client_referral");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME );
    }


}
