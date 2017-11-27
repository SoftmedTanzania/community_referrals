package org.ei.opensrp.drishti.Repository;

import com.google.gson.Gson;

import org.ei.opensrp.drishti.DataModels.ClientReferral;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kency on 14/09/2017.
 */

public class ClientReferralPersonObject {
    private String id, relationalId, fName,mName,lName, CBHS,CTCNumber,IS_VALID,ReferralDate,Referralreason, FacilityId, ReferralService,Status ;

    private String details;
    private Map<String, String> columnMap;

    public ClientReferralPersonObject(String id,
                                      String relationalId,
                                      String fName,
                                      String mName,
                                      String lName,
                                      String CBHS,
                                      String CTCNumber,
                                      String ReferralDate,
                                      String FacilityId,
                                      String ReferralReason,
                                      String ReferralService,
                                      String Status,
                                      String IS_VALID,
                                      String details
                                      ) {
        this.details = details;
        this.id = id;
        this.Status = Status;
        this.CBHS = CBHS;
        this.CTCNumber = CTCNumber;
        this.relationalId = relationalId;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.ReferralDate = ReferralDate;
        this.FacilityId = FacilityId;
        this.Referralreason = ReferralReason;
        this.ReferralService = ReferralService;
        this.IS_VALID = IS_VALID;
    }

    // alternative constructor so you don't pass bucha stuff, PregnantMom contains everything

    public ClientReferralPersonObject(String id, String relationalId, ClientReferral clientReferral) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy", Locale.getDefault());

        this.id = id;
        this.relationalId = relationalId;
        this.fName = clientReferral.getfName();
        this.mName = clientReferral.getmName();
        this.lName= clientReferral.getlName();
        this.ReferralService = clientReferral.getService();
        this.CBHS = clientReferral.getCBHS();
        this.CTCNumber = clientReferral.getCTCNumber();
        this.ReferralDate = clientReferral.getReferralDate();
        this.Referralreason = clientReferral.getReferralReason();
        this.IS_VALID = clientReferral.getIsValid();
        this.Status = clientReferral.getStatus();
        this.FacilityId = clientReferral.getFacilityId();
        this.details = new Gson().toJson(clientReferral);

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCTCNumber() {
        return CTCNumber;
    }

    public void setCTCNumber(String CTCNumber) {
        this.CTCNumber = CTCNumber;
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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
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

    public String getFacilityId() {
        return FacilityId;
    }

    public void setFacilityId(String facilityId) {
        FacilityId = facilityId;
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