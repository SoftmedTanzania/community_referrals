package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.PregnantMom;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class MotherPersonObject {
    private String caseId, type, relationalId, MOTHERS_FIRST_NAME, MOTHERS_LAST_NAME, MOTHERS_SORTVALUE,
            EXPECTED_DELIVERY_DATE, MOTHERS_LAST_MENSTRUATION_DATE, FACILITY_ID, IS_PNC, IS_VALID, PNC_STATUS, MOTHERS_ID;
    private String details;
    private Map<String, String> columnMap;

    public MotherPersonObject(String caseId, String relationalId, String MOTHERS_FIRST_NAME, String MOTHERS_LAST_NAME, String MOTHERS_ID, String MOTHERS_SORTVALUE, String EXPECTED_DELIVERY_DATE,
                              String MOTHERS_LAST_MENSTRUATION_DATE, String FACILITY_ID, String IS_PNC, String IS_VALID, String details, String type) {
        this.details = details;
        this.caseId = caseId;
        this.type = type;
        this.relationalId = relationalId;
        this.MOTHERS_FIRST_NAME = MOTHERS_FIRST_NAME;
        this.MOTHERS_LAST_NAME = MOTHERS_LAST_NAME;
        this.MOTHERS_ID = MOTHERS_ID;
        this.MOTHERS_SORTVALUE = MOTHERS_SORTVALUE;
        this.MOTHERS_LAST_MENSTRUATION_DATE = MOTHERS_LAST_MENSTRUATION_DATE;
        this.EXPECTED_DELIVERY_DATE = EXPECTED_DELIVERY_DATE;
        this.FACILITY_ID = FACILITY_ID;
        this.IS_PNC = IS_PNC;
        this.IS_VALID = IS_VALID;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything
    public MotherPersonObject(String caseId, String relationalId, PregnantMom pregnantMom) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.caseId = caseId;
        this.relationalId = relationalId;
        String[] names = pregnantMom.getName().split(" ");
        this.MOTHERS_FIRST_NAME = names[0];
        if (names.length > 1)
            this.MOTHERS_LAST_NAME = names[1];

        this.MOTHERS_ID = pregnantMom.getId();
        this.MOTHERS_LAST_MENSTRUATION_DATE = sdf.format(pregnantMom.getDateLNMP());
        this.EXPECTED_DELIVERY_DATE = sdf.format(pregnantMom.getEdd());

        this.MOTHERS_SORTVALUE = "";
        this.type = "1";
        this.IS_PNC = "false";
        this.IS_VALID = "true";
        this.FACILITY_ID = pregnantMom.getFacilityId();
        this.details = new Gson().toJson(pregnantMom);

    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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

    public String getMOTHERS_FIRST_NAME() {
        return MOTHERS_FIRST_NAME;
    }

    public void setMOTHERS_FIRST_NAME(String MOTHERS_FIRST_NAME) {
        this.MOTHERS_FIRST_NAME = MOTHERS_FIRST_NAME;
    }

    public String getMOTHERS_LAST_NAME() {
        return MOTHERS_LAST_NAME;
    }

    public void setMOTHERS_LAST_NAME(String MOTHERS_LAST_NAME) {
        this.MOTHERS_LAST_NAME = MOTHERS_LAST_NAME;
    }

    public String getMOTHERS_SORTVALUE() {
        return MOTHERS_SORTVALUE;
    }

    public void setMOTHERS_SORTVALUE(String MOTHERS_SORTVALUE) {
        this.MOTHERS_SORTVALUE = MOTHERS_SORTVALUE;
    }

    public String getEXPECTED_DELIVERY_DATE() {
        return EXPECTED_DELIVERY_DATE;
    }

    public void setEXPECTED_DELIVERY_DATE(String EXPECTED_DELIVERY_DATE) {
        this.EXPECTED_DELIVERY_DATE = EXPECTED_DELIVERY_DATE;
    }

    public String getMOTHERS_LAST_MENSTRUATION_DATE() {
        return MOTHERS_LAST_MENSTRUATION_DATE;
    }

    public void setMOTHERS_LAST_MENSTRUATION_DATE(String MOTHERS_LAST_MENSTRUATION_DATE) {
        this.MOTHERS_LAST_MENSTRUATION_DATE = MOTHERS_LAST_MENSTRUATION_DATE;
    }

    public String getFACILITY_ID() {
        return FACILITY_ID;
    }

    public void setFACILITY_ID(String FACILITY_ID) {
        this.FACILITY_ID = FACILITY_ID;
    }

    public String getIS_PNC() {
        return IS_PNC;
    }

    public void setIS_PNC(String IS_PNC) {
        this.IS_PNC = IS_PNC;
    }

    public String getIS_VALID() {
        return IS_VALID;
    }

    public void setIS_VALID(String IS_VALID) {
        this.IS_VALID = IS_VALID;
    }

    public String getPNC_STATUS() {
        return PNC_STATUS;
    }

    public void setPNC_STATUS(String PNC_STATUS) {
        this.PNC_STATUS = PNC_STATUS;
    }

    public String getMOTHERS_ID() {
        return MOTHERS_ID;
    }

    public void setMOTHERS_ID(String MOTHERS_ID) {
        this.MOTHERS_ID = MOTHERS_ID;
    }

    public Map<String, String> getcolumnMap() {
        return columnMap;
    }

    public void setcolumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }
}
