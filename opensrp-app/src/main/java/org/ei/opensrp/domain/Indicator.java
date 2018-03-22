package org.ei.opensrp.domain;

/**
 * Created by kency on 1/21/18.
 */

public class Indicator {
    private String referralServiceIndicatorId, referralIndicatorId,indicatorName,indicatorNameSw,isActive;

    public Indicator(){

    }

    public Indicator(String id, String referralIndicatorId,String indicatorName,String indicatorNameSw, String isActive) {
        this.referralServiceIndicatorId = id;
        this.referralIndicatorId = referralIndicatorId;
        this.indicatorName = indicatorName;
        this.indicatorNameSw = indicatorNameSw;
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

    public String getIndicatorNameSw() {
        return indicatorNameSw;
    }

    public void setIndicatorNameSw(String indicatorNameSw) {
        this.indicatorNameSw = indicatorNameSw;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
