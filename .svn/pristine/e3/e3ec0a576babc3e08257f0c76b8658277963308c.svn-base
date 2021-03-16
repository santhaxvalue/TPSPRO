package com.panelManagement.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class  UserInfoModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String startTime = "";
    private String endTime = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String gender = "";
    private DateOfBirthModel dateofbirth = null;
    private String phoneNumber = "";
    private String city = "";
    private String zipcode = "";
    private String address = "";
    private String profilePic = "";
    private Type loginType;
    private String userID = "";
    private String publicIp = "";
    private String detectedCountry = "";
    private String shortCode = "";
    private int mobileNumberMaxLength;

    private ArrayList<CountryModel> citylist;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String firstName) {
        this.startTime = firstName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String firstName) {
        this.endTime = firstName;
    }

    public String getUserID() {
        return userID;
    }

    public void setuserid(String value) {
        this.userID = value;
    }

    public String getLoginType() {
        if (loginType.equals(Type.FACEBOOK))
            return String.valueOf(1);
        if (loginType.equals(Type.TWITTER))
            return String.valueOf(2);
        if (loginType.equals(Type.GMAIL))
            return String.valueOf(3);
        return String.valueOf(0);
    }

    public void setLoginType(Type value) {
        this.loginType = value;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMobileMaxLength() {
        return mobileNumberMaxLength;
    }

    public void setMobileLength(int value) {
        this.mobileNumberMaxLength = value;
    }

    public String getDetectedCountry() {
        return this.detectedCountry;
    }

    public void setDetectedCountry(String value) {
        this.detectedCountry = value;
    }

    public String getCountryShortCode() {
        return this.shortCode;
    }

    public void setCountryShortCode(String value) {
        this.shortCode = value;
    }

    public DateOfBirthModel getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(DateOfBirthModel dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getShortMonths()[month - 1];
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPublicIpaddress() {
        if (TextUtils.isEmpty(publicIp))
            return "183.182.86.226";
        return publicIp;
    }

    public void setPublicIpAddress(String value) {
        this.publicIp = value;
    }

    public String getCity() {
        return city;
    }

    // public Country getCountry() {
    // return country;
    // }
    //
    // public void setCountry(String value,Type type) {
    // switch (type) {
    // case FACEBOOK:
    // String[] parts = value.trim().split(",");
    // String countryCode = null;
    // String isoCode = null;
    // country = new Country(parts[0].toString(), parts[1].toString(),
    // parts[2].toString().trim(),countryCode,isoCode);
    // break;
    // default:
    // break;
    // }
    //
    // }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setUserProfiImage(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<CountryModel> getCityList() {
        return citylist;
    }

    public void setCityList(ArrayList<CountryModel> value) {
        this.citylist = value;
    }

    public enum Type {
        MANUAL, FACEBOOK, TWITTER, GMAIL
    }

    public class Country {
        String country = "";
        String city = "";
        String state = "";
        String countryCode = "";
        String isoCode = "";

        public Country(String city, String state, String country,
                       String countryCode, String isoCode) {
            this.country = country;
            this.state = state;
            this.city = city;
            this.countryCode = countryCode;
            this.isoCode = isoCode;
        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getCountrycode() {
            return countryCode;
        }

        public String getISOCode() {
            return isoCode;
        }

    }

}
