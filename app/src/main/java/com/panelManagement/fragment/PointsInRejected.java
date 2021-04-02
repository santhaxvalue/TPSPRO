package com.panelManagement.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.EarnedPointHistory;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.view.PointInReviewAdapter;
import com.panelManagement.view.PointsInRejectedAdapter;
import com.panelManagement.view.RewardHistoryListAdapter;
import com.panelManagement.widgets.TextViewPlusBold;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.panelManagement.activity.HomeActivity.tt;

public class PointsInRejected extends BaseFragment {


    ArrayList<EarnedPointHistory> rewardsarray;
    RewardHistoryListAdapter adaptorSurvey;
    RewardPointsModels rewardsPointsData;
    RelativeLayout layout;
    TextView txtValuePointEarned12;
    TextView txtValuePointRedeem;
    TextView txtValuePointAvailable;
    Bundle extras;
    public TextViewPlusBold head1;
    private RecyclerView mListView1;
    private TextView txt_points_earned_reward_history12;
    TextView pirTitle1, pirc11, pirc21, pirc31, pirc41, pirc51, pp1;
    public static CardView pirCardView1;
    public ImageView img_earned_points1,img_earned_warning1;
    TextView emptyText1;

    ArrayList<String> surveyNameList = new ArrayList<>();
    ArrayList<String> reviewPointsList = new ArrayList<>();
    ArrayList<String> ticketList = new ArrayList<>();
    ArrayList<String> surveyIdList = new ArrayList<>();
    ArrayList<String> SurveyCompletionDateList = new ArrayList<>();

    public PointsInRejected() {
        // TODO Auto-generated constructor stub
    }

    public static PointsInRejected newInstance(RewardPointsModels pointsModels) {
//    public static PointsInReview newInstance() {

        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, pointsModels);
        PointsInRejected fragment = new PointsInRejected();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            rewardsPointsData = (RewardPointsModels) getArguments().get(RewardPointsFragment.KEYLIST);
        }
        if (rewardsPointsData != null) {
            rewardsarray = rewardsPointsData.getArrayEarnedPoint();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pointsinrejected_header, container, false);
        try {
            JSONObject jsonObjectPIR = new JSONObject();
            jsonObjectPIR.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            jsonObjectPIR.put("UserId", "2965968");
//            jsonObjectPIR.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            jsonObjectPIR.put("UserId", "2967111");



//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("Userid", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject1.put("LanguageCulture", InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

            Log.d("userid1:","userid1:"+ InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            Log.d("userid11:","userid11:"+InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

//            requestTypePost(Constants.API_SURVEY_POINTS_REVIEW, jsonObjectPIR, Constants.REQUESTCODE_SURVEYPOINTSREVIEWS);
//            requestTypePost(Constants.API_SURVEY_POINTS_REVIEW, jsonObject, Constants.REQUESTCODE_SURVEYPOINTSREVIEW);

            requestTypePost(Constants.API_SURVEY_POINTS_REJECTED, jsonObject1, Constants.REQUESTCODE_SURVEYPOINTSREJECTED);

            HomeActivity.setRewardsBackground();
            getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
            txt_points_earned_reward_history12 = getActivity().findViewById(R.id.txt_points_earned_reward_history12);
            head1 = view.findViewById(R.id.head1);
            Spannable span2 = new SpannableString(head1.getText());
            span2.setSpan(new RelativeSizeSpan(1.3f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            head1.setText(span2);
            //old code
//            head.setVisibility(View.VISIBLE);
            txtValuePointEarned12 = view.findViewById(R.id.txt_points_earned_reward_history12);
            //       txtValuePointRedeem = view.findViewById(R.id.txt_points_redeemed);
            //     txtValuePointAvailable = view.findViewById(R.id.txt_points_available);

            Log.d("pointsinreview_points1:","pointsinreview_points11:"+rewardsPointsData.getPointsRejected());


            if (rewardsPointsData != null) {
                txtValuePointEarned12.setText(rewardsPointsData.getPointsRejected());
                txt_points_earned_reward_history12.setText(rewardsPointsData.getPointsRejected());
//            PointsEarned.txt_points_earned_reward_history.setText(rewardsPointsData.getEarnedPoints());
                // txtValuePointRedeem.setText(rewardsPointsData.getSpentPoints());
                //txtValuePointAvailable.setText(rewardsPointsData.getAvailablePoints().substring(0, rewardsPointsData.getAvailablePoints().indexOf(".")));
            }

            mListView1 = view.findViewById(R.id.lv_survey1);
            emptyText1 = view.findViewById(R.id.empty_text1);

            pirCardView1 = view.findViewById(R.id.pirCardView1);
            pirCardView1.setVisibility(View.GONE);
            pirTitle1 = view.findViewById(R.id.pirTitle1);
            pirc11 = view.findViewById(R.id.pirc11);
            pirc21 = view.findViewById(R.id.pirc21);
            pirc31 = view.findViewById(R.id.pirc31);
            pirc41 = view.findViewById(R.id.pirc41);
            pirc51 = view.findViewById(R.id.pirc51);
            pp1 = view.findViewById(R.id.tv_pointsEarned1);
            img_earned_points1 = view.findViewById(R.id.img_i1);
            img_earned_warning1 = view.findViewById(R.id.img_ii1);
            pp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), ",mfbdn,jfvdhfv", Toast.LENGTH_SHORT).show();
                }
            });
            getActivity().findViewById(R.id.img_i1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), ",mfbdn,jfvdhfv", Toast.LENGTH_SHORT).show();
//                    mListView1.setVisibility(View.GONE);
//                    head1.setVisibility(View.GONE);
//                    pirCardView1.setVisibility(View.VISIBLE);
//                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//                    hide.setDuration(1000);
//                    pirCardView1.startAnimation(hide);
                }
            });

            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mListView1.setVisibility(View.GONE);
//                    head1.setVisibility(View.GONE);
//                    pirCardView1.setVisibility(View.VISIBLE);
//                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//                    hide.setDuration(1000);
//                    pirCardView1.startAnimation(hide);
                }
            });


            getActivity().findViewById(R.id.header_pointsinrejected).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mListView1.setVisibility(View.GONE);
//                    head1.setVisibility(View.GONE);
//                    pirCardView1.setVisibility(View.VISIBLE);
//                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
//                    hide.setDuration(1000);
//                    pirCardView1.startAnimation(hide);

                }
            });



            //lv_survey_list.setEm(emptyText);
            mListView1.setLayoutManager(new LinearLayoutManager(context));
            if (rewardsarray != null || rewardsarray.size() != 0) {
                emptyText1.setVisibility(View.GONE);
//RR               mListView.setAdapter(new PointInReviewAdapter(getActivity(), rewardsarray));
            } else {
                emptyText1.setVisibility(View.VISIBLE);
            }
            // setting animation for layout
            layout = view.findViewById(R.id.reward_history_layout1);
            Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            hide.setDuration(1000);
            layout.startAnimation(hide);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

    @Override
    public void vLayout(String res, int requestcode) {


        Log.d("requestcode1111222:","requestcode1111222:"+requestcode);
        Log.d("response4444222:","response4444222:"+res);
//        PointsInReviewModel pointsInReviewModel = new PointsInReviewModel();
//        pointsInReviewModel.setSurveyName();

        //old code for changes
//        if (requestcode == 1234)
//            getPointsInReviewValue(res);
//        else {
//            getPointsInReviewListValue(res);
//        }
//        Log.v("Response", res);

        //New code for changes

        // Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data

// Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data

//        Integer pointsinreview = Integer.parseInt(AvailableSurveyFragment.txt_points_inreview_available.getText().toString());
        Integer pointsinrejected = Integer.parseInt(rewardsPointsData.getPointsRejected());


        Log.d("pointsinrejected:","pointsinrejected:"+pointsinrejected);

        if (requestcode == 1234 || pointsinrejected == 0) {
//            getPointsInReviewValue(res);
            head1.setVisibility(View.GONE);
            getActivity().findViewById(R.id.img_i1).setVisibility(View.GONE);
            getActivity().findViewById(R.id.img_ii1).setVisibility(View.VISIBLE);
            emptyText1.setVisibility(View.VISIBLE);

        }else {
            getPointsInReviewListValue(res);
            head1.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.img_ii1).setVisibility(View.GONE);
            emptyText1.setVisibility(View.GONE);
        }

    }

    @Override
    public void rError(int requestcode) {

        {
            Log.v("Error", requestcode + "");

        }
    }

    private void getPointsInReviewListValue(String res) {
        try {
            JSONArray jsonarray = new JSONArray(res);
//            ArrayList<String> surveyNameList = new ArrayList<>();
//            ArrayList<String> reviewPointsList = new ArrayList<>();
//            ArrayList<String> ticketList = new ArrayList<>();
//            ArrayList<String> surveyIdList = new ArrayList<>();
//            ArrayList<String> SurveyCompletionDateList = new ArrayList<>();


            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String SurveyName = jsonobject.getString("SurveyName");
                String ReviewPoints = jsonobject.getString("RejectedPoints");
                String TicketDetails = jsonobject.getString("TicketDetails");
                String SurveyId = jsonobject.getString("SurveyId");
                String strSurveyCompletionDate = jsonobject.getString("_SurveyCompletionDate");

                Log.d("step1:","step1:"+SurveyName);
                Log.d("step2:","step2:"+ReviewPoints);
                Log.d("step3:","step3:"+TicketDetails);
                Log.d("step4:","step4:"+SurveyId);
                Log.d("step5:","step5:"+strSurveyCompletionDate);

                surveyNameList.add(SurveyName);
                reviewPointsList.add(ReviewPoints);
                ticketList.add(TicketDetails);
                surveyIdList.add(SurveyId);
                SurveyCompletionDateList.add(strSurveyCompletionDate);
            }

            for(int i=0;i<surveyNameList.size();i++){
                Log.d("surveryName:","surveyName:"+surveyNameList.get(i));
            }

            emptyText1.setVisibility(View.GONE);
            mListView1.setLayoutManager(new LinearLayoutManager(context));
            mListView1.setAdapter(new PointsInRejectedAdapter(getActivity(),surveyNameList,reviewPointsList,ticketList,surveyIdList,SurveyCompletionDateList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
