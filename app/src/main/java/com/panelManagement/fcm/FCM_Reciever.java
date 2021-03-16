package com.panelManagement.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SplashScreenActivity;
import com.panelManagement.activity.SurveyViewActivity;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

/**
 * Created by Centura User1 on 24-01-2017.
 */

public class FCM_Reciever extends FirebaseMessagingService {

    Intent resultIntent;
    Context context;
    boolean isPicAvailable = false;
    private String mNotificationSoundDir = "";
    //Notification Tone
    private int mSound = R.raw.panel_station_notification_tone;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = FCM_Reciever.this;
        pushNotificationNew(remoteMessage, context);
    }

    private void pushNotificationNew(RemoteMessage remoteMessage, Context context) {
        Map<String, String> data = remoteMessage.getData();
        String message = "" + data.get("body");
        String title = data.get("title");
        String link = data.get("surveyUrl");
        String imageUrl = data.get("ImageUrl");
        String type = data.get("NotificationType");
        String imageHyperLink = data.get("ImageHyperLink");
        boolean flag = true;

        mNotificationSoundDir = "android.resource://" + getPackageName() + "/";

        if (!TextUtils.isEmpty(imageHyperLink) && TextUtils.isEmpty(link)) {
            link = imageHyperLink;
            flag = false;
        }


        if (title == null)
            title = "";
        if (link == null)
            link = "";
        if (imageUrl == null)
            imageUrl = "";
        if (message == null)
            message = "";
        isPicAvailable = !imageUrl.isEmpty();
        switch (type) {
            case "1":
                if (InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS).equals("true")) {

                    if (link != null) {
                        if (!link.trim().equals("") && flag) {
                            resultIntent = new Intent(this, SurveyViewActivity.class);
                            resultIntent.putExtra(SurveyViewActivity.SURVEY_URL_KEY, link);
                            resultIntent.putExtra(Constants.PUSHNOTIFY, Constants.PUSHNOTIFY);
                            resultIntent.putExtra(Constants.CHOOSETABS, 1);
                        } else if (!link.trim().equals("") && !flag) {
                            resultIntent = new Intent(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(link));
                        } else {
                            resultIntent = new Intent(this, HomeActivity.class);
                            resultIntent.putExtra(Constants.CHOOSETABS, 1);
                        }
                    }
                } else
                    resultIntent = new Intent(this, SplashScreenActivity.class);
                break;

            case "2":
            case "3":
                if (InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS).equals("true")) {
                    if (!link.trim().equals("")) {
                        resultIntent = new Intent(Intent.ACTION_VIEW);
                        resultIntent.setData(Uri.parse(link));
                    } else {
                        resultIntent = new Intent(this, HomeActivity.class);
                        resultIntent.putExtra(Constants.CHOOSETABS, 1);
                    }
                } else
                    resultIntent = new Intent(this, SplashScreenActivity.class);
                break;
            default:
                resultIntent = new Intent(this, SplashScreenActivity.class);
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_channel));
        if (resultIntent != null) {
            TaskStackBuilder tsb = TaskStackBuilder.create(this);
            tsb.addNextIntent(resultIntent);
            PendingIntent pendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                    | PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Style style = null;
            Bitmap icon = null;
            if (isPicAvailable) {
                Bitmap image = null;
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_trans);
                try {
                    URL url = new URL(imageUrl);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                style = new NotificationCompat.BigPictureStyle(builder).
                        bigPicture(image).setSummaryText(message);
            } else {
                style = new NotificationCompat.BigTextStyle(builder).bigText(message);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                sendNotificationLower(message, title, builder, pendingIntent, style, icon);

            } else {

                sendNotificationHigher(message, link, flag, pendingIntent, style, icon);
            }
        }
    }

    private void sendNotificationLower(String message, String title, NotificationCompat.Builder builder, PendingIntent pendingIntent, NotificationCompat.Style style, Bitmap icon) {
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(icon)
                .setSound(Uri.parse(mNotificationSoundDir + mSound))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(style)
                .setAutoCancel(true);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notification);
    }

    private void sendNotificationHigher(String message, String link, boolean flag, PendingIntent pendingIntent, NotificationCompat.Style style, Bitmap icon) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = null;

            notificationChannel = new NotificationChannel("TPS Survey", "TPS Notifcation", importance);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(Uri.parse(mNotificationSoundDir + mSound), attributes);
            notificationManager.createNotificationChannel(notificationChannel);

            if (link != null) {
                if (!link.trim().isEmpty()) {
                    Intent intent = new Intent(this, SurveyViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (flag) {
                        intent.putExtra("surveyUrl", link);
                    } else {
                        intent.putExtra("ImageHyperLink", link);
                    }
                    //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, "TPS Survey")
                                    .setSmallIcon(R.drawable.app_logo_n)
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(message)
                                    .setAutoCancel(true)
                                    .setLargeIcon(icon)
                                    .setStyle(style)
                                    .setSound(Uri.parse(mNotificationSoundDir + mSound))
                                    .setContentIntent(pendingIntent);

                    notificationManager.notify(0, notificationBuilder.build());
                } else {

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.CHOOSETABS, 1);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, "TPS Survey")
                                    .setSmallIcon(R.drawable.app_logo_n)
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(message)
                                    .setAutoCancel(true)
                                    .setLargeIcon(icon)
                                    .setStyle(style)
                                    .setSound(Uri.parse(mNotificationSoundDir + mSound))
                                    .setContentIntent(pendingIntent1);

                    notificationManager.notify(0, notificationBuilder.build());
                }
            }
        }
    }
}