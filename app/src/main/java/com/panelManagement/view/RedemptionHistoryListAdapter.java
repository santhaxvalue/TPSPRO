package com.panelManagement.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.RedeemPointHistory;
import com.panelManagement.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static android.support.v4.content.ContextCompat.startActivity;

public class RedemptionHistoryListAdapter extends RecyclerView.Adapter<RedemptionHistoryListAdapter.ViewHolder> implements OnTouchListener {

    Handler mHandler = new Handler();
    private ArrayList<RedeemPointHistory> items;
    private Context context;
    private int filterCount = 0;

    public RedemptionHistoryListAdapter(Context context, ArrayList<RedeemPointHistory> value) {
        this.items = value;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.redemption_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final RedeemPointHistory profile = items.get(position);

        holder.dayLabel.setText(profile.getTransactionDate().substring(8, 10));

        //holder.dateLayout.setBackgroundColor(getRandomColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.dateLayout.setBackgroundColor(_getRandomColor());
        } else {
            holder.dateLayout.setBackgroundColor(_getRandomColor());
        }

        switch (Integer.parseInt(profile.getTransactionDate().substring(5, 7))) {
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

        if (TextUtils.isEmpty(profile.getImageUrl())) {
            holder.logo.setVisibility(View.GONE);
            holder.venderName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(profile.getRedeemChoices()) && !profile.getRedeemChoices().equalsIgnoreCase("null"))
                holder.venderName.setText(profile.getRedeemChoices());
            else if (!TextUtils.isEmpty(profile.getName()) && !profile.getName().equalsIgnoreCase("null"))
                holder.venderName.setText(profile.getName());
            else
                holder.venderName.setText(context.getString(R.string.pointexpired));

        } else {
            holder.logo.setVisibility(View.VISIBLE);
            holder.venderName.setVisibility(View.GONE);
            Picasso.get().load(profile.getImageUrl()).into(holder.logo);
//            {
//                //panel
//                /*img_icon.setDefaultImageResource(R.drawable.ic_jabong);
//                img_icon.setUrl(profile.getImageUrl());*/
//            }
//            {
//                //SourceEdge
//               // Utility.Glide(context, profile.getImageUrl(), holder.logo);
//            }
        }

        //new code
        if(!profile.getVoucherCode().equals("") && profile.getVoucherCode() != null) {
            holder.voucherCodelayout.setVisibility(View.VISIBLE);
            //new code
            if (profile.getVoucherCode().contains("https://") || profile.getVoucherCode().contains("http://")) {
                holder.txtDate.setText(context.getResources().getString(R.string.click_here));
                holder.txtDate.setOnClickListener(v -> {

                    voucherDialog(context, profile.getVoucherCode());
                });
            } else {
                holder.txtDate.setText(profile.getVoucherCode());
            }

            //new code
        }else{
            holder.voucherCodelayout.setVisibility(View.GONE);
        }
        //new code

        Log.d("vpasswordnew:","vpasswordnew:"+profile.getVpassword());

        if((profile.getVpassword().equals("") && profile.getVpassword() == null) || profile.getVpassword().length() == 0){
            holder.voucherpinlayout.setVisibility(View.GONE);
        }else {
            holder.voucherpinlayout.setVisibility(View.VISIBLE);
            holder.voucherPinnew.setText(profile.getVpassword());
        }




        holder.surveyEarn.setText("" + profile.getPoints() + " " + context.getResources().getString(R.string.txt_points));
        // viewWrapper.getSurveyDate().setText(getMydate(profile.getTransactionDate()));
        holder.txtSurveyDate.setText(Utility.formateDateFromstring("yyyy-MM-dd'T'HH:mm:ss.SSS",
                "dd-MMM-yyyy HH:mm", profile.getTransactionDate()));
        if (!TextUtils.isEmpty(profile.getRedemptionStatus()) && !profile.getRedemptionStatus().equals("null")) {
            holder.status.setText(String.valueOf(profile.getRedemptionStatus()));
            if (profile.getRedemptionStatus().equals("Open"/*context.getString(R.string.open*/)) {
                holder.statusIcon.setBackgroundResource(R.drawable.ic_circle_green_tick);
            } else {
                holder.statusIcon.setBackgroundResource(R.drawable.ic_close_dialog);
            }
        }
    }

    private void voucherDialog(Context context,String url) {

        TextView tv_voucherCode,tv_okButton;
        ImageView close_voucher_dialog_general;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_web_voucher_code);
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

        tv_voucherCode = dialog.findViewById(R.id.tv_getWebVoucherCode);
        tv_okButton = dialog.findViewById(R.id.tv_btnWebVoucherOk);
        tv_voucherCode.setText(url);
        close_voucher_dialog_general = dialog.findViewById(R.id.close_WebVoucher_dialog_general);

        close_voucher_dialog_general.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        tv_okButton.setOnClickListener(v -> {
            dialog.dismiss();
            //old code for changes
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                context.startActivity(i);
            //old code for changes

        });

        tv_voucherCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Code
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
                //New Code
            }
        });

        dialog.show();
    }

    private int _getRandomColor() {
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        return androidColors[new Random().nextInt(androidColors.length)];
    }

//    private Drawable _generateGradientColor() {
//        GradientDrawable gd = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM,
//                new int[]{0xFF616261, 0xFF131313});
//        gd.setCornerRadius(0f);
//
//        return gd;
//    }

    @Override
    public int getItemCount() {
        if (items != null) {
            _sortArrayByLatest();
            return items.size();
        } else return 0;
    }

    private void _sortArrayByLatest() {
        Collections.sort(items, (o1, o2) -> {
            if (o1.getTransactionDate() == null || o2.getTransactionDate() == null)
                return 0;
            return o1.getTransactionDate().compareTo(o2.getTransactionDate());
        });
        Collections.reverse(items);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public int getRandomColor()
    {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView voucherPinnew;
        TextView txtSurveyDate;
        TextView surveyEarn;
        TextView status;
        TextView dayLabel;
        TextView monthLabel;
        LinearLayout dateLayout;
        ImageView logo;
        ImageView statusIcon;
        private TextView venderName;
        RelativeLayout voucherpinlayout;
        RelativeLayout voucherCodelayout;

        public ViewHolder(View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.iv_vendorimg);
            txtDate = itemView.findViewById(R.id.tv_voucherCode);
            voucherPinnew = itemView.findViewById(R.id.tv_voucherPin);
            voucherpinlayout = itemView.findViewById(R.id.voucherpinlayout);
            voucherCodelayout = itemView.findViewById(R.id.voucherCodelayout);
            txtSurveyDate = itemView.findViewById(R.id.tv_calendarDate);
            surveyEarn = itemView.findViewById(R.id.tv_earnPoint);
            status = itemView.findViewById(R.id.tv_donetxt);
            venderName = itemView.findViewById(R.id.tv_vendorName);
            statusIcon = itemView.findViewById(R.id.iv_done);
            dayLabel = itemView.findViewById(R.id.day_label_red_his);
            monthLabel = itemView.findViewById(R.id.month_label_red_his);
            dateLayout = itemView.findViewById(R.id.date_ll_red_hist);

        }
    }

    /*
     * public String getMydate(String mydate){ try{ SimpleDateFormat sdf = new
     * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US); Date date =
     * sdf.parse(mydate);
     *
     * SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
     * return sdf2.format(date); }catch(Exception e){ return null; } }
     */


}
