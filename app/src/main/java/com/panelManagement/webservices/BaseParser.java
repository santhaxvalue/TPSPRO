package com.panelManagement.webservices;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.panelManagement.controllers.QuestionTypeId;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.model.AnswerChoiceModel;
import com.panelManagement.model.CommunicationPreferenceItem;
import com.panelManagement.model.CustomNotificationItem;
import com.panelManagement.model.EarnedPointHistory;
import com.panelManagement.model.GeneralRedeemModels;
import com.panelManagement.model.LogicsModel;
import com.panelManagement.model.MaskinglogicsModel;
import com.panelManagement.model.NotificationLogItem;
import com.panelManagement.model.OpinionPollResultItem;
import com.panelManagement.model.OpinionPollsAnswerChoice;
import com.panelManagement.model.PagesModel;
import com.panelManagement.model.PipinglogicsModel;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.model.ProfilerSurveyModels;
import com.panelManagement.model.QuestionModel;
import com.panelManagement.model.QuickPollQuestionItem;
import com.panelManagement.model.RedeemPointHistory;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.model.StaticPageContentModel;
import com.panelManagement.model.SurveyModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class BaseParser {

    public static final String MUTICHOICE_HASH_CHECKING = "#######";
    JSONObject object;
    Context mContext;
    DBAdapter database;

    public BaseParser(Context context, JSONObject object) {
        this.mContext = context;
        this.object = object;
        database = DBAdapter.getDBAdapter(mContext);
    }

    protected JSONObject getObject() {
        if (object == null)
            return new JSONObject();
        return object;
    }

    protected void put(String key, String value) throws JSONException {
        object.put(key, value);
    }

    protected void put(String key, JSONArray value) throws JSONException {
        object.put(key, value);
    }

    protected void put(String key, int value) throws JSONException {
        object.put(key, value);
    }

    protected void put(String key, boolean value) throws JSONException {
        object.put(key, value);
    }

    private boolean isNull(JSONObject jsonObject, String key) {
        if (jsonObject.isNull(key))
            return true;
        else
            return false;
    }

    public Collection<? extends SurveyModels> availableSurvey(JSONObject jsonObject) throws JSONException {
        ArrayList<SurveyModels> surveyarray = new ArrayList<SurveyModels>();
        JSONArray arrayObject = jsonObject.getJSONArray("lstWSPanelistSurvey");
        for (int i = 0; i < arrayObject.length(); i++) {
            JSONObject objetfromArray = arrayObject.getJSONObject(i);
            String endTime = objetfromArray.getString("EndDate");
            String startTime = objetfromArray.getString("StartTime");
            String points = objetfromArray.getString("Points");
            String surveyTitle = objetfromArray.getString("SurveyTitle");
            String createdDate = objetfromArray.getString("CreatedDate");
            String surveyLink = objetfromArray.getString("SurveyLink");
            int id = objetfromArray.getInt("Id");
            boolean isMobileDiary = objetfromArray.getBoolean("IsMobileDiary");
            surveyarray.add(new SurveyModels(endTime, startTime, points, surveyTitle, createdDate, surveyLink, id, isMobileDiary));
        }
        return surveyarray;
    }

    public RewardPointsModels rewardsPoints(JSONObject jsonObject) throws JSONException {
        ArrayList<EarnedPointHistory> arrayEarnedPoint = new ArrayList<>();
        String earnedPoints = jsonObject.getString("EarnedPoints");
        if (TextUtils.isEmpty(earnedPoints) || earnedPoints.equalsIgnoreCase("null"))
            earnedPoints = "0";
        String spentPoints = jsonObject.getString("SpentPoints");
        if (TextUtils.isEmpty(spentPoints) || spentPoints.equalsIgnoreCase("null"))
            spentPoints = "0";

//        String PointsReview = jsonObject.getString("PointsReview");
//        if (TextUtils.isEmpty(PointsReview) || PointsReview.equalsIgnoreCase("null"))
//            PointsReview = "0";
        String availablePoints = jsonObject.getString("AvailablePoints");
        if (TextUtils.isEmpty(availablePoints) || availablePoints.equalsIgnoreCase("null"))
            availablePoints = "0";

        String PointsReviews = jsonObject.getString("PointsReview");
        if (TextUtils.isEmpty(PointsReviews) || PointsReviews.equalsIgnoreCase("null"))
            PointsReviews = "0";

        String PointsRejected = jsonObject.getString("PointsReject");
        if (TextUtils.isEmpty(PointsRejected) || PointsRejected.equalsIgnoreCase("null"))
            PointsRejected = "0";

        boolean IsMobileVerified = jsonObject.getBoolean("IsMobileVerified");

        if (mContext == null) {
            Log.d("MESSAGE", "mContext is null in BaseParsor");
        } else if (Constants.PREF_MOBILENUMBERVERIFIED != 20) {
            Log.d("MESSAGE", "pref Mobile verified is " + Constants.PREF_MOBILENUMBERVERIFIED);
        } else {
            InformatePreferences.putBoolean(mContext, Constants.PREF_MOBILENUMBERVERIFIED, IsMobileVerified);
        }
        if (!IsMobileVerified) {
            Log.d("MESSAGE", "Is MobilleVarified is false");
        }

        if (jsonObject.has("ThresholdPoints")) {
            int threshholdPoints = jsonObject.getInt("ThresholdPoints");
            if (threshholdPoints == 0)
                threshholdPoints = 500;
            InformatePreferences.putInt(mContext, Constants.PREF_THRESHHOLD, threshholdPoints);
        }
        JSONArray arrayEarnedPoints = jsonObject.getJSONArray("EarnedPointHistory");
        arrayEarnedPoint = new ArrayList<EarnedPointHistory>();

        for (int i = 0; i < arrayEarnedPoints.length(); i++) {
            JSONObject innerObject = arrayEarnedPoints.getJSONObject(i);
//            arrayEarnedPoint.add(new EarnedPointHistory(innerObject.getString("ProjectSource"), innerObject.getString("Name"),
//                    innerObject.getString("TransactionDate"), innerObject.getString("Points"), innerObject.getString("CampSource")));
            arrayEarnedPoint.add(new EarnedPointHistory(innerObject.getString("ProjectSource"), innerObject.getString("Name"),
                    innerObject.getString("TransactionDate"), innerObject.getString("Points"), innerObject.getString("CampSource"),innerObject.getString("Sourcestatus")));
        }
        JSONArray arrayRedeemPoints = jsonObject.getJSONArray("RedeemPointHistory");
        ArrayList<RedeemPointHistory> arrayRedeemPoint = new ArrayList<RedeemPointHistory>();
        for (int i = 0; i < arrayRedeemPoints.length(); i++) {
            JSONObject innerObject = arrayRedeemPoints.getJSONObject(i);
            String voucherCode = "";
            if (innerObject.has("VoucherCode"))
                voucherCode = innerObject.getString("VoucherCode");
            String RedemptionStatus = null;
            if (innerObject.has("RedemptionStatus"))
                RedemptionStatus = innerObject.getString("RedemptionStatus");
            String RedeemChoices = null;
            if (innerObject.has("RedeemChoices"))
                RedeemChoices = innerObject.getString("RedeemChoices");
            String ExpiryDate = null;
            if (innerObject.has("ExpiryDate"))
                ExpiryDate = innerObject.getString("ExpiryDate");
            String imageurl = "";
            if (innerObject.has("ImageUrl"))
                imageurl = innerObject.getString("ImageUrl");
            String vpassword = "";
            if (innerObject.has("Vpassword"))
                vpassword = innerObject.getString("Vpassword");
            arrayRedeemPoint.add(new RedeemPointHistory(innerObject.getString("Name"), innerObject.getString("TransactionDate"),
                    innerObject.getInt("Points"), voucherCode, RedemptionStatus, RedeemChoices, ExpiryDate, imageurl, vpassword));
        }
        return new RewardPointsModels(earnedPoints, spentPoints, availablePoints,PointsReviews,PointsRejected, arrayEarnedPoint, arrayRedeemPoint);
    }

    public ArrayList<ProfilerModels> getProfilerInfo(JSONObject jsonObject) throws JSONException {
        ArrayList<ProfilerModels> value = new ArrayList<ProfilerModels>();
        JSONArray jsonarray = jsonObject.getJSONArray("lstCampaignFeaturesRatio");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            String campaignid = "";
            String campaignname = "";
            String campaignurl = "";
            String percentageofcomplete = "";
            String panelistcampaignid = "";
            String Incentives = "";
            String NumberOfDaysLeft = "";
            if (object.has("CampaignID"))
                campaignid = object.getString("CampaignID");
            if (object.has("CampaignName"))
                campaignname = object.getString("CampaignName");
            if (object.has("CampaignURL"))
                campaignurl = object.getString("CampaignURL");
            if (object.has("PercentageOfComplete"))
                percentageofcomplete = object.getString("PercentageOfComplete");
            if (object.has("PanelistCampaignId")) {
                panelistcampaignid = object.getString("PanelistCampaignId");
            }
            if (object.has("Incentives")) {
                Incentives = object.getString("Incentives");
            }
            if (object.has("NumberOfDaysLeft")) {
                NumberOfDaysLeft = object.getString("NumberOfDaysLeft");
            }
            value.add(new ProfilerModels(campaignid, campaignname, campaignurl, percentageofcomplete, panelistcampaignid, Incentives, NumberOfDaysLeft));
        }
        return value;
    }

    public ProfilerSurveyModels getProfilerSurveyByPage(JSONObject data, String campaignId, int pageno, String mCampaignNameSelected) throws JSONException {
        ProfilerSurveyModels value = null;
        int pageId = 0;
        // String name = "";
        int Sequence = 0;
        String PageType = "";
        int mode = 0;
        String themeId = "";
        if (data.has("ID"))
            pageId = data.getInt("ID");
        if (data.has("PageType"))
            PageType = data.getString("PageType");
        if (data.has("Sequence"))
            Sequence = data.getInt("Sequence");
        /*
         * if (data.has("PageType")) PageType = data.getString("PageType");
         */
        if (data.has("Mode"))
            mode = data.getInt("Mode");
        if (data.has("ThemeId"))
            themeId = data.getString("ThemeId");
        database.insertPagesModel(new PagesModel(pageId, campaignId, PageType));
        ArrayList<QuestionModel> arrayQuestionModel = new ArrayList<QuestionModel>();
        JSONArray arrayQuestion = data.getJSONArray("questions");
        //if(arrayQuestion.length()>0)
        for (int i = 0; i < arrayQuestion.length(); i++) {
            String maxLength = "";
            boolean mandatory = false;
            int questionTypeId = 0;
            String questionText = "";
            String quesionId = "";
            String validation = "";
            String createddate = "";
            String modifieddate = "";
            boolean isActive = false;
            boolean isNonEditable = false;
            String mUserAnswer = "";
            JSONObject questionObject = arrayQuestion.getJSONObject(i);
            if (questionObject.has("MaxLength"))
                maxLength = questionObject.getString("MaxLength");
            if (questionObject.has("Mandatory"))
                mandatory = questionObject.getBoolean("Mandatory");
            /*
             * if (questionObject.has("IsNonEditable")) isNonEditable =
             * questionObject.getBoolean("IsNonEditable");
             */
            if (questionObject.has("QuestionTypeId"))
                questionTypeId = questionObject.getInt("QuestionTypeId");
            if (questionObject.has("QuestionText"))
                questionText = questionObject.getString("QuestionText");
            if (questionObject.has("ID"))
                quesionId = questionObject.getString("ID");
            if (questionObject.has("Validation"))
                validation = questionObject.getString("Validation");

            if (questionObject.has("IsActive")) {
                isActive = questionObject.getBoolean("IsActive");
            }
            if (questionObject.has("CreatedDate")) {
                createddate = questionObject.getString("CreatedDate");
                if (TextUtils.isEmpty(createddate)) {
                    createddate = getCurrentTimeStamp();
                } else if (createddate.equalsIgnoreCase("null")) {
                    createddate = getCurrentTimeStamp();
                }
            }
            if (questionObject.has("ModifiedDate")) {
                modifieddate = questionObject.getString("ModifiedDate");
                if (TextUtils.isEmpty(modifieddate)) {
                    modifieddate = getCurrentTimeStamp();
                } else if (modifieddate.equalsIgnoreCase("null")) {
                    modifieddate = getCurrentTimeStamp();
                }
            }
            if (questionObject.has("Answer")) {
                mUserAnswer = questionObject.getString("Answer");
                if (TextUtils.isEmpty(mUserAnswer)) {
                    mUserAnswer = "";
                } else if (mUserAnswer.equalsIgnoreCase("null")) {
                    mUserAnswer = "";
                }
            }
            if (questionTypeId == QuestionTypeId.MULTICHOICE.onAccessRevoked()) {
                mUserAnswer = MUTICHOICE_HASH_CHECKING;
            }
            if (quesionId.equalsIgnoreCase(Constants.PANEL_EMAIL_ID) || (quesionId.equalsIgnoreCase(Constants.PANEL_GENDERID)))
                isNonEditable = true;
            QuestionModel questionmodel = new QuestionModel(campaignId, pageno, pageId, quesionId, maxLength, validation,
                    mandatory ? 1 : 0, isNonEditable, questionTypeId, questionText, mUserAnswer, isActive, createddate, modifieddate);
            database.insertQuestioniers(questionmodel);
            getAnswerArray(questionObject.getJSONArray("AnswerChoice"), campaignId, questionmodel);
        }
        ArrayList<LogicsModel> arrayLogics = new ArrayList<LogicsModel>();
        JSONArray arraylogics = data.getJSONArray("logics");
        for (int i = 0; i < arraylogics.length(); i++) {
            JSONObject logicObject = arraylogics.getJSONObject(i);
            int actions = 0;
            String pageName = "";
            String Operator = "";
            String Text = "";
            if (logicObject.has("Action"))
                actions = logicObject.getInt("Action");
            if (logicObject.has("Name"))
                pageName = logicObject.getString("Name");
            int logicId = 0;
            if (logicObject.has("ID"))
                logicId = logicObject.getInt("ID");
            JSONArray arrayCondition = logicObject.getJSONArray("conditions");
            for (int condition = 0; condition < arrayCondition.length(); condition++) {
                JSONObject conditionalObject = arrayCondition.getJSONObject(condition);
                String QuestionId = "";
                if (conditionalObject.has("QuestionId"))
                    QuestionId = conditionalObject.getString("QuestionId");
                String operator = "";
                if (conditionalObject.has("Operator"))
                    operator = conditionalObject.getString("Operator");
                String join = "";
                if (conditionalObject.has("Join"))
                    join = conditionalObject.getString("Join");
                String mappedQuestionId = "";
                if (conditionalObject.has("mappedQuestion")) {
                    try {
                        JSONObject mappedObject = conditionalObject.getJSONObject("mappedQuestion");
                        if (mappedObject.has("QuestionId"))
                            mappedQuestionId = mappedObject.getString("QuestionId");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                JSONArray answerArray = conditionalObject.getJSONArray("answers");
                if (answerArray.length() > 0) {
                    for (int answer = 0; answer < answerArray.length(); answer++) {
                        JSONObject answerObject = answerArray.getJSONObject(answer);
                        String answerId = "";
                        if (answerObject.has("Text"))
                            answerId = answerObject.getString("Text");
                        database.insertSkipLogic(new LogicsModel(campaignId, pageId, actions, logicId, pageName, QuestionId,
                                operator, join, mappedQuestionId, answerId));
                    }
                } else if (!TextUtils.isEmpty(mappedQuestionId)) {
                    database.insertSkipLogic(new LogicsModel(campaignId, pageId, actions, logicId, pageName, QuestionId, operator,
                            join, mappedQuestionId, ""));
                } else {
                    database.insertSkipLogic(new LogicsModel(campaignId, pageId, actions, logicId, pageName, QuestionId, operator,
                            join, mappedQuestionId, ""));
                }
            }
        }
        JSONArray pipinglogics = data.getJSONArray("Pipinglogics");
        for (int i = 0; i < pipinglogics.length(); i++) {
            JSONObject pipingObject = pipinglogics.getJSONObject(i);
            int pipingid = 0;
            String pipingName = null;
            int sourceid = 0;
            int targetid = 0;
            if (pipingObject.has("ID"))
                pipingid = pipingObject.getInt("ID");
            if (pipingObject.has("Name"))
                pipingName = pipingObject.getString("Name");
            JSONArray array = pipingObject.getJSONArray("PipingConditions");
            for (int condition = 0; condition < array.length(); condition++) {
                sourceid = array.getJSONObject(condition).getJSONObject("Source").getInt("QuestionID");
                targetid = array.getJSONObject(condition).getJSONObject("Target").getInt("QuestionID");
                database.insertPipingLogic(new PipinglogicsModel(campaignId, pipingid, pageId, pipingName, sourceid, targetid));
            }
        }

        JSONArray maskinglogics = data.getJSONArray("Maskinglogics");
        for (int i = 0; i < maskinglogics.length(); i++) {
            JSONObject maskingObject = maskinglogics.getJSONObject(i);
            int sourceid = 0;
            String sourceAnswerId = "";
            int targetid = 0;
            int maskingid = 0;
            String maskingName = null;
            String targetAnswerId = "";
            if (maskingObject.has("ID"))
                maskingid = maskingObject.getInt("ID");
            if (maskingObject.has("Name"))
                maskingName = maskingObject.getString("Name");
            JSONArray array = maskingObject.getJSONArray("MaskingConditions");
            for (int condition = 0; condition < array.length(); condition++) {
                JSONObject Objectsourceid = array.getJSONObject(condition);
                if (Objectsourceid.has("Source")) {
                    JSONObject sourceArray = Objectsourceid.getJSONObject("Source");
                    if (sourceArray.has("QuestionID"))
                        sourceid = sourceArray.getInt("QuestionID");
                    JSONArray arraySourceAnswer = sourceArray.getJSONArray("answers");
                    for (int answer = 0; answer < arraySourceAnswer.length(); answer++) {
                        JSONObject objectAnswer = arraySourceAnswer.getJSONObject(answer);
                        if (objectAnswer.has("Text"))
                            sourceAnswerId = objectAnswer.getString("Text");
                    }
                    JSONObject arrayTarget = Objectsourceid.getJSONObject("Target");
                    if (arrayTarget.has("QuestionID"))
                        targetid = arrayTarget.getInt("QuestionID");
                    JSONArray arrayTargetAnswer = arrayTarget.getJSONArray("answers");
                    for (int answer = 0; answer < arrayTargetAnswer.length(); answer++) {
                        JSONObject objectAnswer = arrayTargetAnswer.getJSONObject(answer);
                        if (objectAnswer.has("Text"))
                            targetAnswerId = objectAnswer.getString("Text");
                        database.insertMaskingLogic(new MaskinglogicsModel(campaignId, pageId, maskingid, maskingName,
                                sourceid, sourceAnswerId, targetid, targetAnswerId));
                    }
                }
            }
        }
        value = new ProfilerSurveyModels(pageId, PageType, Sequence, PageType, mode, themeId, null, arrayQuestionModel, arrayLogics, null);
        return value;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private void getAnswerArray(JSONArray jsonArray, String campaignId, QuestionModel questionmodel) {
        for (int i = 0; i < jsonArray.length(); i++) {
            String AnswerChoiceText = "";
            int ModifiedBy = 0;
            String ParentAnswerChoice = "";
            boolean KeepPosition = false;
            boolean IsThisDefaultAnswer = false;
            int AnswerChoiceSequence = 0;
            String BeforeAnswerText = "";
            String ModifiedDate = "";
            boolean IsNoneoftheAbove = false;
            boolean IsActive = false;
            String CreatedBy = "";
            String CreatedDate = "";
            String ID = "";
            int QId = 0;
            JSONObject questionObject;
            try {
                questionObject = jsonArray.getJSONObject(i);
                if (questionObject.has("AnswerChoiceText"))
                    AnswerChoiceText = questionObject.getString("AnswerChoiceText");
                if (questionObject.has("ModifiedBy") && !isNull(questionObject, "ModifiedBy")) {
                    ModifiedBy = questionObject.getInt("ModifiedBy");
                }
                if (questionObject.has("ParentAnswerChoice"))
                    ParentAnswerChoice = questionObject.getString("ParentAnswerChoice");
                if (questionObject.has("KeepPosition"))
                    KeepPosition = questionObject.getBoolean("KeepPosition");
                if (questionObject.has("IsThisDefaultAnswer"))
                    IsThisDefaultAnswer = questionObject.getBoolean("IsThisDefaultAnswer");
                if (questionObject.has("AnswerChoiceSequence"))
                    AnswerChoiceSequence = questionObject.getInt("AnswerChoiceSequence");
                if (questionObject.has("BeforeAnswerText"))
                    BeforeAnswerText = questionObject.getString("BeforeAnswerText");
                if (questionObject.has("ModifiedDate"))
                    ModifiedDate = questionObject.getString("ModifiedDate");
                if (questionObject.has("IsNoneoftheAbove"))
                    IsNoneoftheAbove = questionObject.getBoolean("IsNoneoftheAbove");
                if (questionObject.has("IsActive"))
                    IsActive = questionObject.getBoolean("IsActive");
                if (questionObject.has("CreatedBy"))
                    CreatedBy = questionObject.getString("CreatedBy");
                if (questionObject.has("CreatedBy"))
                    CreatedBy = questionObject.getString("CreatedBy");
                if (questionObject.has("ID") && !isNull(questionObject, "ID"))
                    ID = questionObject.getString("ID");
                if (questionObject.has("QId") && !isNull(questionObject, "QId"))
                    QId = questionObject.getInt("QId");
                AnswerChoiceModel answerModel = new AnswerChoiceModel(campaignId, AnswerChoiceText, ModifiedBy,
                        ParentAnswerChoice, KeepPosition, IsThisDefaultAnswer, AnswerChoiceSequence, BeforeAnswerText, ModifiedDate,
                        IsNoneoftheAbove, IsActive, CreatedBy, CreatedDate, ID, QId, IsActive ? 1 : 0, 0);
                database.insertAnswersChoice(answerModel);
                if ((questionmodel.getQuestionCategoryId() != QuestionTypeId.MULTICHOICE.onAccessRevoked()) && IsActive) {
                    questionmodel.setUserAnswer(ID);
                    database.insertQuestioniers(questionmodel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //old code
//    public ArrayList<GeneralRedeemModels> getGeneralRedeemData(JSONObject jsonObject) throws JSONException {
//        ArrayList<GeneralRedeemModels> value = new ArrayList<GeneralRedeemModels>();
//        JSONArray jsonarray = jsonObject.getJSONArray("lstRewardpartners");
//        for (int i = 0; i < jsonarray.length(); i++) {
//
//            JSONObject object = jsonarray.getJSONObject(i);
//
//            String Id = "";
//            String PartnerName = "";
//            String Description = "";
//            String ImageURL = "";
//            boolean IsInstantRedemptionEnabled = false;
//
//            ArrayList<String> denominationList = new ArrayList<>();
//
//            if (object.has("Id"))
//                Id = object.getString("Id");
//            if (object.has("PartnerName"))
//                PartnerName = object.getString("PartnerName");
//            if (object.has("Description"))
//                Description = object.getString("Description");
//            if (object.has("ImageURL"))
//                ImageURL = object.getString("ImageURL");
//
//            if (object.has("IsInstantRedemptionEnabled"))
//                IsInstantRedemptionEnabled = object.getBoolean("IsInstantRedemptionEnabled");
//
//            if (object.has("denominations")){
//                JSONArray denomination_array = object.getJSONArray("denominations");
//
//                for (int j = 0; j < denomination_array.length(); j++) {
//                    denominationList.add(denomination_array.get(j).toString());
//                }
//            }
//
//            JSONArray jsonArray = null;
//            if (object.has("denominations") && !object.isNull("denominations"))
//                jsonArray = object.getJSONArray("denominations");
//            value.add(new GeneralRedeemModels(Id, PartnerName, Description, ImageURL, IsInstantRedemptionEnabled, false,false, denominationList, jsonArray.toString()));
//        }
//        return value;
//    }

    //new code

    public ArrayList<GeneralRedeemModels> getGeneralRedeemData(JSONObject jsonObject) throws JSONException {
        ArrayList<GeneralRedeemModels> value = new ArrayList<GeneralRedeemModels>();
        JSONArray jsonarray = jsonObject.getJSONArray("lstRewardpartners");
        for (int i = 0; i < jsonarray.length(); i++) {

            JSONObject object = jsonarray.getJSONObject(i);

            String Id = "";
            String PartnerName = "";
            String Description = "";
            String ImageURL = "";
            boolean IsInstantRedemptionEnabled = false;
            boolean IsEdenRed = false;

            ArrayList<String> denominationList = new ArrayList<>();

            if (object.has("Id"))
                Id = object.getString("Id");
            if (object.has("PartnerName"))
                PartnerName = object.getString("PartnerName");
            if (object.has("Description"))
                Description = object.getString("Description");
            if (object.has("ImageURL"))
                ImageURL = object.getString("ImageURL");

            if (object.has("IsInstantRedemptionEnabled"))
                IsInstantRedemptionEnabled = object.getBoolean("IsInstantRedemptionEnabled");

            if (object.has("denominations")){
                JSONArray denomination_array = object.getJSONArray("denominations");

                for (int j = 0; j < denomination_array.length(); j++) {
                    denominationList.add(denomination_array.get(j).toString());
                }
            }

            if(object.has("isEdenRed")){
                IsEdenRed = object.getBoolean("isEdenRed");
            }

            JSONArray jsonArray = null;
            if (object.has("denominations") && !object.isNull("denominations"))
                jsonArray = object.getJSONArray("denominations");
            value.add(new GeneralRedeemModels(Id, PartnerName, Description, ImageURL, IsInstantRedemptionEnabled, IsEdenRed,false, denominationList, jsonArray.toString()));
        }
        return value;
    }

    public ArrayList<CommunicationPreferenceItem> getCommunicationPreferenceData(JSONObject jsonObject) throws JSONException {
        ArrayList<CommunicationPreferenceItem> value = new ArrayList<CommunicationPreferenceItem>();
        JSONArray jsonarray = jsonObject.getJSONArray("lstCommPref");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            int Id = 0;
            String ResourceKey = "";
            String Name = "";
            boolean IsAllowedToChange = false;
            String Selected = "";

            if (object.has("ID"))
                Id = object.getInt("ID");
            if (object.has("ResourceKey"))
                ResourceKey = object.getString("ResourceKey");
            if (object.has("Name"))
                Name = object.getString("Name");
            if (object.has("IsAllowedToChange"))
                IsAllowedToChange = object.getBoolean("IsAllowedToChange");
            if (object.has("Selected"))
                Selected = object.getString("Selected");

            value.add(new CommunicationPreferenceItem(Id, ResourceKey, Name, IsAllowedToChange, Selected));
        }
        return value;
    }


    public ArrayList<NotificationLogItem> getNotificationLogData(JSONObject jsonObject) throws JSONException {
        ArrayList<NotificationLogItem> value = new ArrayList<NotificationLogItem>();
        JSONArray jsonarray = jsonObject.getJSONArray("lstPanelistNotification");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            int PanelistId = 0;
            String SurveyBadgeLevel = "";
            String PointsCredited = "";
            boolean SurveyBadgePoints = false;
            boolean MonthlyRetentionPoints = false;
            String CreatedDate = "";
            String Notification = "";

            if (object.has("PanelistId"))
                PanelistId = object.getInt("PanelistId");
            if (object.has("SurveyBadgeLevel"))
                SurveyBadgeLevel = object.getString("SurveyBadgeLevel");
            if (object.has("PointsCredited"))
                PointsCredited = object.getString("PointsCredited");
            if (object.has("SurveyBadgePoints"))
                SurveyBadgePoints = object.getBoolean("SurveyBadgePoints");
            if (object.has("MonthlyRetentionPoints"))
                MonthlyRetentionPoints = object.getBoolean("MonthlyRetentionPoints");
            if (object.has("CreatedDate"))
                CreatedDate = object.getString("CreatedDate");
            if (object.has("Notification"))
                Notification = object.getString("Notification");

            value.add(new NotificationLogItem(PanelistId, SurveyBadgeLevel, PointsCredited, SurveyBadgePoints, MonthlyRetentionPoints, CreatedDate, Notification));
        }
        return value;
    }


    public ArrayList<CustomNotificationItem> getCustomNotificationLogData(JSONObject jsonObject) throws JSONException {
        ArrayList<CustomNotificationItem> value = new ArrayList<CustomNotificationItem>();
        JSONArray jsonarray = jsonObject.getJSONArray("lstPanelistNotification");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            Integer iD = 0;
            Integer panelistID = 0;
            String notificationMessage = "";
            Boolean isOpened = false;
            String createdDate = "";

            if (object.has("ID"))
                iD = object.getInt("ID");
            if (object.has("PanelistID"))
                panelistID = object.getInt("PanelistID");
            if (object.has("IsOpened"))
                isOpened = object.getBoolean("IsOpened");
            if (object.has("CreatedDate"))
                createdDate = object.getString("CreatedDate");
            if (object.has("NotificationMessage"))
                notificationMessage = object.getString("NotificationMessage");

            value.add(new CustomNotificationItem(iD, panelistID, notificationMessage, isOpened, createdDate));
        }
        return value;
    }


    public ArrayList<QuickPollQuestionItem> getQuickPollQuestionList(JSONArray jsonarray) throws JSONException {
        ArrayList<QuickPollQuestionItem> value = new ArrayList<QuickPollQuestionItem>();

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            Integer questionID = 0;
            Integer questionTypeID = 0;
            String questionName = "";
            ArrayList<OpinionPollsAnswerChoice> opinionPollsAnswerChoices = new ArrayList<>();

            if (object.has("QuestionID"))
                questionID = object.getInt("QuestionID");
            if (object.has("QuestionTypeID"))
                questionTypeID = object.getInt("QuestionTypeID");
            if (object.has("QuestionName"))
                questionName = object.getString("QuestionName");

            //Parse options
            JSONArray obj = new JSONArray();
            if (object.has("OpinionPollsAnswerChoices"))
                obj = object.getJSONArray("OpinionPollsAnswerChoices");
            for (int j = 0; j < obj.length(); j++) {
                OpinionPollsAnswerChoice option = new OpinionPollsAnswerChoice();
                JSONObject optionObject = obj.getJSONObject(j);
                if (optionObject.has("AnswerID"))
                    option.setAnswerID(optionObject.getInt("AnswerID"));
                if (optionObject.has("AnswerChoiceText"))
                    option.setAnswerChoiceText(optionObject.getString("AnswerChoiceText"));
                opinionPollsAnswerChoices.add(option);
            }

            value.add(new QuickPollQuestionItem(questionID, questionName, questionTypeID, opinionPollsAnswerChoices));
        }
        return value;
    }

    public ArrayList<OpinionPollResultItem> getOpinionPollResult(JSONObject jsonObject) throws JSONException {
        ArrayList<OpinionPollResultItem> value = new ArrayList<OpinionPollResultItem>();
        JSONArray jsonarray = jsonObject.getJSONArray("listOpinionPollsResult");
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            Integer questionID = 0;
            Integer questionTypeID = 0;
            Integer answerID = 0;
            Integer pollsCount = 0;
            String questionName = "";
            String questionText = "";
            String answerChoiceText = "";
            double countPercentage = 0.0f;

            if (object.has("QuestionID"))
                questionID = object.getInt("QuestionID");
            if (object.has("QuestionTypeID"))
                questionTypeID = object.getInt("QuestionTypeID");
            if (object.has("QuestionText"))
                questionText = object.getString("QuestionText");
            if (object.has("AnswerChoiceText"))
                answerChoiceText = object.getString("AnswerChoiceText");
            if (object.has("QuestionName"))
                questionName = object.getString("QuestionName");
            if (object.has("countPercentage"))
                countPercentage = object.getDouble("countPercentage");
            if (object.has("AnswerID"))
                answerID = object.getInt("AnswerID");
            if (object.has("Pollscount"))
                pollsCount = object.getInt("Pollscount");

            value.add(new OpinionPollResultItem(questionID, questionName, questionText, questionTypeID, answerID, answerChoiceText, pollsCount, countPercentage));
        }
        return value;
    }

    public StaticPageContentModel getStaticPageContent(JSONObject jsonObject) throws JSONException {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject.toString(), StaticPageContentModel.class);
    }

}
