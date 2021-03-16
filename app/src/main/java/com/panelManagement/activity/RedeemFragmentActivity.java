package com.panelManagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.panelManagement.fragment.AlertChangeLanguageFragment;
import com.panelManagement.fragment.MobileVerificationFragment;
import com.panelManagement.fragment.ProfilerFragment;
import com.panelManagement.fragment.RedeemFragment;
import com.panelManagement.fragment.RedemptionHistoryFragment;
import com.panelManagement.fragment.RewardHistoryFragment;
import com.panelManagement.listener.OnBackClickListener;
import com.panelManagement.listener.OnSettingClickListener;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.widgets.BackButtonWidget;
import com.panelManagement.widgets.SettingWidget;

public class RedeemFragmentActivity extends FragmentActivity implements OnBackClickListener, OnSettingClickListener {

    public static final int REWARDHISTORY = 1;
    public static final int REDEMPTIONHISTORY = 2;
    public static final int REDEEM = 3;
    public static final int THANKS = 4;
    public static final String KEYREWARDS = "rewards";
    public static final String KEYLIST = "rewardslist";
    public static final String KEYREWARDSPOINTS = "rewardspoints";
    public static final Float KEYMINIMUMPOINTS = 3000.0f;
    Bundle extra;
    public static RelativeLayout mainCointenar = null;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_redeem);

        InformatePreferences.getStringPrefrence(this, Constants.ISLANGUAGECHANGECALLED).equalsIgnoreCase("false");
        mainCointenar = findViewById(R.id.layout_titlebarTop);
        mainCointenar.setVisibility(View.VISIBLE);
        SettingWidget btn_setting = findViewById(R.id.btn_setting);
        btn_setting.setOnSettingClickedListener(this);
        BackButtonWidget btn_back = findViewById(R.id.btn_back);
        btn_back.setOnBackClickedListener(this);

        if (findViewById(R.id.ic_container_redeem) != null) {
            if (savedInstanceState == null) {
                Fragment mFragment = getSelectedFragment();
                overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.ic_container_redeem, mFragment, mFragment.toString());
                transaction.commit();
            }
        }

        relativeLayout = findViewById(R.id.picture_main_layout);
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);
    }

    private Fragment getSelectedFragment() {
        Intent intent = getIntent();
        int value = intent.getIntExtra(KEYREWARDS, REWARDHISTORY);
        extra = getIntent().getExtras();
        String rewardPoints = null;
        if (value == THANKS) {
            //mainCointenar.setBackgroundResource(R.color.aqua_dark);
        } else {
            mainCointenar.setBackgroundResource(R.color.white);
        }
        switch (value) {
            case REWARDHISTORY:
                return new RewardHistoryFragment();
            case REDEMPTIONHISTORY:
                return new RedemptionHistoryFragment();
            case REDEEM:

                if (InformatePreferences.getBoolean(this, Constants.PREF_MOBILENUMBERVERIFIED, false)) {
                    if (extra != null) {
                        rewardPoints = extra.getString(RedeemFragmentActivity.KEYREWARDSPOINTS);
                        // jayesh - removed min 3000 points constraint for general rewards and made sweepstake fragment default fragment
                        /*   if (Float.parseFloat(rewardPoints) < RedeemFragmentActivity.KEYMINIMUMPOINTS) {*/
                        return new RedeemFragment();
                       /* } else {
                            return new RedeemFragment(RedeemFragmentActivity.TABGENERAL, rewardPoints);
                        }*/
                    }

                } else
                    return new MobileVerificationFragment(extra);
        }
        return new RewardHistoryFragment();
    }

    @Override
    public void onSettingCall() {
        /*DialogFragment fragment = new AlertSettingScreenFragment();
        fragment.show(getSupportFragmentManager(), "");
*/
    }

    @Override
    public void onBackButtonPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack("my_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {

            AlertChangeLanguageFragment myFrag = (AlertChangeLanguageFragment) fragmentManager.findFragmentById(R.id.tabSettingsFragment);
            if (myFrag != null) {
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_up);
                transaction.hide(myFrag);
                transaction.remove(myFrag);
                transaction.commit();
            }
            Intent intent = new Intent();
            setResult(ProfilerFragment.RESULT_PROFILER, intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackButtonPressed();
            return super.onKeyDown(keyCode, event);
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
