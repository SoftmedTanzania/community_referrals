package com.softmed.htmr_chw_staging.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.softmed.htmr_chw_staging.Application.BoreshaAfyaApplication;


/**
 * Created by Kency  on 12/21/17.
 */
public class ReferralService extends IntentService {
    private static final String TAG = ReferralService.class.getSimpleName();

    public ReferralService() {
        super("ReferralService");
    }

    public ReferralService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        android.util.Log.d(TAG, "Facility Referral Services on handle intent");
        ((BoreshaAfyaApplication)getApplication()).getFacilities();
        ((BoreshaAfyaApplication) getApplication()).setReferralService();
        ((BoreshaAfyaApplication) getApplication()).setReferralFeedback();
        ((BoreshaAfyaApplication) getApplication()).setRegistrationReasons();
    }
}
