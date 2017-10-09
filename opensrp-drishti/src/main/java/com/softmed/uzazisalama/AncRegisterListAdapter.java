package com.softmed.uzazisalama;

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
import org.ei.opensrp.commonregistry.CommonRepository;

import com.softmed.uzazisalama.DataModels.PregnantMom;
import com.softmed.uzazisalama.Fragments.AncRegisterFormFragment;

import org.ei.opensrp.drishti.R;

import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.softmed.uzazisalama.util.Utils.convertStandardJSONString;

/**
 * Created by ali on 9/13/17.
 */

public class AncRegisterListAdapter extends RecyclerView.Adapter<AncRegisterListAdapter.ViewHolder> {
    //    private final SmartRegisterCLientsProviderForCursorAdapter listItemProvider;
    private Context context;
    private Cursor cursor;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();

    private static final String TAG = AncRegisterListAdapter.class.getSimpleName(),
            TABLE_NAME = "wazazi_salama_mother";


    public AncRegisterListAdapter(Context context, CommonRepository commonRepository, Cursor cursor, android.content.Context appContext) {
        this.context = context;
        this.appContext = appContext;
        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.motherPersonList = Utils.convertToMotherPersonObjectList(commonPersonObjectList);

        Log.d(TAG, "mother list = " + gson.toJson(motherPersonList));

        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + motherPersonList.size());

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
                    MotherPersonObject mother = motherPersonList.get(getAdapterPosition());
                    Log.d(TAG,"first string ="+gson.toJson(mother));
                    String gsonMom = Utils.convertStandardJSONString(motherPersonList.get(getAdapterPosition()).getDetails());
                    final PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
                    Log.d(TAG, "commonPersonList id= " + pregnantMom.getId());
                    Log.d(TAG, "commonPersonList string= " + gson.toJson(pregnantMom));
                    Log.d(TAG, "commonPersonList1 string= " + gsonMom);
                    Log.d(TAG, "commonPersonList2 string= " + gsonMom+gson.toJson(mother));
                    if(pregnantMom.getReg_type().equals("2")){
                        Log.d(TAG, "am in side for pre registration");
                        int index = ((AncSmartRegisterActivity) appContext).getFormIndex("pregnant_mothers_registration");
                        AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) ((AncSmartRegisterActivity) appContext).getDisplayFormFragmentAtIndex(index);

                            displayFormFragment.setMotherDetails(motherPersonList.get(getAdapterPosition()));
                        ((AncSmartRegisterActivity) appContext).switchToPage(1);


                    }else{
                        Log.d(TAG, "am in side for details");

                                Intent intent =new Intent(appContext, AncDetailActivityAlt.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("mom", gsonMom);
                                intent.putExtra("id", mother.getId());
                        appContext.startActivity(intent);
                    }


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
        // todo get item form list
        MotherPersonObject motherPersonObject = motherPersonList.get(position);
        PregnantMom mom = gson.fromJson(convertStandardJSONString(motherPersonObject.getDetails()), PregnantMom.class);

        holder.textName.setText(mom.getName());
        holder.textEDD.setText(motherPersonObject.getEXPECTED_DELIVERY_DATE());
        holder.textPhysicalAddress.setText(mom.getPhysicalAddress());
        holder.textAge.setText("Miaka");
        holder.textAge.append(" " + mom.getAge());
        //  Glide.with(appContext).load("photoUrl").into(holder.imageProfilePic);

    }

    @Override
    public int getItemCount() {
        //  int itemCount = (int) commonRepository.count();
        int itemCount = motherPersonList.size();
        Log.d(TAG, "item count = " + itemCount);
        return itemCount;
    }
}
