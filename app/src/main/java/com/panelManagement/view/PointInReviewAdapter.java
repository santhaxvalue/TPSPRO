package com.panelManagement.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.EarnedPointHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PointInReviewAdapter extends RecyclerView.Adapter<PointInReviewAdapter.ViewHolder> {

    Handler mHandler = new Handler();
    private ArrayList<EarnedPointHistory> items;
    private EarnedPointHistory positionItem;
    private Context context;
    private int filterCount = 0;
    ArrayList<String> surveyNameList,reviewPointsList,ticketList,surveyIdList,SurveyCompletionDateList;
    public PointInReviewAdapter(Context context, ArrayList<EarnedPointHistory> value,ArrayList<String> surveyNameList,ArrayList<String> reviewPointsList,ArrayList<String> ticketList,ArrayList<String> surveyIdList,ArrayList<String> SurveyCompletionDateList) {
        this.items = value;
        this.context = context;
        this.surveyNameList = surveyNameList;
        this.reviewPointsList = reviewPointsList;
        this.ticketList = ticketList;
        this.surveyIdList = surveyIdList;
        this.SurveyCompletionDateList = SurveyCompletionDateList;
    }

    @Override
    public PointInReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.point_review_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        if (items != null || items.size() != 0) {
//            positionItem = items.get(position);
            holder.dayLabel.setText(SurveyCompletionDateList.get(position).substring(8, 10));
            // holder.dateLayout.setBackgroundColor(getRandomColor());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dateLayout.setBackgroundColor(_getRandomColor());
            } else {
                holder.dateLayout.setBackgroundColor(_getRandomColor());
            }
            switch (Integer.parseInt(SurveyCompletionDateList.get(position).substring(5, 7))) {
                case 1:
                    holder.monthLabel.setText("JAN");
                    break;
                case 2:
                    holder.monthLabel.setText("FEB");
                    break;
                case 3:
                    holder.monthLabel.setText("MAR");
                    break;
                case 4:
                    holder.monthLabel.setText("APR");
                    break;
                case 5:
                    holder.monthLabel.setText("MAY");
                    break;
                case 6:
                    holder.monthLabel.setText("JUN");
                    break;
                case 7:
                    holder.monthLabel.setText("JUL");
                    break;
                case 8:
                    holder.monthLabel.setText("AUG");
                    break;
                case 9:
                    holder.monthLabel.setText("SEP");
                    break;
                case 10:
                    holder.monthLabel.setText("OCT");
                    break;
                case 11:
                    holder.monthLabel.setText("NOV");
                    break;
                case 12:
                    holder.monthLabel.setText("DEC");
                    break;
            }


//            if (!TextUtils.isEmpty(positionItem.getCampSource()) && !positionItem.getCampSource().equalsIgnoreCase("null"))
//                holder.txtHeader.setText(positionItem.getCampSource());
//            else if (!TextUtils.isEmpty(positionItem.getProjectSource()) && !positionItem.getProjectSource().equalsIgnoreCase("null"))
//                holder.txtHeader.setText(positionItem.getProjectSource());
//            else if (!TextUtils.isEmpty(positionItem.getName()) && !positionItem.getName().equalsIgnoreCase("null"))
//                holder.txtHeader.setText(positionItem.getName());
//            else
//                holder.txtHeader.setText(context.getResources().getString(R.string.txt_expired));

            String strServeyname = surveyNameList.get(position);
            String strreviewPointsList = reviewPointsList.get(position);
            String strticketList = ticketList.get(position);
            String strsurveyIdList = surveyIdList.get(position);
            if(!strServeyname.equalsIgnoreCase("") && strServeyname!=null){
                holder.txtHeader.setText(strServeyname);
            }

            //old code
//            if(!strticketList.equalsIgnoreCase("") && strticketList!=null){
//                holder.surveyEarn.setText("Points/Ticket No"+strticketList);
//            }
        //new code
        if(!strreviewPointsList.equalsIgnoreCase("") && strreviewPointsList!=null){
            holder.surveyEarn.setText("Points/Ticket No: "+strreviewPointsList+ " Points");
//            holder.surveyEarn.setText(R.string.points_and_ticketno +strreviewPointsList+ R.string.points_new);
        }

            if(!strsurveyIdList.equalsIgnoreCase("") && strsurveyIdList!=null){
                holder.tv_serveyID.setText("SURVEY ID : "+strsurveyIdList);
//                holder.tv_serveyID.setText(R.string.survey_id_new +strsurveyIdList);
            }

            // holder.txtSurveyDate.setText(Utility.formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd-MMM-yyyy HH:mm", positionItem.getTransactionDate()));

//            if (positionItem.getPoints().matches("^\\d+$")) {
//                holder.surveyEarn.setText(String.valueOf(context.getResources().getString(R.string.txt_earned) + ": "
//                        + positionItem.getPoints() + " " + context.getResources().getString(R.string.txt_points)));
//            } else {
//                holder.surveyEarn.setText(String.valueOf(context.getResources().getString(R.string.txt_earned) + " " +
//                        context.getResources().getString(R.string.txt_tickets) + ": " + positionItem.getPoints()));
//            }
//        }
    }

    private int _getRandomColor() {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        return androidColors[new Random().nextInt(androidColors.length)];
    }

    /*@Override
    public int getItemCount() {
        if (items != null) {
            _sortArrayByLatest();
            return items.size();
        } else return 0;
    }*/
    @Override
    public int getItemCount() {
//        if (surveyNameList != null) {
//            _sortArrayByLatest();
//            return surveyNameList.size();
//        } else return 0;

        return surveyNameList.size();
    }

    private void _sortArrayByLatest() {
        Collections.sort(surveyNameList, (o1, o2) -> {
            if (o1.toString() == null || o2.toString() == null)
                return 0;
            return o1.toString().compareTo(o2.toString());
        });
        Collections.reverse(surveyNameList);
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHeader;
        //TextView txtSurveyDate;
        TextView surveyEarn;
        TextView tv_serveyID;
        TextView dayLabel;
        TextView monthLabel;
        LinearLayout dateLayout;

        ViewHolder(View itemView) {
            super(itemView);

            txtHeader = itemView.findViewById(R.id.tv_SurveyName);
            //txtSurveyDate = itemView.findViewById(R.id.tv_date);
            surveyEarn = itemView.findViewById(R.id.tv_earn);
            tv_serveyID = itemView.findViewById(R.id.tv_serveyID);
            dayLabel = itemView.findViewById(R.id.day_label);
            monthLabel = itemView.findViewById(R.id.month_label);
            dateLayout = itemView.findViewById(R.id.date_ll);

        }
    }
}
