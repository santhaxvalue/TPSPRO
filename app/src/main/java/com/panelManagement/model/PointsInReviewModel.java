package com.panelManagement.model;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class PointsInReviewModel {

    @SerializedName("SurveyName")
    @Expose
    private Object surveyName;
    @SerializedName("ReviewPoints")
    @Expose
    private Integer reviewPoints;
    @SerializedName("TicketDetails")
    @Expose
    private Object ticketDetails;
    @SerializedName("SurveyId")
    @Expose
    private Integer surveyId;
    @SerializedName("SurveyCompletionDate")
    @Expose
    private String surveyCompletionDate;
    @SerializedName("ReviewPointsText1")
    @Expose
    private String reviewPointsText1;
    @SerializedName("ReviewPointsText2")
    @Expose
    private String reviewPointsText2;
    @SerializedName("ReviewPointsText3")
    @Expose
    private String reviewPointsText3;
    @SerializedName("ReviewPointsText4")
    @Expose
    private String reviewPointsText4;
    @SerializedName("ReviewPointsText5")
    @Expose
    private String reviewPointsText5;
    @SerializedName("ReviewPointsText6")
    @Expose
    private String reviewPointsText6;
    @SerializedName("NoDataFound")
    @Expose
    private String noDataFound;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("CreatedDate")
    @Expose
    private Object createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("CreatedBy")
    @Expose
    private Object createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("IsActive")
    @Expose
    private Object isActive;

    public Object getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(Object surveyName) {
        this.surveyName = surveyName;
    }

    public Integer getReviewPoints() {
        return reviewPoints;
    }

    public void setReviewPoints(Integer reviewPoints) {
        this.reviewPoints = reviewPoints;
    }

    public Object getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(Object ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyCompletionDate() {
        return surveyCompletionDate;
    }

    public void setSurveyCompletionDate(String surveyCompletionDate) {
        this.surveyCompletionDate = surveyCompletionDate;
    }

    public String getReviewPointsText1() {
        return reviewPointsText1;
    }

    public void setReviewPointsText1(String reviewPointsText1) {
        this.reviewPointsText1 = reviewPointsText1;
    }

    public String getReviewPointsText2() {
        return reviewPointsText2;
    }

    public void setReviewPointsText2(String reviewPointsText2) {
        this.reviewPointsText2 = reviewPointsText2;
    }

    public String getReviewPointsText3() {
        return reviewPointsText3;
    }

    public void setReviewPointsText3(String reviewPointsText3) {
        this.reviewPointsText3 = reviewPointsText3;
    }

    public String getReviewPointsText4() {
        return reviewPointsText4;
    }

    public void setReviewPointsText4(String reviewPointsText4) {
        this.reviewPointsText4 = reviewPointsText4;
    }

    public String getReviewPointsText5() {
        return reviewPointsText5;
    }

    public void setReviewPointsText5(String reviewPointsText5) {
        this.reviewPointsText5 = reviewPointsText5;
    }

    public String getReviewPointsText6() {
        return reviewPointsText6;
    }

    public void setReviewPointsText6(String reviewPointsText6) {
        this.reviewPointsText6 = reviewPointsText6;
    }

    public String getNoDataFound() {
        return noDataFound;
    }

    public void setNoDataFound(String noDataFound) {
        this.noDataFound = noDataFound;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

}