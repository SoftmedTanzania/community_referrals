package com.softmed.htmr_chw.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.pageradapter.SecuredNativeSmartRegisterCursorAdapterFragment;
import com.softmed.htmr_chw.util.LargeDiagonalCutPathDrawable;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientFollowup;
import org.ei.opensrp.provider.SmartRegisterClientsProvider;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientDetailFragment extends SecuredNativeSmartRegisterCursorAdapterFragment {
    public static final String CLIENT_FOLLOWUP = "item_id";
    private static final String TAG = ClientDetailFragment.class.getSimpleName();
    public ClientFollowup clientFollowup;
    private CommonRepository commonRepository;
    private Cursor cursor;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    private Gson gson = new Gson();
    private TextView name, age, gender, facility, feedback, contacts, sponsor, referedReason, residence, referedDate, note;

    public ClientDetailFragment() {
    }

    public static ClientDetailFragment newInstance(ClientFollowup clientFollowup) {
        ClientDetailFragment fragment = new ClientDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLIENT_FOLLOWUP, clientFollowup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientFollowup = (ClientFollowup) getArguments().getSerializable(CLIENT_FOLLOWUP);
        }
    }

    @Override
    protected void onCreation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        name = (TextView) rootView.findViewById(R.id.name);
        contacts = (TextView) rootView.findViewById(R.id.contacts);
        sponsor = (TextView) rootView.findViewById(R.id.sponsor);
        residence = (TextView) rootView.findViewById(R.id.residence);
        age = (TextView) rootView.findViewById(R.id.age);
        gender = (TextView) rootView.findViewById(R.id.gender);
        facility = (TextView) rootView.findViewById(R.id.refered);
        referedDate = (TextView) rootView.findViewById(R.id.refered_date);
        referedReason = (TextView) rootView.findViewById(R.id.followUp_reason);
        feedback = (TextView) rootView.findViewById(R.id.feedback);
        note = (TextView) rootView.findViewById(R.id.note);
        rootView.findViewById(R.id.details_layout).setBackground(new LargeDiagonalCutPathDrawable());

        setDetails(clientFollowup);

        return rootView;
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

    public String getFacilityName(String id) {

        commonRepository = context().commonrepository("facility");
        cursor = commonRepository.RawCustomQueryForAdapter("select * FROM facility where id ='" + id + "'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "facility");
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        if (commonPersonObjectList.size() > 0) {
            return commonPersonObjectList.get(0).getColumnmaps().get("name");
        } else {
            return "";
        }
    }

    private void setDetails(ClientFollowup clientFollowup) {

        String reg_date = dateFormat.format(clientFollowup.getDate_of_birth());
        String ageS = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date d = dateFormat.parse(reg_date);
            Calendar cal = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            cal.setTime(d);

            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            Integer ageInt = new Integer(age);
            ageS = ageInt.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((clientFollowup.getGender()).equalsIgnoreCase(getResources().getString(R.string.female))) {
            gender.setText(getResources().getString(R.string.female));
        } else {
            gender.setText(getResources().getString(R.string.male));
        }
        age.setText(ageS + " years");
        name.setText(clientFollowup.getFirst_name() + " " + clientFollowup.getMiddle_name() + ", " + clientFollowup.getSurname());
        contacts.setText(clientFollowup.getPhone_number());
        facility.setText(getFacilityName(clientFollowup.getFacility_id()));
        referedDate.setText(dateFormat.format(clientFollowup.getReferral_date()));
//        residence.setText(clientFollowup.getVillage()+" M/kiti -:"+clientFollowup.getVillage_leader());
        residence.setText(clientFollowup.getVillage());
        note.setText(clientFollowup.getReferral_status());

    }
}
