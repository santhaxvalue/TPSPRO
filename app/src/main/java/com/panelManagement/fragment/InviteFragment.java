package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.BuildConfig;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.TextViewPlus;
import com.panelManagement.widgets.TextViewPlusBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InviteFragment extends BaseFragment implements View.OnClickListener, AsyncHttpRequest.RequestListener {

    CardView cardView;
    private TextView details;
    private Button btn_invite;
    private String referalCode = "", strFirstName, strLastName;

    public InviteFragment() {
    }

    public static InviteFragment newInstance() {
        InviteFragment fragment = new InviteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invite, container, false);

        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(false);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        TextView tv_invite_sub_head= view.findViewById(R.id.tv_invite_sub_head);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        HomeActivity.setRewardsBackground();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);
        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_INREGISTRATIONPROCESS, "");
        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LOGINSUCCESS, "true");

        if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("IN")){
            tv_invite_sub_head.setText("And each time you do, earn 250 referral points!");
        }
            try {

            referalCode = InformatePreferences.getStringPrefrence(context, Constants.PREF_REFFERELCODE);
            strFirstName = InformatePreferences.getStringPrefrence(context, Constants.PREF_FIRSTNAME);
            strLastName = InformatePreferences.getStringPrefrence(context, Constants.PREF_LASTNAME);
            errorLogAPI("Invite Success " + " Referel Code " + referalCode + " First Name " + strFirstName + " Last Name " + strLastName);


        } catch (Exception e)
        {
            try {
                String data = "referal code" + InformatePreferences.getStringPrefrence(context, Constants.PREF_REFFERELCODE) +
                        "First Name : " + InformatePreferences.getStringPrefrence(context, Constants.PREF_FIRSTNAME) +
                        "Last Name : " + InformatePreferences.getStringPrefrence(context, Constants.PREF_LASTNAME);
                data += e.toString();
                errorLogAPI(data);
            } catch (Exception e1) {
                errorLogAPI("Invite Crashed in 1st exception");
            }

        }

        details = view.findViewById(R.id.tv_invite_link);
        btn_invite = view.findViewById(R.id.btn_invite);
        details.setOnClickListener(this);
        btn_invite.setOnClickListener(this);

        cardView = view.findViewById(R.id.invite_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        cardView.setAnimation(hide);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_invite_link:
                showCustomAlertDialog(getContext());

                break;
            case R.id.btn_invite:
                if (!TextUtils.isEmpty(referalCode)) {
                 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");

                    String shareSub = "Referral Code ";
                    String shareBody = "The Panel Station - Your Friend " + strFirstName + " " + strLastName + " has invited you to join us and earn along with him by sharing your valuable opinion." +
                            "\nDon’t forget to enter your referral code " + referalCode + " to earn 500 joining Bonus points." +
                            "\niOS App store URL :https://goo.gl/GB2rkz" +
                            "\nAndroid Play store URL :https://goo.gl/9bKx76";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));

                } else {
                    showErrorAlert(" ", getResources().getString(R.string.refer_code_not_avaliable));
                }
        }
    }


    private void errorLogAPI(String data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject.put("Log", data);
       //     jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_ERROR_LOG, jsonObject, Constants.ERRORLOG);
    }


    @SuppressLint("StringFormatInvalid")
    public void showCustomAlertDialog(Context context) {
        ImageView closeDialog;
        TextViewPlus tv_first_point,tv_second_point;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_refer);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        dialog.show();
        closeDialog = dialog.findViewById(R.id.close_dialog_refer);
        tv_first_point = dialog.findViewById(R.id.tv_first_point);
        tv_second_point = dialog.findViewById(R.id.tv_second_point);

        if (InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE).equalsIgnoreCase("IN")){
            tv_first_point.setText("1. You earn 250 referral points (Only upon completion of their three survey).");
            tv_second_point.setText("2. Your friend earns 250 joining points.");
        }

        closeDialog.setOnClickListener((View v) -> {
            dialog.dismiss();
        });
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        //  dismissDialog();
        switch (requestCode) {

            case Constants.ERRORLOG:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("Status").equalsIgnoreCase("true")) {
                        //true, update the app
                        String message = jsonObject.optString("Message");
                    } else { // no update available

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


        }
    }
}
