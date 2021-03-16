package com.panelManagement.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class AlertForgotPasswordFragment extends BaseDialogFragment implements OnClickListener {

    EditText edt_email;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog rootView = new Dialog(getActivity());
        rootView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView.setContentView(R.layout.fragment_forgot_password);
        edt_email = rootView.findViewById(R.id.edt_forgot_email);
        rootView.findViewById(R.id.btn_send_forgot).setOnClickListener(this);
        rootView.findViewById(R.id.iv_close_forgot).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Override
    public void onClick(View arg0) {
        Utility.hideKeyboard(arg0, getActivity());
        DialogFragment fragment = null;
        switch (arg0.getId()) {
            case R.id.btn_send_forgot:
                if (Utility.isConnectedToInternet(getActivity())) {
                    if (!TextUtils.isEmpty(edt_email.getText().toString())) {
                        showDialog(true, getString(R.string.dialog_login));
                        requestTypePost(Constants.API_FORGOTPASSWORD, new ParseJSonObject(getActivity()).getForgotPasswordObject(edt_email.getText().toString()),
                                Constants.REQUESTCODE_FORGOTPASSWORD);
                    } else {
                        //showErrorAlert("", getString(R.string.msg_valid_email));
                        edt_email.setError(getString(R.string.msg_valid_email));
                        edt_email.requestFocus();
                    }
                    // requestTypePost(Constants.API_REGISTERPANELISTTHROUGHMOBILE,
                    // getRegistrationData(),
                    // Constants.REQUESTCODE_SIGNUP_USER);
                } else {
                    showErrorAlert(" ", getString(R.string.msg_low_conn));
                }
                break;
            case R.id.iv_close_forgot:
                dismiss();
                break;
        }

    }

    @Override
    public void vLayout(String res, int requestcode) {
        switch (requestcode) {
            case Constants.REQUESTCODE_FORGOTPASSWORD:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        dismiss();
                        showErrorAlert(" ", object.getString("Message"));
                    } else {
                        edt_email.setText("");
                        showErrorAlert(" ", object.getString("Message"));
                    }
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

}