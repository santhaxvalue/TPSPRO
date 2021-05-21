package com.panelManagement.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.gson.Gson;
import com.kochava.base.Tracker;
import com.panelManagement.fragment.AlertChangeLanguageFragment;
import com.panelManagement.fragment.AlertContactUsFragment;
import com.panelManagement.fragment.AlertDialogErrorFragment;
import com.panelManagement.fragment.AlertSettingScreenFragment;
import com.panelManagement.fragment.AvailableSurveyFragment;
import com.panelManagement.fragment.BadgeFragment;
import com.panelManagement.fragment.DynamicSurveyLinkFragment;
import com.panelManagement.fragment.FAQFragment;
import com.panelManagement.fragment.FanPageFragments;
import com.panelManagement.fragment.GeneralBuyFragment;
import com.panelManagement.fragment.InviteFragment;
import com.panelManagement.fragment.MobileVerificationFragment;
import com.panelManagement.fragment.NotificationLogFragment;
import com.panelManagement.fragment.PointsInReview;
import com.panelManagement.fragment.ProfilerFragment;
import com.panelManagement.fragment.QuickPollQuestionsFragment;
import com.panelManagement.fragment.RedeemFragment;
import com.panelManagement.fragment.RewardPointsFragment;
import com.panelManagement.fragment.SweepstakeFragment;
import com.panelManagement.listener.LocaleChangeListener;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.listener.OnChangePasswordCallListener;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.location.DBhelper;
import com.panelManagement.location.GenericData;
import com.panelManagement.location.LocationModel;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.CircleTransform;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.ImageCompressor;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.LocationService;
import com.panelManagement.utils.Utility;
import com.panelManagement.utils.WebHelper;
import com.panelManagement.view.FabAdapter;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.ParseJSonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.panelManagement.fragment.RewardPointsFragment.APP_REQUEST_CODE;
import static com.panelManagement.utils.Constants.REQUEST_CHANGE_PASSWORD;
import static com.panelManagement.utils.Constants.REQUEST_GET_DYNAMIC_SURVEY;
import static com.panelManagement.utils.Constants.REQUEST_GET_DYNAMIC_SURVEY_LINK;
import static com.panelManagement.utils.Constants.REQUEST_GET_OPINION_POLL_QUESTIONS;
import static com.panelManagement.utils.PanelConstants.LOCATION_PERMS;
import static com.panelManagement.utils.PanelConstants.RC_LOCATION_PERM;

public class HomeActivity extends AppCompatActivity implements LocaleChangeListener, OnBackClickListener, OnprofileSurveyListener, OnChangePasswordCallListener,
        EasyPermissions.PermissionCallbacks, View.OnClickListener, AsyncHttpRequest.RequestListener {

    public static final String KEY_REGISTER = "key.register";
    private static final String REMINDER_JOB_TAG = "LOCATION_JOB";
    private static final String TAG = HomeActivity.class.getName();
    public static String REWARDSFRAGMENTKEY = "my_reward_fragment";
    public static String SETTINGFRAGMENTKEY = "my_setting_fragment";
    public static String QUICKPOLLFRAGMENTKEY = "my_quickpoll_fragment";
    public static String REDEEMFRAGMENTKEY = "my_redeem_fragment";
    public static String SWEEPSTAKEFRAGMENT = "my_sweepstake_fragment";
    public static String GENERALBUYFRAGMENT = "my_general_buy_fragment";
    public static String MYFRAGMENTKEY = "my_fragment";
    public static Context static_context;
    public static boolean isAppClosed;
    public static TextView profileName;
    public static CardView tt;
    static long today;
    private final int PERMISSION_REQUEST_STORAGE = 2;
    private Dialog changePasswordDialog;
    String temp_phone_number;
    ProgressDialog mProgressDialog;
    AlertDialog.Builder alertDialog;
    DBhelper db;
    protected LocationManager locationManager;

    Context context;

    Bitmap rotatedBitmap = null;

    int[] imageId = {
            R.drawable.ic_myrewards,
            R.drawable.ic_myprofile,
            R.drawable.ic_mysurvey,
            R.drawable.ic_contact_us,
            R.drawable.ic_blog,
            R.drawable.ic_invite,
            R.drawable.ic_badgeicon,
            R.drawable.ic_faq,
            R.drawable.ic_settings
    };
    private String phoneNumberStringFromAccountKit;
    private long lastBackPressTime;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageView profileImage;
    public static TextView phoneNum;
    private TextView email;
    private TextView tv_survey_count;
    private LinearLayout phoneLayout;
    public static FrameLayout MainAppbackground;
    private boolean FlagCommit = false;
    private boolean FlagRedeem = false;
    public static View toolbar;
    private Uri uri;
    private String base64_1 = "";
    private String profileURL = "";
    private String phoneNumber = "";
    private TextView tv_page_title, txt_points_earned_reward_history1;
    private LinearLayout ll_profile_details;
    int version;
    private View header_survey_and_redeem;
    private RelativeLayout ptsRedeemed, pirLayout;
    private static final int PERMISSIONS_REQUEST_CAPTURE_IMAGE = 1;
    private String ErrorMsg;
    private AppEventsLogger logger;
    private String versionNumber;
    private String carrierName;
    private String myVersion;
    private String deviceName;
    private boolean fromSettings;
    private boolean isChangePasswordRequired;
    private boolean isNewlySignedIn;
    private FrameLayout lyt_change_password;
    private FloatingActionButton fabButton;
    private TextView btn_change_pwd;
    private FrameLayout lyt_notification;
    private Button mBtnQuickPoll;
    private TextView mSurveyCount;
    private ImageView mQuickPollIcon;
    //private TextView log;

    SharedPreferences sharedPreferences12;

    CircleImageView edit_ph_number;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);

        context = HomeActivity.this;



        findViewById(R.id.tt).setVisibility(View.GONE);
        db = new DBhelper(HomeActivity.this);
        AccountKit.initialize(getApplicationContext(), null);
        fragmentManager = getSupportFragmentManager();
        logger = AppEventsLogger.newLogger(this);
        InformatePreferences.setStringPrefrence(this, Constants.PREF_INREGISTRATIONPROCESS, "");
        InformatePreferences.setStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS, "true");

        profileURL = InformatePreferences.getStringPrefrence(this, Constants.PREF_FILEPATH);
        phoneNumber = InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER);

        Tracker.configure(new Tracker.Configuration(getApplicationContext())
                .setAppGuid("kothepanelstation-fuq9a3xx")
                .setLogLevel(Tracker.LOG_LEVEL_INFO));
        logEVENT_NAME_COMPLETED_REGISTRATIONEvent();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionNumber = String.valueOf(pinfo.versionCode);

        TelephonyManager manager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();

        // OS Version
        myVersion = android.os.Build.VERSION.RELEASE;

        //deviceName
        deviceName = android.os.Build.MODEL;

        getLatLong();



        fabButton = findViewById(R.id.fab_btn);
        txt_points_earned_reward_history1 = findViewById(R.id.txt_points_earned_reward_history1);
        ImageView iv_home = findViewById(R.id.iv_home);
        toolbar = findViewById(R.id.toolbar_main);
        tv_page_title = findViewById(R.id.tv_page_title);

        ll_profile_details = findViewById(R.id.ll_profile_details);
        tt = findViewById(R.id.tt);
        lyt_change_password = findViewById(R.id.lyt_change_password);
        btn_change_pwd = findViewById(R.id.btn_change_pwd);
        lyt_notification = findViewById(R.id.fl_notification);

        lyt_notification.setVisibility(View.INVISIBLE);
        btn_change_pwd.setOnClickListener(this);
        lyt_notification.setOnClickListener(this);


        if (Utility.isConnectedToInternet(this))
            _checkAppVersionCode();
        else showErrorAlert("", getString(R.string.api_network_connection_unavailbale));

        ImageView iv_back_left_arrow = findViewById(R.id.iv_back_left_arrow);
        iv_back_left_arrow.setOnClickListener(this);
        MainAppbackground = findViewById(R.id.mainAppbackground);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(this);
        setBackgroungPlain();
        if (!TextUtils.isEmpty(profileURL) && !profileURL.equals("null")) {
//            Picasso.get().load(profileURL).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).transform(new CircleTransform()).placeholder(R.drawable.ic_profile)
//                    .error(R.drawable.ic_profile)
//                    .networkPolicy(NetworkPolicy.NO_CACHE).into(profileImage);
            Glide.with(this).load(profileURL).apply(RequestOptions.circleCropTransform().error(R.drawable.ic_profile)).into(profileImage);
        } else {
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }

        edit_ph_number = findViewById(R.id.edit_ph_number);
        profileName = findViewById(R.id.user_name);
        //log = findViewById(R.id.logbox);
        email = findViewById(R.id.email_id);
        email.setText(InformatePreferences.getStringPrefrence(this, Constants.EMAILID));
        phoneNum = findViewById(R.id.phone_num);
        tv_survey_count = findViewById(R.id.tv_survey_count);
        phoneLayout = findViewById(R.id.phone_ll);
        header_survey_and_redeem = findViewById(R.id.header_survey_and_redeem);
        ptsRedeemed = findViewById(R.id.ptsRedeemed1);
        pirLayout = findViewById(R.id.pirLayout1);







        edit_ph_number.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        fabButton.setOnClickListener(this);
        tv_survey_count.setOnClickListener(this);
        phoneLayout.setOnClickListener(this);
//        header_survey_and_redeem.setOnClickListener(this);
        ptsRedeemed.setOnClickListener(this);
        pirLayout.setOnClickListener(this);


//         Not required. Removed as per the clients request.
//        if (getIntent().hasExtra(KEY_REGISTER)) {
//            if (getIntent().getBooleanExtra(KEY_REGISTER, false))
//                sendDeviceRegistration();
//        }
        AdWordsConversionReporter.reportWithConversionId(getApplicationContext(), "949442329",
                "Y6_eCNajxmEQma7dxAM", "60.00", true);

        locationTask();

        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_ISDCODE), InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER)));
        }

        //callopinion poll api
        //callOpinionPollAPI();

        //set Quickpoll image
        mQuickPollIcon = (ImageView) findViewById(R.id.btn_quickpoll);
        mQuickPollIcon.setOnClickListener(this);
        Glide.with(this)
                .load(R.raw.quickpoll_icon)
                .into(mQuickPollIcon);

    }

    public static void setBackgroungPlain() {
        MainAppbackground.setBackgroundColor(Color.parseColor("#ececec"));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setRewardsBackground() {
        MainAppbackground.setBackground(ContextCompat.getDrawable(static_context, R.drawable.new_bg));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setSurvayBackground() {
        MainAppbackground.setBackground(ContextCompat.getDrawable(static_context, R.drawable.new_bg));
    }


    public void setPageTitle(String pageTitle) {
        tv_page_title.setText(pageTitle);
    }

    public static void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    private void _checkAppVersionCode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(this, Constants.PREF_ID));
            jsonObject.put("InstalledVersionCode", String.valueOf(BuildConfig.VERSION_CODE));
            jsonObject.put("AppDeviceTypeID", "2");
            jsonObject.put("AppVersion", versionNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_TPSAppUpdate, jsonObject, Constants.REQUEST_CHECK_VERSION_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_pwd:
                hideChangePasswordPage();
                break;
            case R.id.btn_quickpoll:
                if (Utility.isConnectedToInternet(this)) {
                    launchQuickPollFragment();
                } else {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
                break;
            case R.id.fl_notification:
                if (Utility.isConnectedToInternet(this)) {
                    tv_survey_count.setVisibility(View.INVISIBLE);
                    launchNotificationPage();
                } else {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
                break;
//            case R.id.header_survey_and_redeem:
            case R.id.ptsRedeemed1:
                launchRewardsPage();
                break;
            case R.id.pirLayout1:
                launchPointsInReview();
                break;
            case R.id.edit_ph_number:
                //_verifyPhoneViaAccountKitOTP();

                findViewById(R.id.toolbar_main).setVisibility(View.GONE);
                MobileVerificationFragment fragment2 = new MobileVerificationFragment();
                Bundle bundleobj = new Bundle();
                bundleobj.putString("key", "Profile");
                fragment2.setArguments(bundleobj);
                this.getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment2).addToBackStack(HomeActivity.MYFRAGMENTKEY).commit();
               /* MobileVerificationFragment fragment2 = new MobileVerificationFragment();
                this.getSupportFragmentManager().beginTransaction().add(R.id.main_container_fragment, fragment2).addToBackStack(HomeActivity.MYFRAGMENTKEY).commit();*/

                break;
            case R.id.fab_btn:
                showMenu(this);
                break;
            case R.id.iv_home:
                toolbar.setVisibility(View.VISIBLE);
                tv_page_title.setVisibility(View.GONE);
                ll_profile_details.setVisibility(View.VISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                profileName.setVisibility(View.VISIBLE);
                email.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);

                tt.setVisibility(View.GONE);

                findViewById(R.id.fab_btn).setVisibility(View.VISIBLE);
                profileName.setGravity(Gravity.CENTER_HORIZONTAL);
                profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container_fragment, AvailableSurveyFragment.newInstance());
                fragmentTransaction.commit();

                break;
            case R.id.iv_back_left_arrow:
                backPressEvent();
                findViewById(R.id.tt).setVisibility(View.GONE);
                break;

            case R.id.profile_image:
                captureImage();
                break;
            default:
                break;
        }
    }

    private void launchNotificationPage() {
        header_survey_and_redeem.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, NotificationLogFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    public void _verifyPhoneViaAccountKitOTP() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE; use TOKEN to get mobile number
        String countryCode = InformatePreferences.getStringPrefrence(this, Constants.PREF_COUNTRYCODE);
        configurationBuilder.setDefaultCountryCode(countryCode);
        configurationBuilder.setSMSWhitelist(new String[]{countryCode});
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void location() {
        PackageManager packageManager = getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if (hasGPS) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!statusOfGPS) {

                if (alertDialog == null)
                    showSettingsAlert();

            }
        }
    }

    public void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS" + " SETTINGS");
        alertDialog.setMessage("GPS" + " is not enabled! Want to go to settings menu?");
        alertDialog.setPositiveButton("settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });

        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        alertDialog.show();

    }


    private void getLatLong() {

        DecimalFormat df = new DecimalFormat("##.#######");
        LocationModel locationModel = new LocationModel();
        if (locationManager == null) {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        }
        double latitude = 0, longitude = 0;
        LocationService locationService = new LocationService(getBaseContext());
        Location location30 = locationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (location30 != null) {
            latitude = location30.getLatitude();
            longitude = location30.getLongitude();
            locationModel.setDate(GenericData.getCurrentTime());
            locationModel.setLatitude(df.format(latitude));
            locationModel.setLongitude(df.format(longitude));
        } else {
            Location location = locationService.getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                locationModel.setDate(GenericData.getCurrentTime());
                locationModel.setLatitude(df.format(latitude));
                locationModel.setLongitude(df.format(longitude));


            }

        }
        requestTypePost(Constants.API_PANELIST_DEVICE_VERISON, new ParseJSonObject(this).getPanelListDeviceSave(myVersion, carrierName, versionNumber,
                deviceName, latitude, longitude), Constants.REQUESTCODE_PANELIST_SAVE);
    }

    public void showMenu(Context context) {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fbb);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        FloatingActionButton fabBtnDlg = dialog.findViewById(R.id.fab_btn_dialog);
        View menu_container = dialog.findViewById(R.id.menu_container);
        GridView grid = dialog.findViewById(R.id.fab_grid_view);

        FabAdapter adapter = new FabAdapter(this, imageId);

        fabBtnDlg.setOnClickListener(v -> {
            v.setEnabled(false);
            dialog.dismiss();
        });

        menu_container.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dimWindowOutsideDialog(dialog);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            switch (position) {
                case 0:
                    launchRewardsPage();
                    break;
                case 1:
                    launchProfilePage();
                    break;
                case 2:
                    launchMySurvey();
                    break;
                case 3:
                    launchContactUs();
                    break;
                case 4:
                    launchFanPage();
                    break;
                case 5:
                    luanchInvte();
                    break;
                case 6:
                    launchBadge();
                    break;
                case 7:
                    launchFaq();
                    break;
                case 8:
                    launchSettings();
                    break;
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void getBadgeInfoOfPanelist() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(this, Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this
                , Constants.API_SURVEY_BADGE_BY_PANELIST_ID
                , jsonObject.toString()
                , Constants.REQUEST_SURVEY_BADGE_BY_PANELIST_ID
                , AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    private void launchBadge() {
        toolbar.setVisibility(View.GONE);
        tt.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, BadgeFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchSettings() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.GONE);
        tv_page_title.setText(R.string.title_faq);
        ll_profile_details.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phoneLayout.setVisibility(View.VISIBLE);

        tt.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, AlertSettingScreenFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchFaq() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.VISIBLE);
        tv_page_title.setText(R.string.title_faq);
        ll_profile_details.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        profileName.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);

        tt.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, FAQFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void luanchInvte() {
        toolbar.setVisibility(View.GONE);
        tt.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, InviteFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchFanPage() {
        toolbar.setVisibility(View.GONE);

        tt.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, FanPageFragments.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchDynamicSurveyPage(String link) {
        toolbar.setVisibility(View.GONE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, DynamicSurveyLinkFragment.newInstance(link)).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchContactUs() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.VISIBLE);
        tv_page_title.setText(R.string.title_contact_us);
        ll_profile_details.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        profileName.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);

        tt.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, AlertContactUsFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchMySurvey() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.GONE);
        ll_profile_details.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);

        tt.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, AvailableSurveyFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchProfilePage() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.GONE);
        ll_profile_details.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phoneLayout.setVisibility(View.VISIBLE);

        tt.setVisibility(View.GONE);

        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, ProfilerFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }

    private void launchRewardsPage() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.GONE);
        ll_profile_details.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);

        tt.setVisibility(View.GONE);

        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_container_fragment, RewardPointsFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
    }


    private void launchPointsInReview() {
        toolbar.setVisibility(View.VISIBLE);
        tv_page_title.setVisibility(View.GONE);

        ll_profile_details.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);
        header_survey_and_redeem.setVisibility(View.GONE);

        findViewById(R.id.tt).setVisibility(View.VISIBLE);


        profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
        profileName.setGravity(Gravity.CENTER_HORIZONTAL);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_fragment, PointsInReview.newInstance(RewardPointsFragment.rewardsPointsData)).addToBackStack(MYFRAGMENTKEY);
        fragmentTransaction.commit();
        /*RewardPointsModels rewardsPointsData = new ParseJSonObject(HomeActivity.this).getRewardsPoints(new JSONObject(res));
        InformatePreferences.setStringPrefrence(HomeActivity.this), Constants.PREF_AVAILABLEPOINTS_, rewardsPointsData.getAvailablePoints());*/
        txt_points_earned_reward_history1.setText(AvailableSurveyFragment.txt_points_inreview_available.getText().toString());



        Integer n1 = Integer.parseInt(AvailableSurveyFragment.txt_points_inreview_available.getText().toString());


        Log.e("stringTXT", AvailableSurveyFragment.txt_points_inreview_available.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("printinreview", Integer.parseInt(AvailableSurveyFragment.txt_points_inreview_available.getText().toString()));
        myEdit.commit();


    }

    private void dimWindowOutsideDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .2f;
        window.setAttributes(wlp);
        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBadgeInfoOfPanelist();
    }

    @Override
    protected void onStart() {
        super.onStart();

        today = Utility.getTimeStamp();
        static_context = this;
        Constants.FirstAttemp_Metering = true;
    }

    public void backPressEvent() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--) {
                if (fragmentManager.getBackStackEntryAt(i).getName().equals(QUICKPOLLFRAGMENTKEY)) {

                    FlagCommit = true;
                    fragmentManager.popBackStack(QUICKPOLLFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    toolbar.setVisibility(View.GONE);
                    findViewById(R.id.fab_btn).setVisibility(View.VISIBLE);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_fragment, QuickPollQuestionsFragment.newInstance());

                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(SETTINGFRAGMENTKEY)) {

                    FlagCommit = true;
                    fragmentManager.popBackStack(SETTINGFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    phoneLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.fab_btn).setVisibility(View.VISIBLE);

                    profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_fragment, AlertSettingScreenFragment.newInstance());

                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(SETTINGFRAGMENTKEY)) {

                    FlagCommit = true;
                    fragmentManager.popBackStack(SETTINGFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    phoneLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.fab_btn).setVisibility(View.VISIBLE);

                    profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_fragment, AlertSettingScreenFragment.newInstance());
                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(REDEEMFRAGMENTKEY)) {

                    FlagCommit = true;
                    FlagRedeem = true;

                    toolbar.setVisibility(View.VISIBLE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.GONE);

                    fragmentManager.popBackStack(REDEEMFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(REWARDSFRAGMENTKEY)) {
                    FlagCommit = true;
                    fragmentManager.popBackStack(REWARDSFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    toolbar.setVisibility(View.VISIBLE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.GONE);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_container_fragment, RewardPointsFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(SWEEPSTAKEFRAGMENT)) {

                    FlagCommit = true;
                    fragmentManager.popBackStack(SWEEPSTAKEFRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    toolbar.setVisibility(View.VISIBLE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.GONE);
                    header_survey_and_redeem.setVisibility(View.VISIBLE);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.main_container_fragment, sweepstakeFragment).addToBackStack(MYFRAGMENTKEY);

                    break;

                } else if (fragmentManager.getBackStackEntryAt(i).getName().equals(GENERALBUYFRAGMENT)) {

                    FlagCommit = true;
                    fragmentManager.popBackStack(GENERALBUYFRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    toolbar.setVisibility(View.VISIBLE);
                    tv_page_title.setVisibility(View.GONE);
                    ll_profile_details.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                    profileName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.GONE);
                    header_survey_and_redeem.setVisibility(View.VISIBLE);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.main_container_fragment, generalBuyFragment).addToBackStack(MYFRAGMENTKEY);

                    break;

                }
            }
            if (!FlagCommit) {

                fragmentManager.popBackStack(MYFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                toolbar.setVisibility(View.VISIBLE);
                tv_page_title.setVisibility(View.GONE);
                ll_profile_details.setVisibility(View.VISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                profileName.setVisibility(View.VISIBLE);
                email.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                findViewById(R.id.bottom_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.fab_btn).setVisibility(View.VISIBLE);
                profileName.setGravity(Gravity.CENTER_HORIZONTAL);
                profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container_fragment, AvailableSurveyFragment.newInstance());

                //update quickpoll icon visibility.
                callOpinionPollAPI();

                checkDynamicSurvey();
            }

            FlagCommit = false;
            if (!FlagRedeem) {
                fragmentTransaction.commit();
                FlagRedeem = false;
            }
        } else {
            if (PointsInReview.pirCardView != null) {
                if (PointsInReview.pirCardView.getVisibility() == View.VISIBLE)
                    PointsInReview.pirCardView.setVisibility(View.GONE);
                else
                    closeFunc();
            }
            closeFunc();
        }
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

    private void closeFunc() {
        if (lastBackPressTime < System.currentTimeMillis() - 4000) {
            Toast.makeText(this, getResources().getString(R.string.press_again_close), Toast.LENGTH_SHORT).show();
            lastBackPressTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void sendDeviceRegistration() {
        if (Utility.isConnectedToInternet(this)) {
            WebHelper helper = new WebHelper(this);
            Bundle bundle = helper.getRegistrationURLParameter(this, InformatePreferences.getStringPrefrence(this, Constants.EMAILID));

            AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this, Constants.REGISTRATION_URL, 10, bundle, AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {

                @Override
                public void onRequestStarted(int requestCode) {

                }

                @Override
                public void onRequestError(Exception e, int requestCode) {

                }

                @Override
                public void onRequestCompleted(String response, int requestCode) {
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
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location), RC_LOCATION_PERM, LOCATION_PERMS);
        }
    }

    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            backPressEvent();
            findViewById(R.id.tt).setVisibility(View.GONE);
        } else {
            if (!hideChangeLanguage()) {
            }
            if (!isPopFragment) {
                closeFunc();
                isAppClosed = true;
            }
        }
    }

    @Override
    public void setLocaleChange(boolean isLangChanged, String lan_code) {
        InformatePreferences.setStringPrefrence(this, Constants.ISLANGUAGECHANGECALLED, "false");
        recreate();
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

    @Override
    public void onProfilerHandler(ProfilerModels model) {
        Toast.makeText(this, String.valueOf(model.getPercentageOfComplete()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChange(int pageno) {

    }

    @Override
    public void onUIUpdate(int color) {
        // mainCointainer.setBackgroundResource(color);
    }

    @Override
    public void checkSurveyCompleted(boolean isTrue) {

    }

    @Override
    public void fragmentUpdate(int position) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAPTURE_IMAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                    Log.d("", "permission granted success");
                } else {
                    Log.d("", "permission denied");
                }
                return;
            }

        }
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

    public void captureImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.remove_photo)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_photo);
        builder.setItems(options, (dialog, itemPosition) -> {
            switch (itemPosition) {
                case 0:
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CAPTURE_IMAGE);
                        }
                    } else {
                        takePhoto();
                    }
                    break;
                case 1:
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGalleryToChoosePhoto();
                    } else
                        requestStorage();
                    break;
                case 2:
                    _removePhotoApi();
                    break;
            }
        });
        builder.show();
    }

    private void _removePhotoApi() {
        updateImage("");
    }

    public void requestStorage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }
            }
        }
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        uri = Uri.fromFile(f);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", f);
            if (photoUri != null)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            uri = photoUri;
        }
        startActivityForResult(intent, 1);
    }

    private void openGalleryToChoosePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setPicture(requestCode, data);
        }
    }

    private void setPicture(int requestCode, Intent data) {

        if (requestCode == 1) {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            try {
                Bitmap bitmap = null;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                Log.i("info", "onActivityResult");
                checkIfImageRotated(bitmap, f.getAbsolutePath());
                if (uri != null) {
                    base64_1 = base64conversion(ImageCompressor.imageCompression(rotatedBitmap, this));

                } else {
                    base64_1 = "";
                }

                updateImage(base64_1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            if (picturePath != null) {
                Bitmap thumbnail = null;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                thumbnail = BitmapFactory.decodeFile(picturePath, bitmapOptions);
                checkIfImageRotated(thumbnail, picturePath);

                Bitmap scaled = Bitmap.createScaledBitmap(thumbnail, 512, 432, true);

                uri = selectedImage;

                // bitmapImage = thumbnail;

                if (uri != null) {
                    base64_1 = base64conversion(ImageCompressor.imageCompression(rotatedBitmap, this));
                } else {
                    base64_1 = "";
                }
                updateImage(base64_1);
            }
        }
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
            } else if (loginResult.wasCancelled()) {
            } else {
                if (loginResult.getAccessToken() != null) {
                } else {
                }

                setUserInformation();
            }
        }
    }

    private void checkIfImageRotated(Bitmap bitmap, String photoPath) {

        Log.i("info", "checkIfImageRotated");

        ExifInterface ei = null;
        if (photoPath == null)
            return;
        else
            try {
                ei = new ExifInterface(photoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        //log.setText("checkIfImageRotated : Orientation:" + orientation);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                Log.i("info", "checkIfImageRotated 90");
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                Log.i("info", "checkIfImageRotated 180");
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                Log.i("info", "checkIfImageRotated 270");
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                Log.i("info", "checkIfImageRotated NORMAL");
            default:
                Log.i("info", "checkIfImageRotated Default");
                rotatedBitmap = bitmap;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Log.i("info", "rotateImage");
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void setUserInformation() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                phoneNumberStringFromAccountKit = account.getPhoneNumber().getPhoneNumber();

                if (!TextUtils.isEmpty(phoneNumberStringFromAccountKit) && TextUtils.isDigitsOnly(phoneNumberStringFromAccountKit)) {
                    showDialog(true, getString(R.string.dialog_login));
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("UserId", InformatePreferences.getStringPrefrence(HomeActivity.this, Constants.PREF_ID));
                        jsonObject.put("MobileNumber", phoneNumberStringFromAccountKit);
                        requestTypePost(Constants.API_SAVEMOBILE, jsonObject, Constants.REQUESTCODE_SAVEMOBILE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showErrorAlert("", "Invalid phone number");
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });
    }

    private void updateImage(String s) {

        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistId", InformatePreferences.getStringPrefrence(HomeActivity.this, Constants.PREF_ID));
            json.put("imageString", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(HomeActivity.this, Constants.API_UPDATE_IMAGE, json.toString(),
                Constants.REQUEST_UPDATE_IMAGE, AsyncHttpRequest.Type.POST);

        if (Utility.isConnectedToInternet(this)) {

            showDialog(true, getString(R.string.dialog_login));

            mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {
                @Override
                public void onRequestCompleted(String response, int requestCode) {
                    try {
                        dismissDialog();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("Status")) {
                            InformatePreferences.setStringPrefrence(HomeActivity.this, Constants.PREF_FILENAME, jsonObject.getString("FileName"));
                            InformatePreferences.setStringPrefrence(HomeActivity.this, Constants.PREF_FILEPATH, jsonObject.getString("FilePath"));

                            Picasso.get().load(jsonObject.getString("FilePath")).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).
                                    placeholder(R.drawable.ic_profile).transform(new CircleTransform()).into(profileImage);

                            if (!s.isEmpty())
                                Toast.makeText(HomeActivity.this, R.string.profile_update_success, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(HomeActivity.this, R.string.delete_profile_picture, Toast.LENGTH_SHORT).show();
                        } else {
                            showErrorAlert("", jsonObject.getString("Message"));
                        }

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
        } else {
            dismissDialog();

            showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
        }
    }

    private String base64conversion(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    protected void showErrorAlert(String title, String message) {
        if (this != null) {
            DialogFragment fragment = AlertDialogErrorFragment.newInstance("", message);
            fragment.show(getSupportFragmentManager(), "");
        }
    }

    protected void showDialog(boolean isShow, String message) {

        if (isShow) {
            if (mProgressDialog == null)
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception ignored) {

            }
            mProgressDialog = null;
        }
    }

    protected synchronized void requestTypePost(String url, JSONObject jsonobject, int requestCode) {

        Log.d("requestCodekk1:","requestCodekk1:"+url);
        Log.d("requestCodekk11:","requestCodekk11:"+ jsonobject.toString());
        Log.d("requestCodekk111:","requestCodekk111:"+ requestCode);

        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this, url, jsonobject.toString(), requestCode, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onRequestCompleted(String response, int requestCode) {
        Log.d("requestCodekk1111:","requestCodekk1111:"+requestCode);
        Log.d("requestCodekk11111:","requestCodekk11111:"+response);
        dismissDialog();
        switch (requestCode) {
            case REQUEST_GET_OPINION_POLL_QUESTIONS:
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray != null && jsonArray.length() > 0) {
                        mQuickPollIcon.setVisibility(View.VISIBLE);
                    } else {
                        mQuickPollIcon.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case REQUEST_GET_DYNAMIC_SURVEY:
                JSONObject dynamicJobject = null;
                try {
                    dynamicJobject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean dynamicStatus = dynamicJobject.optBoolean("Status");
                String dynamicMessage = dynamicJobject.optString("Message");
                if (dynamicStatus) {
                    //Toast.makeText(static_context, "Dynamic Survey is present", Toast.LENGTH_SHORT).show();
                    openDialog(dynamicMessage);
                }/*else {
                    showErrorAlert("", dynamicMessage);
                }*/
                break;

            case REQUEST_GET_DYNAMIC_SURVEY_LINK:
                //launchDynamicSurveyPage("https://qa.thepanelstation.com/Survey/ID=5D86188E6F760883526D435BE423D1FB4774B36C7AF117BB55B0A2DFFA15CA19/SRC=9");
                JSONObject dynamicLJobject = null;
                try {
                    dynamicLJobject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //boolean dynamicLStatus = dynamicLJobject.optBoolean("Status");
                String dynamicLMessage = dynamicLJobject.optString("Message");
                String dynamicLStatusId = dynamicLJobject.optString("StatusId");
                if (dynamicLStatusId.equals("200") && !TextUtils.isEmpty(dynamicLMessage)) {
                    launchDynamicSurveyPage(dynamicLMessage);
                } else {
                    showErrorAlert("", dynamicLMessage);
                }
                break;

            case REQUEST_CHANGE_PASSWORD:
                JSONObject jobject = null;
                try {
                    jobject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean statusFlag = jobject.optBoolean("Status");
                String message = jobject.optString("Message");
                if (statusFlag) {
                    if (changePasswordDialog != null && changePasswordDialog.isShowing()) {
                        changePasswordDialog.dismiss();
                        if (!fromSettings)
                            _continueNormalFlow();
                    }
                    showErrorAlert("", message);
                } else {
                    showErrorAlert("", message);
                }

                break;

            case Constants.REQUESTCODE_SAVEMOBILE:
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("Status")) {
                        showErrorAlert("", object.optString("Message"));
                        InformatePreferences.putBoolean(this, Constants.PREF_MOBILENUMBERVERIFIED, true);
                        InformatePreferences.setStringPrefrence(this, Constants.PREF_MOBILENUMBER, phoneNumberStringFromAccountKit);
                        phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_ISDCODE),
                                InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER)));

                    } else {
                        showErrorAlert(" ", object.optString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_CHECK_VERSION_CODE:
                try {
                    Log.d("tpsappupdate11:","tpsappupdate11"+response);

                    JSONObject jsonObject = new JSONObject(response);

                    String dobstr = jsonObject.getString("DOB");
                    Log.d("dob:1","dob:1"+dobstr);
                    SharedPreferences sharedPreferences1 = context.getSharedPreferences("DATEOFBIRTH", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("DOB", dobstr);
                    editor1.commit();


                    String mobilenoupdate = jsonObject.getString("MobileNoUpdate");
                    Log.d("mobilenoupdate:1","mobilenoupdate:1"+mobilenoupdate);
                    SharedPreferences sharedPreferences11 = context.getSharedPreferences("MOBILENOUPDATE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor11 = sharedPreferences11.edit();
                    editor11.putString("MOBILENOUPDATE", mobilenoupdate);
                    editor11.commit();


                    sharedPreferences12 = context.getSharedPreferences("MOBILENOUPDATE", Context.MODE_PRIVATE);

                    String profile_mobienoupdate = sharedPreferences12.getString("MOBILENOUPDATE", "");

                    Log.d("profile_mobilenoupdate:","profile_mobilenoupdate:"+profile_mobienoupdate);

                    if(profile_mobienoupdate.equals("true")){
                        edit_ph_number.setVisibility(View.VISIBLE);
                    }else if(profile_mobienoupdate.equals("false")) {
                        edit_ph_number.setVisibility(View.GONE);
                    }

                    boolean IsUserActive = jsonObject.optBoolean("IsUserActive");

                    InformatePreferences.putInt(getApplicationContext(), Constants.PREF_MARKET_ID, jsonObject.getInt("MarketId"));

                    if (IsUserActive) {
                        _checkIfUpdateRequired(jsonObject);
                    } else {
                        ErrorMsg = jsonObject.optString("ErrorMessage");
                        if (ErrorMsg.equals("")) {
                            ErrorMsg = getResources().getString(R.string.user_msg);
                        }
                        new AlertDialog.Builder(this)
                                .setTitle(R.string.alert).setMessage(ErrorMsg)
                                .setPositiveButton("Exit", (dialog, which) ->
                                {
                                    SharedPreferences sharedPreferences = getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(this, SignUpActivity.class);
                                    startActivity(intent);
                                    finish();

                                })
                                .setCancelable(false)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUEST_UPDATE_PHONE_NUMBER:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("Status") && jsonObject.optBoolean("Status")) {
                        InformatePreferences.setStringPrefrence(this, Constants.PREF_MOBILENUMBER, temp_phone_number);
                        // phoneNum.setText(InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER));
                        phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_ISDCODE), InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER)));
                        showErrorAlert(" ", jsonObject.optString("Message"));
                    } else {
                        showErrorAlert(" ", jsonObject.optString("Message"));
                        temp_phone_number = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_SURVEY_BADGE_BY_PANELIST_ID:
                try {
                    //{"BadgeName":"Silver","Points":250,"Status":true,"Message":"","HelpdeskEmailId":"","StatusId":0,"IsUserActive":true}
                    JSONObject object = new JSONObject(response);
                    if (object != null && object.has("BadgeName")) {
                        //"BadgeName": "Blue Member", Blue Member,Bronze,Silver,Gold,Platinum
                        String badge = object.getString("BadgeName");
                        if (TextUtils.isEmpty(badge)) return;

                        if (badge.equals("Blue Member")) {
                            //northing to do
                        } else if (badge.equals("Bronze")) {
                            ((ImageView) findViewById(R.id.user_badge)).setImageResource(R.drawable.bronze);
                            showBadgeIncreaseDialog(object, badge);
                        } else if (badge.equals("Silver")) {
                            ((ImageView) findViewById(R.id.user_badge)).setImageResource(R.drawable.silver);
                        } else if (badge.equals("Gold")) {
                            ((ImageView) findViewById(R.id.user_badge)).setImageResource(R.drawable.gold);
                        } else if (badge.equals("Platinum")) {
                            ((ImageView) findViewById(R.id.user_badge)).setImageResource(R.drawable.platinum);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void _showChangePasswordDialog(boolean isFromSettings) {

        changePasswordDialog = new Dialog(this, R.style.Theme_Dialog);
        changePasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changePasswordDialog.setCancelable(false);
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        changePasswordDialog.setCanceledOnTouchOutside(false);
        Window window = changePasswordDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        changePasswordDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        TextView tv_old_password = changePasswordDialog.findViewById(R.id.tv_old_password);
        TextView tv_new_password = changePasswordDialog.findViewById(R.id.tv_new_password);
        TextView tv_confirm_password = changePasswordDialog.findViewById(R.id.tv_confirm_password);
        TextView tv_expiry_message_lable = changePasswordDialog.findViewById(R.id.expiry_msg);
        _setAsterisk(tv_old_password);
        _setAsterisk(tv_new_password);
        _setAsterisk(tv_confirm_password);

        EditText et_old_password = changePasswordDialog.findViewById(R.id.et_old_password);
        EditText et_new_password = changePasswordDialog.findViewById(R.id.et_new_password);
        EditText et_confirm_password = changePasswordDialog.findViewById(R.id.et_confirm_password);

        disablePaste(et_old_password);
        disablePaste(et_new_password);
        disablePaste(et_confirm_password);

        ImageView closeDialog = changePasswordDialog.findViewById(R.id.close_dialog_change_pw);

        setMessage(tv_expiry_message_lable);

        if (!isFromSettings) {
            closeDialog.setVisibility(View.INVISIBLE);
            tv_expiry_message_lable.setVisibility(View.VISIBLE);
        }
        closeDialog.setOnClickListener(v -> {
            Utility.hideKeyboard(v, this);
            changePasswordDialog.dismiss();
            if (!isFromSettings) {
                _continueNormalFlow();
            }
        });

        Button btn_reset = changePasswordDialog.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(v -> {
            et_old_password.requestFocus();
            et_old_password.setText("");
            et_new_password.setText("");
            et_confirm_password.setText("");
        });

        Button btn_submit = changePasswordDialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> {
            String oldPassword = et_old_password.getText().toString();
            String newPassword = et_new_password.getText().toString();
            String confirmPassword = et_confirm_password.getText().toString();
            if (!TextUtils.isEmpty(oldPassword))
                if (!TextUtils.isEmpty(newPassword))
//                    if (newPassword.length() >= 6 && newPassword.length() < 16)
                    if (!newPassword.equals(oldPassword))
                        if (!TextUtils.isEmpty(confirmPassword))
                            if (newPassword.equals(confirmPassword)) {
                                Utility.hideKeyboard(v, this);
                                HomeActivity.this._callChangePasswordApi(oldPassword, newPassword);
                            } else
                                et_confirm_password.setError(getString(R.string.password_comapre_mismatch));
                        else
                            et_confirm_password.setError(getString(R.string.enter_confirm_password));
                    else
                        et_new_password.setError(getString(R.string.new_old_password_match_error));
                    /*else
                        et_new_password.setError(getString(R.string.new_password_length));*/
                else et_new_password.setError(getString(R.string.new_password));
            else et_old_password.setError(getString(R.string.current_password));
        });

        changePasswordDialog.show();
    }

    private void disablePaste(EditText et_old_password) {
        et_old_password.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() > 1) {
                    return "";
                }
                return null;
            }
        }});
    }

    private void setMessage(TextView message) {
        if (isNewlySignedIn) {
            message.setText(getResources().getString(R.string.please_change_your_password));
        } else {
            message.setText(getResources().getString(R.string.password_expiry_message));
        }
    }


    private void _callChangePasswordApi(String oldPassword, String newPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", InformatePreferences.getStringPrefrence(this, Constants.PREF_ID));
            jsonObject.put("OldPassword", oldPassword);
            jsonObject.put("NewPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.API_CHANGE_PASSWORD, jsonObject, REQUEST_CHANGE_PASSWORD);
    }

    private void _setAsterisk(TextView textView) {
        SpannableString ss = new SpannableString(textView.getText());
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }


    private void _checkIfUpdateRequired(JSONObject jsonObject) throws JSONException {
        if (jsonObject != null && !TextUtils.isEmpty(jsonObject.getString("LanguageCulture"))) {
            Utility.setLocaleLanguage(jsonObject.getString("LanguageCulture"), this);

            Log.d("languageHOME:","languageHOME:"+jsonObject.getString("LanguageCulture"));


        }
        //TandCLink
        if (jsonObject != null && !TextUtils.isEmpty(jsonObject.getString("TandCLink"))) {
            InformatePreferences.setStringPrefrence(this, Constants.PREF_TNC, jsonObject.optString("TandCLink"));
        }

        InformatePreferences.setStringPrefrence(HomeActivity.this,
                Constants.PREF_REFFERELCODE, jsonObject.optString("ReferralCode"));
        //check if new User
        if (jsonObject != null && jsonObject.has("NewlySignedIn"))
            isNewlySignedIn = jsonObject.optBoolean("NewlySignedIn");
        //get the notificatiopn count
        int notifCount = 0;
        if (jsonObject != null && jsonObject.has("NotificationCount"))
            notifCount = jsonObject.getInt("NotificationCount");

        if (notifCount != 0) {
            tv_survey_count.setVisibility(View.VISIBLE);
            tv_survey_count.setText(String.valueOf(notifCount));
        } else {
            tv_survey_count.setVisibility(View.INVISIBLE);
        }
        //check if password is expired
        if (jsonObject != null && jsonObject.has("IsPasswordExpired"))
            isChangePasswordRequired = jsonObject.optBoolean("IsPasswordExpired");
        if (jsonObject.optString("UpdateTheApp").equalsIgnoreCase("true")) {
            String isMandatoryUpdate = jsonObject.optString("IsMandatory");

            if (!isMandatoryUpdate.equalsIgnoreCase("true")) {
                //partial update
                String[] arr = jsonObject.optString("FeatureDetails").split("\\|");
                // check if user has already canceled partial update dialog
                if (InformatePreferences.getBoolean(this, Constants.PREF_IS_PARTIAL_FIRST_CLICKED_CANCEL, true)) {
                    _showCustomPartialUpdateDialog(arr);
                } else {
                    _continueNormalFlow();
                }
            } else {
                // mandatory update
                new AlertDialog.Builder(this)
                        .setTitle(R.string.msg_update_available).setMessage(R.string.msg_update_app_2)
                        .setPositiveButton("Update", (dialog, which) -> {
                            _goToPlayStoreToUpdateAppAndExit();
                        })
                        .setCancelable(false)
                        .show();
            }
        } else { // no update available
            if (isChangePasswordRequired) {
                showChangePasswordPage();
            } else {
                _continueNormalFlow();
            }
        }
    }

    private void showChangePasswordPage() {
        fabButton.setVisibility(View.GONE);
        lyt_change_password.setVisibility(View.VISIBLE);
    }

    private void hideChangePasswordPage() {
        fabButton.setVisibility(View.VISIBLE);
        lyt_change_password.setVisibility(View.GONE);
        _showChangePasswordDialog(false);
    }

    private void showBadgeIncreaseDialog(JSONObject object, String badge) {
        // Bronze->Silver->Gold->Platinum
        if (object.has("Points")) {
            try {
                if (checkBadgeIncreased(badge)) {
                    String newBadge;

                    switch (badge) {
                        case "Platinum":
                            newBadge = getString(R.string.legend);
                            break;
                        case "Gold":
                            newBadge = getString(R.string.grand_master);
                            break;
                        case "Silver":
                            newBadge = getString(R.string.master);
                            break;
                        case "Bronze":
                            newBadge = getString(R.string.sensei);
                            break;
                        default:
                            newBadge = getString(R.string.newbei);
                    }

                    int points = object.getInt("Points");
                    DialogFragment fragment = AlertDialogErrorFragment.newInstance("", String.format(getString(R.string.badge_increase), newBadge, String.valueOf(points)));
                    fragment.show(getSupportFragmentManager(), "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean checkBadgeIncreased(String newBadge) {
        String oldBadge = InformatePreferences.getStringPrefrence_Long(this, Constants.PREF_BADGE_NAME);
        if (newBadge.equalsIgnoreCase("Blue Member")) {
            InformatePreferences.setStringPrefrence(this, Constants.PREF_BADGE_NAME, newBadge);
            return false;
        } else if (!TextUtils.isEmpty(oldBadge)) {
            int oldBadgeNumber = getBadgeNumber(oldBadge);
            int newBadgeNumber = getBadgeNumber(newBadge);
            if (newBadgeNumber > oldBadgeNumber) {
                InformatePreferences.setStringPrefrence(this, Constants.PREF_BADGE_NAME, newBadge);
                return true;
            }
        }
        InformatePreferences.setStringPrefrence(this, Constants.PREF_BADGE_NAME, newBadge);
        return false;
    }

    private int getBadgeNumber(String badge) {
        if ("Bronze".equalsIgnoreCase(badge)) {
            return 1;
        } else if ("Silver".equalsIgnoreCase(badge)) {
            return 2;
        } else if ("Gold".equalsIgnoreCase(badge)) {
            return 3;
        } else if ("Platinum".equalsIgnoreCase(badge)) {
            return 4;
        }
        return 0;
    }

    private void _continueNormalFlow() {
        lyt_notification.setVisibility(View.VISIBLE);

        //callopinion poll api
        callOpinionPollAPI();

        checkDynamicSurvey();

        // false, normal flow
        fragmentTransaction = fragmentManager.beginTransaction();
        if (getIntent() != null) {
            int navigationFromNotification = getIntent().getIntExtra(Constants.CHOOSETABS, -1);
            switch (navigationFromNotification) {
                case 1: // transact AvailableSurvey fragment
                    Log.e(TAG, "AvailableSurveyFragment");
                    fragmentTransaction.replace(R.id.main_container_fragment, AvailableSurveyFragment.newInstance());
                    break;
                case 2: // transact RewardPointsFragment fragment
                    Log.e(TAG, "RewardPointsFragment");
                    fragmentTransaction.replace(R.id.main_container_fragment, RewardPointsFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
                    break;
                case 0: // transact ProfilerFragment fragment
                    Log.e(TAG, "ProfilerFragment");
                    fragmentTransaction.replace(R.id.main_container_fragment, ProfilerFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
                    break;
                default: // NotificationType not found(i.e. -1), proceed normally and transact AvailableSurveyFragment fragment
                    fragmentTransaction.replace(R.id.main_container_fragment, AvailableSurveyFragment.newInstance());
                    break;
            }
            try {
                fragmentTransaction.commit();

            } catch (Exception E) {
                E.printStackTrace();
            }
        }
    }

    private void _goToPlayStoreToUpdateAppAndExit() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void _showCustomPartialUpdateDialog(String[] arr) {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_partial_update);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dimWindowOutsideDialog(dialog);
        TextView tv_update_text_1 = dialog.findViewById(R.id.tv_update_text_1);
        RecyclerView rv_features = dialog.findViewById(R.id.rv_features);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_update = dialog.findViewById(R.id.btn_update);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                strings.add(arr[i].trim());
            }
        }
        if (strings != null && !strings.isEmpty()) {
            tv_update_text_1.setText(arr[0]);
            rv_features.setAdapter(new FeatureAdapter(strings));
            rv_features.setLayoutManager(new LinearLayoutManager(this));
        }

        btn_cancel.setOnClickListener(view -> {
            dialog.dismiss();
            InformatePreferences.putBoolean(this, Constants.PREF_IS_PARTIAL_FIRST_CLICKED_CANCEL, false);
            if (isChangePasswordRequired) {
                showChangePasswordPage();
            } else {
                _continueNormalFlow();
            }
        });

        btn_update.setOnClickListener(view -> _goToPlayStoreToUpdateAppAndExit());

        dialog.show();

    }

    @Override
    public void onRequestError(Exception e, int requestCode) {

    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

    public void logEVENT_NAME_COMPLETED_REGISTRATIONEvent() {


        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        logger.logEvent("EVENT_NAME_COMPLETED_REGISTRATION");
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void callPasswordChange(boolean isFromSettings) {
        _showChangePasswordDialog(isFromSettings);
        fromSettings = isFromSettings;
    }


    public void launchQuickPollFragment() {
        findViewById(R.id.toolbar_main).setVisibility(View.GONE);
        FragmentTransaction transaction = null;
        QuickPollQuestionsFragment commPrefFragment = QuickPollQuestionsFragment.newInstance();
        if (commPrefFragment != null) {
            if (!commPrefFragment.isVisible()) {
                //getView().findViewById(R.id.setting_layout).setVisibility(View.GONE);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container_fragment, commPrefFragment).addToBackStack(MYFRAGMENTKEY);

                transaction.commit();
            }
        }
    }


    private void callOpinionPollAPI() {
        //showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_ID));
            jsonObject.put("MarketId", InformatePreferences.getMarketId(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_OPINIONPOLLSQUESTIONS, jsonObject, REQUEST_GET_OPINION_POLL_QUESTIONS);
    }

    private void checkDynamicSurvey() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(this, Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this
                , Constants.API_GET_DYNAMIC_SURVEY
                , jsonObject.toString()
                , Constants.REQUEST_GET_DYNAMIC_SURVEY
                , AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    private void callDynamicSurveyLink() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_DYNAMIC_SURVEY_LINK, jsonObject, REQUEST_GET_DYNAMIC_SURVEY_LINK);
    }

    /*private void callDynamicSurveyLink() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(this, Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this
                , Constants.API_GET_DYNAMIC_SURVEY_LINK
                , jsonObject.toString()
                , REQUEST_GET_DYNAMIC_SURVEY_LINK
                , AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }*/

    private void openDialog(String message) {
        String[] arrOfStr = message.split(" #### ");
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView = this.getLayoutInflater().inflate(R.layout.dialog_dynamic_survey, null);
        ImageView btn_close = layoutView.findViewById(R.id.iv_close_error);
        Button btn_participate = layoutView.findViewById(R.id.btn_participate);
        TextView dynamic_header = layoutView.findViewById(R.id.dynamic_header);
        TextView dynamic_content = layoutView.findViewById(R.id.dynamic_content);
        dynamic_header.setText(arrOfStr[0]);
        dynamic_content.setText(arrOfStr[1]);
        btn_participate.setText(arrOfStr[2]);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        btn_participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDynamicSurveyLink();
                alertDialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }
}
