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
import android.widget.Toast;

import com.google.gson.Gson;

import net.sqlcipher.database.SQLiteDatabase;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.pageradapter.CHWFollowUpPagerAdapter;
import org.ei.opensrp.drishti.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import org.ei.opensrp.drishti.util.Utils;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.repository.Repository;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.util.ArrayList;
import java.util.List;

import static org.ei.opensrp.drishti.util.Utils.isTablet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link CHWFollowUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CHWFollowUpFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CommonRepository commonRepository;
    private Gson gson = new Gson();
    private Gson gson1 = new Gson();
    private Gson gson2 = new Gson();
    private android.content.Context appContext;
    private List<ClientReferralPersonObject> clientReferralPersonObjectList = new ArrayList<>();
    private Cursor cursor,cursor2;
    private static final String TAG = CHWFollowUpFragment.class.getSimpleName(),
            TABLE_NAME = "client_referral";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public CHWFollowUpFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CHWFollowUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CHWFollowUpFragment newInstance() {
        CHWFollowUpFragment fragment = new CHWFollowUpFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_chwfollow, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.chw_followUp_listView);
        //todo need to select all mothers with usertype id similar to the logged chw user
        commonRepository = context().commonrepository("client_referral");
        //todo martha edit the query
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME );
//        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM "+TABLE_NAME+" where IS_VALID='true'" );

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.clientReferralPersonObjectList = Utils.convertToClientReferralPersonObjectList(commonPersonObjectList);
        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + clientReferralPersonObjectList.size());

        CHWFollowUpPagerAdapter pager = new CHWFollowUpPagerAdapter(getActivity(), clientReferralPersonObjectList);


        int numberOfColumns=2;
        if(isTablet(getActivity())){
            numberOfColumns = 3;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));


        recyclerView.setItemAnimator(new DefaultItemAnimator());



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

    }


}
