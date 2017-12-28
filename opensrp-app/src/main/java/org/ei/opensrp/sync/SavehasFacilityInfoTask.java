package org.ei.opensrp.sync;

import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class SavehasFacilityInfoTask {

    private LockingBackgroundTask lockingBackgroundTask;
    private AllSettings allSettings;

    public SavehasFacilityInfoTask(AllSettings allSettings) {
        this.allSettings = allSettings;
        lockingBackgroundTask = new LockingBackgroundTask(new ProgressIndicator() {
            @Override
            public void setVisible() {
            }

            @Override
            public void setInvisible() {
                Log.logInfo("Successfully saved has Facility boolean value");
            }
        });
    }

    public void save(final String value) {
        lockingBackgroundTask.doActionInBackground(new BackgroundAction<Object>() {
            @Override
            public Object actionToDoInBackgroundThread() {
                allSettings.savehasFacility(value);
                return value;
            }

            @Override
            public void postExecuteInUIThread(Object result) {

            }
        });
    }
}
