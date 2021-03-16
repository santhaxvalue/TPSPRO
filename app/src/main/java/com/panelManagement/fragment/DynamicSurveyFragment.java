package com.panelManagement.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.controllers.NoDefaultSpinner;
import com.panelManagement.controllers.ParentLinearLayout;
import com.panelManagement.controllers.QuestionTypeId;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.model.AnswerChoiceModel;
import com.panelManagement.model.LogicsModel;
import com.panelManagement.model.MaskinglogicsModel;
import com.panelManagement.model.PagesModel;
import com.panelManagement.model.PipinglogicsModel;
import com.panelManagement.model.QuestionModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.CustomSpinnerAdaptor;
import com.panelManagement.webservices.BaseParser;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class DynamicSurveyFragment extends BaseFragment implements OnClickListener {

    public final String IS_FILLED = "Is filled";
    public final String IS_NOT_FILLED = "Is not filled";
    public final String SIGN_IS_EQUAL = "=";
    public final String SIGN_NOT_EQUAL = "!=";
    public final String SIGN_GREATER = ">";
    public final String SIGN_GREATER_EQUALS = ">=";
    public final String SIGN_LESSER = "<";
    public final String SIGN_LESSER_EQUALS = "<=";
    public final String IS_EQUAL = "Is equal to";
    public final String IS_NOT_EQUAL = "Is not equal to";
    public final String IS_GREATER = "Greater than";
    public final String IS_GREATER_EQUALS = "Greater than or equal";
    public final String IS_LESSER = "Less than";
    public final String IS_LESSER_EQUALS = "Less than or equal";
    public final String IN_LIST = "In list";
    public final String NOT_IN_LIST = "Not in list";
    public final String IS_CONTAINS_ANY_OF = "Contains any of";
    public final String DOESENT_CONTAINS_ANY_OF = "Doesnot Contains any of";
    public final String IS_CONTAINS_ALL_OF = "Contains all of";
    public final String DOESENT_CONTAINS_ALL_OF = "Doesnot Contains all of";
    public final String IS_AFTER = "After";
    public final String IS_BEFORE = "Before";
    public final String IS_BETWEEN_RANGE = "Between";
    public final String IS_NOT_BETWEEN = "Not Between";
    public final String IS_CONTAINS = "Contains";
    public final String IS_NOT_CONTAINS = "Doesn't contains";
    public final String Q_EQUAL_TO_QUES = "Equal To Question";
    public final String Q_GREATER_TO_QUES = "Greater Than Question";
    public final String Q_GREATER_EQUAL_TO_QUES = "Greater than or equal Question";
    public final String Q_LESSER_TO_QUES = "Less Than Question";
    public final String Q_LESSER_EQUAL_TO_QUES = "Less than or equal Question";
    public Button next = null;
    int qno = 0;
    ArrayList<QuestionModel> surveyData = new ArrayList<QuestionModel>();
    LinearLayout parentlinear;
    private OnprofileSurveyListener listener;
    private DBAdapter database;
    private String campaignId;
    private int pageId;
    private int mPageNo;
    private PagesModel data;
    private String targetQuestionId = "";
    private int mActionPage;
    private String statusID;
    private boolean mIsActvie;
    private boolean isDateInvalid;

    public DynamicSurveyFragment() {
        super();
    }

    // DBAdapter db=null;
    public static DynamicSurveyFragment newInstance(PagesModel pagesModel, String campaignId, int position) {

        DynamicSurveyFragment truitonFrag = new DynamicSurveyFragment();
        Bundle args = new Bundle();
        args.putSerializable("val", pagesModel);
        args.putInt("pos", position);
        args.putString("campaignId", campaignId);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*
         * if (isVisibleToUser) { Utility.showToast(getActivity(),
         * "Visible page is -Page No:"+mPageNo); }
         */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = DBAdapter.getDBAdapter(getActivity());
        data = (PagesModel) (getArguments() != null ? getArguments().getSerializable("val") : null);
        campaignId = getArguments() != null ? getArguments().getString("campaignId") : null;

        qno = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        view = inflater.inflate(R.layout.quiz, container, false);

        parentlinear = view.findViewById(R.id.ic_linear_backfields_data);
        next = view.findViewById(R.id.next);
        next.setOnClickListener(this);

        if (data.getName().equalsIgnoreCase("Terminated") || data.getName().equalsIgnoreCase("Terminate")
                || data.getName().equalsIgnoreCase("ThankYou") || data.getName().equalsIgnoreCase("Thank You")) {

            ArrayList<QuestionModel> value = database.getQuestionsOnPage(campaignId, data.getPagesId());
            showCustomAlertDialog(value.get(0).getQuestionText());

        } else {
            drawSurvey();
        }

        return view;
    }

    public void showCustomAlertDialog(String s) {
        TextView rewards;
        ImageView closeDialog;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.contact_us_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        dialog.show();
        rewards = dialog.findViewById(R.id.tv_thanks_msg);
        rewards.setText(Html.fromHtml(s).toString());
        closeDialog = dialog.findViewById(R.id.close_dialog_contact_us);

        closeDialog.setOnClickListener((View v) -> {
            dialog.dismiss();
            getActivity().finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void drawSurvey() {
        surveyData.clear();
        surveyData.addAll(database.getQuestionsOnPage(data.getCampaignId(), data.getPagesId()));
        ArrayList<PipinglogicsModel> pipingArray = database.getPipingLogicsData(campaignId, data.getPagesId());
        StringBuffer answerPiping = new StringBuffer();
        if (pipingArray.size() > 0) {
            for (int maskingi = 0; maskingi < pipingArray.size(); maskingi++) {
                PipinglogicsModel modelMasking = pipingArray.get(maskingi);
                targetQuestionId = String.valueOf(modelMasking.getTargetid());
                int sourceQuestionId = modelMasking.getSourceid();
                ArrayList<AnswerChoiceModel> answersSelected = database.getAnswerChoice(campaignId,
                        String.valueOf(sourceQuestionId), 1);
                for (int i = 0; i < answersSelected.size(); i++) {
                    answerPiping.append(answersSelected.get(i));
                    if (i < answersSelected.size() - 1)
                        answerPiping.append(",");
                }
            }
        }
        if (surveyData != null) {
            for (int i = 0; i < surveyData.size(); i++) {
                ParentLinearLayout rowLinear = new ParentLinearLayout(getActivity());
                QuestionModel questionValue = surveyData.get(i);
                mPageNo = questionValue.getPageNo();
                pageId = questionValue.getPageId();
                String colored = "*";
                SpannableStringBuilder builder = new SpannableStringBuilder();
                if (questionValue.isMandatory()) {
                    int start = builder.length();
                    builder.append(colored);
                    int end = builder.length();
                    builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (targetQuestionId.equalsIgnoreCase(questionValue.getQuestionId()))
                    builder.append(
                            questionValue.getQuestionText().replace("#PIPING#", " " + answerPiping.toString() + " "));
                else
                    builder.append(questionValue.getQuestionText());

                TextView textLabel = new TextView(getActivity());
                textLabel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                textLabel.setText(builder);
                textLabel.setTextColor(Color.BLACK);
                textLabel.setTypeface(null, Typeface.BOLD);
                rowLinear.addView(textLabel);

                View lineView = new View(getActivity());
                lineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
                lineView.setBackgroundColor(Color.GRAY);
                rowLinear.addView(lineView);

                QuestionTypeId usCoin = QuestionTypeId.getValue(questionValue.getQuestionTypeId());
                switch (usCoin) {

                    case OPENTEXTSINGLELINE:
                        System.out.println("TPSView: " + usCoin);
                        EditText editTextSingleLine = new EditText(getActivity());
                        editTextSingleLine
                                .setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        editTextSingleLine
                                .setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        editTextSingleLine.setEnabled(!questionValue.isIsnoneditable());
                        editTextSingleLine.setClickable(!questionValue.isIsnoneditable());
                        editTextSingleLine.setTextColor(Color.BLACK);
                        editTextSingleLine.setSingleLine();
                        if (questionValue.getValidation().equalsIgnoreCase("Numeric"))
                            editTextSingleLine.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editTextSingleLine.setTag(questionValue);
                        editTextSingleLine.setTypeface(null, Typeface.BOLD);
                        editTextSingleLine.setText(questionValue.getuserAnswer());
                        rowLinear.addView(editTextSingleLine);
                        break;

                    case OPENTEXTMULTILINE:
                        EditText editTextmultiline = new EditText(getActivity());
                        editTextmultiline
                                .setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        // editTextmultiline.setHint(questionValue.getQuestionText());
                        editTextmultiline.setEnabled(!questionValue.isIsnoneditable());
                        editTextmultiline.setTag(questionValue);
                        editTextmultiline.setTextColor(Color.BLACK);

                        if (questionValue.getValidation().equalsIgnoreCase("Numeric"))
                            editTextmultiline.setInputType(InputType.TYPE_CLASS_NUMBER);

                        editTextmultiline.setTypeface(null, Typeface.BOLD);
                        editTextmultiline.setText(questionValue.getuserAnswer());
                        rowLinear.addView(editTextmultiline);
                        break;

                    case NUMERIC:
                        EditText editTextNumber = new EditText(getActivity());
                        editTextNumber
                                .setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editTextNumber.setHint(questionValue.getQuestionText());
                        editTextNumber.setTextColor(Color.BLACK);
                        editTextNumber.setEnabled(!questionValue.isIsnoneditable());
                        editTextNumber.setTag(questionValue);
                        editTextNumber.setTypeface(null, Typeface.BOLD);
                        editTextNumber.setText(questionValue.getuserAnswer());
                        rowLinear.addView(editTextNumber);
                        break;

                    case DATE:
                        DatePicker datePicker = new DatePicker(getActivity());
                        if (TextUtils.isEmpty(questionValue.getuserAnswer()))
                            datePicker.init(1900, 0, 0, null); // Set default value
                            // , it means user
                            // have not set any
                            // value.
                        else {
                            String selectedDate = questionValue.getuserAnswer();
                            try {
                                StringTokenizer st = new StringTokenizer(selectedDate.trim(), "/");
                                int month = Integer.parseInt(st.nextToken());
                                int day = Integer.parseInt(st.nextToken());
                                int year = Integer.parseInt(st.nextToken());
                                datePicker.init(year, month - 1, day, null);
                            } catch (Exception e) {
                                datePicker.init(1900, 0, 0, null);
                            }

                        }
                        datePicker.setEnabled(!questionValue.isIsnoneditable());
                        datePicker.setTag(questionValue.getQuestionId());
                        rowLinear.addView(datePicker);
                        break;

                    case INFO:
                        TextView infText = new TextView(getActivity());
                        infText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        infText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        infText.setTextColor(Color.BLACK);
                        infText.setTag(questionValue.getQuestionId());
                        infText.setTypeface(null, Typeface.BOLD);
                        rowLinear.addView(infText);
                        break;

                    case SINGLECHOICERADIOBUTTON:
                        ArrayList<AnswerChoiceModel> dd = database.getAnswerChoice(questionValue.getCampaignId(),
                                questionValue.getQuestionId(), 0);
                        if (dd.size() > 0) {
                            final RadioButton[] rb = new RadioButton[dd.size()];
                            final RadioGroup rg = new RadioGroup(getActivity());
                            rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    if (rg.getCheckedRadioButtonId() != -1) {

                                        int id = rg.getCheckedRadioButtonId();
                                        View radioButton = rg.findViewById(id);
                                        int radioId = rg.indexOfChild(radioButton);
                                        RadioButton btn = (RadioButton) rg.getChildAt(radioId);
                                        AnswerChoiceModel object = (AnswerChoiceModel) btn.getTag();
                                        if (object != null) {
                                            ArrayList<MaskinglogicsModel> maskingArray = database
                                                    .getMaskingLogicsData(object.getCampaignId(), object.getAnswerID());
                                            if (maskingArray.size() > 0) {
                                                for (int maskingi = 0; maskingi < maskingArray.size(); maskingi++) {
                                                    MaskinglogicsModel modelMasking = maskingArray.get(maskingi);
                                                    AnswerChoiceModel tempObject = new AnswerChoiceModel(
                                                            object.getCampaignId(), modelMasking.getTargetid(),
                                                            modelMasking.getTargetAnswerId(), 1);
                                                    database.updateMaskingOnAnswers(tempObject);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            rg.setTag(questionValue.getQuestionId());
                            rg.setOrientation(LinearLayout.VERTICAL);
                            for (int k = 0; k < dd.size(); k++) {
                                AnswerChoiceModel valueChecked = dd.get(k);
                                rb[k] = new RadioButton(getActivity());
                                rb[k].setTag(valueChecked);
                                rg.addView(rb[k]);
                                rb[k].setText(valueChecked.getAnswerChoiceText());
                                if (valueChecked.getAnswerID().equalsIgnoreCase(questionValue.getuserAnswer())) {
                                    rb[k].setChecked(true);
                                }
                                rb[k].setTextColor(Color.BLACK);
                            }
                            rowLinear.addView(rg);
                        }
                        break;

                    case SIGNLECHOICEDROPDOWN:
                        ArrayList<AnswerChoiceModel> choiceAnswers = database.getAnswerChoice(questionValue.getCampaignId(),
                                questionValue.getQuestionId(), 0);
                        String userAnswer = questionValue.getuserAnswer();
                        NoDefaultSpinner spinner = new NoDefaultSpinner(getActivity());
                        spinner.setEnabled(!questionValue.isIsnoneditable());
                        CustomSpinnerAdaptor spinnerArrayAdapter = new CustomSpinnerAdaptor(getActivity(),
                                android.R.layout.simple_spinner_item, choiceAnswers);
                        spinner.setPrompt("Select your answer");
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerArrayAdapter);
                        if (!TextUtils.isEmpty(userAnswer.trim())) {
                            for (int choice = 0; choice < choiceAnswers.size(); choice++) {
                                if (choiceAnswers.get(choice).getAnswerID().equalsIgnoreCase(userAnswer)) {
                                    spinner.setSelection(choice);
                                    break;
                                }
                            }
                        }
                        rowLinear.addView(spinner);
                        break;

                    case MULTICHOICE:
                        final ArrayList<AnswerChoiceModel> multiCheck = database
                                .getAnswerChoice(questionValue.getCampaignId(), questionValue.getQuestionId(), 0);
                        LinearLayout ll = new LinearLayout(getActivity());
                        ll.setTag(questionValue);
                        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ll.setOrientation(LinearLayout.VERTICAL);
                        for (int cb = 0; cb < multiCheck.size(); cb++) {
                            final CheckBox cbvalue = new CheckBox(getActivity());
                            final AnswerChoiceModel valueChecked = multiCheck.get(cb);
                            cbvalue.setText(valueChecked.getAnswerChoiceText());
                            cbvalue.setTag(valueChecked);
                            cbvalue.setChecked(valueChecked.isChecked());
                            cbvalue.setTextColor(Color.BLACK);
                            cbvalue.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    final boolean isChecked = cbvalue.isChecked();
                                    String selectedText = cbvalue.getText().toString();
                                    if (isChecked) {
                                        valueChecked.setChecked(1);
                                        database.updateAnswersChoice(valueChecked);
                                        checkOtherSelection(valueChecked.isIsNoneoftheAbove(), selectedText);
                                    } else {
                                        valueChecked.setChecked(0);
                                        database.updateAnswersChoice(valueChecked);
                                    }
                                }

                                private void checkOtherSelection(boolean isOther, String selectedText) {

                                    if (isOther) {
                                        noncheckOtherViews(true, selectedText);
                                    } else {
                                        noncheckOtherViews(false, selectedText);
                                        deSelectNonOfAbove();
                                    }
                                }

                                private void noncheckOtherViews(boolean isNonselected, String selText) {
                                    LinearLayout parentView = (LinearLayout) cbvalue.getParent();
                                    int countCb = parentView.getChildCount();
                                    for (int countCbc = 0; countCbc < countCb; countCbc++) {
                                        View checkBoxv = parentView.getChildAt(countCbc);
                                        if (checkBoxv instanceof CheckBox) {
                                            CheckBox checkbox = ((CheckBox) checkBoxv);
                                            if (checkbox.isChecked()) {
                                                AnswerChoiceModel object = (AnswerChoiceModel) checkbox.getTag();
                                                if (selText.equalsIgnoreCase("All of the above")) {
                                                    checkbox.setChecked(true);
                                                    object.setChecked(1);
                                                    database.updateAnswersChoice(object);
                                                } else {
                                                    if (isNonselected) {
                                                        if (!selText.equalsIgnoreCase(object.getAnswerChoiceText())) {
                                                            checkbox.setChecked(false);
                                                            object.setChecked(0);
                                                            database.updateAnswersChoice(object);
                                                        }
                                                    }

                                                }

                                            }
                                        }
                                    }
                                }

                                private void deSelectNonOfAbove() {
                                    LinearLayout parentView = (LinearLayout) cbvalue.getParent();
                                    int countCb = parentView.getChildCount();
                                    for (int countCbc = 0; countCbc < countCb; countCbc++) {
                                        View checkBoxv = parentView.getChildAt(countCbc);
                                        if (checkBoxv instanceof CheckBox) {
                                            CheckBox checkbox = ((CheckBox) checkBoxv);
                                            if (checkbox.isChecked()) {
                                                AnswerChoiceModel object = (AnswerChoiceModel) checkbox.getTag();
                                                if (object.isIsNoneoftheAbove()) {
                                                    checkbox.setChecked(false);
                                                    object.setChecked(0);
                                                    database.updateAnswersChoice(object);
                                                }

                                            }
                                        }
                                    }


                                }
                            });

                            ll.addView(cbvalue);
                        }
                        rowLinear.addView(ll);
                        break;

                    case GRIDTYPE:
                        // Utility.showToast(getActivity(),
                        // "Hey question of type Grid unable to display this.");
                        TextView gridType = new TextView(getActivity());
                        gridType.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        gridType.setInputType(InputType.TYPE_CLASS_NUMBER);
                        gridType.setTextColor(Color.RED);
                        gridType.setText("Grid Type is not supported for now.");
                        gridType.setTypeface(null, Typeface.BOLD);
                        rowLinear.addView(gridType);

                        break;

                }
                parentlinear.addView(rowLinear);
            }
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof OnprofileSurveyListener) {
            listener = (OnprofileSurveyListener) activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
                // readUserData();
                break;
        }
    }

    private void readUserData() {
        String fn = "";
        String ln = "";
        String mb = "";
        int childcount = parentlinear.getChildCount();
        boolean isValidated = true;
        for (int i = 0; i < childcount; i++) {
            View rows = parentlinear.getChildAt(i);
            if (rows instanceof LinearLayout) {
                LinearLayout linearLayoutData = (LinearLayout) rows;
                int rowCount = linearLayoutData.getChildCount();
                for (int rowdata = 0; rowdata < rowCount; rowdata++) {
                    View innerRowData = linearLayoutData.getChildAt(rowdata);
                    if (innerRowData instanceof EditText) {
                        EditText editTextData = (EditText) innerRowData;
                        QuestionModel object = (QuestionModel) editTextData.getTag();
                        if (object.getQuestionId().equals("1058")) {
                            fn = editTextData.getText().toString();
                        }
                        if (object.getQuestionId().equals("1059")) {
                            ln = editTextData.getText().toString();
                        }
                        if (object.getQuestionId().equals("1060")) {
                            mb = editTextData.getText().toString();
                        }
                        if (TextUtils.isEmpty(editTextData.getText().toString().trim())) {
                            isValidated = false;
                            editTextData.setBackgroundColor(Color.YELLOW);
                            break;
                        } else if (object.getValidation().equalsIgnoreCase("Email")
                                && !Utility.isEmailValid(editTextData.getText().toString().trim())) {
                            isValidated = false;
                            editTextData.setBackgroundColor(Color.RED);
                            Utility.showToast(getActivity(), "Please enter valid email-id");
                            break;
                        } else {
                            database.updateUserAnswer(campaignId, object.getQuestionId(),
                                    editTextData.getText().toString());

                        }
                    } else if (innerRowData instanceof Spinner) {
                        NoDefaultSpinner spinnerData = (NoDefaultSpinner) innerRowData;
                        AnswerChoiceModel object = (AnswerChoiceModel) spinnerData.getSelectedItem();
                        if (object == null) {
                            isValidated = false;
                            spinnerData.setBackgroundResource(R.drawable.dropdown_error);
                            break;
                        } else {
                            database.updateUserAnswer(campaignId, String.valueOf(object.getQuestionId()), String.valueOf(object.getAnswerID()));
                            if (!TextUtils.isEmpty(object.getAnswerID())) {
                                if (object.getAnswerID().equalsIgnoreCase("8371")) {
                                    listener.fragmentUpdate(5);
                                } else if (object.getAnswerID().equalsIgnoreCase("8372")) {
                                    listener.fragmentUpdate(6);
                                } else if (object.getAnswerID().equalsIgnoreCase("8373")) {
                                    listener.fragmentUpdate(7);
                                } else if (object.getAnswerID().equalsIgnoreCase("8374")) {
                                    listener.fragmentUpdate(8);
                                }
                            }
                            Log.d("Profiler", "" + object);
                        }
                    } else if (innerRowData instanceof DatePicker) {
                        DatePicker datepicker = (DatePicker) innerRowData;
                        int year__ = datepicker.getYear();
                        int month__ = datepicker.getMonth();
                        int day__ = datepicker.getDayOfMonth();
                        if (year__ == 1900) {
                            isValidated = false;
                            datepicker.setBackgroundColor(Color.RED);
                        } else {

                            String tag = (String) datepicker.getTag();
                            String yr = String.valueOf(year__);
                            String mMonth = String.valueOf(month__ + 1);
                            String mDay = String.valueOf(day__);
                            database.updateUserAnswer(campaignId, tag, mMonth + "/" + mDay + "/" + yr);
                        }
                    } else if (innerRowData instanceof LinearLayout) {
                        // For checkbox
                        boolean isCheckforOne = false;
                        LinearLayout linearCheckBox = (LinearLayout) innerRowData;
                        int countCb = linearCheckBox.getChildCount();
                        if (innerRowData instanceof RadioGroup) {
                            RadioGroup rg1 = (RadioGroup) innerRowData;
                            if (rg1.getCheckedRadioButtonId() != -1) {
                                int id = rg1.getCheckedRadioButtonId();
                                View radioButton = rg1.findViewById(id);
                                int radioId = rg1.indexOfChild(radioButton);
                                RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
                                AnswerChoiceModel object = (AnswerChoiceModel) btn.getTag();
                                // Follwing commented line is the original code updated on finding a issue in profilers
                                // database.updateUserAnswer(campaignId, String.valueOf(object.getQuestionId()), String.valueOf(object.getAnswerID()));
                                database.updateUserAnswer(campaignId, String.valueOf(object.getQuestionId()), String.valueOf(object.getAnswerID()));
                                Log.d("Profiler", "" + object);
                                isCheckforOne = true;
                            }
                        } else {
                            for (int count = 0; count < countCb; count++) {
                                View checkBox = linearCheckBox.getChildAt(count);
                                if (checkBox instanceof CheckBox) {
                                    if (((CheckBox) checkBox).isChecked()) {
                                        AnswerChoiceModel object = (AnswerChoiceModel) checkBox.getTag();
                                        // Follwing commented line is the original code updated on finding a issue in profilers
                                        // database.updateUserAnswer(campaignId, String.valueOf(object.getQuestionId()), String.valueOf(object.getAnswerID()));
                                        database.updateUserAnswer(campaignId, String.valueOf(object.getQuestionId()), BaseParser.MUTICHOICE_HASH_CHECKING);
                                        Log.d("Profiler", "" + object);
                                        isCheckforOne = true;
                                    }
                                }
                            }
                        }
                        if (!isCheckforOne) {
                            isValidated = false;
                            linearCheckBox.setBackgroundColor(Color.GREEN);
                        }
                    }
                }
            }
        }
        if (isValidated) {
            skipLogicValidation(isValidated);
            if (!TextUtils.isEmpty(fn) && !TextUtils.isEmpty(ln)) {
                InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_FIRSTNAME, fn);
                InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_LASTNAME, ln);
            }
            if (!TextUtils.isEmpty(mb)) {
                InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_MOBILENUMBER, mb);
                HomeActivity.phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(context, Constants.PREF_ISDCODE), InformatePreferences.getStringPrefrence(context, Constants.PREF_MOBILENUMBER)));


            }
        }
    }

    private void skipLogicValidation(boolean isValidated) {
        ArrayList<LogicsModel> questionArray = database.getskipLogics(campaignId, pageId);

        int childcount = parentlinear.getChildCount();
        boolean dateTime = false;
        for (int i = 0; i < childcount; i++) {
            View rows = parentlinear.getChildAt(i);
            if (rows instanceof LinearLayout) {
                LinearLayout linearLayoutData = (LinearLayout) rows;
                int rowCount = linearLayoutData.getChildCount();
                for (int rowdata = 0; rowdata < rowCount; rowdata++) {
                    View innerRowData = linearLayoutData.getChildAt(rowdata);
                    if (innerRowData instanceof DatePicker) {
                        dateTime = true;
                        break;
                    }
                }
            }
        }

        if (isValidated && questionArray.size() > 0) {
            boolean isTerminatePageActivated = false;
            for (int i = 0; i < questionArray.size(); i++) {
                LogicsModel logicModel = questionArray.get(i);
                QuestionModel questionModel = database.getQuestionsByTargetId(campaignId, -1,
                        Integer.parseInt(logicModel.getLookqustionid()));
                if (questionModel != null) {
                    if (isPageTerminated(questionModel) && (!mIsActvie && !dateTime)) {
                        isTerminatePageActivated = true;
                    }
                    if (isTerminatePageActivated) {
                        /*
                         * LogicsModel mLogicModel =
                         * database.getskipLogicForCurrentPage(campaignId,
                         * pageId, questionModel.getQuestionId());
                         */
                        if (!dateTime && !mIsActvie) {
                            listener.onPageChange(database.getPageNoByAction(campaignId, mActionPage));
                        } else {
                            isTerminatePageActivated = false;
                        }

                        break;
                    }
                }
            }

            if (!isTerminatePageActivated) {
                LogicsModel mLogicModel = null;
                boolean flag = false;
                for (int i = 0; i < questionArray.size(); i++) {
                    LogicsModel logicModel = questionArray.get(i);
                    QuestionModel questionModel = database.getQuestionsByTargetId(campaignId, pageId,
                            Integer.parseInt(logicModel.getLookqustionid()));
                    if (questionModel != null) {
                        mLogicModel = database.getskipLogicForCurrentPage(campaignId, pageId,
                                questionModel.getQuestionId());
                        if (mLogicModel != null) {
                            flag = getConditionalQueryResult(questionModel, mLogicModel);
                            if (flag) {
                                if (mLogicModel.getJoin().equalsIgnoreCase("OR")) {
                                    break;
                                }
                            } else if (mLogicModel.getJoin().equalsIgnoreCase("And")) {
                                break;
                            }
                        }
                    }
                }


                // Jump Page
                if (flag && mLogicModel != null) {
                    listener.onPageChange(database.getPageNoByAction(campaignId, mLogicModel.getAction()));
                } else if (dateTime && mIsActvie) {
                    LogicsModel logicModel = questionArray.get(0);
                    if (logicModel != null)
                        if (isDateInvalid) {
                            listener.onPageChange(database.getPageNoByAction(campaignId, mActionPage));
                            isDateInvalid = false;
                        } else if (!logicModel.getOperator().equalsIgnoreCase("Greater Than Question"))
                            listener.onPageChange(database.getPageNoByAction(campaignId, logicModel.getAction()));
                        else listener.onPageChange(mPageNo + 1);

                } else {
                    if (isDateInvalid) {
                        listener.onPageChange(database.getPageNoByAction(campaignId, mActionPage));
                        isDateInvalid = false;
                    } else
                        listener.onPageChange(mPageNo + 1);
                }
            }
        } else {
            listener.onPageChange(mPageNo + 1);
        }
    }

    public boolean getConditionalQueryResult(QuestionModel questionModel, LogicsModel mLogicModel) {

        boolean isLogicApply = false;
        int resultCount = -1;
        QuestionTypeId questionOperation = QuestionTypeId.getValue(questionModel.getQuestionTypeId());
        if (mLogicModel != null) {
            if (questionOperation == QuestionTypeId.SIGNLECHOICEDROPDOWN
                    || questionOperation == QuestionTypeId.NUMERIC) {
                resultCount = database.getConditionQueryCount(questionModel, mLogicModel.getJoin());
                switch (mLogicModel.getOperator()) {
                    case "Not in list":
                    case "Is not equal to":
                        if (resultCount == 0)
                            isLogicApply = true;
                        break;
                    case "in list":
                    case "Is equal to":
                        if (resultCount > 0)
                            return true;
                        break;

                    default:
                        break;
                }
            } else if (questionOperation == QuestionTypeId.MULTICHOICE) {
                ArrayList<AnswerChoiceModel> answerList = database.getAnswerChoice(questionModel.getCampaignId(),
                        questionModel.getQuestionId(), 0);

                switch (mLogicModel.getOperator()) {
                    case "Contains all of":
                        resultCount = database.generateMultiConditionQuery(questionModel, answerList,
                                mLogicModel.getOperator(), mLogicModel.getAction(), " NOT IN ");
                        if (resultCount == 0)
                            isLogicApply = true;
                        break;
                    case "Contains any of":
                    case "Is equal to":
                        resultCount = database.generateMultiConditionQuery(questionModel, answerList,
                                mLogicModel.getOperator(), mLogicModel.getAction(), " IN ");
                        if (resultCount > 0)
                            isLogicApply = true;
                        break;
                    case "Doesn't contain any of":
                        resultCount = database.generateMultiConditionQuery(questionModel, answerList,
                                mLogicModel.getOperator(), mLogicModel.getAction(), " NOT IN ");
                        if (resultCount > 0)
                            isLogicApply = true;
                        break;
                    case "Doesn't contain all of":
                        resultCount = database.generateMultiConditionQuery(questionModel, answerList,
                                mLogicModel.getOperator(), mLogicModel.getAction(), " IN ");
                        if (resultCount == 0)
                            isLogicApply = true;
                        break;
                    default:
                        break;
                }
            } else if (questionOperation == QuestionTypeId.DATE) {
                switch (mLogicModel.getOperator()) {
                    case "Is filled":

                        break;
                    case "Is not filled":

                        break;
                    case "Is equal to":

                        break;
                    case "Is not equal to":

                        break;
                    case "After":

                        break;
                    case "Before":

                        break;
                    default:
                        break;
                }
            }
        }
        return isLogicApply;
    }

    public int getCheckedAnswerCount(ArrayList<AnswerChoiceModel> answerList) {
        int counter = 0;
        for (Iterator iterator = answerList.iterator(); iterator.hasNext(); ) {
            AnswerChoiceModel answerChoiceModel = (AnswerChoiceModel) iterator.next();
            if (answerChoiceModel.isChecked()) {
                counter++;
            }
        }
        return counter;
    }

    public boolean getIsEqualAnswer(String questionid, int pageid, String campaignId, String userAnswerID, String JoinType) {
        int count = database.getConditionQueryCount(campaignId, pageid, questionid, userAnswerID, JoinType);
        return count != 0;
    }

    public boolean getNotInListAnswer(String questionid, int pageid, String campaignId, String userAnswerID, String JoinType) {
        int count = database.getConditionQueryCount(campaignId, pageid, questionid, userAnswerID, JoinType);
        return count == 0;
    }

    public boolean getInListAnswer(String questionid, int pageid, String campaignId, String userAnswerID, String JoinType) {
        int count = database.getConditionQueryCount(campaignId, pageid, questionid, userAnswerID, JoinType);
        return count != 0;
    }

    private boolean isPageTerminated(QuestionModel questionModel) {
        return isTerminationCondition(questionModel);
    }

    public Date getParsingDate(String datevalue) {
        Date parsedDate = null;
        SimpleDateFormat format = null;
        // 04-08-2015 00:00:00
        try {
            parsedDate = isValidDateFormat("mm/dd/yyyy", datevalue);
            if (parsedDate == null)
                parsedDate = isValidDateFormat("dd-MM-yyyy hh:mm:ss", datevalue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedDate;
    }

    public Date isValidDateFormat(String format, String datevalue) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            return sdf.parse(datevalue);
        } catch (Exception e) {
            return null;
        }

    }

    private void postData(String statusID) {
        try {
            if (Utility.isConnectedToInternet(getActivity())) {
                showDialog(true, getString(R.string.dialog_login));

                JSONObject object = new ParseJSonObject(getActivity()).getProfilerInputObject(database, campaignId, statusID);
                requestTypePost(Constants.API_SAVEPANELISTPROFILE, object, Constants.REQUESTCODE_SAVEPROFILER);
            } else {
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();
        switch (requestcode) {
            case Constants.REQUESTCODE_SAVEPROFILER:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.has("Status")) {
                        listener.checkSurveyCompleted(true);
                        // if (object.getBoolean("Status")) {
                        listener.onPageChange(database.getPageNoByAction(campaignId, mActionPage));
                        //} else
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public boolean isTerminationCondition(QuestionModel questionModel) {
        boolean isTerminated = false;
        QuestionTypeId questionOperation = QuestionTypeId.getValue(questionModel.getQuestionTypeId());
        if (questionOperation == null)
            return isTerminated;
        LogicsModel mLogicModel = database.getskipLogicForCurrentPage(campaignId, pageId,
                questionModel.getQuestionId());
        int actionId = 0;
        if (mLogicModel != null)
            switch (questionOperation) {
                case SINGLECHOICERADIOBUTTON:
                    /* Simgle choice radio button */
                    isTerminated = isSingleRadioCondition(questionModel, mLogicModel);
                    break;
                case SIGNLECHOICEDROPDOWN:
                    isTerminated = isSingleDropDownCondition(questionModel, mLogicModel);
                    break;
                case MULTICHOICE:
                    isTerminated = isMultiChoiceCondition(questionModel, mLogicModel);
                    break;
                case NUMERIC:
                    isTerminated = isNumericCondition(questionModel, mLogicModel);
                    break;
                case DATE:
                    List<LogicsModel> mLogicModelList = database.getskipLogicForCurrentPageList(campaignId, pageId,
                            questionModel.getQuestionId());
                    for (Iterator iterator = mLogicModelList.iterator(); iterator.hasNext(); ) {
                        LogicsModel mLogicModel1 = (LogicsModel) iterator.next();
                        actionId = mLogicModel1.getAction();
                        isTerminated = isDateCondition(questionModel, mLogicModel1);
                        if (isTerminated && mLogicModel1.getJoin().equalsIgnoreCase("OR")) {
                            break;
                        } else if (!isTerminated && mLogicModel1.getJoin().equalsIgnoreCase("AND")) {
                            break;
                        }
                    }
                    break;
                case OPENTEXTSINGLELINE:

                    break;
                case OPENTEXTMULTILINE:

                    break;
                default:
                    break;
            }

        if (isTerminated) {
            String pageName = database.getPagesIdByName(campaignId, actionId);
            if (actionId == 0)
                mActionPage = mLogicModel.getAction();
            else
                mActionPage = actionId;
            if (pageName.equalsIgnoreCase("Terminated") || pageName.equalsIgnoreCase("Terminate")) {
                postData(Constants.EXIT_TERMINATEPROFILER);
            } else if (pageName.equalsIgnoreCase("ThankYou") || pageName.equalsIgnoreCase("Thank You")) {
                postData(Constants.EXIT_THANKYOU);
            }
        }
        // if(isTerminated)
        // break;
        // }
        return isTerminated;
    }

    public boolean isSingleRadioCondition(QuestionModel questionModel, LogicsModel mLogicModel) {
        boolean isTerminated = false;
        int resultCount = database.getConditionQueryCount(questionModel, mLogicModel.getJoin());
        switch (mLogicModel.getOperator()) {
            case IS_FILLED:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case IS_NOT_FILLED:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_IS_EQUAL:
            case IS_EQUAL:
            case IN_LIST:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_NOT_EQUAL:
            case IS_NOT_EQUAL:
            case NOT_IN_LIST:
                if (resultCount == 0)
                    isTerminated = true;
                break;

            default:
                break;
        }
        return isTerminated;
    }

    public boolean isSingleDropDownCondition(QuestionModel questionModel, LogicsModel mLogicModel) {
        boolean isTerminated = false;
        int resultCount = database.getConditionQueryCount(questionModel, mLogicModel.getJoin());
        switch (mLogicModel.getOperator()) {
            case IS_FILLED:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case IS_NOT_FILLED:
                if (resultCount == 0)
                    isTerminated = true;
                break;
            case SIGN_IS_EQUAL:
            case IS_EQUAL:
            case IN_LIST:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_NOT_EQUAL:
            case IS_NOT_EQUAL:
            case NOT_IN_LIST:
                if (resultCount == 0)
                    isTerminated = true;
                break;

            default:
                break;
        }
        return isTerminated;
    }

    public boolean isNumericCondition(QuestionModel questionModel, LogicsModel mLogicModel) {
        boolean isTerminated = false;
        int resultCount = database.getConditionQueryCount(questionModel, mLogicModel.getJoin(),
                getNumricOperator(mLogicModel.getOperator()));
        switch (mLogicModel.getOperator()) {
            case IS_FILLED:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case IS_NOT_FILLED:
                if (resultCount == 0)
                    isTerminated = true;
                break;
            case SIGN_IS_EQUAL:
            case IS_EQUAL:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_NOT_EQUAL:
            case IS_NOT_EQUAL:
                if (resultCount == 0)
                    isTerminated = true;
                break;
            case SIGN_GREATER:
            case IS_GREATER:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_GREATER_EQUALS:
            case IS_GREATER_EQUALS:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_LESSER:
            case IS_LESSER:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case SIGN_LESSER_EQUALS:
            case IS_LESSER_EQUALS:
                if (resultCount > 0)
                    isTerminated = true;
                break;
            default:
                break;
        }
        return isTerminated;
    }

    public boolean isMultiChoiceCondition(QuestionModel questionModel, LogicsModel mLogicModel) {
        boolean isTerminated = false;
        ArrayList<AnswerChoiceModel> answerList = database.getAnswerChoice(questionModel.getCampaignId(),
                questionModel.getQuestionId(), 0);
        int resultCount = -1;
        switch (mLogicModel.getOperator()) {
            case IS_FILLED:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " NOT IN ");
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case IS_NOT_FILLED:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " NOT IN ");
                if (resultCount == 0)
                    isTerminated = true;
                break;
            case IS_CONTAINS_ALL_OF:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " NOT IN ");
                if (resultCount == 0)
                    isTerminated = true;
                break;
            case IS_CONTAINS_ANY_OF:
            case IS_EQUAL:
            case SIGN_IS_EQUAL:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " IN ");
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case DOESENT_CONTAINS_ANY_OF:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " IN ");
                if (resultCount > 0)
                    isTerminated = true;
                break;
            case DOESENT_CONTAINS_ALL_OF:
                resultCount = database.generateMultiConditionQuery(questionModel, answerList, mLogicModel.getOperator(),
                        mLogicModel.getAction(), " NOT IN ");
                if (resultCount == 0)
                    isTerminated = true;
                break;
            default:
                break;
        }
        return isTerminated;
    }

    public String getNumricOperator(String operation) {
        String operatorSign = "=";
        switch (operation) {
            case SIGN_IS_EQUAL:
            case IS_EQUAL:
                operatorSign = "=";
                break;
            case SIGN_NOT_EQUAL:
            case IS_NOT_EQUAL:
                operatorSign = "!=";
                break;
            case SIGN_GREATER:
            case IS_GREATER:
                operatorSign = ">";
                break;
            case SIGN_GREATER_EQUALS:
            case IS_GREATER_EQUALS:
                operatorSign = ">=";
            case SIGN_LESSER:
            case IS_LESSER:
                operatorSign = "<";
                break;
            case SIGN_LESSER_EQUALS:
            case IS_LESSER_EQUALS:
                operatorSign = "<=";
                break;
            default:
                break;
        }
        return operatorSign;
    }

    public boolean isDateCondition(QuestionModel questionModel, LogicsModel mLogicsModel) {
        boolean isTerminated = false;
        String userAnswer = questionModel.getuserAnswer();
        QuestionModel model = null;
        String dbAnswer = "";
        if (TextUtils.isEmpty(mLogicsModel.getMaskedQuestionId())) {
            dbAnswer = mLogicsModel.getAnswerid();
        } else {
            model = database.getQuestionsByTargetId(questionModel.getCampaignId(), -1,
                    Integer.parseInt(mLogicsModel.getMaskedQuestionId()));
            if (model != null)
                dbAnswer = model.getuserAnswer();
        }

        if (!TextUtils.isEmpty(dbAnswer)) {
            Date userDate = getParsingDate(userAnswer);
            Date prevDate = getParsingDate(dbAnswer);
            if (userDate != null && prevDate != null) {
                switch (mLogicsModel.getOperator()) {
                    case Q_GREATER_TO_QUES:
                        isTerminated = prevDate.after(userDate);
                        isDateInvalid = prevDate.after(userDate);
                        break;
                    case IS_AFTER:
                        // isTerminated = prevDate.after(userDate);
                        isTerminated = userDate.after(prevDate);
                        break;
                    case Q_LESSER_TO_QUES:
                        isTerminated = prevDate.before(userDate);
                        break;
                    case IS_BEFORE:
                        // isTerminated = prevDate.before(userDate);
                        isTerminated = userDate.before(prevDate);
                        break;
                    case IS_NOT_EQUAL:
                    case SIGN_NOT_EQUAL:
                        isTerminated = !prevDate.equals(userDate);
                        break;
                    case SIGN_IS_EQUAL:
                    case IS_EQUAL:
                    case Q_EQUAL_TO_QUES:
                        isTerminated = prevDate.equals(userDate);
                        break;
                    case IS_BETWEEN_RANGE:
                        /* Not implemented */
                        // isTerminated = (userDate.after(startDate) &&
                        // userDate.before(endDate));
                        break;
                    case IS_NOT_BETWEEN:
                        /* Not implemented */
                        // isTerminated = !(userDate.after(startDate) &&
                        // userDate.before(endDate));
                        break;
                    default:
                        break;
                }
            }
        }
        return isTerminated;
    }

    @Override
    public void rError(int requestcode) {
        // Toast.makeText(getActivity(), "Error in connection call ",
        // Toast.LENGTH_LONG).show();
    }

    public void setIsActive(boolean isActive) {
        mIsActvie = isActive;
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(getString(R.string.dialog_login));
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    readUserData();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mDialog.dismiss();
        }
    }

}