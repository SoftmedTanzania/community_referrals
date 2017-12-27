package org.ei.opensrp.drishti.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.ReferralServiceDataModel;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;
import org.ei.opensrp.repository.ReferralServiceRepository;
import org.ei.opensrp.util.Log;
import org.ei.opensrp.view.BackgroundAction;
import org.ei.opensrp.view.LockingBackgroundTask;
import org.ei.opensrp.view.ProgressIndicator;

import java.util.ArrayList;

/**
 * Created by Dimas Ciputra on 3/24/15.
 */
public class ReferralService extends IntentService {
    private static final String TAG = ReferralService.class.getSimpleName();

    public ReferralService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        android.util.Log.d(TAG,"Facility Referral Services on handle intent");
        ((BoreshaAfyaApplication)getApplication()).setReferralService();
    }
}
