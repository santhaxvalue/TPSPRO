/**
 *
 */
package com.panelManagement.model;

import java.io.Serializable;

/**
 * @author manojp
 */
public class MaskinglogicsModel implements Serializable {

    private static final long serialVersionUID = 4L;
    String campaignid = "";
    int pageid = 0;
    int maskingid = 0;
    String maskingName;
    int sourceid = 0;
    int targetid = 0;
    String targetAnswerId = "";
    String sourceanswerid = "";

    public MaskinglogicsModel(String campaignid, int pageid, int maskingid,
                              String maskingName, int sourceid, String sourceanswerid,
                              int targetid, String targetAnswerId) {
        this.campaignid = campaignid;
        this.pageid = pageid;
        this.maskingid = maskingid;
        this.maskingName = maskingName;
        this.sourceid = sourceid;
        this.sourceanswerid = sourceanswerid;
        this.targetid = targetid;
        this.targetAnswerId = targetAnswerId;
    }

    public String getCampaignid() {
        return campaignid;
    }

    public int getPageid() {
        return pageid;
    }

    public int getMaskingid() {
        return maskingid;
    }

    public String getMaskingName() {
        return maskingName;
    }

    public int getSourceid() {
        return sourceid;
    }

    public int getTargetid() {
        return targetid;
    }

    public String getTargetAnswerId() {
        return targetAnswerId;
    }

    public String getSourceAnswerId() {
        return sourceanswerid;
    }

}
