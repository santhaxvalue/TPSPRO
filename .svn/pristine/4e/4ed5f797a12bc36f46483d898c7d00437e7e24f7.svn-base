package com.panelManagement.model;

import android.text.TextUtils;

import java.io.Serializable;

public class RedeemPointHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name = "";
    private String transactionDate = "";
    private int points = 0;
    private String voucherCode = "";
    private String redemptionStatus = "";
    private String redeemChoices = "";
    private String expiryDate = "";
    private String imageUrl = "";

    public RedeemPointHistory(String name, String transactionDate, int points,
                              String voucherCode, String redemptionStatus, String redeemChoices,
                              String expiryDate, String imageUrl) {
        super();
        this.name = name;
        this.transactionDate = transactionDate;
        this.points = points;
        this.voucherCode = voucherCode;
        this.redemptionStatus = redemptionStatus;
        this.redeemChoices = redeemChoices;
        this.expiryDate = expiryDate;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        if (TextUtils.isEmpty(imageUrl))
            return "";
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public int getPoints() {
        return points;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getRedemptionStatus() {
        return redemptionStatus;
    }

    public String getRedeemChoices() {
        return redeemChoices;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

}
