package org.ei.opensrp.domain;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class Facility  {
    private String facilityName, openMRSUIID;

    public Facility(String facilityName, String openMRSUIID) {
        this.facilityName = facilityName;
        this.openMRSUIID = openMRSUIID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getOpenMRSUIID() {
        return openMRSUIID;
    }

    public void setOpenMRSUIID(String openMRSUIID) {
        this.openMRSUIID = openMRSUIID;
    }
}
