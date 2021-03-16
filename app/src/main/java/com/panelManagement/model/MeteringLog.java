package com.panelManagement.model;

import java.util.ArrayList;

/**
 * Created by Centura User1 on 07-02-2017.
 */

public class MeteringLog {
    String PanelistId;
    ArrayList<String> TimeStamps;

    public MeteringLog() {
        PanelistId = "";
        TimeStamps = new ArrayList<String>();
    }

    public MeteringLog(String id, ArrayList<String> data) {
        PanelistId = id;
        TimeStamps = data;
    }
}
