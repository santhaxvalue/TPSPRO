package com.panelManagement.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panelManagement.activity.HomeFragmentActivity;
import com.panelManagement.activity.R;
import com.panelManagement.activity.RedeemFragmentActivity;
import com.panelManagement.activity.SplashScreenActivity;
import com.panelManagement.activity.SurveyViewActivity;
import com.panelManagement.fragment.RewardPointsFragment;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

public class Notificationlistener extends BroadcastReceiver {

    public static boolean flag;
    Intent resultIntent;
    String typeTPS, link;

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));
        typeTPS = intent.getExtras().getString("datatype");


        link = intent.getExtras().getString("link"); // jayesh
        switch (typeTPS) {
            case "1":
                if (InformatePreferences.getStringPrefrence(context, Constants.PREF_LOGINSUCCESS).equals("true")) {
                    if (!link.equals("")) {
                        resultIntent = new Intent(context, SurveyViewActivity.class);          //changed by Niyaj 28-08-2017
                        resultIntent.putExtra(SurveyViewActivity.SURVEY_URL_KEY, link);
                        resultIntent.putExtra(Constants.PUSHNOTIFY, Constants.PUSHNOTIFY);
                        resultIntent.putExtra(Constants.CHOOSETABS, 1);
                        context.startActivity(resultIntent);
                    } else {
                        resultIntent = new Intent(context, HomeFragmentActivity.class);
                        resultIntent.putExtra(Constants.CHOOSETABS, 1);
                        context.startActivity(resultIntent);
                    }
                } else {
                    resultIntent = new Intent(context, SplashScreenActivity.class);
                    context.startActivity(resultIntent);
                }

                break;

            case "2":
                if (InformatePreferences.getStringPrefrence(context, Constants.PREF_LOGINSUCCESS).equals("true")) {

                    resultIntent = new Intent(context, HomeFragmentActivity.class);          //changed by Niyaj 28-08-2017
                    //  RewardPointsFragment.rewardsPointsData = new ParseJSonObject(getActivity()).getRewardsPoints(new JSONObject(res));
                    resultIntent.putExtra(Constants.CHOOSETABS, 2);
                    resultIntent.putExtra("action", R.id.rewardBtnredemtion);
                    context.startActivity(resultIntent);
                    flag = true;
                } else {
                    resultIntent = new Intent(context, SplashScreenActivity.class);
                    context.startActivity(resultIntent);
                }
                break;
            case "3":
                if (InformatePreferences.getStringPrefrence(context, Constants.PREF_LOGINSUCCESS).equals("true")) {

                    resultIntent = new Intent(context, RedeemFragmentActivity.class);          //changed by Niyaj 28-08-2017
                    resultIntent.putExtra(RewardPointsFragment.KEYREWARDS, 2);
                } else {
                    resultIntent = new Intent(context, SplashScreenActivity.class);
                    context.startActivity(resultIntent);
                }
                break;
            case "4":
                resultIntent = new Intent(context, SplashScreenActivity.class);
                context.startActivity(resultIntent);
            default:
                break;
        }

    }
}
