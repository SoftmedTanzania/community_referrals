package com.softmed.htmr_chw.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.Pageradapter.FollowupClintsRecyclerAdapter;
import com.softmed.htmr_chw.Pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;

import org.ei.opensrp.domain.ClientFollowup;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientFollowupRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;


public class FollowupClientsFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private ClientFollowupRepository clientFollowupRepository;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<ClientFollowup> followupClients = new ArrayList<>();
    private Cursor cursor;
    private boolean mTwoPane;
    private View v;
    private RecyclerView recyclerView;
    private static final String TAG = FollowupClientsFragment.class.getSimpleName(),
            TABLE_NAME = "followup_client";


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
        v = inflater.inflate(R.layout.fragment_chwregistration, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.item_list);
        clientFollowupRepository = context().followupClientRepository();
        try {
            followupClients = clientFollowupRepository.all();

            FollowupClintsRecyclerAdapter followupClintsRecyclerAdapter = new FollowupClintsRecyclerAdapter(getActivity(), followupClients);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


            if (v.findViewById(R.id.item_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp) or in landscape.
                // If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;

                followupClintsRecyclerAdapter.setIsInTwoPane(mTwoPane);
                followupClintsRecyclerAdapter.notifyDataSetChanged();

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            } else {
                int numberOfColumns = 3;
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            recyclerView.setAdapter(followupClintsRecyclerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public void populateData() {
        clientFollowupRepository = context().followupClientRepository();

        FollowupClintsRecyclerAdapter followupClintsRecyclerAdapter = new FollowupClintsRecyclerAdapter(getActivity(), clientFollowupRepository.all());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());


        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp) or in landscape.
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            followupClintsRecyclerAdapter.setIsInTwoPane(mTwoPane);
            followupClintsRecyclerAdapter.notifyDataSetChanged();

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            int numberOfColumns = 3;
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        recyclerView.setAdapter(followupClintsRecyclerAdapter);
    }


}
