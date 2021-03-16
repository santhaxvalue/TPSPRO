package com.panelManagement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanelistProfileModel {

    @SerializedName("PanelistId")
    @Expose
    private String panelistId;
    @SerializedName("QuestionId")
    @Expose
    private String questionId;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;

    public String getPanelistId() {
        return panelistId;
    }

    public void setPanelistId(String panelistId) {
        this.panelistId = panelistId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

