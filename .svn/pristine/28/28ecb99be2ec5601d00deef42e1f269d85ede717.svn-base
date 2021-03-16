package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.panelManagement.activity.R;
import com.panelManagement.model.SurveyModels;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class SurveyViewFragment extends Fragment implements OnClickListener,
        OnBackStackChangedListener {

    public static final String SURVEY_URL_KEY = "com.panelstation.survey";
    private WebView webView;
    private String mUrl;

    public SurveyViewFragment() {

    }

    public static SurveyViewFragment newInstance() {
        SurveyViewFragment fragment = new SurveyViewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundles = getArguments();
        if (bundles != null)
            mUrl = bundles.getString(SURVEY_URL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_survey_view, container, false);
        webView = (WebView) rootView.findViewById(R.id.ic_panelwebview_survey);
        webView.setClickable(true);
        WebSettings wSettings = webView.getSettings();
        wSettings.setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        WebClientClass webViewClient = new WebClientClass();
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        webView.loadUrl(mUrl);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }

    }

    @Override
    public void onBackStackChanged() {
    }

    public class WebClientClass extends WebViewClient {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!pd.isShowing()) {
                pd.setMessage("Please wait...");
                pd.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
}