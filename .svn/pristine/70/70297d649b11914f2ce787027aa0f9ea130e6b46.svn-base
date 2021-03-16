package com.panelManagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.kochava.android.tracker.EventParameters;
import com.kochava.android.tracker.EventType;
import com.kochava.android.tracker.Feature;
import com.mobileapptracker.MATEvent;
import com.mobileapptracker.MobileAppTracker;
import com.panelManagement.activity.HomeFragmentActivity;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ConfirmationFragment extends Fragment implements OnClickListener {
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmation, null);

        view.findViewById(R.id.btn_startearning).setOnClickListener(this);
        AdWordsConversionReporter.reportWithConversionId(getActivity(), "949442329", "Y6_eCNajxmEQma7dxAM", "60.00", true);

        try {
            MobileAppTracker mobileAppTracker = MobileAppTracker.getInstance();
            mobileAppTracker.measureEvent(MATEvent.REGISTRATION);
            initializeKochava();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
                    intent.putExtra(HomeFragmentActivity.KEY_REGISTER, true);
                    startActivity(intent);
                    getActivity().finish();
                }
            };
            timer.schedule(timerTask, 20000, 20000);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
        intent.putExtra(HomeFragmentActivity.KEY_REGISTER, true);
        startActivity(intent);
        getActivity().finish();
    }

    private void initializeKochava() {
        EventParameters eventParameters = new EventParameters(EventType.RegistrationComplete);
        eventParameters.userId(InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_ID));
        eventParameters.userName(InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME));
        HashMap<String, Object> datamap = new HashMap<String, Object>();
        datamap.put(Feature.INPUTITEMS.KOCHAVA_APP_ID, "kothepanelstation-fuq9a3xx");
        datamap.put(Feature.INPUTITEMS.REQUEST_ATTRIBUTION, true);

        /*new Feature(getActivity(), datamap);*/
        new Feature(getActivity(), datamap).eventStandard(eventParameters);
    }

}
