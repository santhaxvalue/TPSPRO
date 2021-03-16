package com.panelManagement.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.panelManagement.activity.R;
import com.panelManagement.model.GeneralRedeemModels;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class ViewPagerFragment extends BaseFragment {

    private GeneralRedeemModels redeemModels;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    public static ViewPagerFragment newInstance(GeneralRedeemModels generalRedeemModels) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("redeem_model", generalRedeemModels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            redeemModels = (GeneralRedeemModels) getArguments().getSerializable("redeem_model");
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ImageView imageView = view.findViewById(R.id.view_pager_img);
        Picasso.get()
                .load(redeemModels.getImageURL())
                .noFade()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        GradientDrawable myGrad = (GradientDrawable) imageView.getBackground();
        myGrad.setStroke(4, getRandomColor());

        return view;
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
