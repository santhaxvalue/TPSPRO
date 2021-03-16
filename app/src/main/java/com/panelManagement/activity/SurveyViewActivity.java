package com.panelManagement.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.panelManagement.utils.Constants;
import com.panelManagement.widgets.AdvancedWebView;
import com.panelManagement.widgets.AdvancedWebView.Listener;


public class SurveyViewActivity extends Activity implements Listener {

    public static final String SURVEY_URL_KEY = "com.panelstation.survey";
    ProgressDialog mDialog;
    private AdvancedWebView mWebView;
    private String mUrl;
    private boolean isIntentFromNotification = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_view);
        Intent bundles = getIntent();

        // mUrl= getIntent().getStringExtra("surveyUrl");
        String strURL = bundles.getStringExtra("surveyUrl");
        mDialog = new ProgressDialog(SurveyViewActivity.this);
        if (bundles != null) {
            isIntentFromNotification = bundles.hasExtra(Constants.PUSHNOTIFY);
            mUrl = bundles.getStringExtra(SURVEY_URL_KEY);
        }
        mWebView = findViewById(R.id.ic_panelwebview_survey);
        if (savedInstanceState != null)
            mWebView.restoreState(savedInstanceState);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setListener(this, this);


        if (strURL != null) {
            if (!strURL.isEmpty()) {
                mWebView.loadUrl(strURL);
            }
        }


        if (savedInstanceState == null)
            mWebView.loadUrl(mUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDownloadRequested(String arg0, String arg1, String arg2, String arg3, long arg4) {
    }

    @Override
    public void onExternalPageRequest(String arg0) {

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onPageError(int arg0, String arg1, String arg2) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onPageFinished(String arg0) {

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onPageStarted(String arg0, Bitmap arg1) {
        if (!mDialog.isShowing()) {
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(getString(R.string.dialog_login));
            mDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
//		if (!mWebView.onBackPressed()) { return; }
        // check if this intent was fired from notification or not; if yes, finish this intent and start HomeActivity, else, proceed normally
        if (isIntentFromNotification) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mWebView.restoreState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //    onScreenRotate(newConfig);
            // new AdvancedWebView(this);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //      onScreenRotate(newConfig);
            //new AdvancedWebView(this);
        }
    }

    public void onScreenRotate(final Configuration configuration) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(SurveyViewActivity.this);
        dialog.setTitle("Screen rotation will reset all data");
        dialog.show();
        dialog.setCancelable(false);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dialog.dismiss();
            }
        });
    }

}
