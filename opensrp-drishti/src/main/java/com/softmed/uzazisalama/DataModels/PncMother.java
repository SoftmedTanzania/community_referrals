package com.softmed.uzazisalama.DataModels;

/**
 * Created by ali on 9/5/17.
 */

public class PncMother {
    private String childCaseId, motherCaseId, deliveryType, para,gravida,mother_status,bba,deliveryComplication,CreatedBy,ModifyBy;
    private long deliveryDate, admissionDate;
    public  PncMother(){

     }
    public PncMother(String childCaseId, String motherCaseId, String deliveryType,String para, String gravida, String bba,String mother_status, String deliveryComplication,long deliveryDate,long admissionDate ,String CreatedBy, String ModifyBy) {
        childCaseId = childCaseId;
        motherCaseId = motherCaseId;
        deliveryType = deliveryType;
        deliveryComplication = deliveryComplication;
        deliveryDate = deliveryDate;
        admissionDate = admissionDate;
        para = para;
        bba = bba;
        gravida = gravida;
        mother_status = mother_status;
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

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public long getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(long admissionDate) {
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

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getGravida() {
        return gravida;
    }

    public void setGravida(String gravida) {
        this.gravida = gravida;
    }

    public String getMother_status() {
        return mother_status;
    }

    public void setMother_status(String mother_status) {
        this.mother_status = mother_status;
    }

    public String getBba() {
        return bba;
    }

    public void setBba(String bba) {
        this.bba = bba;
    }
}
