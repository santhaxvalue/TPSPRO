package com.panelManagement.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.EarnedPointHistory;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.view.RewardHistoryListAdapter;
import com.panelManagement.widgets.TextViewPlusBold;

import java.util.ArrayList;
import java.util.Arrays;

public class RewardHistoryFragment extends BaseFragment {

    ArrayList<EarnedPointHistory> rewardsarray;
    RewardHistoryListAdapter adaptorSurvey;
    RewardPointsModels rewardsPointsData;
    RelativeLayout layout;
    TextView txtValuePointEarned;
    TextView txtValuePointRedeem;
    TextView txtValuePointAvailable;
    Bundle extras;
    TextViewPlusBold head;
    private RecyclerView mListView;
    private TextView txt_points_earned_reward_history;

    public RewardHistoryFragment() {
        // TODO Auto-generated constructor stub
    }

    public static RewardHistoryFragment newInstance(RewardPointsModels pointsModels) {

        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, pointsModels);
        RewardHistoryFragment fragment = new RewardHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //extras = getActivity().getIntent().getExtras();
        if (getArguments() != null) {
            rewardsPointsData = (RewardPointsModels) getArguments().get(RewardPointsFragment.KEYLIST);
        }
        if (rewardsPointsData != null) {
            rewardsarray = rewardsPointsData.getArrayEarnedPoint();
            Log.e("ArrayList", rewardsarray.get(0).getPoints());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rewards_history, container, false);
        HomeActivity.setRewardsBackground();
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        txt_points_earned_reward_history = getActivity().findViewById(R.id.txt_points_earned_reward_history);
        head = view.findViewById(R.id.head);
        Spannable span2 = new SpannableString(head.getText());
        span2.setSpan(new RelativeSizeSpan(1.3f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        head.setText(span2);
        txtValuePointEarned = view.findViewById(R.id.txt_points_earned_reward_history);
        //       txtValuePointRedeem = view.findViewById(R.id.txt_points_redeemed);
        //     txtValuePointAvailable = view.findViewById(R.id.txt_points_available);
        if (rewardsPointsData != null) {
            txtValuePointEarned.setText(rewardsPointsData.getEarnedPoints());
            txt_points_earned_reward_history.setText(rewardsPointsData.getEarnedPoints());
//            PointsEarned.txt_points_earned_reward_history.setText(rewardsPointsData.getEarnedPoints());
            // txtValuePointRedeem.setText(rewardsPointsData.getSpentPoints());
            //txtValuePointAvailable.setText(rewardsPointsData.getAvailablePoints().substring(0, rewardsPointsData.getAvailablePoints().indexOf(".")));
        }

        mListView = view.findViewById(R.id.lv_survey);
        TextView emptyText = view.findViewById(R.id.empty_text);
        //lv_survey_list.setEm(emptyText);
        mListView.setLayoutManager(new LinearLayoutManager(context));
        if (rewardsarray != null || rewardsarray.size() != 0) {
            emptyText.setVisibility(View.GONE);
            mListView.setAdapter(new RewardHistoryListAdapter(getActivity(), rewardsarray));
//            Log.e("ArrayList1", rewardsarray.get(1).getPoints());
        } else {
            emptyText.setVisibility(View.VISIBLE);
        }
        mListView.setEnabled(false);

        // setting animation for layout
        layout = view.findViewById(R.id.reward_history_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        layout.startAnimation(hide);

        return view;
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

}
