package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by Coze on 11/20/17.
 */

public class Client implements Serializable {

    private String id, relationalid, client_id,details, first_name, middle_name, surname, facility_id,
            is_valid, service_provider_mobile_number, ward, village, Kijitongoji, care_taker_name, care_taker_phone_number,
            veo, service_provider_uuid, phone_number, gender, community_based_hiv_service, ctc_number,registration_reason_id;
    private long date_of_birth,status;
    public Client() {

    }

    public Client(String id,
                  String relationalId,
                  String client_id,
                  String first_name,
                  String middle_name,
                  String surname,
                  long date_of_birth,
                  String gender,
                  String community_based_hiv_service,
                  String ctc_number,
                  String facility_id,
                  String is_valid,
                  String care_taker_name,
                  String care_taker_phone_number,
                  long status,
                  String phone_number,
                  String village,
                  String veo,
                  String ward,
                  String registration_reason_id,
                  String details
    ) {
        this.details = details;
        this.id = id;
        this.client_id = client_id;
        this.community_based_hiv_service = community_based_hiv_service;
        this.ctc_number = ctc_number;
        this.relationalid = relationalId;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.surname = surname;
        this.care_taker_phone_number = care_taker_phone_number;
        this.facility_id = facility_id;
        this.care_taker_name = care_taker_name;
        this.is_valid = is_valid;
        this.gender = gender;
        this.status=status;
        this.village=village;
        this.ward=ward;
        this.veo = veo;
        this.status=status;
        this.phone_number = phone_number;
        this.date_of_birth = date_of_birth;
        this.registration_reason_id = registration_reason_id;
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

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getKijitongoji() {
        return Kijitongoji;
    }

    public void setKijitongoji(String kijitongoji) {
        Kijitongoji = kijitongoji;
    }

    public String getCare_taker_name() {
        return care_taker_name;
    }

    public void setCare_taker_name(String care_taker_name) {
        this.care_taker_name = care_taker_name;
    }

    public String getCare_taker_phone_number() {
        return care_taker_phone_number;
    }

    public void setCare_taker_phone_number(String care_taker_phone_number) {
        this.care_taker_phone_number = care_taker_phone_number;
    }

    public String getVeo() {
        return veo;
    }

    public void setVeo(String veo) {
        this.veo = veo;
    }

    public String getService_provider_uuid() {
        return service_provider_uuid;
    }

    public void setService_provider_uuid(String service_provider_uuid) {
        this.service_provider_uuid = service_provider_uuid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public long getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(long date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getRegistration_reason_id() {
        return registration_reason_id;
    }

    public void setRegistration_reason_id(String registration_reason_id) {
        this.registration_reason_id = registration_reason_id;
    }
}
