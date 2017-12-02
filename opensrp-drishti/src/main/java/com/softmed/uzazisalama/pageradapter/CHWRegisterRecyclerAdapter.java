package org.ei.opensrp.drishti.pageradapter;

import android.content.Context;
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

public class CHWRegisterRecyclerAdapter extends
        RecyclerView.Adapter<CHWRegisterRecyclerAdapter.ViewHolder> {

    private List<ClientReferralPersonObject> clients;
    private Context mContext;
    private ClientReferralPersonObject client;

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
        ClientReferral clientReferral = new Gson().fromJson(gsonReferral,ClientReferral.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        String reg_date = dateFormat.format(clientReferral.getDateLastVisited());

        // Set item views based on your views and data model
        TextView name = viewHolder.nameTextView;
        TextView referralDate = viewHolder.referralDateTextView;
        TextView visited = viewHolder.visitedTextView;
        TextView CBHS = viewHolder.CBHSTextView;

        visited.setText("moja");
        referralDate.setText(clientReferral.getReferralDate());
        name.setText(clientReferral.getfName());
        CBHS.setText(clientReferral.getCBHS());

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, referralDateTextView, visitedTextView, CBHSTextView;
        ImageView iconOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            referralDateTextView = (TextView) itemView.findViewById(R.id.referralDate);
            visitedTextView = (TextView) itemView.findViewById(R.id.visited);
            CBHSTextView = (TextView) itemView.findViewById(R.id.CBHS);
            iconOptions = (ImageView) itemView.findViewById(R.id.iconOptions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((AncSmartRegisterActivity) mContext).showPreRegistrationDetailsDialog(clients.get(getAdapterPosition()));
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
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_details, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    // TODO: handle option selected
                    case R.id.popOpt1:
                        ((AncSmartRegisterActivity) mContext).showPreRegistrationVisitDialog(clients.get(position));
                        return true;

                    case R.id.popOpt2:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.popOpt3:
                        // delete mother
                        ((AncSmartRegisterActivity) mContext).confirmDelete(clients.get(position));
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}