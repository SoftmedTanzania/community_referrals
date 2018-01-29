package org.ei.opensrp.domain;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class Facility  {
    private String facilityName, id,relationalid,details;



    public Facility(String facilityName, String openMRSUIID) {
        this.facilityName = facilityName;
        this.id = openMRSUIID;
        this.relationalid = openMRSUIID;
        this.details =null;

    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getOpenMRSUIID() {
        return id;
    }

    public void setOpenMRSUIID(String openMRSUIID) {
        this.facilityName = openMRSUIID;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
