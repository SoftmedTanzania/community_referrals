package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by ilakozejumanne on 3/15/19.
 */

public class ReferralFeedback implements Serializable {

    private String id, desc, descSw, referralType;

    public ReferralFeedback() {

    }

    public ReferralFeedback(String id, String desc, String descSw, String referralType) {
        this.id = id;
        this.desc = desc;
        this.descSw = descSw;
        this.referralType = referralType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescSw() {
        return descSw;
    }

    public void setDescSw(String descSw) {
        this.descSw = descSw;
    }

    public String getReferralType() {
        return referralType;
    }

    public void setReferralType(String referralType) {
        this.referralType = referralType;
    }
}
