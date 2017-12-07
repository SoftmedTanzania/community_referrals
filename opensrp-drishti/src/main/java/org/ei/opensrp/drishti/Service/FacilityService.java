package org.ei.opensrp.drishti.Service;

import android.content.ContentValues;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Facility;
import org.ei.opensrp.drishti.Repository.FacilityObject;
import org.ei.opensrp.repository.FacilityRepository;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

import java.util.ArrayList;


/**
 * Created by Kency Shaka on 3/12/17.
 */
public class FacilityService {
    private Context context;
    private static final String TAG = FacilityService.class.getSimpleName();
    private LockingBackgroundTask lockingBackgroundTask;
    private CommonRepository commonRepository;

    ArrayList<Facility> referralList;
    public FacilityService(CommonRepository serviceRepository) {
        this.commonRepository = serviceRepository;
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
                Facility facility = new Facility();

                referralList = facility.createFacilityList();
                int size = referralList.size();
                for(int i=0; size < i; i++){
                    setFacility(referralList.get(i));
                }
                Log.logDebug("referral service is set in the database");
                return service;
            }

            @Override
            public void postExecuteInUIThread(Object result) {

            }
        });
    }


    public void setFacility(Facility facility){

        ContentValues values = new FacilityRepository().createValuesFor(facility);
        android.util.Log.d(TAG, "values = " + new Gson().toJson(values));

        CommonRepository commonRepository = context.commonrepository("facility");
        commonRepository.customInsert(values);
    }


}
