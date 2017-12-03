package org.ei.opensrp.drishti.DataModels;

/**
 * Created by ali on 10/2/17.
 */

public class FollowUpReport {

    private long date;
    private String motherId, facilityId, createdBy, modifyBy;
    private boolean childDealth,
            albumin,
            over40WeeksPregnancy,
            unproportionalPregnancyHeight,
            highBloodPressure,
            highSugar,
            badChildPosition,
            hbBelow60;
    private int followUpNumber; // 0=early (for mom on risk), 1=first, 2=second, 3=third, 4=fourth

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getFacilityName() {
        return facilityId;
    }

    public void setFacilityName(String facilityName) {
        this.facilityId = facilityName;
    }

    public boolean isChildDealth() {
        return childDealth;
    }

    public void setChildDealth(boolean childDealth) {
        this.childDealth = childDealth;
    }

    public boolean isAlbumin() {
        return albumin;
    }

    public void setAlbumin(boolean albumin) {
        this.albumin = albumin;
    }

    public boolean isOver40WeeksPregnancy() {
        return over40WeeksPregnancy;
    }

    public void setOver40WeeksPregnancy(boolean over40WeeksPregnancy) {
        this.over40WeeksPregnancy = over40WeeksPregnancy;
    }

    public boolean isUnproportionalPregnancyHeight() {
        return unproportionalPregnancyHeight;
    }

    public void setUnproportionalPregnancyHeight(boolean unproportionalPregnancyHeight) {
        this.unproportionalPregnancyHeight = unproportionalPregnancyHeight;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(boolean highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public boolean isHighSugar() {
        return highSugar;
    }

    public void setHighSugar(boolean highSugar) {
        this.highSugar = highSugar;
    }

    public boolean isBadChildPosition() {
        return badChildPosition;
    }

    public void setBadChildPosition(boolean badChildPosition) {
        this.badChildPosition = badChildPosition;
    }

    public boolean isHbBelow60() {
        return hbBelow60;
    }

    public void setHbBelow60(boolean hbBelow60) {
        this.hbBelow60 = hbBelow60;
    }

    public int getFollowUpNumber() {
        return followUpNumber;
    }

    public void setFollowUpNumber(int followUpNumber) {
        this.followUpNumber = followUpNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
}
