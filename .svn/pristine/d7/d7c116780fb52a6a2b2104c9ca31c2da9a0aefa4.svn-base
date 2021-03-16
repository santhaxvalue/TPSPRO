package com.panelManagement.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.panelManagement.activity.R;

public class MatchThePuzzleWebViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String url;
    private WebView browser;


    public MatchThePuzzleWebViewFragment() {
    }

    public static MatchThePuzzleWebViewFragment newInstance(String param1) {
        MatchThePuzzleWebViewFragment fragment = new MatchThePuzzleWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_the_puzzle_web_view, container, false);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        browser = view.findViewById(R.id.webview);
        // browser.setWebViewClient(new AlertTnCFragment.MyBrowser());
        browser.loadUrl(url);
        return view;
    }

}
