package com.panelManagement.model;

public class NotificationLogItem {

    private Integer panelistId = 0;
    private String surveyBadgeLevel = "";
    private String pointsCredited = "";
    private Boolean surveyBadgePoints = false;
    private Boolean monthlyRetentionPoints = false;
    private String createdDate = "";
    private String notification = "";

    public NotificationLogItem(Integer panelistId, String surveyBadgeLevel, String pointsCredited, Boolean surveyBadgePoints, Boolean monthlyRetentionPoints, String createdDate, String notification) {
        this.panelistId = panelistId;
        this.surveyBadgeLevel = surveyBadgeLevel;
        this.pointsCredited = pointsCredited;
        this.surveyBadgePoints = surveyBadgePoints;
        this.monthlyRetentionPoints = monthlyRetentionPoints;
        this.createdDate = createdDate;
        this.notification = notification;
    }

    public Integer getPanelistId() {
        return panelistId;
    }

    public void setPanelistId(Integer panelistId) {
        this.panelistId = panelistId;
    }

    public String getSurveyBadgeLevel() {
        return surveyBadgeLevel;
    }

    public void setSurveyBadgeLevel(String surveyBadgeLevel) {
        this.surveyBadgeLevel = surveyBadgeLevel;
    }

    public String getPointsCredited() {
        return pointsCredited;
    }

    public void setPointsCredited(String pointsCredited) {
        this.pointsCredited = pointsCredited;
    }

    public Boolean getSurveyBadgePoints() {
        return surveyBadgePoints;
    }

    public void setSurveyBadgePoints(Boolean surveyBadgePoints) {
        this.surveyBadgePoints = surveyBadgePoints;
    }

    public Boolean getMonthlyRetentionPoints() {
        return monthlyRetentionPoints;
    }

    public void setMonthlyRetentionPoints(Boolean monthlyRetentionPoints) {
        this.monthlyRetentionPoints = monthlyRetentionPoints;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
