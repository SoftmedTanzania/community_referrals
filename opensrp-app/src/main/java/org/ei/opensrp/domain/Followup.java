package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by Coze on 11/20/17.
 */

public class Followup implements Serializable {

    private String id, relationalid, other_notes,details;
    private long  referral_id,client_id,referral_feedback_id,referral_date;

    public Followup(String id, String relationalid, long client_id,long referral_id,  long referral_feedback_id, String other_notes,long referral_date, String details) {
        this.id = id;
        this.relationalid = relationalid;
        this.other_notes = other_notes;
        this.referral_id = referral_id;
        this.client_id = client_id;
        this.referral_feedback_id = referral_feedback_id;
        this.referral_date = referral_date;
        this.details = details;
    }

    public Followup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getOther_notes() {
        return other_notes;
    }

    public void setOther_notes(String other_notes) {
        this.other_notes = other_notes;
    }

    public long getReferral_id() {
        return referral_id;
    }

    public void setReferral_id(long referral_id) {
        this.referral_id = referral_id;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public long getReferral_feedback_id() {
        return referral_feedback_id;
    }

    public void setReferral_feedback_id(long referral_feedback_id) {
        this.referral_feedback_id = referral_feedback_id;
    }

    public long getReferral_date() {
        return referral_date;
    }

    public void setReferral_date(long referral_date) {
        this.referral_date = referral_date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
