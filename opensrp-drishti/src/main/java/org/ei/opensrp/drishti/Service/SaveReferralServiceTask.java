package org.ei.opensrp.drishti.Service;

import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class SaveReferralServiceTask {
    private ReferralService referralService;
    private LockingBackgroundTask lockingBackgroundTask;
    private CommonRepository repository;

    public SaveReferralServiceTask(CommonRepository serviceRepository) {
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
                referralService = new ReferralService();
                Log.logDebug("referral service is set in the database");
                return service;
            }

            @Override
            public void postExecuteInUIThread(Object result) {

            }
        });
    }
}
