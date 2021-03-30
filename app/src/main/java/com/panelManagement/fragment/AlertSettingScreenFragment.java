package com.panelManagement.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.listener.OnChangePasswordCallListener;
import com.panelManagement.model.IdReason;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.panelManagement.utils.Constants.REQUEST_CHANGE_PASSWORD;
import static com.panelManagement.utils.Constants.REQUEST_GET_UNSUBSCRIBE_REASONS;
import static com.panelManagement.utils.Constants.REQUEST_LOGOUT;

public class AlertSettingScreenFragment extends BaseFragment implements OnClickListener {

    ScrollView relativeLayout;
    private Context mContext;
    private Activity mActivity;
    private Dialog changePasswordDialog;
    private OnChangePasswordCallListener listener;

    public static AlertSettingScreenFragment newInstance() {
        return new AlertSettingScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        HomeActivity.setSurvayBackground();
        mActivity.findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        mActivity.findViewById(R.id.iv_back_left_arrow).setClickable(true);
        mActivity.findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, Utility.getDp(context, 36));
        layout.setLayoutParams(params);

        view.findViewById(R.id.btn_changelanguage).setOnClickListener(this);
        view.findViewById(R.id.btn_PrivacyPolicy).setOnClickListener(this);
        view.findViewById(R.id.btn_communication_pref).setOnClickListener(this);
        view.findViewById(R.id.btn_tnc).setOnClickListener(this);
        view.findViewById(R.id.facebook_layout).setOnClickListener(this);
        view.findViewById(R.id.twitter_layout).setOnClickListener(this);
//        view.findViewById(R.id.google_layout).setOnClickListener(this);
        view.findViewById(R.id.btn_change_password).setOnClickListener(this);
        view.findViewById(R.id.btn_unsubscribe).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        relativeLayout = view.findViewById(R.id.setting_layout);
        Animation hide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);
        return view;
    }

    @Override
    public void onClick(View arg0) {
        DialogFragment fragment = null;
        FragmentTransaction transaction = null;

        switch (arg0.getId()) {
            case R.id.facebook_layout:
                _openSocialLink(InformatePreferences.getStringPrefrence(context, Constants.PREF_FB_LINK_SOCIAL_CONNECT));
                break;
            case R.id.twitter_layout:
                _openSocialLink(InformatePreferences.getStringPrefrence(context, Constants.PREF_TWITTER_LINK_SOCIAL_CONNECT));
                break;
//            case R.id.google_layout:
//                _openSocialLink(InformatePreferences.getStringPrefrence(context, Constants.PREF_GOOGLELINK_SOCIAL_CONNECT));
//                break;
            case R.id.btn_changelanguage:
                mActivity.findViewById(R.id.toolbar_main).setVisibility(View.GONE);
                AlertChangeLanguageFragment mFragment = AlertChangeLanguageFragment.newInstance();
                if (mFragment != null) {
                    if (!mFragment.isVisible()) {
                        getView().findViewById(R.id.setting_layout).setVisibility(View.GONE);
                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
                        transaction.replace(R.id.main_container_fragment, mFragment).addToBackStack(HomeActivity.SETTINGFRAGMENTKEY);

                        transaction.commit();
                    }
                }
                break;

            case R.id.btn_communication_pref:
                mActivity.findViewById(R.id.toolbar_main).setVisibility(View.GONE);
                CommunicationPreferenceFragment commPrefFragment = CommunicationPreferenceFragment.newInstance();
                if (commPrefFragment != null) {
                    if (!commPrefFragment.isVisible()) {
                        getView().findViewById(R.id.setting_layout).setVisibility(View.GONE);
                        transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container_fragment, commPrefFragment).addToBackStack(HomeActivity.SETTINGFRAGMENTKEY);

                        transaction.commit();
                    }
                }

                break;

            case R.id.btn_PrivacyPolicy:
                callStaticPageContentScreen(Constants.CONTENT_TYPE_PRIVACY_POLICY);
                break;

            case R.id.btn_change_password:
                //_showChangePasswordDialog();
                listener.callPasswordChange(true);
                break;
            case R.id.btn_unsubscribe:
                _callUnsubscribeGetApi();
                break;
            case R.id.btn_logout:
                _callLogoutApi();
                break;
            case R.id.btn_tnc:
                callStaticPageContentScreen(Constants.CONTENT_TYPE_TERMS_AND_CONDITIONS);
                break;
        }
    }

    private void callStaticPageContentScreen(String contentType) {
        FragmentTransaction transaction;
        StaticPageContentFragment staticPageContentFragment = StaticPageContentFragment.newInstance(contentType);
        if (staticPageContentFragment != null) {
            if (!staticPageContentFragment.isVisible()) {
                getView().findViewById(R.id.setting_layout).setVisibility(View.GONE);
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container_fragment, staticPageContentFragment).addToBackStack(HomeActivity.SETTINGFRAGMENTKEY);

                transaction.commit();
            }
        }
    }

    private void _callLogoutApi() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
            jsonObject.put("LogOutDate", getCurrentDateTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_LOGOUT, jsonObject, REQUEST_LOGOUT);
    }

    private void _callUnsubscribeGetApi() {
        showDialog(true, getString(R.string.dialog_login));
        requestTypeGET(Utility.getURL(Constants.API_GET_UNSUBSCRIBE_REASONS, InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE)),
                Constants.REQUEST_GET_UNSUBSCRIBE_REASONS);
    }


    private void _openSocialLink(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.trim())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof HomeActivity) {
            listener = (OnChangePasswordCallListener) context;
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(res);
            boolean statusFlag = jsonObject.optBoolean("Status");
            String message = jsonObject.optString("Message");
            switch (requestcode) {
                case REQUEST_CHANGE_PASSWORD:
                    if (statusFlag) {
                        if (changePasswordDialog != null && changePasswordDialog.isShowing()) {
                            changePasswordDialog.dismiss();
                        }
                        showErrorAlert("", message);
                    } else {
                        showErrorAlert("", message);
                    }

                    break;
                case REQUEST_GET_UNSUBSCRIBE_REASONS:
                    if (statusFlag) {
                        ArrayList<IdReason> reasonsArrayList = new Gson().fromJson(String.valueOf(jsonObject.optJSONArray("lstUnsubscribe")), new TypeToken<ArrayList<IdReason>>() {
                        }.getType());
                        UnsubscribeFragment unsubscribeFragment = UnsubscribeFragment.newInstance(reasonsArrayList);
                        if (unsubscribeFragment != null) {
                            if (reasonsArrayList != null && !reasonsArrayList.isEmpty()) {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
                                getView().findViewById(R.id.setting_layout).setVisibility(View.GONE);
                                transaction.replace(R.id.main_container_fragment, unsubscribeFragment).addToBackStack(HomeActivity.SETTINGFRAGMENTKEY);
                                mActivity.findViewById(R.id.toolbar_main).setVisibility(View.GONE);
                                transaction.commit();
                            } else {
                                showErrorAlert("", "Could not process your request at the moment. Please try again later.");
                            }
                        }
                    } else
                        showErrorAlert("", message);

                    break;
                case REQUEST_LOGOUT:
                    _clearSesionOnLogout();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rError(int requestcode) {

    }
}