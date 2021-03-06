package com.panelManagement.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.webservices.AsyncHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BadgeFragment extends BaseFragment implements AsyncHttpRequest.RequestListener {

    public static BadgeFragment newInstance() {
        BadgeFragment fragment = new BadgeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basges, container, false);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(false);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        HomeActivity.setRewardsBackground();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);
        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_INREGISTRATIONPROCESS, "");
        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LOGINSUCCESS, "true");
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        view.findViewById(R.id.container_medal).setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleProgressbar(View.VISIBLE);
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(getContext(), Constants.API_SURVEY_BADGE_DESCRIPTION, new JSONObject().toString(), Constants.REQUEST_SURVEY_BADGE_DESCRIPTION, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    private void toggleProgressbar(int visibility) {
        if (getView() != null)
            getView().findViewById(R.id.loading_progress).setVisibility(visibility);
    }


    @Override
    public void vLayout(String res, int requestcode) {
        toggleProgressbar(View.GONE);
    }

    @Override
    public void rError(int requestcode) {
        toggleProgressbar(View.GONE);
    }


    @Override
    public void onRequestCompleted(String response, int requestCode) {
        toggleProgressbar(View.GONE);
        switch (requestCode) {
            case Constants.REQUEST_SURVEY_BADGE_DESCRIPTION:
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("surveyBadgeList")) {
                        JSONArray jsonArray = object.getJSONArray("surveyBadgeList");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject != null && jsonObject.has("BadgeName") && jsonObject.has("BadgeDescription")) {
                                switch (i) {
                                    case 3:
                                        if (getView() != null) {
                                            ((TextView) (getView().findViewById(R.id.bronze_badge_title))).setText(jsonObject.getString("BadgeName"));
                                            ((TextView) (getView().findViewById(R.id.bronze_badge_desc))).setText(jsonObject.getString("BadgeDescription"));
                                        }
                                        break;
                                    case 2:
                                        if (getView() != null) {
                                            ((TextView) (getView().findViewById(R.id.silver_badge_title))).setText(jsonObject.getString("BadgeName"));
                                            ((TextView) (getView().findViewById(R.id.silver_badge_desc))).setText(jsonObject.getString("BadgeDescription"));
                                        }
                                        break;
                                    case 1:
                                        if (getView() != null) {
                                            ((TextView) (getView().findViewById(R.id.gold_badge_title))).setText(jsonObject.getString("BadgeName"));
                                            ((TextView) (getView().findViewById(R.id.gold_badge_desc))).setText(jsonObject.getString("BadgeDescription"));
                                        }
                                        break;
                                    case 0:
                                        if (getView() != null) {
                                            ((TextView) (getView().findViewById(R.id.platinum_badge_title))).setText(jsonObject.getString("BadgeName"));
                                            ((TextView) (getView().findViewById(R.id.platinum_badge_desc))).setText(jsonObject.getString("BadgeDescription"));
                                        }
                                        break;
                                }
                                if (getView() != null)
                                    getView().findViewById(R.id.container_medal).setVisibility(View.VISIBLE);
                            }

                        }
                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
