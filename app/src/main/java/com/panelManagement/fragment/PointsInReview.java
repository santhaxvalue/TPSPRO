package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.EarnedPointHistory;
import com.panelManagement.model.PointsInReviewModel;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.view.PointInReviewAdapter;
import com.panelManagement.view.RewardHistoryListAdapter;
import com.panelManagement.widgets.TextViewPlusBold;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_APPEND;
import static com.panelManagement.activity.HomeActivity.tt;


public class PointsInReview extends BaseFragment {

    ArrayList<EarnedPointHistory> rewardsarray;
    RewardHistoryListAdapter adaptorSurvey;
    RewardPointsModels rewardsPointsData;
    RelativeLayout layout;
    TextView txtValuePointEarned;
    TextView txtValuePointRedeem;
    TextView txtValuePointAvailable;
    Bundle extras;
    public TextViewPlusBold head;
    private RecyclerView mListView;
//    private TextView txt_points_earned_reward_history;
    TextView txt_points_earned_reward_historygo;
    TextView pirTitle, pirc1, pirc2, pirc3, pirc4, pirc5, pp;
    public static CardView pirCardView;
    public ImageView img_earned_points,img_earned_warning;
    TextView emptyText;

    ArrayList<String> surveyNameList = new ArrayList<>();
    ArrayList<String> reviewPointsList = new ArrayList<>();
    ArrayList<String> ticketList = new ArrayList<>();
    ArrayList<String> surveyIdList = new ArrayList<>();
    ArrayList<String> SurveyCompletionDateList = new ArrayList<>();

    public PointsInReview() {
        // TODO Auto-generated constructor stub
    }

    public static PointsInReview newInstance(RewardPointsModels pointsModels) {
//    public static PointsInReview newInstance() {

        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, pointsModels);
        PointsInReview fragment = new PointsInReview();
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pointsinreview_header, container, false);

        txt_points_earned_reward_historygo = getActivity().findViewById(R.id.txt_points_earned_reward_historygo);


        try {
            JSONObject jsonObjectPIR = new JSONObject();
            jsonObjectPIR.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            jsonObjectPIR.put("UserId", "2965968");
//            jsonObjectPIR.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            jsonObjectPIR.put("UserId", "2967111");



            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UserId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("Userid", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject1.put("LanguageCulture", InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

            Log.d("userid1:","userid1:"+ InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            Log.d("userid11:","userid11:"+InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

            requestTypePost(Constants.API_SURVEY_POINTS_REVIEW, jsonObjectPIR, Constants.REQUESTCODE_SURVEYPOINTSREVIEWS);
            requestTypePost(Constants.API_SURVEY_POINTS_REVIEW, jsonObject, Constants.REQUESTCODE_SURVEYPOINTSREVIEW);

            requestTypePost(Constants.API_SURVEY_POINTS_REJECTED, jsonObject1, Constants.REQUESTCODE_SURVEYPOINTSREJECTED);

            HomeActivity.setRewardsBackground();
            getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
//            txt_points_earned_reward_history = getActivity().findViewById(R.id.txt_points_earned_reward_history);
            head = view.findViewById(R.id.head);
            Spannable span2 = new SpannableString(head.getText());
            span2.setSpan(new RelativeSizeSpan(1.3f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            head.setText(span2);
            //old code
//            head.setVisibility(View.VISIBLE);
//            txtValuePointEarned = view.findViewById(R.id.txt_points_earned_reward_history);
            //       txtValuePointRedeem = view.findViewById(R.id.txt_points_redeemed);
            //     txtValuePointAvailable = view.findViewById(R.id.txt_points_available);

            if (rewardsPointsData != null) {
//                txtValuePointEarned.setText(rewardsPointsData.getEarnedPoints());
//                txt_points_earned_reward_history.setText(rewardsPointsData.getEarnedPoints());

//                txtValuePointEarned.setText(rewardsPointsData.getPointsReview());
//                txt_points_earned_reward_history.setText(rewardsPointsData.getPointsReview());



                txt_points_earned_reward_historygo.setText(rewardsPointsData.getPointsReview());


//            PointsEarned.txt_points_earned_reward_history.setText(rewardsPointsData.getEarnedPoints());
                // txtValuePointRedeem.setText(rewardsPointsData.getSpentPoints());
                //txtValuePointAvailable.setText(rewardsPointsData.getAvailablePoints().substring(0, rewardsPointsData.getAvailablePoints().indexOf(".")));
            }



            mListView = view.findViewById(R.id.lv_survey);
            emptyText = view.findViewById(R.id.empty_text);

            pirCardView = view.findViewById(R.id.pirCardView);
            pirCardView.setVisibility(View.GONE);
            pirTitle = view.findViewById(R.id.pirTitle);
            pirc1 = view.findViewById(R.id.pirc1);
            pirc2 = view.findViewById(R.id.pirc2);
            pirc3 = view.findViewById(R.id.pirc3);
            pirc4 = view.findViewById(R.id.pirc4);
            pirc5 = view.findViewById(R.id.pirc5);
            pp = view.findViewById(R.id.tv_pointsEarned);
            img_earned_points = view.findViewById(R.id.img_i);
            img_earned_warning = view.findViewById(R.id.img_ii);
            pp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), ",mfbdn,jfvdhfv", Toast.LENGTH_SHORT).show();
                }
            });
            getActivity().findViewById(R.id.img_i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getActivity(), ",mfbdn,jfvdhfv", Toast.LENGTH_SHORT).show();
                    mListView.setVisibility(View.GONE);
                    head.setVisibility(View.GONE);
                    pirCardView.setVisibility(View.VISIBLE);
                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    hide.setDuration(1000);
                    pirCardView.startAnimation(hide);
                }
            });

            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListView.setVisibility(View.GONE);
                    head.setVisibility(View.GONE);
                    pirCardView.setVisibility(View.VISIBLE);
                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    hide.setDuration(1000);
                    pirCardView.startAnimation(hide);
                }
            });
           getActivity().findViewById(R.id.header_pointsinreview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListView.setVisibility(View.GONE);
                    head.setVisibility(View.GONE);
                    pirCardView.setVisibility(View.VISIBLE);
                    Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    hide.setDuration(1000);
                    pirCardView.startAnimation(hide);

                }
            });



            //lv_survey_list.setEm(emptyText);
            mListView.setLayoutManager(new LinearLayoutManager(context));
            if (rewardsarray != null || rewardsarray.size() != 0) {
                emptyText.setVisibility(View.GONE);
//RR               mListView.setAdapter(new PointInReviewAdapter(getActivity(), rewardsarray));
            } else {
                emptyText.setVisibility(View.VISIBLE);
            }
            // setting animation for layout
            layout = view.findViewById(R.id.reward_history_layout);
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

        Log.d("requestcode1111:","requestcode1111:"+requestcode);
        Log.d("response4444:","response4444:"+res);
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

       Integer pointsinreview = Integer.parseInt(AvailableSurveyFragment.txt_points_inreview_available.getText().toString());
       Log.d("pointsinreview:","pointsinreview:"+pointsinreview);

        if (requestcode == 1234 || pointsinreview == 0) {
            getPointsInReviewValue(res);
            head.setVisibility(View.GONE);
            //old code
//            getActivity().findViewById(R.id.img_i).setVisibility(View.GONE);
            //old code
            //new code
            img_earned_points.setVisibility(View.GONE);
            //new code

            //old code
//            getActivity().findViewById(R.id.img_ii).setVisibility(View.VISIBLE);
            //old code
            //new code
            img_earned_warning.setVisibility(View.VISIBLE);
            //new code
            emptyText.setVisibility(View.VISIBLE);
        }else {
            getPointsInReviewListValue(res);
            head.setVisibility(View.VISIBLE);
            //old code
//            getActivity().findViewById(R.id.img_ii).setVisibility(View.GONE);
            //old code
            //new code
            img_earned_warning.setVisibility(View.GONE);
            //new code
            emptyText.setVisibility(View.GONE);
        }

    }

    @Override
    public void rError(int requestcode) {
        Log.v("Error", requestcode + "");
    }


    private void getPointsInReviewValue(String res) {
        try {
            JSONArray jsonarray = new JSONArray(res);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String ReviewPointsText1 = jsonobject.getString("ReviewPointsText1");
                String ReviewPointsText2 = jsonobject.getString("ReviewPointsText2");
                String ReviewPointsText3 = jsonobject.getString("ReviewPointsText3");
                String ReviewPointsText4 = jsonobject.getString("ReviewPointsText4");
                String ReviewPointsText5 = jsonobject.getString("ReviewPointsText5");
                String ReviewPointsText6 = jsonobject.getString("ReviewPointsText6");
                pirTitle.setText(ReviewPointsText1);
                pirc1.setText(ReviewPointsText2);
                pirc2.setText(ReviewPointsText3);
                pirc3.setText(ReviewPointsText4);
                pirc4.setText(ReviewPointsText5);
                pirc5.setText(ReviewPointsText6);
            }
//            pirCardView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
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
                String ReviewPoints = jsonobject.getString("ReviewPoints");
                String TicketDetails = jsonobject.getString("TicketDetails");
                String SurveyId = jsonobject.getString("SurveyId");
                String strSurveyCompletionDate = jsonobject.getString("SurveyCompletionDate");

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

            emptyText.setVisibility(View.GONE);
            mListView.setLayoutManager(new LinearLayoutManager(context));
            mListView.setAdapter(new PointInReviewAdapter(getActivity(), rewardsarray,surveyNameList,reviewPointsList,ticketList,surveyIdList,SurveyCompletionDateList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

