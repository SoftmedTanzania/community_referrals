package org.ei.opensrp.mcare.pageradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.mcare.datamodels.PreRegisteredMother;
import org.ei.opensrp.mcare.fragment.AncRegister1stFragment;
import org.ei.opensrp.mcare.fragment.AncRegister2ndFragment;
import org.ei.opensrp.mcare.fragment.CHWFollowUpFragment;
import org.ei.opensrp.mcare.fragment.CHWRegistrationFragment;

import java.util.List;


/**
 * Created by martha on 8/22/17.
 */

public class CHWRegisterPagerAdapter extends
        RecyclerView.Adapter<CHWRegisterPagerAdapter.ViewHolder> {

    private List<PreRegisteredMother> vMother;
    private Context mContext;

    public CHWRegisterPagerAdapter(Context context, List<PreRegisteredMother> mothers) {
        vMother = mothers;
        mContext = context;
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

        PreRegisteredMother mother = vMother.get(position);

        // Set item views based on your views and data model
        TextView name = viewHolder.nameTextView;
        name.setText(mother.getName());
        TextView edd = viewHolder.eddTextView;
        edd.setText(mother.getEdd());
        TextView visited = viewHolder.visitedTextView;
        visited.setText(mother.getVisited());
        TextView risk = viewHolder.riskTextView;
        risk.setText(mother.getRisk());

    }

    @Override
    public int getItemCount() {
        return vMother.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView,eddTextView, visitedTextView,riskTextView;

        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.name);
            eddTextView = (TextView) itemView.findViewById(R.id.edd);
            visitedTextView = (TextView) itemView.findViewById(R.id.visited);
            riskTextView = (TextView) itemView.findViewById(R.id.risk);
        }


    }
}