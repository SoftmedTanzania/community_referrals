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
 * Use the {@link ANCFollowUpFormFragment#newInstance} factory method to
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ANCFollowUpFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ANCFollowUpFormFragment newInstance(String param1, String param2) {
        ANCFollowUpFormFragment fragment = new ANCFollowUpFormFragment();
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
        View v = inflater.inflate(R.layout.activity_house_hold_register_form, container, false);
        findViews(v);
        setListeners();

        return v;
    }

    private void findViews(View view) {
        editTextFacilityName = (EditText) view.findViewById(R.id.editTextGoBHHID);
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

        checkBoxPressure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkBoxPressure.isChecked()) {
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

        checkboxHb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkboxHb.isChecked()) {
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
        checkboxKimo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkboxKimo.isChecked()) {
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
                if (checkboxUmriWaMimba.isChecked()) {
                    umriWaMimba = "true";
                } else {
                    umriWaMimba = "false";
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> dataHash = new HashMap<String, String>();
                dataHash.put("facility_name",editTextFacilityName.toString());
                dataHash.put("pressure", pressure);
                dataHash.put("hb", hb);
                dataHash.put("albumin", albumini);
                dataHash.put("sugar", sugar);
                dataHash.put("mlaloWaMtoto", mlaloWaMtoto);
                dataHash.put("childDeath", childDeath);
                dataHash.put("umriWaMimba", umriWaMimba);
                dataHash.put("kimo", kimo);


                String trial_one = dataHash.get(pressure);
                String trial_two = dataHash.get(hb);
                String trial_three = dataHash.get(kimo);
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
