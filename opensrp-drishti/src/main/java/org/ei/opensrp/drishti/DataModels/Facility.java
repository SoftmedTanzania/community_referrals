package org.ei.opensrp.drishti.DataModels;

import android.database.Cursor;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 9/5/17.
 */

public class Facility {
    private String name;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();

    public Facility(String id, String relationID, String name) {
        name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Facility> createFacilityList() {
        ArrayList<Facility> visitedMom = new ArrayList<Facility>();


        visitedMom.add(new Facility("0001","null","facility A"));
        visitedMom.add(new Facility("0002","null","facility B"));
        visitedMom.add(new Facility("0003","null","facility C"));
        visitedMom.add(new Facility("0004","null","facility D"));


        return visitedMom;
    }
}
