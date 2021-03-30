package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

public class FanPageFragments extends BaseFragment implements OnClickListener {

    private View fragView;
    private WebView webView;


    public static FanPageFragments newInstance() {

        Bundle args = new Bundle();

        FanPageFragments fragment = new FanPageFragments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragView == null) {
            ViewGroup nullView = null;
            View rootView = inflater.inflate(R.layout.fragment_fan_page, nullView, false);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
            getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
            getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
            FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            layout.setLayoutParams(params);

            webView = rootView.findViewById(R.id.ic_webview_fanpage);
            webView.setClickable(true);
            WebSettings wSettings = webView.getSettings();
            wSettings.setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            WebClientClass webViewClient = new WebClientClass();
            webView.setWebViewClient(webViewClient);
            WebChromeClient webChromeClient = new WebChromeClient();
            webView.setWebChromeClient(webChromeClient);
            String url = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_FBLINK);

//            if (InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE).equalsIgnoreCase("CN"))
//                webView.loadUrl("http://weibo.com/thepanelstation");
//            else if (!TextUtils.isEmpty(url))
//                webView.loadUrl(url);
//            else
//                webView.loadUrl("https://m.facebook.com/thepanelstationindian?v=wall");

            if (!TextUtils.isEmpty(url))
                webView.loadUrl(url);

            fragView = rootView;
        }

        ViewGroup parent = (ViewGroup) fragView.getParent();
        if (parent != null) {
            parent.removeView(fragView);
        }

        return fragView;
    }

    @Override
    public void onClick(View arg0) {

    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    public class WebClientClass extends WebViewClient {
        ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!pd.isShowing()) {
                pd.setMessage(getString(R.string.dialog_login));
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
