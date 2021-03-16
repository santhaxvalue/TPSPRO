package com.panelManagement.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.gson.Gson;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.fragment.AlertChangeLanguageFragment;
import com.panelManagement.fragment.AvailableSurveyFragment;
import com.panelManagement.fragment.BaseFragment;
import com.panelManagement.fragment.FanPageFragments;
import com.panelManagement.fragment.ProfilerFragment;
import com.panelManagement.fragment.RewardPointsFragment;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.listener.OnSettingClickListener;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.location.AppLocationService;
import com.panelManagement.location.DBhelper;
import com.panelManagement.location.LocationModel;
import com.panelManagement.location.Receiver;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.utils.WebHelper;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;
import com.panelManagement.widgets.BackButtonWidget;
import com.panelManagement.widgets.SettingWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.panelManagement.utils.PanelConstants.LOCATION_PERMS;
import static com.panelManagement.utils.PanelConstants.RC_LOCATION_PERM;

public class HomeFragmentActivity extends FragmentActivity implements OnTabChangeListener, OnSettingClickListener,
        OnBackClickListener, OnprofileSurveyListener, EasyPermissions.PermissionCallbacks {

    public static final String TAB_1_TAG = "profiler";
    public static final String TAB_2_TAG = "survey";
    public static final String TAB_3_TAG = "rewards";
    public static final String TAB_4_TAG = "fragment_fan_page";
    public static final String KEY_REGISTER = "key.register";
    public static boolean isAppClosed;
    FrameLayout container;
    static TextView tabhost_tv;
    static Context static_context;
    static long today;
    private static DBAdapter database;
    DBhelper db;
    RelativeLayout mainCointainer = null;
    TabHost.TabSpec spec1;
    TabHost.TabSpec spec2;
    TabHost.TabSpec spec3;
    TabHost.TabSpec spec4;
    Bundle bundleTab;
    private FragmentTabHost _tabHost;
    private long lastBackPressTime;

    @SuppressLint("HardwareIds")

    private static View createTabView(Context context, String tabText, String textValue) {
        ViewGroup nullView = null;
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_bar, nullView, false);
        tabhost_tv = view.findViewById(R.id.tabhost_tab_textview_title);
        ImageView iv = view.findViewById(R.id.tabhost_tab_imageview_icon);
        tabhost_tv.setText(textValue);
        switch (tabText) {
            case TAB_1_TAG:
                iv.setImageResource(R.drawable.selector_tab_profiler);
                break;
            case TAB_2_TAG:
                iv.setImageResource(R.drawable.selector_tab_survey);
                break;
            case TAB_3_TAG:
                iv.setImageResource(R.drawable.selector_tab_rewards);
                break;
            case TAB_4_TAG:
                iv.setImageResource(R.drawable.selector_tab_fanpage);
                break;
        }

        return view;
    }

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        db = new DBhelper(HomeFragmentActivity.this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.maintabhost_activity);

        InformatePreferences.setStringPrefrence(this, Constants.PREF_INREGISTRATIONPROCESS, "");
        InformatePreferences.setStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS, "true");
        mainCointainer = findViewById(R.id.layout_titlebarTop);
        bundleTab = bundle;
        _tabHost = findViewById(android.R.id.tabhost);
        container = findViewById(R.id.tabFrameLayout);
        _tabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            _tabHost.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        database = DBAdapter.getDBAdapter(HomeFragmentActivity.this);
        spec1 = _tabHost.newTabSpec(TAB_1_TAG);
        spec2 = _tabHost.newTabSpec(TAB_2_TAG);
        spec3 = _tabHost.newTabSpec(TAB_3_TAG);
        spec4 = _tabHost.newTabSpec(TAB_4_TAG);

        spec1.setIndicator(createTabView(this, TAB_1_TAG, getResources().getString(R.string.profiler)));
        spec2.setIndicator(createTabView(this, TAB_2_TAG, getResources().getString(R.string.surveys)));
        spec3.setIndicator(createTabView(this, TAB_3_TAG, getResources().getString(R.string.rewards)));
        spec4.setIndicator(createTabView(this, TAB_4_TAG, getResources().getString(R.string.fanpage)));

        _tabHost.setOnTabChangedListener(this);
        bundleTab = new Bundle();
        // for Change Language default is false
        bundleTab.putBoolean(Constants.CHANGE_LANGUAGE, false);
        _tabHost.addTab(spec1, ProfilerFragment.class, bundleTab);
        _tabHost.addTab(spec2, AvailableSurveyFragment.class, bundleTab);
        _tabHost.addTab(spec3, RewardPointsFragment.class, bundleTab);
        _tabHost.addTab(spec4, FanPageFragments.class, bundleTab);

        SettingWidget btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnSettingClickedListener(this);
        BackButtonWidget btn_back = findViewById(R.id.btn_back);
        btn_back.setOnBackClickedListener(this);


        if (getIntent().hasExtra(Constants.CHOOSETABS)) {

            int tabNum = getIntent().getIntExtra(Constants.CHOOSETABS, 0);  //setting default tab
            _tabHost.setCurrentTab(tabNum);
        }

        //sendDeviceRegistration();

//        Not required. Removed as per clients request
//        if (getIntent().hasExtra(KEY_REGISTER)) {
//            if (getIntent().getBooleanExtra(KEY_REGISTER, false))
//                sendDeviceRegistration();
//        }
        AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(), "949442329",
                "Y6_eCNajxmEQma7dxAM", "60.00", true);
        //ODMmeterStatus();


        locationTask();

        if (InformatePreferences.getInt(HomeFragmentActivity.this, Constants.PREF_PROFILERCOMPLETE) == 100) {
            _tabHost.setCurrentTab(1);
        }
    }

    private void location() {
        PackageManager packageManager = this.getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (hasGPS) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!statusOfGPS) {
                showSettingsAlert();
            }
        }

        /*Intent intent = new Intent();
        intent.setAction("com.panelManagement.location.Receiver");
        sendBroadcast(intent);*/
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeFragmentActivity.this);

        alertDialog.setTitle("GPS" + " SETTINGS");

        alertDialog.setMessage("GPS" + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            HomeFragmentActivity.this.startActivity(intent);
        });

        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();

        });
        alertDialog.show();
    }

    private void ODMmeterStatus() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(HomeFragmentActivity.this, Constants.PREF_ID));
            //   json.put("LanguageCulture", InformatePreferences.getStringPrefrence(HomeFragmentActivity.this,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpRequest mAppRequest;
        mAppRequest = new AsyncHttpRequest(HomeFragmentActivity.this, Constants.API_ODMmeterSTATUS, json.toString(),
                Constants.REQUESTCODE_ODMMETERINGSTATUS, AsyncHttpRequest.Type.POST);

        mAppRequest.setRequestListener(new RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InformatePreferences.putBoolean(HomeFragmentActivity.this, Constants.PREF_ODMMETERINGSTATUS, jsonObject.getBoolean("Status"));
                    if (jsonObject.getBoolean("Status")) {
                        //HomeActivity.StartODMLogNow();
                        Toast.makeText(HomeFragmentActivity.this, "2", Toast.LENGTH_SHORT).show();
                        location();
                        //backend process for location
                        PendingIntent service = null;
                        long interval = 30 * 60 * 1000;     //30 minutes
                        Intent intentForService = new Intent(HomeFragmentActivity.this, Receiver.class);
                        final AlarmManager alarmManager = (AlarmManager) HomeFragmentActivity.this.getSystemService(Context.ALARM_SERVICE);
                        final Calendar time = Calendar.getInstance();
                        time.set(Calendar.MINUTE, 0);
                        time.set(Calendar.SECOND, 0);
                        time.set(Calendar.MILLISECOND, 0);
                        if (service == null) {
                            service = PendingIntent.getBroadcast(HomeFragmentActivity.this, 0, intentForService, PendingIntent.FLAG_UPDATE_CURRENT);
                        }
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, interval, service);
                        savelocation();
                    } else
                        stopService(new Intent(HomeFragmentActivity.this, AppLocationService.class));
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

    // saving the location
    private void savelocation() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(HomeFragmentActivity.this, Constants.PREF_ID));
            // json.put("LanguageCulture", InformatePreferences.getStringPrefrence(HomeFragmentActivity.this,Constants.PREF_LOCALECODE));

            ArrayList<LocationModel> locationModels;
            locationModels = db.getAllLocation();
            JSONArray jsonArray = new JSONArray(new Gson().toJson(locationModels));
            json.put("Locations", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(HomeFragmentActivity.this,
                Constants.API_GEOLOCATION, json.toString(), Constants.REQUESTCODE_GEOLOCATIONSAVE, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(new RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("Status"))
                        db.cleardb();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  if (FCM_Reciever.flag == true && requestCode == 1) {
                    Intent intent = new Intent(HomeFragmentActivity.this, RedeemFragmentActivity.class);
                    intent.putExtra(RedeemFragmentActivity.KEYREWARDS, 2);
                    intent.putExtra("action", R.id.rewardBtnredemtion);
                    FCM_Reciever.flag = false;
                    startActivity(intent);
                }*/
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

        today = Utility.getTimeStamp();
        static_context = HomeFragmentActivity.this;
        // Constants.FirstAttemp_Metering = true;
        //handler.postDelayed(runnable, 1000);

    }

    private void detectingCOuntry() {
        if (Utility.isConnectedToInternet(this)) {
            AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this,
                    Constants.GET_COUNTRY_URL, null, 123,
                    AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(new RequestListener() {

                @Override
                public void onRequestStarted(int requestCode) {

                }

                @Override
                public void onRequestError(Exception e, int requestCode) {

                }

                @Override
                public void onRequestCompleted(String response, int requestCode) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equalsIgnoreCase("Success")) {
                            String countryName = object.getString("country");
                            String countryCode = object.getString("code");
                            InformatePreferences.setStringPrefrence(HomeFragmentActivity.this, Constants.PREF_COUNTRY, countryName);
                            InformatePreferences.setStringPrefrence(HomeFragmentActivity.this, Constants.PREF_COUNTRYCODE, countryCode);
                            //startMeterPrefrences();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            mAppRequest.execute();
        }
    }


    @Override
    public void onBackPressed() {

        if (!hideChangeLanguage()) {
            boolean isPopFragment = false;
            String currentTabTag = _tabHost.getCurrentTabTag();

            switch (currentTabTag) {
                case TAB_1_TAG:
                    isPopFragment = ((BaseFragment) getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
                    break;

                case TAB_2_TAG:
                    if (getSupportFragmentManager().findFragmentByTag(TAB_2_TAG) instanceof BaseFragment)
                        isPopFragment = ((BaseFragment) getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
                    break;

                case TAB_3_TAG:
                    if (getSupportFragmentManager().findFragmentByTag(TAB_3_TAG) instanceof BaseFragment)
                        isPopFragment = ((BaseFragment) getSupportFragmentManager().findFragmentByTag(TAB_3_TAG)).popFragment();
                    break;

            }
            if (!isPopFragment) {
                closeFunc();
                isAppClosed = true;
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId) {
            case TAB_1_TAG:
                mainCointainer.setBackgroundResource(R.color.gray_light);
                break;

            case TAB_2_TAG:
                mainCointainer.setBackgroundResource(R.color.white);
                break;

            case TAB_3_TAG:
                mainCointainer.setBackgroundResource(R.color.white);

                break;

            case TAB_4_TAG:
                mainCointainer.setBackgroundResource(R.color.white);
                break;
        }

        hideChangeLanguage();
    }

    private boolean hideChangeLanguage() {
        InformatePreferences.setStringPrefrence(this, Constants.ISLANGUAGECHANGECALLED, "false");
        FragmentManager fragmentManager = getSupportFragmentManager();

        AlertChangeLanguageFragment myFrag = (AlertChangeLanguageFragment) fragmentManager.findFragmentById(R.id.tabSettingsFragment);
        if (myFrag != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
            transaction.hide(myFrag);
            transaction.remove(myFrag);
            transaction.commit();
            return true;
        } else
            return false;
    }

    @Override
    public void onSettingCall() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment myFrag = fragmentManager.findFragmentById(R.id.tabSettingsFragment);
        if (myFrag != null) {
            return;
        }
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onBackButtonPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AlertChangeLanguageFragment myFrag = (AlertChangeLanguageFragment) fragmentManager.findFragmentById(R.id.tabSettingsFragment);
        if (myFrag != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
            transaction.hide(myFrag);
            transaction.remove(myFrag);
            transaction.commit();
        } else
            closeFunc();
    }

    private void closeFunc() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            Toast.makeText(this, getResources().getString(R.string.press_again_close), Toast.LENGTH_SHORT).show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onProfilerHandler(ProfilerModels model) {
        Toast.makeText(this, String.valueOf(model.getPercentageOfComplete()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChange(int pageno) {

    }

    @Override
    public void onUIUpdate(int color) {
        mainCointainer.setBackgroundResource(color);
    }

    @Override
    public void checkSurveyCompleted(boolean isTrue) {

    }

    @Override
    public void fragmentUpdate(int position) {

    }

    @SuppressWarnings("deprecation")
    protected void showAlertDialogAlternate() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.lbl_appusagepermission));

        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
        alertDialog.setButton("OK", (dialog, which) -> alertDialog.hide());
    }

    private void sendDeviceRegistration() {
        if (Utility.isConnectedToInternet(this)) {
            WebHelper helper = new WebHelper(this);
            Bundle bundle = helper.getRegistrationURLParameter(this, InformatePreferences.getStringPrefrence(this, Constants.EMAILID));

            AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this, Constants.REGISTRATION_URL, 10, bundle, AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(new RequestListener() {

                @Override
                public void onRequestStarted(int requestCode) {

                }

                @Override
                public void onRequestError(Exception e, int requestCode) {

                }

                @Override
                public void onRequestCompleted(String response, int requestCode) {
                    // Utility.showToast(HomeFragmentActivity.this, response);
                }
            });
            mAppRequest.execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean hasLocationPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_PERMS);
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void locationTask() {
        if (hasLocationPermissions()) {
            // Have permissions, do the thing!
            //ODMmeterStatus();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location), RC_LOCATION_PERM, LOCATION_PERMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    protected void showAlertDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getString(R.string.allowdatausage));

        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                try {
                    startActivityForResult(intent, 1213);
                } catch (android.content.ActivityNotFoundException ex) {

                    alertDialog.hide();
                    alertDialog.dismiss();
                    // isActivityNotFound = true;
                    showAlertDialogAlternate();
                    Toast.makeText(getApplicationContext(), "Device doesn't support access usage settings support!!",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),
                            "Please turn on " + getString(R.string.app_name)
                                    + " App Usage permission form Device Settings->Security->Apps with usage access",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        alertDialog.show();
    }
}