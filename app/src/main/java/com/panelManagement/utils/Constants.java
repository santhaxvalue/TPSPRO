package com.panelManagement.utils;

import com.config.Config;
import com.panelManagement.location.LocationModel;

public class Constants {
    public static final int ENGLISH = 101;
    public static final int TRADITIONAL_CHINESE = 102;
    public static final int MEXICAN_SPANISH = 103;
    public static final int ARGENTINIAN_SPANISH = 104;

    public static final int CHILE_SPANISH = 105;
    public static final int PORTUGEESE = 106;
    public static final int RUSSIAN = 107;
    public static final int TURKISH = 108;
    public static final int BAHASA_INDONESIA = 109;

    public static final int KOREAN = 110;
    public static final int POLISH = 111;
    public static final int PHILIPPINES = 112;
    public static final int SIMPLIFIED_CHINESE = 113;
    public static final int THI = 114;

    public static final int TAGALOG = 117;
    public static final int COLOMBIAN_SPANISH = 115;
    public static final int VIETNAMESE = 118;
    public static final int MALAYSIA = 119;
    public static final int HONKONG = 120;
    public static final int GERMANY = 121;
    public static final int FRANCE = 123;
    public static final int ITALIAN = 124;


    public static final String REGISTRATION_URL = "http://tpsapi.informatemi.com/tps/api_register.php";


    //////////////////////////////////////////////////////////////////////////////////
    /// Stage/Dev:http://communisense.borderlessaccess.com:8085/api/psapi/
    /// QA/UAT:http://182.74.71.250:302/api/psapi/
    /// Live/Prod:http://communisense.borderlessaccess.com:9094/api/psapi/
    //////////////////////////////////////////////////////////////////////////////////


    public static final String BASEURL = Config.BASE_URL;
    public static final String GET_COUNTRY_URL = "http://tpsapi.informatemi.com/tps/api_isEligibleJson.php";
    public static final String GET_WEB_URL_FOR_SIGNUP = "https://thepanelstation.com";

    public static final String PANLISTLOGIN = BASEURL + "PanelistValidationMobileandDesktop";
    public static final String API_ERROR_LOG = BASEURL + "ErrorLogSave";
    public static final String API_GETPANELISTPROFILERSINFO = BASEURL + "GetPanelistprofilersinfo";
    public static final String GETINCENTIVEDETAILSMOBILE = BASEURL + "GetIncentiveDetailsMobile";
    public static final String API_GETCAMPAIGNXML = BASEURL + "GetCampaignXml";
    public static final String API_BUYTICKET = BASEURL + "BuyTicket";
    public static final String API_GENERALSWEEPSTAKES = BASEURL + "GetSweepstakes";
    //public static final String API_GENERALREWARDS = BASEURL + "GeneralRewards"; OLD API
    public static final String API_GENERALREWARDS = BASEURL + "GeneralRewardsNew";
    public static final String API_CONTACTUS = BASEURL + "ContactUs";
    public static final String API_GENERALREDEEM = BASEURL + "GeneralRedeem";
    public static final String API_GETGENERALREWARDSNEW = BASEURL + "GetGeneralRewardsnew";
    //public static final String API_AUTOSOCIALLOGIN = BASEURL + "AutomaticSocialLogin";
    public static final String API_AUTOSOCIALLOGIN = BASEURL + "AutomaticSocialLogin";

    public static final String API_MOBILENUMBEREXISTS = BASEURL + "IsMobileNumberExists";
    public static final String API_ENCRYPTMOBILENUMBER = BASEURL + "GetEncryptString";
    public static final String API_MOBILEVERIFICATIONPIN = BASEURL + "GetMobileVerificationPin";
    public static final String API_SAVEMOBILE = BASEURL + "SaveMobile";
    public static final String API_ISVALIDUSER = BASEURL + "IsValidUser";
    public static final String API_SAVEPANELISTPROFILE = BASEURL + "SavePanelistProfiler";
    public static final String API_REGISTERPANELISTTHROUGHMOBILE = BASEURL + "RegisterPanelistthroughMobile_New"; //need toset lang culture
    public static final String API_FORGOTPASSWORD = BASEURL + "ForgotPassword";
    public static final String PANLISTAVAILABLESURVEYDATATABLE = BASEURL + "PanlistAvailableSurveyDatatable";
    public static final String API_SENDEMAILOPT = BASEURL + "ResendOTP";
    public static final String API_PANELIST_DEVICE_VERISON = BASEURL + "PanelistDeviceVersionSave";
    //public static final String API_VERIFYEMAILOTP = BASEURL + "OTPVerification";
    public static final String API_VERIFYEMAILOTP = BASEURL + "OTPVerification";
    public static final String LogMeteringData = BASEURL + "LogMeteringData"; // need to set langu culture
    public static final String API_GEOLOCATION = BASEURL + "GeoLocationSave";
    public static final String API_ODMmeterSTATUS = BASEURL + "ODMmeterstatus";
    //public static final String API_GETFAQINFO = BASEURL + "GetPanelistFAQ";
    public static final String API_GETFAQINFO = BASEURL + "GetPanelistFAQ"; //changed by dhanvarsh
    public static final String API_SAVEPAYTMID = BASEURL + "SavePanelistPaytmId";
    public static final String API_SAVEPAYTMID_AND_PAYPAL = BASEURL + "SavePanelistRedemptionOption";
    public static final String API_GETPAYTMID = BASEURL + "GetPanelistPayTMId";
    public static final String API_GETPAYTMID_AND_PAYPAL_ID = BASEURL + "GetPanelistRedemptionOption";
    public static final String API_CONTACTUS_LIST = BASEURL + "PanelistContactUS"; //changed by dhanvarsh // nned to add lang culture
    public static final String API_UPDATE_IMAGE = BASEURL + "UploadImage";
    public static final String API_TPSAppUpdate = BASEURL + "TPSAppUpdate";
    public static final String API_REFERRAL_VALIDATION = BASEURL + "ReferralCodeValidation";
    public static final String API_CHANGE_PASSWORD = BASEURL + "ChangePassword";
    public static final String API_GET_UNSUBSCRIBE_REASONS = BASEURL + "UnSubscribeGet"; // need to add lang culture
    public static final String API_GET_COMMUNICATION_PREFERENCE = BASEURL + "GetPanelistCommunicationPreference";
    public static final String API_GET_OPINIONPOLLSQUESTIONS = BASEURL + "GetOpinionPollsQuestions";
    public static final String API_GET_DYNAMIC_SURVEY = BASEURL + "CheckDynamicSurvey";
    public static final String API_GET_DYNAMIC_SURVEY_LINK = BASEURL + "GetDynamicSurveyLink";
    public static final String API_GET_SAVE_OPINION_POLLS = BASEURL + "SaveOpinionPolls";
    public static final String API_GET_OPINIONPOLLSRESULT = BASEURL + "GetOpinionPollsResult";
    public static final String API_SAVE_COMMUNICATION_PREFERENCE = BASEURL + "SavePanelistCommunicationPreference";
    public static final String API_GET_NOTIFICATIONLOG = BASEURL + "GetPanelistNotificationLog";
    //public static final String API_GET_ACTIVITYLOGS = BASEURL + "c";
    public static final String API_GET_ACTIVITYLOGS = BASEURL + "GetActivityLogs";
    public static final String API_UPDATE_NOTIFICATION_COUNT = BASEURL + "UpdateNotificationCount";
    public static final String API_GET_CUSTUMNOTIFICATION = BASEURL + "GetPanelistCustomNotification";
    public static final String API_RESUBSCRIBE_USER = BASEURL + "ResubscribeMailSent";
    public static final String API_RESUBSCRIBE_DETAILS_SAVE = BASEURL + "ResubscribeDetailsSave";
    public static final String API_LOGOUT = BASEURL + "PanelistLogOut";
    public static final String API_UNSUBSCRIBE_USER = BASEURL + "UnSubscribeSave";
    public static final String API_UNSUBSCRIBE_DELETE_USER = BASEURL + "UnSubscribeDeleteSaveWeb";

    //old code
//    public static final String API_MATCH_THE_PUZZLE_STAGE = "http://staging.thepanelstation.com/routing.aspx";
    //old code

    //new code
    //    API_MATCH_THE_PUZZLE_STAGE url for staging/QA/Production  on 17/06/2021

//    Staging Link: https://staging.thepanelstation.com/
//    QA Link: https://qa.thepanelstation.com/
//    Production Link: https://surveys.thepanelstation.com/

    public static final String API_MATCH_THE_PUZZLE_STAGE = "https://surveys.thepanelstation.com/routing.aspx";
    //new code



    public static final String API_CONSENT_LOG_SAVE = BASEURL + "ConsentLogSave";
    public static final String API_CONSENT_CHECK = BASEURL + "PanelistConsentTextcheck";
    public static final String API_SAVE_LANGUAGE = BASEURL + "LanguageCultureSave";
    public static final String API_SURVEY_BADGE_DESCRIPTION = BASEURL + "SurveyBadgeDescription";
    public static final String API_SURVEY_BADGE_BY_PANELIST_ID = BASEURL + "SurveyBadgeByPanelistId";

    public static final String API_STATIC_PAGE_CONTENT = BASEURL + "StaticPageContent";
    public static final String API_SURVEY_POINTS_REVIEW = BASEURL + "SurveyPointsReview";

    public static final String API_SURVEY_POINTS_REJECTED = BASEURL + "SurveyPointsRejects";

    /**
     * Get the Content Type
     */
    public static final String CONTENT_TYPE_TERMS_AND_CONDITIONS = "Terms and Conditions";
    public static final String CONTENT_TYPE_PRIVACY_POLICY = "Privacy Policy";


    public static final String PANEL_MESSAGE = "message";
    public static final String PANEL_EMAIL_ID = "1057";
    public static final String PANELID = "0";
    public static final String PANEL_FN = "1058";
    public static final String PANEL_LN = "1059";
    public static final String PANEL_GENDERID = "1063";
    public static final String PANEL_GENDER_MALE = "8928";
    public static final String PANEL_GENDER_FEMALE = "8929";
    public static final String PANEL_DOB = "1061";
    public static final String PANEL_COUNTRY = "2450";
    public static final String PANEL_CITY = "2451";
    public static final String PANEL_PHONENUMBER = "1060";
    public static final String PANEL_ZIP = "1411";
    public static final String PANEL_STATUS_INTAKE = "INTAKE";
    public static final String PANEL_STATUS_DOI = "DOI";
    public static final String CAMPAINID = "7422";
    public static final String DEVICETYPEID = "2"; // for android , 1 for IOS
    public static final String SIGNUP_KEY_USERINFO = "userinfo.data";
    public static final String SIGNUP_KEY_OTP = "usermailid.data";
    public static final String SIGNUP_KEY_COUNTRY = "userinfo.country";
    public static final String IGNUP_KEY_CITY = "userinfo.city";
    public static final int REQUESTCODE_VERIFY_USER = 1;
    public static final int REQUESTCODE_SIGNUP_USER = 2;
    public static final int REQUESTCODE_SIGNIN = 3;
    public static final int REQUESTCODE_AVAILABLE_SURVEY = 4;
    public static final int REQUEST_AVAILABLE_POINTS = 21;
    public static final int REQUESTCODE_PROFILERINFO = 5;
    public static final int REQUESTCODE_ODMMETERINGSTATUS = 0;
    public static final int REQUESTCODE_GEOLOCATIONSAVE = 1;
    public static final int REQUESTCODE_CAMPAIGN = 6;
    public static final int REQUESTCODE_BUYTICKET = 7;
    public static final int REQUESTCODE_GENERALSWEEPSTAKES = 8;
    public static final int REQUESTCODE_GENERALREWARDS = 9;
    public static final int REQUESTCODE_CONTACTUS = 10;
    public static final int REQUESTCODE_GENERALREDEEM = 11;
    public static final int REQUESTCODE_AUTOSOCIALLOGIN = 12;
    public static final int REQUESTCODE_MOBILEVERIFICATIONPIN = 13;
    public static final int REQUESTCODE_MOBILEEXISTS = 41;
    public static final int REQUESTCODE_ENCRYPTMOBILE = 42;
    public static final int REQUESTCODE_PANELIST_SAVE = 98;
    public static final int REQUESTCODE_SAVEMOBILE = 14;
    public static final int REQUESTCODE_SURVEYPOINTSREVIEW = 1234;
    public static final int REQUESTCODE_SURVEYPOINTSREJECTED = 12340;
    public static final int REQUESTCODE_SURVEYPOINTSREVIEWS = 12345;
    public static final int REQUESTCODE_ISVALIDUSER = 15;
    public static final int REQUESTCODE_SAVEPROFILER = 16;
    public static final int REQUESTCODE_FORGOTPASSWORD = 17;
    public static final int REQUEST_REDEEMFRAGMENT = 18;
    public static final int REQUEST_CONTACT_SUBJECT_LIST = 19;
    public static final int REQUEST_FAQ_LIST = 20;
    public static final int REQUEST_SAVE_PAYTM = 22;
    public static final int REQUEST_CHANGE_PASSWORD = 292;
    public static final int REQUEST_REFER_VALIDATION = 23;
    public static final int REQUEST_UPDATE_IMAGE = 24;
    public static final int REQUEST_VALIDATE_PAYTM = 25;
    public static final int REQUEST_UNSUBSCRIBE_USER = 2502;
    public static final int REQUEST_UNSUBSCRIBE_DELETE_USER = 2503;
    public static final int REQUEST_GET_UNSUBSCRIBE_REASONS = 2523;
    public static final int REQUEST_GET_COMMUNICATION_PREFERENCE = 2524;
    public static final int REQUEST_GET_OPINION_POLL_QUESTIONS = 2532;
    public static final int REQUEST_GET_DYNAMIC_SURVEY = 2536;
    public static final int REQUEST_GET_DYNAMIC_SURVEY_LINK = 2537;
    public static final int REQUEST_GET_OPINION_POLL_RESULT = 2534;
    public static final int REQUEST_GET_SAVE_OPINION_POLLS = 2533;
    public static final int REQUEST_SAVE_COMMUNICATION_PREFERENCE = 2525;
    public static final int REQUEST_LOGOUT = 2526;
    public static final int REQUEST_GET_NOTIFICATIONLOG = 2527;
    public static final int REQUEST_GET_CUSTUMNOTIFICATION = 2528;
    public static final int REQUEST_GET_RESUBSCRIBE_USER = 2530;
    public static final int REQUEST_RESUBSCRIBE_DETAILS_SAVE = 2531;
    public static final int REQUEST_GET_ACTIVITYLOGS = 2529;
    public static final int REQUEST_GET_UPDATE_NOTIFICATION_COUNT = 2535;
    public static final int REQUESTCODE_CONSENT_LOG_SAVE = 290;
    public static final int REQUESTCODE_GET_LANGUAGE_DETAILS = 2901;

    public static final int REQUEST_GET_STATIC_PAGE_CONTENT = 2999;

    public static final int PREF_FIRSTNAME = 1;
    public static final int PREF_LASTNAME = 2;
    public static final int PREF_PANELISTSESSIONID = 3;
    public static final int PREF_COMMONURL = 4;
    public static final int PREF_ID = 5;
    public static final int PREF_SESSIONID = 6;
    public static final int PREF_TNC = 7;
    public static final int PREF_PRIVACYPOLICY = 8;
    public static final int PREF_FBLINK = 9;
    public static final int PREF_LOCALECODE = 10;
    public static final int PREF_LOGINSUCCESS = 11;
    public static final int PREF_IPADDRESS = 12;
    public static final int PREF_COUNTRY = 13;
    public static final int PREF_EARNEDPOINTS_ = 14;
    public static final int PREF_SPENTPOINTS_ = 15;
    public static final int PREF_AVAILABLEPOINTS_ = 16;
    public static final int PREF_ISDCODE = 17;
    public static final int PREF_COUNTRYCODE = 18;
    public static final int PREF_MOBILENUMBERMAXLENGHT = 19;
    public static final int PREF_MOBILENUMBERVERIFIED = 20;
    public static final int PREF_PROFILERCOMPLETE = 21;
    public static final int ISLANGUAGECHANGECALLED = 22;
    public static final int EMAILID = 24;
    public static final int PREF_INREGISTRATIONPROCESS = 25;
    public static final int PREF_MOBILENUMBERMINLENGHT = 26;
    public static final int PREF_METERINGLOG = 27;
    public static final int PREF_FILENAME = 28;
    public static final int PREF_FILEPATH = 29;
    public static final int PREF_REFFERELCODE = 35;
    public static final int PREF_REFFERELPANELLISTID = 50;
    public static final int PREF_MOBILENUMBER = 31;
    public static final int PREF_GETPAYTM = 32;
    public static final int PREF_FB_LINK_SOCIAL_CONNECT = 36;
    public static final int PREF_TWITTER_LINK_SOCIAL_CONNECT = 33;
    public static final int PREF_GOOGLELINK_SOCIAL_CONNECT = 34;
    public static final int REQUEST_SURVEY_BADGE_DESCRIPTION = 35;
    public static final int REQUEST_SURVEY_BADGE_BY_PANELIST_ID = 36;
    public static final int PREF_BADGE_NAME = 37;
    public static final int REQUEST_CODE_SURVEY_COUNT = 38;
    public static final int PREF_MARKET_ID = 39;
    public static final int IS_REDEEM_INSTANTLY = 40;
    public static final int PREF_CONSENT_ONE = 281;
    public static final int PREF_CONSENT_TWO = 219;
    public static final int PREF_CONSENT_FINAL = 360;
    public static final int PREF_GPS_DIALOG = 1000;
    public static final int CHECK_GPS = 1001;
    public static final int GETGENERALREWARDSNEW_CODE =301;
    // Controls Declaration

    public static final String CONTROL_OPENTEXTSINGLELINE = "Open Text - Single Line";
    public static final String CONTROL_OPENTEXTMULTILINE = "Open Text - Multi Line";
    public static final String CONTROL_DROPDOWN = "Single Choice - Drop Down";
    public static final String CONTROL_DATE = "date";
    public static final String INREGISTRATIONPROCESS = "yes";

    /* Auto Social Login and Normal Login */
    public static final int LOGIN = 0;
    public static final int AUTOSOCIALLOGIN = 1;

    /* Mobile Number message Code */
    public static final String PANELSTATION = "RM-PNLSTN";

    /* Get OS Type */
    public static final String OS_TYPE = "Android";
    public static final String APP_GP_URL = "https://play.google.com/store/apps/details?id=com.panelManagement.activity";

    public static final String CHANGE_LANGUAGE = "changeLanguage";
    public static final String EXITPROFILERINBETWEEN = "21";
    public static final String EXIT_TERMINATEPROFILER = "23";
    public static final String EXIT_THANKYOU = "22";
    public static final String PUSHNOTIFY = "pushnotification";
    public static final String CHOOSETABS = "choosetab";
    public static final int PREF_THRESHHOLD = 500;
    public static final int PREF_ODMMETERINGSTATUS = 30;
    public static final String isJobScheduled = "isJobScheduled";
    public static final String AVAILABLE_POINTS = "available_points";
    public static final int REQUEST_UPDATE_PHONE_NUMBER = 566;
    public static final String API_UPDATE_PHONE_NUMBER = BASEURL + "updateusermobile";
    public static final int PREF_EMAIL_ID = 3623;
    public static final int REQUEST_MATCH_THE_PUZZLE = 23232;
    public static final int REQUEST_CHECK_VERSION_CODE = 56623;
    public static final int REQUEST_SAVE_LANGUAGE_CODE = 56624;


    public static final int REQUESTCODE_CONSENT_CHECK = 33;
    public static final int PREF_IS_CONSENT_GIVEN = 3523;
    public static final int PREF_HELP_DESK_EMAIL = 653;
    public static final int PREF_IS_PARTIAL_FIRST_CLICKED_CANCEL = 121314;
    public static final int ERRORLOG = 404;
    public static final int PREF_LOCALECODE_VALUE = 1023;

    public static boolean FirstAttemp_Metering = true;
    public static boolean EnableMetering = false;
    public static boolean IsSurvey = false;
    public static LocationModel locationcurrent = new LocationModel();


    //Date formats
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String INPUT_DATE_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String INPUT_DATE_ACTIVITY_LOG_FORMAT = "dd MMMM yyyy hh:mm a";
    public static final String INPUT_DATE_HH_MM_FORMAT = "hh:mm a";
}
