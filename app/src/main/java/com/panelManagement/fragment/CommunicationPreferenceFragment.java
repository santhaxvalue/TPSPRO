package com.panelManagement.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.model.CommunicationPreferenceItem;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.CommonRecyclerAdapter;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.panelManagement.utils.Constants.REQUEST_GET_COMMUNICATION_PREFERENCE;
import static com.panelManagement.utils.Constants.REQUEST_SAVE_COMMUNICATION_PREFERENCE;

public class CommunicationPreferenceFragment extends BaseFragment implements OnBackClickListener, View.OnClickListener {

    private RecyclerView mCommunicationPrefList;
    private ParseJSonObject mParseJsonObject;
    private ArrayList<CommunicationPreferenceItem> list;
    private CommonRecyclerAdapter mRecyclerAdapter;
    private Button mBbtnSaveCommunicationPreferences;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication_preference, container, false);

        mCommunicationPrefList = view.findViewById(R.id.commPrefQlist);
        mBbtnSaveCommunicationPreferences = view.findViewById(R.id.btn_save_pref);

        mParseJsonObject = new ParseJSonObject(getActivity());

        callCommunicationPrefApi();
        mBbtnSaveCommunicationPreferences.setOnClickListener(this);
        relativeLayout = view.findViewById(R.id.lyt_comm_pref);

        return view;
    }

    private void callCommunicationPrefApi() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_COMMUNICATION_PREFERENCE, jsonObject, REQUEST_GET_COMMUNICATION_PREFERENCE);
    }

    private void callSavePreferenceApi() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
            jsonObject.put("CommPrefID", getSelectedPreferences());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_SAVE_COMMUNICATION_PREFERENCE, jsonObject, REQUEST_SAVE_COMMUNICATION_PREFERENCE);
    }

    public static CommunicationPreferenceFragment newInstance() {
        return new CommunicationPreferenceFragment();
    }

    @Override
    public void vLayout(String res, int requestcode) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(res);
            boolean statusFlag = jsonObject.optBoolean("Status");
            String message = jsonObject.optString("Message");
            animateLayout();
            switch (requestcode) {

                case REQUEST_GET_COMMUNICATION_PREFERENCE:
                    if (statusFlag) {
                        list = mParseJsonObject.getCommunicationPreferenceData(new JSONObject(res));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        mCommunicationPrefList.setLayoutManager(layoutManager);
                        mRecyclerAdapter = new CommonRecyclerAdapter(list);
                        mCommunicationPrefList.setAdapter(mRecyclerAdapter);
                        mRecyclerAdapter.notifyDataSetChanged();
                    } else
                        showErrorAlert("", message);
                    break;

                case REQUEST_SAVE_COMMUNICATION_PREFERENCE:
                    showErrorAlert("", getResources().getString(R.string.comm_pref_save_success));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_pref:
                callSavePreferenceApi();
                break;
        }
    }

    private String getSelectedPreferences() {
        String selectedString = "";
        if (mRecyclerAdapter != null) {
            ArrayList<Integer> selectedIDs = mRecyclerAdapter.getSelectedList();
            selectedString = Utility.getStringFromList(selectedIDs);
        }
        return selectedString;
    }


}
