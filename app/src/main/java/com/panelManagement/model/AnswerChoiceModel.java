/**
 *
 */
package com.panelManagement.model;

import java.io.Serializable;

/**
 * @author manojp
 */
public class AnswerChoiceModel implements Serializable {
    private static final long serialVersionUID = 5L;
    String campaignId = "";
    String AnswerChoiceText = "";
    int ModifiedBy;
    String ParentAnswerChoice = "";
    boolean KeepPosition;
    boolean IsThisDefaultAnswer;
    int AnswerChoiceSequence;
    String BeforeAnswerText = "";
    String ModifiedDate = "";
    boolean IsNoneoftheAbove;
    boolean IsActive = false;
    String CreatedBy = "";
    String CreatedDate = "";
    String id = "";
    int qId = 0;
    int ischecked;
    int maskingActive = 0;

    public AnswerChoiceModel(String campaignId, String answerChoiceText,
                             int modifiedBy, String parentAnswerChoice, boolean keepPosition,
                             boolean isThisDefaultAnswer, int answerChoiceSequence,
                             String beforeAnswerText, String modifiedDate,
                             boolean isNoneoftheAbove, boolean isActive, String createdBy,
                             String createdDate, String iD, int qId, int ischecked,
                             int maskingActive) {
        this.campaignId = campaignId;
        this.AnswerChoiceText = answerChoiceText;
        this.ModifiedBy = modifiedBy;
        this.ParentAnswerChoice = parentAnswerChoice;
        this.KeepPosition = keepPosition;
        this.IsThisDefaultAnswer = isThisDefaultAnswer;
        this.AnswerChoiceSequence = answerChoiceSequence;
        this.BeforeAnswerText = beforeAnswerText;
        this.ModifiedDate = modifiedDate;
        this.IsNoneoftheAbove = isNoneoftheAbove;
        this.IsActive = isActive;
        this.CreatedBy = createdBy;
        this.CreatedDate = createdDate;
        this.id = iD;
        this.qId = qId;
        this.ischecked = ischecked;
        this.maskingActive = maskingActive;
    }

    public AnswerChoiceModel(String campaignId2, int targetid,
                             String targetAnswerId, int maskingActive) {
        this.campaignId = campaignId2;
        this.qId = targetid;
        this.id = targetAnswerId;
        this.maskingActive = maskingActive;
    }

    public boolean isChecked() {
        return ischecked == 1 ? true : false;
    }

    public void setChecked(int check) {
        this.ischecked = check;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getAnswerChoiceText() {
        return AnswerChoiceText;
    }

    public int getModifiedBy() {
        return ModifiedBy;
    }

    public String getParentAnswerChoice() {
        return ParentAnswerChoice;
    }

    public boolean isKeepPosition() {
        return KeepPosition;
    }

    public boolean isIsThisDefaultAnswer() {
        return IsThisDefaultAnswer;
    }

    public int getAnswerChoiceSequence() {
        return AnswerChoiceSequence;
    }

    public String getBeforeAnswerText() {
        return BeforeAnswerText;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public boolean isIsNoneoftheAbove() {
        return IsNoneoftheAbove;
    }

    public boolean isIsActive() {
        return IsActive;
    }

    public int getMaskingActive() {
        return maskingActive;
    }

    public void setMaskingActive(int check) {
        this.maskingActive = check;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setAnswerId(String value) {
        this.id = value;
    }

    public String getAnswerID() {
        return id;
    }

    public int getQuestionId() {
        return qId;
    }

    public void setQuestionId(int value) {
        this.qId = value;
    }

    @Override
    public String toString() {
        return this.AnswerChoiceText;
    }

}
