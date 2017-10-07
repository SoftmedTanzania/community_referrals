package com.softmed.uzazisalama.pageradapter;

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

import com.softmed.uzazisalama.AncSmartRegisterActivity;
import com.softmed.uzazisalama.DataModels.PregnantMom;
import org.ei.opensrp.drishti.R;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.util.DatesHelper;
import com.softmed.uzazisalama.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by martha on 8/22/17.
 */

public class CHWFollowUpPagerAdapter extends
        RecyclerView.Adapter<CHWFollowUpPagerAdapter.ViewHolder> {

    private List<MotherPersonObject> fMother;
    private Context mContext;

    public CHWFollowUpPagerAdapter(Context context, List<MotherPersonObject> mothers) {
        fMother = mothers;
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

        MotherPersonObject mother = fMother.get(position);
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        Log.d("CHWFollowUpPagerAdapter", "gsonMom = " + gsonMom);
        PregnantMom pregnantMom = new Gson().fromJson(gsonMom, PregnantMom.class);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        long lnmp = pregnantMom.getDateLNMP();
        String anc1 = dateFormat.format(DatesHelper.calculate1stVisitFromLNMP(lnmp));
        String anc2 = dateFormat.format(DatesHelper.calculate2ndVisitFromLNMP(lnmp));
        String anc3 = dateFormat.format(DatesHelper.calculate3rdVisitFromLNMP(lnmp));
        String anc4 = dateFormat.format(DatesHelper.calculate4thVisitFromLNMP(lnmp));

        viewHolder.nameTextView.setText(mother.getMOTHERS_FIRST_NAME() + " " + mother.getMOTHERS_LAST_NAME());
        viewHolder.ageTextView.setText(String.valueOf(pregnantMom.getAge()) +" years");
        viewHolder.riskTextView.setText("high");
        viewHolder.villageTextView.setText(pregnantMom.getPhysicalAddress());
        viewHolder.uniqueIDTextView.setText(pregnantMom.getId());
        viewHolder.facilityTextView.setText(pregnantMom.getFacilityId());
        viewHolder.anc1TextView.setText(anc1);
        viewHolder.anc2TextView.setText(anc2);
        viewHolder.anc3TextView.setText(anc3);
        viewHolder.anc4TextView.setText(anc4);

    }

    @Override
    public int getItemCount() {
        return fMother.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, riskTextView, uniqueIDTextView, villageTextView, ageTextView, numberTextView, facilityTextView, anc1TextView, anc2TextView, anc3TextView, anc4TextView;
        public ImageView iconOptions;

        public ViewHolder(View itemView) {

            super(itemView);

            iconOptions = (ImageView) itemView.findViewById(R.id.action_options);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            riskTextView = (TextView) itemView.findViewById(R.id.risk);
            uniqueIDTextView = (TextView) itemView.findViewById(R.id.unique);
            villageTextView = (TextView) itemView.findViewById(R.id.location);
            ageTextView = (TextView) itemView.findViewById(R.id.age);
            numberTextView = (TextView) itemView.findViewById(R.id.communication);
            facilityTextView = (TextView) itemView.findViewById(R.id.facility);
            anc1TextView = (TextView) itemView.findViewById(R.id.date_one);
            anc2TextView = (TextView) itemView.findViewById(R.id.date_two);
            anc3TextView = (TextView) itemView.findViewById(R.id.date_three);
            anc4TextView = (TextView) itemView.findViewById(R.id.date_four);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((AncSmartRegisterActivity) mContext).showFollowUpDetailsDialog(fMother.get(getAdapterPosition()));
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
                        ((AncSmartRegisterActivity) mContext).showFollowUpFormDialog(fMother.get(position));
                        return true;

                    case R.id.popOpt2:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}