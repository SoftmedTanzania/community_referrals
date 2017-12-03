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
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.DataModels.PregnantMom;
import org.ei.opensrp.drishti.Fragments.AncRegisterFormFragment;
import org.ei.opensrp.drishti.Repository.ClientReferralPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.ei.opensrp.drishti.util.Utils.convertStandardJSONString;

/**
 * Created by ali on 9/13/17.
 */

public class AncRegisterListAdapter extends RecyclerView.Adapter<AncRegisterListAdapter.ViewHolder> {
    //    private final SmartRegisterCLientsProviderForCursorAdapter listItemProvider;
    private Context context;
    private Cursor cursor;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<ClientReferralPersonObject> clientReferralPersonObjectList = new ArrayList<>();

    private static final String TAG = AncRegisterListAdapter.class.getSimpleName(),
            TABLE_NAME = "client_referral";


    public AncRegisterListAdapter(Context context, CommonRepository commonRepository, Cursor cursor, android.content.Context appContext) {
        this.context = context;
        this.appContext = appContext;
        List<CommonPersonObject> commonPersonObjectList = commonRepository.readAllcommonForField(cursor, TABLE_NAME);
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        this.clientReferralPersonObjectList = Utils.convertToClientReferralPersonObjectList(commonPersonObjectList);

        Log.d(TAG, "mother list = " + gson.toJson(clientReferralPersonObjectList));

        Log.d(TAG, "repo count = " + commonRepository.count() + ", list count = " + clientReferralPersonObjectList.size());

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

                    String gsonMom = Utils.convertStandardJSONString(clientReferralPersonObjectList.get(getAdapterPosition()).getDetails());
                    final PregnantMom pregnantMom = new Gson().fromJson(gsonMom,PregnantMom.class);
                    Log.d(TAG, "commonPersonList type= " + pregnantMom.getReg_type());
                    if(pregnantMom.getReg_type().equals("2")){
                        Log.d(TAG, "am in side for pre registration");
                        int index = ((AncSmartRegisterActivity) appContext).getFormIndex("pregnant_mothers_registration");
                        AncRegisterFormFragment displayFormFragment = (AncRegisterFormFragment) ((AncSmartRegisterActivity) appContext).getDisplayFormFragmentAtIndex(index);

//                            displayFormFragment.setMotherDetails(clientReferralPersonObjectList.get(getAdapterPosition()));
                        ((AncSmartRegisterActivity) appContext).switchToPage(1);


                    }else{
                        Log.d(TAG, "am in side for details");
                        appContext.startActivity(new Intent(appContext, AncDetailActivityAlt.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("mom", gsonMom));
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
        ClientReferralPersonObject clientReferralPersonObject = clientReferralPersonObjectList.get(position);
        ClientReferral clientReferral = gson.fromJson(convertStandardJSONString(clientReferralPersonObject.getDetails()), ClientReferral.class);

        holder.textName.setText(clientReferral.getFirst_name());
        holder.textEDD.setText(clientReferral.getReferral_date());
        holder.textPhysicalAddress.setText(clientReferral.getWard());

        //  Glide.with(appContext).load("photoUrl").into(holder.imageProfilePic);

    }

    @Override
    public int getItemCount() {
        //  int itemCount = (int) commonRepository.count();
        int itemCount = clientReferralPersonObjectList.size();
        Log.d(TAG, "item count = " + itemCount);
        return itemCount;
    }
}
