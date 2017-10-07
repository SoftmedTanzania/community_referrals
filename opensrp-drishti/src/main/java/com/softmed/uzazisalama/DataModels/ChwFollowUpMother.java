package com.softmed.uzazisalama.DataModels;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class ChwFollowUpMother {
    private String mName, EDD, risk, uniqueID, village, age, number, facility, anc1,anc2,anc3,anc4;

    public ChwFollowUpMother(String name, String eDD,String eRisk,String eUnique, String eVillage, String eAge,String eNumber, String eFacility, String eAnc1,String eAnc2, String eAnc3,String eAnc4) {
        mName = name;
        EDD = eDD;
        risk = eRisk;
        uniqueID =eUnique;
        village = eVillage;
        age = eAge;
        number = eNumber;
        facility = eFacility;
        anc1 = eAnc1;
        anc2 = eAnc2;
        anc3 = eAnc3;
        anc4 = eAnc4;
    }

    public String getName() {
        return mName;
    }

    public String getEdd() {
        return EDD;
    }
    public String getRisk() {
        return risk;
    }
    public String getAge() {  return age;    }
    public String getNumber() {
        return number;
    }
    public String getVillage() {
        return village;
    }
    public String getFacility() {
        return facility;
    }
    public String getUniqueID() { return uniqueID;
    }
    public String getAnc1() {
        return anc1;
    }
    public String getAnc2() { return anc2; }
    public String getAnc3() { return anc3; }
    public String getAnc4() { return anc4; }


    public static ArrayList<ChwFollowUpMother> createFollowUpMotherList() {
        ArrayList<ChwFollowUpMother> visitedMom = new ArrayList<ChwFollowUpMother>();


        visitedMom.add(new ChwFollowUpMother("Martha Shaka","2 Jan 17" ,"high","MA 1-1-1","Kwembe","28 years","0712763363","Mt meru","30 Jul","3 Jan","1 Sep","11 NOv"));
        visitedMom.add(new ChwFollowUpMother("Janeth Lagai","2 Mar 16" ,"moderate","MA-1-1-","Kanisa Road","28 years","0712763363","Ngaramtoni","30 Jul","3 Jan","1 Sep","11 NOv"));
        visitedMom.add(new ChwFollowUpMother("Pili Juma","22 Mar 16" ,"low","MA 1-1-1","Kwembe","28 years","0712763363","Kaloleni","30 Jul","3 Jan","1 Sep","11 NOv"));


        return visitedMom;
    }
}
