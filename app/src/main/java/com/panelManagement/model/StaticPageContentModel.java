package com.panelManagement.model;

import com.google.gson.annotations.SerializedName;

public class StaticPageContentModel {

    @SerializedName("MarketId")
    private int marketId;
    @SerializedName("PageContent")
    private String pageContent;

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

}