package org.ei.opensrp.domain;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class ReferralServiceDataModel {

    private static final String TAG = ReferralServiceDataModel.class.getSimpleName();
    private String serviceName, id;

    public ReferralServiceDataModel() {

    }

    public ReferralServiceDataModel(String id, String name) {
        this.id = id;
        this.serviceName = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return serviceName;
    }

    public void setName(String name) {
        this.serviceName = name;
    }

    public static ArrayList<ReferralServiceDataModel> createReferralList() {
        ArrayList<ReferralServiceDataModel> visitedMom = new ArrayList<ReferralServiceDataModel>();


        visitedMom.add(new ReferralServiceDataModel("0001","Ushauri nasaha na kupima"));
        visitedMom.add(new ReferralServiceDataModel("0002","Rufaa kwenda kliniki ya TB na Matunzo (CTC) B"));
        visitedMom.add(new ReferralServiceDataModel("0003","Rufaa kwenda kituo cha kutoa huduma za afya kutokana na magonjwa nyemelezi"));
        visitedMom.add(new ReferralServiceDataModel("0004","Kliniki ya kutibu kifua kikuu"));
        visitedMom.add(new ReferralServiceDataModel("0005","Rufaa kwenda kliniki ya kutibu Malaria"));
        visitedMom.add(new ReferralServiceDataModel("0006","Huduma za kuzuia maambukizi toka kwa mama kwenda mtoto"));
        visitedMom.add(new ReferralServiceDataModel("0007","Huduma ya afya ya uzazi na mtoto (RCH)"));
        visitedMom.add(new ReferralServiceDataModel("0008","Huduma ya Tohara (VMMC)"));
        visitedMom.add(new ReferralServiceDataModel("0009","Huduma za kuzuia ukatili wa kijinsia(Dawati la jinsia)"));
        visitedMom.add(new ReferralServiceDataModel("0010","Huduma za kuzuia maambukizi toka kwa mama kwenda mtoto"));


        Log.d(TAG,"service list ="+visitedMom.toString());
        return visitedMom;
    }
}
