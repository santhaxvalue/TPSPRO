package com.panelManagement.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.panelManagement.activity.R;
import com.panelManagement.model.GeneralRedeemModels;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class RedeemFragmentCorousal extends Fragment {


    GeneralRedeemModels reward;
    static final String ARG_RES_ID = "Res_id";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ImageView iv = view.findViewById(R.id.view_pager_img);

        if (!TextUtils.isEmpty(reward.getImageURL())) {
            Picasso.get()
                    .load(reward.getImageURL())
                    .noFade()
                    .placeholder(R.drawable.placeholder)
                    .into(iv);
        }

        GradientDrawable myGrad = (GradientDrawable) iv.getBackground();
        myGrad.setStroke(4, getRandomColor());

        return view;
    }

    public static RedeemFragmentCorousal newInstance(GeneralRedeemModels general) {
        RedeemFragmentCorousal redeemFragmentNew = new RedeemFragmentCorousal();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RES_ID, general);
        redeemFragmentNew.setArguments(bundle);
        return redeemFragmentNew;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reward = (GeneralRedeemModels) getArguments().getSerializable(ARG_RES_ID);
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}