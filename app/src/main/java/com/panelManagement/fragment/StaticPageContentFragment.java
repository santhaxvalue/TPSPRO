package com.panelManagement.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.StaticPageContentModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static com.panelManagement.utils.Constants.REQUEST_GET_STATIC_PAGE_CONTENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticPageContentFragment extends BaseFragment {


    private static String mPageContentType;
    private Context context;
    private ParseJSonObject mParseJsonObject;
    private WebView mPageContentWebview;

    public StaticPageContentFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void vLayout(String res, int requestcode) {
        try {
            switch (requestcode) {

                case REQUEST_GET_STATIC_PAGE_CONTENT:
                    StaticPageContentModel content = mParseJsonObject.getStaticPageContent(new JSONObject(res));
                    if (content != null && !(TextUtils.isEmpty(content.getPageContent()))) {
                        mPageContentWebview.getSettings().setJavaScriptEnabled(true);
                        mPageContentWebview.loadDataWithBaseURL("", content.getPageContent(), "text/html", "UTF-8", "");
                    } else {
                        showErrorAlert("", "Content is unavailable");
                        getActivity().onBackPressed();
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rError(int requestcode) {

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_static_page_content, container, false);
        context = this.getContext();

        mParseJsonObject = new ParseJSonObject(getActivity());
        mPageContentWebview = view.findViewById(R.id.terms_and_condition_txt);

        getActivity().findViewById(R.id.toolbar_main).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.tv_page_title).setVisibility(View.VISIBLE);


        ((HomeActivity) getActivity()).setPageTitle(getPageTitle());
        getActivity().findViewById(R.id.ll_profile_details).setVisibility(View.GONE);
        getActivity().findViewById(R.id.profile_image).setVisibility(View.GONE);
        getActivity().findViewById(R.id.user_name).setVisibility(View.GONE);
        getActivity().findViewById(R.id.email_id).setVisibility(View.GONE);
        getActivity().findViewById(R.id.phone_ll).setVisibility(View.GONE);

        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);
        HomeActivity.setSurvayBackground();

        callStaticPageContentApi();


        return view;

    }

    private String getPageTitle() {
        if (mPageContentType.equals(Constants.CONTENT_TYPE_PRIVACY_POLICY))

            return (context.getResources().getString(R.string.lbl_privacy_statement));

        else if (mPageContentType.equals(Constants.CONTENT_TYPE_TERMS_AND_CONDITIONS))

            return (context.getResources().getString(R.string.Terms));

        else
            return null;

    }

    public static StaticPageContentFragment newInstance(String contentType) {
        mPageContentType = contentType;
        return new StaticPageContentFragment();
    }


    private void callStaticPageContentApi() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("MarketId", 22);
            jsonObject.put("MarketId", InformatePreferences.getMarketId(context));
            jsonObject.put("ContentType", mPageContentType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_STATIC_PAGE_CONTENT, jsonObject, REQUEST_GET_STATIC_PAGE_CONTENT);
    }

}
