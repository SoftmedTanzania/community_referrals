package org.ei.opensrp.drishti.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;

import org.ei.opensrp.drishti.PncRegisterListAdapter;
import org.ei.opensrp.drishti.Repository.PncPersonObject;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;

import static org.ei.opensrp.drishti.util.Utils.isTablet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link PncListFragment#} factory method to
 * create an instance of this fragment.
 */
public class PncListFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CommonRepository commonRepository;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<PncPersonObject> motherPersonList = new ArrayList<>();
    private Cursor cursor;
    private static final String TAG = PncListFragment.class.getSimpleName(),
            TABLE_NAME = "uzazi_salama_pnc";

    private PncRegisterListAdapter pager;
    private RecyclerView recyclerView;

    public PncListFragment() {
        // Required empty public constructor
    }



    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_pnc_details, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.pre_reg_listView);
        v.findViewById(R.id.buttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ((AncSmartRegisterActivity) getActivity()).switchToBaseFragment(null);
            }
        });
        populateData();

        return v;
    }

    public void populateData(){
        commonRepository = context().commonrepository("uzazi_salama_pnc");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME );

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

//        this.motherPersonList = Utils.convertToPncPersonObjectList(commonPersonObjectList);
        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + motherPersonList.size());

        CommonRepository motherRepository = context().commonrepository("wazazi_salama_mother");
        CommonRepository childRepository = context().commonrepository("uzazi_salama_child");
        pager = new PncRegisterListAdapter(getActivity(),motherRepository,childRepository,motherPersonList,getContext());


//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        int numberOfColumns=2;
        if(isTablet(getActivity())){
            numberOfColumns = 3;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
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
