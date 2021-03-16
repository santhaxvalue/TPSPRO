package com.panelManagement.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewPlusBold extends TextView {
    static Typeface fontnormal;
    Context mContext;

    public TextViewPlusBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public TextViewPlusBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewPlusBold(Context context) {
        super(context);
        init(context, null);
    }

    void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;
        mContext = context;
        if (fontnormal == null) {
            fontnormal = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/century-gothic-bold.ttf");
            this.setTypeface(fontnormal);
        }
    }

}
