package com.panelManagement.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.panelManagement.listener.OnSettingClickListener;

public class SettingWidget extends Button implements OnClickListener {
    private OnSettingClickListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
    }

    public SettingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public SettingWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        // TODO Auto-generated constructor stub
    }

    public SettingWidget(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setOnSettingClickedListener(OnSettingClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onSettingCall();
        }

    }

}
