package com.panelManagement.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panelManagement.activity.R;


public class Reward2 extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    public static TextView txt_points_earned;
    public static TextView txt_points_redeemed;
    public static TextView txt_points_available;
    private String mParam1;
    private String mParam2;
    private String mParam3;


    public Reward2() {
    }

    public static Reward2 newInstance(String param1, String param2, String param3) {
        Reward2 fragment = new Reward2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.header_points, container, false);
        txt_points_earned = view.findViewById(R.id.txt_points_earned);
        txt_points_redeemed = view.findViewById(R.id.txt_points_redeemed);
        txt_points_available = view.findViewById(R.id.txt_points_available);
        return view;
    }

}
