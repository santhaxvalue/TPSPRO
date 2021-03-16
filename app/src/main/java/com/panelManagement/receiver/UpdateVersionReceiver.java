package com.panelManagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.panelManagement.database.DBAdapter;

public class UpdateVersionReceiver extends BroadcastReceiver {
    String ts;
    DBAdapter database;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ODM", " update version receiver call");
    }


}
