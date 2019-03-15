package org.ei.opensrp.domain;

import java.io.Serializable;

/**
 * Created by ilakozejumanne on 3/15/19.
 */

public class RegistrationReasons implements Serializable {

    private String id, descEn, descSw,active;

    public RegistrationReasons(String id, String descEn, String descSw,
                               String active) {
        this.id = id;
        this.descEn = descEn;
        this.descSw = descSw;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescEn() {
        return descEn;
    }

    public void setDescEn(String descEn) {
        this.descEn = descEn;
    }

    public String getDescSw() {
        return descSw;
    }

    public void setDescSw(String descSw) {
        this.descSw = descSw;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
