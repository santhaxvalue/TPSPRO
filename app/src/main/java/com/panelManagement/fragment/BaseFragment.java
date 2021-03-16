package com.panelManagement.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SignUpActivity;
import com.panelManagement.activity.SplashScreenActivity;
import com.panelManagement.activity.UnSubscribeActivity;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.model.PointsInReviewModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.panelManagement.utils.Constants.REQUEST_GET_OPINION_POLL_QUESTIONS;

public abstract class BaseFragment extends Fragment implements RequestListener {

    ProgressDialog mProgressDialog;
    Context context;
    boolean isFirstConsentGiven = false;
    boolean isSecondConsentGiven = false;
    boolean isFinalConsentGiven = false;
    boolean isFirstAnswerNo = false;
    boolean isSecondAnswerNo = false;
    boolean isFirstAnswerYes = false;
    private AsyncHttpRequest mAppRequest;
    private long lastBackPressTime;
    private FragmentActivity activity;


    public BaseFragment() {
        super();
    }

    public String getTermsConditionText2(Context mContext) {
        String terms = "";
        try {
            terms = getString(R.string.consent_disclaimer_1) +
                    " <a href='" + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_TNC) + "'>"
                    + getString(R.string.lbl_terms_cond) + "</a>" + getString(R.string.txt_and_text) +
                    " <a href='" + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_PRIVACYPOLICY) + "'>"
                    + getString(R.string.lbl_privacy_statement) + "</a>" + " " + getString(R.string.consent_disclaimer_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = this.getContext();
    }

    protected void _checkConsentData() {
        //showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_CONSENT_CHECK, jsonObject, Constants.REQUESTCODE_CONSENT_CHECK);
    }

    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.INPUT_DATE_FORMAT);
        return sdf.format(new Date());
    }

    protected void showDialog(boolean isShow, String message) {

        if (isShow) {
            if (mProgressDialog == null)
                mProgressDialog = new ProgressDialog(getActivity());
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

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    protected synchronized void requestTypeGET(String url, int requestCode) {
        if (!InformatePreferences.getStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED).equalsIgnoreCase("true")) {
            if (Utility.isConnectedToInternet(getActivity())) {
                mAppRequest = new AsyncHttpRequest(getActivity(), url, null, requestCode, AsyncHttpRequest.Type.GET);
                mAppRequest.setRequestListener(this);
                mAppRequest.execute();
            } else {
                dismissDialog();
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));

            }
        } else {

            if (lastBackPressTime < System.currentTimeMillis() - 6000) {
                InformatePreferences.getStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED).equalsIgnoreCase("false");
            }
            dismissDialog();

        }

    }

    public synchronized void requestTypePost(String url, JSONObject jsonobject, int requestCode) {
        if (!InformatePreferences.getStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED).equalsIgnoreCase("true")) {
            if (Utility.isConnectedToInternet(context)) {
                if (requestCode == Constants.REQUEST_UNSUBSCRIBE_USER || requestCode == Constants.REQUEST_UNSUBSCRIBE_DELETE_USER) {
                    DBAdapter dbAdapter = new DBAdapter(context);
                    dbAdapter.clearLog();
                    dbAdapter.deleteAllRecords();

                }

                Log.d("newone1:","newone1:"+url);
                Log.d("newone2:","newone2:"+jsonobject.toString());
                Log.d("newone3:","newone3:"+requestCode);


                mAppRequest = new AsyncHttpRequest(getActivity(), url, jsonobject.toString(), requestCode, AsyncHttpRequest.Type.POST);
                mAppRequest.setRequestListener(this);
                mAppRequest.execute();
            } else {
                dismissDialog();
                if (getActivity() != null)
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                else
                    dismissDialog();
            }
        } else {
            if (lastBackPressTime < System.currentTimeMillis() - 6000) {
                InformatePreferences.getStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED).equalsIgnoreCase("false");
            }
            dismissDialog();
        }
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        dismissDialog();
        JSONObject jsonObject = null;
        if (requestCode == REQUEST_GET_OPINION_POLL_QUESTIONS) {
            vLayout(response, requestCode);
        } else {
            try {
                jsonObject = new JSONObject(response);
                try {

                    if (requestCode == Constants.REQUEST_AVAILABLE_POINTS) {
                        InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_AVAILABLEPOINTS_, jsonObject.optString("AvailablePoints"));
                    }

                    if (jsonObject != null && jsonObject.has("LanguageCulture") && !TextUtils.isEmpty(jsonObject.getString("LanguageCulture"))) {
                        Utility.setLocaleLanguage(jsonObject.getString("LanguageCulture"), getActivity());
                    }
                    if (!jsonObject.getBoolean("IsUserActive")) {
                        Log.d("TPS_HTTP", "res " + response);
                        //callUnScribe();
                        if (!TextUtils.isEmpty(jsonObject.getString("ResubscribeMessage"))) {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.alert).setMessage(jsonObject.getString("ResubscribeMessage"))
                                    .setPositiveButton("Rejoin", (dialog, which) -> {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        LoginManager.getInstance().logOut();
                                        Intent intent = new Intent(context, UnSubscribeActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    })
                                    .setNegativeButton("Exit", (dialog, which) -> {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        LoginManager.getInstance().logOut();
                                        Intent intent = new Intent(context, SignUpActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.alert).setMessage(getResources().getString(R.string.user_msg))
                                    .setPositiveButton("Exit", (dialog, which) -> {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        editor.commit();
                                        LoginManager.getInstance().logOut();
                                        Intent intent = new Intent(context, SignUpActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    } else {
                        Log.e("TPS_HTTP", "res " + response);
                        vLayout(response, requestCode);
                    }
                } catch (Exception e) {
                    if (e.getMessage().equals("No value for IsUserActive")) {
                        vLayout(response, requestCode);
                    }
                }

            } catch (JSONException e) {
                Integer pointsinreviewcount = Integer.parseInt(AvailableSurveyFragment.txt_points_inreview_available.getText().toString());
                if (requestCode == 1234) {
                    vLayout(response, requestCode);
                }
                else if (requestCode == 12345) {
                    vLayout(response, requestCode);
                } else if (pointsinreviewcount == 0){
                    vLayout(response, requestCode);
                }

                if (e.getMessage().equals("No value for IsUserActive")) {
                    vLayout(response, requestCode);
                }
            }
        }


    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        if (isAdded()) {
            dismissDialog();
            if (e != null) {
                String message = e.getMessage();
                if (!TextUtils.isEmpty(message)) {
                    if (message.equals("UnknownHostException")) {
                        showErrorAlert(" ", "Please check your internet connection settings.");
                    } else
                        showErrorAlert(" ", getString(R.string.msg_serverdown));
                } else {
                    showErrorAlert(" ", "Please check your internet connection settings.");
                }
            }
        }
    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

    @Override
    public void onDestroyView() {
        /*
         * if (mAppRequest != null) if (mAppRequest.getStatus() !=
         * AsyncTask.Status.FINISHED) mAppRequest.cancel(true);
         */
        super.onDestroyView();

    }

    protected void _callUnsubscribeApi(String otherReason, String ids) {
        if (TextUtils.isEmpty(ids)) {
            showErrorAlert("Error", getString(R.string.please_select_reason));
            return;
        }
        showDialog(true, getString(R.string.dialog_login));
        int lastIndexOfComma = ids.lastIndexOf(",");
        String finalIds;
        if (lastIndexOfComma > 0)
            finalIds = ids.substring(0, lastIndexOfComma);
        else
            finalIds = ids;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject.put("ReasonForOther", otherReason);
            jsonObject.put("ReasonIds", finalIds);
            //  jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_UNSUBSCRIBE_USER, jsonObject, Constants.REQUEST_UNSUBSCRIBE_USER);
    }

    protected void _callUnsubscribeDeleteApi(String otherReason, String ids) {
        if (TextUtils.isEmpty(ids)) {
            showErrorAlert("Error", getString(R.string.please_select_reason));
            return;
        }
        showDialog(true, getString(R.string.dialog_login));
        int lastIndexOfComma = ids.lastIndexOf(",");
        String finalIds;
        if (lastIndexOfComma > 0)
            finalIds = ids.substring(0, lastIndexOfComma);
        else
            finalIds = ids;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject.put("ReasonForOther", otherReason);
            jsonObject.put("ReasonIds", finalIds);
            //  jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_UNSUBSCRIBE_DELETE_USER, jsonObject, Constants.REQUEST_UNSUBSCRIBE_DELETE_USER);
    }

    protected void _showUnsubscribeSuccessfulDialog() {
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE);
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE_VALUE);
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_unsubscribe_successful_2);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
        LoginManager.getInstance().logOut();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.clearLog();
        ImageView closeDialog = dialog.findViewById(R.id.iv_close_error);
        TextView tv_reg_link = dialog.findViewById(R.id.tv_reg_link);
        TextView tv_last_email = dialog.findViewById(R.id.tv_last_email);
        Linkify.addLinks(tv_reg_link, Linkify.ALL);
        Linkify.addLinks(tv_last_email, Linkify.ALL);
        tv_reg_link.setMovementMethod(LinkMovementMethod.getInstance());
        tv_last_email.setMovementMethod(LinkMovementMethod.getInstance());
        closeDialog.setOnClickListener(v -> {
            // Utility.hideKeyboard(v, context);
            Utility.setLocaleLanguage("en", getActivity());
            Intent intent = new Intent(context, UnSubscribeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            dialog.dismiss();
            ((Activity) context).finish();
        });
        dialog.show();
    }

    protected void _showUnsubscribeDeleteSuccessfulDialog() {
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE);
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE_VALUE);
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_unsubscribe_delete_successful);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
        LoginManager.getInstance().logOut();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.clearLog();
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(v -> {
            // Utility.hideKeyboard(v, context);
            Utility.setLocaleLanguage("en", getActivity());
            Intent intent = new Intent(context, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            dialog.dismiss();
            ((Activity) context).finish();
        });

        dialog.show();
    }

    protected void _clearSesionOnLogout() {
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE);
        InformatePreferences.remove(getContext(), Constants.PREF_LOCALECODE_VALUE);

        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();
        LoginManager.getInstance().logOut();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.clearLog();

        Utility.setLocaleLanguage("en", getActivity());
        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //  dialog.dismiss();
        ((Activity) context).finish();
    }

    protected void showErrorAlert(String title, String message) {
        if (isAdded()) {
            if (activity != null) {
                DialogFragment fragment = AlertDialogErrorFragment.newInstance("", message);
                try {
                    fragment.show(activity.getSupportFragmentManager(), "");
                } catch (Exception e) {

                }
            }
        }
    }

    protected void requestUpdatePoints() {
        try {
            if (Utility.isConnectedToInternet(getActivity())) {
                showDialog(true, getString(R.string.dialog_login));

                requestTypePost(Constants.GETINCENTIVEDETAILSMOBILE, new ParseJSonObject(getActivity()).getSessionObject(), Constants.REQUEST_AVAILABLE_POINTS);
            } else {
                showErrorAlert(" ", getString(R.string.msg_low_conn));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callGetGeneralRewardsnewAPI() {

        try {
            if (Utility.isConnectedToInternet(getActivity())) {
                showDialog(true, getString(R.string.dialog_login));

                Log.e("GeneralRewardInput",new ParseJSonObject(getActivity()).getSessionObject().toString());
                requestTypePost(Constants.API_GETGENERALREWARDSNEW, new ParseJSonObject(getActivity()).getSessionObject(), Constants.GETGENERALREWARDSNEW_CODE);
            } else {
                showErrorAlert(" ", getString(R.string.msg_low_conn));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public abstract void vLayout(String res, int requestcode);

    public abstract void rError(int requestcode);

    protected void _showOptOutDialog(Dialog gdprDialog) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_opt_out);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        TextView textView13 = dialog.findViewById(R.id.textView13);
        Button btn_opt_out = dialog.findViewById(R.id.btn_opt_out);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Linkify.addLinks(textView13, Linkify.ALL);
        textView13.setMovementMethod(LinkMovementMethod.getInstance());
        textView13.setClickable(true);

        btn_cancel.setOnClickListener(v -> dialog.dismiss());

        btn_opt_out.setOnClickListener(v -> {
            gdprDialog.dismiss();
            _callUnsubscribeApi("GDPR Compliance not agreed", "8");
            dialog.dismiss();
        });

        dialog.show();
    }

    protected void _showGDPRConsentForm(Context context, OnConsentListener onConsentListener) {

        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_consent_form);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        ImageView closeDialog = dialog.findViewById(R.id.iv_close_error);
        TextView lastTv = dialog.findViewById(R.id.textView11);
        TextView textView7 = dialog.findViewById(R.id.textView7);
        RadioGroup radioGroup1 = dialog.findViewById(R.id.radioGroup);
        TextView tv_in_eu_unions = dialog.findViewById(R.id.tv_in_eu_unions);
        RadioGroup radioGroup2 = dialog.findViewById(R.id.radioGroup2);
        RadioGroup radioGroup3 = dialog.findViewById(R.id.radioGroup3);
        RadioButton rb_one_yes = dialog.findViewById(R.id.rb_one_yes);
        RadioButton rb_one_no = dialog.findViewById(R.id.rb_one_no);
        RadioButton rb_two_yes = dialog.findViewById(R.id.rb_two_yes);
        RadioButton rb_two_no = dialog.findViewById(R.id.rb_two_no);
        Button btn_proceed = dialog.findViewById(R.id.btn_proceed);//PREF_HELP_DESK_EMAIL
        Group group_consent_form = dialog.findViewById(R.id.group_consent_form);
        Group group_second_question = dialog.findViewById(R.id.group_second_question);
        group_consent_form.setVisibility(View.GONE);
        group_second_question.setVisibility(View.GONE);
        lastTv.setText(Html.fromHtml(getTermsConditionText2(context)));
        lastTv.setClickable(true);
        String helpDeskWhole = InformatePreferences.getStringPrefrence(context, Constants.PREF_HELP_DESK_EMAIL);
        String helpDeskEmail;
        String[] arr = helpDeskWhole.split(" ");
        if (!TextUtils.isEmpty(helpDeskWhole) && arr.length > 3) {
            helpDeskEmail = arr[3];
        } else {
            helpDeskEmail = helpDeskWhole;
        }
        textView7.setText(getString(R.string.if_at_any_point_custom, helpDeskEmail));
        Linkify.addLinks(textView7, Linkify.ALL);
        lastTv.setMovementMethod(LinkMovementMethod.getInstance());
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            isFirstConsentGiven = true;
            radioGroup2.clearCheck();
            radioGroup3.clearCheck();
            group_second_question.setVisibility(checkedId == R.id.rb_one_yes ? View.GONE : View.VISIBLE);
            group_consent_form.setVisibility(checkedId == R.id.rb_one_yes ? View.VISIBLE : View.GONE);
            isSecondConsentGiven = radioGroup1.getCheckedRadioButtonId() == R.id.rb_one_yes;
            isSecondAnswerNo = !(radioGroup1.getCheckedRadioButtonId() == R.id.rb_one_yes);
            isFirstAnswerYes = radioGroup1.getCheckedRadioButtonId() == R.id.rb_one_yes;
        });

        radioGroup2.setOnCheckedChangeListener(((group, checkedId) -> {
            radioGroup3.clearCheck();
            isSecondConsentGiven = radioGroup2.getCheckedRadioButtonId() == R.id.rb_two_no || radioGroup2.getCheckedRadioButtonId() == R.id.rb_two_yes;
            if (radioGroup1.getCheckedRadioButtonId() == R.id.rb_one_no) {
                if (radioGroup2.getCheckedRadioButtonId() == R.id.rb_two_no) {
                    isSecondAnswerNo = true;
                    isFinalConsentGiven = true;
                } else {
                    isSecondAnswerNo = false;
                    isFinalConsentGiven = false;
                }
                group_consent_form.setVisibility(checkedId == R.id.rb_two_yes ? View.VISIBLE : View.GONE);
            }
        }));

        radioGroup3.setOnCheckedChangeListener(((group, checkedId) -> {
            isFinalConsentGiven = radioGroup3.getCheckedRadioButtonId() == R.id.rb_agree || radioGroup3.getCheckedRadioButtonId() == R.id.rb_not_agree;
        }));

//        closeDialog.setOnClickListener(v -> dialog.dismiss());
        btn_proceed.setOnClickListener(v -> {
            if (isFirstConsentGiven && isSecondConsentGiven && isFinalConsentGiven) {

                RadioButton radioButton = dialog.findViewById(radioGroup3.getCheckedRadioButtonId());

                if (radioButton != null && radioButton.getText().toString().equalsIgnoreCase("No, I don't agree")) {
                    if (InformatePreferences.getStringPrefrence(context, Constants.PREF_LOGINSUCCESS).equalsIgnoreCase("true")) {
                        _showOptOutDialog(dialog); // dialog to be dismissed
                    } else {
                        showErrorAlert("", getString(R.string.msg_eu_region_decline_consent_form));
                    }

                } else {
                    if (isSecondAnswerNo) {
                        if (onConsentListener != null) {
                            RadioButton radioButton1 = dialog.findViewById(radioGroup1.getCheckedRadioButtonId());
                            RadioButton radioButton2 = dialog.findViewById(radioGroup2.getCheckedRadioButtonId());
                            String str_selected_radio_from_rg_1 = radioButton1.getText().toString().equalsIgnoreCase(getString(R.string.txt_no)) ? "No" : "Yes";
                            String str_selected_radio_from_rg_2 = radioButton2.getText().toString().equalsIgnoreCase(getString(R.string.txt_no)) ? "No" : "Yes";

                            onConsentListener.onConsentGiven(str_selected_radio_from_rg_1,
                                    str_selected_radio_from_rg_2,
                                    "",
                                    dialog);
                        }
                    } else if (!isFirstAnswerYes) {
                        if (onConsentListener != null) {
                            RadioButton radioButton1 = dialog.findViewById(radioGroup1.getCheckedRadioButtonId());
                            RadioButton radioButton2 = dialog.findViewById(radioGroup2.getCheckedRadioButtonId());
                            RadioButton radioButton3 = dialog.findViewById(radioGroup3.getCheckedRadioButtonId());
                            String str_selected_radio_from_rg_1 = radioButton1.getText().toString().equalsIgnoreCase(getString(R.string.txt_no)) ? "No" : "Yes";
                            String str_selected_radio_from_rg_2 = radioButton2.getText().toString().equalsIgnoreCase(getString(R.string.txt_no)) ? "No" : "Yes";
                            String str_selected_radio_from_rg_3 = radioButton3.getText().toString().equalsIgnoreCase(getString(R.string.no_i_dont_agree)) ? "No, I don't agree" : "Yes, I agree";

                            onConsentListener.onConsentGiven(str_selected_radio_from_rg_1,
                                    str_selected_radio_from_rg_2,
                                    str_selected_radio_from_rg_3, dialog);
                        }
                    } else {
                        if (onConsentListener != null) {
                            RadioButton radioButton1 = dialog.findViewById(radioGroup1.getCheckedRadioButtonId());
                            // RadioButton radioButton2 = dialog.findViewById(radioGroup2.getCheckedRadioButtonId());
                            RadioButton radioButton3 = dialog.findViewById(radioGroup3.getCheckedRadioButtonId());

                            String str_selected_radio_from_rg_1 = radioButton1.getText().toString().equalsIgnoreCase(getString(R.string.txt_no)) ? "No" : "Yes";
                            String str_selected_radio_from_rg_3 = radioButton3.getText().toString().equalsIgnoreCase(getString(R.string.no_i_dont_agree)) ? "No, I don't agree" : "Yes, I agree";
                            onConsentListener.onConsentGiven(str_selected_radio_from_rg_1,
                                    "No",
                                    str_selected_radio_from_rg_3, dialog);
                        }
                    }
                    dialog.setOnDismissListener(dialog1 -> {
                        isFirstConsentGiven = false;
                        isSecondConsentGiven = false;
                        isFinalConsentGiven = false;
                        isFirstAnswerNo = false;
                        isSecondAnswerNo = false;
                        isFirstAnswerYes = false;
                    });
                }


            }
        });
        dialog.show();
    }

    public interface OnConsentListener {
        void onConsentGiven(String consentOne, String consentTwo, String consentFinal, Dialog dialog);
    }

}
