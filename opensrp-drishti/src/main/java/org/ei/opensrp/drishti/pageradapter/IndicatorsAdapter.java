package org.ei.opensrp.drishti.pageradapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ei.opensrp.drishti.DataModels.RiskIndicator;

import org.ei.opensrp.drishti.R;

import java.util.List;

/**
 * Created by ali on 10/11/17.
 */

public class IndicatorsAdapter extends RecyclerView.Adapter<IndicatorsAdapter.ViewHolder> {

    private List<RiskIndicator> indicatorList;

    private int colorRed, colorBlue, detectedIndicators = 0;
    private final static String TAG = IndicatorsAdapter.class.getSimpleName();

    public IndicatorsAdapter(List<RiskIndicator> indicatorList, Context context) {
        this.indicatorList = indicatorList;
        this.colorBlue = ContextCompat.getColor(context, android.R.color.holo_blue_light);
        this.colorRed = ContextCompat.getColor(context, android.R.color.holo_red_light);

        Log.d(TAG, "indicators list = " + indicatorList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textIndicator, textComment;
        ImageView iconIndicator;

        ViewHolder(View itemView) {
            super(itemView);

            textIndicator = (TextView) itemView.findViewById(R.id.textIndicator);
            textComment = (TextView) itemView.findViewById(R.id.textComment);
            iconIndicator = (ImageView) itemView.findViewById(R.id.iconIndicator);
        }
    }

    @Override
    public IndicatorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_indicator, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IndicatorsAdapter.ViewHolder holder, int position) {

        RiskIndicator indicator = indicatorList.get(position);

        holder.textIndicator.setText(indicator.getTitle());

        if (indicator.isDetected()) {
            holder.iconIndicator.setImageResource(R.drawable.ic_alert_circle_outline);
            holder.iconIndicator.setColorFilter(colorRed);
            holder.textComment.setText("Hatari");
            this.detectedIndicators++;
        } else {
            holder.iconIndicator.setImageResource(R.drawable.ic_ok);
            holder.iconIndicator.setColorFilter(colorBlue);
            holder.textComment.setText("Salama");
        }

        if (position == indicatorList.size() - 1)
            Log.d(TAG, "Detected risks = " + detectedIndicators + ", undetected = "
                    + (indicatorList.size() - detectedIndicators));

    }

    @Override
    public int getItemCount() {
        return indicatorList.size();
    }
}
