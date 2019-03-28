package com.softmed.htmr_chw.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softmed.htmr_chw.Activities.ClientDetailsActivity;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Client;

import java.util.ArrayList;
import java.util.List;

import static org.ei.opensrp.AllConstants.ENGLISH_LOCALE;


/**
 * Created by Coze on 2/10/19.
 */

public class CBHSClientsListAdapter extends
        RecyclerView.Adapter<CBHSClientsListAdapter.ViewHolder> {
    private static String TAG = CBHSClientsListAdapter.class.getSimpleName();
    private CommonRepository commonRepository;
    private List<Client> clients = new ArrayList<>();
    private Context mContext;

    public CBHSClientsListAdapter(Context context, List<Client> client) {
        clients = client;
        mContext = context;

    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.cbhs_client_list_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Client client = clients.get(position);

        viewHolder.nameTextView.setText(client.getFirst_name() + " " + client.getMiddle_name() + " " + client.getSurname());
        viewHolder.phoneNumber.setText(client.getPhone_number());
        viewHolder.village.setText(client.getVillage());

        if(client.getGender().equalsIgnoreCase("male")){
            viewHolder.gender.setText(mContext.getString(R.string.male));
        }else{
            viewHolder.gender.setText(mContext.getString(R.string.female));
        }

        viewHolder.gender.setText(client.getGender());
        viewHolder.cbhsNo.setText(client.getCommunity_based_hiv_service());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mContext, ClientDetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("client", client);
                bundle.putString("type", "CBHS");

                intent1.putExtras(bundle);
                mContext.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, phoneNumber, gender,cbhsNo, village;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.client_name);
            phoneNumber = itemView.findViewById(R.id.phone_number);
            gender = itemView.findViewById(R.id.gender);
            village = itemView.findViewById(R.id.village);
            cbhsNo = itemView.findViewById(R.id.cbhs_no);
            this.itemView = itemView;

        }

    }

}