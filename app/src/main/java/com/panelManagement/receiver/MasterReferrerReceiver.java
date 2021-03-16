package com.panelManagement.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kochava.android.tracker.ReferralCapture;


public class MasterReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        new ReferralCapture().onReceive(context, intent);
    }

}
