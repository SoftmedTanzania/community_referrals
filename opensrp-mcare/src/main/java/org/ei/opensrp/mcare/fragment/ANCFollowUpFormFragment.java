package org.ei.opensrp.mcare.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ei.opensrp.mcare.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.ei.opensrp.util.FormUtils.populateJSONWithData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ANCFollowUpFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 
 * create an instance of this fragment.
 */
public class ANCFollowUpFormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ANCFollowUpFormFragment.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button submitButton;
    private CheckBox checkBoxPressure,checkboxHb,chechboxAlbumini, checkboxSugar, checkboxUmriWaMimba,
                     checkboxChildDeath, chechkboxMlaloWaMtoto, checkboxKimo;
    private String pressure,hb, albumini,sugar, umriWaMimba,childDeath,mlaloWaMtoto,kimo;
    private String formName;
    private EditText editTextFacilityName;

    private OnFragmentInteractionListener mListener;

    public ANCFollowUpFormFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ancfollow_up_form, container, false);
        findViews(v);
        setListeners();

        return v;
    }

    private void findViews(View view) {
        editTextFacilityName = (EditText) view.findViewById(R.id.facility);
        checkBoxPressure = (CheckBox) view.findViewById(R.id.checkbox_pressure);
        chechboxAlbumini = (CheckBox) view.findViewById(R.id.checkbox_albumin);
        checkboxHb = (CheckBox) view.findViewById(R.id.checkbox_hb_below_60);
        chechkboxMlaloWaMtoto = (CheckBox) view.findViewById(R.id.checkbox_mlalo_wa_mtotos);
        checkboxChildDeath = (CheckBox) view.findViewById(R.id.checkbox_baby_death);
        checkboxKimo = (CheckBox) view.findViewById(R.id.checkbox_kimo);
        checkboxSugar = (CheckBox) view.findViewById(R.id.checkbox_sugar_level);
        checkboxUmriWaMimba = (CheckBox) view.findViewById(R.id.checkbox_umri_wa_mimba);
        submitButton = (Button)view.findViewById(R.id.submit);


    }

    private void setListeners() {




        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkBoxPressure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if ( isChecked )
                        {   pressure = "true";
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
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                        if ( isChecked) {
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
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
