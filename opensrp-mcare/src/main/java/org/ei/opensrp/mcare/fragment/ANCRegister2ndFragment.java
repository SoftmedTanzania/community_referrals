package org.ei.opensrp.mcare.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ei.opensrp.mcare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ANCRegister2ndFragment extends Fragment {


    public ANCRegister2ndFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_ancregister2nd, container, false);

        return fragmentView;
    }


    public SparseBooleanArray getCheckBoxMap() {
        // todo get all checked boxes
        return new SparseBooleanArray();
    }

}
