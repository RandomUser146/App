package com.example.app.util;

public class SystemItem {
    private String systemName;
    private String systemLocation;
    private String systemStatus;

    public SystemItem(String systemName, String systemLocation, String systemStatus) {
        this.systemName = systemName;
        this.systemLocation = systemLocation;
        this.systemStatus = systemStatus;
    }

    public SystemItem() {
    }

    public String getSystemName() {
        return systemName;
    }

    public String getSystemLocation() {
        return systemLocation;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public void setSystemLocation(String systemLocation) {
        this.systemLocation = systemLocation;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }
}
