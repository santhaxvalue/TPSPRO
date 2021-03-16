package com.panelManagement.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import static com.panelManagement.utils.Constants.REQUEST_GET_RESUBSCRIBE_USER;
import static com.panelManagement.utils.Constants.REQUEST_RESUBSCRIBE_DETAILS_SAVE;

public class UnSubscribeActivity extends BaseActivity implements View.OnClickListener {

    private View mLytUnsubscribeSuccess;
    private View mLytSubscribeSuccess;
    private AutoCompleteTextView mResubscribeEmail;
    private TextView mResubscribe;
    private TextView mSignIn;
    private Dialog changePasswordDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_subscribe);
        initViews();
        checkForDeeplink();
    }

    private void checkForDeeplink() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if (InformatePreferences.getStringPrefrence(getApplicationContext(), Constants.PREF_LOGINSUCCESS).equals("true")) {
            Constants.EnableMetering = false;
            //ODMmeterStatus();

            Intent mainIntent = new Intent(UnSubscribeActivity.this, HomeActivity.class);
            Intent intent1 = getIntent();
            if (intent1 != null) {
                if (intent1.hasExtra(Constants.PUSHNOTIFY)) {
                    int DefaultTab = intent1.getIntExtra(Constants.CHOOSETABS, 1);
                    mainIntent.putExtra(Constants.CHOOSETABS, DefaultTab);
                }
            }
            startActivity(mainIntent);
            PanelConstants.isLogin = true;
            finish();
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);

        } else if (data != null) {
            mLytUnsubscribeSuccess.setVisibility(View.GONE);
            String panelistId = (Uri.parse(data.toString())).getQueryParameter("PID");
            if (!TextUtils.isEmpty(panelistId))
                _showChangePasswordDialog(panelistId);
        }
    }

    private void initViews() {
        mLytUnsubscribeSuccess = findViewById(R.id.lyt_unsubscribe_success);
        mLytSubscribeSuccess = findViewById(R.id.lyt_subscribe_success);
        mResubscribeEmail = findViewById(R.id.resubscribe_email);
        mResubscribe = findViewById(R.id.btn_resubscribe);
        mSignIn = findViewById(R.id.btn_sign_in);

        mResubscribe.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void vLayout(String res, int requestcode) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(res);
            boolean statusFlag = jsonObject.optBoolean("Status");
            String message = jsonObject.optString("Message");
            switch (requestcode) {
                case REQUEST_GET_RESUBSCRIBE_USER:
                    showErrorAlert("", message);

                    break;
                case REQUEST_RESUBSCRIBE_DETAILS_SAVE:
                    if (statusFlag) {
                        if (changePasswordDialog != null)
                            changePasswordDialog.dismiss();
                        showSubscribeSuccessScreen();
                        showErrorAlert("", message);
                    } else {
                        showErrorAlert("", message);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSubscribeSuccessScreen() {
        mLytUnsubscribeSuccess.setVisibility(View.GONE);
        mLytSubscribeSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resubscribe:
                validateResubscribe();
                break;
            case R.id.btn_sign_in:
                _redirectToSignInPage();
                break;
        }
    }

    private void _redirectToSignInPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
        ((Activity) this).finish();
    }

    private void validateResubscribe() {
        if (!TextUtils.isEmpty(mResubscribeEmail.getText().toString().trim()))
            callResubscribeAPI();
        else
            showErrorAlert("", "Email can not be empty!");
    }

    private void callResubscribeAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmailId", mResubscribeEmail.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_RESUBSCRIBE_USER, jsonObject, REQUEST_GET_RESUBSCRIBE_USER);
    }

    private void _callChangePasswordApi(String panelistId, String newPassword) {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", panelistId);
            jsonObject.put("Password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_RESUBSCRIBE_DETAILS_SAVE, jsonObject, REQUEST_RESUBSCRIBE_DETAILS_SAVE);
    }


    private void _showChangePasswordDialog(String panelistId) {

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
        tv_old_password.setVisibility(View.GONE);
        TextView tv_new_password = changePasswordDialog.findViewById(R.id.tv_new_password);
        TextView tv_confirm_password = changePasswordDialog.findViewById(R.id.tv_confirm_password);
        TextView tv_expiry_message_lable = changePasswordDialog.findViewById(R.id.expiry_msg);
        _setAsterisk(tv_old_password);
        _setAsterisk(tv_new_password);
        _setAsterisk(tv_confirm_password);

        EditText et_old_password = changePasswordDialog.findViewById(R.id.et_old_password);
        et_old_password.setVisibility(View.GONE);
        EditText et_new_password = changePasswordDialog.findViewById(R.id.et_new_password);
        EditText et_confirm_password = changePasswordDialog.findViewById(R.id.et_confirm_password);

        ImageView closeDialog = changePasswordDialog.findViewById(R.id.close_dialog_change_pw);
        closeDialog.setVisibility(View.INVISIBLE);

        Button btn_reset = changePasswordDialog.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(v -> {
            et_new_password.requestFocus();
            et_new_password.setText("");
            et_confirm_password.setText("");
        });

        Button btn_submit = changePasswordDialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> {
            String newPassword = et_new_password.getText().toString();
            String confirmPassword = et_confirm_password.getText().toString();
            if (!TextUtils.isEmpty(newPassword))
//                if (newPassword.length() >= 6 && newPassword.length() < 16)
                    if (!TextUtils.isEmpty(confirmPassword))
                        if (newPassword.equals(confirmPassword)) {
                            Utility.hideKeyboard(v, this);
                            UnSubscribeActivity.this._callChangePasswordApi(panelistId, newPassword);
                        } else
                            et_confirm_password.setError(getString(R.string.password_comapre_mismatch));
                    else
                        et_confirm_password.setError(getString(R.string.enter_confirm_password));
               /* else
                    et_new_password.setError(getString(R.string.new_password_length));*/
            else et_new_password.setError(getString(R.string.new_password));
        });

        changePasswordDialog.show();
    }


    private void _setAsterisk(TextView textView) {
        SpannableString ss = new SpannableString(textView.getText());
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }
}
