package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileVerificationFragment extends BaseFragment implements OnClickListener {

    public static String verify_Code;
    public static String encrypted_mobile_number = "";

    public static ArrayList<String> listOfMessages = new ArrayList<String>();
    private static EditText edt_pinNumber;
    private static TextView tv_pinSent;
    Bundle extraBundle;
    private EditText edt_mobileNumber;
    private TextView tv_pin;
    private TextView tv_resend_otp;

    private TextView infolink;

    private Button btn_verify;
    private Button btn_verify_resend_otp;
    private Button btn_new_submit;
    private int maxPhoneLength;
    private int minPhoneLength;
    private int btn_submit = 2;
    private boolean isPinNoEnabled;
    private String prevScreen;
    public RewardPointsModels rewardsPointsData;


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
                            String val = "";
                            if (matcher.find()) {
                                val = matcher.group(1);
                                edt_pinNumber.setText(val);// 4 digit number
                            }
                            // do your stuff
                        }
                    } catch (Exception e) {
                        // Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    };

    public MobileVerificationFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public MobileVerificationFragment(Bundle extra) {
        super();
        this.extraBundle = extra;
    }

    public static void onSMSReceived(String message) {
        String tempMessages = message.replaceAll("[^0-9]", "");
        if (!tempMessages.equalsIgnoreCase(verify_Code)) {
            listOfMessages.add(tempMessages);
        }
        verify_Code = tempMessages;
        edt_pinNumber.setText(String.valueOf(verify_Code));
        tv_pinSent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPinNoEnabled = false;
        Bundle bundle = getArguments();

        if (null != bundle) {
            prevScreen = bundle.getString("key");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile_verification, container, false);
        edt_mobileNumber = view.findViewById(R.id.edt_mobilenumber);
        //edt_mobileNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(InformatePreferences.getMaxMobileLength(getActivity())) });
        edt_pinNumber = view.findViewById(R.id.edt_pin);
        edt_pinNumber.setVisibility(View.GONE);
        tv_pinSent = view.findViewById(R.id.tv_pinSent);
        tv_pinSent.setVisibility(View.GONE);
        tv_pin = view.findViewById(R.id.tv_pin);
        tv_pin.setVisibility(View.GONE);

        TextView tv_isdCode = view.findViewById(R.id.tv_isdCode);
        tv_isdCode.setText(InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_ISDCODE));
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(this);

        btn_verify_resend_otp = view.findViewById(R.id.btn_verify_resend_otp);
        btn_verify_resend_otp.setOnClickListener(this);

        btn_new_submit = view.findViewById(R.id.btn_new_submit);
        btn_new_submit.setOnClickListener(this);

        tv_resend_otp = view.findViewById(R.id.tv_resend_otp);
        tv_resend_otp.setOnClickListener(this);

        infolink = view.findViewById(R.id.infolink);
        infolink.setOnClickListener(this);

        maxPhoneLength = InformatePreferences.getMaxMobileLength(getActivity());
        minPhoneLength = InformatePreferences.getMinMobileLength(getActivity());

        if (maxPhoneLength != 0)
            edt_mobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxPhoneLength)});

        IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(SmsListener, i);
        return view;
    }

    void checkForEncryptedMobileNumber(boolean resendOTP) {
        if (!TextUtils.isEmpty(encrypted_mobile_number)) {
            try {
                if (Utility.isConnectedToInternet(getActivity())) {
                    showDialog(true, getString(R.string.dialog_login));
                    requestTypePost(Constants.API_MOBILEVERIFICATIONPIN, new ParseJSonObject(getActivity()).getMobileVerificationObject(encrypted_mobile_number, resendOTP),
                            Constants.REQUESTCODE_MOBILEVERIFICATIONPIN);
                } else {
                    showErrorAlert(" ", getString(R.string.msg_low_conn));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify: {
                String phone = edt_mobileNumber.getText().toString();

                Log.d("inputnumber:","inputnumbet:"+phone);

                //old code
//                edt_mobileNumber.setFocusable(false);

//                if (phone.length() < minPhoneLength || edt_mobileNumber.getText().toString().equals(InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER))) {
                if (phone.length() < minPhoneLength) {

                    edt_mobileNumber.getText().clear();
                    showErrorAlert(" ", getString(R.string.error_phone_length));

//                    edt_mobileNumber.setText(null);
                }else if(phone.equals("0000000000") || phone.equals("00000000000")||phone.equals("000000000000")||phone.equals("0000000000000")) {

                    edt_mobileNumber.getText().clear();
                    showErrorAlert(" ", getString(R.string.error_phone_length));

                }else {
                    edt_mobileNumber.setEnabled(false);
                    if ((edt_pinNumber.getVisibility() == View.INVISIBLE || edt_pinNumber.getVisibility() == View.GONE)
                            && TextUtils.isEmpty(edt_pinNumber.getText().toString())) {

                        btn_verify.setEnabled(false);
                        btn_verify.setBackgroundColor(Color.RED);


                        btn_verify.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_verify.setEnabled(true);
                                btn_verify.setBackgroundColor(btn_verify.getContext().getResources().getColor(R.color.aqua_dark));
                            }
                        }, 45000);


                        try {
                            if (Utility.isConnectedToInternet(getActivity())) {
                                showDialog(true, getString(R.string.dialog_login));
                                requestTypePost(Constants.API_ENCRYPTMOBILENUMBER, new ParseJSonObject(getActivity()).getEncryptedMobileNumberObject(edt_mobileNumber.getText().toString()),
                                        Constants.REQUESTCODE_ENCRYPTMOBILE);

                            } else {
                                showErrorAlert(" ", getString(R.string.msg_low_conn));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                checkForEncryptedMobileNumber(false);
                            }
                        }, 5000);
                    }

                }
                break;
            }

            case R.id.tv_resend_otp: {
                tv_resend_otp.setEnabled(false);
                tv_resend_otp.setTextColor(Color.RED);

                /*tv_resend_otp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_resend_otp.setEnabled(true);
                        tv_resend_otp.setTextColor(tv_resend_otp.getContext().getResources().getColor(R.color.aqua_dark));
                    }
                }, 45000);*/

                checkForEncryptedMobileNumber(true);

            }
            break;

            case R.id.btn_verify_resend_otp: {
                tv_resend_otp.setEnabled(false);
                tv_resend_otp.setTextColor(Color.RED);

                /*tv_resend_otp.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_resend_otp.setEnabled(true);
                        tv_resend_otp.setTextColor(tv_resend_otp.getContext().getResources().getColor(R.color.aqua_dark));
                    }
                }, 45000);*/

                checkForEncryptedMobileNumber(true);

            }
            break;




            case R.id.infolink: {

                showErrorAlert("", getString(R.string.info_link));



            }
            break;

            case R.id.btn_new_submit: {

                if (edt_pinNumber.getText().toString().equals(String.valueOf(verify_Code))) {
                    try {
                        if (Utility.isConnectedToInternet(getActivity())) {
                            showDialog(true, getString(R.string.dialog_login));
                            requestTypePost(Constants.API_SAVEMOBILE, new ParseJSonObject(getActivity())
                                    .getSaveMobileObject(edt_mobileNumber.getText().toString()), Constants.REQUESTCODE_SAVEMOBILE);
                        } else {
                            showErrorAlert(" ", getString(R.string.msg_low_conn));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                   /* boolean isVerified = false;
                    for (int i = 0; i < listOfMessages.size(); i++) {
                        if (listOfMessages.get(i).equalsIgnoreCase(edt_pinNumber.getText().toString())) {
                            isVerified = true;
                            break;
                        }
                    }
                    if (isVerified) {
                        showDialog(true, getString(R.string.dialog_login));
                        requestTypePost(Constants.API_SAVEMOBILE, new ParseJSonObject(getActivity()).getSaveMobileObject(
                                edt_mobileNumber.getText().toString()), Constants.REQUESTCODE_SAVEMOBILE);
                    } else*/
                        showErrorAlert("", getString(R.string.unabletoverifypin));
                }

                break;
            }

            default:
                break;
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();
        switch (requestcode) {

            case Constants.REQUESTCODE_ENCRYPTMOBILE:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        Log.d("mobile122:","mobile122");
                        encrypted_mobile_number = object.getString("Message");
                    } else {
                        Log.d("mobile123:","mobile123");
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_MOBILEVERIFICATIONPIN:
                try {
                    JSONObject object = new JSONObject(res);

//                    Boolean status = object.getBoolean("Status");
//                    String infootpmsg = object.getString("OTPInfoMessage");
//
//                    Log.d("otpresponse:","otpresponse:"+status);
//                    Log.d("otpresponse:1","otpresponse:1"+infootpmsg);

                    Log.d("otpresponse:","otpresponse:"+res);


                    if (object.getBoolean("Status")) {
                        verify_Code = object.getString("Message");
                        listOfMessages.add(verify_Code);
                        edt_mobileNumber.setEnabled(false);
                        edt_mobileNumber.setCursorVisible(false);
                        //OLD CODE
                        btn_verify.setVisibility(View.GONE);
                        //NEW CODE
                        btn_verify_resend_otp.setVisibility(View.VISIBLE);
                       /* btn_verify.setId(btn_submit);
                        btn_verify.setText(getResources().getString(R.string.txt_submit));
                        btn_verify.setOnClickListener(this);*/
                        isPinNoEnabled = true;
                        edt_pinNumber.setVisibility(View.VISIBLE);
                        tv_pin.setVisibility(View.VISIBLE);
                        tv_pinSent.setVisibility(View.VISIBLE);
                        btn_new_submit.setVisibility(View.VISIBLE);
                        //old code
//                        tv_resend_otp.setVisibility(View.VISIBLE);
                        //old code

                        //New Code
                        tv_resend_otp.setVisibility(View.GONE);
                        //New Code
                        if(object.getBoolean("isOtpLimitExceeded")){
                            infolink.setVisibility(View.VISIBLE);
                        }

                        //showErrorAlert("", getString(R.string.txt_msg_pinsent));
                    } else {

                        if(object.getBoolean("isOtpLimitExceeded")){
                            infolink.setVisibility(View.VISIBLE);
                        }


                        showErrorAlert(" ", object.getString("Message"));
                    }

//                    else if(!object.getBoolean("Status") && (!object.getString("OTPInfoMessage").equals("") || object.getString("OTPInfoMessage") != null)) {
//
//                        showErrorAlert(" ", object.getString("OTPInfoMessage"));
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_SAVEMOBILE:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        //showErrorAlert(" ", object.getString("Message"));
                        edt_pinNumber.setEnabled(false);
                        btn_new_submit.setEnabled(false);
                        tv_resend_otp.setEnabled(false);
                        showAlert(object.getString("Message"));
                        InformatePreferences.putBoolean(getActivity(), Constants.PREF_MOBILENUMBERVERIFIED, true);
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER, edt_mobileNumber.getText().toString());

                    } else {
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case Constants.REQUEST_AVAILABLE_POINTS:
                try {
                    Log.e("GetIncenDetailsMobile",res.toString());
                    rewardsPointsData = new ParseJSonObject(context).getRewardsPoints(new JSONObject(res));

                    String availablePoints = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
                    double minimum = Double.parseDouble(availablePoints);

                    int minimumPOints = Integer.parseInt(new DecimalFormat("#").format(minimum));

                    getActivity().findViewById(R.id.toolbar_main).setVisibility(View.VISIBLE);
                    Fragment fragment = RedeemFragment.newInstance(rewardsPointsData, minimumPOints);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void rError(int requestcode) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(SmsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        if (!TextUtils.isEmpty(message))
            alertDialog.setMessage(message);

        alertDialog.setButton(Activity.RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
                if (prevScreen.equals("Reward")) {
                    requestTypePost(Constants.GETINCENTIVEDETAILSMOBILE, new ParseJSonObject(getActivity()).getSessionObject(), Constants.REQUEST_AVAILABLE_POINTS);

                } else if (prevScreen.equals("Profile")) {
                    ProfilerFragment fragment2 = new ProfilerFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment2).addToBackStack(HomeActivity.MYFRAGMENTKEY).commit();
                }
            }
        });
        alertDialog.show();
    }
}
/*    private void showAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        if (!TextUtils.isEmpty("message"))
            alertDialog.setTitle("message");
        if (!TextUtils.isEmpty(message))
            alertDialog.setMessage(message);

        alertDialog.setButton(Activity.RESULT_OK, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                String rewardPoints = null;
                if (extraBundle != null) {
                    rewardPoints = extraBundle.getString(RedeemFragmentActivity.KEYREWARDSPOINTS);
                }else{
                    rewardPoints = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
                }
                if (Float.parseFloat(rewardPoints) < RedeemFragmentActivity.KEYMINIMUMPOINTS) {

                    Fragment mFragment = new RedeemFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.ic_container_redeem, mFragment, mFragment.toString());
                    transaction.commit();

                } else {

                    Fragment mFragment = new RedeemFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.ic_container_redeem, mFragment, mFragment.toString());
                    transaction.commit();
                }
            }
        });
        alertDialog.show();
        TextView titleView = alertDialog
                .findViewById(getActivity().getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }

        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setGravity(Gravity.CENTER);
        }

    }*/
