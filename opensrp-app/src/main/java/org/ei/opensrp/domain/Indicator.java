package org.ei.opensrp.domain;

/**
 * Created by kency on 1/21/18.
 */

public class Indicator {
    private String referralServiceIndicatorId, referralIndicatorId,indicatorName,isActive;

    public Indicator(){

    }

    public Indicator(String id, String referralIndicatorId,String indicatorName,String isActive) {
        this.referralServiceIndicatorId = id;
        this.referralIndicatorId = referralIndicatorId;
        this.indicatorName = indicatorName;
        this.isActive = isActive;

    }

    public String getReferralServiceIndicatorId() {
        return referralServiceIndicatorId;
    }

    public void setReferralServiceIndicatorId(String referralServiceIndicatorId) {
        this.referralServiceIndicatorId = referralServiceIndicatorId;
    }

    public String getReferralIndicatorId() {
        return referralIndicatorId;
    }

    public void setReferralIndicatorId(String referralIndicatorId) {
        this.referralIndicatorId = referralIndicatorId;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
