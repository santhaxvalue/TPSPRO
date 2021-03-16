package com.panelManagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panelManagement.database.DBAdapter;

public class InfiniteReceiver extends BroadcastReceiver {
    public static final String CUSTOM_INTENT = "com.panelManagement.activity.24HOUR";
    String ts;
    DBAdapter database;

    @Override
    public void onReceive(final Context context, Intent intent) {

    }


}
