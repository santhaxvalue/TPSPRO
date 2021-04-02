package com.panelManagement.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;


public class RedeemFragment extends BaseFragment implements OnClickListener {

    JSONObject objectSweepstake = null;
    String rewardPoints;
    String offerImageURL;
    RelativeLayout relativeLayout;
    RewardPointsModels rewardsPointsData;
    TextView tvPointAvailable;
    private ImageView offerImage;
    int minimumPOints;
    public static int availablePoints = 0;
    public static int minimumGeneralPoints = 3000;//Minimum points, if IsRedeemInstanly is false

    public RedeemFragment() {
        super();
    }

    public static RedeemFragment newInstance(RewardPointsModels pointsModels, int minipoints) {

        Bundle args = new Bundle();
        args.putSerializable(RewardPointsFragment.KEYLIST, pointsModels);
        RedeemFragment fragment = new RedeemFragment();
        args.putInt("MinimumPoints", minipoints);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rewardsPointsData = (RewardPointsModels) getArguments().getSerializable(RewardPointsFragment.KEYLIST);
            availablePoints = getArguments().getInt("MinimumPoints");
            rewardPoints = rewardsPointsData.getAvailablePoints();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_redeem, container, false);

        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);

        offerImage = view.findViewById(R.id.offer_image);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);

        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);

        tvPointAvailable = getActivity().findViewById(R.id.txt_points_redeemed_available);
        String minipoints = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
        double minimum = Double.parseDouble(minipoints);
        int minimumPOints = Integer.parseInt(new DecimalFormat("#").format(minimum));

        tvPointAvailable.setText(minimumPOints + "");
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        Utility.setMargins(context, layout, Utility.getDp(context, 40));
        HomeActivity.setRewardsBackground();
        if (rewardsPointsData != null)

            try {
                if (Utility.isConnectedToInternet(getActivity())) {

                    showDialog(true, getString(R.string.dialog_login));
                    requestTypePost(Constants.API_GENERALSWEEPSTAKES, new ParseJSonObject(getActivity()).getSessionObject(), Constants.REQUEST_REDEEMFRAGMENT);

                } else {

                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        TextView sweepBuy = view.findViewById(R.id.sweepstakes_buy);
        TextView generalBuy = view.findViewById(R.id.general_buy);

        sweepBuy.setOnClickListener(this);
        generalBuy.setOnClickListener(this);

        relativeLayout = view.findViewById(R.id.redeeem_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (v.getId()) {

            case R.id.sweepstakes_buy:

                String minipoints = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);
                double minimum = Double.parseDouble(minipoints);
                int minimumPOints = Integer.parseInt(new DecimalFormat("#").format(minimum));


                if (minimumPOints >= RewardPointsFragment.minimumRedeemPoints) {
                    Fragment mFragment = SweepstakeFragment.newInstance(objectSweepstake);
                    transaction.replace(R.id.main_container_fragment, mFragment).addToBackStack(HomeActivity.REDEEMFRAGMENTKEY);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                } else {
                    int redeemValue = (int) (RewardPointsFragment.minimumRedeemPoints - minimumPOints);
                    showErrorAlert("", String.format(getResources().getString(R.string.alert_dialog_txt_part3), redeemValue));
                }
                break;

            case R.id.general_buy:
                String minipoint_aftergenralbuy = InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_AVAILABLEPOINTS_);

                Log.d("strvalue1:","strvalue1:"+minipoint_aftergenralbuy);

                //new code
                if(minipoint_aftergenralbuy.contains(".")) {
                   String strvalue = minipoint_aftergenralbuy.substring(0,minipoint_aftergenralbuy.indexOf("."));
                    minimumPOints = Integer.parseInt(strvalue);

                    Log.d("strvalue11:", "strvalue11:" + strvalue);
                }else {

                    //old code
                    minimumPOints = Integer.parseInt(minipoint_aftergenralbuy);
                }

                if((InformatePreferences.getBoolean(context, Constants.IS_REDEEM_INSTANTLY,false)))
                    minimumGeneralPoints = 500;
                else {
                    minimumGeneralPoints = 3000;
//                    minimumGeneralPoints = 100;
                }

                if (minimumPOints >= minimumGeneralPoints) {
                    //old code
//                if (100000 >= minimumGeneralPoints) {
                    //old code
                    //transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);
                    Fragment fragment = GeneralBuyFragment.newInstance(rewardsPointsData);
                    transaction.replace(R.id.main_container_fragment, fragment).addToBackStack(HomeActivity.REDEEMFRAGMENTKEY);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                } else {
                    int redeemValue = (int) (minimumGeneralPoints - minimumPOints);
                    final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_general_reward_need_points);
                    dialog.setCanceledOnTouchOutside(true);
                    _dimWindowOutsideDialog(dialog);
                    TextView offertext = dialog.findViewById(R.id.tv_offer);
                    TextView participate = dialog.findViewById(R.id.participate);
                    ImageView iv_close_error = dialog.findViewById(R.id.close_dialog_general);
                    ImageView iv_offer = dialog.findViewById(R.id.offer_dilog_promotion);
                    offertext.setText(String.format(getResources().getString(R.string.alert_dialog_txt_part1), redeemValue));

                    iv_close_error.setOnClickListener(view -> dialog.dismiss());

                    Log.d("imgurl:","imagurl:"+offerImageURL);

                    Picasso.get()
                            .load(offerImageURL) // jayesh; encoded the image_layout url ***
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//                            .placeholder(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholdernew)
                            .into(iv_offer);

                    participate.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (Float.parseFloat(rewardPoints) >= RewardPointsFragment.minimumRedeemPoints) {
                                //transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);
                                Fragment mFragment = SweepstakeFragment.newInstance(objectSweepstake);
                                transaction.replace(R.id.main_container_fragment, mFragment).addToBackStack(HomeActivity.REDEEMFRAGMENTKEY);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.commit();
                            } else {
                                int redeemValue = (int) (RewardPointsFragment.minimumRedeemPoints - Float.parseFloat(rewardPoints));
                                showErrorAlert("", String.format(getResources().getString(R.string.alert_dialog_txt_part3), redeemValue));
                            }
                        }
                    });
                    dialog.show();

                }
                break;

            default:
                break;
        }
    }

    private void _dimWindowOutsideDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .2f;
        window.setAttributes(wlp);
        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();
        switch (requestcode) {

            case Constants.REQUEST_REDEEMFRAGMENT:

                Log.d("resgoo:","resgoo:"+res);

                try {
                    objectSweepstake = new JSONObject(res);
                    if (objectSweepstake.getBoolean("Status")) {

                        String imageUrl = objectSweepstake.getString("DrawImage").replaceAll("\\\\", "");

                        Log.d("resgoo:","resgoo:"+imageUrl);

                        String finalUrl = null;
                        try {
                            finalUrl = URLEncoder.encode(imageUrl, "UTF-8").replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("\\+", "%20");

                            Log.d("resgoo:","resgoo:"+finalUrl);

                            offerImageURL = finalUrl;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Log.d("resgoo:","resgoo:"+offerImageURL);


                        Picasso.get()
                                .load(offerImageURL) // jayesh; encoded the image_layout url ***
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//                                .placeholder(R.drawable.placeholder)
                                .placeholder(R.drawable.placeholdernew)
                                .into(offerImage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void rError(int requestcode) {

    }


}