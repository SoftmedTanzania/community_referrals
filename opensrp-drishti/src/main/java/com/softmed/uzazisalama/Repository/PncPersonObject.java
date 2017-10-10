package com.softmed.uzazisalama.Repository;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.PncMother;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class PncPersonObject {
    private String id,childCaseId, motherCaseId, DELIVERY_TYPE, relationalId, DELIVERY_COMPLICATION, DELIVERY_DATE, ADMISSION_DATE,
            CreatedBy, ModifyBy;
    private String details;
    private Map<String, String> columnMap;

    public PncPersonObject(String id,
                           String relationalId,
                           String childCaseId,
                           String motherCaseId,
                           String DELIVERY_TYPE,
                           String DELIVERY_COMPLICATION,
                           String DELIVERY_DATE,
                           String ADMISSION_DATE,
                           String details,
                           String CreatedBy,
                           String ModifyBy) {
        this.details = details;
        this.id = id;
        this.childCaseId = childCaseId;
        this.relationalId = relationalId;
        this.DELIVERY_TYPE = DELIVERY_TYPE;
        this.DELIVERY_COMPLICATION = DELIVERY_COMPLICATION;
        this.motherCaseId = motherCaseId;
        this.DELIVERY_DATE = DELIVERY_DATE;
        this.ADMISSION_DATE = ADMISSION_DATE;
        this.CreatedBy = CreatedBy;
        this.ModifyBy = ModifyBy;
    }

    // alternative constructor so you don't pass bucha stuff, pncMother contains everything
    public PncPersonObject(String id, String childCaseId, String motherCaseId,PncMother pncMother) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = id;
        this.motherCaseId = motherCaseId;
        this.childCaseId = childCaseId;
        this.DELIVERY_TYPE = pncMother.getDeliveryType();
        this.DELIVERY_COMPLICATION = pncMother.getDeliveryComplication();
        this.DELIVERY_DATE = sdf.format(pncMother.getDeliveryDate());
        this.ADMISSION_DATE = sdf.format(pncMother.getAdmissionDate());
        this.details = new Gson().toJson(pncMother);
        this.CreatedBy = pncMother.getCreatedBy();
        this.ModifyBy = pncMother.getModifyBy();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public String getChildCaseId() {
        return childCaseId;
    }

    public void setChildCaseId(String childCaseId) {
        this.childCaseId = childCaseId;
    }

    public String getMotherCaseId() {
        return motherCaseId;
    }

    public void setMotherCaseId(String motherCaseId) {
        this.motherCaseId = motherCaseId;
    }

    public String getDELIVERY_TYPE() {
        return DELIVERY_TYPE;
    }

    public void setDELIVERY_TYPE(String DELIVERY_TYPE) {
        this.DELIVERY_TYPE = DELIVERY_TYPE;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getDELIVERY_COMPLICATION() {
        return DELIVERY_COMPLICATION;
    }

    public void setDELIVERY_COMPLICATION(String DELIVERY_COMPLICATION) {
        this.DELIVERY_COMPLICATION = DELIVERY_COMPLICATION;
    }

    public String getDELIVERY_DATE() {
        return DELIVERY_DATE;
    }

    public void setDELIVERY_DATE(String DELIVERY_DATE) {
        this.DELIVERY_DATE = DELIVERY_DATE;
    }

    public String getADMISSION_DATE() {
        return ADMISSION_DATE;
    }

    public void setADMISSION_DATE(String ADMISSION_DATE) {
        this.ADMISSION_DATE = ADMISSION_DATE;
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

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }
}
