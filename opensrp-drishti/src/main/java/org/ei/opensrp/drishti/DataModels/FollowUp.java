package org.ei.opensrp.drishti.DataModels;

import org.ei.opensrp.drishti.Repository.MotherPersonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 9/5/17.
 */

public class FollowUp {
    private String Comment,Date,Token,reason,IsValid,sponserName, sponserNumber;

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getIsValid() {
        return IsValid;
    }

    public void setIsValid(String isValid) {
        IsValid = isValid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSponserName() {
        return sponserName;
    }

    public void setSponserName(String sponserName) {
        this.sponserName = sponserName;
    }

    public String getSponserNumber() {
        return sponserNumber;
    }

    public void setSponserNumber(String sponserNumber) {
        this.sponserNumber = sponserNumber;
    }
}
