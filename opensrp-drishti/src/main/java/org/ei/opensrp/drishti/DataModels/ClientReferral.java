package org.ei.opensrp.drishti.DataModels;

/**
 * Created by kency on 11/20/17.
 */

public class ClientReferral {

    private String clientName,ReferralDate,ReferralFacility,ReferralReason,isValid, ReferralService,kata,Kijiji,Kijitongoji,VillageLeader,ServiceProviderGroup,ServiceProviderName,PhoneNumber,Service,Gender,CBHS;

    public String getClientName() {
        return clientName;
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

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getReferralDate() {
        return ReferralDate;
    }

    public void setReferralDate(String referralDate) {
        ReferralDate = referralDate;
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

    public String getServiceProviderName() {
        return ServiceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        ServiceProviderName = serviceProviderName;
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
