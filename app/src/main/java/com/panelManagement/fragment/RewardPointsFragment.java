package com.panelManagement.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.mukesh.OtpView;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.panelManagement.activity.HomeActivity.MYFRAGMENTKEY;

public class RewardPointsFragment extends BaseFragment implements OnClickListener {

    public static final int REWARDHISTORY = 1;
    public static final int THANKS = 4;
    public static final int REDEMPTIONHISTORY = 2;
    public static final int REDEEM = 3;
    public static final String KEYREWARDS = "rewards";
    public static final String KEYLIST = "rewardslist";
    public static final String KEYREWARDSPOINTS = "rewardspoints";
    public static final Float KEYMINIMUMPOINTS = 3000.0f;
    private static final int RESULT_CODE_PURCHASED = 201;
    public static String verify_Code;
    public static ArrayList<String> listOfMessages = new ArrayList<>();
    //public static int minimumGeneralPoints = 3000;
    public static int minimumRedeemPoints = 500; // by default Actual value is coming from shared prefrence this value initialized just to avoid error.
    public static int APP_REQUEST_CODE = 99;
    private static OtpView pin;
    public static RewardPointsModels rewardsPointsData;
    String phoneNumberString;
    String val = "";
    Context context;
    RelativeLayout relativeLayout;
    TextView txt_points_earned;
    TextView txt_points_redeemed;
    TextView txt_points_available;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int maxPhoneLength;


    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;




    private int minPhoneLength;
    private boolean dontCallFronCreate;
    private BroadcastReceiver SmsListener = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras(); // ---get the SMS message
                // passed in---
                SmsMessage[] msgs = null;
                // String msg_from;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            // msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            Pattern pattern = Pattern.compile("(\\d{4})");
                            Matcher matcher = pattern.matcher(msgBody);
                            if (matcher.find()) {
                                val = matcher.group(1);
                                pin.setOTP(val);// 4 digit number
                            }
                            // do your stuff
                        }
                    } catch (Exception e) {
                        Log.e("Exception caught", e.getMessage());
                    }
                }
            }
        }
    };

    public RewardPointsFragment() {
        super();
    }

    public static RewardPointsFragment newInstance() {
        return new RewardPointsFragment();
    }

    public static void onSMSReceived(String message) {
        String tempMessages = message.replaceAll("[^0-9]", "");
        if (!tempMessages.equalsIgnoreCase(verify_Code)) {
            listOfMessages.add(tempMessages);
        }
        verify_Code = tempMessages;
        pin.setOTP(verify_Code);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        //  ((HomeActivity)getActivity())._secondaryFragment(Reward2.newInstance("1111", "1111", "1111"), HomeActivity.REWARDSFRAGMENTKEY);
        IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(SmsListener, i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        //String isRedeemInstant = InformatePreferences.getStringPrefrence(context, Constants.IS_REDEEM_INSTANTLY);
        if(InformatePreferences.getBoolean(context, Constants.IS_REDEEM_INSTANTLY,false))
        {
            TextView redeem = view.findViewById(R.id.redeem_text);
            redeem.setText(R.string.redeem_instantly);
        }

        int dpValue = -45; // margin in dips
        float d = context.getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d); // margin in pixels




        getActivity().findViewById(R.id.cv_points_available).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.cv_points_earned).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.cv_points_redeemed).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.VISIBLE); // 1st
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        Utility.setMargins(context, layout, Utility.getDp(context, 60));
        view.findViewById(R.id.rewardBtnhistory).setOnClickListener(this);
        view.findViewById(R.id.rewardBtnRedeem).setOnClickListener(this);
        view.findViewById(R.id.rewardBtnredemtion).setOnClickListener(this);
        view.findViewById(R.id.pointsinreviewCard).setOnClickListener(this);
        //new code
        view.findViewById(R.id.pointsinrerejectedCard).setOnClickListener(this);
        //new code
        view.findViewById(R.id.btn_invite).setOnClickListener(this);
        HomeActivity.setRewardsBackground();
        txt_points_earned = getActivity().findViewById(R.id.txt_points_earned);
        txt_points_redeemed = getActivity().findViewById(R.id.txt_points_redeemed);
        txt_points_available = getActivity().findViewById(R.id.txt_points_available);

        txt_points_earned.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_EARNEDPOINTS_));
        txt_points_redeemed.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_SPENTPOINTS_));
        txt_points_available.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_AVAILABLEPOINTS_));
        if (isAdded()) {
            dontCallFronCreate = false;
            requestUpdatePoints();
        }
        relativeLayout = view.findViewById(R.id.myreward_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.cv_points_available).setOnClickListener(v1 -> redeemPoints());
        getActivity().findViewById(R.id.cv_points_earned).setOnClickListener(v -> showRewardHistory());
        getActivity().findViewById(R.id.cv_points_redeemed).setOnClickListener(v -> showRedemptionHistory());


    }

    @Override
    public void onResume() {
        super.onResume();
        if (dontCallFronCreate) {
            requestTypePost(Constants.GETINCENTIVEDETAILSMOBILE, new ParseJSonObject(getActivity()).getSessionObject(), Constants.REQUEST_AVAILABLE_POINTS);

            getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
            Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            hide.setDuration(1000);
            relativeLayout.setAnimation(hide);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        dontCallFronCreate = true;
    }

    @Override
    public void onClick(View v) {

        InformatePreferences.setStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED, "false");
        switch (v.getId()) {
            case R.id.rewardBtnhistory:
                showRewardHistory();
                break;

            case R.id.rewardBtnredemtion:
                showRedemptionHistory();
                break;

            case R.id.btn_invite:

                HomeActivity.hideToolbar();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container_fragment, InviteFragment.newInstance()).addToBackStack(MYFRAGMENTKEY);
                fragmentTransaction.commit();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
                break;

            case R.id.rewardBtnRedeem:


                dobDateGenerator();


//                    redeemPoints();
                break;
            case R.id.pointsinreviewCard:
                showPointsReview();
                break;
            case R.id.pointsinrerejectedCard:
                showPointsRejected();
                break;
        }
    }

    private void dobDateGenerator() {

        Toast toast= Toast.makeText(getActivity(),
                R.string.enter_dob, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.toast_drawable);
        toast.show();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("selecteddate:", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;

            }
        };

    }


    private void redeemPoints() {
//        if(InformatePreferences.getBoolean(context, Constants.IS_REDEEM_INSTANTLY,false))
//        {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            Fragment fragment = GeneralBuyFragment.newInstance(rewardsPointsData);
//            transaction.replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REDEEMFRAGMENTKEY);
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            transaction.commit();
//        }
//        else
            minimumRedeemPoints = InformatePreferences.getInt(getActivity(), Constants.PREF_THRESHHOLD);
            String availablePoints= InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
            int minimumPOints = Integer.parseInt(availablePoints);

            if (rewardsPointsData != null) {
                if (Float.parseFloat(rewardsPointsData.getAvailablePoints()) < RewardPointsFragment.minimumRedeemPoints &&  !(InformatePreferences.getBoolean(context, Constants.IS_REDEEM_INSTANTLY,false))) {
                    int redeemValue = (int) (RewardPointsFragment.minimumRedeemPoints - (Float.parseFloat(rewardsPointsData.getAvailablePoints())));
                    showErrorAlert("", String.format(getResources().getString(R.string.alert_dialog_txt_part3), redeemValue));
                }

                else if (InformatePreferences.getBoolean(getActivity(), Constants.PREF_MOBILENUMBERVERIFIED, false)) {

                    Fragment fragment = RedeemFragment.newInstance(rewardsPointsData,minimumPOints);
                    getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
                    //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.second_container, SurveyFloating.newInstance(""), SECOND_CONTAINER).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();

                } else {
                    //showErrorAlert("", "Please earn points by attending surveys.");
                    //showMobileVerification();
                    //_phoneLogin();
                    if (!(InformatePreferences.getBoolean(getContext(), Constants.PREF_MOBILENUMBERVERIFIED, false)))
                    {
                        HomeActivity.hideToolbar();
                        getActivity().findViewById(R.id.cv_points_available).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.cv_points_earned).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.cv_points_redeemed).setVisibility(View.GONE);

                        MobileVerificationFragment  fragment2 = new MobileVerificationFragment();
                        Bundle bundleobj = new Bundle();
                        bundleobj.putString("key", "Reward");
                        fragment2.setArguments(bundleobj);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment2).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
                    }

                    Log.e("redeemped points", "Empty");
                }
            } else {
                Log.e("reward data", "null");
            }
    }

    public void _phoneLogin() {
        final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE; use TOKEN to get mobile number
        String countryCode = InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE);
        configurationBuilder.setDefaultCountryCode(countryCode);
        configurationBuilder.setSMSWhitelist(new String[]{countryCode});
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

//    private void showMobileVerification() {
//        TextView isdCode;
//        ImageView closeDialog;
//        Button verify;
//        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_mobile_verification);
//        dialog.setCanceledOnTouchOutside(false);
//        Window window = dialog.getWindow();
//        assert window != null;
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        dialog.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        wlp.dimAmount = .5f;
//        window.setAttributes(wlp);
//        dialog.show();
//
//        isdCode = dialog.findViewById(R.id.tv_isdCode);
//        edtMobileNumber = dialog.findViewById(R.id.edt_mobilenumber);
//        verify = dialog.findViewById(R.id.btn_verify_mobile);
//
//        isdCode.setText("+" + InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_ISDCODE));
//
//        maxPhoneLength = InformatePreferences.getMaxMobileLength(getActivity());
//        minPhoneLength = InformatePreferences.getMinMobileLength(getActivity());
//
//        if (maxPhoneLength != 0)
//            edtMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxPhoneLength)});
//
//
//        closeDialog = dialog.findViewById(R.id.close_dialog_change_pw);
//
//        closeDialog.setOnClickListener((View v) -> {
//            try {
//                getActivity().unregisterReceiver(SmsListener);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Utility.hideKeyboard(v, context);
//            dialog.dismiss();
//        });
//
//        verify.setOnClickListener(v -> {
//            Utility.hideKeyboard(v, getActivity());
//            phone = edtMobileNumber.getText().toString();
//            if (phone.length() < minPhoneLength) {
//                showErrorAlert(" ", getString(R.string.error_phone_length));
//            } else {
//                try {
//                    if (Utility.isConnectedToInternet(getActivity())) {
//                        dialog.dismiss();
//                        showDialog(true, getString(R.string.dialog_login));
//                        requestTypePost(Constants.API_MOBILEVERIFICATIONPIN, new ParseJSonObject(getActivity()).getMobileVerificationObject(
//                                InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_IPADDRESS), edtMobileNumber.getText().toString()),
//                                Constants.REQUESTCODE_MOBILEVERIFICATIONPIN);
//                    } else {
//                        showErrorAlert(" ", getString(R.string.msg_low_conn));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    private void showPinVerification() {
//        Button submit;
//        ImageView closeDialog;
//        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_pin_verification);
//        dialog.setCanceledOnTouchOutside(false);
//        Window window = dialog.getWindow();
//        assert window != null;
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        dialog.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        wlp.dimAmount = .5f;
//        window.setAttributes(wlp);
//
//
//        pin = dialog.findViewById(R.id.edt_pin);
//
//        pin.enableKeypad();
//
//        submit = dialog.findViewById(R.id.btn_verify_pin);
//        submit.setOnClickListener(v -> {
//            dialog.dismiss();
//            if (pin.getOTP().equals(String.valueOf(verify_Code))) {
//                try {
//                    if (Utility.isConnectedToInternet(getActivity())) {
//                        showDialog(true, getString(R.string.dialog_login));
//                        requestTypePost(Constants.API_SAVEMOBILE, new ParseJSonObject(getActivity())
//                                .getSaveMobileObject(edtMobileNumber.getText().toString()), Constants.REQUESTCODE_SAVEMOBILE);
//                    } else {
//                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                boolean isVerified = false;
//                for (int j = 0; j < listOfMessages.size(); j++) {
//                    if (listOfMessages.get(j).equalsIgnoreCase(pin.getOTP())) {
//                        isVerified = true;
//                        break;
//                    }
//                }
//                if (isVerified) {
//                    showDialog(true, getString(R.string.dialog_login));
//                    requestTypePost(Constants.API_SAVEMOBILE, new ParseJSonObject(getActivity()).getSaveMobileObject(
//                            edtMobileNumber.getText().toString()), Constants.REQUESTCODE_SAVEMOBILE);
//                } else
//                    showErrorAlert("", getString(R.string.unabletoverifypin));
//            }
//
//        });
//        closeDialog = dialog.findViewById(R.id.close_dialog_pin_verification);
//
//        closeDialog.setOnClickListener((View v) -> {
//            dialog.dismiss();
//            try {
//                getActivity().unregisterReceiver(SmsListener);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        dialog.show();
//    }

    private void showRedemptionHistory() {

        if (rewardsPointsData != null) {
            //   ((HomeActivity) getActivity())._secondaryFragment(PointsRedeemed.newInstance(), HomeActivity.REWARDSFRAGMENTKEY);
            Fragment fragment = RedemptionHistoryFragment.newInstance(rewardsPointsData);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
        } else {
            showErrorAlert("", "Please earn points by attending surveys.");
            Log.e("redeemption history", "Empty");
        }
    }

    private void showRewardHistory() {
        if (rewardsPointsData != null) {
            //    ((HomeActivity) getActivity())._secondaryFragment(PointsEarned.newInstance(), HomeActivity.REWARDSFRAGMENTKEY);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
            Fragment fragment = RewardHistoryFragment.newInstance(rewardsPointsData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
        } else {
            showErrorAlert("", "Please earn points by attending surveys.");
            Log.e("rewards history", "Empty");
        }
    }


    private void showPointsReview() {
        if (rewardsPointsData != null) {
            //    ((HomeActivity) getActivity())._secondaryFragment(PointsEarned.newInstance(), HomeActivity.REWARDSFRAGMENTKEY);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);

            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.VISIBLE);

            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);

            Fragment fragment = PointsInReview.newInstance(rewardsPointsData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
        } else {
            showErrorAlert("", "Please earn points by attending surveys.");
            Log.e("rewards history", "Empty");
        }
    }

    private void showPointsRejected() {
        if (rewardsPointsData != null) {
            //    ((HomeActivity) getActivity())._secondaryFragment(PointsEarned.newInstance(), HomeActivity.REWARDSFRAGMENTKEY);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.VISIBLE);


            Fragment fragment = PointsInRejected.newInstance(rewardsPointsData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
        } else {
            showErrorAlert("", "Please earn points by attending surveys.");
            Log.e("rewards history", "Empty");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfilerFragment.RESULT_PROFILER) {
            if (isAdded())
                requestUpdatePoints();
        }/*else if (requestCode == RESULT_CODE_PURCHASED) {
            showRedemptionHistory();
		}*/

        if (requestCode == APP_REQUEST_CODE) {
            // confirm that this response matches your request

            if (resultCode == Activity.RESULT_OK) {

                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                String toastMessage;
                if (loginResult.getError() != null) {
                    toastMessage = loginResult.getError().getErrorType().getMessage();
                    // showErrorActivity(loginResult.getError());
                } else if (loginResult.wasCancelled()) {
                    toastMessage = "Login Cancelled";
                } else {
                    if (loginResult.getAccessToken() != null) {
                        toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    } else {
                        toastMessage = String.format(
                                "Success:%s...",
                                loginResult.getAuthorizationCode().substring(0, 10));
                    }

                    // If you have an authorization code, retrieve it from
                    // loginResult.getAuthorizationCode()
                    // and pass it to your server and exchange it for an access token.

                    // Success! Start your next activity...
                    // goToMyLoggedInActivity();
                }

                // Surface the result to your user in an appropriate way.
//            Toast.makeText(
//                    context,
//                    toastMessage,
//                    Toast.LENGTH_LONG)
//                    .show();

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        String accountKitId = account.getId();
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        phoneNumberString = phoneNumber.getPhoneNumber();

                        if (!TextUtils.isEmpty(phoneNumberString)) {
                            showDialog(true, getString(R.string.dialog_login));
                            JSONObject jsonObject = new JSONObject();

                            try {
                                jsonObject.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
                                jsonObject.put("MobileNumber", phoneNumberString);
                                requestTypePost(Constants.API_SAVEMOBILE, jsonObject, Constants.REQUESTCODE_SAVEMOBILE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            showErrorAlert("", getString(R.string.err_msg_invalid_phone_number));
                        }
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                        showErrorAlert("", getString(R.string.api_network_connection_unavailbale));
                    }
                });
            }
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        Log.v("Response", res);
        switch (requestcode) {

            case Constants.REQUESTCODE_MOBILEVERIFICATIONPIN:

                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {

                        IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                        getActivity().registerReceiver(SmsListener, i);

                        verify_Code = object.getString("Message");
                        listOfMessages.add(verify_Code);

                        //   showPinVerification();

                    } else {
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_SAVEMOBILE:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        try {
                            getActivity().unregisterReceiver(SmsListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showErrorAlert("", object.optString("Message"));
                        InformatePreferences.putBoolean(getActivity(), Constants.PREF_MOBILENUMBERVERIFIED, true);
                        InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_MOBILENUMBER, phoneNumberString );
                        HomeActivity.phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(context, Constants.PREF_ISDCODE), InformatePreferences.getStringPrefrence(context, Constants.PREF_MOBILENUMBER)));


                    } else {
                        showErrorAlert(" ", object.optString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case Constants.REQUEST_AVAILABLE_POINTS:
                try {
                    Log.e("GetIncenRewardPoFrag",res.toString());
                    rewardsPointsData = new ParseJSonObject(context).getRewardsPoints(new JSONObject(res));
                    if (rewardsPointsData != null) {

                        InformatePreferences.setStringPrefrence(context, Constants.PREF_EARNEDPOINTS_, rewardsPointsData.getEarnedPoints());
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_SPENTPOINTS_, rewardsPointsData.getSpentPoints());
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_AVAILABLEPOINTS_, rewardsPointsData.getAvailablePoints());
//                        txtValuePointEarned.setText(rewardsPointsData.getEarnedPoints());
//                        txtValuePointRedeem.setText(rewardsPointsData.getSpentPoints());
//                        txtValuePointAvailable.setText(rewardsPointsData.getAvailablePoints());
//                        Reward2.txt_points_earned.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_EARNEDPOINTS_));
//                        Reward2.txt_points_redeemed.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_SPENTPOINTS_));
//                        Reward2.txt_points_available.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_AVAILABLEPOINTS_));
                        txt_points_earned.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_EARNEDPOINTS_));
                        txt_points_redeemed.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_SPENTPOINTS_));
                        txt_points_available.setText(InformatePreferences.getStringPrefrence(context, Constants.PREF_AVAILABLEPOINTS_));
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.second_container, Reward2.newInstance("1111", "1111", "1111"), SECOND_CONTAINER).commit();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void rError(int requestcode) {

    }

    private void showAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        if (!TextUtils.isEmpty("message"))
            alertDialog.setTitle("message");
        if (!TextUtils.isEmpty(message))
            alertDialog.setMessage(message);

        alertDialog.setButton(Activity.RESULT_OK, "OK", (DialogInterface dialog, int which) -> {
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
           // Fragment mFragment = RedeemFragment.newInstance(rewardsPointsData);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
          //  transaction.replace(R.id.main_container_fragment, mFragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY);
            transaction.commit();
        });
        alertDialog.show();
        TextView titleView = alertDialog.findViewById(getActivity().getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }

        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(SmsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
