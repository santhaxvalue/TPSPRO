package com.panelManagement.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseParseModel {
    protected JSONObject object;
    protected ArrayList<CountryModel> countryList;
    protected ArrayList<CountryModel> cityList;

    public BaseParseModel(JSONObject object) {
        this.object = object;
        countryList = new ArrayList<CountryModel>();

    }

    protected void parseCoutryList(String key) throws JSONException {
        JSONArray countryArray = object.getJSONArray(key);
        for (int i = 0; i < countryArray.length(); i++) {
            JSONObject countryObject = countryArray.getJSONObject(i);
            countryList.add(new CountryModel(countryObject
                    .getString("countryname"), countryObject
                    .getString("shortCode")));
        }

    }

    protected void parseCityList(String code) throws JSONException {
        JSONArray cityArray = object.getJSONArray(code);
        for (int i = 0; i < cityArray.length(); i++) {
            String countryObject = cityArray.getString(i);
            cityList.add(new CountryModel(countryObject, code));
        }

    }
}
