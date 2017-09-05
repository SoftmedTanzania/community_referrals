package org.ei.opensrp.mcare.datamodels;

/**
 * Created by ali on 9/5/17.
 */

public class PregnantMom {
    private String name, id, phone;
    private long dateReg, dateLNMP, dateDeliveryPrediction;
    private int age, height, pregnancyCount, birthCount, childrenCount;
    private boolean isAbove20WeeksPregnant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDateReg() {
        return dateReg;
    }

    public void setDateReg(long dateReg) {
        this.dateReg = dateReg;
    }

    public long getDateLNMP() {
        return dateLNMP;
    }

    public void setDateLNMP(long dateLNMP) {
        this.dateLNMP = dateLNMP;
    }

    public long getDateDeliveryPrediction() {
        return dateDeliveryPrediction;
    }

    public void setDateDeliveryPrediction(long dateDeliveryPrediction) {
        this.dateDeliveryPrediction = dateDeliveryPrediction;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPregnancyCount() {
        return pregnancyCount;
    }

    public void setPregnancyCount(int pregnancyCount) {
        this.pregnancyCount = pregnancyCount;
    }

    public int getBirthCount() {
        return birthCount;
    }

    public void setBirthCount(int birthCount) {
        this.birthCount = birthCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public boolean isAbove20WeeksPregnant() {
        return isAbove20WeeksPregnant;
    }

    public void setAbove20WeeksPregnant(boolean above20WeeksPregnant) {
        isAbove20WeeksPregnant = above20WeeksPregnant;
    }
}
