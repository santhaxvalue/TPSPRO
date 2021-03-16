package com.panelManagement.model;

import android.text.TextUtils;

import java.io.Serializable;

public class ProfilerModels implements Serializable {
    private static final long serialVersionUID = 1L;

    private String campaignid = "";
    private String campaignName = "";
    private String campaignURL = "";
    private String percentageOfComplete = "";
    private String panelistCampaignId = "";
    private String incentives = "";
    private String numberOfDaysLeft = "";

    public ProfilerModels(String campaignID, String campaignName,
                          String campaignURL, String percentageOfComplete,
                          String panelistCampaignId, String incentives,
                          String numberOfDaysLeft) {
        this.campaignid = campaignID;
        this.campaignName = campaignName;
        this.campaignURL = campaignURL;
        this.percentageOfComplete = percentageOfComplete;
        this.panelistCampaignId = panelistCampaignId;
        this.incentives = incentives;
        this.numberOfDaysLeft = numberOfDaysLeft;
    }

    public String getCampaignID() {
        return campaignid;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getCampaignURL() {
        return campaignURL;
    }

    public int getPercentageOfComplete() {
        int percentageComplete = 0;
        if (TextUtils.isEmpty(percentageOfComplete))
            return 0;
        else {
            try {
                percentageComplete = Integer.parseInt(percentageOfComplete);
            } catch (Exception e) {
            }
        }
        return percentageComplete;
    }

    public String getPanelistCampaignId() {
        return panelistCampaignId;
    }

    public String getIncentives() {
        return incentives;
    }

    public void setIncentives(String incentives) {
        this.incentives = incentives;
    }

    public String getNumberOfDaysLeft() {
        return numberOfDaysLeft;
    }

    public void setNumberOfDaysLeft(String numberOfDaysLeft) {
        this.numberOfDaysLeft = numberOfDaysLeft;
    }

}
