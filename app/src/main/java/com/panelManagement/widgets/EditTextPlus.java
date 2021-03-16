package com.panelManagement.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class EditTextPlus extends EditText {
    static Typeface fontnormal;
    Context mContext;

    public EditTextPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EditTextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextPlus(Context context) {
        super(context);
        init(context, null);

    }

    public void setLayoutParams(int width, int height) {
        super.setLayoutParams(new LayoutParams(width, height));
    }

    void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;
        mContext = context;
        // setHintTextColor(context.getResources().getColor(R.color.edittext_hint));
        // setTextColor(context.getResources().getColor(R.color.edittext_text));
        // setTextSize(14);
        if (fontnormal == null)
            fontnormal = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/MyriadPro-Bold.ttf");
        setTypeface(fontnormal);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
