package com.panelManagement.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryParseModel extends BaseParseModel {

    public CountryParseModel(JSONObject object) {
        super(object);
    }

    public void setCountryList() {
        try {
            super.parseCoutryList("cityList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CountryModel> getCountryList() {
        return countryList;
    }

    public ArrayList<CountryModel> getCityList(String code) {
        cityList = new ArrayList<CountryModel>();
        try {
            super.parseCityList(code.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityList;
    }
}
