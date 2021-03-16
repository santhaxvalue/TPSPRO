package com.panelManagement.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panelManagement.activity.R;
import com.panelManagement.model.OpinionPollResultItem;
import com.panelManagement.viewholder.CommonPreferenceViewHolder;
import com.panelManagement.viewholder.OpinionResultViewHolder;


import java.util.ArrayList;

public class QuickPollRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OpinionPollResultItem> mOpinionPollResultList = new ArrayList<>();
    private Context mContext;

    public QuickPollRecyclerAdapter(Context context, ArrayList<OpinionPollResultItem> opinionPollResultList) {
        mContext = context;
        mOpinionPollResultList = opinionPollResultList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_opinionresult_item, null);
        OpinionResultViewHolder holder = new OpinionResultViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OpinionResultViewHolder) {
            ((OpinionResultViewHolder) holder).setData(mOpinionPollResultList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mOpinionPollResultList.size();
    }
}
