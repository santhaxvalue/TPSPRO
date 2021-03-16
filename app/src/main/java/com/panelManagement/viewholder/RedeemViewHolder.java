package com.panelManagement.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.fragment.GeneralBuyFragment;
import com.panelManagement.model.OpinionPollResultItem;

public class RedeemViewHolder extends RecyclerView.ViewHolder {
    private final Context mContext;
    private Button mButton;

    public RedeemViewHolder(Context context, View itemView) {
        super(itemView);
        mButton = itemView.findViewById(R.id.redeem_point);
        mContext = context;
    }

    public void setData(String name, int selectedPosition, int currentPosition, GeneralBuyFragment mListener) {
       mButton.setText(name);
       if(selectedPosition == currentPosition) {

           mButton.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
           mButton.setTextColor(mContext.getResources().getColor(R.color.white));

       } else {
           mButton.setBackgroundColor(mContext.getResources().getColor(R.color.normal_gray));
           mButton.setTextColor(mContext.getResources().getColor(R.color.black));
       }

       mButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mListener.onClickRedeemPoints(name, currentPosition);
           }
       });
    }
}
