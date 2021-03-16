package com.panelManagement.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralRedeemModels implements Serializable {
    private static final long serialVersionUID = 1L;

    String Id = "";
    String PartnerName = "";

    String Description = "";
    String ImageURL = "";
    boolean isInstantRedemptionEnabled;
    boolean isEdenRed ;
    boolean isCheck;
    private ArrayList<String> denominations = null;
    private String jsonArray;

    public GeneralRedeemModels(String id, String partnerName, String description, String imgUrl, boolean isInstantRedemptionEnabled,boolean isEdenRed, boolean isChecked, ArrayList<String> denominations, String jsonArray) {

        this.Id = id;
        this.PartnerName = partnerName;
        this.Description = description;
        this.ImageURL = imgUrl;
        this.isInstantRedemptionEnabled = isInstantRedemptionEnabled;
        this.isEdenRed  =  isEdenRed ;
        this.isCheck = isChecked;
        this.denominations = denominations;
        this.jsonArray = jsonArray;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public void setPartnerName(String partnerName) {
        PartnerName = partnerName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }


    public boolean isInstantRedemptionEnabled() {
        return isInstantRedemptionEnabled;
    }


    public void setisInstantRedemptionEnabled(boolean isInstantRedemptionEnabled) {
        this.isInstantRedemptionEnabled = isInstantRedemptionEnabled;
    }

    public boolean isEdenRed() {
        return isEdenRed;
    }


    public void setisEdenRed(boolean isEdenRed) {
        this.isEdenRed = isEdenRed;
    }



    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getJsonArray() {
        return jsonArray;
    }

    public ArrayList<String> getDenominations() {
        return denominations;
    }

    public void setDenominations(ArrayList<String> denominations) {
        this.denominations = denominations;
    }

}
