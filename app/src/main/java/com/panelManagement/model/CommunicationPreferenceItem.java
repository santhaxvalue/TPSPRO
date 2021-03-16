package com.panelManagement.model;

import java.io.Serializable;

public class CommunicationPreferenceItem implements Serializable {
    private Integer id;
    private String resourceKey = "";
    private String name = "";
    private Boolean isAllowedToChange = false;
    private String selected = "1";

    public CommunicationPreferenceItem(Integer iD, String resourceKey, String name, Boolean isAllowedToChange, String selected) {
        this.id = iD;
        this.resourceKey = resourceKey;
        this.name = name;
        this.isAllowedToChange = isAllowedToChange;
        this.selected = selected;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer iD) {
        this.id = iD;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAllowedToChange() {
        return isAllowedToChange;
    }

    public void setIsAllowedToChange(Boolean isAllowedToChange) {
        this.isAllowedToChange = isAllowedToChange;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
