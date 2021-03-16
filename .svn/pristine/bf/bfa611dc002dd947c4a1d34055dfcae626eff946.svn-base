package com.panelManagement.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panelManagement.activity.R;

public class PointsRedeemed extends Fragment {
    public static TextView txt_points_redeemed_redemption_history;
    public PointsRedeemed() {
    }

    public static PointsRedeemed newInstance() {
        return new PointsRedeemed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_redemption_history, container, false);
        txt_points_redeemed_redemption_history = view.findViewById(R.id.txt_points_redeemed_redemption_history);
        return view;
    }
}
