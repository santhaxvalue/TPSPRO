package com.panelManagement.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

/**
 * Created by Centura User1 on 30-05-2017.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Toast.makeText(context, "RECEIVER", Toast.LENGTH_SHORT).show();
        if (InformatePreferences.getBoolean(context, Constants.PREF_ODMMETERINGSTATUS, true))
            context.startService(new Intent(context, AppLocationService.class));



        /*PendingIntent service = null;
        Intent intentForService = new Intent(context, AppLocationService.class);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        if (service == null) {
            service = PendingIntent.getService(context, 0, intentForService, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        Toast.makeText(context, "RECEIVER", Toast.LENGTH_SHORT).show();
        alarmManager.setRepeating(AlarmManager.RTC, time.getTime().getTime(), 5000, service);*/

       /* if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            context.startService(new Intent(context,AppLocationService.class));
        }*/
    }
}
