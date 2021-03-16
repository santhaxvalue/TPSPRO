package com.panelManagement.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.panelManagement.listener.OnBackClickListener;

public class BackButtonWidget extends Button implements OnClickListener {
    private OnBackClickListener listener;

    public BackButtonWidget(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
    }

    public BackButtonWidget(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public BackButtonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        // TODO Auto-generated constructor stub
    }

    public BackButtonWidget(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void setOnBackClickedListener(OnBackClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onBackButtonPressed();
        }

    }

}
