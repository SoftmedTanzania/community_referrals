package org.ei.opensrp.drishti.pageradapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.AncSmartRegisterActivity;
import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by martha on 8/22/17.
 */

public class CHWFollowUpPagerAdapter extends
        RecyclerView.Adapter<CHWFollowUpPagerAdapter.ViewHolder> {

    private List<ClientReferralPersonObject> client;
    private Context mContext;

    public CHWFollowUpPagerAdapter(Context context, List<ClientReferralPersonObject> clients) {
        client = clients;
        mContext = context;

    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.card_chw_followup, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ClientReferralPersonObject clientReferralPersonObject = client.get(position);
        String gsonReferral = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        Log.d("CHWFollowUpPagerAdapter", "gsonReferral = " + gsonReferral);
        ClientReferral clientReferral = new Gson().fromJson(gsonReferral, ClientReferral.class);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());


        viewHolder.nameTextView.setText(clientReferral.getfName());
        viewHolder.ageTextView.setText(clientReferral.getClientDOB() +" years");
        viewHolder.communicationTextView.setText(clientReferral.getPhoneNumber());
        viewHolder.villageTextView.setText(clientReferral.getKijiji() +"/ "+clientReferral.getKijitongoji());
        viewHolder.scheduleDateTextView.setText(clientReferral.getReferralDate());
        viewHolder.facilityTextView.setText(clientReferral.getFacilityId());

    }

    @Override
    public int getItemCount() {
        return client.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, communicationTextView, scheduleDateTextView, villageTextView, ageTextView,facilityTextView;
        public ImageView iconOptions;

        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.textName);
            communicationTextView = (TextView) itemView.findViewById(R.id.communication);
            villageTextView = (TextView) itemView.findViewById(R.id.textPhysicalAddress);
            ageTextView = (TextView) itemView.findViewById(R.id.textAge);
            scheduleDateTextView = (TextView) itemView.findViewById(R.id.textNextVisitDate);
            facilityTextView = (TextView) itemView.findViewById(R.id.facility);
            iconOptions = (ImageView) itemView.findViewById(R.id.iconOptions);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((AncSmartRegisterActivity) mContext).showFollowUpDetailsDialog(client.get(getAdapterPosition()));
                }
            });

            iconOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show options
                    showPop(getAdapterPosition(), view);
                }
            });
        }

    }

    public void showPop(final int position, View anchor) {

        PopupMenu popupMenu = new PopupMenu((AncSmartRegisterActivity) mContext, anchor);
        // inflate menu xml res
        popupMenu.getMenuInflater().inflate(R.menu.menu_follow_up_details, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    // TODO: handle option selected
                    case R.id.popOpt1:
                        ((AncSmartRegisterActivity) mContext).showFollowUpFormDialog(client.get(position));
                        return true;

                    case R.id.popOpt2:
                        ((AncSmartRegisterActivity) mContext).showFollowUpDetailsDialog(client.get(position));
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}