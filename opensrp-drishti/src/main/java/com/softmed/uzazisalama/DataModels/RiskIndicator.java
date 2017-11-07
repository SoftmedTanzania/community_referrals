package com.softmed.uzazisalama.DataModels;

/**
 * Created by ali on 10/11/17.
 */

public class RiskIndicator {
    private String title;
    private boolean detected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }
}
