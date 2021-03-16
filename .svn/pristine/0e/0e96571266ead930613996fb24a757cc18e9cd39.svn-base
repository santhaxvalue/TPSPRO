package com.panelManagement.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;


public class AlertTnCFragment extends BaseFragment implements OnClickListener {

    ProgressDialog pd;
    private WebView browser;
    private LinearLayout ll_error;
    private String failUrl;

    public static AlertTnCFragment newInstance() {
        AlertTnCFragment frag = new AlertTnCFragment();
        return frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_privacypolicy, container, false);

        if (rootView != null) {
            rootView.findViewById(R.id.error_btn).setOnClickListener(this);
            ll_error = rootView.findViewById(R.id.ll_error);
            ll_error.setVisibility(View.GONE);

            browser = rootView.findViewById(R.id.webView);
            browser.setWebViewClient(new MyBrowser());
            open();
        }
        return rootView;
    }

    public void open() {
        String url = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_TNC);
        if (Utility.isConnectedToInternet(getActivity())) {
            pogressDialog();
            browser.getSettings().setLoadsImagesAutomatically(true);
            browser.setVisibility(View.VISIBLE);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            browser.loadUrl(url);
        } else {
            Utility.alertDialogCustom(getActivity(), R.string.msg_low_conn, true);
        }
    }

    private void pogressDialog() {
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.dialog_login));
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    public void errorLoadingRedirect(String url) {
        pogressDialog();
        ll_error.setVisibility(View.GONE);
        browser.setVisibility(View.VISIBLE);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_btn:
                if (Utility.isConnectedToInternet(getActivity()))
                    errorLoadingRedirect(failUrl);

                break;

            default:
                break;
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url.replace("tel:", "")));
                startActivity(intent);
            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                if (!pd.isShowing()) {
                    pd.show();
                }
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("on onPageFinished");
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            System.out.println("on onReceivedError");
            browser.clearCache(true);
            browser.loadUrl("about:blank");
            browser.setVisibility(View.GONE);
            ll_error.setVisibility(View.VISIBLE);
            failUrl = failingUrl;
            System.out.println("on failUrl" + failUrl);
        }
    }

}