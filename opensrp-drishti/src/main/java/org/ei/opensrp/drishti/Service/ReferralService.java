package org.ei.opensrp.drishti.Service;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.DristhiConfiguration;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ReferralServiceDataModel;
import org.ei.opensrp.repository.FacilityRepository;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.service.HTTPAgent;
import org.ei.opensrp.service.UserService;
import org.ei.opensrp.util.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kency shaka on 03/12/2017.
 */

public class ReferralService {
    private static final String TAG = UserService.class.getSimpleName();
    private ReferralServiceRepository repository;
    private HTTPAgent httpAgent;
    private Context context;
    private Session session;
    private DristhiConfiguration configuration;
    private SaveReferralServiceTask serviceReferralServiceTask;
    private ReferralServiceDataModel referralServiceDataModel;
    ArrayList<ReferralServiceDataModel> referralList;

    public ReferralService(ReferralServiceRepository repository,  HTTPAgent httpAgent, Session session,
                       DristhiConfiguration configuration, SaveReferralServiceTask serviceReferralServiceTask) {
        this.repository = repository;
        this.httpAgent = httpAgent;
        this.session = session;
        this.configuration = configuration;
        this.serviceReferralServiceTask = serviceReferralServiceTask;
    }


    public ReferralService(){
        referralServiceDataModel = new ReferralServiceDataModel();

        referralList = referralServiceDataModel.createReferralList();
        int size = referralList.size();
        for(int i=0; size < i; i++){
            setReferralService(referralList.get(i));
        }
    }

    public void fetchingServiceData( String serviceInfo) {
        saveReferralService(getReferralService(serviceInfo));

    }
    public String getReferralService(String referralService) {
        try {
            JSONObject serviceInfoJson = new JSONObject(referralService);
            return serviceInfoJson.getString("referral_service");
        } catch (JSONException e) {
            Log.v("Error : ", e.getMessage());
            return null;
        }
    }

    public void saveReferralService(String referralService) {
        serviceReferralServiceTask.save(referralService);
    }

    public void setReferralService(ReferralServiceDataModel referralService){

        ContentValues values = new ReferralServiceRepository().createValuesFor(referralService);
        android.util.Log.d(TAG, "values = " + new Gson().toJson(values));

        CommonRepository commonRepository = context.commonrepository("facility");
        commonRepository.customInsert(values);
    }




}
