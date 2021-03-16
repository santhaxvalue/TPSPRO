package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnprofileSurveyListener;

public class AlertThanksFragment extends Fragment {
    String purchasedPoints;
    private OnprofileSurveyListener listener;

    @SuppressLint("ValidFragment")
    public AlertThanksFragment(String pointsNum) {
        this.purchasedPoints = pointsNum;
    }


    public AlertThanksFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnprofileSurveyListener) {
            listener = (OnprofileSurveyListener) activity;
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_thanks, container, false);
        TextView tv_purchased = (TextView) rootView.findViewById(R.id.tv_purchased_ticket);
        tv_purchased.setText(String.format(getResources().getString(R.string.txt_puchased_ticket_part1), purchasedPoints));
        if (listener != null)
            listener.onUIUpdate(R.color.aqua_dark);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //RedeemFragmentActivity.mainCointenar.setVisibility(View.GONE);
        //RedeemFragmentActivity.mainCointenar.setBackgroundResource(R.color.aqua_dark);
    }
}