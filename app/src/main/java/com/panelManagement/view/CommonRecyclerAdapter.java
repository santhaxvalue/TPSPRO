package com.panelManagement.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnCheckBoxClickListener;
import com.panelManagement.model.CommunicationPreferenceItem;
import com.panelManagement.model.CustumCombinedNotificationItem;
import com.panelManagement.model.NotificationLogItem;
import com.panelManagement.viewholder.CommonPreferenceViewHolder;
import com.panelManagement.viewholder.NotificationLogViewHolder;

import java.util.ArrayList;

public class CommonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnCheckBoxClickListener {

    private static final int VIEW_TYPE_COMM_PREF = 0;
    private static final int VIEW_TYPE_NOTIFICATION = 1;
    private int mType = VIEW_TYPE_COMM_PREF;
    private ArrayList<CommunicationPreferenceItem> preferenceList = new ArrayList<>();
    private ArrayList<CustumCombinedNotificationItem> notificationList = new ArrayList<>();

    private ArrayList<Integer> selectedItemList = new ArrayList<>();


    @Override
    public void onCheckBoxClicked(int id) {

        if (selectedItemList.contains(id))
            selectedItemList.remove(selectedItemList.indexOf(id));
        else
            selectedItemList.add(id);
    }


    public CommonRecyclerAdapter(ArrayList<CommunicationPreferenceItem> list) {
        preferenceList = list;
    }

    public CommonRecyclerAdapter(ArrayList<CustumCombinedNotificationItem> list, int type) {
        notificationList = list;
        mType = type;
    }

    @Override
    public int getItemViewType(int position) {
        if (mType == VIEW_TYPE_COMM_PREF)
            return VIEW_TYPE_COMM_PREF;
        else
            return VIEW_TYPE_NOTIFICATION;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COMM_PREF:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_preference_item, parent, false);
                CommonPreferenceViewHolder holder = new CommonPreferenceViewHolder(parent.getContext(),itemView);
                return holder;
            case VIEW_TYPE_NOTIFICATION:
                View notificatonView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_notification_item, null);
                NotificationLogViewHolder notificationLogViewHolder = new NotificationLogViewHolder(notificatonView);
                return  notificationLogViewHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonPreferenceViewHolder) {
            ((CommonPreferenceViewHolder) holder).setData(preferenceList.get(position), this);
        } else if (holder instanceof NotificationLogViewHolder) {
            ((NotificationLogViewHolder) holder).setData(notificationList.get(position), this);
        }
    }

    /* Get selected checkbox list */
    public ArrayList<Integer> getSelectedList() {
        return selectedItemList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if (mType == VIEW_TYPE_COMM_PREF)
            return preferenceList.size();
        else if (mType == VIEW_TYPE_NOTIFICATION)
            return notificationList.size();
        else return 0;
    }

}

