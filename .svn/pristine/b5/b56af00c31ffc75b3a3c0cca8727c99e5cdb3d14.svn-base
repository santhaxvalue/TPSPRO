package com.panelManagement.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.RedeemPointHistory;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.view.RedemptionHistoryListAdapter;
import com.panelManagement.widgets.TextViewPlusBold;

import java.util.ArrayList;

public class RedemptionHistoryFragment extends Fragment {

    TextView txtValuePointEarned;
    TextView txtValuePointRedeem;
    TextView txtValuePointAvailable;
    RedemptionHistoryListAdapter adaptorRedemption;
    ArrayList<RedeemPointHistory> redemptionHistroyarray;
    Bundle extras;
    RewardPointsModels rewardsPointsData;
    RelativeLayout relativeLayout;
    private TextViewPlusBold head_red_hist;
    private TextView txt_points_redeemed_redemption_history;

    public static RedemptionHistoryFragment newInstance(RewardPointsModels pointsModels) {

        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, pointsModels);
        RedemptionHistoryFragment fragment = new RedemptionHistoryFragment();
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
            redemptionHistroyarray = rewardsPointsData.getArrayRedeemPoint();
            adaptorRedemption = new RedemptionHistoryListAdapter(getActivity(), redemptionHistroyarray);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_redemption_history, container, false);

        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        HomeActivity.setRewardsBackground();
        head_red_hist = view.findViewById(R.id.head_red_hist);
        Spannable span2 = new SpannableString(head_red_hist.getText());
        txt_points_redeemed_redemption_history = getActivity().findViewById(R.id.txt_points_redeemed_redemption_history);
        span2.setSpan(new RelativeSizeSpan(1.3f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        head_red_hist.setText(span2);
        //txtValuePointEarned = view.findViewById(R.id.txt_points_earned);
        txtValuePointRedeem = view.findViewById(R.id.txt_points_redeemed_redemption_history);
        //txtValuePointAvailable = view.findViewById(R.id.txt_points_available);
        RecyclerView mListView = view.findViewById(R.id.lv_survey);
        TextView emptyText = view.findViewById(R.id.txtNoRewardPoints);

        if (rewardsPointsData != null) {
            // txtValuePointEarned.setText(rewardsPointsData.getEarnedPoints());
        //    txtValuePointRedeem.setText(rewardsPointsData.getSpentPoints());
            txt_points_redeemed_redemption_history.setText(rewardsPointsData.getSpentPoints());
            // txtValuePointAvailable.setText(rewardsPointsData.getAvailablePoints().substring(0, rewardsPointsData.getAvailablePoints().indexOf(".")));
        }

        if (redemptionHistroyarray == null || redemptionHistroyarray.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.INVISIBLE);
        }

        mListView.setLayoutManager(new LinearLayoutManager(getContext()));

        mListView.setAdapter(adaptorRedemption);

        relativeLayout = view.findViewById(R.id.redemption_layout);
        Animation hide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }
}
