package org.ei.opensrp.drishti.pageradapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ClientReferral;
import org.ei.opensrp.drishti.ChwSmartRegisterActivity;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by martha on 8/22/17.
 */

public class ReferredClientsListAdapter extends
        RecyclerView.Adapter<ReferredClientsListAdapter.ViewHolder> {
    private static String TAG = ReferredClientsListAdapter.class.getSimpleName();
    private CommonRepository commonRepository;
    private List<ClientReferralPersonObject> client;
    private Context mContext;

    public ReferredClientsListAdapter(Context context, List<ClientReferralPersonObject> clients, CommonRepository commonRepository) {
        client = clients;
        mContext = context;
        this.commonRepository = commonRepository;

    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.referal_list_client_item, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        ClientReferralPersonObject clientReferralPersonObject = client.get(position);

        String gsonReferral = Utils.convertStandardJSONString(clientReferralPersonObject.getDetails());
        Log.d(TAG, "gsonReferral = " + gsonReferral);
        ClientReferral clientReferral = new Gson().fromJson(gsonReferral, ClientReferral.class);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());


        viewHolder.nameTextView.setText(clientReferral.getFirst_name()+" "+clientReferral.getMiddle_name()+" "+clientReferral.getSurname());
        viewHolder.referralReason.setText(clientReferral.getReferral_reason());
        viewHolder.scheduleDateTextView.setText(dateFormat.format(clientReferral.getReferral_date()));
        viewHolder.serviceName.setText(getReferralServiceName(clientReferral.getReferral_service_id()));


        if(clientReferral.getStatus().equals("0")){
            viewHolder.referralStatus.setText("Pending");
            viewHolder.statusIcon.setBackgroundColor(mContext.getResources().getColor(R.color.blue_400));
        }else if(clientReferral.getStatus().equals("1")){
            viewHolder.referralStatus.setText("Successful");
            viewHolder.statusIcon.setBackgroundColor(mContext.getResources().getColor(R.color.green_400));
        }else{
            viewHolder.referralStatus.setText("Unsuccessful");
            viewHolder.statusIcon.setBackgroundColor(mContext.getResources().getColor(R.color.red_400));
        }


    }

    @Override
    public int getItemCount() {
        return client.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, referralReason, scheduleDateTextView, referralStatus,serviceName;
        public View statusIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.client_name);
            referralReason = (TextView) itemView.findViewById(R.id.referral_reasons);
            referralStatus = (TextView) itemView.findViewById(R.id.status);
            scheduleDateTextView = (TextView) itemView.findViewById(R.id.ref_date);
            serviceName = (TextView) itemView.findViewById(R.id.referral_service);
            statusIcon = itemView.findViewById(R.id.status_color);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((ChwSmartRegisterActivity) mContext).showFollowUpDetailsDialog(client.get(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show options
                    showPop(getAdapterPosition(), view);
                }
            });
        }

    }

    public void showPop(final int position, View anchor) {

        PopupMenu popupMenu = new PopupMenu((ChwSmartRegisterActivity) mContext, anchor);
        // inflate menu xml res
        popupMenu.getMenuInflater().inflate(R.menu.menu_follow_up_details, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    // TODO: handle option selected
                    case R.id.popOpt1:
                        ((ChwSmartRegisterActivity) mContext).showPreRegistrationVisitDialog(client.get(position));
                        return true;

                    case R.id.popOpt2:
                        ((ChwSmartRegisterActivity) mContext).showFollowUpDetailsDialog(client.get(position));
                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    public String getReferralServiceName(String id){
        Cursor cursor = commonRepository.RawCustomQueryForAdapter("select * FROM referral_service where id ='"+ id +"'");

        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, "referral_service");

        return commonPersonObjectList.get(0).getColumnmaps().get("name");
    }
}