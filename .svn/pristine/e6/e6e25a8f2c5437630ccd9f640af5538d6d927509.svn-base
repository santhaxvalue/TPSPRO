/**
 *
 */
package com.panelManagement.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author manojp \n Logic is to compare with user input and jump pages with its
 *         id accordingly
 */
public class LogicsModel implements Serializable {
    private static final long serialVersionUID = 4L;
    String campaignId = "";
    int pagesId = 0;
    int action = 0;
    String name = "";
    String lookqustionid = "";
    String operator = "";
    String answerid = "";
    String join = "";
    String mappedQuestionId = "";
    private int logicId;

    public LogicsModel(String campaignId, int pagesId, int action, int logicId, String name,
                       String lookqustionid, String operator, String join, String mappedquestionid, String answerid) {
        this.campaignId = campaignId;
        this.pagesId = pagesId;
        this.action = action;
        this.name = name;
        this.lookqustionid = lookqustionid;
        this.operator = operator;
        this.answerid = answerid;
        this.join = join;
        this.logicId = logicId;
        this.mappedQuestionId = mappedquestionid;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public int getPagesId() {
        return pagesId;
    }

    public void setPagesId(int pagesId) {
        this.pagesId = pagesId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLookqustionid() {
        return lookqustionid;
    }

    public void setLookqustionid(String lookqustionid) {
        this.lookqustionid = lookqustionid;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAnswerid() {
        return answerid;
    }

    public void setAnswerid(String answerid) {
        this.answerid = answerid;
    }

    public String getMaskedQuestionId() {
        return mappedQuestionId;
    }

    public String getJoin() {
        if (TextUtils.isEmpty(join)) {
            join = "OR";
        }
        return join;
    }

    public int getLogicId() {
        return logicId;
    }

}
