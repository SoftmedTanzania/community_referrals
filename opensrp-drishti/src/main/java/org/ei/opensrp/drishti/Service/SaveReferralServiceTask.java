package org.ei.opensrp.drishti.Service;

import org.ei.opensrp.drishti.Repository.ReferralServiceRepository;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class SaveReferralServiceTask {

    private LockingBackgroundTask lockingBackgroundTask;
    private ReferralServiceRepository repository;

    public SaveReferralServiceTask(ReferralServiceRepository serviceRepository) {
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
//                repository.saveTeamInformation(service);
                return service;
            }

            @Override
            public void postExecuteInUIThread(Object result) {

            }
        });
    }
}
