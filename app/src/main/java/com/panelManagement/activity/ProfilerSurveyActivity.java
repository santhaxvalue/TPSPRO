package com.panelManagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.panelManagement.database.DBAdapter;
import com.panelManagement.fragment.DynamicSurveyFragment;
import com.panelManagement.fragment.ProfilerFragment;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.listener.OnprofileSurveyListener;
import com.panelManagement.model.PagesModel;
import com.panelManagement.model.PanelistProfileModel;
import com.panelManagement.model.ProfilerModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.BackButtonWidget;
import com.panelManagement.widgets.SettingWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfilerSurveyActivity extends BaseActivity implements OnprofileSurveyListener, OnBackClickListener {

    public static final String KEY_CAMPAIGNID = "keycampignid";
    public static final String KEY_CAMPAIGNAME = "keycampignname";
    public static final String KEY_JSONOBJECT = "jsondata";
    boolean isSurveyFinished;
    List<Fragment> fragments;
    RelativeLayout mainCointainer = null;
    private DBAdapter database;
    private String campaignId;
    private boolean isProfilerCompleted;
    private ArrayList<PagesModel> arrayQuestions;
    private int mpageNumber;
    private LinearLayout linearShowProfiler;
    private FrameLayout progressBar;
    private String mCampaignNameSelected;
    private AsyncTaskRunner asyncQuestioniers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragmentpage);
        mainCointainer = findViewById(R.id.layout_titlebarTop);
        linearShowProfiler = findViewById(R.id.linear_profiler_show);
        linearShowProfiler.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.linear_profilerProgressbar);
        SettingWidget btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setVisibility(View.INVISIBLE);
        BackButtonWidget btn_back = findViewById(R.id.btn_back);
        btn_back.setOnBackClickedListener(this);
        database = DBAdapter.getDBAdapter(this);

        try {
            Intent intent = getIntent();
            String jsonData = "";
            mCampaignNameSelected = intent.getStringExtra(KEY_CAMPAIGNAME);
            campaignId = intent.getStringExtra(KEY_CAMPAIGNID);
            if (intent.hasExtra(KEY_JSONOBJECT)) {
                jsonData = intent.getStringExtra(KEY_JSONOBJECT);
                asyncQuestioniers = new AsyncTaskRunner(new JSONObject(jsonData), this);
                asyncQuestioniers.execute();
            } else {
                linearShowProfiler.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                fragments = getFragments();
                manageFragment(fragments.get(0), true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    private void manageFragment(Fragment newInstanceFragment, boolean fistelement) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fistelement)
                transaction.add(R.id.framelayout_pager, newInstanceFragment);
            else {
                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
                transaction.replace(R.id.framelayout_pager, newInstanceFragment);
            }

            // transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        } catch (Exception e) {

        }
        //changed 26-10-17
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        arrayQuestions = database.getPagesByCampaignID(campaignId);
        for (int i = 0; i < arrayQuestions.size(); i++) {
            fList.add(DynamicSurveyFragment.newInstance(arrayQuestions.get(i), campaignId, i));
        }
        return fList;
    }

    private void postData(String statusID) {
        try {
            if (Utility.isConnectedToInternet(this)) {
                showDialog(true, getString(R.string.dialog_login));
                JSONObject object = new ParseJSonObject(this).getProfilerInputObject(database, campaignId, statusID);
               // runOnUiThread(() -> _getFirstAndLastName(object));
                requestTypePost(Constants.API_SAVEPANELISTPROFILE, object, Constants.REQUESTCODE_SAVEPROFILER);
            } else {
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void _getFirstAndLastName(JSONObject object) {
//        Gson gson = new Gson();
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(object.optJSONArray("PanelistProfilingData").toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Type listtype = new TypeToken<ArrayList<PanelistProfileModel>>() {
//        }.getType();
//        ArrayList<PanelistProfileModel> list = gson.fromJson(jsonArray.toString(), listtype);
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getQuestionId().equalsIgnoreCase("1058")) {
//                firstName = list.get(i).getAnswer();
//                Log.e("firstName", firstName);
//            }
//
//            if (list.get(i).getQuestionId().equalsIgnoreCase("1059")) {
//                lastName = list.get(i).getAnswer();
//
//                Log.e("lastName", lastName);
//
//            }
//        }
//    }

    @Override
    public void onProfilerHandler(ProfilerModels model) {

    }

    @Override
    public void onPageChange(int pageno) {
        mpageNumber = pageno;
        if (pageno < fragments.size()) {
            loadView(pageno);
        }

    }

    private void loadView(int pageno) {

        if (!isProfilerCompleted) {
            String pageName = arrayQuestions.get(pageno).getName();
            if (!TextUtils.isEmpty(pageName)) {
                if (pageName.equalsIgnoreCase("Terminated") || pageName.equalsIgnoreCase("Terminate")) {
                    mainCointainer.setBackgroundResource(R.color.aqua_dark);
                    postData(Constants.EXIT_TERMINATEPROFILER);
                } else if (pageName.equalsIgnoreCase("ThankYou") || pageName.equalsIgnoreCase("Thank You")) {
                    mainCointainer.setBackgroundResource(R.color.aqua_dark);
                    postData(Constants.EXIT_THANKYOU);
                }

            }
        }
        manageFragment(fragments.get(pageno), false);

    }

    @Override
    public void onBackPressed() {

        onBackButtonPressed();
        super.onBackPressed();
    }

    @Override
    public void onBackButtonPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        if (asyncQuestioniers != null) {
            asyncQuestioniers.cancel(true);
        }
        if (mpageNumber < 1) {
            finish();
        } else if (!isProfilerCompleted) {
            exitAppDialog();
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void checkSurveyCompleted(boolean isTrue) {
        this.isProfilerCompleted = isTrue;
    }

    @Override
    public void fragmentUpdate(int position) {
        ((DynamicSurveyFragment)fragments.get(position)).setIsActive(true);
    }

    public void exitAppDialog() {
        if (Utility.isConnectedToInternet(this)) {
            JSONObject object = null;
            try {
                object = new ParseJSonObject(ProfilerSurveyActivity.this).getProfilerInputObject(database, campaignId, Constants.EXITPROFILERINBETWEEN);
                requestTypePost(Constants.API_SAVEPANELISTPROFILE, object, Constants.REQUESTCODE_SAVEPROFILER);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                        if (object.getBoolean("Status")) {
                            database.deleteAllRecords();
                            isProfilerCompleted = true;
                            if (HomeActivity.profileName != null && ProfilerFragment.isGeneralSurvey) {
                                ProfilerFragment.isGeneralSurvey = false;
                                HomeActivity.profileName.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_FIRSTNAME),
                                        InformatePreferences.getStringPrefrence(this, Constants.PREF_LASTNAME)));

                            }
                            HomeActivity.phoneNum.setText(String.format("+%s - %s", InformatePreferences.getStringPrefrence(this, Constants.PREF_ISDCODE), InformatePreferences.getStringPrefrence(this, Constants.PREF_MOBILENUMBER)));

//                            InformatePreferences.setStringPrefrence(this, Constants.PREF_FIRSTNAME, firstName);
//                            InformatePreferences.setStringPrefrence(this, Constants.PREF_LASTNAME, lastName);
                        } /*else
                            showErrorAlert("success", object.optString("Message"));*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onUIUpdate(int color) {

    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private JSONObject object;
        private Context mContext;

        public AsyncTaskRunner(JSONObject res, Context context) {
            this.object = res;
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                JSONArray nofopages = object.getJSONObject("xcampaign").getJSONArray("pages");
                for (int i = 0; i < nofopages.length(); i++) {
                    JSONObject pagesObject = nofopages.getJSONObject(i);
                    new ParseJSonObject(mContext).parseProfilerSurveyByPage(pagesObject, campaignId, i, mCampaignNameSelected);
                }
                return "completed";

            } catch (JSONException e) {

            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            asyncQuestioniers = null;
            if (result != null) {
                linearShowProfiler.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                fragments = getFragments();
                manageFragment(fragments.get(0), true);
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

}
