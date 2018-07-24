package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by kency on 11/20/17.
 */

public class ClientReferral implements Serializable {

    private String id, relationalid, details, referral_feedback, other_notes,
            services_given_to_patient, referral_uuid, first_name, middle_name, surname, facility_id,
            referral_reason, is_valid, service_provider_mobile_number, ward, village, Kijitongoji,
            village_leader, service_provider_group, service_provider_uiid, phone_number,
            referral_service_id, gender, community_based_hiv_service, ctc_number, referral_status,is_emergency;
    private long date_of_birth, referral_date,appointment_date;
    private boolean test_results;
    private String indicator_ids;

    public ClientReferral() {

    }

    public ClientReferral(String id,
                          String relationalId,
                          String first_name,
                          String middle_name,
                          String surname,
                          String community_based_hiv_service,
                          String ctc_number,
                          Long referral_date,
                          Long appointment_date,
                          String facility_id,
                          String ReferralReason,
                          String referral_service_id,
                          String referral_status,
                          String is_valid,
                          String is_emergency,
                          String details
    ) {
        this.details = details;
        this.id = id;
        this.referral_status = referral_status;
        this.community_based_hiv_service = community_based_hiv_service;
        this.ctc_number = ctc_number;
        this.relationalid = relationalId;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.surname = surname;
        this.referral_date = referral_date;
        this.appointment_date = appointment_date;
        this.facility_id = facility_id;
        this.referral_reason = ReferralReason;
        this.referral_service_id = referral_service_id;
        this.is_valid = is_valid;
        this.is_emergency = is_emergency;
    }

    public String getOther_notes() {
        return other_notes;
    }

    public void setOther_notes(String other_notes) {
        this.other_notes = other_notes;
    }

    public String getServices_given_to_patient() {
        return services_given_to_patient;
    }

    public void setServices_given_to_patient(String services_given_to_patient) {
        this.services_given_to_patient = services_given_to_patient;
    }

    public boolean isTest_results() {
        return test_results;
    }

    public void setTest_results(boolean test_results) {
        this.test_results = test_results;
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


    public String getReferral_uuid() {
        return referral_uuid;
    }

    public void setReferral_uuid(String referral_uuid) {
        this.referral_uuid = referral_uuid;
    }

    public String getIndicator_ids() {
        return indicator_ids;
    }

    public void setIndicator_ids(String indicator_ids) {
        this.indicator_ids = indicator_ids;
    }

    public String getReferral_status() {
        return referral_status;
    }

    public void setReferral_status(String referral_status) {
        this.referral_status = referral_status;
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

    public long getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(long date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCtc_number() {
        return ctc_number;
    }

    public void setCtc_number(String ctc_number) {
        this.ctc_number = ctc_number;
    }

    public String getReferral_feedback() {
        return referral_feedback;
    }

    public void setReferral_feedback(String referral_feedback) {
        this.referral_feedback = referral_feedback;
    }

    public String getService_provider_mobile_number() {
        return service_provider_mobile_number;
    }

    public void setService_provider_mobile_number(String service_provider_mobile_number) {
        this.service_provider_mobile_number = service_provider_mobile_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public String getReferral_reason() {
        return referral_reason;
    }

    public void setReferral_reason(String referral_reason) {
        this.referral_reason = referral_reason;
    }

    public long getReferral_date() {
        return referral_date;
    }

    public void setReferral_date(long referral_date) {
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

    public long getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(long appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getCommunity_based_hiv_service() {
        return community_based_hiv_service;
    }

    public void setCommunity_based_hiv_service(String community_based_hiv_service) {
        this.community_based_hiv_service = community_based_hiv_service;
    }
}
