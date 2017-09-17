package org.ei.opensrp.drishti.DataModels;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import org.ei.opensrp.Context;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.drishti.Repository.MotherPersonObject;
import org.ei.opensrp.drishti.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 9/5/17.
 */

public class PreRegisteredMother {
    private String mName, EDD, visited,risk;
    private Context context;
    private Cursor cursor;
    private Gson gson = new Gson();
    private android.content.Context appContext;
    private List<MotherPersonObject> motherPersonList = new ArrayList<>();

    public PreRegisteredMother(String name,String eDD,String eVisited,String eRisk ) {
        mName = name;
        EDD = eDD;
        visited = eVisited;
        risk = eRisk;
    }

    public String getName() {
        return mName;
    }

    public String getEdd() {
        return EDD;
    }
    public String getVisited() {
        return visited;
    }
    public String getRisk() {
        return risk;
    }
    private static int lastPreRegisteredMotherId = 0;

    public static ArrayList<PreRegisteredMother> createPreRegisteredMotherList() {
        ArrayList<PreRegisteredMother> visitedMom = new ArrayList<PreRegisteredMother>();


        visitedMom.add(new PreRegisteredMother("Martha Shaka","2 Jan 17","twice" ,"high"));
        visitedMom.add(new PreRegisteredMother("Janeth Lagai","2 Mar 16","once" ,"moderate"));


        return visitedMom;
    }
}
