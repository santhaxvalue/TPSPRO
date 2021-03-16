package com.panelManagement.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class TextViewPlus extends AppCompatTextView {
    Typeface fontnormal;
    Context mContext;

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public TextViewPlus(Context context) {
       this(context, null);
    }

    void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;
        mContext = context;
        if (fontnormal == null) {
            fontnormal = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/gothic.ttf");
            this.setTypeface(fontnormal);
        }
    }

}
