package com.panelManagement.model;

import java.io.Serializable;

public class ChangeLanguageModel implements Serializable {
    private static final long serialVersionUID = 1L;
    String name = "";
    String localeCode = "";
    boolean isChecked = false;

    public ChangeLanguageModel(String name, Boolean isChecked, String localeCode) {
        super();
        this.name = name;
        this.isChecked = isChecked;
        this.localeCode = localeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

}
