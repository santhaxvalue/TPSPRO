package com.panelManagement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author Manoj Prasad Helper class for shared preferences
 */
public class InformatePreferences {
    public static final String KEY_INFORMATE_VALUE = "informatevalues";

    private static SharedPreferences getPreferences(Context context) {
        if (context == null) {
            Log.d("message", "context is null");
            return null;
        } else {
            return context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
        }
    }

    public static void putLong(Context context, String key, long value) {
        getPreferences(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long value) {
        return getPreferences(context).getLong(key, value);
    }

    public static void putString(Context context, String key, String value) {
        if (context != null)
            getPreferences(context).edit().putString(key, value).commit();
        else
            Log.e("context", "null");
    }

    public static String getString(Context context, String key, String defValue) {
        if (context != null)
            return getPreferences(context).getString(key, defValue);
        else {
            Log.e("context", "null");
            return "";
        }
    }

    public static int getInt(Context context, int key) {
        if (context != null)
            return getPreferences(context).getInt(KEY_INFORMATE_VALUE + key, 0);
        else {
            Log.e("context", "null");
            return 0;
        }
    }

    public static void putInt(Context context, int key, int value) {
        if (context != null)
            getPreferences(context).edit().putInt(KEY_INFORMATE_VALUE + key, value).commit();
        else
            Log.e("context", "null");
    }

    public static boolean getBoolean(Context context, int key, boolean def) {
        return getPreferences(context).getBoolean(KEY_INFORMATE_VALUE + key, def);
    }

    public static void putBoolean(Context context, int key, boolean value) {
        getPreferences(context).edit().putBoolean(KEY_INFORMATE_VALUE + key, value).commit();
    }

    public static String getStringPrefrence(Context context, int type) {
        if (context != null)
            return getString(context, KEY_INFORMATE_VALUE + type, "");
        else {
            Log.e("context", "null");
            return "";
        }
    }

    public static String getStringPrefrence_Long(Context context, int type) {
        return getString(context, KEY_INFORMATE_VALUE + type, "0");
    }

    public static void setStringPrefrence(Context context, int type, String value) {
        putString(context, KEY_INFORMATE_VALUE + type, value);
    }

    public static void clear(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static void remove(Context context, int type) {
        if (context != null)
            getPreferences(context).edit().remove(KEY_INFORMATE_VALUE + type).commit();
    }

    public static int getMaxMobileLength(Context context) {
        try {
            return getInt(context, Constants.PREF_MOBILENUMBERMAXLENGHT);
        } catch (Exception e) {
            return 10;
        }
    }

    public static int getMinMobileLength(Context context) {
        try {
            return getInt(context, Constants.PREF_MOBILENUMBERMINLENGHT);
        } catch (Exception e) {
            return 3;
        }
    }

    public static int getMarketId(Context context) {
        try {
            return getInt(context, Constants.PREF_MARKET_ID);
        } catch (Exception e) {
            return 0;
        }
    }

}
