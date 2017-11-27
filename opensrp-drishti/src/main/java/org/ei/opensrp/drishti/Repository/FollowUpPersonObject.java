package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.DataModels.FollowUp;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class FollowUpPersonObject {
    private String id, relationalId, Comment,Token,Date, Reason,IsValid ;

    private String details;
    private Map<String, String> columnMap;

    public FollowUpPersonObject(String id,
                                String relationalId,
                                String Comment,
                                String Token,
                                String Date,
                                String Reason,
                                String IsValid,
                                String details
                                      ) {
        this.details = details;
        this.id = id;
        this.Comment = Comment;
        this.Token = Token;
        this.Reason = Reason;
        this.Date = Date;
        this.relationalId = relationalId;
        this.IsValid = IsValid;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything

    public FollowUpPersonObject(String id, String relationalId, FollowUp followUp) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = relationalId;
        this.Date = followUp.getDate();
        this.Comment = followUp.getComment();
        this.Reason= followUp.getReason();
        this.Token = followUp.getToken();
        this.IsValid = followUp.getIsValid();
        this.details = new Gson().toJson(followUp);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getIsValid() {
        return IsValid;
    }

    public void setIsValid(String isValid) {
        IsValid = isValid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }
}