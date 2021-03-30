package com.panelManagement.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class RewardPointsModels implements Serializable {

    private static final long serialVersionUID = 1L;
    String earnedPoints = "";
    String spentPoints = "";
    String availablePoints = "0";
    String PointsReview = "0";
    String PointsRejected = "0";
    ArrayList<EarnedPointHistory> arrayEarnedPoint = null;
    ArrayList<RedeemPointHistory> arrayRedeemPoint = null;

    public RewardPointsModels(String earnedPoints, String spentPoints,
                              String availablePoints,
                              String PointsReview,
                              String PointsRejected,
                              ArrayList<EarnedPointHistory> arrayEarnedPoint,
                              ArrayList<RedeemPointHistory> arrayRedeemPoint) {
        super();
        this.earnedPoints = earnedPoints;
        this.spentPoints = spentPoints;
        this.availablePoints = availablePoints;
        this.PointsReview = PointsReview;
        this.PointsRejected = PointsRejected;
        this.arrayEarnedPoint = arrayEarnedPoint;
        this.arrayRedeemPoint = arrayRedeemPoint;
    }

    public String getEarnedPoints() {
        if (TextUtils.isEmpty(earnedPoints))
            return "0";
        return String.valueOf((int) Double.parseDouble(earnedPoints));
    }

    public String getSpentPoints() {
        if (TextUtils.isEmpty(spentPoints))
            return "0";
        return String.valueOf((int) Double.parseDouble(spentPoints));
    }

    public String getAvailablePoints() {
        if (TextUtils.isEmpty(availablePoints))
            return "0";
        return String.valueOf((int) Double.parseDouble(availablePoints));
    }

    public String getPointsReview() {
        if (TextUtils.isEmpty(PointsReview))
            return "0";
        return String.valueOf((int) Double.parseDouble(PointsReview));
    }

    public String getPointsRejected() {
        if (TextUtils.isEmpty(PointsRejected))
            return "0";
        return String.valueOf((int) Double.parseDouble(PointsRejected));
    }


    public ArrayList<EarnedPointHistory> getArrayEarnedPoint() {
        if (arrayEarnedPoint == null)
            arrayEarnedPoint = new ArrayList<>();
        return arrayEarnedPoint;
    }

    public ArrayList<RedeemPointHistory> getArrayRedeemPoint() {
        return arrayRedeemPoint;
    }

}
