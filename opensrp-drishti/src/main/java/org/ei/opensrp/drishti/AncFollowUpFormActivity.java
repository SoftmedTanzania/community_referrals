package org.ei.opensrp.drishti;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.PregnantMom;

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
    private CheckBox checkBoxPressure, checkboxHb, chechboxAlbumini, checkboxSugar, checkboxUmriWaMimba,
            checkboxChildDeath, chechkboxMlaloWaMtoto, checkboxKimo;
    private String pressure, hb, albumini, sugar, umriWaMimba, childDeath, mlaloWaMtoto, kimo;
    private String formName;
    private EditText editTextFacilityName;


    private PregnantMom pregnantMom;
    private Gson gson = new Gson();

    public AncFollowUpFormActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ancfollow_up_form);

        String gsonMom = getIntent().getStringExtra("mom");
        Log.d(TAG, "mom=" + gsonMom);

        pregnantMom = gsonMom != null ? gson.fromJson(gsonMom, PregnantMom.class) : null;


        findViews();
        setListeners();

        ((TextView) findViewById(R.id.txt_title_label)).setText("Uzazi Salama Mahudhurio ya Marudio");

    }


    private void findViews() {
        editTextFacilityName = (EditText) findViewById(R.id.facility);
        checkBoxPressure = (CheckBox) findViewById(R.id.checkbox_pressure);
        chechboxAlbumini = (CheckBox) findViewById(R.id.checkbox_albumin);
        checkboxHb = (CheckBox) findViewById(R.id.checkbox_hb_below_60);
        chechkboxMlaloWaMtoto = (CheckBox) findViewById(R.id.checkbox_mlalo_wa_mtotos);
        checkboxChildDeath = (CheckBox) findViewById(R.id.checkbox_baby_death);
        checkboxKimo = (CheckBox) findViewById(R.id.checkbox_kimo);
        checkboxSugar = (CheckBox) findViewById(R.id.checkbox_sugar_level);
        checkboxUmriWaMimba = (CheckBox) findViewById(R.id.checkbox_umri_wa_mimba);
        submitButton = (Button) findViewById(R.id.submit);
    }

    private void setListeners() {


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            pressure = "true";
                        } else {
                            pressure = "false";
                        }

                    }
                });

                chechboxAlbumini.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (chechboxAlbumini.isChecked()) {
                            albumini = "true";
                        } else {
                            albumini = "false";
                        }
                    }
                });

                checkboxHb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            hb = "true";
                        } else {
                            hb = "false";
                        }
                    }
                });
                chechkboxMlaloWaMtoto.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (chechkboxMlaloWaMtoto.isChecked()) {
                            mlaloWaMtoto = "true";
                        } else {
                            mlaloWaMtoto = "false";
                        }
                    }
                });
                checkboxChildDeath.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (checkboxChildDeath.isChecked()) {
                            childDeath = "true";
                        } else {
                            childDeath = "false";
                        }
                    }
                });
                checkboxKimo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            kimo = "true";
                        } else {
                            kimo = "false";
                        }
                    }
                });
                checkboxSugar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (checkboxSugar.isChecked()) {
                            sugar = "true";
                        } else {
                            sugar = "false";
                        }
                    }
                });
                checkboxUmriWaMimba.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (checkboxUmriWaMimba.isEnabled()) {
                            umriWaMimba = "true";
                        } else {
                            umriWaMimba = "false";
                        }
                    }
                });

                HashMap<String, String> followHash = new HashMap<String, String>();
                followHash.put("facility_name", editTextFacilityName.toString());
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
                Log.d(TAG, "pressure = " + trial_one);
                Log.d(TAG, "pressure_1 = " + trial_two);
                Log.d(TAG, "pressure_2 = " + trial_three);
            }
        });
    }


}
