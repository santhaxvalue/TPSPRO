package com.panelManagement.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.gigamole.infinitecycleviewpager.VerticalInfiniteCycleViewPager;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.panelManagement.fragment.LoginFragment;
import com.panelManagement.fragment.SignupProfileInfoFragment;
import com.panelManagement.fragment.SocialSignupFragment;
import com.panelManagement.listener.OnClickAction;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;
import com.panelManagement.utils.WebHelper;
import com.panelManagement.view.VerticalCarouselAdapter;
import com.panelManagement.webservices.AsyncHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.panelManagement.utils.PanelConstants.LOCATION_PERMS;
import static com.panelManagement.utils.PanelConstants.RC_LOCATION_PERM;

@SuppressLint("SetJavaScriptEnabled")
public class SignUpActivity extends FragmentActivity implements OnClickAction, EasyPermissions.PermissionCallbacks, AsyncHttpRequest.RequestListener/*, OnInfiniteCyclePageTransformListener */ {
    VerticalCarouselAdapter verticalCarouselAdapter;
    // public static ArrayList<CountryModel> countryList = new
    // ArrayList<CountryModel>();
    static SharedPreferences pref;
    UserInfoModel userInfo = null;
    Context context;
    int FlagPosition = 0;
    VerticalInfiniteCycleViewPager recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signupcontainer);

        context = SignUpActivity.this;

        recyclerView = findViewById(R.id.login_carasol);

        if (getIntent() != null) {
            verticalCarouselAdapter = new VerticalCarouselAdapter(context, getSupportFragmentManager(), getIntent().getStringExtra(Constants.PANEL_EMAIL_ID),
                    getIntent().getStringExtra("message"));
        } else {
            verticalCarouselAdapter = new VerticalCarouselAdapter(context, getSupportFragmentManager(), "", "");
        }

        recyclerView.setAdapter(verticalCarouselAdapter);
        OnInfiniteCyclePageTransformListener test = new OnInfiniteCyclePageTransformListener() {
            @Override
            public void onPreTransform(View page, float position) {

            }

            @Override
            public void onPostTransform(View page, float position) {

                if (LoginFragment.signuplable != null && LoginFragment.login_socialmedia_layout != null) {
                    if (page.getId() == R.id.login_main_layout) {
                        LoginFragment.signuplable.setVisibility(View.GONE);
                        LoginFragment.login_socialmedia_layout.setVisibility(View.VISIBLE);
                    } else {
                        LoginFragment.signuplable.setVisibility(View.VISIBLE);
                        LoginFragment.login_socialmedia_layout.setVisibility(View.GONE);
                    }
                }

            }
        };
        recyclerView.setOnInfiniteCyclePageTransformListener(test);
        // recyclerView.setOnInfiniteCyclePageTransformListener(this);

        recyclerView.setScrollDuration(1500);

        PanelConstants.DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.ic_tabview_container, getSelectedFragment()).commit();
        }*/

        _startDifferentScenariosAfterPermissionsGranted();
        //panel
        /*try {
            GCMBinding(this);
		} catch (Exception e) {
			Toast.makeText(this,"Please login to Google Account for PushNotifications",Toast.LENGTH_LONG).show();
		}*/
        //SourceEdge
        //  Utility.requestAccounts(SignUpActivity.this);
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        hide.setDuration(1000);
        recyclerView.setAnimation(hide);

        //check for app update
        checkForAppUpdate();
    }

    private void checkForAppUpdate() {
        if (Utility.isConnectedToInternet(this)) {

            //create request object
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("InstalledVersionCode", String.valueOf(BuildConfig.VERSION_CODE));
                jsonObject.put("AppDeviceTypeID", "2");

                Log.d("version_params:","version_params:"+jsonObject.toString());

//                Log.d("version_params1:","version_params1:"+https://surveys-api.thepanelstation.com/api/psapi/TPSAppUpdate);

                //appUpdate api call
                AsyncHttpRequest mAppRequest = new AsyncHttpRequest(this, Constants.API_TPSAppUpdate, jsonObject.toString(), Constants.REQUEST_CHECK_VERSION_CODE, AsyncHttpRequest.Type.POST);
                mAppRequest.setRequestListener(this);
                mAppRequest.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private void _startDifferentScenariosAfterPermissionsGranted() {

        if (hasLocationPermissions()) {
            // Have permissions, do the thing!
            Utility.generateKeyHash(this);
            String value = Locale.getDefault().getLanguage();
            Locale locale = new Locale(value);
            Locale.setDefault(locale);
            setUserLanguage();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location),
                    RC_LOCATION_PERM,
                    LOCATION_PERMS);
        }
    }

    private boolean hasLocationPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_PERMS);
    }

    public void GCMBinding(Context context) throws Exception {
        GCMRegistrar.checkDevice(context);
        if (PanelConstants.DEBUG)
            Log.d("GCM", GCMRegistrar.getRegistrationId(context));
        GCMRegistrar.checkManifest(context);
        String regId = GCMRegistrar.getRegistrationId(context);
        if (regId.equals("")) {
            GCMRegistrar.register(context, PanelConstants.GCM_SENDER_ID);

        } else {
            if (PanelConstants.DEBUG)
                Log.d("GCM", "Already registered");
            PanelConstants.GCM_REG_ID = regId;
            if (PanelConstants.DEBUG)
                Log.d("Tag", "Device Id" + PanelConstants.GCM_REG_ID);
            GCMRegistrar.setRegisteredOnServer(context, true);
        }
    }

    public void setUserLanguage() {
        try {
            int localeID = pref.getInt("locale_language", 0);
            if (localeID > 0) {
                Locale locale = new Locale("en");
                switch (localeID) {
                    case Constants.PORTUGEESE:
                        locale = new Locale("pt", "BR");
                        break;
                    case Constants.RUSSIAN:
                        locale = new Locale("ru");
                        break;
                    case Constants.SIMPLIFIED_CHINESE:
                        locale = new Locale("zh", "CN");
                        break;
                    case Constants.TRADITIONAL_CHINESE:
                        locale = new Locale("zh", "CH");
                        break;
                    case Constants.MEXICAN_SPANISH:
                        locale = new Locale("es", "MX");
                        break;
                    case Constants.ARGENTINIAN_SPANISH:
                        locale = new Locale("es", "AR");
                        break;
                    case Constants.CHILE_SPANISH:
                        locale = new Locale("es", "CL");
                        break;
                    case Constants.BAHASA_INDONESIA:
                        locale = new Locale("in");
                        break;
                    case Constants.TURKISH:
                        locale = new Locale("tr");
                        break;
                    case Constants.KOREAN:
                        locale = new Locale("ko", "KR");
                        break;
                    case Constants.POLISH:
                        locale = new Locale("pl");
                        break;
                    case Constants.THI:
                        locale = new Locale("th");
                        break;
                    case Constants.COLOMBIAN_SPANISH:
                        locale = new Locale("es", "CO");
                        break;
                    case Constants.TAGALOG:
                        locale = new Locale("tl");
                        break;
                    case Constants.VIETNAMESE:
                        locale = new Locale("vi");
                        break;
                    case Constants.MALAYSIA:
                        locale = new Locale("ms", "MY");
                        break;
                    case Constants.HONKONG:
                        locale = new Locale("zh", "CH");
                        break;
                    case Constants.GERMANY:                     //added by Niyaj 23-08-2017
                        locale = new Locale("de", "DE");
                        break;
                    case Constants.FRANCE:
                        locale = new Locale("fr", "FR");
                        break;
                    case Constants.ITALIAN:
                        locale = new Locale("it");
                        break;
                }

               /* if (localeID == Constants.PORTUGEESE) {
                    locale = new Locale("pt", "BR");
                } else if (localeID == Constants.RUSSIAN) {
                    locale = new Locale("ru");
                } else if (localeID == Constants.SIMPLIFIED_CHINESE) {
                    locale = new Locale("zh", "CN");
                } else if (localeID == Constants.TRADITIONAL_CHINESE) {
                    locale = new Locale("zh", "CH");
                } else if (localeID == Constants.TRADITIONAL_CHINESE) {
                    locale = new Locale("zh");
                } else if (localeID == Constants.MEXICAN_SPANISH) {
                    locale = new Locale("es", "MX");
                } else if (localeID == Constants.ARGENTINIAN_SPANISH) {
                    locale = new Locale("es", "AR");
                } else if (localeID == Constants.CHILE_SPANISH) {
                    locale = new Locale("es", "CL");
                } else if (localeID == Constants.BAHASA_INDONESIA) {
                    locale = new Locale("in");
                } else if (localeID == Constants.TURKISH) {
                    locale = new Locale("tr");
                } else if (localeID == Constants.KOREAN) {
                    locale = new Locale("ko", "KR");
                } else if (localeID == Constants.POLISH) {
                    locale = new Locale("pl");
                } else if (localeID == Constants.THI) {
                    locale = new Locale("th");
                } else if (localeID == Constants.CHILE_SPANISH) {
                    locale = new Locale("th");
                } else if (localeID == Constants.COLOMBIAN_SPANISH) {
                    locale = new Locale("es", "CO");
                } else if (localeID == Constants.TAGALOG) {
                    locale = new Locale("tl");
                }*/
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void setDefaultLanguage(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    @Override
    public void onClickView(View v) {
    }

    @Override
    public void onBackPressed() {
        int currentFragmnet = 0;
        if (currentFragmnet == 0)
            super.onBackPressed();
        else {
            if (SocialSignupFragment.alredyOTP || currentFragmnet == 1)
                recyclerView.setCurrentItem(1);
            else
                return;
        }
    }

    @Override
    public void SocialInfo(UserInfoModel data) {
        this.userInfo = data;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SignupProfileInfoFragment mFragment = new SignupProfileInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.SIGNUP_KEY_USERINFO, userInfo);
        mFragment.setArguments(bundle);
        transaction.replace(R.id.ic_tabview_container, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            //   Toast.makeText(this, "here" + String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if ((requestCode == LoginFragment.RC_SIGN_IN)) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.ic_tabview_container);
            if (fragment instanceof SocialSignupFragment)
                fragment.onActivityResult(requestCode, resultCode, intent);
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

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        switch (requestCode) {
            case Constants.REQUEST_CHECK_VERSION_CODE:
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    boolean IsUserActive = jsonObject.optBoolean("IsUserActive");

                    Log.d("response2323:","response2323:"+jsonObject.toString());

                    _checkIfUpdateRequired(jsonObject);

                } catch (Exception e) {

                }
        }
    }

    private void _checkIfUpdateRequired(JSONObject jsonObject) throws JSONException {

        String installedVersion = jsonObject.optString("InstalledVersionCode");
        String playStoreVersion = jsonObject.optString("PlayStoreVersionCode");

        Log.d("version1:","version1:"+installedVersion);
        Log.d("version11:","version11:"+playStoreVersion);

        if (jsonObject.optString("UpdateTheApp").equalsIgnoreCase("true")) {
            String isMandatoryUpdate = jsonObject.optString("IsMandatory");

            if (!isMandatoryUpdate.equalsIgnoreCase("true")) {
                //partial update
                String[] arr = jsonObject.optString("FeatureDetails").split("\\|");
                // check if user has already canceled partial update dialog
                if (InformatePreferences.getBoolean(this, Constants.PREF_IS_PARTIAL_FIRST_CLICKED_CANCEL, true)) {
                    _showCustomPartialUpdateDialog(arr);
                }
            } else {
                // mandatory update
                mandatoryUpdate();
            }
        } else if (Integer.parseInt(installedVersion) < Integer.parseInt(playStoreVersion)) {
            // mandatory update
            mandatoryUpdate();
        }
    }

    private void mandatoryUpdate() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.msg_update_available).setMessage(R.string.msg_update_app_2)
                .setPositiveButton("Update", (dialog, which) -> {
                    _goToPlayStoreToUpdateAppAndExit();
                })
                .setCancelable(false)
                .show();
    }

    private void _showCustomPartialUpdateDialog(String[] arr) {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_partial_update);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

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
        });

        btn_update.setOnClickListener(view -> _goToPlayStoreToUpdateAppAndExit());

        dialog.show();
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

    @Override
    public void onRequestError(Exception e, int requestCode) {

    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

  /*  @Override
    public void onPreTransform(View page, float position) {

    }

    @Override
    public void onPostTransform(View page, float position) {
        if (page != null) {
            Log.e(page.toString(), String.valueOf(position));

        //    int i = verticalCarouselAdapter.getCurrentPosition();
        //    SocialSignupFragment.newInstance(i);
        }
    }
*/

    /* public void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }*/
}
