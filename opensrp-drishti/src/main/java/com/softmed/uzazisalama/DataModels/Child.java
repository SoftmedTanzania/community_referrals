package com.softmed.uzazisalama.DataModels;

import java.util.ArrayList;

/**
 * Created by ali on 9/5/17.
 */

public class Child {
    private String gender, status, weight, problem, CreatedBy, ModifyBy;

    public Child(String gender, String status, String weight, String problem, String CreatedBy, String ModifyBy) {
        gender = gender;
        status = status;
        weight = weight;
        problem =problem;
        CreatedBy = CreatedBy;
        ModifyBy = ModifyBy;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
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
