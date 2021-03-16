package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsMessage;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mukesh.OtpView;
import com.panelManagement.activity.R;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.TextViewPlus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupOtpFragment extends BaseFragment {
    private final long interval = 1000;
    String emailid;
    TextViewPlus textviewResend = null;
    boolean alredyotp = false;
    private UserInfoModel mCoredata;
    private ProgressDialog mProgressDialog;
    private EditText editEmail;
    private OtpView edtOtp;
    private TextViewPlus txtTimer;
    private long startTime = 120000; // 1 min
    private MyCountDownTimer countDownTimer;
    private boolean timerHasStarted = false;

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
                                edtOtp.setOTP(val);// 4 digit number
                                if (!TextUtils.isEmpty(edtOtp.getOTP()))
                                    verifyEmail();
                                else {
                                    showErrorAlert("", getString(R.string.error_enter_otp));
                                    Utility.showKeyboard(edtOtp, getActivity());
                                }
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

    public static SignupOtpFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle("bundle", bundle);
        SignupOtpFragment fragment = new SignupOtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(SmsListener, i);
        if (bundle != null) {
            if (bundle.containsKey(Constants.SIGNUP_KEY_OTP)) {
                startTime = 10;
                alredyotp = true;
            } else if (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO)) {
                mCoredata = (UserInfoModel) bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO);
                emailid = mCoredata != null ? mCoredata.getEmail()
                        : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID);
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_INREGISTRATIONPROCESS,
                        Constants.INREGISTRATIONPROCESS);
            }
        }
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_otp, null);
        if (alredyotp) {
            editEmail = view.findViewById(R.id.otp_email);
            editEmail.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_otp_cancel).setVisibility(View.VISIBLE);
        }
        view.findViewById(R.id.parentMessages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Utility.hideKeyboard(arg0, getActivity());
            }
        });
/*
        view.findViewById(R.id.btn_otp_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SocialSignupFragment mFragment = new SocialSignupFragment();
                Bundle bundle = new Bundle();
                mFragment.setArguments(bundle);
                transaction.replace(R.id.ic_tabview_container, mFragment);
                transaction.commit();
            }
        });*/
        view.findViewById(R.id.btn_signup_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (alredyotp) {
                    if (!TextUtils.isEmpty(editEmail.getText()))
                        if (Utility.isEmailValid(editEmail.getText().toString()))
                            if (!TextUtils.isEmpty(edtOtp.getOTP())) {
                                emailid = editEmail.getText().toString();
                                verifyEmail();
                            } else {
                                showErrorAlert("", getString(R.string.error_enter_otp));
                                Utility.showKeyboard(edtOtp, getActivity());
                            }
                        else {
                            //showErrorAlert("", getString(R.string.msg_valid_email));
                            editEmail.setError(getString(R.string.msg_valid_email));
                            editEmail.requestFocus();
                            Utility.showKeyboard(editEmail, getActivity());
                        }
                    else {
                        //showErrorAlert("", getString(R.string.otp_email));
                        editEmail.setError(getString(R.string.otp_email));
                        editEmail.requestFocus();
                        Utility.showKeyboard(editEmail, getActivity());
                    }
                } else {
                    if (!TextUtils.isEmpty(edtOtp.getOTP()))
                        verifyEmail();
                    else {
                        //showErrorAlert("", getString(R.string.error_enter_otp));
                        showErrorAlert("", getString(R.string.error_enter_otp));
                        Utility.showKeyboard(edtOtp, getActivity());
                    }
                }
            }
        });
        TextViewPlus txtCheckMail = view.findViewById(R.id.ic_textview_checkmail);
        txtCheckMail.setText(Html.fromHtml(getString(R.string.ui_msg_checkemail)));
        txtTimer = view.findViewById(R.id.itimer);

        textviewResend = view.findViewById(R.id.txt_resend);
        textviewResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (Utility.isConnectedToInternet(getActivity()))
                    requestEmail();
                else
                    showErrorAlert("", getString(R.string.async_task_error));
            }
        });
        TextViewPlus txtTermsCondition = view.findViewById(R.id.ic_signupContactTermscondition);
        txtTermsCondition.setText(Html.fromHtml(getString(R.string.ui_termsandcondition)));
        txtTermsCondition.setClickable(true);
        txtTermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
        edtOtp = view.findViewById(R.id.edtOtp);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        // txtTimer.setText(txtTimer.getText() + String.valueOf(startTime /
        // 1000));
        startTimer(false);
        return view;
    }

    private String getTermsConditionText(FragmentActivity activity) {

        return null;
    }

    private void startTimer(boolean timerd) {
        this.timerHasStarted = timerd;
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            textviewResend.setVisibility(View.INVISIBLE);
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            if (alredyotp)
                textviewResend.setVisibility(View.INVISIBLE);   // changed @Niyaj 14-08-2017
            else {
                textviewResend.setVisibility(View.VISIBLE);
            }
        }

    }

    private void requestEmail() {
        showDialog(true, getString(R.string.dialog_login));
        //requestTypePost(Constants.API_SENDEMAILOPT, new ParseJSonObject(getActivity()).getEmailOtp(emailid), 1);
        requestTypePost(Constants.API_SENDEMAILOPT, new ParseJSonObject(getActivity()).getEmailOtp(mCoredata != null
                ? mCoredata.getEmail() : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID)), 1);

    }

    private void verifyEmail() {
        if (Utility.isConnectedToInternet(getActivity())) {
            showDialog(true, getString(R.string.dialog_login));
            requestTypePost(Constants.API_VERIFYEMAILOTP,
                    new ParseJSonObject(getActivity()).verifyEmailOtp(
                            emailid, edtOtp.getOTP()), 2);
        } else {
            showErrorAlert("", getString(R.string.async_task_error));
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void showDialog(boolean isShow, String message) {

        if (isShow) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    @Override
    protected void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();

            mProgressDialog = null;
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onRequestCompleted(String response, int requestCode) {
        dismissDialog();
        try {
            JSONObject object = new JSONObject(response);
            switch (requestCode) {
                case 1:
                    if (object.has("Status") && object.getBoolean("Status")) {
                        startTimer(false);
                        showErrorAlert("", String.format(getString(R.string.msgcheckemailforotp), mCoredata != null ? mCoredata.getEmail()
                                : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID)));
                    } else {
                        showErrorAlert("", object.getString("message"));
                    }
                    break;
                case 2:
                    if (object.has("Status") && object.getBoolean("Status")) {
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.getString("PanelistId"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_SESSIONID,
                                object.getString("PanelistSessionID"));

                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ISDCODE,
                                object.getString("ISDCode"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE,
                                object.getString("CountryCode"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBERMAXLENGHT,
                                object.getString("MobileNumberMaxLength"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME,
                                object.getString("FirstName"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LASTNAME,
                                object.getString("LastName"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PANELISTSESSIONID,
                                object.getString("PanelistSessionID"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PRIVACYPOLICY,
                                object.getString("PPLink"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_TNC,
                                object.getString("TandCLink"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FBLINK,
                                object.getString("FBLink"));

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        ConfirmationFragment mFragment = new ConfirmationFragment();
                        Bundle bundle = new Bundle();
                        mFragment.setArguments(bundle);
                        transaction.replace(R.id.ic_register_container, mFragment);
                        transaction.commit();
                    } else {
                        showErrorAlert("", object.getString("message"));
                    }

                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        dismissDialog();
    }

    @Override
    public void onRequestStarted(int requestCode) {


    }

    @Override
    public void vLayout(String res, int requestcode) {


    }

    @Override
    public void rError(int requestcode) {

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            txtTimer.setText("");
            if (alredyotp)
                textviewResend.setVisibility(View.INVISIBLE);   // changed @Niyaj 14-08-2017
            else {
                textviewResend.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (isAdded()) {
                txtTimer.setText(getResources().getString(R.string.resendactivationcode) + " " + String.format("%d " + getResources().getString(R.string.min) + " %d " + getResources().getString(R.string.sec), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
        }
    }
}
