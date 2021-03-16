package com.panelManagement.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.panelManagement.database.DatabaseHelper;

public class DatabaseManager {

    public static DatabaseManager mManager;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private Context mContext;

    public DatabaseManager(Context context) {
        mDbHelper = new DatabaseHelper(context);
        mDatabase = mDbHelper.getWritableDatabase();
        this.mContext = context;
        /*
		 * Check database is updated or not
		 */
        if (!isDatabaseUpdated()) {
            mDbHelper.onUpgrade(mDatabase, 0, 0);
        }

    }

    public static DatabaseManager getInstance(Context context) {
        if (mManager == null)
            mManager = new DatabaseManager(context);

        return mManager;

    }

    @SuppressWarnings("static-access")
    public void gettingLoginDetails() {
        try {

            Cursor c = mDatabase.rawQuery("SELECT PASSWORD,LOGIN_EMAIL from " + DatabaseHelper.TABLE_DETAILS, null);
            // Cursor c = sqldb.query(TABLE_POSTING_DETAILS, null, null, null,
            // null, null, null);
            if (c.moveToFirst()) {
                DatabaseHelper.EMAIL_ID = c.getString(c.getColumnIndex("EMAIL_ID"));
                DatabaseHelper.PASSWORD = c.getString(c.getColumnIndex("PASSWORD"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public boolean isDatabaseUpdated() {

        try {
            Cursor c = mDatabase.rawQuery("SELECT I_AGREE from " + DatabaseHelper.TABLE_DETAILS, null);
            // Cursor c = sqldb.query(TABLE_POSTING_DETAILS, null, null, null,
            // null, null, null);
            if (c.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	/*
	 * public long insertLocale(int localeCode, String locale, boolean isRight)
	 * { ContentValues values = new ContentValues();
	 * 
	 * values.put(DatabaseHelper.LOCALE_ID, localeCode);
	 * values.put(DatabaseHelper.COLUMN_LOCALE_CODE, locale);
	 * values.put(DatabaseHelper.COLUMN_IS_RIGHT, isRight);
	 * 
	 * long rowId = mDatabase .insert(DatabaseHelper.TABLE_LOCALE, null,
	 * values);
	 * 
	 * return rowId;
	 * 
	 * }
	 */

	/*
	 * public long insertDetails(int localeId, HashMap<String, String>
	 * mDetailMap) {
	 * 
	 * String LOGIN,String FORGOT_PASSWORD,String REMEMBER_ME,String
	 * SIGN_IN,String EMAIL_ID,String PASSWORD, String SEND,String SUBMIT,String
	 * CANCEL,String LOGIN_EMAIL,String LOGIN_FLAKEY_CONECTION,String
	 * LOGIN_INVALID_DETAILS, String LOGIN_PASSWORD,String
	 * LOGIN_SESSION_EXPIRE,String LOGIN_UNREGISTERED,String
	 * LOGIN_VALID_EMAIL,String SURVEY,String REWARDPOINTS, String
	 * POINTS_EARNED, String POINTS_REDEEMED,String POINTS_AVAILABLE,String
	 * REDEMPTION,String AVAILABLE_SURVEYS, String MSG_PLEASE_WAIT,String
	 * MSG_REFERESHING,String MSG_CONNECTIVITY_LOST,String
	 * MSG_CONFIRM_OPEN,String OK,String MSG_NO_SURVEYS, String
	 * MSG_NO_REWARDS,String MSG_NO_REDEMPTIONS,String MSG_LOGOUT,String
	 * YES,String NO, String MSG_LOGOUT_CONNECTION,String MSG_TIMEOUT,String
	 * MSG_LOW_CONNECTIVITY,String MSG_INFORMATION
	 * 
	 * 
	 * ContentValues values = new ContentValues();
	 * values.put(DatabaseHelper.LOCALE_ID, localeId);
	 * 
	 * values.put(DatabaseHelper.LOGIN, mDetailMap.get(DatabaseHelper.LOGIN));
	 * values.put(DatabaseHelper.FORGOT_PASSWORD,
	 * mDetailMap.get(DatabaseHelper.FORGOT_PASSWORD));
	 * values.put(DatabaseHelper.REMEMBER_ME,
	 * mDetailMap.get(DatabaseHelper.REMEMBER_ME));
	 * values.put(DatabaseHelper.SIGN_IN,
	 * mDetailMap.get(DatabaseHelper.SIGN_IN));
	 * values.put(DatabaseHelper.EMAIL_ID,
	 * mDetailMap.get(DatabaseHelper.EMAIL_ID));
	 * values.put(DatabaseHelper.PASSWORD,
	 * mDetailMap.get(DatabaseHelper.PASSWORD));
	 * 
	 * values.put(DatabaseHelper.SEND, mDetailMap.get(DatabaseHelper.SEND));
	 * values.put(DatabaseHelper.SUBMIT, mDetailMap.get(DatabaseHelper.SUBMIT));
	 * values.put(DatabaseHelper.CANCEL, mDetailMap.get(DatabaseHelper.CANCEL));
	 * values.put(DatabaseHelper.LOGIN_EMAIL,
	 * mDetailMap.get(DatabaseHelper.LOGIN_EMAIL));
	 * values.put(DatabaseHelper.LOGIN_FLAKEY_CONECTION,
	 * mDetailMap.get(DatabaseHelper.LOGIN_FLAKEY_CONECTION));
	 * values.put(DatabaseHelper.LOGIN_INVALID_DETAILS,
	 * mDetailMap.get(DatabaseHelper.LOGIN_INVALID_DETAILS));
	 * 
	 * values.put(DatabaseHelper.LOGIN_PASSWORD,
	 * mDetailMap.get(DatabaseHelper.LOGIN_PASSWORD));
	 * values.put(DatabaseHelper.LOGIN_SESSION_EXPIRE,
	 * mDetailMap.get(DatabaseHelper.LOGIN_SESSION_EXPIRE));
	 * values.put(DatabaseHelper.LOGIN_UNREGISTERED,
	 * mDetailMap.get(DatabaseHelper.LOGIN_UNREGISTERED));
	 * values.put(DatabaseHelper.LOGIN_VALID_EMAIL,
	 * mDetailMap.get(DatabaseHelper.LOGIN_VALID_EMAIL));
	 * values.put(DatabaseHelper.SETTINGS,
	 * mDetailMap.get(DatabaseHelper.SETTINGS));
	 * values.put(DatabaseHelper.SURVEY, mDetailMap.get(DatabaseHelper.SURVEY));
	 * values.put(DatabaseHelper.REWARDPOINTS,
	 * mDetailMap.get(DatabaseHelper.REWARDPOINTS));
	 * 
	 * values.put(DatabaseHelper.POINTS_EARNED,
	 * mDetailMap.get(DatabaseHelper.POINTS_EARNED));
	 * values.put(DatabaseHelper.POINTS_REDEEMED,
	 * mDetailMap.get(DatabaseHelper.POINTS_REDEEMED));
	 * values.put(DatabaseHelper.POINTS_AVAILABLE,
	 * mDetailMap.get(DatabaseHelper.POINTS_AVAILABLE));
	 * values.put(DatabaseHelper.REDEMPTION,
	 * mDetailMap.get(DatabaseHelper.REDEMPTION));
	 * values.put(DatabaseHelper.AVAILABLE_SURVEYS,
	 * mDetailMap.get(DatabaseHelper.AVAILABLE_SURVEYS));
	 * values.put(DatabaseHelper.MSG_PLEASE_WAIT,
	 * mDetailMap.get(DatabaseHelper.MSG_PLEASE_WAIT));
	 * 
	 * values.put(DatabaseHelper.MSG_REFERESHING,
	 * mDetailMap.get(DatabaseHelper.MSG_REFERESHING));
	 * values.put(DatabaseHelper.MSG_CONNECTIVITY_LOST,
	 * mDetailMap.get(DatabaseHelper.MSG_CONNECTIVITY_LOST));
	 * values.put(DatabaseHelper.MSG_CONFIRM_OPEN,
	 * mDetailMap.get(DatabaseHelper.MSG_CONFIRM_OPEN));
	 * values.put(DatabaseHelper.OK, mDetailMap.get(DatabaseHelper.OK));
	 * values.put(DatabaseHelper.MSG_NO_SURVEYS,
	 * mDetailMap.get(DatabaseHelper.MSG_NO_SURVEYS));
	 * 
	 * values.put(DatabaseHelper.MSG_NO_REWARDS,
	 * mDetailMap.get(DatabaseHelper.MSG_NO_REWARDS));
	 * values.put(DatabaseHelper.MSG_NO_REDEMPTIONS,
	 * mDetailMap.get(DatabaseHelper.MSG_NO_REDEMPTIONS));
	 * values.put(DatabaseHelper.MSG_LOGOUT,
	 * mDetailMap.get(DatabaseHelper.MSG_LOGOUT));
	 * values.put(DatabaseHelper.YES, mDetailMap.get(DatabaseHelper.YES));
	 * values.put(DatabaseHelper.NO, mDetailMap.get(DatabaseHelper.NO));
	 * 
	 * values.put(DatabaseHelper.MSG_LOGOUT_CONNECTION,
	 * mDetailMap.get(DatabaseHelper.MSG_LOGOUT_CONNECTION));
	 * values.put(DatabaseHelper.MSG_TIMEOUT,
	 * mDetailMap.get(DatabaseHelper.MSG_TIMEOUT));
	 * values.put(DatabaseHelper.MSG_LOW_CONNECTIVITY,
	 * mDetailMap.get(DatabaseHelper.MSG_LOW_CONNECTIVITY));
	 * values.put(DatabaseHelper.MSG_INFORMATION,
	 * mDetailMap.get(DatabaseHelper.MSG_INFORMATION));
	 * values.put(DatabaseHelper.LANGUAGE,
	 * mDetailMap.get(DatabaseHelper.LANGUAGE));
	 * 
	 * 
	 * 
	 * values.put(DatabaseHelper.TAKE_BY,
	 * mDetailMap.get(DatabaseHelper.TAKE_BY)); values.put(DatabaseHelper.EARN,
	 * mDetailMap.get(DatabaseHelper.EARN)); values.put(DatabaseHelper.POINTS,
	 * mDetailMap.get(DatabaseHelper.POINTS));
	 * 
	 * 
	 * values.put(DatabaseHelper.EARNED, mDetailMap.get(DatabaseHelper.EARNED));
	 * values.put(DatabaseHelper.VOUCHER_CODE,
	 * mDetailMap.get(DatabaseHelper.VOUCHER_CODE));
	 * values.put(DatabaseHelper.REDEEMED,
	 * mDetailMap.get(DatabaseHelper.REDEEMED));
	 * values.put(DatabaseHelper.STATUS, mDetailMap.get(DatabaseHelper.STATUS));
	 * 
	 * 
	 * values.put(DatabaseHelper.DONE, mDetailMap.get(DatabaseHelper.DONE));
	 * values.put(DatabaseHelper.VALID_TILL,
	 * mDetailMap.get(DatabaseHelper.VALID_TILL));
	 * 
	 * 
	 * values.put(DatabaseHelper.MONTH_JAN,
	 * mDetailMap.get(DatabaseHelper.MONTH_JAN));
	 * values.put(DatabaseHelper.MONTH_FEB,
	 * mDetailMap.get(DatabaseHelper.MONTH_FEB));
	 * values.put(DatabaseHelper.MONTH_MAR,
	 * mDetailMap.get(DatabaseHelper.MONTH_MAR));
	 * values.put(DatabaseHelper.MONTH_APR,
	 * mDetailMap.get(DatabaseHelper.MONTH_APR));
	 * values.put(DatabaseHelper.MONTH_MAY,
	 * mDetailMap.get(DatabaseHelper.MONTH_MAY));
	 * values.put(DatabaseHelper.MONTH_JUN,
	 * mDetailMap.get(DatabaseHelper.MONTH_JUN));
	 * values.put(DatabaseHelper.MONTH_JUL,
	 * mDetailMap.get(DatabaseHelper.MONTH_JUL));
	 * values.put(DatabaseHelper.MONTH_AUG,
	 * mDetailMap.get(DatabaseHelper.MONTH_AUG));
	 * values.put(DatabaseHelper.MONTH_SEP,
	 * mDetailMap.get(DatabaseHelper.MONTH_SEP));
	 * values.put(DatabaseHelper.MONTH_OCT,
	 * mDetailMap.get(DatabaseHelper.MONTH_OCT));
	 * values.put(DatabaseHelper.MONTH_NOV,
	 * mDetailMap.get(DatabaseHelper.MONTH_NOV));
	 * values.put(DatabaseHelper.MONTH_DEC,
	 * mDetailMap.get(DatabaseHelper.MONTH_DEC));
	 * 
	 * values.put(DatabaseHelper.I_AGREE,
	 * mDetailMap.get(DatabaseHelper.I_AGREE));
	 * values.put(DatabaseHelper.PRIVACY_POLICY,
	 * mDetailMap.get(DatabaseHelper.PRIVACY_POLICY));
	 * values.put(DatabaseHelper.AND_TEXT,
	 * mDetailMap.get(DatabaseHelper.AND_TEXT));
	 * values.put(DatabaseHelper.TERMS_CONDITION,
	 * mDetailMap.get(DatabaseHelper.TERMS_CONDITION));
	 * values.put(DatabaseHelper.I_ALLOW,
	 * mDetailMap.get(DatabaseHelper.I_ALLOW)); values.put(DatabaseHelper.VISIT,
	 * mDetailMap.get(DatabaseHelper.VISIT));
	 * 
	 * 
	 * 
	 * long rowId = mDatabase.insert(DatabaseHelper.TABLE_DETAILS, null,
	 * values);
	 * 
	 * return rowId;
	 * 
	 * }
	 */

	/*
	 * public void getLocaleList() { ArrayList<LocaleModel> mLocaleList = new
	 * ArrayList<LocaleModel>();
	 * 
	 * Cursor c = mDatabase.query(DatabaseHelper.TABLE_LOCALE, null, null, null,
	 * null, null, null);
	 * 
	 * while (c.moveToNext()) {
	 * 
	 * boolean isRight = false;
	 * 
	 * if (c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_IS_RIGHT)) == 1)
	 * isRight = true;
	 * 
	 * LocaleModel mLocale = new LocaleModel(c.getString(c
	 * .getColumnIndex(DatabaseHelper.COLUMN_LOCALE_CODE)),
	 * c.getInt(c.getColumnIndex(DatabaseHelper.LOCALE_ID)), isRight);
	 * mLocaleList.add(mLocale);
	 * 
	 * } c.close(); REMOVE-LOCALE
	 * //LocaleDetailModel.getInstance().setLocaleList(mLocaleList);
	 * 
	 * }
	 */

	/*
	 * public boolean getLocaleDetails(int localeId) { HashMap<String, String>
	 * mLocaleMap = new HashMap<String, String>();
	 * 
	 * String selection = DatabaseHelper.LOCALE_ID + "=" + localeId;
	 * 
	 * Cursor c = mDatabase.query(DatabaseHelper.TABLE_DETAILS, null, selection,
	 * null, null, null, null);
	 * 
	 * while (c.moveToNext()) {
	 * 
	 * mLocaleMap.put(DatabaseHelper.LOGIN,
	 * c.getString(c.getColumnIndex(DatabaseHelper.LOGIN)));
	 * mLocaleMap.put(DatabaseHelper.FORGOT_PASSWORD, c.getString(c
	 * .getColumnIndex(DatabaseHelper.FORGOT_PASSWORD)));
	 * mLocaleMap.put(DatabaseHelper.REMEMBER_ME,
	 * c.getString(c.getColumnIndex(DatabaseHelper.REMEMBER_ME)));
	 * mLocaleMap.put(DatabaseHelper.SIGN_IN,
	 * c.getString(c.getColumnIndex(DatabaseHelper.SIGN_IN)));
	 * mLocaleMap.put(DatabaseHelper.EMAIL_ID,
	 * c.getString(c.getColumnIndex(DatabaseHelper.EMAIL_ID)));
	 * mLocaleMap.put(DatabaseHelper.PASSWORD,
	 * c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.SEND,
	 * c.getString(c.getColumnIndex(DatabaseHelper.SEND)));
	 * mLocaleMap.put(DatabaseHelper.SUBMIT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.SUBMIT)));
	 * mLocaleMap.put(DatabaseHelper.CANCEL,
	 * c.getString(c.getColumnIndex(DatabaseHelper.CANCEL)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_EMAIL,
	 * c.getString(c.getColumnIndex(DatabaseHelper.LOGIN_EMAIL)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_FLAKEY_CONECTION, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_FLAKEY_CONECTION)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_INVALID_DETAILS, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_INVALID_DETAILS)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.LOGIN_PASSWORD, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_PASSWORD)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_SESSION_EXPIRE, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_SESSION_EXPIRE)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_UNREGISTERED, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_UNREGISTERED)));
	 * mLocaleMap.put(DatabaseHelper.LOGIN_VALID_EMAIL, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LOGIN_VALID_EMAIL)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.SETTINGS,
	 * c.getString(c.getColumnIndex(DatabaseHelper.SETTINGS)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.SURVEY,c.getString(c.getColumnIndex(
	 * DatabaseHelper.SURVEY)));
	 * //mLocaleMap.put(DatabaseHelper.SURVEY,mContext.
	 * getString(R.string.Survey)); mLocaleMap.put(DatabaseHelper.REWARDPOINTS,
	 * c.getString(c.getColumnIndex(DatabaseHelper.REWARDPOINTS)));
	 * 
	 * mLocaleMap .put(DatabaseHelper.POINTS_EARNED, c.getString(c
	 * .getColumnIndex(DatabaseHelper.POINTS_EARNED)));
	 * mLocaleMap.put(DatabaseHelper.POINTS_REDEEMED, c.getString(c
	 * .getColumnIndex(DatabaseHelper.POINTS_REDEEMED)));
	 * mLocaleMap.put(DatabaseHelper.POINTS_AVAILABLE, c.getString(c
	 * .getColumnIndex(DatabaseHelper.POINTS_AVAILABLE)));
	 * mLocaleMap.put(DatabaseHelper.REDEMPTION,
	 * c.getString(c.getColumnIndex(DatabaseHelper.REDEMPTION)));
	 * mLocaleMap.put(DatabaseHelper.AVAILABLE_SURVEYS, c.getString(c
	 * .getColumnIndex(DatabaseHelper.AVAILABLE_SURVEYS)));
	 * mLocaleMap.put(DatabaseHelper.MSG_PLEASE_WAIT, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_PLEASE_WAIT)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.MSG_REFERESHING, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_REFERESHING)));
	 * mLocaleMap.put(DatabaseHelper.MSG_CONNECTIVITY_LOST, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_CONNECTIVITY_LOST)));
	 * mLocaleMap.put(DatabaseHelper.MSG_CONFIRM_OPEN, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_CONFIRM_OPEN)));
	 * mLocaleMap.put(DatabaseHelper.OK,
	 * c.getString(c.getColumnIndex(DatabaseHelper.OK)));
	 * mLocaleMap.put(DatabaseHelper.MSG_NO_SURVEYS, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_NO_SURVEYS)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.MSG_NO_REWARDS, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_NO_REWARDS)));
	 * mLocaleMap.put(DatabaseHelper.MSG_NO_REDEMPTIONS, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_NO_REDEMPTIONS)));
	 * mLocaleMap.put(DatabaseHelper.MSG_LOGOUT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MSG_LOGOUT)));
	 * mLocaleMap.put(DatabaseHelper.YES,
	 * c.getString(c.getColumnIndex(DatabaseHelper.YES)));
	 * mLocaleMap.put(DatabaseHelper.NO,
	 * c.getString(c.getColumnIndex(DatabaseHelper.NO)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.MSG_LOGOUT_CONNECTION, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_LOGOUT_CONNECTION)));
	 * mLocaleMap.put(DatabaseHelper.MSG_TIMEOUT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MSG_TIMEOUT)));
	 * mLocaleMap.put(DatabaseHelper.MSG_LOW_CONNECTIVITY, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_LOW_CONNECTIVITY)));
	 * mLocaleMap.put(DatabaseHelper.MSG_INFORMATION, c.getString(c
	 * .getColumnIndex(DatabaseHelper.MSG_INFORMATION)));
	 * mLocaleMap.put(DatabaseHelper.LANGUAGE, c.getString(c
	 * .getColumnIndex(DatabaseHelper.LANGUAGE)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.TAKE_BY,
	 * c.getString(c.getColumnIndex(DatabaseHelper.TAKE_BY)));
	 * mLocaleMap.put(DatabaseHelper.EARN,
	 * c.getString(c.getColumnIndex(DatabaseHelper.EARN)));
	 * mLocaleMap.put(DatabaseHelper.POINTS,
	 * c.getString(c.getColumnIndex(DatabaseHelper.POINTS)));
	 * 
	 * 
	 * mLocaleMap.put(DatabaseHelper.EARNED,
	 * c.getString(c.getColumnIndex(DatabaseHelper.EARNED)));
	 * mLocaleMap.put(DatabaseHelper.VOUCHER_CODE,
	 * c.getString(c.getColumnIndex(DatabaseHelper.VOUCHER_CODE)));
	 * mLocaleMap.put(DatabaseHelper.REDEEMED,
	 * c.getString(c.getColumnIndex(DatabaseHelper.REDEEMED)));
	 * mLocaleMap.put(DatabaseHelper.STATUS,
	 * c.getString(c.getColumnIndex(DatabaseHelper.STATUS)));
	 * 
	 * 
	 * mLocaleMap.put(DatabaseHelper.DONE,
	 * c.getString(c.getColumnIndex(DatabaseHelper.DONE)));
	 * mLocaleMap.put(DatabaseHelper.VALID_TILL,
	 * c.getString(c.getColumnIndex(DatabaseHelper.VALID_TILL)));
	 * 
	 * 
	 * mLocaleMap.put(DatabaseHelper.MONTH_JAN,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_JAN)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_FEB,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_FEB)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_MAR,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_MAR)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_APR,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_APR)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_MAY,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_MAY)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_JUN,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_JUN)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_JUL,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_JUL)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_AUG,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_AUG)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_SEP,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_SEP)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_OCT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_OCT)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_NOV,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_NOV)));
	 * mLocaleMap.put(DatabaseHelper.MONTH_DEC,
	 * c.getString(c.getColumnIndex(DatabaseHelper.MONTH_DEC)));
	 * 
	 * mLocaleMap.put(DatabaseHelper.I_AGREE,
	 * c.getString(c.getColumnIndex(DatabaseHelper.I_AGREE)));
	 * mLocaleMap.put(DatabaseHelper.PRIVACY_POLICY,
	 * c.getString(c.getColumnIndex(DatabaseHelper.PRIVACY_POLICY)));
	 * mLocaleMap.put(DatabaseHelper.AND_TEXT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.AND_TEXT)));
	 * mLocaleMap.put(DatabaseHelper.TERMS_CONDITION,
	 * c.getString(c.getColumnIndex(DatabaseHelper.TERMS_CONDITION)));
	 * mLocaleMap.put(DatabaseHelper.I_ALLOW,
	 * c.getString(c.getColumnIndex(DatabaseHelper.I_ALLOW)));
	 * mLocaleMap.put(DatabaseHelper.VISIT,
	 * c.getString(c.getColumnIndex(DatabaseHelper.VISIT)));
	 * 
	 * 
	 * 
	 * 
	 * } REMOVE-LOCALE
	 * //LocaleDetailModel.getInstance().setLocaledetails(mLocaleMap);
	 * 
	 * if (c.getCount() > 0) return true; else return false; }
	 */

    public boolean isRightAlignMent(int localeid) {

        String selection = DatabaseHelper.LOCALE_ID + "=" + localeid;

        Cursor c = mDatabase.query(DatabaseHelper.TABLE_LOCALE, null,
                selection, null, null, null, null);
        int val = 0;
        while (c.moveToNext()) {
            val = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_IS_RIGHT));

        }
        if (val == 0)
            return false;
        else
            return true;
    }

    public void clearDatabase() {
        mDatabase.delete(DatabaseHelper.TABLE_DETAILS, null, null);
        mDatabase.delete(DatabaseHelper.TABLE_LOCALE, null, null);

    }

}
