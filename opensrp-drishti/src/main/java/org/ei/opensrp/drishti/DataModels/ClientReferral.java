package org.ei.opensrp.drishti.DataModels;

/**
 * Created by kency on 11/20/17.
 */

public class ClientReferral {

    private String fName,mName,lName,clientDOB,ReferralDate, FacilityId,ReferralReason,isValid, ProviderMobileNumber,kata,Kijiji,Kijitongoji,VillageLeader,ServiceProviderGroup, ServiceProviderId,PhoneNumber,Service,Gender,CBHS,CTCNumber,Status;
    private boolean has2WeekCough,
            hasFever,
            hadWeightLoss,
            hasSevereSweating,
            hasBloodCough,
            isLostFollowUp;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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

    public String getClientDOB() {
        return clientDOB;
    }

    public String getCTCNumber() {
        return CTCNumber;
    }

    public boolean isHas2WeekCough() {
        return has2WeekCough;
    }

    public void setHas2WeekCough(boolean has2WeekCough) {
        this.has2WeekCough = has2WeekCough;
    }

    public boolean isHasFever() {
        return hasFever;
    }

    public void setHasFever(boolean hasFever) {
        this.hasFever = hasFever;
    }

    public boolean isHadWeightLoss() {
        return hadWeightLoss;
    }

    public void setHadWeightLoss(boolean hadWeightLoss) {
        this.hadWeightLoss = hadWeightLoss;
    }

    public boolean isHasSevereSweating() {
        return hasSevereSweating;
    }

    public void setHasSevereSweating(boolean hasSevereSweating) {
        this.hasSevereSweating = hasSevereSweating;
    }

    public boolean isHasBloodCough() {
        return hasBloodCough;
    }

    public void setHasBloodCough(boolean hasBloodCough) {
        this.hasBloodCough = hasBloodCough;
    }

    public boolean isLostFollowUp() {
        return isLostFollowUp;
    }

    public void setLostFollowUp(boolean lostFollowUp) {
        isLostFollowUp = lostFollowUp;
    }

    public void setCTCNumber(String CTCNumber) {
        this.CTCNumber = CTCNumber;
    }

    public void setClientDOB(String clientDOB) {
        this.clientDOB = clientDOB;
    }

    public String getfName() {
        return fName;
    }

    public String getIsValid() {
        return isValid;
    }

    public String getReferralReason() {
        return ReferralReason;
    }

    public void setReferralReason(String referralReason) {
        ReferralReason = referralReason;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getReferralDate() {
        return ReferralDate;
    }

    public void setReferralDate(String referralDate) {
        ReferralDate = referralDate;
    }

    public String getFacilityId() {
        return FacilityId;
    }

    public void setFacilityId(String facilityId) {
        FacilityId = facilityId;
    }

    public String getProviderMobileNumber() {
        return ProviderMobileNumber;
    }

    public void setProviderMobileNumber(String providerMobileNumber) {
        this.ProviderMobileNumber = providerMobileNumber;
    }

    public String getKata() {
        return kata;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public String getKijiji() {
        return Kijiji;
    }

    public void setKijiji(String kijiji) {
        Kijiji = kijiji;
    }

    public String getKijitongoji() {
        return Kijitongoji;
    }

    public void setKijitongoji(String kijitongoji) {
        Kijitongoji = kijitongoji;
    }

    public String getVillageLeader() {
        return VillageLeader;
    }

    public void setVillageLeader(String villageLeader) {
        VillageLeader = villageLeader;
    }

    public String getServiceProviderGroup() {
        return ServiceProviderGroup;
    }

    public void setServiceProviderGroup(String serviceProviderGroup) {
        ServiceProviderGroup = serviceProviderGroup;
    }

    public String getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getCBHS() {
        return CBHS;
    }

    public void setCBHS(String CBHS) {
        this.CBHS = CBHS;
    }
}
