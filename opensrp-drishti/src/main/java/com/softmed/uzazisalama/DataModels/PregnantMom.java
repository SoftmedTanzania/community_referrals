package com.softmed.uzazisalama.DataModels;

/**
 * Created by ali on 9/5/17.
 */

public class PregnantMom implements java.io.Serializable {
    private String mother_name, cardid, phone, discountId, education, occupation, lastSmsToken, chwComment, reg_type,
            is_pnc,
            is_valid,catchmentArea, createdBy, ModifyBy,
            husbandName, husbandEducation, husbandOccupation, physicalAddress, facilityId;

    private long dateReg, dateLNMP, edd, dateRegistration, dateLastVisited;

    private int age, height, previousFertilityCount,
            hivStatus, // 0=no, 1=yes, 2=unknown
            successfulBirths, livingChildren;
    private boolean isAbove20WeeksPregnant;
    private boolean has10YrsPassedSinceLastPreg;
    private boolean hadStillBirth;
    private boolean has2orMoreBBA;
    private boolean hasHeartProblem;
    private boolean hasDiabetes;
    private boolean hasTB;
    private boolean fourOrMorePreg;
    private boolean firstPregAbove35Yrs;
    private boolean heightBelow150;
    private boolean csDelivery;
    private boolean kilemaChaNyonga;
    private boolean bleedingOnDelivery;
    private boolean kondoKukwama;
    private boolean ancAppointment1;
    private boolean ancAppointment2;
    private boolean ancAppointment3;
    private boolean ancAppointment4;
    private boolean isOnRisk;
    private boolean ancAppointmentEarly;

    public String getName() {
        return mother_name;
    }

    public void setName(String name) {
        this.mother_name = name;
    }

    public String getId() {
        return cardid;
    }

    public void setId(String id) {
        this.cardid = id;
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

    public long getEdd() {
        return edd;
    }

    public void setEdd(long edd) {
        this.edd = edd;
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

    public int getPreviousFertilityCount() {
        return previousFertilityCount;
    }

    public void setPreviousFertilityCount(int previousFertilityCount) {
        this.previousFertilityCount = previousFertilityCount;
    }

    public int getSuccessfulBirths() {
        return successfulBirths;
    }

    public void setSuccessfulBirths(int successfulBirths) {
        this.successfulBirths = successfulBirths;
    }

    public int getLivingChildren() {
        return livingChildren;
    }

    public void setLivingChildren(int livingChildren) {
        this.livingChildren = livingChildren;
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

    public boolean isHadStillBirth() {
        return hadStillBirth;
    }

    public void setHadStillBirth(boolean hadStillBirth) {
        this.hadStillBirth = hadStillBirth;
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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public long getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(long dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public long getDateLastVisited() {
        return dateLastVisited;
    }

    public void setDateLastVisited(long dateLastVisited) {
        this.dateLastVisited = dateLastVisited;
    }

    public boolean isAncAppointment1() {
        return ancAppointment1;
    }

    public void setAncAppointment1(boolean ancAppointment1) {
        this.ancAppointment1 = ancAppointment1;
    }

    public boolean isAncAppointment2() {
        return ancAppointment2;
    }

    public void setAncAppointment2(boolean ancAppointment2) {
        this.ancAppointment2 = ancAppointment2;
    }

    public boolean isAncAppointment3() {
        return ancAppointment3;
    }

    public void setAncAppointment3(boolean ancAppointment3) {
        this.ancAppointment3 = ancAppointment3;
    }

    public boolean isAncAppointment4() {
        return ancAppointment4;
    }

    public void setAncAppointment4(boolean ancAppointment4) {
        this.ancAppointment4 = ancAppointment4;
    }

    public String getLastSmsToken() {
        return lastSmsToken;
    }

    public void setLastSmsToken(String lastSmsToken) {
        this.lastSmsToken = lastSmsToken;
    }

    public String getChwComment() {
        return chwComment;
    }

    public void setChwComment(String chwComment) {
        this.chwComment = chwComment;
    }

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }

    public String getIs_pnc() {
        return is_pnc;
    }

    public void setIs_pnc(String is_pnc) {
        this.is_pnc = is_pnc;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public boolean isOnRisk() {
        return isOnRisk;
    }

    public void setOnRisk(boolean onRisk) {
        isOnRisk = onRisk;
    }

    public boolean isAncAppointmentEarly() {
        return ancAppointmentEarly;
    }

    public void setAncAppointmentEarly(boolean ancAppointmentEarly) {
        this.ancAppointmentEarly = ancAppointmentEarly;
    }

    public int getHivStatus() {
        return hivStatus;
    }

    public void setHivStatus(int hivStatus) {
        this.hivStatus = hivStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifyBy() {
        return ModifyBy;
    }

    public void setModifyBy(String modifyBy) {
        ModifyBy = modifyBy;
    }

    public String getCatchmentArea() {
        return catchmentArea;
    }

    public void setCatchmentArea(String catchmentArea) {
        this.catchmentArea = catchmentArea;
    }
}
