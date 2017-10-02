package org.ei.opensrp.drishti.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.ei.opensrp.drishti.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AncRegister2ndFragment extends Fragment {

    private static CheckBox checkBoxAgeBelow20, checkBox10YrsPassed, checkBoxBabyDeath,
            checkBox2orMoreBBA, checkBoxHeartProb, checkBoxDiabetes, checkBoxTB,
            checkBox4orMorePregnancies, checkBox1stPregAbove35Yrs, checkBoxHeightBelow150,
            checkBoxCSDelivery, checkBoxKilemaChaNyonga, checkBoxBleedingOnDelivery,
            checkBoxKondoKukwama;

    private static CardView cardRiskIndicators;
    private static LinearLayout layoutRiskAge, layoutRiskHeight, layoutRiskFertilityCount, layoutRiskHIV;

    public AncRegister2ndFragment() {
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
        checkBox4orMorePregnancies = (CheckBox) fragmentView.findViewById(R.id.checkbox4orMorePregnancies);
        checkBox1stPregAbove35Yrs = (CheckBox) fragmentView.findViewById(R.id.checkbox1stPregAbove35Yrs);
        checkBoxHeightBelow150 = (CheckBox) fragmentView.findViewById(R.id.checkboxHeightBelow150);
        checkBoxCSDelivery = (CheckBox) fragmentView.findViewById(R.id.checkboxCSDelivery);
        checkBoxKilemaChaNyonga = (CheckBox) fragmentView.findViewById(R.id.checkboxKilemaChaNyonga);
        checkBoxBleedingOnDelivery = (CheckBox) fragmentView.findViewById(R.id.checkboxBleedingOnDelivery);
        checkBoxKondoKukwama = (CheckBox) fragmentView.findViewById(R.id.checkboxKondoKukwama);

        cardRiskIndicators = (CardView) fragmentView.findViewById(R.id.cardRiskIndicators);
        layoutRiskAge = (LinearLayout) fragmentView.findViewById(R.id.layoutRiskAge);
        layoutRiskHeight = (LinearLayout) fragmentView.findViewById(R.id.layoutRiskHeight);
        layoutRiskFertilityCount = (LinearLayout) fragmentView.findViewById(R.id.layoutRiskFertilityCount);
        layoutRiskHIV = (LinearLayout) fragmentView.findViewById(R.id.layoutRiskHIV);

        layoutRiskAge.setVisibility(View.GONE);
        layoutRiskHeight.setVisibility(View.GONE);
        layoutRiskFertilityCount.setVisibility(View.GONE);
        layoutRiskHIV.setVisibility(View.GONE);

        return fragmentView;
    }


    public SparseBooleanArray getIndicatorsMap() {
        // todo get all checked boxes
        SparseBooleanArray indicators = new SparseBooleanArray();

        try {
            indicators.put(R.id.checkboxAgeBelow20, checkBoxAgeBelow20.isChecked());
            indicators.put(R.id.checkbox10YrsLastPreg, checkBox10YrsPassed.isChecked());
            indicators.put(R.id.checkboxBabyDeath, checkBoxBabyDeath.isChecked());
            indicators.put(R.id.checkbox2orMoreBBA, checkBox2orMoreBBA.isChecked());
            indicators.put(R.id.checkboxHeartProb, checkBoxHeartProb.isChecked());
            indicators.put(R.id.checkboxDiabetes, checkBoxDiabetes.isChecked());
            indicators.put(R.id.checkboxTB, checkBoxTB.isChecked());
            indicators.put(R.id.checkbox4orMorePregnancies, checkBox4orMorePregnancies.isChecked());
            indicators.put(R.id.checkbox1stPregAbove35Yrs, checkBox1stPregAbove35Yrs.isChecked());
            indicators.put(R.id.checkboxHeightBelow150, checkBoxHeightBelow150.isChecked());
            indicators.put(R.id.checkboxCSDelivery, checkBoxCSDelivery.isChecked());
            indicators.put(R.id.checkboxKilemaChaNyonga, checkBoxKilemaChaNyonga.isChecked());
            indicators.put(R.id.checkboxBleedingOnDelivery, checkBoxBleedingOnDelivery.isChecked());
            indicators.put(R.id.checkboxKondoKukwama, checkBoxKondoKukwama.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return indicators;
    }


    public void updateRiskIndicators(int age, int height, int fertilityCount, int radioCheckedHIV) {
        boolean isToShowCard = false;

        if (age < 20 && age != -1) {
            layoutRiskAge.setVisibility(View.VISIBLE);
            isToShowCard = true;
        } else
            layoutRiskAge.setVisibility(View.GONE);

        if (height < 150 && height != -1) {
            layoutRiskHeight.setVisibility(View.VISIBLE);
            isToShowCard = true;
        } else
            layoutRiskHeight.setVisibility(View.GONE);

        if (fertilityCount >= 4 && fertilityCount != -1) {
            layoutRiskFertilityCount.setVisibility(View.VISIBLE);
            isToShowCard = true;
        } else
            layoutRiskFertilityCount.setVisibility(View.GONE);

        if (radioCheckedHIV == R.id.radioYesHIV) {
            layoutRiskHIV.setVisibility(View.VISIBLE);
            isToShowCard = true;
        } else
            layoutRiskHIV.setVisibility(View.GONE);


        if (isToShowCard)
            cardRiskIndicators.setVisibility(View.VISIBLE);
        else
            cardRiskIndicators.setVisibility(View.GONE);
    }

}
