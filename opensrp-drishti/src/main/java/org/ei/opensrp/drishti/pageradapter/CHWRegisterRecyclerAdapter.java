package org.ei.opensrp.drishti.pageradapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.ChwSmartRegisterActivity;
import org.ei.opensrp.drishti.ClientsDetailsActivity;
import org.ei.opensrp.drishti.Fragments.ClientDetailFragment;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by martha on 8/22/17.
 */

public class CHWRegisterRecyclerAdapter extends
        RecyclerView.Adapter<CHWRegisterRecyclerAdapter.ViewHolder> {

    private List<ClientReferralPersonObject> clients;
    private Context mContext;
    private ClientReferralPersonObject client;
    private boolean mTwoPane=true;

    public CHWRegisterRecyclerAdapter(Context context, List<ClientReferralPersonObject> clients) {
        this.clients = clients;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.card_chw_pre_reg, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        client = clients.get(position);
        String gsonReferral = Utils.convertStandardJSONString(client.getDetails());
        final ClientReferral clientReferral = new Gson().fromJson(gsonReferral,ClientReferral.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        // Set item views based on your views and data model
        TextView phoneNumberTextView = viewHolder.phoneNumberTextView;
        TextView CBHS = viewHolder.CBHSTextView;
        phoneNumberTextView.setText(clientReferral.getPhone_number());
        viewHolder.nameTextView.setText(clientReferral.getFirst_name()+" " + clientReferral.getMiddle_name()+", "+clientReferral.getSurname());

        if(clientReferral.getCommunity_based_hiv_service()!=null) {
            if(!clientReferral.getCommunity_based_hiv_service().equals(""))
                CBHS.setText("CBHS : " + clientReferral.getCommunity_based_hiv_service());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    ClientDetailFragment fragment = ClientDetailFragment.newInstance(clientReferral);
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ClientsDetailsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("client_referral", clientReferral);
                    intent.putExtras(bundle);

                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, phoneNumberTextView, CBHSTextView;
        ImageView iconOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            phoneNumberTextView = (TextView) itemView.findViewById(R.id.phone_number);
            CBHSTextView = (TextView) itemView.findViewById(R.id.community_based_hiv_service);
            iconOptions = (ImageView) itemView.findViewById(R.id.iconOptions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((ChwSmartRegisterActivity) mContext).showPreRegistrationDetailsDialog(clients.get(getAdapterPosition()));
                }
            });




//            iconOptions.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // show options
//                    showPop(getAdapterPosition(), view);
//                }
//            });

        }

    }


    public void showPop(final int position, View anchor) {

        PopupMenu popupMenu = new PopupMenu((ChwSmartRegisterActivity) mContext, anchor);
        // inflate menu xml res
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_details, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    // TODO: handle option selected
                    case R.id.popOpt1:
                        ((ChwSmartRegisterActivity) mContext).showPreRegistrationVisitDialog(clients.get(position));
                        return true;

                    case R.id.popOpt2:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.popOpt3:
                        // delete mother
                        ((ChwSmartRegisterActivity) mContext).confirmDelete(clients.get(position));
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    public void setIsInTwoPane(boolean mTwoPane){
        this.mTwoPane = mTwoPane;
    }
}