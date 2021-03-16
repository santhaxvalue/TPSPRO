package com.panelManagement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_LOCALE = "locale";
    public static final String TABLE_DETAILS = "details";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCALE_CODE = "locale_code";
    public static final String COLUMN_IS_RIGHT = "isRight";
    public static final String LOCALE_ID = "locale_id";
    public static final String DATABASE_NAME = "panelstaion.db";
    private static final int DATABASE_VERSION = 2;
    // DATABASE TAGS
    public static String LOGIN = "Login";
    public static String FORGOT_PASSWORD = "ForgotPassword";
    public static String REMEMBER_ME = "RememberMe";
    public static String SIGN_IN = "SignIn";
    public static String EMAIL_ID = "EmailId";
    public static String PASSWORD = "Password";
    public static String SEND = "Send";
    public static String SUBMIT = "Submit";
    public static String CANCEL = "Cancel";
    public static String LOGIN_EMAIL = "login_email";
    public static String LOGIN_PASSWORD = "login_password";
    public static String LOGIN_VALID_EMAIL = "login_valid_email";
    public static String LOGIN_FLAKEY_CONECTION = "login_flakey_connection";
    public static String LOGIN_SESSION_EXPIRE = "login_session_expire";
    public static String LOGIN_INVALID_DETAILS = "login_invalid_details";
    public static String LOGIN_UNREGISTERED = "login_unregistered";
    public static String SETTINGS = "settings";
    public static String SURVEY = "Survey";
    public static String REWARDPOINTS = "RewardPoints";
    public static String POINTS_EARNED = "points_earned";
    public static String POINTS_REDEEMED = "points_redeemed";
    public static String POINTS_AVAILABLE = "pointsavailable";
    public static String REDEMPTION = "Redemption";
    public static String AVAILABLE_SURVEYS = "AvailableSurveys";
    public static String MSG_PLEASE_WAIT = "msg_please_wait";
    public static String MSG_REFERESHING = "msg_refreshing";
    public static String MSG_CONNECTIVITY_LOST = "msg_connectivity_lost";
    public static String MSG_CONFIRM_OPEN = "msg_confirm_open";
    public static String OK = "Ok";
    public static String MSG_NO_SURVEYS = "msg_no_surveys";
    public static String MSG_NO_REWARDS = "msg_no_rewards";
    public static String MSG_NO_REDEMPTIONS = "msg_no_redemoption";
    public static String MSG_LOGOUT = "msg_logout";
    public static String YES = "yes";
    public static String NO = "no";
    public static String MSG_LOGOUT_CONNECTION = "msg_loggout_connection";
    public static String MSG_TIMEOUT = "msg_timeout";
    public static String MSG_LOW_CONNECTIVITY = "msg_low_connectivity";
    public static String MSG_INFORMATION = "msg_information";
    public static String LOADING = "loading";
    public static String LANGUAGE = "language";
    public static String TAKE_BY = "take_by";
    public static String EARN = "earn";
    public static String POINTS = "points";
    public static String EARNED = "earned";
    public static String VOUCHER_CODE = "voucher_code";
    public static String STATUS = "status";
    public static String REDEEMED = "redeemed";
    public static String DONE = "done";
    public static String VALID_TILL = "valid_til";
    public static String MONTH_JAN = "month_jan";
    public static String MONTH_FEB = "month_feb";
    public static String MONTH_MAR = "month_mar";
    public static String MONTH_APR = "month_apr";
    public static String MONTH_MAY = "month_may";
    public static String MONTH_JUN = "month_jun";
    public static String MONTH_JUL = "month_jul";
    public static String MONTH_AUG = "month_aug";
    public static String MONTH_SEP = "month_sep";
    public static String MONTH_OCT = "month_oct";
    public static String MONTH_NOV = "month_nov";
    public static String MONTH_DEC = "month_dec";
    public static String I_AGREE = "I_AGREE";
    public static String PRIVACY_POLICY = "PRIVACY_POLICY";
    public static String AND_TEXT = "AND_TEXT";
    public static String TERMS_CONDITION = "TERMS_CONDITION";
    public static String I_ALLOW = "I_ALLOW";
    public static String VISIT = "VISIT";
    public static String SIGNIN = "SignIn";
    public static String SIGNUP = "SignUp";
    public static String LOGIN_THROUGH = "login_through";
    public static String ITsQUICKER = "quicker";
    public static String REG_THROUGH_MAIL = "mailregistration";
    public static String CONF_EMAIL = "conf_email";
    public static String START_EARNING = "start_earning";
    public static String USER_NAME = "user_name";
    public static String PERSONAL_INFO = "personal_info";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String GENDER = "gender";
    public static String DOB = "dob";
    public static String MONTH = "month";
    public static String DAY = "day";
    public static String YEAR = "year";
    public static String NEXT = "next";
    public static String ENSURE_PRIVACY = "ensure_privacy";
    public static String CONTACT_INFO = "contact_info";
    public static String PHONE_NUMBER = "ph_number";
    public static String COUNTRY = "country";
    public static String CITY = "city";
    public static String ZIPCODE = "zipcode";
    public static String ADDRESS = "address";
    public static String REG_COMPLETE = "registration_complete";
    public static String LOGIN_CREDTOMAIL = "login_creditional_to_mail";
    public static String REFER_SPAM = "refer_spam";
    public static String CHECK_MAIL = "check_mail";
    public static String GET_IN_TOUCH = "get_in_touch";
    private Context mContext;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Log.e("Neha","On creating database");
        try {
            db.execSQL("create table " + TABLE_LOCALE + "(" + COLUMN_ID
                    + " integer primary key autoincrement, " + LOCALE_ID
                    + " integer, " + COLUMN_LOCALE_CODE + " text, "
                    + COLUMN_IS_RIGHT + " integer);");

            db.execSQL("create table " + TABLE_DETAILS + "(" + COLUMN_ID
                    + " integer primary key autoincrement, " + LOCALE_ID
                    + " integer, " + LOGIN + " text, " + FORGOT_PASSWORD
                    + " text, " + REMEMBER_ME + " text, " + SIGN_IN + " text, "
                    + EMAIL_ID + " text, " + PASSWORD + " text, " + SEND
                    + " text, " + SUBMIT + " text, " + CANCEL + " text, "
                    + LOGIN_EMAIL + " text, " + LOGIN_FLAKEY_CONECTION
                    + " text, " + LOGIN_INVALID_DETAILS + " text, "
                    + LOGIN_PASSWORD + " text, " + LOGIN_SESSION_EXPIRE
                    + " text, " + LOGIN_UNREGISTERED + " text, "
                    + LOGIN_VALID_EMAIL + " text, " + SETTINGS + " text, "
                    + SURVEY + " text, " + REWARDPOINTS + " text, "
                    + POINTS_EARNED + " text, " + POINTS_REDEEMED + " text, "
                    + POINTS_AVAILABLE + " text, " + REDEMPTION + " text, "
                    + AVAILABLE_SURVEYS + " text, " + MSG_PLEASE_WAIT
                    + " text, " + MSG_REFERESHING + " text, "
                    + MSG_CONNECTIVITY_LOST + " text, " + MSG_CONFIRM_OPEN
                    + " text, " + OK + " text, " + MSG_NO_SURVEYS + " text, "
                    + MSG_NO_REWARDS + " text, " + MSG_NO_REDEMPTIONS
                    + " text, " + MSG_LOGOUT + " text, " + YES + " text, " + NO
                    + " text, " + MSG_LOGOUT_CONNECTION + " text, "
                    + MSG_TIMEOUT + " text, " + MSG_LOW_CONNECTIVITY
                    + " text, " + MSG_INFORMATION + " text, " + LANGUAGE
                    + " text, " + TAKE_BY + " text, " + EARN + " text, "
                    + POINTS + " text, " + EARNED + " text, " + VOUCHER_CODE
                    + " text, " + REDEEMED + " text, " + STATUS + " text, "
                    + DONE + " text, " + VALID_TILL + " text, " + MONTH_JAN
                    + " text, " + MONTH_FEB + " text, " + MONTH_MAR + " text, "
                    + MONTH_APR + " text, " + MONTH_MAY + " text, " + MONTH_JUN
                    + " text, " + MONTH_JUL + " text, " + MONTH_AUG + " text, "
                    + MONTH_SEP + " text, " + MONTH_OCT + " text, " + MONTH_NOV
                    + " text, " + MONTH_DEC + " text, "
                    + "I_AGREE  text, PRIVACY_POLICY  text,"
                    + "AND_TEXT  text,  TERMS_CONDITION  text, "
                    + "I_ALLOW  text,VISIT text);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCALE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        onCreate(db);

    }

}
