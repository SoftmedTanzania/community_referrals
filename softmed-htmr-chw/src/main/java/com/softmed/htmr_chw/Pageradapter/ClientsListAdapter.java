package com.softmed.htmr_chw.Pageradapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softmed.htmr_chw.Activities.ClientDetails;
import com.softmed.htmr_chw.R;

import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Client;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by martha on 8/22/17.
 */

public class ClientsListAdapter extends
        RecyclerView.Adapter<ClientsListAdapter.ViewHolder> {
    private static String TAG = ClientsListAdapter.class.getSimpleName();
    private CommonRepository commonRepository;
    private List<Client> clients = new ArrayList<>();;
    private Context mContext;

    public ClientsListAdapter(Context context, List<Client> client) {
        clients = client;
        mContext = context;

    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.client_list_item, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {


        final Client client = clients.get(position);

        viewHolder.nameTextView.setText(client.getFirst_name()+" "+client.getMiddle_name()+" "+client.getSurname());
        viewHolder.phoneNumber.setText(client.getPhone_number());
        viewHolder.village.setText(client.getVillage());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mContext, ClientDetails.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("client", client);

                intent1.putExtras(bundle);
                mContext.startActivity(intent1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return clients.size();
//        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, phoneNumber, age, village;
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.client_name);
            phoneNumber = (TextView) itemView.findViewById(R.id.phone_number);
            age = (TextView) itemView.findViewById(R.id.age);
            village = (TextView) itemView.findViewById(R.id.village);
            this.itemView=itemView;


        }

    }

}