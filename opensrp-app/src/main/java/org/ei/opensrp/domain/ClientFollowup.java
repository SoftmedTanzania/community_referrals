package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by kency on 11/20/17.
 */

public class ClientFollowup implements Serializable {

    private String id, relationalid, details,  other_notes,
              first_name, middle_name, surname, facility_id,
            referral_reason, is_valid, service_provider_mobile_number, ward, village, map_cue,
            village_leader, service_provider_group, service_provider_uiid, phone_number,referral_feedback,
            referral_service_id, gender, community_based_hiv_service, ctc_number, referral_status,care_taker_name,care_taker_name_phone_number,care_taker_relationship;
    private long date_of_birth, visit_date,referral_date;

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

    public String getOther_notes() {
        return other_notes;
    }

    public void setOther_notes(String other_notes) {
        this.other_notes = other_notes;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public String getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(String facility_id) {
        this.facility_id = facility_id;
    }

    public String getReferral_reason() {
        return referral_reason;
    }

    public void setReferral_reason(String referral_reason) {
        this.referral_reason = referral_reason;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public String getService_provider_mobile_number() {
        return service_provider_mobile_number;
    }

    public void setService_provider_mobile_number(String service_provider_mobile_number) {
        this.service_provider_mobile_number = service_provider_mobile_number;
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

    public String getMap_cue() {
        return map_cue;
    }

    public void setMap_cue(String map_cue) {
        this.map_cue = map_cue;
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

    public String getCtc_number() {
        return ctc_number;
    }

    public void setCtc_number(String ctc_number) {
        this.ctc_number = ctc_number;
    }

    public String getReferral_status() {
        return referral_status;
    }

    public void setReferral_status(String referral_status) {
        this.referral_status = referral_status;
    }

    public String getCare_taker_name() {
        return care_taker_name;
    }

    public void setCare_taker_name(String care_taker_name) {
        this.care_taker_name = care_taker_name;
    }

    public String getCare_taker_name_phone_number() {
        return care_taker_name_phone_number;
    }

    public void setCare_taker_name_phone_number(String care_taker_name_phone_number) {
        this.care_taker_name_phone_number = care_taker_name_phone_number;
    }

    public String getCare_taker_relationship() {
        return care_taker_relationship;
    }

    public void setCare_taker_relationship(String care_taker_relationship) {
        this.care_taker_relationship = care_taker_relationship;
    }

    public String getReferral_feedback() {
        return referral_feedback;
    }

    public void setReferral_feedback(String referral_feedback) {
        this.referral_feedback = referral_feedback;
    }

    public long getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(long date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public long getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(long visit_date) {
        this.visit_date = visit_date;
    }

    public long getReferral_date() {
        return referral_date;
    }

    public void setReferral_date(long referral_date) {
        this.referral_date = referral_date;
    }
}
