package org.ei.opensrp.drishti.Service;

import android.util.Log;

import org.ei.opensrp.DristhiConfiguration;
import org.ei.opensrp.repository.AllSettings;
import org.ei.opensrp.repository.AllSharedPreferences;
import org.ei.opensrp.repository.Repository;
import org.ei.opensrp.service.HTTPAgent;
import org.ei.opensrp.service.UserService;
import org.ei.opensrp.util.Session;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kency shaka on 03/12/2017.
 */

public class ReferralService {
    private static final String TAG = UserService.class.getSimpleName();
    private final Repository repository;
    private final AllSettings allSettings;
    private final AllSharedPreferences allSharedPreferences;
    private HTTPAgent httpAgent;
    private Session session;
    private DristhiConfiguration configuration;
    private SaveReferralServiceTask serviceReferralServiceTask;

    public ReferralService(Repository repository, AllSettings allSettings, AllSharedPreferences allSharedPreferences, HTTPAgent httpAgent, Session session,
                       DristhiConfiguration configuration, SaveReferralServiceTask serviceReferralServiceTask) {
        this.repository = repository;
        this.allSettings = allSettings;
        this.allSharedPreferences = allSharedPreferences;
        this.httpAgent = httpAgent;
        this.session = session;
        this.configuration = configuration;
        this.serviceReferralServiceTask = serviceReferralServiceTask;
    }

    public void fetchingServiceData( String serviceInfo) {
        saveReferralService(getReferralService(serviceInfo));

    }
    public String getReferralService(String referralService) {
        try {
            JSONObject serviceInfoJson = new JSONObject(referralService);
            return serviceInfoJson.getString("referral_service");
        } catch (JSONException e) {
            Log.v("Error : ", e.getMessage());
            return null;
        }
    }

    public void saveReferralService(String referralService) {
        serviceReferralServiceTask.save(referralService);
    }



}
