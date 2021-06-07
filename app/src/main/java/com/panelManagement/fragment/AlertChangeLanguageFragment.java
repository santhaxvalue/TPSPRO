package com.panelManagement.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.panelManagement.activity.BuildConfig;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.listener.LocaleChangeListener;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.model.ChangeLanguageModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.ChangeLanguageListAdapter;
import com.panelManagement.webservices.AsyncHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlertChangeLanguageFragment extends BaseFragment implements LocaleChangeListener, OnBackClickListener {

    ChangeLanguageListAdapter adaptorLanguage;
    ArrayList<ChangeLanguageModel> langarray;
    Activity mActivity = null;
    TextView tv_language;
    private ListView mListView;
    private String[] countryList, localeCode;
    private boolean IsFirst = true;
    private LocaleChangeListener listener;

    public AlertChangeLanguageFragment() {
        super();
    }

    public static AlertChangeLanguageFragment newInstance() {
        Bundle args = new Bundle();
        AlertChangeLanguageFragment fragment = new AlertChangeLanguageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        langarray = new ArrayList<>();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof LocaleChangeListener)
            listener = (LocaleChangeListener) activity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_language, container, false);

        mActivity = getActivity();
        tv_language = view.findViewById(R.id.tv_language);

        mListView = view.findViewById(R.id.lv_survey);

        TextView emptyText = view.findViewById(android.R.id.empty);
        mListView.setEmptyView(emptyText);

        countryList = getResources().getStringArray(R.array.supported_lang);
        localeCode = getResources().getStringArray(R.array.supported_lang_code);

        for (int i = 0; i < countryList.length; i++) {

            String localeId = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_LOCALECODE_VALUE);

            Log.d("newwelcomeone:","newwelcomeone:"+localeId);

            if (localeId.equalsIgnoreCase(localeCode[i])) {
                langarray.add(new ChangeLanguageModel(countryList[i], true, localeCode[i]));
            } else
                langarray.add(new ChangeLanguageModel(countryList[i], false, localeCode[i]));
        }
        adaptorLanguage = new ChangeLanguageListAdapter(getActivity(), langarray, this);
        mListView.setAdapter(adaptorLanguage);
        return view;
    }

    @Override
    public void vLayout(String res, int requestcode) {

    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void setLocaleChange(boolean isLangChanged, String lang_code) {
        Dialog dialog = Utility.MandatoryalertDialog(getContext(), getString(R.string.dialog_login));
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            jsonObject.put("LanguageCulture", lang_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(getContext(), Constants.API_SAVE_LANGUAGE, jsonObject.toString(), Constants.REQUEST_SAVE_LANGUAGE_CODE, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, lang_code);
                Utility.setLocaleLanguage(lang_code, mActivity);
                setLanguage();
                doLanguageChange(isLangChanged, lang_code, dialog);
            }

            @Override
            public void onRequestError(Exception e, int requestCode) {
                Dialog dialog = Utility.MandatoryalertDialog(getContext(), mActivity.getResources().getString(R.string.async_task_error));
                dialog.show();
            }

            @Override
            public void onRequestStarted(int requestCode) {

            }
        });
        mAppRequest.execute();
    }

    private void doLanguageChange(boolean isLangChanged, String lang_code, Dialog dialog) {
        if (listener != null) {
            dialog.dismiss();
            listener.setLocaleChange(isLangChanged, lang_code);
            IsFirst = false;
        }

        tv_language.setText(mActivity.getString(R.string.txt_language));

        if (!IsFirst) {
            IsFirst = true;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container_fragment, AlertSettingScreenFragment.newInstance()).addToBackStack(HomeActivity.MYFRAGMENTKEY);
            fragmentTransaction.commit();
            fragmentManager.popBackStack(HomeActivity.SETTINGFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        dialog.dismiss();
    }

    @Override
    public void onBackButtonPressed() {
        /*dismiss();*/
        if (listener != null)
            listener.onBackButtonPressed();
    }

    private void setLanguage() {
        String strLocalCode = InformatePreferences.getStringPrefrence(mActivity, Constants.PREF_LOCALECODE);
        switch (strLocalCode) {
            case "en":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "hi-in");
                break;
            case "ar":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ar-EG");
                break;
            case "de-rDE":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "de-DE");
                break;
            case "es":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-CL");
                break;
            case "fr":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fr-FR");
                break;
            case "in":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "id-ID");
                break;
            case "it":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "it-IT");
                break;
            case "ko":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ko-KR");
                break;
            case "ms":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ms-MY");
                break;
            case "pl":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pl-PL");
                break;
            case "pt":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pt-BR");
                break;
            case "ru":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ru-RU");
                break;
            case "th":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "th-TH");
                break;
            case "tl":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fil-PH");
                break;
            case "tr":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "tr-TR");
                break;
            case "vi":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "vi-VN");
            case "vi-VN":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "vi-VN");
                break;
            case "zh-CN":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                break;
            case "zh-TW":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHT");
                break;
            case "zh-CH":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                break;
            case "es-MX":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-ES");
                break;
            case "es-AR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-AR");
                break;
            case "zh-CHT":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHT");
                break;
            case "es-ES":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-ES");
                break;
            case "ar-EG":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ar-EG"); //done
                break;

            case "ar-MA":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ar-MA"); //done
                break;

            case "de-DE":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "de-DE"); //done
                break;
            case "es-CL":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "es-CL"); //done
                break;
            case "fr-FR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fr-FR"); //done
                break;
            case "in-ID":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "id-ID"); //done
                break;
            case "it-IT":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "it-IT"); //done
                break;
            case "ko-KR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ko-KR"); //done
                break;
            case "ms-MY":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ms-MY"); //done
                break;
            case "pl-PL":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pl-PL"); //done
                break;
            case "pt-BR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "pt-BR"); //done
                break;
            case "ru-RU":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "ru-RU"); //done
                break;
            case "th-TH":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "th-TH"); //done
                break;
            case "fil-PH":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "fil-PH"); //done
                break;
            case "tr-TR":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "tr-TR"); //done
                break;
            case "zh-CHS":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                break;
            case "zh-CNS":
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "zh-CHS");
                break;
            default:
                InformatePreferences.setStringPrefrence(mActivity, Constants.PREF_LOCALECODE, "hi-in");
        }
    }
}