package com.panelManagement.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panelManagement.activity.R;


public class PointsEarned extends Fragment {
    public static TextView txt_points_earned_reward_history;

    public PointsEarned() {
    }

    public static PointsEarned newInstance() {
        return new PointsEarned();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_reward_history, container, false);
        txt_points_earned_reward_history = view.findViewById(R.id.txt_points_earned_reward_history);
        return view;
    }

}
