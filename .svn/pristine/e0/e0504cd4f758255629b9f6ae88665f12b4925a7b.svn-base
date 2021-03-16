package com.panelManagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.panelManagement.utils.PanelConstants;

public class PushNotifierActivity extends Activity {

    public static final String TAG = "PushNotifier";
    public static boolean isNofificationViewed;
    private boolean isForeground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        handleNotifications();
        super.onResume();
    }

    private void handleNotifications() {

        isForeground = PanelConstants.IS_FOREGROUND;
        if (isForeground == true) {
        } else {
            SharedPreferences prefs = getSharedPreferences("Login", 0);
            Boolean rememberMe = prefs.getBoolean("remember_me", false);
            if (PanelConstants.isLogin && rememberMe) {
                Intent resultActivity = new Intent(this,
                        HomeFragmentActivity.class);
                startActivity(resultActivity);
            } else {
                Intent resultActivity = new Intent(this,
                        SplashScreenActivity.class);
                startActivity(resultActivity);
            }

        }
        finish();
    }

}
