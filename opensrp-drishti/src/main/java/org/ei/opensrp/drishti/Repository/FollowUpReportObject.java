package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;
import org.ei.opensrp.drishti.DataModels.FollowUpReport;

import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class FollowUpReportObject {
    private String id,relationalId,
            facilityId,CreatedBy, ModifyBy, motherId;
    private String details;
    private int followUpData;
    private long reportDate;
    private boolean onRisk;
    private Map<String, String> columnMap;

    public FollowUpReportObject(String id,
                                String relationalId,
                                long reportDate,
                                boolean onRisk,
                                String motherId,
                                String facilityId,
                                int followUpData,
                                String details,
                                String CreatedBy,
                                String ModifyBy) {
        this.details = details;
        this.id = id;
        this.relationalId = relationalId;
        this.reportDate = reportDate;
        this.onRisk = onRisk;
        this.motherId = motherId;
        this.followUpData = followUpData;
        this.facilityId = facilityId;
        this.CreatedBy = CreatedBy;
        this.ModifyBy = ModifyBy;
    }

    // alternative constructor so you don't pass bucha stuff, FollowUpReport contains everything
    public FollowUpReportObject(String id, String relationalId, FollowUpReport followUpReport) {

        this.id = id;
        this.relationalId = relationalId;
        this.reportDate = followUpReport.getDate();
        this.followUpData = followUpReport.getFollowUpNumber();
        this.motherId = followUpReport.getMotherId();
        if (followUpReport.isAlbumin()
                || followUpReport.isBadChildPosition()
                || followUpReport.isChildDealth()
                || followUpReport.isHbBelow60()
                || followUpReport.isHighBloodPressure()
                || followUpReport.isHighSugar()
                || followUpReport.isOver40WeeksPregnancy()
                || followUpReport.isUnproportionalPregnancyHeight())
            this.onRisk =  true;
        else
            this.onRisk =  false;

        this.facilityId = followUpReport.getFacilityName();
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

    public boolean getOnRisk() {
        return onRisk;
    }

    public void setOnRisk(boolean onRisk) {
        this.onRisk = onRisk;
    }

    public int getFollowUpData() {
        return followUpData;
    }

    public void setFollowUpData(int followUpData) {
        this.followUpData = followUpData;
    }

    public long getReportDate() {
        return reportDate;
    }

    public void setReportDate(long reportDate) {
        this.reportDate = reportDate;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
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
