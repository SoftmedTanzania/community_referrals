package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;
import org.ei.opensrp.drishti.DataModels.Child;
import org.ei.opensrp.drishti.DataModels.PregnantMom;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class ChildPersonObject {
    private String id,GENDER, STATUS, relationalId, WEIGHT, PROBLEM, CreatedBy, ModifyBy;
    private String details;
    private Map<String, String> columnMap;

    public ChildPersonObject(String id,
                             String relationalId,
                             String GENDER,
                             String STATUS,
                             String WEIGHT,
                             String PROBLEM,
                             String details,
                             String CreatedBy,
                             String ModifyBy) {
        this.details = details;
        this.id = id;
        this.GENDER = GENDER;
        this.relationalId = relationalId;
        this.STATUS = STATUS;
        this.WEIGHT = WEIGHT;
        this.PROBLEM = PROBLEM;
        this.CreatedBy = CreatedBy;
        this.ModifyBy = ModifyBy;
    }

    public ChildPersonObject(String id, String relationalId, Child child) {

        this.id = id;
        this.relationalId = relationalId;
        this.GENDER = child.getGender();
        this.STATUS = child.getStatus();
        this.WEIGHT = child.getWeight();
        this.PROBLEM = child.getProblem();
        this.details = new Gson().toJson(child);
        this.CreatedBy = child.getCreatedBy();
        this.ModifyBy = child.getModifyBy();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(String WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getPROBLEM() {
        return PROBLEM;
    }

    public void setPROBLEM(String PROBLEM) {
        this.PROBLEM = PROBLEM;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifyBy() {
        return ModifyBy;
    }

    public void setModifyBy(String modifyBy) {
        ModifyBy = modifyBy;
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
}
