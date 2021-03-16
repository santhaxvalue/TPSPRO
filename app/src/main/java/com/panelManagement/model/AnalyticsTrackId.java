package com.panelManagement.model;

import android.content.Context;

import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

public class AnalyticsTrackId {


    public static final String TRACKINGID_ARGENTINA = "UA-45922521-8";
    public static final String TRACKINGID_AUSTRALIA = "UA-45922521-19";
    public static final String TRACKINGID_BRAZIL = "UA-45922521-7";
    public static final String TRACKINGID_CHILE = "UA-45922521-15";
    public static final String TRACKINGID_CHINA = "UA-45922521-5";
    public static final String TRACKINGID_COLOMBIA = "UA-45922521-18";
    public static final String TRACKINGID_INDONESIA = "UA-45922521-10";
    public static final String TRACKINGID_MEXICO = "UA-45922521-9";
    public static final String TRACKINGID_RUSSIA = "UA-45922521-14";

    public static final String TRACKINGID_NEWZELAND = "UA-45922521-16";
    public static final String TRACKINGID_NIGERIA = "UA-45922521-17";
    public static final String TRACKINGID_PHILIPINES = "UA-45922521-13";
    public static final String TRACKINGID_POLAND = "UA-45922521-12";
    public static final String TRACKINGID_SOUTHAFRICA = "UA-45922521-4";
    public static final String TRACKINGID_SINGAPORE = "UA-45922521-20";

    public static final String TRACKINGID_SOUTHKOREA = "UA-45922521-11";
    public static final String TRACKINGID_TAIWAN = "UA-45922521-20";
    public static final String TRACKINGID_TURKEY = "UA-45922521-3";

    public static final String TRACKINGID_INDIA = "";
    public static final String TRACKINGID_THAILAND = "";
    private static final String TRACKINGID_VIETNAM = "";
    private static final String TRACKINGID_MALAYSIA = "";

    public AnalyticsTrackId() {
    }

    public static String getid(Context context) {
        String id = TRACKINGID_CHINA;
        if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("CN")) {
            id = TRACKINGID_CHINA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("BR")) {
            id = TRACKINGID_BRAZIL;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("RU")) {
            id = TRACKINGID_RUSSIA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("ZA")) {
            id = TRACKINGID_SOUTHAFRICA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("AR")) {
            id = TRACKINGID_ARGENTINA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("ID")) {
            id = TRACKINGID_INDONESIA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("TR")) {
            id = TRACKINGID_TURKEY;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("KP")) {
            id = TRACKINGID_SOUTHKOREA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("KR")) {
            id = TRACKINGID_SOUTHKOREA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("PL")) {
            id = TRACKINGID_POLAND;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("PH")) {
            id = TRACKINGID_PHILIPINES;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("IN")) {
            id = TRACKINGID_INDIA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("MX")) {
            id = TRACKINGID_MEXICO;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("CL")) {
            id = TRACKINGID_CHILE;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("NZ")) {
            id = TRACKINGID_NEWZELAND;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("NG")) {
            id = TRACKINGID_NIGERIA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("AU")) {
            id = TRACKINGID_AUSTRALIA;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("SG")) {
            id = TRACKINGID_SINGAPORE;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("TH")) {
            id = TRACKINGID_THAILAND;
        } else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("TW")) {
            id = TRACKINGID_TAIWAN;
        }/*else if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("VI")) {
            id = TRACKINGID_VIETNAM;
		}*/

        return id;
    }


}
