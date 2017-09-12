package org.ei.opensrp.mcare.datamodels;

/**
 * Created by ali on 9/5/17.
 */

public class PregnantMom {
    private String name, id, phone, discountId, education, occupation,
            husbandName, husbandEducation, husbandOccupation, physicalAddress;

    private long dateReg, dateLNMP, dateDeliveryPrediction;
    private int age, height, pregnancyCount, birthCount, childrenCount;
    private boolean isAbove20WeeksPregnant,
            has10YrsPassedSinceLastPreg,
            hasBabyDeath,
            has2orMoreBBA,
            hasHeartProblem,
            hasDiabetes,
            hasTB,
            fourOrMorePreg,
            firstPregAbove35Yrs,
            heightBelow150,
            csDelivery,
            kilemaChaNyonga,
            bleedingOnDelivery,
            kondoKukwama;

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

    public boolean isHas10YrsPassedSinceLastPreg() {
        return has10YrsPassedSinceLastPreg;
    }

    public void setHas10YrsPassedSinceLastPreg(boolean has10YrsPassedSinceLastPreg) {
        this.has10YrsPassedSinceLastPreg = has10YrsPassedSinceLastPreg;
    }

    public boolean isHasBabyDeath() {
        return hasBabyDeath;
    }

    public void setHasBabyDeath(boolean hasBabyDeath) {
        this.hasBabyDeath = hasBabyDeath;
    }

    public boolean isHas2orMoreBBA() {
        return has2orMoreBBA;
    }

    public void setHas2orMoreBBA(boolean has2orMoreBBA) {
        this.has2orMoreBBA = has2orMoreBBA;
    }

    public boolean isHasHeartProblem() {
        return hasHeartProblem;
    }

    public void setHasHeartProblem(boolean hasHeartProblem) {
        this.hasHeartProblem = hasHeartProblem;
    }

    public boolean isHasDiabetes() {
        return hasDiabetes;
    }

    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    public boolean isHasTB() {
        return hasTB;
    }

    public void setHasTB(boolean hasTB) {
        this.hasTB = hasTB;
    }

    public boolean isFourOrMorePreg() {
        return fourOrMorePreg;
    }

    public void setFourOrMorePreg(boolean fourOrMorePreg) {
        this.fourOrMorePreg = fourOrMorePreg;
    }

    public boolean isFirstPregAbove35Yrs() {
        return firstPregAbove35Yrs;
    }

    public void setFirstPregAbove35Yrs(boolean firstPregAbove35Yrs) {
        this.firstPregAbove35Yrs = firstPregAbove35Yrs;
    }

    public boolean isHeightBelow150() {
        return heightBelow150;
    }

    public void setHeightBelow150(boolean heightBelow150) {
        this.heightBelow150 = heightBelow150;
    }

    public boolean isCsDelivery() {
        return csDelivery;
    }

    public void setCsDelivery(boolean csDelivery) {
        this.csDelivery = csDelivery;
    }

    public boolean isKilemaChaNyonga() {
        return kilemaChaNyonga;
    }

    public void setKilemaChaNyonga(boolean kilemaChaNyonga) {
        this.kilemaChaNyonga = kilemaChaNyonga;
    }

    public boolean isBleedingOnDelivery() {
        return bleedingOnDelivery;
    }

    public void setBleedingOnDelivery(boolean bleedingOnDelivery) {
        this.bleedingOnDelivery = bleedingOnDelivery;
    }

    public boolean isKondoKukwama() {
        return kondoKukwama;
    }

    public void setKondoKukwama(boolean kondoKukwama) {
        this.kondoKukwama = kondoKukwama;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getHusbandEducation() {
        return husbandEducation;
    }

    public void setHusbandEducation(String husbandEducation) {
        this.husbandEducation = husbandEducation;
    }

    public String getHusbandOccupation() {
        return husbandOccupation;
    }

    public void setHusbandOccupation(String husbandOccupation) {
        this.husbandOccupation = husbandOccupation;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }
}
