package com.panelManagement.webservices;

import android.content.Context;
import android.text.TextUtils;

import com.panelManagement.database.DBAdapter;
import com.panelManagement.model.AnswerChoiceModel;
import com.panelManagement.model.CommunicationPreferenceItem;
import com.panelManagement.model.CustomNotificationItem;
import com.panelManagement.model.GeneralRedeemModels;
import com.panelManagement.model.NotificationLogItem;
import com.panelManagement.model.OpinionPollResultItem;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.model.ProfilerSurveyModels;
import com.panelManagement.model.QuestionModel;
import com.panelManagement.model.QuickPollQuestionItem;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.model.SurveyModels;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ParseJSonObject extends BaseParser {

    private static final String OSTYPE = "2";
    JSONArray array;

    public ParseJSonObject(Context context) {
        super(context, new JSONObject());
	/*	if (TextUtils.isEmpty(PanelConstants.GCM_REG_ID.trim())) {
            String mRegId = GCMRegistrar.getRegistrationId(mContext);
			PanelConstants.GCM_REG_ID = mRegId;
		} */
        array = new JSONArray();
    }

    /**
     * to check session
     *
     * @return json object for session api
     */
    public JSONObject getSessionObject() {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            //   put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            //  put("AppDeviceTypeID", OSTYPE);
            // put("NotificationID", PanelConstants.getFcmRegId());
            //    put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    public JSONObject getForgotObject(String emailID) {
        try {
            put("emailID", emailID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }


    /**
     * mobile verification json object
     *
     * @param mobileNumber
     * @param resendOTP
     * @return mobile verification json object data
     */
    public JSONObject getMobileVerificationObject(String mobileNumber, boolean resendOTP) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
           /* put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("Ip", ip_address);*/
            put("ResendOTP", resendOTP);
            put("MobileNumber", mobileNumber);
            //    put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /*public JSONObject getMobileVerificationObject(String ip_address, String mobileNumber) {*/

    /**
     * mobile exists json object
     *
     * @param mobileNumber
     * @return mobile exists json object data
     */
    public JSONObject getMobileExistsObject( String mobileNumber) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
//            put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("MobileNumber", mobileNumber);
            // put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }


    /**
     * encrypt mobile number json object
     *
     * @param mobileNumber
     * @return encrypt mobile number json object data
     */
    public JSONObject getEncryptedMobileNumberObject( String mobileNumber) {
        try {
            put("InputString", mobileNumber);
            // put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }



    public JSONObject getPanelListDeviceSave(String OS,String sim,String version, String devicename,double lat,double lng) {
        try {
            put("OSVersion", OS);
            put("TelecomCarrierName", sim);
            put("AppVersion", version);
            put("DeviceName", devicename);
            put("PanellistId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("DeviceId", Utility.getDeviceId(mContext));
            put("DeviceType", "Phone");
            put("LoginTime", Utility.getCurrentTimeStamp());
            put("GeoLocationCoordinates", String.valueOf(lat)+","+String.valueOf(lng));


            //    put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

  /*  AppVersion = "5.2";
    DeviceId = "ED1DD25D-956B-458F-B126-4C3D5D943C8F";
    DeviceName = "iPhone8,1";
    DeviceType = Phone;
    GeoLocationCoordinates = "12.919685,77.601089";
    LoginTime = "2019-01-23 18:24:02";
    OSVersion = "ios12.1.2";
    PanellistId = 2976078;
    TelecomCarrierName = AirTel;
*/
    /**
     * already registered mobile number
     *
     * @param mobileNumber
     * @return json object for registered mobile number
     */
    public JSONObject getSaveMobileObject(String mobileNumber) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("MobileNumber", mobileNumber);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * json object for contact us api
     *
     * @param subject
     * @param body
     * @return json object of contact us
     */
    public JSONObject getContactUSObject(String subject, String body, String image) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("ImageString", image);
            put("Subject", subject);
            put("body", body);
            //     put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * json object parameter for login
     *
     * @param username
     * @param password
     * @return json object of login api
     */
    public JSONObject getLoginObject(String username, String password, String loginDate) {
        try {
            put("Username", username);
            put("Password", password);
            put("LoginDate", loginDate);
            put("AppDeviceTypeID", OSTYPE);
            put("AppDeviceID", Utility.getDeviceId(mContext));
            put("NotificationID", PanelConstants.getFcmRegId());
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check registered uers credentials
     *
     * @param Emailid
     * @param DeviceId
     * @param Country
     * @param os
     * @param SocialPlugInUserID
     * @return json object for registered user validity
     */
    public JSONObject getIsValidUserObject(String Emailid, String DeviceId, String Country, String os, String SocialPlugInUserID) {
        try {
            put("Emailid", Emailid);
            put("DeviceId", DeviceId);
            put("CountryCode", Country);
            //  put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

            // put("os",os);
            // put("SocialPlugInUserID", SocialPlugInUserID);
            // put("SocialPlugInId",Constants.DEVICETYPEID);
            // put("NotificationID", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check link of social sites
     *
     * @param username
     * @param password
     * @param SocialPlugInUserID
     * @param socialType
     * @return json object for social links
     */
    public JSONObject getSocialLoginLinkObject(String username, String password, String SocialPlugInUserID, String socialType) {
        try {
            put("Password", password);
            put("Username", username);
            put("AppDeviceTypeID", OSTYPE);
            put("AppDeviceID", Utility.getDeviceId(mContext));
            put("SocialPlugInUserID", SocialPlugInUserID);
            put("SocialPlugInId", socialType);
            put("NotificationID", PanelConstants.getFcmRegId());
            //  put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check login for social sites
     *
     * @param SocialPlugInUserID
     * @param socialType
     * @return json object for social login
     */
    public JSONObject getSocialLoginObject(String SocialPlugInUserID, String socialType) {
        try {
            put("SocialPlugInUserID", SocialPlugInUserID);
            put("SocialPlugInId", socialType);
            put("AppDeviceTypeID", OSTYPE);
            put("AppDeviceID", Utility.getDeviceId(mContext));
            put("NotificationID", PanelConstants.getFcmRegId());
            //  put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check for survey availability
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public Collection<? extends SurveyModels> getAvailableSurvey(JSONObject jsonObject) throws JSONException {
        return super.availableSurvey(jsonObject);
    }

    /**
     * to get reward points
     *
     * @param jsonObject
     * @return reward points
     * @throws JSONException
     */
    public RewardPointsModels getRewardsPoints(JSONObject jsonObject) throws JSONException {
        return super.rewardsPoints(jsonObject);
    }

    /**
     * to get general redeem data
     *
     * @param jsonObject
     * @return general redeem data
     * @throws JSONException
     */
    public ArrayList<GeneralRedeemModels> getRedeemData(JSONObject jsonObject) throws JSONException {
        return super.getGeneralRedeemData(jsonObject);
    }

    /**
     * to get profile information
     *
     * @param jsonObject
     * @return profile information
     * @throws JSONException
     */
    @Override
    public ArrayList<ProfilerModels> getProfilerInfo(JSONObject jsonObject) throws JSONException {
        return super.getProfilerInfo(jsonObject);
    }

    /**
     * to get communication preference
     * @param jsonObject
     * @return Communication Preference items
     * @throws JSONException
     */
    @Override
    public ArrayList<CommunicationPreferenceItem> getCommunicationPreferenceData(JSONObject jsonObject) throws JSONException {
        return super.getCommunicationPreferenceData(jsonObject);
    }


    /**
     * to get Notification Log details
     * @param jsonObject
     * @return Notification Log Items items
     * @throws JSONException
     */
    @Override
    public ArrayList<NotificationLogItem> getNotificationLogData(JSONObject jsonObject) throws JSONException {
        return super.getNotificationLogData(jsonObject);
    }

    /**
     * to get CustomNotification  details
     * @param jsonObject
     * @return CustomNotification Items items
     * @throws JSONException
     */
    @Override
    public ArrayList<CustomNotificationItem> getCustomNotificationLogData(JSONObject jsonObject) throws JSONException {
        return super.getCustomNotificationLogData(jsonObject);
    }

    /**
     * to get QuickPoll QuestionDetails
     * @param jsonarray
     * @return Quick poll question items
     * @throws JSONException
     */
    @Override
    public ArrayList<QuickPollQuestionItem> getQuickPollQuestionList(JSONArray jsonarray) throws JSONException {
        return super.getQuickPollQuestionList(jsonarray);
    }

    /**
     * to get QuickPoll opinion poll Result
     * @param jsonObject
     * @return OpinionPollResult Items
     * @throws JSONException
     */
    @Override
    public ArrayList<OpinionPollResultItem> getOpinionPollResult(JSONObject jsonObject) throws JSONException {
        return super.getOpinionPollResult(jsonObject);
    }

    /**
     * to check user campaign
     *
     * @param value
     * @return user campaign json object
     * @throws JSONException
     */
    public JSONObject getUserCampaign(String value) throws JSONException {
        object = new JSONObject();
        put("panelistcampaignID", value);
        //put("panelistID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
        //put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
        //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        return super.getObject();
    }

    /**
     * to parse profile survey model data
     *
     * @param data
     * @param campaignId
     * @param pageno
     * @param mCampaignNameSelected
     * @return parsed profile survey by paged
     * @throws JSONException
     */
    public ProfilerSurveyModels parseProfilerSurveyByPage(JSONObject data, String campaignId, int pageno, String mCampaignNameSelected) throws JSONException {
        return super.getProfilerSurveyByPage(data, campaignId, pageno, mCampaignNameSelected);
    }

    /**
     * to check ticket purchase
     *
     * @param nooftickets
     * @param drawmarketgroupid
     * @return json object for ticket buy api
     */
    public JSONObject getBuyTicketObject(int nooftickets, int drawmarketgroupid) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("NumberofTickets", nooftickets);
            put("DrawMarketGroupID", drawmarketgroupid);
            //       put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check redeem object
     *
     * @param NumberofPoints
     * @param RedeeempartnerID
     * @param ItemName
     * @return json object for general redeem object
     */
    public JSONObject getGeneralRedeemObject(int NumberofPoints, int RedeeempartnerID, String ItemName) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("NumberofPoints", NumberofPoints);
            put("RedeeempartnerID", RedeeempartnerID);
            put("ItemName", ItemName);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

            put( "isEdenRed", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    public JSONObject getGeneralRedeemObject1(int NumberofPoints, int RedeeempartnerID, String ItemName, boolean isEdenRed) {
        try {
            put("UserId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            put("sessionID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_SESSIONID));
            put("NumberofPoints", NumberofPoints);
            put("RedeeempartnerID", RedeeempartnerID);
            put("ItemName", ItemName);
            put("isEdenRed", isEdenRed);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    public void putSignupObject(String questionId, String answer) {
        try {
            JSONObject object = new JSONObject();
            object.put("PanelistId", "0");
            object.put("QuestionId", questionId);
            object.put("Answer", answer);
            object.put("CreatedDate", Utility.getCurrentTimeStamp());
            object.put("ModifiedDate", Utility.getCurrentTimeStamp());
            //  object.put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putSignupObject(String questionId, int answer) {
        try {
            JSONObject object = new JSONObject();
            object.put("PanelistId", "0");
            object.put("QuestionId", questionId);
            object.put("Answer", answer);
            object.put("CreatedDate", Utility.getCurrentTimeStamp());
            object.put("ModifiedDate", Utility.getCurrentTimeStamp());
            // object.put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * to check signup
     *
     * @param logintype
     * @param socialPluginId
     * @return json object for sign up api
     */
    public JSONObject getSignupObject(String logintype, String socialPluginId, String referCode, String image, String languageId,String referrealpanelid) {
        try {
            put("PanelistProfilingData", array);
            put("StartTime", Utility.getCurrentTimeStamp());
            put("EndTime", Utility.getCurrentTimeStamp());
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

            int manual = UserInfoModel.Type.MANUAL.ordinal();
            if (manual == Integer.parseInt(logintype))
                put("PanelistStatus", "INTAKE");
            else
                put("PanelistStatus", "DOI");
            put("DeviceId", Utility.getDeviceId(mContext));
            put("IPAddress", InformatePreferences.getStringPrefrence(mContext,
                    Constants.PREF_IPADDRESS));
            put("IsMobileVerified", "0");
            put("SocialplugInId", logintype);
            put("SocialpluginUserID", socialPluginId);
            put("DeviceTypeID", OSTYPE);
            put("NotificationID", PanelConstants.getFcmRegId());
            put("ReferrerPanelistId", referrealpanelid);
            put("ReferrerCode", referCode);
            put("imageString", image);
            put("LanguagePreference", languageId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check profiles
     *
     * @param db
     * @param campaignid
     * @param statusId
     * @return json object for profiles
     * @throws JSONException
     */
    public JSONObject getProfilerInputObject(DBAdapter db, String campaignid, String statusId) throws JSONException {
        object = new JSONObject();
        put("StatusID", statusId);
        put("PanelistCampaignId", campaignid);
        //  put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        ArrayList<QuestionModel> allanswerArray = db.getQuestionsOnPage(campaignid, 0);
        JSONArray array = new JSONArray();
        for (int i = 0; i < allanswerArray.size(); i++) {
            QuestionModel profilerObject = allanswerArray.get(i);
            if (!TextUtils.isEmpty(profilerObject.getuserAnswer())) {
                if (profilerObject.getuserAnswer().equalsIgnoreCase(MUTICHOICE_HASH_CHECKING)) {
                    ArrayList<AnswerChoiceModel> allAnswer = database.getAnswerChoice(profilerObject.getCampaignId(), profilerObject.getQuestionId(), 1);
                    for (int k = 0; k < allAnswer.size(); k++) {
                        JSONObject object = new JSONObject();
                        AnswerChoiceModel modelAnswer = allAnswer.get(k);
                        object.put("PanelistId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
                        object.put("QuestionId", modelAnswer.getQuestionId());
                        object.put("Answer", modelAnswer.getAnswerID());
                        String createdDate = profilerObject.getCreatedDate();
                        String modifiedDate = profilerObject.getModifiedDate();
                        if (TextUtils.isEmpty(createdDate)) {
                            createdDate = getCurrentTimeStamp();
                        }
                        if (TextUtils.isEmpty(modifiedDate)) {
                            modifiedDate = createdDate;
                        }
                        object.put("CreatedDate", createdDate);
                        object.put("ModifiedDate", modifiedDate);
                        array.put(object);
                    }
                } else {
                    JSONObject object = new JSONObject();
                    object.put("PanelistId", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
                    object.put("QuestionId", profilerObject.getQuestionId());
                    object.put("Answer", profilerObject.getuserAnswer());
                    //  object.put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

                    String createdDate = profilerObject.getCreatedDate();
                    String modifiedDate = profilerObject.getModifiedDate();
                    if (TextUtils.isEmpty(createdDate)) {
                        createdDate = getCurrentTimeStamp();
                    }
                    if (TextUtils.isEmpty(modifiedDate)) {
                        modifiedDate = createdDate;
                    }
                    object.put("CreatedDate", createdDate);
                    object.put("ModifiedDate", modifiedDate);
                    array.put(object);
                }
            }
        }
        put("PanelistProfilingData", array);

        return super.getObject();
    }

    /**
     * to check for forgot password
     *
     * @param email
     * @return json object for forgot password api
     */
    public JSONObject getForgotPasswordObject(String email) {
        try {
            put("emailID", email);
            // put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check for otp
     *
     * @param email
     * @return json object for otp api
     */
    public JSONObject getEmailOtp(String email) {
        try {
            put("EmailId", email);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    /**
     * to check for otp verification
     *
     * @param email
     * @param otp
     * @return json object for otp verification api
     */
    public JSONObject verifyEmailOtp(String email, String otp) {
        try {
            put("EmailId", email);
            put("OTP", otp);
            //     put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.getObject();
    }

    public JSONObject getFaqObject(String countryCode) {

        try {
            // put("CountryCode", countryCode);
            put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }

    public JSONObject getUpdateImage(String panelistId, String image) {
        try {
            put("PanelistId", panelistId);
            put("imageString", image);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }

    public JSONObject savePaytmId(String PanelistId, String PaytmId,String paypal) {

        try {
            put("PanelistId", PanelistId);
            put("PaytmId", PaytmId);
            put("PayPalId", paypal);
            // put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }

    public JSONObject getPaytmId(String PanelistId) {

        try {
            put("PanelistId", PanelistId);
            //   put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }

    public JSONObject referCodeValidation(String refercode) {
        try {
            put("ReferrerCode", refercode);
            //  put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }


    public JSONObject errorLog(String PanelListID,String log) {
        try {
            put("PanelistId", PanelListID);
            put("Log", log);
            //    put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext,Constants.PREF_LOCALECODE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.getObject();
    }
}
