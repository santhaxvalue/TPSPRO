package com.panelManagement.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.model.OpinionPollResultItem;
import com.panelManagement.model.OpinionPollsAnswerChoice;
import com.panelManagement.model.QuickPollQuestionItem;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.QuickPollRecyclerAdapter;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import static com.panelManagement.utils.Constants.REQUEST_GET_OPINION_POLL_QUESTIONS;
import static com.panelManagement.utils.Constants.REQUEST_GET_OPINION_POLL_RESULT;
import static com.panelManagement.utils.Constants.REQUEST_GET_SAVE_OPINION_POLLS;

public class QuickPollQuestionsFragment extends BaseFragment implements OnBackClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {


    private RadioGroup mRadioGroup;
    private ParseJSonObject mParseJsonObject;
    private ArrayList<QuickPollQuestionItem> mOpinionPollQuestionList;
    private TextView mQuestionText;
    private ImageView mNextQuestion;
    private ImageView mPrevQuestion;
    private Button mSubmit;
    private Button mPollResult;
    private LinearLayout mBtnLayout;
    private RelativeLayout mQuickPollParentLyt;
    private TextView mFragmentHeading;
    private ScrollView mQuestionView;
    private ScrollView mPollResultView;
    private boolean isResultClicked;

    //variables for pollResult
    private RecyclerView mResultsView;
    private QuickPollRecyclerAdapter mAdapter;
    private TextView mResultQuestionText;
    private ArrayList<OpinionPollResultItem> mOpinionPollResultList = new ArrayList<>();

    private int mSelectedAnswerId = 0;
    private static int mQuestionTypeId;
    private static int mQuestionId;
    private static int mCurrentPostion = 0;

    public QuickPollQuestionsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static QuickPollQuestionsFragment newInstance() {
        QuickPollQuestionsFragment fragment = new QuickPollQuestionsFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void vLayout(String res, int requestcode) {

        switch (requestcode) {
            case REQUEST_GET_OPINION_POLL_QUESTIONS:
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    if (jsonArray != null && jsonArray.length() > 0) {

                        mOpinionPollQuestionList = mParseJsonObject.getQuickPollQuestionList(jsonArray);
                        //set the first question
                        mCurrentPostion = 0;
                        showHideButtonLayout(false);
                        setUi();
                        setDataOnTheScreen(mOpinionPollQuestionList.get(0));
                    } else {
                        showHideButtonLayout(true);
                        showErrorDialogAndRedirectBack(getResources().getString(R.string.no_quick_poll));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case REQUEST_GET_OPINION_POLL_RESULT:
                try {
                    JSONObject object = new JSONObject(res);
                    mOpinionPollResultList = mParseJsonObject.getOpinionPollResult(object);
                    if (mOpinionPollResultList != null && mOpinionPollResultList.size() > 0) {

//                        //Enable next button for navigation from the poll result
//                        mNextQuestion.setEnabled(true);
//                        mNextQuestion.setBackgroundResource(R.drawable.next_click);

                        //make pollResult view Visible and hide QuestionView
                        setQuestionView(false);

                        displayResult();
                    } else {
                        showErrorAlert("", getResources().getString(R.string.error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_GET_SAVE_OPINION_POLLS:
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(res);
                    boolean statusFlag = jsonObject.optBoolean("Status");
                    String message = jsonObject.optString("Message");
                    if (statusFlag) {
                        //remove the submitted question
                        removeQuestionFromTheList(mQuestionId);

                        //show result for the current question
                        showPollResult();

                        // reset variables
                        resetData();

                    } else {
                        showErrorAlert("", message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void showPollResult() {
        callOpinionPollResultAPI();
    }

    private void setQuestionView(boolean show) {
        if (show) {
            mFragmentHeading.setText(getResources().getString(R.string.quick_poll));
            mBtnLayout.setVisibility(View.VISIBLE);
            mQuestionView.setVisibility(View.VISIBLE);
            mPollResultView.setVisibility(View.GONE);
        } else {
            mFragmentHeading.setText(getResources().getString(R.string.poll_result));
            mQuestionView.setVisibility(View.GONE);
            mPollResultView.setVisibility(View.VISIBLE);
            mBtnLayout.setVisibility(View.GONE);
        }
    }

    private void showHideButtonLayout(boolean hide) {
        if (hide) {
            mQuickPollParentLyt.setVisibility(View.GONE);
            mBtnLayout.setVisibility(View.GONE);
        } else {
            mQuickPollParentLyt.setVisibility(View.VISIBLE);
            mBtnLayout.setVisibility(View.VISIBLE);
        }

    }

    private void showErrorDialogAndRedirectBack(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.Ok, (dialog, which) -> {
                    onBackButtonPressed();
                })
                .setCancelable(false)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void moveToNextQuestion() {
        isResultClicked = false;
        setQuestionView(true);
        if (mOpinionPollQuestionList.size() > mCurrentPostion && mCurrentPostion > 0) {
            setDataOnTheScreen(mOpinionPollQuestionList.get(mCurrentPostion));
        } else if (mOpinionPollQuestionList.size() > 0) {
            mCurrentPostion = 0;
            setDataOnTheScreen(mOpinionPollQuestionList.get(0));
        } else {
            onBackButtonPressed();
        }
    }

    private void removeQuestionFromTheList(int mQuestionId) {
        if (mOpinionPollQuestionList.size() > 0) {
            int pos = getPosition(mQuestionId);
            mOpinionPollQuestionList.remove(pos);
        }
    }

    private int getPosition(int mQuestionId) {
        for (int i = 0; i < mOpinionPollQuestionList.size(); i++) {
            if (mOpinionPollQuestionList.get(i).getQuestionID() == mQuestionId)
                return i;
        }
        return 0;
    }

    private void resetData() {
        mQuestionId = 0;
        mSelectedAnswerId = 0;
        mQuestionTypeId = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setDataOnTheScreen(QuickPollQuestionItem questionItem) {
        mQuestionText.setText(questionItem.getQuestionName());
        setOptionsForQuestion(questionItem.getOpinionPollsAnswerChoices());

        mQuestionId = questionItem.getQuestionID();
        mQuestionTypeId = questionItem.getQuestionTypeID();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void rError(int requestcode) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setOptionsForQuestion(List<OpinionPollsAnswerChoice> options) {
        mRadioGroup.setOrientation(LinearLayout.VERTICAL);
        mRadioGroup.removeAllViews();

        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.lyt_radio_btn, null, false);
            radioButton.setId(options.get(i).getAnswerID());
            radioButton.setText(options.get(i).getAnswerChoiceText());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 50, 0, 0);
            radioButton.setLayoutParams(params);


            this.mRadioGroup.addView(radioButton);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_poll_question, container, false);
        initViews(view);

        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);

        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);

        //Avoids overlap while navigated from FAQ's or ContactUs Page
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, Utility.getDp(context, 36));
        layout.setLayoutParams(params);

        mParseJsonObject = new ParseJSonObject(getActivity());

        //call getOpinion poll API
        callOpinionPollAPI();

        return view;
    }

    private void callOpinionPollAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
            jsonObject.put("MarketId", InformatePreferences.getMarketId(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_OPINIONPOLLSQUESTIONS, jsonObject, REQUEST_GET_OPINION_POLL_QUESTIONS);
    }

    private void initViews(View view) {
        mRadioGroup = view.findViewById(R.id.options_radiogroup);
        mQuestionText = view.findViewById(R.id.questionText);
        mNextQuestion = view.findViewById(R.id.next_question);
        mPrevQuestion = view.findViewById(R.id.prev_question);
        mSubmit = view.findViewById(R.id.btn_save_choice);
        mPollResult = view.findViewById(R.id.btn_result);
        mBtnLayout = view.findViewById(R.id.btn_layout);
        mFragmentHeading = view.findViewById(R.id.tv_frag_heading);
        mQuestionView = view.findViewById(R.id.questionView);
        mPollResultView = view.findViewById(R.id.pollResultView);
        mQuickPollParentLyt = view.findViewById(R.id.lyt_quick_poll_parent);

        mResultsView = view.findViewById(R.id.poll_result_item);
        mResultsView.setNestedScrollingEnabled(false);
        mResultQuestionText = view.findViewById(R.id.resultQuestionText);



        mRadioGroup.setOnCheckedChangeListener(this);
        mNextQuestion.setOnClickListener(this);
        mPrevQuestion.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mPollResult.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_question:
                mSelectedAnswerId = 0;
                if (!isResultClicked)
                    mCurrentPostion++;
                setUi();
                moveToNextQuestion();
                break;
            case R.id.prev_question:
                mSelectedAnswerId = 0;
                if (!isResultClicked)
                    mCurrentPostion--;
                setUi();
                moveToNextQuestion();
                break;
            case R.id.btn_save_choice:
                submitPoll();
                break;
            case R.id.btn_result:
                isResultClicked = true;
                showPollResult();
                break;
        }

    }


    private void callOpinionPollResultAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("QuestionId", mQuestionId);
            jsonObject.put("MarketId", InformatePreferences.getMarketId(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_OPINIONPOLLSRESULT, jsonObject, REQUEST_GET_OPINION_POLL_RESULT);
    }


    private void submitPoll() {
        if (mSelectedAnswerId == 0) {
            showErrorAlert("", "Please select any option");
        } else {
            callSubmitPollAPI();
        }

    }

    private void callSubmitPollAPI() {
        showDialog(true, getString(R.string.dialog_login));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(getContext(), Constants.PREF_ID));
            jsonObject.put("QuestionId", mQuestionId);
            jsonObject.put("QuestionTypeId", mQuestionTypeId);
            jsonObject.put("AnswerChoice", mSelectedAnswerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestTypePost(Constants.API_GET_SAVE_OPINION_POLLS, jsonObject, REQUEST_GET_SAVE_OPINION_POLLS);
    }


    private void setUi() {

        if (mCurrentPostion > mOpinionPollQuestionList.size() - 1) {
            mCurrentPostion = 0;
        }

        if ((mOpinionPollQuestionList.size() - 1) == mCurrentPostion) {
            mNextQuestion.setEnabled(false);
            mNextQuestion.setBackgroundResource(R.drawable.next_disable);
        } else {
            mNextQuestion.setEnabled(true);
            mNextQuestion.setBackgroundResource(R.drawable.next_click);
        }
        if (mCurrentPostion == 0) {
            mPrevQuestion.setEnabled(false);
            mPrevQuestion.setBackgroundResource(R.drawable.prev_disable);
        } else {
            mPrevQuestion.setEnabled(true);
            mPrevQuestion.setBackgroundResource(R.drawable.previous);
        }
    }

    private void displayResult() {
        mResultQuestionText.setText(mOpinionPollResultList.get(0).getQuestionName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mResultsView.setLayoutManager(layoutManager);
        mAdapter = new QuickPollRecyclerAdapter(getContext(), mOpinionPollResultList);
        mResultsView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().onBackPressed();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mSelectedAnswerId = checkedId;
    }
}
