package com.panelManagement.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.OpinionPollResultItem;

public class OpinionResultViewHolder extends RecyclerView.ViewHolder {
    private RelativeLayout mPercentageLayout;
    private TextView mPercentage;
    private TextView mOptionsText;
    private RelativeLayout mResultLayout;
    private TextView mPercentWhenTooLess;


    public OpinionResultViewHolder(View itemView) {
        super(itemView);
        mPercentageLayout = itemView.findViewById(R.id.percentage_lyout);
        mOptionsText = itemView.findViewById(R.id.optionText);
        mPercentage = itemView.findViewById(R.id.option_percent);
        mResultLayout = itemView.findViewById(R.id.result_layout);
        mPercentWhenTooLess = itemView.findViewById(R.id.option_percent_front);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(20, 50, 0, 0);
        mResultLayout.setLayoutParams(layoutParams);
    }

    public void setData(OpinionPollResultItem opinionPollResultItem) {

        if (opinionPollResultItem != null) {
            int currentWidth = mPercentage.getLayoutParams().width;
            Double percent = opinionPollResultItem.getCountPercentage();

            int val = percent.intValue();
            int newWidth = ((currentWidth / 2) * val) / 100;
            mPercentage.getLayoutParams().width = newWidth;

            mOptionsText.setText(opinionPollResultItem.getAnswerChoiceText());
            //mPercentage.setText(String.format("%.1f", opinionPollResultItem.getCountPercentage()) + "%");

            //TODO: When percent is less than 20, the area will be smaller to display percentage. Hence percentage is displayed in front of the area.
            if (val >= 25)
                mPercentage.setText(String.format("%.1f", opinionPollResultItem.getCountPercentage()) + "%");
            else
                mPercentWhenTooLess.setText(String.format("%.1f", opinionPollResultItem.getCountPercentage()) + "%");
        }
    }
}
