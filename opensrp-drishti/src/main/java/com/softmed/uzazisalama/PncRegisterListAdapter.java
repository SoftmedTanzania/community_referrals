package com.softmed.uzazisalama;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.softmed.uzazisalama.Application.UzaziSalamaApplication;
import com.softmed.uzazisalama.DataModels.PncMother;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.drishti.R;

import com.softmed.uzazisalama.Repository.ChildPersonObject;
import com.softmed.uzazisalama.Repository.MotherPersonObject;
import com.softmed.uzazisalama.Repository.PncPersonObject;
import com.softmed.uzazisalama.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.softmed.uzazisalama.util.Utils.convertStandardJSONString;
import static org.ei.opensrp.R.color.smart_register_client_divider_color;


/**
 * Created by martha on 8/22/17.
 */

public class PncRegisterListAdapter extends
        RecyclerView.Adapter<PncRegisterListAdapter.ViewHolder> {

    private List<PncPersonObject> mothers;
    private Context mContext;
    private PncPersonObject mother;
    private CommonRepository mcommonRepository,childcommonRepository;
    private Gson gson = new Gson();

    private android.content.Context appContext;
    private List<PncPersonObject> motherPersonList = new ArrayList<>();

    private static final String TAG = PncRegisterListAdapter.class.getSimpleName();

    public PncRegisterListAdapter(Context context,CommonRepository commonRepository,CommonRepository childcommonRepository ,List<PncPersonObject> mothers,android.content.Context appContext) {
        this.mothers = mothers;
        this.mContext = context;
        this.appContext = appContext;
        this.mcommonRepository = commonRepository;
        this.childcommonRepository = childcommonRepository;
        Log.d(TAG,"am here pnc adapter");
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.pnc_detail_activity, parent, false);
        Log.d(TAG, "am here in pnc" );
        return new ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // todo get item form list
        PncPersonObject pncmotherPersonObject = mothers.get(position);

        Cursor cursor = mcommonRepository.RawCustomQueryForAdapter("select * FROM wazazi_salama_mother where id ='"+pncmotherPersonObject.getMotherCaseId() +"'" );
        List<CommonPersonObject> commonPersonObjectList = mcommonRepository.readAllcommonForField(cursor,"wazazi_salama_mother" );
        Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

        MotherPersonObject motherPersonObject = Utils.convertToMotherPersonObject(commonPersonObjectList.get(0));
        Log.d(TAG, "motherPersonObject = " + gson.toJson(motherPersonObject));

        holder.textName.setText(motherPersonObject.getMOTHERS_FIRST_NAME()+" "+motherPersonObject.getMOTHERS_LAST_NAME());
        holder.textAge.setText("Delivery Date:-");
        holder.textAge.append(" " +pncmotherPersonObject.getDELIVERY_DATE());
        holder.textPhysicalAddress.setText("facility/hospital :-");
        holder.textPhysicalAddress.append(" " + motherPersonObject.getFACILITY_ID());
        //  Glide.with(appContext).load("photoUrl").into(holder.imageProfilePic);

    }

    @Override
    public int getItemCount() {
        return mothers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textAge, textPhysicalAddress, textEDD, textLastVisitDate, textNextVisitDate;
        ImageView imageProfilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textAge = (TextView) itemView.findViewById(R.id.textAge);
            textPhysicalAddress = (TextView) itemView.findViewById(R.id.textPhysicalAddress);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to AncDetailActivity
                    PncPersonObject mother = mothers.get(getAdapterPosition());
                    String gsonPncMom = Utils.convertStandardJSONString(mothers.get(getAdapterPosition()).getDetails());
                    Log.d(TAG, "pnc mother= " + gsonPncMom);

                    //getting the mother details
                    Cursor cursor = mcommonRepository.RawCustomQueryForAdapter("select * FROM wazazi_salama_mother where id ='"+mother.getMotherCaseId() +"'" );
                    List<CommonPersonObject> commonPersonObjectList = mcommonRepository.readAllcommonForField(cursor,"wazazi_salama_mother" );
                    Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList));

                    MotherPersonObject motherPersonObject = Utils.convertToMotherPersonObject(commonPersonObjectList.get(0));
                    String gsonMom = Utils.convertStandardJSONString(motherPersonObject.getDetails());
                    Log.d(TAG, "motherPersonObject = " + gsonMom);

                    //getting the child details
                    Cursor cursor2 = childcommonRepository.RawCustomQueryForAdapter("select * FROM uzazi_salama_child where id ='"+mother.getChildCaseId() +"'" );
                    List<CommonPersonObject> commonPersonObjectList2 = childcommonRepository.readAllcommonForField(cursor2,"uzazi_salama_child" );
                    Log.d(TAG, "commonPersonList = " + gson.toJson(commonPersonObjectList2));

                    ChildPersonObject childPersonObject = Utils.convertToChildPersonObject(commonPersonObjectList2.get(0));
                    String gsonChild = Utils.convertStandardJSONString(childPersonObject.getDetails());
                    Log.d(TAG, "childPersonObject = " + gsonChild);

                    Intent intent =new Intent(appContext, PncDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mom", gsonMom);
                    intent.putExtra("pncMom", gsonPncMom);
                    intent.putExtra("child", gsonChild);
                    appContext.startActivity(intent);



                }
            });
        }

    }

}