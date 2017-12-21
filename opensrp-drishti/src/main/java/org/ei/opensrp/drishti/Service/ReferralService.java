package org.ei.opensrp.drishti.Service;

import android.content.ContentValues;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ReferralServiceDataModel;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

import java.util.ArrayList;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class ReferralService {

    private Context context;
    private static final String TAG = ReferralService.class.getSimpleName();
    private LockingBackgroundTask lockingBackgroundTask;
    private CommonRepository repository;

    private ReferralServiceDataModel referralServiceDataModel;
    ArrayList<ReferralServiceDataModel> referralList;
    public ReferralService(CommonRepository serviceRepository) {
        this.repository = serviceRepository;
        lockingBackgroundTask = new LockingBackgroundTask(new ProgressIndicator() {
            @Override
            public void setVisible() {
            }

            @Override
            public void setInvisible() {
                Log.logInfo("Successfully saved referral service information");
            }
        });
    }

    public void save(final String service) {
        lockingBackgroundTask.doActionInBackground(new BackgroundAction<Object>() {
            @Override
            public Object actionToDoInBackgroundThread() {
                referralServiceDataModel = new ReferralServiceDataModel();

                referralList = referralServiceDataModel.createReferralList();
                int size = referralList.size();
                for(int i=0; size < i; i++){
                    setReferralService(referralList.get(i));
                }
                Log.logDebug("referral service is set in the database");
                return service;
            }

            @Override
            public void postExecuteInUIThread(Object result) {

            }
        });
    }

    public void setReferralService(ReferralServiceDataModel referralService){

        ContentValues values = new ReferralServiceRepository().createValuesFor(referralService);
        android.util.Log.d(TAG, "values = " + new Gson().toJson(values));

        CommonRepository commonRepository = context.commonrepository("referral_service");
        commonRepository.customInsert(values);
    }
}
