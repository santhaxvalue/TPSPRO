package com.panelManagement.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.CustumCombinedNotificationItem;
import com.panelManagement.model.NotificationLogItem;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.CommonRecyclerAdapter;

public class NotificationLogViewHolder extends RecyclerView.ViewHolder {
    private TextView notificationMessgae;
    private TextView dateTime;
    public NotificationLogViewHolder(View itemView) {
        super(itemView);

        notificationMessgae = itemView.findViewById(R.id.tv_notification_line);
        dateTime = itemView.findViewById(R.id.tv_date);
    }

    public void setData(CustumCombinedNotificationItem notificationLogItem, CommonRecyclerAdapter commonRecyclerAdapter) {

        if(notificationLogItem != null) {
            notificationMessgae.setText(notificationLogItem.getNotificationMessage());
            dateTime.setText(Utility.getDateFromString(notificationLogItem.getCreationDate(), Constants.INPUT_DATE_UTC_FORMAT, Constants.INPUT_DATE_HH_MM_FORMAT));
        }
    }
}
