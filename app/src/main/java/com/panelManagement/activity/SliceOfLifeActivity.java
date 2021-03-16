package com.panelManagement.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.panelManagement.database.DBAdapter;
import com.panelManagement.fragment.SliceSurveyFragment;
import com.panelManagement.model.PagesModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SliceOfLifeActivity extends BaseActivity {

    List<Fragment> fragments;
    RelativeLayout mainCointainer = null;
    private DBAdapter database;
    private String campaignId;
    private boolean isProfilerCompleted;
    private ArrayList<PagesModel> arrayQuestions;
    private int mpageNumber;
    private LinearLayout linearShowProfiler;
    private FrameLayout progressBar;
    RelativeLayout relativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliceview);
        mainCointainer = (RelativeLayout) findViewById(R.id.layout_titlebarTop);
        linearShowProfiler = (LinearLayout) findViewById(R.id.linear_profiler_show);
        linearShowProfiler.setVisibility(View.INVISIBLE);
        progressBar = (FrameLayout) findViewById(R.id.linear_profilerProgressbar);
        database = DBAdapter.getDBAdapter(this);
        linearShowProfiler.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        fragments = getFragments();
        //manageFragment(fragments.get(0), true);

        relativeLayout = findViewById(R.id.slice_view);
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);
    }

    private void manageFragment(Fragment newInstanceFragment, boolean fistelement) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fistelement)
            transaction.add(R.id.framelayout_pager, newInstanceFragment);
        else {
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up);
            transaction.replace(R.id.framelayout_pager, newInstanceFragment);
        }

        // transaction.addToBackStack(null);
        transaction.commit();
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        arrayQuestions = database.getPagesByCampaignID(campaignId);
        for (int i = 0; i < arrayQuestions.size(); i++) {
            fList.add(SliceSurveyFragment.newInstance(arrayQuestions.get(i), campaignId, i));
        }
        return fList;
    }

    private void postData(String statusID) {
        try {
            if (Utility.isConnectedToInternet(this)) {
                showDialog(true, getString(R.string.dialog_login));
                JSONObject object = new ParseJSonObject(this).getProfilerInputObject(database, campaignId, statusID);
                requestTypePost(Constants.API_SAVEPANELISTPROFILE, object, Constants.REQUESTCODE_SAVEPROFILER);
            } else {
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public void exitAppDialog() {
        if (Utility.isConnectedToInternet(this)) {
            JSONObject object = null;
            try {
                object = new ParseJSonObject(SliceOfLifeActivity.this).getProfilerInputObject(database, campaignId,
                        Constants.EXITPROFILERINBETWEEN);
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
                        } else
                            showErrorAlert("success", object.getString("Message"));
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

    public class MyAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;

        public MyAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
    }


}
