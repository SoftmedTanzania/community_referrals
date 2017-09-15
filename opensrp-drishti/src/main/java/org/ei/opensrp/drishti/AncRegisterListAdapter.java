package org.ei.opensrp.drishti;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.cursoradapter.SmartRegisterCLientsProviderForCursorAdapter;
import org.ei.opensrp.drishti.Repository.CustomMotherRepository;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 9/13/17.
 */

public class AncRegisterListAdapter extends RecyclerView.Adapter {
    //    private final SmartRegisterCLientsProviderForCursorAdapter listItemProvider;
    private Context context;
    private Cursor cursor;
    private CommonRepository customMotherRepository;
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

    public AncRegisterListAdapter(Context context, CommonRepository customMotherRepository, Cursor cursor) {
        this.context = context;
        this.customMotherRepository = customMotherRepository;
        this.motherPersonList = Utils.convertToMotherPersonObjectList(customMotherRepository.readAllcommonForField(cursor, TABLE_NAME));

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
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.smart_register_mcare_anc_client_alt, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        CommonPersonObject personinlist = commonRepository.readAllcommonforCursorAdapter(cursor);
//        CommonPersonObjectClient pClient = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
//        pClient.setColumnmaps(personinlist.getColumnmaps());
        // listItemProvider.getView(pClient, view);

        // todo get item form list

    }

    @Override
    public int getItemCount() {
        //  int itemCount = (int) customMotherRepository.count();
        int itemCount = motherPersonList.size();
        Log.d(TAG, "item count = " + itemCount);
        return itemCount;
    }
}
