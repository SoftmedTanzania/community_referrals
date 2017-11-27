package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.Facility;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class FacilityObject {
    private String id, relationalId, Name;

    private String details;
    private Map<String, String> columnMap;

    public FacilityObject(String id,
                          String relationalId,
                          String name,
                          String details
                                      ) {
        this.details = details;
        this.id = id;
        this.Name = name;
        this.relationalId = relationalId;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything

    public FacilityObject(String id, String relationalId, Facility facility) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = relationalId;
        this.Name = facility.getName();
        this.details = new Gson().toJson(facility);

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

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    }