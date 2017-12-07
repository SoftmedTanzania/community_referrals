package org.ei.opensrp.drishti.Service;

import org.ei.opensrp.drishti.DataModels.Facility;
import org.ei.opensrp.drishti.Repository.FacilityRepository;
import org.ei.opensrp.drishti.Repository.ReferralServiceRepository;
import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

import java.util.ArrayList;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class FacilityService {

    private LockingBackgroundTask lockingBackgroundTask;
    private FacilityRepository facilityRepository;

    ArrayList<Facility> referralList;
    public FacilityService(FacilityRepository serviceRepository) {
        this.facilityRepository = serviceRepository;
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

        facilityRepository.add(facility);
    }


}
