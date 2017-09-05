package org.ei.opensrp.mcare.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.ei.opensrp.mcare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ANCRegister2ndFragment extends Fragment {

    CheckBox checkBoxAgeBelow20, checkBox10YrsPassed, checkBoxBabyDeath,
            checkBox2orMoreBBA, checkBoxHeartProb, checkBoxDiabetes, checkBoxTB;

    public ANCRegister2ndFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_ancregister2nd, container, false);

        checkBoxAgeBelow20 = (CheckBox) fragmentView.findViewById(R.id.checkboxAgeBelow20);
        checkBox10YrsPassed = (CheckBox) fragmentView.findViewById(R.id.checkbox10YrsLastPreg);
        checkBoxBabyDeath = (CheckBox) fragmentView.findViewById(R.id.checkboxBabyDeath);
        checkBox2orMoreBBA = (CheckBox) fragmentView.findViewById(R.id.checkbox2orMoreBBA);
        checkBoxHeartProb = (CheckBox) fragmentView.findViewById(R.id.checkboxHeartProb);
        checkBoxDiabetes = (CheckBox) fragmentView.findViewById(R.id.checkboxDiabetes);
        checkBoxTB = (CheckBox) fragmentView.findViewById(R.id.checkboxTB);

        return fragmentView;
    }


    public SparseBooleanArray getIndicatorsMap() {
        // todo get all checked boxes
        SparseBooleanArray indicators = new SparseBooleanArray();

        indicators.put(R.id.checkboxAgeBelow20, checkBoxAgeBelow20.isChecked());
        indicators.put(R.id.checkbox10YrsLastPreg, checkBox10YrsPassed.isChecked());
        indicators.put(R.id.checkboxBabyDeath, checkBoxBabyDeath.isChecked());
        indicators.put(R.id.checkbox2orMoreBBA, checkBox2orMoreBBA.isChecked());
        indicators.put(R.id.checkboxHeartProb, checkBoxHeartProb.isChecked());
        indicators.put(R.id.checkboxDiabetes, checkBoxDiabetes.isChecked());
        indicators.put(R.id.checkboxTB, checkBoxTB.isChecked());


        return indicators;
    }

}
