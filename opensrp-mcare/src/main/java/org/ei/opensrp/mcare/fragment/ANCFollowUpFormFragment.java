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
        submitButton = (Button)view.findViewById(R.id.submit);


    }

    private void setListeners() {



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> dataHash = new HashMap<String, String>();
                dataHash.put("facility_name",editTextFacilityName.toString());
//                dataHash.put("existing_location",currentLocation);

                         


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
