package com.panelManagement.model;

import java.io.Serializable;

public class SurveyModels implements Serializable {


    private String surveyName = "";
    private String date = "";
    private String endTime = "";
    private String points = "";
    private String surveyLink = "";
    int id = 0;
    private String startTime = "";
    private boolean isMobileDiary;


    public SurveyModels(String endTime, String startTime, String points,
                        String surveyTitle, String createdDate, String surveyLink, int id, boolean isMobileDiary) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.points = points;
        this.surveyName = surveyTitle;
        this.date = createdDate;
        this.surveyLink = surveyLink;
        this.id = id;

        this.isMobileDiary = isMobileDiary;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public String getDate() {
        return date;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPoints() {
        return points;
    }

    public String getSurveyLink() {
        return surveyLink;
    }

    public int getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }
    public boolean isMobileDiary() {
        return isMobileDiary;
    }

    public void setMobileDiary(boolean mobileDiary) {
        isMobileDiary = mobileDiary;
    }


}
