package com.panelManagement.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.panelManagement.fragment.RedeemFragmentCorousal;
import com.panelManagement.model.GeneralRedeemModels;

import java.util.ArrayList;

// Created by Niyaj Centura Technology on 1/11/2018.

public class MultiViewAdapter extends FragmentStatePagerAdapter {

    private ArrayList<GeneralRedeemModels> generalRedeemModels;
    private Context context;

    public MultiViewAdapter(FragmentManager fm, Context context, ArrayList<GeneralRedeemModels> redeemModels) {
        super(fm);
        this.context = context;
        this.generalRedeemModels = redeemModels;
    }

    @Override
    public Fragment getItem(int position) {
        return RedeemFragmentCorousal.newInstance(generalRedeemModels.get(position));
    }

    @Override
    public int getCount() {
        return generalRedeemModels.size();
    }
}
