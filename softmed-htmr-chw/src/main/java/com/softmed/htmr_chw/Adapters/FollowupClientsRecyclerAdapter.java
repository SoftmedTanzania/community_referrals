package com.softmed.htmr_chw.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmed.htmr_chw.Activities.FollowupReferralDetailsActivity;
import com.softmed.htmr_chw.Fragments.FollowupClientDetailFragment;
import com.softmed.htmr_chw.R;
import com.softmed.htmr_chw.Domain.ClientReferral;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by martha on 8/22/17.
 */

public class FollowupClientsRecyclerAdapter extends
        RecyclerView.Adapter<FollowupClientsRecyclerAdapter.ViewHolder> {

    private static String TAG = FollowupClientsRecyclerAdapter.class.getSimpleName();
    private List<ClientReferral> clients;
    private Context mContext;
    private boolean mTwoPane = true;
    private Typeface robotoRegular,sansBold;

    public FollowupClientsRecyclerAdapter(Context context, List<ClientReferral> clients) {
        this.clients = clients;
        this.mContext = context;

        robotoRegular = Typeface.createFromAsset(mContext.getAssets(), "roboto_regular.ttf");
        sansBold = Typeface.createFromAsset(mContext.getAssets(), "google_sans_bold.ttf");

        Log.d(TAG, "follow up adapter constructor : " + new Gson().toJson(clients));
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.followup_referral_item, parent, false);


        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final ClientReferral client = clients.get(position);

        Log.d(TAG, "follow up adapter : " + new Gson().toJson(clients));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        // Set item views based on your views and data model
        TextView phoneNumberTextView = viewHolder.phoneNumberTextView;
        TextView CBHS = viewHolder.CBHSTextView;

        phoneNumberTextView.setText(client.getPhone_number());
        viewHolder.nameTextView.setText(client.getFirst_name() + " " + client.getMiddle_name() + ", " + client.getSurname());

        if (client.getCommunity_based_hiv_service() != null) {
            if (!client.getCommunity_based_hiv_service().equals(""))
                CBHS.setText("CBHS : " + client.getCommunity_based_hiv_service());
        }


        try {
            long diff = Calendar.getInstance().getTimeInMillis() - client.getAppointment_date();

            long daysSinceAppointment = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (daysSinceAppointment > 28) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_900));
            } else if (daysSinceAppointment > 22) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_700));
            } else if (daysSinceAppointment > 19) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_600));
            } else if (daysSinceAppointment > 14) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_400));
            } else if (daysSinceAppointment > 9) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_200));
            } else if (daysSinceAppointment > 6) {
                viewHolder.statusColor.setBackgroundColor(mContext.getResources().getColor(R.color.red_050));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    FollowupClientDetailFragment fragment = FollowupClientDetailFragment.newInstance(client);
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FollowupReferralDetailsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("client_followup", client);
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
        View itemView,statusColor;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            phoneNumberTextView = (TextView) itemView.findViewById(R.id.phone_number);
            CBHSTextView = (TextView) itemView.findViewById(R.id.community_based_hiv_service);
            statusColor = itemView.findViewById(R.id.referral_layout);

            nameTextView.setTypeface(sansBold);
            phoneNumberTextView.setTypeface(sansBold);
            CBHSTextView.setTypeface(robotoRegular);

        }

    }

    public void setIsInTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }
}