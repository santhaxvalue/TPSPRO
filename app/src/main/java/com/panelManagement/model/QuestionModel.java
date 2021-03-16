package com.panelManagement.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author manojp
 */
public class QuestionModel implements Serializable {
    private static final long serialVersionUID = 3L;
    String campaignId = "";
    String maxLength = "";
    String validation = "";
    int sequence = 0;
    int mandatory;
    boolean isnoneditable;
    String answer = "";
    int marketid;
    int languageid;
    String gridtypequestionview = "";
    String minrange = "";
    String maxrange = "";
    String mindt = "";
    String maxdt = "";
    boolean required;
    boolean isAllTranslationAvailable;
    String minLength = "";
    String isRandom = "";
    String isMandatory = "";
    String questionname = "";
    int questionCategoryId;
    int questionTypeId;
    String questionText = "";
    String instructions = "";
    String questionCategory = "";
    String questionType = "";
    String oldfeatureId = "";
    String ishtmlType = "";
    String isgridType = "";
    String groupId = "";
    String gridtypeSchema = "";
    String subquestions = "";
    String textBeforeAnswer = "";
    String textAfterAnswer = "";
    String id = "";
    String createdDate = "";
    String modifiedDate = "";
    int createdBy;
    int modifiedBy;
    boolean isActive;
    ArrayList<AnswerChoiceModel> arrayAnswer = null;
    String useranswer = "";
    int pageno;
    int pageId;
    boolean isSkipStatus;

    public QuestionModel(String campaignId, int pageno, int pageId,
                         String questionID, String maxLength, String validation,
                         int mandatory, boolean isnoneditable, int controlId, String questionText,
                         String useranswer, boolean isActive, String createddate,
                         String modifieddate) {
        this.campaignId = campaignId;
        this.pageno = pageno;
        this.pageId = pageId;
        this.id = questionID;
        this.maxLength = maxLength;
        this.questionTypeId = controlId;
        this.questionText = questionText;
        this.useranswer = useranswer;
        this.isActive = isActive;
        this.validation = validation;
        this.mandatory = mandatory;
        this.isnoneditable = isnoneditable;
        this.createdDate = createddate;
        this.modifiedDate = modifieddate;
    }

    public boolean isSkipStatusTrue() {
        return isSkipStatus;
    }

    public void setskipStatus(boolean isTrue) {
        this.isSkipStatus = isTrue;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getMaxdt() {
        return maxdt;
    }

    public boolean isAllTranslationAvailable() {
        return isAllTranslationAvailable;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public String getValidation() {
        return validation;
    }

    public int getSequence() {
        return sequence;
    }

    public boolean isMandatory() {
        return mandatory == 1 ? true : false;
    }

    public boolean isIsnoneditable() {
        return isnoneditable;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMarketid() {
        return marketid;
    }

    public int getLanguageid() {
        return languageid;
    }

    public String getGridtypequestionview() {
        return gridtypequestionview;
    }

    public String getMinrange() {
        return minrange;
    }

    public String getMaxrange() {
        return maxrange;
    }

    public String getMindt() {
        return mindt;
    }

    public boolean isRequired() {
        return required;
    }

    public String getMinLength() {
        return minLength;
    }

    public String getIsRandom() {
        return isRandom;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public String getQuestionname() {
        return questionname;
    }

    public int getQuestionCategoryId() {
        return questionCategoryId;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String value) {
        this.questionText = value;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getOldfeatureId() {
        return oldfeatureId;
    }

    public String getIshtmlType() {
        return ishtmlType;
    }

    public String getIsgridType() {
        return isgridType;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGridtypeSchema() {
        return gridtypeSchema;
    }

    public String getSubquestions() {
        return subquestions;
    }

    public String getTextBeforeAnswer() {
        return textBeforeAnswer;
    }

    public String getTextAfterAnswer() {
        return textAfterAnswer;
    }

    public String getQuestionId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<AnswerChoiceModel> getArrayAnswer() {
        return arrayAnswer;
    }

    public void setUserAnswer(String value) {
        this.useranswer = value;
    }

    public String getuserAnswer() {
        return useranswer;
    }

    public int getPageNo() {
        return pageno;
    }

    public int getPageId() {
        return pageId;
    }

}
