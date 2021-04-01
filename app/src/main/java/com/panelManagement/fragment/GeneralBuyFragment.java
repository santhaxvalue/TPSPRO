package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.adapter.RedeemPointsListAdapter;
import com.panelManagement.model.GeneralRedeemModels;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.MultiViewAdapter;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static com.panelManagement.activity.HomeActivity.GENERALBUYFRAGMENT;

public class GeneralBuyFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, RedeemPointsListAdapter.onRedeemPointsClickListener {

    MultiViewAdapter multiViewAdapter;
    ArrayList<GeneralRedeemModels> redeemarray = new ArrayList<>();
    RewardPointsModels rewardPointsData;
    String rewardPoints;
    ViewPager carouselPicker;
    RelativeLayout relativeLayout;
    TextView tvPointAvailable, tvPointReview;
    private String rewardPrice = "0";
    private GeneralRedeemModels mSelectedValue;
    private Dialog payTmDialog;
    private TabLayout tl_dots;
    PagerContainer pagerContainer;
    private RedeemPointsListAdapter mAdapter;
    private RecyclerView mRedeemRecycler;
    private EditText mRedeemPointsEdt;

    RewardPointsModels rewardsPointsData = null;




    public GeneralBuyFragment() {
    }

    public static GeneralBuyFragment newInstance(RewardPointsModels rewardPointsModels) {
        GeneralBuyFragment fragment = new GeneralBuyFragment();
        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, rewardPointsModels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rewardPointsData = (RewardPointsModels) getArguments().getSerializable(RewardPointsFragment.KEYLIST);
            if (rewardPointsData != null)
                //rewardPoints = rewardPointsData.getAvailablePoints();
                rewardPoints = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
        }
    }

    private void setRedeemPointsBtn() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRedeemRecycler.setLayoutManager(layoutManager);
        mAdapter = new RedeemPointsListAdapter(getContext(), mSelectedValue.getDenominations(), this);
        mRedeemRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_redemption_general, container, false);

//        ViewCompat.setElevation(view, 8.0f);
        TextView btn_submit = view.findViewById(R.id.btn_submit);
        carouselPicker = view.findViewById(R.id.carasol);
        carouselPicker.addOnPageChangeListener(this);
        pagerContainer = view.findViewById(R.id.pager_container);

        mRedeemRecycler = view.findViewById(R.id.ll_points_layout);
        mRedeemPointsEdt = view.findViewById(R.id.redeem_points);
        mRedeemPointsEdt.addTextChangedListener(filterTextWatcher);

        tl_dots = view.findViewById(R.id.tl_dots);
        tl_dots.setupWithViewPager(carouselPicker);

        tvPointAvailable = getActivity().findViewById(R.id.txt_points_redeemed_available);
        tvPointReview = getActivity().findViewById(R.id.txt_points_inreview_available);

        btn_submit.setOnClickListener(this);

        try {
            if (Utility.isConnectedToInternet(getActivity())) {
                showDialog(true, getString(R.string.dialog_login));
                requestTypePost(Constants.API_GENERALREWARDS, new ParseJSonObject(getActivity()).getSessionObject(),
                        Constants.REQUESTCODE_GENERALREWARDS);
            } else {
                showErrorAlert(" ", getString(R.string.msg_low_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        relativeLayout = view.findViewById(R.id.general_redeeem_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && String.valueOf(s).contains("0")) {
                mRedeemPointsEdt.setText("");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:

               Log.d("mSelectedValue:","mSelectedValue:"+mSelectedValue);
                Log.d("mSelectedValue:","mSelectedValue:"+mSelectedValue.isInstantRedemptionEnabled());

                try {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        if (mSelectedValue != null) {

                            //new code
                            if(mSelectedValue.getDenominations().get(0).equals("0")) {
                                mRedeemRecycler.setVisibility(View.INVISIBLE);
                            }else {
                                //new code

                                if (mSelectedValue.isInstantRedemptionEnabled()) {
                                    if (!(TextUtils.isEmpty(mRedeemPointsEdt.getText().toString()))) {
                                        int enteredRedeemPoints = Integer.parseInt(String.valueOf(mRedeemPointsEdt.getText()));
                                        if (enteredRedeemPoints > 0 && enteredRedeemPoints <= Integer.parseInt(rewardPoints)) {
                                            rewardPrice = mRedeemPointsEdt.getText().toString();
                                            proceedToRedeem();
                                        } else {
                                            showErrorAlert("", getString(R.string.error_msg_for_invalid_redeem_points));
                                        }
                                    } else {
                                        showErrorAlert("", getString(R.string.error_msg_for_invalid_redeem_points));
                                    }
                                } else {
                                    proceedToRedeem();
                                }
                            }
                        } else {
                            showErrorAlert("", getString(R.string.msg_select_any_coupoun));
                        }
                    } else {
                        showErrorAlert(" ", getString(R.string.msg_low_conn));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    private void proceedToRedeem() {
        if (mSelectedValue.getPartnerName().toLowerCase().contains("paytm") || mSelectedValue.getPartnerName().toLowerCase().contains("paypal")) {
            validatePaytm();
        } else {
            //old code
//            callGeneralRedeemApi();
            //new code
            showddAlertDialog(context,"voucherCode","voucherCodeAvailable");
        }
    }

    private void callGeneralRedeemApi() {
        showDialog(true, getString(R.string.dialog_login));
        if (mSelectedValue.isEdenRed()){

            requestTypePost(Constants.API_GENERALREDEEM, new ParseJSonObject(getActivity()).getGeneralRedeemObject1(Integer.parseInt(rewardPrice),
                    Integer.parseInt(mSelectedValue.getId()), mSelectedValue.getPartnerName(), mSelectedValue.isEdenRed()), Constants.REQUESTCODE_GENERALREDEEM);

        }else {

            requestTypePost(Constants.API_GENERALREDEEM, new ParseJSonObject(getActivity()).getGeneralRedeemObject(Integer.parseInt(rewardPrice),
                    Integer.parseInt(mSelectedValue.getId()), mSelectedValue.getPartnerName()), Constants.REQUESTCODE_GENERALREDEEM);
        }
    }

    private void validatePaytm() {
        showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.API_GETPAYTMID_AND_PAYPAL_ID, new ParseJSonObject(context)
                .getPaytmId(InformatePreferences.getStringPrefrence(context, Constants.PREF_ID)), Constants.REQUEST_VALIDATE_PAYTM);
    }

    @Override
    public void vLayout(String res, int requestcode) {



        switch (requestcode) {

            case Constants.REQUESTCODE_GENERALREWARDS:

                try {
                    Log.e("GeneralRewardsNew",res);
                    redeemarray = new ParseJSonObject(getActivity()).getRedeemData(new JSONObject(res));
                    mSelectedValue = redeemarray.get(0);

                    lap();

                    //new code
                    if(mSelectedValue.getDenominations().get(0).equals("0")) {
                        mRedeemRecycler.setVisibility(View.INVISIBLE);
                    }else {
                        //new code


                        //Take the reward from the editText based on the condition
                        if (mSelectedValue.isInstantRedemptionEnabled()) {
                            mRedeemPointsEdt.setVisibility(View.VISIBLE);
                            mRedeemRecycler.setVisibility(View.GONE);
                        } else {
                            mRedeemPointsEdt.setVisibility(View.GONE);
                            mRedeemRecycler.setVisibility(View.VISIBLE);

                            rewardPrice = mSelectedValue.getDenominations().get(0);
                            setRedeemPointsBtn();
                        }
                        //new code
                    }
                    //new code

                    //multiViewAdapter = new MultiViewAdapter(getChildFragmentManager(), getContext(), redeemarray);
                    //carouselPicker.setAdapter(multiViewAdapter);
                    //carouselPicker.setPageMargin(getResources().getDimensionPixelSize(R.dimen.dp660));
                    //carouselPicker.setCurrentItem(1, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_GENERALREDEEM:
                try {
                    Log.e("GENERALREDEEM",res);
                    JSONObject object = new JSONObject(res);

                    if (object.getBoolean("Status")) {

                        Log.e("SizeList", redeemarray.size()+"");

                        if (redeemarray.get(0).isEdenRed() == false || redeemarray.get(0).isEdenRed() == true){
                            // static value for testing  boolean istrue=true;
                            //if (istrue){
                            // String voucherCode="new Voucher code Z1R5G5C6FF88W789Ff";
                            String voucherCode=object.get("voucherCode").toString();
                            Log.d("voucherCode:","voucherCode:"+voucherCode);
                            String voucherCodeNew = object.get("Vpassword").toString();

                            // String voucherCode=object.get("voucherCode").toString();

                            //old code
//                            showddAlertDialog(context,voucherCode,"voucherCodeAvailable");

                            //latest old code
//                            if(voucherCode.equals("")){
//                                String VoucherLink=object.get("VoucherLink").toString();
//                                showddAlertDialog(context,VoucherLink,"voucherLinkAvailable");
//                            }else {
//                                showddAlertDialog(context,voucherCode,"voucherCodeAvailable");
//                            }
                            //latest old code

                            // String voucherCode=object.get("voucherCode").toString();
                            //old code

                            //new code

                            if(voucherCode.equals("") || voucherCode.equals("null")){
                                String VoucherLink=object.get("VoucherLink").toString();
                                showVoucherAlertDialog(context,VoucherLink,"voucherLinkAvailable",voucherCodeNew);
                            }else {
                                showVoucherAlertDialog(context,voucherCode,"voucherCodeAvailable",voucherCodeNew);
                            }

                                    //new code


                        }else {

                            showCustomAlertDialog(context, rewardPrice);
                            for (int i = 0; i < redeemarray.size(); i++) {
                                GeneralRedeemModels reversedData = redeemarray.get(i);
                                if (reversedData.isCheck()) {
                                    reversedData.setCheck(false);
                                }
                            }
                            mSelectedValue = null;
                            rewardPrice = "";
                        }


                    } else {

                        if (object.getString("IsProfiler").equals("true")) {
                            showErrorAlert("error", object.getString("Message"));
                            launchProfilePage();
                        }else {
                            showErrorAlert("", object.getString("Message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUEST_SAVE_PAYTM:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        showDialog(true, getString(R.string.dialog_login));
                        if (payTmDialog != null && payTmDialog.isShowing()) payTmDialog.dismiss();
                        if (mSelectedValue.isEdenRed()) {

                            requestTypePost(Constants.API_GENERALREDEEM, new ParseJSonObject(getActivity()).getGeneralRedeemObject1(Integer.parseInt(rewardPrice),
                                    Integer.parseInt(mSelectedValue.getId()), mSelectedValue.getPartnerName(), mSelectedValue.isEdenRed()), Constants.REQUESTCODE_GENERALREDEEM);

                        } else {
                            requestTypePost(Constants.API_GENERALREDEEM, new ParseJSonObject(getActivity()).getGeneralRedeemObject(Integer.parseInt(rewardPrice),
                                    Integer.parseInt(mSelectedValue.getId()), mSelectedValue.getPartnerName()), Constants.REQUESTCODE_GENERALREDEEM);
                        }
                    } else {
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUEST_VALIDATE_PAYTM:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {

                        String paytmID = object.optString("PAYTMId");
                        String paypalEmailId = object.optString("PayPalId");
                        showPaytmAlertDialog(context, paypalEmailId, paytmID);
                    } else {
                        showErrorAlert(" ", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUEST_AVAILABLE_POINTS:
//                RewardPointsModels rewardsPointsData = null;
//                try {
//                    Log.e("GetIncentGeneralBuy",res.toString());
//                    rewardsPointsData = new ParseJSonObject(getContext()).getRewardsPoints(new JSONObject(res));
//                    JSONObject jsonObject = new JSONObject(res);
//                    InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_AVAILABLEPOINTS_, rewardsPointsData.getAvailablePoints());
////                SurveyFloating.txt_points_redeemed.setText(rewardsPointsData.getAvailablePoints());
//                    tvPointAvailable.setText(rewardsPointsData.getAvailablePoints());
//                    tvPointReview.setText(rewardsPointsData.getPointsReview());
//
//                    int threshold = jsonObject.optInt("ThresholdPoints");
//                    int availablePoints = jsonObject.optInt("AvailablePoints");
//
//                    if (availablePoints >= threshold) {
//                        FragmentManager fm = getActivity().getSupportFragmentManager();
//                        fm.popBackStack(HomeActivity.REDEEMFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    } else {
//                        FragmentManager fm = getActivity().getSupportFragmentManager();
//                        fm.popBackStack(HomeActivity.REWARDSFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    }
//                    //getFragmentManager().popBackStack();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }



                try {
                    Log.d("GeneralVpassword",res);
                    rewardsPointsData = new ParseJSonObject(getContext()).getRewardsPoints(new JSONObject(res));
                }catch (Exception e){
                    e.printStackTrace();
                }

                showRedemptionHistory(rewardsPointsData);

                break;

            case Constants.GETGENERALREWARDSNEW_CODE:
                Log.e("GETGENERALREWARDSNEWAPi",res);
//                showRedemptionHistory();

                requestTypePost(Constants.GETINCENTIVEDETAILSMOBILE, new ParseJSonObject(getActivity()).getSessionObject(), Constants.REQUEST_AVAILABLE_POINTS);

                break;

            default:
                break;
        }
    }

    private void showRedemptionHistory(RewardPointsModels rewardsPointsData) {
        if (rewardsPointsData != null) {
            //   ((HomeActivity) getActivity())._secondaryFragment(PointsRedeemed.newInstance(), HomeActivity.REWARDSFRAGMENTKEY);
            Fragment fragment = RedemptionHistoryFragment.newInstance(rewardsPointsData);
            getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REWARDSFRAGMENTKEY).commit();
        } else {
            showErrorAlert("", "Please earn points by attending surveys.");
            Log.e("redeemption history", "Empty");
        }
    }


    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mSelectedValue = redeemarray.get(position);
        mRedeemPointsEdt.setText("");

        //new code
        if(mSelectedValue.getDenominations().get(0).equals("0")) {
            mRedeemRecycler.setVisibility(View.INVISIBLE);
        }else {
            //new code

            //Take the reward from the editText based on the condition
            if (mSelectedValue.isInstantRedemptionEnabled()) {
                mRedeemPointsEdt.setVisibility(View.VISIBLE);
                mRedeemRecycler.setVisibility(View.GONE);
            } else {
                mRedeemRecycler.setVisibility(View.VISIBLE);
                mRedeemPointsEdt.setVisibility(View.GONE);
                rewardPrice = mSelectedValue.getDenominations().get(0);
                setRedeemPointsBtn();
                if (mAdapter != null) {
                    mAdapter.updateListItems(mSelectedValue.getDenominations());
                }
            }
            //new code
        }
        //new code
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showCustomAlertDialog(Context context, String count) {
        TextView rewards;
        ImageView closeDialog;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_general_reward);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        rewards = dialog.findViewById(R.id.tv_reward_point_general);
        rewards.setText(" " + count);
        closeDialog = dialog.findViewById(R.id.close_dialog_general);

        closeDialog.setOnClickListener((View v) -> {
            dialog.dismiss();
            requestUpdatePoints();
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            fm.popBackStack(HomeActivity.REDEEMFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
        dialog.show();
    }


    public void showVoucherAlertDialog(Context context,String voucherCode,String voucherStatus,String voucherCodeNew){

        TextView tv_voucherCode,tv_getvoucherLink,tv_okButton,tv_voucherCodeNew,tv_voucherCodeNewText;
        ImageView close_voucher_dialog_general;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_your_voucher);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        tv_voucherCode = dialog.findViewById(R.id.tv_getvoucherCode);

        tv_voucherCodeNew = dialog.findViewById(R.id.tv_getvoucherCodeNew);

        tv_voucherCodeNewText = dialog.findViewById(R.id.tv_getvoucherCodeNewText);

        //new code
        tv_getvoucherLink  = dialog.findViewById(R.id.tv_getvoucherLink);
                //new code

        tv_okButton = dialog.findViewById(R.id.tv_btnVoucherOk);
        //old code
//        tv_voucherCode.setText(voucherCode);
        //old code

        //new code

        tv_voucherCodeNew.setText(voucherCodeNew);

        Log.d("voucherpinis:","voucherpinis:"+voucherCodeNew);

        if(voucherStatus.equals("voucherCodeAvailable")){
            tv_voucherCode.setText(voucherCode);
            tv_getvoucherLink.setVisibility(View.GONE);

            if(voucherCodeNew.equals("") || voucherCodeNew == null){
                tv_voucherCodeNew.setVisibility(View.GONE);
                tv_voucherCodeNewText.setVisibility(View.GONE);
            }else {
                tv_voucherCodeNew.setVisibility(View.VISIBLE);
                tv_voucherCodeNewText.setVisibility(View.VISIBLE);
            }

        }else if(voucherStatus.equals("voucherLinkAvailable")){
            tv_voucherCode.setVisibility(View.GONE);
            tv_getvoucherLink.setVisibility(View.VISIBLE);
            tv_getvoucherLink.setText(voucherCode);

            if(voucherCodeNew.equals("") || voucherCodeNew == null){
                tv_voucherCodeNew.setVisibility(View.GONE);
                tv_voucherCodeNewText.setVisibility(View.GONE);
            }else {
                tv_voucherCodeNew.setVisibility(View.VISIBLE);
                tv_voucherCodeNewText.setVisibility(View.VISIBLE);
            }

        }


        close_voucher_dialog_general = dialog.findViewById(R.id.close_voucher_dialog_general);

        close_voucher_dialog_general.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        tv_okButton.setOnClickListener(v -> {
            dialog.dismiss();
            // show in another pop up
            //  callGetGeneralRewardsnewAPI();
            //old code
//            openWarningDialog(context);
            //New code
            callGetGeneralRewardsnewAPI();
        });


        dialog.show();

    }

    public void showddAlertDialog(Context context,String voucherCode,String voucherStatus){

        TextView tv_voucherCode,tv_okButton;
        ImageView close_voucher_dialog_general;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_d);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        tv_voucherCode = dialog.findViewById(R.id.tv_getvoucherCode);
        tv_okButton = dialog.findViewById(R.id.tv_btnVoucherOk);
        tv_voucherCode.setText(voucherCode);


        close_voucher_dialog_general = dialog.findViewById(R.id.close_voucher_dialog_general);

        close_voucher_dialog_general.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        tv_okButton.setOnClickListener(v -> {
            dialog.dismiss();
            //old code
//            showVoucherAlertDialog(context,voucherCode);

            //old code

            // show in another pop up
//              callGetGeneralRewardsnewAPI();
            // openWarningDialog(context);

            //new Code
            callGeneralRedeemApi();
            //new code

        });


        dialog.show();

    }

    private void openWarningDialog(Context context) {

        TextView tv_okButton;
        ImageView close_voucher_dialog_general;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_redemption_warning);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        tv_okButton = dialog.findViewById(R.id.tv_btnWarning);
        close_voucher_dialog_general = dialog.findViewById(R.id.close_voucherWarning_dialog_general);

        close_voucher_dialog_general.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        tv_okButton.setOnClickListener(v -> {
            dialog.dismiss();

            callGetGeneralRewardsnewAPI();

        });


        dialog.show();


    }


    public void lap() {
        pagerContainer.setOverlapEnabled(true);
        ViewPager viewPager = pagerContainer.getViewPager();
        MultiViewAdapter pagerAdapter = new MultiViewAdapter(getChildFragmentManager(), context, redeemarray);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        new CoverFlow.Builder().with(viewPager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.dpM90))
                .build();
        pagerContainer.onPageSelected(0);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                if(fragment.getView() != null) {
                    ViewCompat.setElevation(fragment.getView(), 8.0f);
                }
            }
        });
    }


    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    public void showPaytmAlertDialog(Context context, String paypal, String paytm) {
        TextView rewards, paytm_head;
        EditText paytmId;
        ImageView closeDialog;
        Button buttonPaytmSubmit;
        payTmDialog = new Dialog(context, R.style.Theme_Dialog);
        payTmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payTmDialog.setCancelable(false);
        payTmDialog.setContentView(R.layout.paytm_dialog);
        payTmDialog.setCanceledOnTouchOutside(false);
        Window window = payTmDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        payTmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        paytm_head = payTmDialog.findViewById(R.id.paytm_head);

        paytmId = payTmDialog.findViewById(R.id.paytm_email_id);
        payTmDialog.show();
        if (mSelectedValue != null) {

            if (mSelectedValue.getPartnerName().toLowerCase().contains("paytm")) {
                paytm_head.setText(R.string.enter_paytm_id);
                paytmId.setText(paytm);

            } else if (mSelectedValue.getPartnerName().toLowerCase().contains("paypal")) {
                paytm_head.setText(R.string.enter_paypal_id);
                paytmId.setText(paypal);

            }
        }


        buttonPaytmSubmit = payTmDialog.findViewById(R.id.btn_submit_paytm);
        buttonPaytmSubmit.setOnClickListener((View v) -> {
            if (!TextUtils.isEmpty(paytmId.getText().toString())) {
                showDialog(true, getString(R.string.dialog_login));

                String paytmID = "", payPalid = "",
                        panellistid = InformatePreferences.getStringPrefrence(context, Constants.PREF_ID);
                if (mSelectedValue != null) {


                    if (mSelectedValue.getPartnerName().toLowerCase().contains("paytm")) {
                        paytmID = paytmId.getText().toString();
                        payPalid = paypal;
                    } else if (mSelectedValue.getPartnerName().toLowerCase().contains("paypal")) {
                        paytmID = paytm;
                        payPalid = paytmId.getText().toString();
                    }
                }

                requestTypePost(Constants.API_SAVEPAYTMID_AND_PAYPAL, new ParseJSonObject(context)
                        .savePaytmId(panellistid, paytmID, payPalid), Constants.REQUEST_SAVE_PAYTM);
                if (payTmDialog != null) {
                    payTmDialog.dismiss();
                }
            } else {
                paytmId.setError("Please enter your Paytm Id");
                paytmId.requestFocus();
            }
        });
        // rewards = payTmDialog.findViewById(R.id.tv_reward_point_general);
        closeDialog = payTmDialog.findViewById(R.id.close_dialog_paytm);

        closeDialog.setOnClickListener((View v) -> {
            payTmDialog.dismiss();
        });

    }


    @Override
    public void onClickRedeemPoints(String points, int currentPosition) {
        rewardPrice = points;
        if (Float.parseFloat(rewardPoints) >= Float.parseFloat(points)) {
            if (mAdapter != null) {
                mAdapter.updateSelecedValue(currentPosition);
            }
        } else {
            int bal = (int) (Float.parseFloat(points) - Float.parseFloat(rewardPoints));
            showErrorAlert("", String.format(getString(R.string.need_more_points), bal, mSelectedValue.getPartnerName()));
        }
    }

    private void launchProfilePage() {
        ProfilerFragment nextFrag = new ProfilerFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container_fragment, nextFrag)
                .addToBackStack(GENERALBUYFRAGMENT)
                .commit();
    }

}
