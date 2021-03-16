package com.panelManagement.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.panelManagement.model.MeteringLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TimeZone;

public class WebHelper {
    static Context mContext;

    @SuppressWarnings("static-access")
    public WebHelper(Context context) {
        WebHelper.mContext = context;
    }

    public static String encodeParam(String param) {
        try {
            param = URLEncoder.encode(param, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    public static String getDeviceID() {
        String android_id = "";
        android_id = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
        if (TextUtils.isEmpty(android_id)) {
            return "OSException";
        }
        return android_id;
    }

    public static String getBRAND() {
        String manufacturar = "";
        manufacturar = Build.BRAND;
        if (TextUtils.isEmpty(manufacturar)) {
            return "OSException";
        }
        return manufacturar;
    }

    public static String getManufacturar() {
        String manufacturar = "";
        manufacturar = Build.MANUFACTURER;
        if (TextUtils.isEmpty(manufacturar)) {
            return "OSException";
        }
        return manufacturar;
    }

    public static String getOSVersion() {
        String version = "";
        version = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(version)) {
            return "OSException";
        }
        return version;
    }

    public static String getOS() {
        return "Android";
    }

    public static String getModel() {
        String model = "";
        model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            return "OSException";
        }
        return model;
    }

    public static String getApplication_version() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeZone() {
        try {
            TimeZone tz = TimeZone.getDefault();
            return tz.getID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCarrierName() {
        TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    public static String getPanelName() {
        return "SIMMONS";
    }

    public static boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static int getScreenHeight(Activity activity) {
        try {
            return activity.getWindowManager().getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bundle getRegistrationURLParameter(Context context, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("username", email);
        bundle.putString("deviceId", getDeviceID());
        bundle.putString("deviceOS", getOS());
        bundle.putString("deviceBrand", getManufacturar());
        bundle.putString("deviceModel", getModel());
        bundle.putString("manufacturer", getManufacturar());
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            bundle.putString("landing_page", version);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        bundle.putString("notification_id", PanelConstants.getFcmRegId());
        return bundle;
    }

    public JSONObject getMeteringLogParameter(Context static_context, ArrayList<String> timestamps) {
        JSONObject json = new JSONObject();
        if (timestamps.size() > 0) {
            Gson gson = new Gson();
            MeteringLog meteringLog = new MeteringLog(InformatePreferences.getStringPrefrence(static_context, Constants.PREF_ID), timestamps);
            try {
                json = new JSONObject(gson.toJson(meteringLog));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /*bundle.putString("PanelistId", InformatePreferences.getStringPrefrence(static_context, Constants.PREF_ID));
        bundle.putString("TimeStamps", timestamps.toString());*/
        return json;
    }

    public String getLoginURLParameter(Context context) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("deviceId=" + encodeParam(getDeviceID()));
        sbf.append("&deviceOS=" + encodeParam(getOS()));
        return sbf.toString();
    }

    /*
     * vapi.informatemi.com/tps/api_ping.php? deviceId=qwertyuiop&
     * deviceOS=iOS7.0& deviceBrand=Apple& deviceModel=iPhone5S&
     * manufacturer=Apple
     */
    public Bundle getPingURLParameter() {
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", getDeviceID());
        bundle.putString("deviceOS", getOS());
        bundle.putString("deviceBrand", getBRAND());
        bundle.putString("&deviceModel", getModel());
        bundle.putString("&manufacturer", getManufacturar());
        return bundle;
    }

    public String getRegistrationURLParameter() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("&username=" + encodeParam(HelperBase.email));
        sbf.append("&deviceId=" + encodeParam(getDeviceID()));
        sbf.append("&deviceOS=" + encodeParam(getOS()));
        sbf.append("&deviceBrand=" + encodeParam(getManufacturar()));
        sbf.append("&deviceModel=" + encodeParam(getModel()));
        sbf.append("&manufacturer=" + encodeParam(getManufacturar()));
        return sbf.toString();
    }

    public ContentValues getRegistrationParameter() {
        ContentValues values = new ContentValues();
        try {
            values.put("username", HelperBase.email);
            values.put("deviceId", getDeviceID());
            values.put("deviceOS", getOS());
            values.put("deviceBrand", getBRAND());
            values.put("deviceModel", getModel());
        } catch (Exception e) {
        }
        return values;
    }

}
