package com.panelManagement.model;

import android.content.Context;

import com.panelManagement.activity.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/*REMOVE-LOCALE*/
public class LocaleDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public static LocaleDetailModel mModel;
    public static Context mContext = null;
    private HashMap<String, String> mLocaldetails;
    private ArrayList<LocaleModel> mLocaleList;

    public static LocaleDetailModel getInstance(Context context) {
        if (mModel == null)
            mModel = new LocaleDetailModel();
        mContext = context;
        return mModel;

    }

    public HashMap<String, String> getLocaledetails() {
        return mLocaldetails;
    }

    public void setLocaledetails(HashMap<String, String> mLocaldetails) {
        this.mLocaldetails = mLocaldetails;
    }

    public ArrayList<LocaleModel> getLocaleList() {
        if (mLocaleList == null)
            mLocaleList = getSupportedLocaleList();
        return mLocaleList;
    }

    public void setLocaleList(ArrayList<LocaleModel> mLocaleList) {
        this.mLocaleList = mLocaleList;
    }

    public ArrayList<LocaleModel> getSupportedLocaleList() {
        ArrayList<LocaleModel> mLocaleList = new ArrayList<LocaleModel>();

        String[] supportedLanguages = mContext.getResources().getStringArray(R.array.supported_lang);
        String[] supportedLanguagesCodes = mContext.getResources().getStringArray(R.array.supported_language_identification);

        for (int index = 0; index < supportedLanguages.length; index++) {

            boolean isRight = false;
            try {
                LocaleModel mLocale = new LocaleModel(supportedLanguages[index], Integer.parseInt(supportedLanguagesCodes[index]), isRight);
                mLocaleList.add(mLocale);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mLocaleList;
    }

}
