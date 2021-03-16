package com.panelManagement.model;

import java.io.Serializable;

public class EarnedPointHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    String projectSource = "";
    String name = "";
    String transactionDate = "";
    String points = "";
    String campSource = "";

    public EarnedPointHistory(String projectSource, String name, String transactionDate, String points, String campSource) {
        super();
        this.projectSource = projectSource;
        this.name = name;
        this.transactionDate = transactionDate;
        this.points = points;
        this.campSource = campSource;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public String getName() {
        return name;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getPoints() {
        return points;
    }

    public String getCampSource() {
        return campSource;
    }

}


