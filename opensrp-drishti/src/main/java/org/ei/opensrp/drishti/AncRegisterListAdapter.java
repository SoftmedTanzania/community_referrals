package org.ei.opensrp.drishti;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.cursoradapter.SmartRegisterCLientsProviderForCursorAdapter;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 9/13/17.
 */

public class AncRegisterListAdapter extends RecyclerView.Adapter<AncRegisterListAdapter.ViewHolder> {
    //    private final SmartRegisterCLientsProviderForCursorAdapter listItemProvider;
    private Context context;
    private Cursor cursor;
    private Gson gson = new Gson();
    private CustomMotherRepository customMotherRepository;
    private android.content.Context appContext;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();

    private static final String TAG = AncRegisterListAdapter.class.getSimpleName(),
            TABLE_NAME = "wazazi_salama_mother";


    public AncRegisterListAdapter(Context context, Cursor cursor, SmartRegisterCLientsProviderForCursorAdapter listItemProvider, CustomMotherRepository customMotherRepository) {
//        super(context, cursor);
//        this.listItemProvider = listItemProvider;
        this.context = context;
        this.customMotherRepository = customMotherRepository;
        this.cursor = cursor;
        this.motherPersonList = customMotherRepository.readAllMotherForField(cursor, TABLE_NAME);
    }

    public AncRegisterListAdapter(Context context, CustomMotherRepository customMotherRepository, Cursor cursor, android.content.Context appContext) {
        this.context = context;
        this.customMotherRepository = customMotherRepository;
        this.motherPersonList = customMotherRepository.readAllMotherForField(cursor, TABLE_NAME);
        this.appContext = appContext;

        Log.d(TAG, "repo count = " + customMotherRepository.count() + ", list count = " + motherPersonList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textAge, textPhysicalAddress, textEDD, textLastVisitDate, textNextVisitDate;
        ImageView imageProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textAge = (TextView) itemView.findViewById(R.id.textAge);
            textPhysicalAddress = (TextView) itemView.findViewById(R.id.textPhysicalAddress);
            textLastVisitDate = (TextView) itemView.findViewById(R.id.textLastVisitDate);
            textNextVisitDate = (TextView) itemView.findViewById(R.id.textNextVisitDate);
            imageProfilePic = (ImageView) itemView.findViewById(R.id.imageProfilePic);
            textEDD = (TextView) itemView.findViewById(R.id.textEDD);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to AncDetailActivity
                    appContext.startActivity(new Intent(appContext, AncDetailActivityAlt.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("mom", motherPersonList.get(getAdapterPosition()).getDetails()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.smart_register_mcare_anc_client_alt, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        CommonPersonObject personinlist = commonRepository.readAllcommonforCursorAdapter(cursor);
//        CommonPersonObjectClient pClient = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
//        pClient.setColumnmaps(personinlist.getColumnmaps());
        // listItemProvider.getView(pClient, view);

        // todo get item form list
        MotherPersonObject motherPersonObject = motherPersonList.get(position);
        PregnantMom mom = gson.fromJson(motherPersonObject.getDetails(), PregnantMom.class);

        holder.textName.setText(mom.getName());
        holder.textEDD.setText(motherPersonObject.getEXPECTED_DELIVERY_DATE());
        holder.textPhysicalAddress.setText(mom.getPhysicalAddress());
        holder.textAge.setText("Miaka");
        holder.textAge.append(" " + mom.getAge());

    }

    @Override
    public int getItemCount() {
        //  int itemCount = (int) customMotherRepository.count();
        int itemCount = motherPersonList.size();
        Log.d(TAG, "item count = " + itemCount);
        return itemCount;
    }
}
