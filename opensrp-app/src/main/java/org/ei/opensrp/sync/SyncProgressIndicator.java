package org.ei.opensrp.sync;

import android.util.Log;

import org.ei.opensrp.view.ProgressIndicator;

import static org.ei.opensrp.event.Event.SYNC_COMPLETED;
import static org.ei.opensrp.event.Event.SYNC_STARTED;

public class SyncProgressIndicator implements ProgressIndicator {

    private static final String TAG = SyncProgressIndicator.class.getSimpleName();
    @Override
    public void setVisible() {
        org.ei.opensrp.Context.getInstance().allSharedPreferences().saveIsSyncInProgress(true);
        SYNC_STARTED.notifyListeners(true);
        Log.d(TAG,"it has started");
    }

    @Override
    public void setInvisible() {
        org.ei.opensrp.Context.getInstance().allSharedPreferences().saveIsSyncInProgress(false);
        Log.d(TAG,"it has ended");
        SYNC_COMPLETED.notifyListeners(true);
    }
}
