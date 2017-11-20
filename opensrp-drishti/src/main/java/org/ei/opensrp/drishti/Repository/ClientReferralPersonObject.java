package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.ClientReferral;
import org.ei.opensrp.drishti.DataModels.PregnantMom;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class ClientReferralPersonObject {
    private String id, relationalId, clientName, CBHS,IS_VALID,ReferralDate,Referralreason, ReferralFacility, ReferralService ;

    private String details;
    private Map<String, String> columnMap;

    public ClientReferralPersonObject(String id,
                                      String relationalId,
                                      String clientName,
                                      String ReferralDate,
                                      String ReferralFacility,
                                      String ReferralReason,
                                      String ReferralService,
                                      String IS_VALID,
                                      String details,
                                      String CBHS) {
        this.details = details;
        this.id = id;
        this.CBHS = CBHS;
        this.relationalId = relationalId;
        this.clientName = clientName;
        this.ReferralDate = ReferralDate;
        this.ReferralFacility = ReferralFacility;
        this.Referralreason = ReferralReason;
        this.ReferralService = ReferralService;
        this.IS_VALID = IS_VALID;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything

    public ClientReferralPersonObject(String id, String relationalId, ClientReferral clientReferral) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = relationalId;
        this.clientName = clientReferral.getClientName();
        this.ReferralService = clientReferral.getService();
        this.CBHS = clientReferral.getCBHS();
        this.ReferralDate = clientReferral.getReferralDate();
        this.Referralreason = clientReferral.getReferralReason();
        this.IS_VALID = clientReferral.getIsValid();
        this.ReferralFacility = clientReferral.getReferralFacility();
        this.details = new Gson().toJson(clientReferral);

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCBHS() {
        return CBHS;
    }

    public void setCBHS(String CBHS) {
        this.CBHS = CBHS;
    }

    public String getIS_VALID() {
        return IS_VALID;
    }

    public void setIS_VALID(String IS_VALID) {
        this.IS_VALID = IS_VALID;
    }

    public String getReferralDate() {
        return ReferralDate;
    }

    public void setReferralDate(String referralDate) {
        ReferralDate = referralDate;
    }

    public String getReferralreason() {
        return Referralreason;
    }

    public void setReferralreason(String referralreason) {
        Referralreason = referralreason;
    }

    public String getReferralFacility() {
        return ReferralFacility;
    }

    public void setReferralFacility(String referralFacility) {
        ReferralFacility = referralFacility;
    }

    public String getReferralService() {
        return ReferralService;
    }

    public void setReferralService(String referralService) {
        ReferralService = referralService;
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