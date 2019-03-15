package com.softmed.htmr_chw.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;


/**
 * Created by Coze  on 03/15/19.
 */
public class RegistrationReasonsService extends IntentService {
    private static final String TAG = RegistrationReasonsService.class.getSimpleName();

    public RegistrationReasonsService() {
        super("ReferralService");
    }

    public RegistrationReasonsService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        android.util.Log.d(TAG, "Registration Reasons on handle intent");
        ((BoreshaAfyaApplication) getApplication()).setRegistrationReasons();
    }
}
