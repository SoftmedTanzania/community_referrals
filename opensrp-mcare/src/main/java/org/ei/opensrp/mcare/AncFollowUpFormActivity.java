package org.ei.opensrp.mcare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class AncFollowUpFormActivity extends Activity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AncFollowUpFormActivity.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button submitButton;
    private RadioButton radioButtonPressure,radioButtonHb,radioButtonAlbumini, radioButtonSugar, radioButtonUmriWaMimba,
                     radioButtonChildDeath, radioButtonMlaloWaMtoto, radioButtonKimo;
    private RadioGroup radioGroupPressure,radioGroupHb,radioGroupAlbumini, radioGroupSugar, radioGroupUmriWaMimba,
            radioGroupChildDeath, radioGroupMlaloWaMtoto, radioGroupKimo;
    private String pressure,hb, albumini,sugar, umriWaMimba,childDeath,mlaloWaMtoto,kimo;
    private String formName;
    private EditText editTextFacilityName;

    public AncFollowUpFormActivity() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ancfollow_up_form);




        findViews();
        setListeners();

        ((TextView)findViewById(R.id.txt_title_label)).setText("Uzazi Salama Mahudhurio ya Marudio");

    }



    private void findViews() {
        editTextFacilityName = (EditText) findViewById(R.id.facility);
        radioGroupPressure = (RadioGroup) findViewById(R.id.pressure);
        radioGroupAlbumini = (RadioGroup) findViewById(R.id.albumin);
        radioGroupHb = (RadioGroup) findViewById(R.id.hb);
        radioGroupMlaloWaMtoto = (RadioGroup) findViewById(R.id.mlalo);
        radioGroupChildDeath = (RadioGroup) findViewById(R.id.kifo);
        radioGroupKimo = (RadioGroup) findViewById(R.id.kimo);
        radioGroupSugar = (RadioGroup) findViewById(R.id.sukari);
        radioGroupUmriWaMimba = (RadioGroup) findViewById(R.id.umri);
        submitButton = (Button)findViewById(R.id.submit);


    }

    private void setListeners() {




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedPressureId = radioGroupPressure.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonPressure = (RadioButton) findViewById(selectedPressureId);
                pressure = (String) radioButtonPressure.getText();

                int selectedHbId = radioGroupHb.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonHb = (RadioButton) findViewById(selectedHbId);
                hb = (String) radioButtonHb.getText();

                int selectedSukariId = radioGroupSugar.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonSugar = (RadioButton) findViewById(selectedSukariId);
                sugar = (String) radioButtonSugar.getText();

                int selectedMlaloId = radioGroupMlaloWaMtoto.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonMlaloWaMtoto = (RadioButton) findViewById(selectedMlaloId);
                mlaloWaMtoto = (String) radioButtonMlaloWaMtoto.getText();

                int selectedAlbuminId = radioGroupAlbumini.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonAlbumini = (RadioButton) findViewById(selectedAlbuminId);
                albumini = (String) radioButtonAlbumini.getText();

                int selectedKimoId = radioGroupKimo.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonKimo = (RadioButton) findViewById(selectedKimoId);
                kimo = (String) radioButtonKimo.getText();

                int selectedKifoId = radioGroupChildDeath.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonChildDeath = (RadioButton) findViewById(selectedKifoId);
                childDeath = (String) radioButtonChildDeath.getText();

                int selectedId = radioGroupUmriWaMimba.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonUmriWaMimba = (RadioButton) findViewById(selectedId);
                umriWaMimba = (String) radioButtonUmriWaMimba.getText();



                HashMap<String,String> followHash = new HashMap<String, String>();
                followHash.put("facility_name",editTextFacilityName.toString());
                followHash.put("pressure", pressure);
                followHash.put("hb", hb);
                followHash.put("albumin", albumini);
                followHash.put("sugar", sugar);
                followHash.put("mlaloWaMtoto", mlaloWaMtoto);
                followHash.put("childDeath", childDeath);
                followHash.put("umriWaMimba", umriWaMimba);
                followHash.put("kimo", kimo);


                String trial_one = followHash.get(pressure);
                String trial_two = followHash.get(hb);
                String trial_three = followHash.get(umriWaMimba);
                Log.d(TAG,"pressure = "+trial_one);
                Log.d(TAG,"pressure_1 = "+trial_two);
                Log.d(TAG,"pressure_2 = "+trial_three);
            }
        });
    }


}
