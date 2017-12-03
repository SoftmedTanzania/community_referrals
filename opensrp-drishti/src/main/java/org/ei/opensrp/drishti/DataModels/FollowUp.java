package org.ei.opensrp.drishti.DataModels;

/**
 * Created by ali on 9/5/17.
 */

public class FollowUp {
    private String comment, follow_up_date, token, follow_up_reason,is_valid, sponser_name, sponser_mobile_number;

    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsValid() {
        return is_valid;
    }

    public void setIsValid(String isValid) {
        is_valid = isValid;
    }

    public String getFollow_up_date() {
        return follow_up_date;
    }

    public void setFollow_up_date(String follow_up_date) {
        this.follow_up_date = follow_up_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFollow_up_reason() {
        return follow_up_reason;
    }

    public void setFollow_up_reason(String follow_up_reason) {
        this.follow_up_reason = follow_up_reason;
    }

    public String getSponser_name() {
        return sponser_name;
    }

    public void setSponser_name(String sponser_name) {
        this.sponser_name = sponser_name;
    }

    public String getSponser_mobile_number() {
        return sponser_mobile_number;
    }

    public void setSponser_mobile_number(String sponser_mobile_number) {
        this.sponser_mobile_number = sponser_mobile_number;
    }
}
