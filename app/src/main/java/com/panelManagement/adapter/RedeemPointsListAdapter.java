package com.panelManagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panelManagement.activity.R;
import com.panelManagement.fragment.GeneralBuyFragment;
import com.panelManagement.viewholder.RedeemViewHolder;


import java.util.ArrayList;

public class RedeemPointsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final GeneralBuyFragment mListener;
    private ArrayList<String> mRedeeemPointsList = new ArrayList<>();
    private Context mContext;
    private int mSelectedPosition = 0;
    public RedeemPointsListAdapter(Context context, ArrayList<String> opinionPollResultList, GeneralBuyFragment listener) {
        mContext = context;
        mRedeeemPointsList = opinionPollResultList;
        mListener = listener;
    }

    public interface onRedeemPointsClickListener{
        public void onClickRedeemPoints(String points, int currentPosition);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reedem_points, null);
        RedeemViewHolder holder = new RedeemViewHolder(mContext, itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RedeemViewHolder) {
            ((RedeemViewHolder) holder).setData(mRedeeemPointsList.get(position), mSelectedPosition, position, mListener);
        }

    }

    @Override
    public int getItemCount() {
        return mRedeeemPointsList.size();
    }

    public void updateListItems(ArrayList<String> redeeemPointsList) {
        mRedeeemPointsList = redeeemPointsList;
        mSelectedPosition = 0;
        notifyDataSetChanged();
    }

    public void updateSelecedValue(int selectedValue){
        mSelectedPosition = selectedValue;
        notifyDataSetChanged();
    }

}
