package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by Coze on 11/20/17.
 */

public class Referral implements Serializable {

    private String id, relationalid, details, referral_feedback, other_notes,
            services_given_to_patient, referral_uuid, facility_id,
            referral_reason, is_valid, service_provider_uiid,
            referral_service_id, referral_status,is_emergency, client_id;
    private long  referral_date,appointment_date,referral_type;
    private String indicator_ids;

    public Referral() {

    }

    public Referral(String id,
                    String relationalId,
                    String client_id,
                    Long referral_date,
                    Long appointment_date,
                    String facility_id,
                    String ReferralReason,
                    String referral_service_id,
                    String referral_status,
                    String is_emergency,
                    String is_valid,
                    String indicator_ids,
                    String other_notes,
                    String services_given_to_patient,
                    int referral_type,
                    String service_provider_uiid,
                    String referral_feedback,
                    String referral_uuid,
                    String details
    ) {
        this.details = details;
        this.id = id;
        this.client_id = client_id;
        this.referral_status = referral_status;
        this.relationalid = relationalId;
        this.referral_date = referral_date;
        this.appointment_date = appointment_date;
        this.facility_id = facility_id;
        this.referral_reason = ReferralReason;
        this.referral_service_id = referral_service_id;
        this.is_valid = is_valid;
        this.is_emergency = is_emergency;
        this.other_notes=other_notes;
        this.services_given_to_patient=services_given_to_patient;
        this.referral_type=referral_type;
        this.indicator_ids = indicator_ids;
        this.referral_feedback = referral_feedback;
        this.service_provider_uiid = service_provider_uiid;
        this.referral_uuid = referral_uuid;
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

    public String getReferral_feedback() {
        return referral_feedback;
    }

    public void setReferral_feedback(String referral_feedback) {
        this.referral_feedback = referral_feedback;
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

    public String getReferral_uuid() {
        return referral_uuid;
    }

    public void setReferral_uuid(String referral_uuid) {
        this.referral_uuid = referral_uuid;
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

    public String getService_provider_uiid() {
        return service_provider_uiid;
    }

    public void setService_provider_uiid(String service_provider_uiid) {
        this.service_provider_uiid = service_provider_uiid;
    }

    public String getReferral_service_id() {
        return referral_service_id;
    }

    public void setReferral_service_id(String referral_service_id) {
        this.referral_service_id = referral_service_id;
    }

    public String getReferral_status() {
        return referral_status;
    }

    public void setReferral_status(String referral_status) {
        this.referral_status = referral_status;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public long getReferral_date() {
        return referral_date;
    }

    public void setReferral_date(long referral_date) {
        this.referral_date = referral_date;
    }

    public long getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(long appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getIndicator_ids() {
        return indicator_ids;
    }

    public void setIndicator_ids(String indicator_ids) {
        this.indicator_ids = indicator_ids;
    }

    public long getReferral_type() {
        return referral_type;
    }

    public void setReferral_type(long referral_type) {
        this.referral_type = referral_type;
    }
}
