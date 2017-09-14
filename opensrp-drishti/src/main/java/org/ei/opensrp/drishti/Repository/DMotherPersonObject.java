package org.ei.opensrp.drishti.Repository;

import org.apache.commons.codec.StringEncoder;
import org.ei.opensrp.commonregistry.CommonPersonObject;

import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class DMotherPersonObject extends CommonPersonObject {
    private String caseId,type,relationalid,MOTHERS_FIRST_NAME,MOTHERS_LAST_NAME,MOTHERS_SORTVALUE,
            EXPECTED_DELIVERY_DATE,MOTHERS_LAST_MENSTRUATION_DATE,FACILITY_ID,IS_PNC,IS_VALID,PNC_STATUS,MOTHERS_ID;
    private Map<String, String> details,columnmaps;

    public DMotherPersonObject(String caseId, String relationalid, String MOTHERS_FIRST_NAME, String MOTHERS_LAST_NAME, String MOTHERS_ID, String MOTHERS_SORTVALUE, String EXPECTED_DELIVERY_DATE,
                               String MOTHERS_LAST_MENSTRUATION_DATE, String FACILITY_ID, String IS_PNC, String IS_VALID, Map<String, String> details, String type) {
        super(caseId, relationalid, details, type);
        this.details = details;
        this.caseId = caseId;
        this.type = type;
        this.relationalid = relationalid;
        this.MOTHERS_FIRST_NAME = MOTHERS_FIRST_NAME ;
        this.MOTHERS_LAST_NAME = MOTHERS_LAST_NAME;
        this.MOTHERS_ID = MOTHERS_ID;
        this.MOTHERS_SORTVALUE = MOTHERS_SORTVALUE;
        this.MOTHERS_LAST_MENSTRUATION_DATE = MOTHERS_LAST_MENSTRUATION_DATE;
        this.EXPECTED_DELIVERY_DATE = EXPECTED_DELIVERY_DATE;
        this.FACILITY_ID = FACILITY_ID;
        this.IS_PNC = IS_PNC;
        this.IS_VALID = IS_VALID;

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

}
