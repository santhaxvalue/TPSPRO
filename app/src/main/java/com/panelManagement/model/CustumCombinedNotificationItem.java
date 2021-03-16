package com.panelManagement.model;

/*
 * This class contains the information to be displayed to the user
 * This class contains the combined information obtained from
 * NotificationLog and custumNotification API
 */

public class CustumCombinedNotificationItem {
    private String notificationMessage = "";
    private String creationDate = "";

    public CustumCombinedNotificationItem() {

    }
    public CustumCombinedNotificationItem(String notificationMessage, String creationDate) {
        this.notificationMessage = notificationMessage;
        this.creationDate = creationDate;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
