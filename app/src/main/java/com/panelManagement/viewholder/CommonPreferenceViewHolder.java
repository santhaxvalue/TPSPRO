package com.panelManagement.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnCheckBoxClickListener;
import com.panelManagement.model.CommunicationPreferenceItem;

public class CommonPreferenceViewHolder extends RecyclerView.ViewHolder {
    CheckBox radioButton;
    Context mContext;

    //Mapping Constants keys for Communication preference to achieve localisation
    public static final String COMM_PREF_EMAIL = "lblEmailCommPref";
    public static final String COMM_PREF_MOBILE_SMS = "lblMobileSMSCommPref";
    public static final String COMM_PREF_MOBILE_WHATSAPP = "lblWhatsappCommPref";
    public static final String COMM_PREF_MOBILE_PUSH_NOTIFICATION = "lblMobileNotifyCommPref";
    public static final String COMM_PREF_DESKTOP_NOTIFICATION = "lblDesktopNotifyCommPref";

    public CommonPreferenceViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        radioButton = itemView.findViewById(R.id.radioButton);
    }

    public void setData(CommunicationPreferenceItem prefItem, OnCheckBoxClickListener listener) {
        if (prefItem != null) {

            radioButton.setText(getLocalLanguageString(prefItem.getResourceKey().trim(),prefItem));
            if (prefItem.getSelected().equals("1")) {
                radioButton.setChecked(true);
                listener.onCheckBoxClicked(prefItem.getID());
            }
            if (!prefItem.getIsAllowedToChange()) {
                radioButton.setClickable(false);
            }
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onCheckBoxClicked(prefItem.getID()));
        }
    }

    private String getLocalLanguageString(String resourceKey,CommunicationPreferenceItem prefItem) {
        switch (resourceKey) {
            case COMM_PREF_EMAIL:
                return mContext.getResources().getString(R.string.comm_pref_email);
            case COMM_PREF_MOBILE_SMS:
                return mContext.getResources().getString(R.string.comm_pref_mobile_sms);
            case COMM_PREF_MOBILE_WHATSAPP:
                return prefItem.getName();
            case COMM_PREF_MOBILE_PUSH_NOTIFICATION:
                return mContext.getResources().getString(R.string.comm_pref_mobile_push_notification);
            case COMM_PREF_DESKTOP_NOTIFICATION:
                return mContext.getResources().getString(R.string.comm_pref_desktop_notification);
            default:
                return "";
        }
    }
}
