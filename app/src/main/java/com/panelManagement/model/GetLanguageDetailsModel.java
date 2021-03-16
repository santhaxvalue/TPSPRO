package com.panelManagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class    GetLanguageDetailsModel {

    @SerializedName("lanuageDetails")
    @Expose
    private ArrayList<LanguageDetail> languageDetails = new ArrayList<>();
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;

    public ArrayList<LanguageDetail> getLanguageDetails() {
        return languageDetails;
    }

    public void setLanguageDetails(ArrayList<LanguageDetail> languageDetails) {
        this.languageDetails = languageDetails;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}