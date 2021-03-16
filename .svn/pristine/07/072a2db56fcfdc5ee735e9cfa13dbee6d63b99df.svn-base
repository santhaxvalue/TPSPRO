package com.panelManagement.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.TextViewCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.R;
import com.panelManagement.activity.SurveyViewActivity;
import com.panelManagement.fragment.ProfilerFragment;
import com.panelManagement.fragment.SurveyViewFragment;
import com.panelManagement.model.SurveyModels;
import com.panelManagement.utils.Utility;

import java.util.ArrayList;

public class SurveyListAdapter extends PagerAdapter {

    LinearLayout layout;
    ImageView dropImg, targetImg;
    private ArrayList<SurveyModels> items;
    private Context context;
    private OnPuzzleCompleted mListener;

    public SurveyListAdapter(Context context, ArrayList<SurveyModels> value, OnPuzzleCompleted mListener) {
        this.items = value;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.survey_list_item, container, false);
        final SurveyModels profile = items.get(position);
        TextView txtHead = view.findViewById(R.id.tv_SurveyName);
        TextView txtSurveyDate = view.findViewById(R.id.tv_date);
        TextView surveyEarn = view.findViewById(R.id.tv_number);
        layout = view.findViewById(R.id.ll_survey);
        ImageButton nextArrow = view.findViewById(R.id.next_arrow);


        TextViewCompat.setAutoSizeTextTypeWithDefaults(txtHead, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        txtHead.setText(profile.getSurveyName());

        String formattedDate;
        if (profile.isMobileDiary()) {
            formattedDate = Utility.formatDateTimeFromString(txtSurveyDate.getContext(), "yyyy-MM-dd'T'HH:mm:ss", profile.getEndTime());
        } else {
            formattedDate = Utility.formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy", profile.getEndTime());
        }

        txtSurveyDate.setText(String.format("%1$s %2$s", layout.getContext().getResources().getString(R.string.txt_expired), formattedDate));
        surveyEarn.setText(String.format("%s %s", profile.getPoints(), layout.getContext().getResources().getString(R.string.txt_points)));

        nextArrow.setOnClickListener((View v) -> {
            SurveyModels value = items.get(position);
            Intent intent = new Intent(context, SurveyViewActivity.class);
            String url = Uri.parse(value.getSurveyLink()).toString();
            intent.putExtra(SurveyViewFragment.SURVEY_URL_KEY, url);
            ((Activity) context).startActivityForResult(intent, ProfilerFragment.RESULT_PROFILER);
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public interface OnPuzzleCompleted {
        void onPuzzleCompleted();
    }

}
