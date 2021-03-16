package com.panelManagement.utils;

import android.Manifest;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * @author Infobeans Constants used in application are described here
 */
public class PanelConstants {

    public static final String GCM_SENDER_ID = "686405028035";
    public static final boolean DEBUG = true;
    public static final int NOTIFICATION_ID = 10;
    public static final int RC_LOCATION_PERM = 124;
    public static final String[] LOCATION_PERMS =
            {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String FCM_REG_ID = "";
    public static String GCM_REG_ID = "";
    public static String DEVICE_ID = "";
    public static boolean isBackground = false;
    public static boolean isRead = false;
    public static boolean IS_FOREGROUND = true;
    public static boolean isLogin = false;
    public static boolean isDialogDismissed = false;

    public static String getFcmRegId() {
        if (FCM_REG_ID == "")
            FCM_REG_ID = FirebaseInstanceId.getInstance().getToken();
        return FCM_REG_ID;
    }
}
