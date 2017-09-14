package org.ei.opensrp.mcare.pageradapter;

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

import org.ei.opensrp.mcare.R;
import org.ei.opensrp.mcare.chw.CHWRegisterActivity;
import org.ei.opensrp.mcare.datamodels.PreRegisteredMother;

import java.util.List;


/**
 * Created by martha on 8/22/17.
 */

public class CHWRegisterRecyclerAdapter extends
        RecyclerView.Adapter<CHWRegisterRecyclerAdapter.ViewHolder> {

    private List<PreRegisteredMother> mothers;
    private Context mContext;

    public CHWRegisterRecyclerAdapter(Context context, List<PreRegisteredMother> mothers) {
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

        PreRegisteredMother mother = mothers.get(position);

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
                    ((CHWRegisterActivity) mContext).showPreRegistrationDetailsDialog(mothers.get(getAdapterPosition()));
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


    public void showPop(int position, View anchor) {

        PopupMenu popupMenu = new PopupMenu((CHWRegisterActivity) mContext, anchor);
        // inflate menu xml res
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_details, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    // TODO: handle option selected
                    case R.id.popOpt1:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.popOpt2:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.popOpt3:
                        Toast.makeText(mContext, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }
}