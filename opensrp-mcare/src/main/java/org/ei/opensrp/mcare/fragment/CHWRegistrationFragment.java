package org.ei.opensrp.mcare.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.mcare.datamodels.PreRegisteredMother;
import org.ei.opensrp.mcare.pageradapter.CHWRegisterRecyclerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link CHWFollowUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CHWRegistrationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public CHWRegistrationFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CHWFollowUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CHWFollowUpFragment newInstance(String param1, String param2) {
        CHWFollowUpFragment fragment = new CHWFollowUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_chwregistration, container, false);

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.pre_reg_listView);

        ArrayList<PreRegisteredMother> mothers = PreRegisteredMother.createPreRegisteredMotherList();
        CHWRegisterRecyclerAdapter pager = new CHWRegisterRecyclerAdapter(getActivity(),mothers);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setAdapter(pager);

        return v;
    }


}
