package com.softmed.htmr_chw.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.domain.Facility;

import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class ReferralServiceObject {
    private String id, relationalId, name,nameSw,category;

    private String details;
    private Map<String, String> columnMap;


    public ReferralServiceObject(String id,
                                     String name,String name_sw,String category){

            this.id = id;
            this.name = name;
            this.nameSw = name_sw;
            this.category = category;
        }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything

    public ReferralServiceObject(String id, String relationalId, Facility facility) {

        this.id = id;
        this.relationalId = relationalId;
        this.name = facility.getFacilityName();
        this.details = new Gson().toJson(facility);

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public String getNameSw() {
        return nameSw;
    }

    public void setNameSw(String nameSw) {
        this.nameSw = nameSw;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    }