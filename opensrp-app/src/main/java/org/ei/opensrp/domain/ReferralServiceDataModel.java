package org.ei.opensrp.domain;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martha on 1/1/18.
 */

public class ReferralServiceDataModel {

    private static final String TAG = ReferralServiceDataModel.class.getSimpleName();
    private String serviceName, id,relationalid,category,isActive;

    private Map<String, String> details;

    public ReferralServiceDataModel() {

    }

    public ReferralServiceDataModel(String id, String name,String isActive,String category) {
        this.id = id;
        this.serviceName = name;
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
