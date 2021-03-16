package com.panelManagement.model;

public class OpinionPollResultItem {

    private Integer questionID;
    private String questionName;
    private String questionText;
    private Integer questionTypeID;
    private Integer answerID;
    private String answerChoiceText;
    private Integer pollscount;
    private Double countPercentage;

    public OpinionPollResultItem(Integer questionID, String questionName, String questionText, Integer questionTypeID, Integer answerID, String answerChoiceText, Integer pollscount, Double countPercentage) {
        this.questionID = questionID;
        this.questionName = questionName;
        this.questionText = questionText;
        this.questionTypeID = questionTypeID;
        this.answerID = answerID;
        this.answerChoiceText = answerChoiceText;
        this.pollscount = pollscount;
        this.countPercentage = countPercentage;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getQuestionTypeID() {
        return questionTypeID;
    }

    public void setQuestionTypeID(Integer questionTypeID) {
        this.questionTypeID = questionTypeID;
    }

    public Integer getAnswerID() {
        return answerID;
    }

    public void setAnswerID(Integer answerID) {
        this.answerID = answerID;
    }

    public String getAnswerChoiceText() {
        return answerChoiceText;
    }

    public void setAnswerChoiceText(String answerChoiceText) {
        this.answerChoiceText = answerChoiceText;
    }

    public Integer getPollscount() {
        return pollscount;
    }

    public void setPollscount(Integer pollscount) {
        this.pollscount = pollscount;
    }

    public Double getCountPercentage() {
        return countPercentage;
    }

    public void setCountPercentage(Double countPercentage) {
        this.countPercentage = countPercentage;
    }

}