package org.ei.opensrp.drishti.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.domain.Facility;
import org.ei.opensrp.drishti.Application.BoreshaAfyaApplication;
import org.ei.opensrp.repository.FacilityRepository;
import org.ei.opensrp.view.LockingBackgroundTask;

import java.util.ArrayList;


/**
 * Created by Kency Shaka on 3/12/17.
 */
public class FacilityService extends IntentService {
    private static final String TAG = FacilityService.class.getSimpleName();

    public FacilityService() {
        super("FacilityService");
    }
    public FacilityService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"Facility Service on handle intent");
        ((BoreshaAfyaApplication)getApplication()).getFacilities();
    }
}
