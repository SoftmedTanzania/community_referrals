package org.ei.opensrp.domain;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class ReferralServiceDataModel {

    private static final String TAG = ReferralServiceDataModel.class.getSimpleName();
    private String serviceName, serviceId;

    public ReferralServiceDataModel() {

    }

    public ReferralServiceDataModel(String serviceId, String name) {
        this.serviceId = serviceId;
        this.serviceName = name;

    }

    public String getId() {
        return serviceId;
    }

    public void setId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return serviceName;
    }

    public void setName(String name) {
        this.serviceName = name;
    }

}
