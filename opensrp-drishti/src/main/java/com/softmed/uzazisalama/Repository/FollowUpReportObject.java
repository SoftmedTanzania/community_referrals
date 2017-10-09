package com.softmed.uzazisalama.Repository;

import android.util.Log;

import com.google.gson.Gson;
import com.softmed.uzazisalama.DataModels.FollowUpReport;
import com.softmed.uzazisalama.DataModels.PregnantMom;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class FollowUpReportObject {
    private String id,relationalId,
            FACILITY_ID,CreatedBy, ModifyBy, MOTHER_ID;
    private String details;
    private int FOLLOW_UP_DATA;
    private long  REPORT_DATE;
    private boolean  IS_ON_RISK;
    private Map<String, String> columnMap;

    public FollowUpReportObject(String id,
                                String relationalId,
                                long REPORT_DATE,
                                boolean IS_ON_RISK,
                                String MOTHERS_ID,
                                String FACILITY_ID,
                                int FOLLOW_UP_DATA,
                                String details,
                                String CreatedBy,
                                String ModifyBy) {
        this.details = details;
        this.id = id;
        this.relationalId = relationalId;
        this.REPORT_DATE = REPORT_DATE;
        this.IS_ON_RISK = IS_ON_RISK;
        this.MOTHER_ID = MOTHERS_ID;
        this.FOLLOW_UP_DATA = FOLLOW_UP_DATA;
        this.FACILITY_ID = FACILITY_ID;
        this.CreatedBy = CreatedBy;
        this.ModifyBy = ModifyBy;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything
    public FollowUpReportObject(String id, String relationalId, FollowUpReport followUpReport) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = relationalId;
        this.REPORT_DATE = followUpReport.getDate();
        this.FOLLOW_UP_DATA = followUpReport.getFollowUpNumber();
        this.MOTHER_ID = followUpReport.getMotherId();
        if (followUpReport.isAlbumin()
                || followUpReport.isBadChildPosition()
                || followUpReport.isChildDealth()
                || followUpReport.isHbBelow60()
                || followUpReport.isHighBloodPressure()
                || followUpReport.isHighSugar()
                || followUpReport.isOver40WeeksPregnancy()
                || followUpReport.isUnproportionalPregnancyHeight())
            this.IS_ON_RISK =  true;
        else
            this.IS_ON_RISK =  false;

        this.FACILITY_ID = followUpReport.getFacilityName();
        this.details = new Gson().toJson(followUpReport);
        this.CreatedBy = followUpReport.getCreatedBy();
        this.ModifyBy = followUpReport.getModifyBy();

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

    public boolean getIS_ON_RISK() {
        return IS_ON_RISK;
    }

    public void setIS_ON_RISK(boolean IS_ON_RISK) {
        this.IS_ON_RISK = IS_ON_RISK;
    }

    public int getFOLLOW_UP_DATA() {
        return FOLLOW_UP_DATA;
    }

    public void setFOLLOW_UP_DATA(int FOLLOW_UP_DATA) {
        this.FOLLOW_UP_DATA = FOLLOW_UP_DATA;
    }

    public long getREPORT_DATE() {
        return REPORT_DATE;
    }

    public void setREPORT_DATE(long REPORT_DATE) {
        this.REPORT_DATE = REPORT_DATE;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }

    public String getFACILITY_ID() {
        return FACILITY_ID;
    }

    public void setFACILITY_ID(String FACILITY_ID) {
        this.FACILITY_ID = FACILITY_ID;
    }

    public String getMOTHERS_ID() {
        return MOTHER_ID;
    }

    public void setMOTHERS_ID(String MOTHERS_ID) {
        this.MOTHER_ID = MOTHERS_ID;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
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
}
