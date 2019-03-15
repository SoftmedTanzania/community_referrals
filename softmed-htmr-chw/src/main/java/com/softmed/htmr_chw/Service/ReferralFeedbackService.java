package com.softmed.htmr_chw.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.softmed.htmr_chw.Application.BoreshaAfyaApplication;


/**
 * Created by Coze  on 03/15/19.
 */
public class ReferralFeedbackService extends IntentService {
    private static final String TAG = ReferralFeedbackService.class.getSimpleName();

    public ReferralFeedbackService() {
        super("ReferralFeedbackService");
    }

    public ReferralFeedbackService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        android.util.Log.d(TAG, "Facility Referral Feedback on handle intent");
        ((BoreshaAfyaApplication) getApplication()).setReferralFeedback();
    }
}
