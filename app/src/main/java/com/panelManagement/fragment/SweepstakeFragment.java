package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static com.panelManagement.activity.HomeActivity.MYFRAGMENTKEY;
import static com.panelManagement.activity.HomeActivity.SWEEPSTAKEFRAGMENT;

public class SweepstakeFragment extends BaseFragment implements View.OnClickListener {

    TextView quantityTxt, balancePoints, pointsNum, eachTicket;
    int count = 1;
    ScrollView relativeLayout;
    private JSONObject object = null;
    private int drawMarketGropuID;
    private int ticketValue;
    private long availablePoints, tempAvailablePoints;
   // private TextView tvPointAvailable;

    public SweepstakeFragment() {
    }

    public static SweepstakeFragment newInstance(JSONObject jsonObject) {
        SweepstakeFragment fragment = new SweepstakeFragment();
        Bundle args = new Bundle();
        args.putString("jsonobject", jsonObject.toString());
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString("jsonobject");
            try {
                object = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sweetstakes, container, false);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        Utility.setMargins(context, layout, Utility.getDp(context, 40));
        quantityTxt = view.findViewById(R.id.tv_quantityNumber);
        quantityTxt.setText(String.valueOf(count));
        balancePoints = view.findViewById(R.id.tv_points);
        pointsNum = view.findViewById(R.id.tv_pointnumber);
        eachTicket = view.findViewById(R.id.tv_eachtickettxt);
        ImageView minusIcon = view.findViewById(R.id.iv_iconsMinus);
        ImageView plusIcon = view.findViewById(R.id.iv_iconPlus);
        minusIcon.setOnClickListener(this);
        plusIcon.setOnClickListener(this);
        TextView buyBtn = view.findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(this);
    //    tvPointAvailable = getActivity().findViewById(R.id.txt_points_redeemed_available);

        try {
            Log.d(":","1:"+object.getBoolean("Status"));
            Log.d("valuenew1:","1:"+object.getLong("AvailablePoints"));
            Log.d("valuenew1:","1:"+object.getInt("TicketValue"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            if (object != null) {

                Log.d("valuenew11:","1:"+object.getBoolean("Status"));
                Log.d("valuenew11:","1:"+object.getLong("AvailablePoints"));
                Log.d("valuenew11:","1:"+object.getInt("TicketValue"));


                if (object.getBoolean("Status")) {
                    availablePoints = object.getLong("AvailablePoints");
                //    tvPointAvailable.setText(String.valueOf(availablePoints));
                    eachTicket.setText(String.format(getResources().getString(R.string.txt_each_ticket_part1), object.optInt("TicketValue")));

                    ticketValue = object.getInt("TicketValue");
                    balancePoints.setText(String.valueOf(availablePoints - ticketValue));

                    pointsNum.setText(String.valueOf(ticketValue));
                    drawMarketGropuID = object.getInt("DrawMarketGroupID");

                    Log.d("drawMarketGroupID:","drawMarketGroupID:"+String.valueOf(drawMarketGropuID));

                } else {
                    eachTicket.setText(String.format(getResources().getString(R.string.txt_each_ticket_part1), 0));
                    showErrorAlert("error", object.getString("Message"));
                    plusIcon.setEnabled(false);
                    minusIcon.setEnabled(false);
                    buyBtn.setEnabled(false);

                }
            } else {
                Log.e("JSONObject", "nulll");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        relativeLayout = view.findViewById(R.id.sweepstake_layout);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_iconsMinus:
                if (count > 1) {
                    count--;
                    quantityTxt.setText(String.valueOf(count));
                    int pointTotal = count * ticketValue;
                    tempAvailablePoints = availablePoints - pointTotal;
                    pointsNum.setText(String.valueOf(pointTotal));
                    balancePoints.setText(String.valueOf(tempAvailablePoints));
                }
                break;

            case R.id.iv_iconPlus:
                int pointTotal = (count + 1) * ticketValue;
                tempAvailablePoints = availablePoints - pointTotal;
                if (pointTotal <= availablePoints) {
                    count++;
                    quantityTxt.setText(String.valueOf(count));
                    pointsNum.setText(String.valueOf(pointTotal));
                    balancePoints.setText(String.valueOf(tempAvailablePoints));
                } else {
                    showErrorAlert(" ", String.format(getString(R.string.txt_tickets_buyMax), availablePoints / ticketValue));
                }
                break;

            case R.id.buyBtn:
                try {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        showDialog(true, getString(R.string.dialog_login));

                        requestTypePost(Constants.API_BUYTICKET, new ParseJSonObject(getActivity()).getBuyTicketObject(Integer.parseInt(quantityTxt.getText().toString()),
                                drawMarketGropuID), Constants.REQUESTCODE_BUYTICKET);

                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {

        switch (requestcode) {

            case Constants.REQUESTCODE_BUYTICKET:

                Log.d("oneoneresponse:","oneoneresponse:"+res);

                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getString("Status").equals("true")) {

                        showCustomAlertDialog();

                    } else {
                        if(object.getString("IsProfiler").equals("true")) {
                            showErrorAlert("error", object.getString("Message"));
                            launchProfilePage();
                        } else {
                            showErrorAlert("error", object.getString("Message"));
                            requestUpdatePoints();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_AVAILABLE_POINTS:
                RewardPointsModels rewardsPointsData;
                try {
                    Log.e("GetIncenSweep",res.toString());
                    rewardsPointsData = new ParseJSonObject(context).getRewardsPoints(new JSONObject(res));
                    if (rewardsPointsData != null) {
                        JSONObject jsonObject = new JSONObject(res);
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_EARNEDPOINTS_, rewardsPointsData.getEarnedPoints());
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_SPENTPOINTS_, rewardsPointsData.getSpentPoints());
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_AVAILABLEPOINTS_, rewardsPointsData.getAvailablePoints());
                        TextView txt_points_redeemed = getActivity().findViewById(R.id.txt_points_redeemed_available);
                        txt_points_redeemed.setText(rewardsPointsData.getAvailablePoints());
                        //SurveyFloating.txt_points_redeemed.setText(rewardsPointsData.getAvailablePoints());
                        int threshold = jsonObject.optInt("ThresholdPoints");
                        int availablePoints = jsonObject.optInt("AvailablePoints");

                        if(availablePoints >= threshold)
                        {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack(HomeActivity.REDEEMFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        else
                         {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            fm.popBackStack(HomeActivity.REWARDSFRAGMENTKEY, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                         }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void rError(int requestcode) {

    }

    @SuppressLint("StringFormatInvalid")
    public void showCustomAlertDialog() {
        TextView countNumber;
        ImageView closeDialog;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sweepstake);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = null;
        if (window != null) {
            wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            wlp.dimAmount = .5f;
            window.setAttributes(wlp);
        }

        dialog.show();

        countNumber = dialog.findViewById(R.id.tv_purchased_ticket);
        countNumber.setText(String.format(getResources().getString(R.string.txt_puchased_ticket_part1), String.valueOf(count)));
        closeDialog = dialog.findViewById(R.id.close_dialog_sweepstake);

        closeDialog.setOnClickListener((View v) -> {
            dialog.dismiss();
            requestUpdatePoints();

        });
    }

    private void launchProfilePage() {
        ProfilerFragment nextFrag= new ProfilerFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container_fragment, nextFrag)
                .addToBackStack(SWEEPSTAKEFRAGMENT)
                .commit();
    }
}
