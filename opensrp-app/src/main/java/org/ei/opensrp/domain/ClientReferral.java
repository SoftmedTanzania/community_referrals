package org.ei.opensrp.domain;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by kency on 11/20/17.
 */

public class ClientReferral implements Serializable {

    private String id, relationalid,details, first_name, middle_name, surname,last_ctc_date, date_of_birth, referral_date, facility_id, referral_reason, is_valid, service_provider_mobile_number, ward, village,Kijitongoji, village_leader, service_provider_group, service_provider_uiid, phone_number, referral_service_id, gender, community_based_hiv_service, ctc_number,Status;
    private boolean has_2Week_cough,
            has_fever,
            had_weight_loss,
            has_severe_sweating,
            has_symptomps_for_associative_diseases,
            has_affected_partner,
            has_headache,
            has_loss_of_appetite,
            has_blood_cough,
            is_at_hot_spot,
            is_lost_follow_up,
            is_vomiting;

    public ClientReferral(String id,
                          String relationalId,
                          String first_name,
                          String middle_name,
                          String surname,
                          String community_based_hiv_service,
                          String ctc_number,
                          String referral_date,
                          String facility_id,
                          String ReferralReason,
                          String referral_service_id,
                          String referral_status,
                          String is_valid,
                          String details
    ) {
        this.details = details;
        this.id = id;
        this.Status = referral_status;
        this.community_based_hiv_service = community_based_hiv_service;
        this.ctc_number = ctc_number;
        this.relationalid = relationalId;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.surname = surname;
        this.referral_date = referral_date;
        this.facility_id = facility_id;
        this.referral_reason = ReferralReason;
        this.referral_service_id = referral_service_id;
        this.is_valid = is_valid;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getCtc_number() {
        return ctc_number;
    }

    public boolean isHas_2Week_cough() {
        return has_2Week_cough;
    }

    public void setHas_2Week_cough(boolean has_2Week_cough) {
        this.has_2Week_cough = has_2Week_cough;
    }

    public boolean is_at_hot_spot() {
        return is_at_hot_spot;
    }

    public void setIs_at_hot_spot(boolean is_at_hot_spot) {
        this.is_at_hot_spot = is_at_hot_spot;
    }

    public boolean isHas_fever() {
        return has_fever;
    }

    public void setHas_fever(boolean has_fever) {
        this.has_fever = has_fever;
    }

    public boolean isHad_weight_loss() {
        return had_weight_loss;
    }

    public void setHad_weight_loss(boolean had_weight_loss) {
        this.had_weight_loss = had_weight_loss;
    }

    public String getLast_ctc_date() {
        return last_ctc_date;
    }

    public void setLast_ctc_date(String last_ctc_date) {
        this.last_ctc_date = last_ctc_date;
    }

    public String getService_provider_mobile_number() {
        return service_provider_mobile_number;
    }

    public void setService_provider_mobile_number(String service_provider_mobile_number) {
        this.service_provider_mobile_number = service_provider_mobile_number;
    }

    public boolean isHas_symptomps_for_associative_diseases() {
        return has_symptomps_for_associative_diseases;
    }

    public void setHas_symptomps_for_associative_diseases(boolean has_symptomps_for_associative_diseases) {
        this.has_symptomps_for_associative_diseases = has_symptomps_for_associative_diseases;
    }

    public boolean isHas_affected_partner() {
        return has_affected_partner;
    }

    public void setHas_affected_partner(boolean has_affected_partner) {
        this.has_affected_partner = has_affected_partner;
    }

    public boolean isHas_headache() {
        return has_headache;
    }

    public void setHas_headache(boolean has_headache) {
        this.has_headache = has_headache;
    }

    public boolean isHas_loss_of_appetite() {
        return has_loss_of_appetite;
    }

    public void setHas_loss_of_appetite(boolean has_loss_of_appetite) {
        this.has_loss_of_appetite = has_loss_of_appetite;
    }

    public boolean is_lost_follow_up() {
        return is_lost_follow_up;
    }

    public boolean is_vomiting() {
        return is_vomiting;
    }

    public void setIs_vomiting(boolean is_vomiting) {
        this.is_vomiting = is_vomiting;
    }

    public boolean isHas_severe_sweating() {
        return has_severe_sweating;
    }

    public void setHas_severe_sweating(boolean has_severe_sweating) {
        this.has_severe_sweating = has_severe_sweating;
    }

    public boolean isHas_blood_cough() {
        return has_blood_cough;
    }

    public void setHas_blood_cough(boolean has_blood_cough) {
        this.has_blood_cough = has_blood_cough;
    }

    public boolean isIs_lost_follow_up() {
        return is_lost_follow_up;
    }

    public void setIs_lost_follow_up(boolean is_lost_follow_up) {
        this.is_lost_follow_up = is_lost_follow_up;
    }

    public void setCtc_number(String ctc_number) {
        this.ctc_number = ctc_number;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public String getReferral_reason() {
        return referral_reason;
    }

    public void setReferral_reason(String referral_reason) {
        this.referral_reason = referral_reason;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getReferral_date() {
        return referral_date;
    }

    public void setReferral_date(String referral_date) {
        this.referral_date = referral_date;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(String facility_id) {
        this.facility_id = facility_id;
    }

    public String getProviderMobileNumber() {
        return service_provider_mobile_number;
    }

    public void setProviderMobileNumber(String providerMobileNumber) {
        this.service_provider_mobile_number = providerMobileNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getKijitongoji() {
        return Kijitongoji;
    }

    public void setKijitongoji(String kijitongoji) {
        Kijitongoji = kijitongoji;
    }

    public String getVillage_leader() {
        return village_leader;
    }

    public void setVillage_leader(String village_leader) {
        this.village_leader = village_leader;
    }

    public String getService_provider_group() {
        return service_provider_group;
    }

    public void setService_provider_group(String service_provider_group) {
        this.service_provider_group = service_provider_group;
    }

    public String getService_provider_uiid() {
        return service_provider_uiid;
    }

    public void setService_provider_uiid(String service_provider_uiid) {
        this.service_provider_uiid = service_provider_uiid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getReferral_service_id() {
        return referral_service_id;
    }

    public void setReferral_service_id(String referral_service_id) {
        this.referral_service_id = referral_service_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCommunity_based_hiv_service() {
        return community_based_hiv_service;
    }

    public void setCommunity_based_hiv_service(String community_based_hiv_service) {
        this.community_based_hiv_service = community_based_hiv_service;
    }
}
