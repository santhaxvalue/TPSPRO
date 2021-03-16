package com.panelManagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.activity.SurveyViewActivity;
import com.panelManagement.model.SurveyModels;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.RoundCornersDrawable;

/**
 * Created by vincentpaing on 6/7/16.
 */
public class OverlapFragment extends Fragment {

    SurveyModels profile;
    static final String ARG_RES_ID = "ARG_RES_ID";

    public static OverlapFragment newInstance(SurveyModels profile) {
        OverlapFragment overlapFragment = new OverlapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RES_ID, profile);
        overlapFragment.setArguments(bundle);
        return overlapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = (SurveyModels) getArguments().getSerializable(ARG_RES_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_content_layout_card, container, false);
        TextView txtHead = view.findViewById(R.id.tv_SurveyName);
        TextView txtSurveyDate = view.findViewById(R.id.tv_date);
        TextView surveyEarn = view.findViewById(R.id.tv_number);
        CardView layout = view.findViewById(R.id.cvVideo);
        ImageView img = view.findViewById(R.id.ivVideoThump);
        LinearLayout linearLayout = view.findViewById(R.id.linear_points);

        TextViewCompat.setAutoSizeTextTypeWithDefaults(txtHead, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        txtHead.setText(profile.getSurveyName()+" [ " +profile.getId()+ " ]");

        if (profile.isMobileDiary()) {
            layout.setCardBackgroundColor(Color.TRANSPARENT);
            linearLayout.setVisibility(View.GONE);
            img.setBackgroundResource(R.drawable.mobile_bg);
        } else {
            layout.setCardBackgroundColor(getResources().getColor(R.color.white));
        }

        String formattedDate;
        if (profile.isMobileDiary()) {
            formattedDate = Utility.formatDateTimeFromString(txtSurveyDate.getContext(), "yyyy-MM-dd'T'HH:mm:ss", profile.getEndTime());
        } else {
            formattedDate = Utility.formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy", profile.getEndTime());
        }

        txtSurveyDate.setText(String.format("%1$s %2$s", getString(R.string.txt_expired), formattedDate));
        surveyEarn.setText(String.format("%s %s", profile.getPoints(), getString(R.string.txt_points)));
        layout.setOnClickListener((View v) -> {
            Intent intent = new Intent(getActivity(), SurveyViewActivity.class);
            String url = Uri.parse(profile.getSurveyLink()).toString();
            intent.putExtra(SurveyViewFragment.SURVEY_URL_KEY, url);
            ((Activity) getContext()).startActivityForResult(intent, ProfilerFragment.RESULT_PROFILER);
        });
        return view;
    }
}


