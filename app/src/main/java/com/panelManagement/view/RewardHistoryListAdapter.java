package com.panelManagement.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.panelManagement.activity.R;
import com.panelManagement.model.EarnedPointHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RewardHistoryListAdapter extends RecyclerView.Adapter<RewardHistoryListAdapter.ViewHolder> {

    Handler mHandler = new Handler();
    private ArrayList<EarnedPointHistory> items = new ArrayList<>();
    private EarnedPointHistory positionItem;
    private Context context;
    private int filterCount = 0;

    public RewardHistoryListAdapter(Context context, ArrayList<EarnedPointHistory> value) {
        this.items = value;
        this.context = context;
        Log.e("ArrayList122", this.items.get(0).getPoints());
    }

    @Override
    public RewardHistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rewards_history_list_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (items != null || items.size() != 0) {
            positionItem = items.get(position);

            holder.dayLabel.setText(positionItem.getTransactionDate().substring(8, 10));
            Log.e("ArrayList12", items.get(0).getPoints());
            // holder.dateLayout.setBackgroundColor(getRandomColor());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.dateLayout.setBackgroundColor(_getRandomColor());
            } else {
                holder.dateLayout.setBackgroundColor(_getRandomColor());
            }
            switch (Integer.parseInt(positionItem.getTransactionDate().substring(5, 7))) {
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
            Log.e("Earned", positionItem.getPoints());

//            Log.d("length:","length:"+positionItem.getSourcestatus().length());

            if (!TextUtils.isEmpty(positionItem.getCampSource()) && !positionItem.getCampSource().equalsIgnoreCase("null")) {
                //old code
//                holder.txtHeader.setText(positionItem.getCampSource());
                //old code

                //new code
                if (TextUtils.isEmpty(positionItem.getSourcestatus())) {
                    holder.txtHeader.setText(positionItem.getCampSource());
                } else {
//                    holder.txtHeader.setText(positionItem.getCampSource()+" (Complete)");
                    holder.txtHeader.setText(positionItem.getCampSource() + " (" + positionItem.getSourcestatus() + ")");
                }
                //new code
            } else if (!TextUtils.isEmpty(positionItem.getProjectSource()) && !positionItem.getProjectSource().equalsIgnoreCase("null")){
                //old code
//                holder.txtHeader.setText(positionItem.getProjectSource());
                //old code

                //new code
                if (TextUtils.isEmpty(positionItem.getSourcestatus())) {
                    holder.txtHeader.setText(positionItem.getProjectSource());
                } else {
//                    holder.txtHeader.setText(positionItem.getCampSource()+" (Complete)");
                    holder.txtHeader.setText(positionItem.getProjectSource() + " (" + positionItem.getSourcestatus() + ")");
                }
                //new code

        }
            else if (!TextUtils.isEmpty(positionItem.getName()) && !positionItem.getName().equalsIgnoreCase("null")) {
                //old code
//                holder.txtHeader.setText(positionItem.getName());
                //old code

                //new code
                if (TextUtils.isEmpty(positionItem.getSourcestatus())) {
                    holder.txtHeader.setText(positionItem.getName());
                } else {
//                    holder.txtHeader.setText(positionItem.getCampSource()+" (Complete)");
                    holder.txtHeader.setText(positionItem.getName() + " (" + positionItem.getSourcestatus() + ")");
                }
                //new code

            }
            else {
                //old code
//                holder.txtHeader.setText(context.getResources().getString(R.string.txt_expired));
                //old code

                //new code
                if (TextUtils.isEmpty(positionItem.getSourcestatus())) {
                    holder.txtHeader.setText(context.getResources().getString(R.string.txt_expired));
                } else {
//                    holder.txtHeader.setText(positionItem.getCampSource()+" (Complete)");
                    holder.txtHeader.setText(context.getResources().getString(R.string.txt_expired) + " (" + positionItem.getSourcestatus() + ")");
                }
                //new code

            }

            // holder.txtSurveyDate.setText(Utility.formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd-MMM-yyyy HH:mm", positionItem.getTransactionDate()));

            if (positionItem.getPoints().matches("^\\d+$")) {
                holder.surveyEarn.setText(String.valueOf(context.getResources().getString(R.string.txt_earned) + ": "
                        + items.get(position).getPoints() + " " + context.getResources().getString(R.string.txt_points)));

                Log.e("Earned", positionItem.getPoints() );
            } else {
                holder.surveyEarn.setText(String.valueOf(context.getResources().getString(R.string.txt_earned) + " " +
                        context.getResources().getString(R.string.txt_tickets) + ": " + positionItem.getPoints()));
            }
        } else {

        }
    }

    private int _getRandomColor() {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        return androidColors[new Random().nextInt(androidColors.length)];
    }

    @Override
    public int getItemCount() {

        //viswa commend
       /* if (items != null) {
            _sortArrayByLatest();
            return items.size();
        } else return 0;*/
//viswa commend
        return items.size();
    }

    private void _sortArrayByLatest() {
        Collections.sort(items, (o1, o2) -> {
            if (o1.getTransactionDate() == null || o2.getTransactionDate() == null)
                return 0;
            return o1.getTransactionDate().compareTo(o2.getTransactionDate());
        });
        Collections.reverse(items);
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHeader;
        //TextView txtSurveyDate;
        TextView surveyEarn;
        TextView dayLabel;
        TextView monthLabel;
        LinearLayout dateLayout;

        ViewHolder(View itemView) {
            super(itemView);

            txtHeader = itemView.findViewById(R.id.tv_SurveyName);
            //txtSurveyDate = itemView.findViewById(R.id.tv_date);
            surveyEarn = itemView.findViewById(R.id.tv_earn);
            dayLabel = itemView.findViewById(R.id.day_label);
            monthLabel = itemView.findViewById(R.id.month_label);
            dateLayout = itemView.findViewById(R.id.date_ll);

        }
    }
}
