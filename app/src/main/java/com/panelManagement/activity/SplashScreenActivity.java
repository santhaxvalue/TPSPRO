package com.panelManagement.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kochava.android.tracker.Feature;
import com.mobileapptracker.MobileAppTracker;
import com.panelManagement.location.AppLocationService;
import com.panelManagement.location.Receiver;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.LocationService;
import com.panelManagement.utils.MyBounceInterpolator;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;
import com.panelManagement.webservices.ParseJSonObject;

import io.fabric.sdk.android.Fabric;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.panelManagement.utils.PanelConstants.FCM_REG_ID;

public class SplashScreenActivity extends Activity implements RequestListener {
    private static final long SPLASH_TIME = 4000;
    public static boolean IsSurvey = false;
    private Handler handler = new Handler();
    private Runnable splash_runnable;
    private MobileAppTracker mobileAppTracker;
    private WebView myBrowser;
    private String username;
    private ImageView logo;
    private LinearLayout one, two, three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        setContentView(R.layout.activity_splash);
        //Utility.generateKeyHash(this);
        logo = findViewById(R.id.logo_id);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        KEYHASH();
        splashAnimation();
        //	Utility.exportDatabse(this, DBAdapter.DATABASENAME);
        splash_runnable = this::onDataInitialized;

        //boolean isFirstTime= InformatePreferences.getBoolean(getApplicationContext(), Constants.PREF_GPS_DIALOG, true);


        PanelConstants.FCM_REG_ID = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(FCM_REG_ID)) Log.d("FCMKey", FCM_REG_ID);
        handler.postDelayed(splash_runnable, SPLASH_TIME);
        iniliseMAT();
        initializeKochava();

        IsSurvey = true;

        Utility.setLocaleLanguage(InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_LOCALECODE), this);
        myBrowser = findViewById(R.id.webview);
        myBrowser.addJavascriptInterface(new MyJavaScriptInterface(this), "AndroidFunction");
        WebSettings webSettings = myBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //new code
        webSettings.setAppCacheMaxSize(0);
        webSettings.setAllowFileAccess(false);
        webSettings.setAppCacheEnabled(false);

        myBrowser.getSettings().setAppCacheMaxSize(0);
        myBrowser.getSettings().setAppCacheEnabled(false);
        myBrowser.getSettings().setAllowFileAccess(false);

        myBrowser.loadUrl("file:///android_asset/encript.html");
    }

    /**
     * checking for location
     */
    private void location() {
        startService(new Intent(this, AppLocationService.class));
        LocationService locationService = new LocationService(this);

        Location gpsLocation = locationService.getLocation(LocationManager.GPS_PROVIDER);
        PackageManager packageManager = this.getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (hasGPS) {
            if (gpsLocation == null) {
                // showSettingsAlert("GPS");
            }
        }
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SplashScreenActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                SplashScreenActivity.this.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void iniliseMAT() {
        try {
            // Initialize MAT
            mobileAppTracker = MobileAppTracker.init(getApplicationContext(),
                    getString(R.string.mat_addid),
                    getString(R.string.mat_onversionkey));
            mobileAppTracker.setAndroidId(Secure.getString(getContentResolver(), Secure.ANDROID_ID));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // See sample code at
                    // http://developer.android.com/google/play-services/id.html
                    try {
                        Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                        mobileAppTracker.setGoogleAdvertisingId(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
                    } catch (IOException e) {
                        // Unrecoverable error connecting to Google Play
                        // services (e.g.,
                        // the old version of the service doesn't support
                        // getting AdvertisingId).
                        mobileAppTracker.setAndroidId(Secure.getString(
                                getContentResolver(), Secure.ANDROID_ID));
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // Google Play services is not available entirely.
                        mobileAppTracker.setAndroidId(Secure.getString(
                                getContentResolver(), Secure.ANDROID_ID));
                    } catch (GooglePlayServicesRepairableException e) {
                        // Encountered a recoverable error connecting to Google
                        // Play services.
                        mobileAppTracker.setAndroidId(Secure.getString(
                                getContentResolver(), Secure.ANDROID_ID));
                    } catch (NullPointerException e) {
                        // getId() is sometimes null
                        mobileAppTracker.setAndroidId(Secure.getString(
                                getContentResolver(), Secure.ANDROID_ID));
                    }
                }
            }).start();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initializeKochava() {
        HashMap<String, Object> datamap = new HashMap<String, Object>();
        datamap.put(Feature.INPUTITEMS.KOCHAVA_APP_ID, "kothepanelstation-fuq9a3xx");
        datamap.put(Feature.INPUTITEMS.REQUEST_ATTRIBUTION, true);
        new Feature(this, datamap);
    }

    private void KEYHASH() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash====>", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void splashAnimation() {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 40);
        myAnim.setInterpolator(interpolator);
        one.startAnimation(myAnim);

        final Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            // Do something after 5s = 5000ms
            one.setVisibility(View.VISIBLE);
        }, 500);

        final Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            // Do something after 5s = 5000ms
            two.setVisibility(View.VISIBLE);
        }, 1000);

        final Handler handler3 = new Handler();
        handler3.postDelayed(() -> {
            // Do something after 5s = 5000ms
            logo.setVisibility(View.VISIBLE);
        }, 1500);
    }

    public void onDataInitialized() {
        if (InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS).equals("true")) {
            Constants.EnableMetering = false;
            //ODMmeterStatus();

            Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(Constants.PUSHNOTIFY)) {
                    int DefaultTab = intent.getIntExtra(Constants.CHOOSETABS, 1);
                    mainIntent.putExtra(Constants.CHOOSETABS, DefaultTab);
                }
            }
            startActivity(mainIntent);
            PanelConstants.isLogin = true;
            finish();
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);

        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            username = pref.getString("user_Name", "");
            String password = pref.getString("user_PASSWORD", "");

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                if (today.month < 10) {
                    password = password + "_" + today.monthDay + "0" + today.month + today.year
                            + today.format("%k%M%S") + "_";
                } else {
                    password = password + "_" + today.monthDay + today.month + today.year
                            + today.format("%k%M%S") + "_";
                }
                myBrowser.loadUrl("javascript:myFunction_encrypted(\"" + password + "\")");
            } else {
                Intent mainIntent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        }
    }

    private void ODMmeterStatus() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(SplashScreenActivity.this, Constants.PREF_ID));
            // json.put("LanguageCulture", InformatePreferences.getStringPrefrence(SplashScreenActivity.this,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(SplashScreenActivity.this, Constants.API_ODMmeterSTATUS, json.toString(),
                Constants.REQUESTCODE_ODMMETERINGSTATUS, AsyncHttpRequest.Type.POST);

        mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InformatePreferences.putBoolean(SplashScreenActivity.this, Constants.PREF_ODMMETERINGSTATUS, jsonObject.getBoolean("Status"));

                    Constants.EnableMetering = jsonObject.getBoolean("Status");
                    Constants.FirstAttemp_Metering = true;
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    Intent intent = getIntent();
                    if (intent != null) {
                        if (intent.hasExtra(Constants.PUSHNOTIFY)) {
                            int DefaultTab = intent.getIntExtra(Constants.CHOOSETABS, 1);
                            mainIntent.putExtra(Constants.CHOOSETABS, DefaultTab);
                        }
                    }
                    startActivity(mainIntent);
                    PanelConstants.isLogin = true;
                    finish();
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestError(Exception e, int requestCode) {

            }

            @Override
            public void onRequestStarted(int requestCode) {

            }
        });
        mAppRequest.execute();
    }

    private void resetOldPrefrence() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = pref.edit();
        edit.putString("user_Name", "");
        edit.putString("user_PASSWORD", "");
        edit.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mobileAppTracker != null) {
                // Get source of open for app re-engagement
                mobileAppTracker.setReferralSources(this);
                // MAT will not function unless the measureSession call is
                // included
                mobileAppTracker.measureSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {

    }

    @Override
    public void onRequestError(Exception e, int requestCode) {

    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.INPUT_DATE_FORMAT);
        return sdf.format(new Date());
    }

    private class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getEncryptedText(String password) {
            if (Utility.isConnectedToInternet(SplashScreenActivity.this)) {
                AsyncHttpRequest mAppRequest = new AsyncHttpRequest(SplashScreenActivity.this, Constants.PANLISTLOGIN,
                        new ParseJSonObject(SplashScreenActivity.this).getLoginObject(username, password, getCurrentDateTime()).toString(), 1,
                        AsyncHttpRequest.Type.POST);
                mAppRequest.setRequestListener(new RequestListener() {

                    @Override
                    public void onRequestStarted(int requestCode) {
                    }

                    @Override
                    public void onRequestError(Exception e, int requestCode) {
                        Utility.showToast(SplashScreenActivity.this, getString(R.string.msg_serverdown));
                        //finish();

                    }

                    @Override
                    public void onRequestCompleted(String response, int requestCode) {

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.has("Status") && object.getBoolean("Status")) {
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_ID, object.getString("ID"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_SESSIONID,
                                        object.getString("PanelistSessionID"));

                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_ISDCODE,
                                        object.getString("ISDCode"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_COUNTRYCODE,
                                        object.getString("CountryCode"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_MOBILENUMBERMAXLENGHT,
                                        object.getString("MobileNumberMaxLength"));

                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_FIRSTNAME,
                                        object.getString("FirstName"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_LASTNAME,
                                        object.getString("LastName"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_PANELISTSESSIONID,
                                        object.getString("PanelistSessionID"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_PRIVACYPOLICY,
                                        object.getString("PPLink"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_TNC,
                                        object.getString("TandCLink"));
                                InformatePreferences.setStringPrefrence(SplashScreenActivity.this, Constants.PREF_FBLINK,
                                        object.getString("FBLink"));
                                resetOldPrefrence();
                                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Intent i = getIntent();
                                if (i != null) {
                                    if (i.hasExtra(Constants.PUSHNOTIFY)) {
                                        int DefaultTab = i.getIntExtra(Constants.CHOOSETABS, 1);
                                        intent.putExtra(Constants.CHOOSETABS, DefaultTab);
                                    }
                                }
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                                finish();

                            } else {
                                Utility.showToast(SplashScreenActivity.this, object.getString("message"));
                            }
                        } catch (JSONException e) {
                            // Utility.showToast(getActivity(), e.getMessage());
                            e.printStackTrace();
                        }


                    }
                });
                mAppRequest.execute();
            } else {

                Utility.showToast(SplashScreenActivity.this, getString(R.string.msg_low_conn));
            }
        }
    }
}
