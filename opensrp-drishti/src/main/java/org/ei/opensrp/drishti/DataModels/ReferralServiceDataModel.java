package org.ei.opensrp.drishti.DataModels;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class ReferralServiceDataModel {
    private String name, id;

    public ReferralServiceDataModel() {

    }

    public ReferralServiceDataModel(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<ReferralServiceDataModel> createFacilityList() {
        ArrayList<ReferralServiceDataModel> visitedMom = new ArrayList<ReferralServiceDataModel>();


        visitedMom.add(new ReferralServiceDataModel("0001","serviceA"));
        visitedMom.add(new ReferralServiceDataModel("0002","service B"));


        return visitedMom;
    }
}
