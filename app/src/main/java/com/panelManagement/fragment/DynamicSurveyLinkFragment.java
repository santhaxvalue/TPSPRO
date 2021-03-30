package com.panelManagement.fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Utility;
public class DynamicSurveyLinkFragment extends BaseFragment implements View.OnClickListener {
    private View fragView;
    private WebView webView;
    private static final String KEYLINK = "link";
    private String link;
    public static DynamicSurveyLinkFragment newInstance(String link) {
        Bundle args = new Bundle();
        DynamicSurveyLinkFragment fragment = new DynamicSurveyLinkFragment();
        args.putString(KEYLINK, link);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        if (budle != null) {
            link = budle.getString(KEYLINK);
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragView == null) {
            ViewGroup nullView = null;
            View rootView = inflater.inflate(R.layout.fragment_dynamic_survey_link, nullView, false);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
            getActivity().findViewById(R.id.bottom_bar).setVisibility(View.GONE);
            getActivity().findViewById(R.id.fab_btn).setVisibility(View.GONE);
            //getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
            FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
            params.setMargins(0, 0, 0, Utility.getDp(context, -20));
            layout.setLayoutParams(params);
            webView = rootView.findViewById(R.id.ic_webview);
            webView.setClickable(true);
            WebSettings wSettings = webView.getSettings();
            wSettings.setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setUseWideViewPort(true);
            WebClientClass webViewClient = new WebClientClass();
            webView.setWebViewClient(webViewClient);
            WebChromeClient webChromeClient = new WebChromeClient();
            webView.setWebChromeClient(webChromeClient);
            if (!TextUtils.isEmpty(link))
                webView.loadUrl(link);
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
