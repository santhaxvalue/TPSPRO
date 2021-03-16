package com.panelManagement.model;

import java.io.Serializable;

/**
 * @author manojp this logic does piping the questions based on user input.
 */
public class PipinglogicsModel implements Serializable {
    private static final long serialVersionUID = 2L;

    String campaignid = "";
    int pipingid = 0;
    int pageid = 0;
    String pipingName;
    int sourceid = 0;
    int targetid = 0;

    public PipinglogicsModel(String campaignid, int pipingid, int pageid,
                             String pipingName, int sourceid, int targetid) {
        this.campaignid = campaignid;
        this.pipingid = pipingid;
        this.pageid = pageid;
        this.pipingName = pipingName;
        this.sourceid = sourceid;
        this.targetid = targetid;
    }

    public String getCampaignid() {
        return campaignid;
    }

    public int getPipingid() {
        return pipingid;
    }

    public int getPageid() {
        return pageid;
    }

    public String getPipingName() {
        return pipingName;
    }

    public int getSourceid() {
        return sourceid;
    }

    public int getTargetid() {
        return targetid;
    }

}
