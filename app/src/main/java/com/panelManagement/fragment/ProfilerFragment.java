package com.panelManagement.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.HomeFragmentActivity;
import com.panelManagement.activity.ProfilerSurveyActivity;
import com.panelManagement.activity.R;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.ProfilerListAdapter;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.SeekArcView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfilerFragment extends BaseFragment implements OnClickListener, OnTouchListener, OnprofileSurveyListener {

    public static final int RESULT_PROFILER = 2;
    public static boolean isGeneralSurvey;
    SeekArcView mSeekArcProgressProfiler;
    TextView mSeekArcProgress, tv_point_complete_profile;
    ArrayList<ProfilerModels> listprofiledata = new ArrayList<ProfilerModels>();
    ProfilerListAdapter adaptorProfiledata;
    GridView listview;
    ParseJSonObject jsonObject;
    String campaignId;
    DBAdapter database;
    LinearLayout linearLayout;
    private View fragView;
    private String mCampaignNameSelected;

    public static ProfilerFragment newInstance() {
        return new ProfilerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = DBAdapter.getDBAdapter(getActivity());
        adaptorProfiledata = new ProfilerListAdapter(getActivity(), listprofiledata, this);
        jsonObject = new ParseJSonObject(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragView == null) {
            ViewGroup nullView = null;

            View view = inflater.inflate(R.layout.fragment_profiler, container, false);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);

            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);

            getActivity().findViewById(R.id.toolbar_main).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.profile_image).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.user_name).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.email_id).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.phone_ll).setVisibility(View.VISIBLE);

            ((TextView) getActivity().findViewById(R.id.user_name)).setGravity(Gravity.CENTER_HORIZONTAL);
            ((TextView) getActivity().findViewById(R.id.user_name)).setText(String.format("%s %s",
                    InformatePreferences.getStringPrefrence(context, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(context, Constants.PREF_LASTNAME)));
            ((TextView) getActivity().findViewById(R.id.phone_num)).setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(context, Constants.PREF_ISDCODE),
                    InformatePreferences.getStringPrefrence(context, Constants.PREF_MOBILENUMBER)));
            HomeActivity.setSurvayBackground();

            FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
            Utility.setMargins(context, layout, 0);

            mSeekArcProgressProfiler = view.findViewById(R.id.seekArc_circular1);
            mSeekArcProgressProfiler.setOnTouchListener(this);
            mSeekArcProgressProfiler.setProgressWidth(8);


            mSeekArcProgress = view.findViewById(R.id.seekArcProgress);

            tv_point_complete_profile = view.findViewById(R.id.tv_point_complete_profile);

            listview = view.findViewById(R.id.ic_profiler_gridview);
            listview.setAdapter(adaptorProfiledata);

            fragView = view;
        }

        ViewGroup parent = (ViewGroup) fragView.getParent();
        if (parent != null) {
            parent.removeView(fragView);
        }
        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!InformatePreferences.getBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, false)) {
            _checkConsentData();
        } else {
            request();
        }
    }

    private void request() {
        showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.API_GETPANELISTPROFILERSINFO, jsonObject.getSessionObject(), Constants.REQUESTCODE_PROFILERINFO);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View arg0) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();
        // Utility.showToast(getActivity(), res);
        switch (requestcode) {
            case Constants.REQUESTCODE_PROFILERINFO:
                try {
                    JSONObject object = new JSONObject(res);
                    int percentage = Integer.parseInt(object.getString("OverallCompletionPercentage"));
                    //Constants.EnableMetering = object.getBoolean("MeteringSwitch");
                    //metering log
                    //HomeActivity.startMeterPrefrences();
                    mSeekArcProgressProfiler.setProgress(percentage);
                    mSeekArcProgressProfiler.setAnimation(mSeekArcProgressProfiler, Integer.parseInt(object.getString("OverallCompletionPercentage")));
                    mSeekArcProgress.setText(object.getString("OverallCompletionPercentage") + "%");

                    listprofiledata.clear();
                    listprofiledata.addAll(jsonObject.getProfilerInfo(new JSONObject(res)));
                    int allAddedIncentive = 0;
                    for (int i = 0; i < listprofiledata.size(); i++) {
                        int incentive = 0;
                        ProfilerModels profileModel = listprofiledata.get(i);
                        if (!TextUtils.isEmpty(profileModel.getIncentives())) {
                            incentive = Integer.parseInt(profileModel.getIncentives());
                        }
                        allAddedIncentive = allAddedIncentive + incentive;
                    }
                    InformatePreferences.putInt(getActivity(), Constants.PREF_PROFILERCOMPLETE, percentage);
                    if (percentage == 100) {
                        tv_point_complete_profile.setText(getString(R.string.profilecompleted));
                    } else {
                        String pointsvalue = String.format(getResources().getString(R.string.txt_complete_profile_point_1), allAddedIncentive);
                        if (pointsvalue.length() > 80)
                            tv_point_complete_profile.setTextSize(10.0f);
                        tv_point_complete_profile.setText(pointsvalue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adaptorProfiledata.notifyDataSetInvalidated();
                adaptorProfiledata.notifyDataSetChanged();
                linearLayout = fragView.findViewById(R.id.container);
                Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                hide.setDuration(1000);
                linearLayout.setAnimation(hide);
                break;
            case Constants.REQUESTCODE_CAMPAIGN:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        Intent intent = new Intent(getActivity(), ProfilerSurveyActivity.class);
                        intent.putExtra(ProfilerSurveyActivity.KEY_JSONOBJECT, res);
                        intent.putExtra(ProfilerSurveyActivity.KEY_CAMPAIGNAME, mCampaignNameSelected);
                        intent.putExtra(ProfilerSurveyActivity.KEY_CAMPAIGNID, campaignId);
                        startActivityForResult(intent, RESULT_PROFILER);
                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    showErrorAlert("error", e.getMessage());
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_CONSENT_CHECK:
                try {
                    JSONObject responseOB = new JSONObject(res);
                    InformatePreferences.setStringPrefrence(context, Constants.PREF_HELP_DESK_EMAIL, responseOB.optString("HelpdeskEmailId"));
                    if (responseOB.optBoolean("Status")) {
                        request();
                        boolean statusFlag = responseOB.optBoolean("Status");
                        InformatePreferences.putBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, statusFlag);
                        Log.e("Consent", responseOB.optString("Message"));
                    } else {
                        _showGDPRConsentForm(context, ((consentOne, consentTwo, consentFinal, dialog) -> {
                            try {
                                showDialog(true, getString(R.string.dialog_login));
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("ConsentText1", consentOne);
                                jsonObject.put("ConsentText2", consentTwo);
                                jsonObject.put("ConsentGiven", consentFinal);
                                jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
                                jsonObject.put("ConsentVersion", "v1.0");
                                jsonObject.put("IPAddress", PanelConstants.DEVICE_ID);
                                jsonObject.put("BrowserVersion", PanelConstants.DEVICE_ID);
                                jsonObject.put("BrowserExtraInfo", PanelConstants.DEVICE_ID);

                                requestTypePost(Constants.API_CONSENT_LOG_SAVE, jsonObject, Constants.REQUESTCODE_CONSENT_LOG_SAVE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }));
                        Log.e("Consent", responseOB.optString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_CONSENT_LOG_SAVE:
                try {
                    request();
                    JSONObject responseOB = new JSONObject(res);
                    boolean statusFlag = responseOB.optBoolean("Status");
                    InformatePreferences.putBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, statusFlag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_UNSUBSCRIBE_USER:
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(res);
                    boolean status = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("Message");
                    if (status) {
                        _showUnsubscribeSuccessfulDialog();
                    } else {
                        showErrorAlert("", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void rError(int requestcode) {
        dismissDialog();
    }

    @Override
    public void onProfilerHandler(ProfilerModels model) {
        if (model != null) {
            if (model.getCampaignID().equals("10744"))
                ProfilerFragment.isGeneralSurvey = true;
            else ProfilerFragment.isGeneralSurvey = false;
            try {
                InformatePreferences.setStringPrefrence(getActivity(), Constants.ISLANGUAGECHANGECALLED, "false");
                campaignId = model.getPanelistCampaignId();
                /*   if (!database.isPanelistExist(campaignId)) {*/
                if (Utility.isConnectedToInternet(getActivity())) {
                    showDialog(true, getString(R.string.dialog_login));
                    mCampaignNameSelected = model.getCampaignName();
                    requestTypePost(Constants.API_GETCAMPAIGNXML, jsonObject.getUserCampaign(campaignId), Constants.REQUESTCODE_CAMPAIGN);
                } else {
                    dismissDialog();
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dismissDialog();
            }
        } else {
            isGeneralSurvey = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_PROFILER) {
            request();
            ((TextView) getActivity().findViewById(R.id.user_name)).setText(String.format("%s %s", InformatePreferences.getStringPrefrence(context, Constants.PREF_FIRSTNAME),
                    InformatePreferences.getStringPrefrence(context, Constants.PREF_LASTNAME)));
        }
    }

    @Override
    public void onPageChange(int pageno) {
    }

    @Override
    public void onUIUpdate(int color) {
    }

    @Override
    public void checkSurveyCompleted(boolean isTrue) {
    }

    @Override
    public void fragmentUpdate(int position) {

    }
}
