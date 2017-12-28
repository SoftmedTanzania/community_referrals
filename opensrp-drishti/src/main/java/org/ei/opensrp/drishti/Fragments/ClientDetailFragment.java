package org.ei.opensrp.drishti.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.util.LargeDiagonalCutPathDrawable;

public class ClientDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String CLIENT_REFERRAL = "item_id";
    public ClientReferral clientReferral;
    private  TextView name,contacts,sponsor,refered,referedDate,chwId,note;
    public ClientDetailFragment() {
    }

    public static ClientDetailFragment newInstance(ClientReferral clientReferral) {
        ClientDetailFragment fragment = new ClientDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLIENT_REFERRAL, clientReferral);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientReferral = (ClientReferral) getArguments().getSerializable(CLIENT_REFERRAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        name = (TextView) rootView.findViewById(R.id.name);
        contacts = (TextView) rootView.findViewById(R.id.contacts);
        sponsor = (TextView) rootView.findViewById(R.id.sponsor);
        refered = (TextView) rootView.findViewById(R.id.refered);
        refered = (TextView) rootView.findViewById(R.id.refered_date);
        referedDate = (TextView) rootView.findViewById(R.id.refered_reason);
        chwId = (TextView) rootView.findViewById(R.id.chw_id);
        note = (TextView) rootView.findViewById(R.id.note);
        rootView.findViewById(R.id.details_layout).setBackground(new LargeDiagonalCutPathDrawable());

        setDetails(clientReferral);

        return rootView;
    }



    private void setDetails(ClientReferral clientReferral){
        this.clientReferral = clientReferral;

        name . setText(clientReferral.getFirst_name()+" "+clientReferral.getMiddle_name()+", "+ clientReferral.getSurname());
        contacts.setText(clientReferral.getPhone_number());
        refered.setText(clientReferral.getFacility_id());
        referedDate.setText(clientReferral.getReferral_date());
        chwId.setText(clientReferral.getService_provider_uiid());
        note.setText(clientReferral.getStatus());

    }
}
