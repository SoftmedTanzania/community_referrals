package com.softmed.uzazisalama.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.ei.opensrp.drishti.R;

import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.pageradapter.CHWRegisterRecyclerAdapter;
import com.softmed.uzazisalama.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import com.softmed.uzazisalama.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link CHWFollowUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CHWPreRegistrationFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CommonRepository commonRepository;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();
    private Cursor cursor;
    private static final String TAG = CHWPreRegistrationFragment.class.getSimpleName(),
            TABLE_NAME = "wazazi_salama_mother";

    private CHWRegisterRecyclerAdapter pager;
    private RecyclerView recyclerView;

    public CHWPreRegistrationFragment() {
        // Required empty public constructor
    }



    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chwregistration, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.pre_reg_listView);

        populateData();

        return v;
    }

    public void populateData(){
        commonRepository = context().commonrepository("wazazi_salama_mother");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME+" where IS_VALID='true'" );

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.motherPersonList = Utils.convertToMotherPersonObjectList(commonPersonObjectList);
        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + motherPersonList.size());

        pager = new CHWRegisterRecyclerAdapter(getActivity(),motherPersonList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(pager);
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


}
