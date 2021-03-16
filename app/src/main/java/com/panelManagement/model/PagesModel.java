package com.panelManagement.model;

import java.io.Serializable;

public class PagesModel implements Serializable {
    private static final long serialVersionUID = 1L;
    int pagesId = 0;
    String campaignId = "";
    String name = "";

    public PagesModel(int pagesId, String campaignId, String name) {
        this.pagesId = pagesId;
        this.campaignId = campaignId;
        this.name = name;
    }

    public int getPagesId() {
        return pagesId;
    }

    public void setPagesId(int pagesId) {
        this.pagesId = pagesId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
