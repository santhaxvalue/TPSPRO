package com.panelManagement.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.widgets.SeekArcView;

import java.util.ArrayList;
import java.util.Random;

public class ProfilerListAdapter extends ArrayAdapter<ProfilerModels> implements OnTouchListener {

    Handler mHandler = new Handler();
    OnprofileSurveyListener listener;
    boolean isAnimationtrue;
    private ArrayList<ProfilerModels> items;
    private Context context;
    private int filterCount = 0;

    public ProfilerListAdapter(Context context, ArrayList<ProfilerModels> profilerData, OnprofileSurveyListener listener) {
        super(context, R.layout.profiler_survey_status1, profilerData);
        this.items = profilerData;
        this.context = context;
        this.listener = listener;
        this.isAnimationtrue = false;

        Handler mhandler = new Handler();

        mhandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                isAnimationtrue = true;

            }
        }, 5000);
    }

    @Override
    public ProfilerModels getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewWrapper viewWrapper;
        final ProfilerModels profile = items.get(position);
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profiler_survey_status1, null);
            viewWrapper = new ViewWrapper(view);
            view.setTag(viewWrapper);
        }
        else {
            viewWrapper = (ViewWrapper) view.getTag();
        }

        viewWrapper.getProfileName().setText(profile.getCampaignName());
        viewWrapper.getPercentage().setText(String.valueOf(profile.getPercentageOfComplete()) + "%");

        if(position == 0)
        {
            viewWrapper.getPercentage().setBackgroundColor(Color.parseColor("#6E99DE"));
        }
        if(position == 1)
        {
            viewWrapper.getPercentage().setBackgroundColor(Color.parseColor("#FE4845"));
        }
        if(position == 2)
        {
            viewWrapper.getPercentage().setBackgroundColor(Color.parseColor("#EE8C29"));
        }
        if(position == 3)
        {
            viewWrapper.getPercentage().setBackgroundColor(Color.parseColor("#F07556"));
        }
        if(position == 4)
        {
            viewWrapper.getPercentage().setBackgroundColor(Color.parseColor("#94CD89"));
        }

       // int[] colors = new int[]{getRandomColor(), getRandomColor(), getRandomColor()};
        //GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        viewWrapper.getPercentage();

        SeekArcView seekbar = viewWrapper.getSeekBar();
        seekbar.setOnTouchListener(this);
        seekbar.setProgressWidth(8);
        seekbar.setProgress(profile.getPercentageOfComplete());
        if (!isAnimationtrue)
            seekbar.setAnimation(seekbar, profile.getPercentageOfComplete());

        ImageButton editButton = viewWrapper.getEditButton();

       /* if (profile.getPercentageOfComplete() < 100) {
            editButton.setImageResource(R.drawable.ic_edit_grey);
        } else {
            editButton.setImageResource(R.drawable.edit_disabled);
        }*/

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onProfilerHandler(profile);
                }
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setFilterCount(int count) {
        this.filterCount = count;
    }

    public int getFilteredCount() {
        return filterCount;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    class ViewWrapper {
        TextView txtDate;
        View base;
        TextView txtEventName;
        SeekArcView seekbar;
        private ImageButton editButton;

        public ViewWrapper(View base) {
            this.base = base;
        }

        public TextView getProfileName() {
            if (txtDate == null)
                txtDate = base.findViewById(R.id.lbl_survey_title);
            return txtDate;
        }

        public TextView getPercentage() {
            if (txtEventName == null)
                txtEventName = base.findViewById(R.id.lbl_survey_percentage);
            return txtEventName;
        }

        public SeekArcView getSeekBar() {
            if (seekbar == null)
                seekbar = base.findViewById(R.id.seekArc_circular_survey);
            return seekbar;
        }

        public ImageButton getEditButton() {
            if (editButton == null)
                editButton = base.findViewById(R.id.btn_edit);
            return editButton;
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
