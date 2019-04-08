package com.softmed.ccm_chw.Fragments;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.ccm_chw.Adapters.FollowupClintsRecyclerAdapter;
import com.softmed.ccm_chw.Domain.ClientReferral;
import com.softmed.ccm_chw.R;
import com.softmed.ccm_chw.util.Utils;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.cursoradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.ClientRepository;
import org.ei.opensrp.repository.ReferralRepository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import java.util.ArrayList;
import java.util.List;


public class FollowupReferralsFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    private static final String TAG = FollowupReferralsFragment.class.getSimpleName();
    private Cursor cursor;
    private boolean mTwoPane;
    private View v;
    private RecyclerView recyclerView;
    private List<ClientReferral> clientReferrals = new ArrayList<>();
    private CommonRepository commonRepository;


    public FollowupReferralsFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chwregistration, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.item_list);
        TextView followupDetailsLabel = v.findViewById(R.id.followup_details_label);
        TextView followupDesccLael = v.findViewById(R.id.followup_descc_lael);


        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "roboto_regular.ttf");
        Typeface sansBold = Typeface.createFromAsset(getActivity().getAssets(), "google_sans_bold.ttf");

        followupDetailsLabel.setTypeface(sansBold);
        followupDesccLael.setTypeface(robotoRegular);

        commonRepository = context().commonrepository(ReferralRepository.TABLE_NAME);
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM " + ReferralRepository.TABLE_NAME +
                " INNER JOIN " + ClientRepository.TABLE_NAME + " ON " + ReferralRepository.TABLE_NAME + "." + ReferralRepository.CLIENT_ID + " = " + ClientRepository.TABLE_NAME + "." + ClientRepository.CLIENT_ID + " WHERE " + ReferralRepository.REFERRAL_TYPE + " = 4 AND "+ReferralRepository.ReferralStatus+" = '0' ");
        clientReferrals = Utils.convertToClientReferralObjectList(cursor);

        Log.d(TAG,"Client Referral ID = "+new Gson().toJson(clientReferrals));

        try {
            FollowupClintsRecyclerAdapter followupClintsRecyclerAdapter = new FollowupClintsRecyclerAdapter(getActivity(), clientReferrals);

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
        commonRepository = context().commonrepository(ReferralRepository.TABLE_NAME);
        Cursor cursor = commonRepository.RawCustomQueryForAdapter("select * FROM " + ReferralRepository.TABLE_NAME +
                " INNER JOIN " + ClientRepository.TABLE_NAME + " ON " + ReferralRepository.TABLE_NAME + "." + ReferralRepository.CLIENT_ID + " = " + ClientRepository.TABLE_NAME + "." + ClientRepository.CLIENT_ID + " WHERE " + ReferralRepository.REFERRAL_TYPE + " = 4 AND "+ReferralRepository.ReferralStatus+" = '0'");

        clientReferrals = Utils.convertToClientReferralObjectList(cursor);
        cursor.close();

        FollowupClintsRecyclerAdapter followupClintsRecyclerAdapter = new FollowupClintsRecyclerAdapter(getActivity(), clientReferrals);

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
