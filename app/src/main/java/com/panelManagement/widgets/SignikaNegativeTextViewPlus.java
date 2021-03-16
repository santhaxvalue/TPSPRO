package com.panelManagement.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class SignikaNegativeTextViewPlus extends TextViewPlus {

    static Typeface fontnormal;
    Context mContext;

    public SignikaNegativeTextViewPlus(Context context, AttributeSet attrs,
                                       int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SignikaNegativeTextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SignikaNegativeTextViewPlus(Context context) {
        super(context);
        init(context, null);
    }

    @Override
    void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;
        mContext = context;
        if (fontnormal == null) {
            fontnormal = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/MyriadPro-Bold.ttf");
            this.setTypeface(fontnormal);
        }
    }

}
