package com.panelManagement.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.model.CustomNotificationItem;
import com.panelManagement.model.CustumCombinedNotificationItem;
import com.panelManagement.model.NotificationLogItem;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.CommonRecyclerAdapter;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.TextViewPlusBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.panelManagement.utils.Constants.REQUEST_GET_ACTIVITYLOGS;
import static com.panelManagement.utils.Constants.REQUEST_GET_CUSTUMNOTIFICATION;
import static com.panelManagement.utils.Constants.REQUEST_GET_NOTIFICATIONLOG;
import static com.panelManagement.utils.Constants.REQUEST_GET_UPDATE_NOTIFICATION_COUNT;

public class NotificationLogFragment extends BaseFragment implements OnBackClickListener {

    private RecyclerView mNotificationList;
    private TextViewPlusBold mNotificationHeader,tv_ActivityLog;
    private ParseJSonObject mParseJsonObject;
    private ArrayList<NotificationLogItem> mNotificationLogItemsList;
    private ArrayList<CustomNotificationItem> mCustomNotificationList;
    private CommonRecyclerAdapter mRecyclerAdapter;
    private ArrayList<CustumCombinedNotificationItem> custumCombinedNotificationItemList = new ArrayList<>();
    private RelativeLayout relativeLayout;
    private int mNotificationCount = 0;
    private TextView mLastLogin;
    private TextView mNoNotification;
    private RelativeLayout mLytActivityLog;

    private TextView mLastLogout;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_logs, container, false);

        mActivity.findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        mActivity.findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        mParseJsonObject = new ParseJSonObject(getActivity());

        initViews(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        callUpdateNotificationCount();
        callNotificationLogAPI();
        callCustumNotificationAPI();
        callGetActivityAPI();
    }

    private void initViews(View view) {
        mNotificationList = view.findViewById(R.id.notificationList);
        mNotificationHeader = view.findViewById(R.id.tv_notificationLog);
        mLastLogin = view.findViewById(R.id.tv_logIn);
        mLastLogout = view.findViewById(R.id.tv_logout);
        mLytActivityLog = view.findViewById(R.id.lut_activity_log);
        mNoNotification = view.findViewById(R.id.no_notifications);
        relativeLayout = view.findViewById(R.id.lyt_notification);
        tv_ActivityLog = view.findViewById(R.id.tv_ActivityLog);
    }

    //InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID)

    private void callNotificationLogAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_NOTIFICATIONLOG, jsonObject, REQUEST_GET_NOTIFICATIONLOG);
    }

    private void callUpdateNotificationCount() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_UPDATE_NOTIFICATION_COUNT, jsonObject, REQUEST_GET_UPDATE_NOTIFICATION_COUNT);
    }

    private void callGetActivityAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_ACTIVITYLOGS, jsonObject, REQUEST_GET_ACTIVITYLOGS);
    }

    private void callCustumNotificationAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_CUSTUMNOTIFICATION, jsonObject, REQUEST_GET_CUSTUMNOTIFICATION);
    }



    public static NotificationLogFragment newInstance() {
        return new NotificationLogFragment();
    }

    @Override
    public void vLayout(String res, int requestcode) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(res);
            boolean statusFlag = jsonObject.optBoolean("Status");
            String message = jsonObject.optString("Message");
            switch (requestcode) {

                case REQUEST_GET_NOTIFICATIONLOG:

                    if (statusFlag) {
                        mNotificationLogItemsList = mParseJsonObject.getNotificationLogData(new JSONObject(res));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mNotificationList.setLayoutManager(layoutManager);

                        //Add notification to the combined notificationList
                        addNotificationLogToCombinedList(mNotificationLogItemsList);

                        setNotificationHeader();

                        showDialog(true, getString(R.string.dialog_login));
                        mNotificationList.setAdapter(mRecyclerAdapter);
                        mRecyclerAdapter.notifyDataSetChanged();
                    } else
                        showErrorAlert("", message);
                    break;

                case REQUEST_GET_CUSTUMNOTIFICATION:
                    showDialog(true, getString(R.string.dialog_login));
                    if (statusFlag) {
                        mCustomNotificationList = mParseJsonObject.getCustomNotificationLogData(new JSONObject(res));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mNotificationList.setLayoutManager(layoutManager);

                        //Add notification to the combined notificationList
                        addCustomNotificationToCombinedList(mCustomNotificationList);
                    } else
                        showErrorAlert("", message);
                    break;
                case REQUEST_GET_ACTIVITYLOGS:
                    statusFlag = true;
                    String lastLogIn = "";
                    String lastLogOut = "";
                    if (statusFlag) {
                        if (jsonObject != null && !TextUtils.isEmpty(jsonObject.getString("LastLogin"))) {
                            lastLogIn = jsonObject.getString("LastLogin");
                        }
                        if (jsonObject != null && !TextUtils.isEmpty(jsonObject.getString("LastLogout"))) {
                            lastLogOut = jsonObject.getString("LastLogout");
                        }
                        animateLayout();
                        //setNotificationHeader();
                        setActivityLogHeader();
                        setActivityLog(lastLogIn, lastLogOut);
                        mRecyclerAdapter = new CommonRecyclerAdapter(custumCombinedNotificationItemList, 1);
                        mNotificationList.setAdapter(mRecyclerAdapter);
                        mRecyclerAdapter.notifyDataSetChanged();

                    } else
                        showErrorAlert("", message);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setActivityLog(String lastLogIn, String lastLogOut) {
        if(!TextUtils.isEmpty(lastLogIn) && !TextUtils.isEmpty(lastLogOut)) {
            mLytActivityLog.setVisibility(View.VISIBLE);
            mLastLogin.setText(String.format("%s %s", getResources().getString(R.string.last_log_in), Utility.getDateFromString(lastLogIn, Constants.INPUT_DATE_UTC_FORMAT, Constants.INPUT_DATE_ACTIVITY_LOG_FORMAT)));
            mLastLogout.setText(String.format("%s %s", getResources().getString(R.string.last_log_out), Utility.getDateFromString(lastLogOut, Constants.INPUT_DATE_UTC_FORMAT, Constants.INPUT_DATE_ACTIVITY_LOG_FORMAT)));
        } else {
            mLytActivityLog.setVisibility(View.GONE);
        }
    }

    private void setNotificationHeader() {
        String header = String.format("%s ", getResources().getString(R.string.notification));

        if(custumCombinedNotificationItemList.size() > 0) {
            mNoNotification.setVisibility(View.GONE);
            header = header + "(" + custumCombinedNotificationItemList.size() + ")";
        } else {
            mNoNotification.setVisibility(View.VISIBLE);
        }

        mNotificationHeader.setText(header);
    }

    private void setActivityLogHeader() {
        String header = String.format("%s ", getResources().getString(R.string.activity));

        if(custumCombinedNotificationItemList.size() > 0) {
            mNoNotification.setVisibility(View.GONE);
            header = header + "(" + custumCombinedNotificationItemList.size() + ")";
        } else {

        }

        tv_ActivityLog.setText(header);
    }

    private void addCustomNotificationToCombinedList(ArrayList<CustomNotificationItem> mCustomNotificationList) {
        if(mCustomNotificationList != null && mCustomNotificationList.size() > 0)
            for (CustomNotificationItem item: mCustomNotificationList) {

                /*
                 * Create type combined CustomNotificationList and add required information
                 */
                CustumCombinedNotificationItem newItem = new CustumCombinedNotificationItem();
                newItem.setCreationDate(item.getCreatedDate());
                newItem.setNotificationMessage(item.getNotificationMessage());

                //insert newItem to the combined list
                custumCombinedNotificationItemList.add(newItem);
            }
    }

    private void addNotificationLogToCombinedList(ArrayList<NotificationLogItem> list) {
        if(list != null && list.size() > 0)
        for (NotificationLogItem item: list) {

            /*
            * Create type combined NotificationLogItemsList and add required information
             */
            CustumCombinedNotificationItem newItem = new CustumCombinedNotificationItem();
            newItem.setCreationDate(item.getCreatedDate());
            newItem.setNotificationMessage(item.getNotification());

            //insert newItem to the combined list
            custumCombinedNotificationItemList.add(newItem);
        }
    }

    private void animateLayout() {
        Animation hide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);
    }


    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onBackButtonPressed() {

    }




}
