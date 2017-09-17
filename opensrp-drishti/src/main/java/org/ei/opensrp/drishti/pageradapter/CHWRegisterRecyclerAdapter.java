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
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.R;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.chw.CHWSmartRegisterActivity;
import org.ei.opensrp.drishti.DataModels.PreRegisteredMother;
import org.ei.opensrp.drishti.util.DatesHelper;
import org.ei.opensrp.drishti.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by martha on 8/22/17.
 */

public class CHWRegisterRecyclerAdapter extends
        RecyclerView.Adapter<CHWRegisterRecyclerAdapter.ViewHolder> {

    private List<MotherPersonObject> mothers;
    private Context mContext;
    private MotherPersonObject mother;

    public CHWRegisterRecyclerAdapter(Context context, List<MotherPersonObject> mothers) {
        this.mothers = mothers;
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

        mother = mothers.get(position);
        String gsonMom = Utils.convertStandardJSONString(mother.getDetails());
        PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String reg_date = dateFormat.format(pregnantMom.getDateReg());

        // Set item views based on your views and data model
        TextView name = viewHolder.nameTextView;
        TextView edd = viewHolder.eddTextView;
        TextView visited = viewHolder.visitedTextView;
        TextView risk = viewHolder.riskTextView;

        visited.setText(reg_date);
        edd.setText(mother.getEXPECTED_DELIVERY_DATE());
        name.setText(mother.getMOTHERS_FIRST_NAME() +" "+mother.getMOTHERS_LAST_NAME());
        //todo check the risk level factor from the indicators
        risk.setText("high");

    }

    @Override
    public int getItemCount() {
        return mothers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, eddTextView, visitedTextView, riskTextView;
        ImageView iconOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            eddTextView = (TextView) itemView.findViewById(R.id.edd);
            visitedTextView = (TextView) itemView.findViewById(R.id.visited);
            riskTextView = (TextView) itemView.findViewById(R.id.risk);
            iconOptions = (ImageView) itemView.findViewById(R.id.iconOptions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pass mother to show details
                    ((AncSmartRegisterActivity) mContext).showPreRegistrationDetailsDialog(mothers.get(getAdapterPosition()));
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
                        ((AncSmartRegisterActivity) mContext).showPreRegistrationVisitDialog(mothers.get(position));
                        return true;

                    case R.id.popOpt2:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.popOpt3:
                        // delete mother
                        ((AncSmartRegisterActivity) mContext).confirmDelete();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}