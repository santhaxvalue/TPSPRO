package com.panelManagement.model;

import java.io.Serializable;

public class CountryModel implements Serializable {
    private static final long serialVersionUID = 1L;
    String countryDisplayname = "";
    String countrycode = "";
    boolean isChecked;
    int id = 0;

    public CountryModel(String countryDisplayname, int id) {
        this.countryDisplayname = countryDisplayname;
        this.id = id;

    }

    public CountryModel(String countryDisplayname, String countrycode) {
        this.countryDisplayname = countryDisplayname;
        this.countrycode = countrycode;
    }

    public String getCityDisplayname() {
        return countryDisplayname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public int getCityId() {
        return id;
    }

    public void setCountryChecked(boolean value) {
        this.isChecked = value;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
