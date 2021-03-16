package com.panelManagement.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;


public class SurveyFloating extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    public static TextView txt_points_redeemed;
    private String mParam1;

    public SurveyFloating() {
    }

    public static Fragment newInstance(String param1) {
        SurveyFloating fragment = new SurveyFloating();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.header_redeem_points, container, false);
        txt_points_redeemed = view.findViewById(R.id.txt_points_redeemed);
        txt_points_redeemed.setText(InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_AVAILABLEPOINTS_));
        return view;
    }

}
