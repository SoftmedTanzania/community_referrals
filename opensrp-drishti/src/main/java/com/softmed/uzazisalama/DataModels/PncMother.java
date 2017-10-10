package com.softmed.uzazisalama.DataModels;

/**
 * Created by ali on 9/5/17.
 */

public class PncMother {
    private String childCaseId, motherCaseId, deliveryType, deliveryComplication, deliveryDate, admissionDate,CreatedBy,ModifyBy;
    public  PncMother(){

     }
    public PncMother(String childCaseId, String motherCaseId, String deliveryType, String deliveryComplication,String deliveryDate,String admissionDate ,String CreatedBy, String ModifyBy) {
        childCaseId = childCaseId;
        motherCaseId = motherCaseId;
        deliveryType = deliveryType;
        deliveryComplication = deliveryComplication;
        deliveryDate = deliveryDate;
        admissionDate = admissionDate;
        CreatedBy = CreatedBy;
        ModifyBy = ModifyBy;
    }

    public String getChildCaseId() {
        return childCaseId;
    }

    public void setChildCaseId(String childCaseId) {
        this.childCaseId = childCaseId;
    }

    public String getMotherCaseId() {
        return motherCaseId;
    }

    public void setMotherCaseId(String motherCaseId) {
        this.motherCaseId = motherCaseId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryComplication() {
        return deliveryComplication;
    }

    public void setDeliveryComplication(String deliveryComplication) {
        this.deliveryComplication = deliveryComplication;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifyBy() {
        return ModifyBy;
    }

    public void setModifyBy(String modifyBy) {
        ModifyBy = modifyBy;
    }
}
