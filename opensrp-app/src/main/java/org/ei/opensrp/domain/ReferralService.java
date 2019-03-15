package org.ei.opensrp.domain;

import java.util.Map;

/**
 * Created by Martha on 1/1/18.
 */

public class ReferralService {

    private static final String TAG = ReferralService.class.getSimpleName();
    private String serviceName,serviceNameSw, id,relationalid,category,isActive;

    private Map<String, String> details;

    public ReferralService() {

    }

    public ReferralService(String id, String serviceName, String serviceNameSw, String category, String isActive) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceNameSw = serviceNameSw;
        this.details = null;
        this.relationalid = id;
        this.isActive = isActive;
        this.category = category;

    }



    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServiceNameSw() {
        return serviceNameSw;
    }

    public void setServiceNameSw(String serviceNameSw) {
        this.serviceNameSw = serviceNameSw;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

}
