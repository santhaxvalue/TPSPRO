package com.panelManagement.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class ButtonPlus extends AppCompatButton {
    static Typeface fontnormal;
    Context mContext;

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //	init(context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context, attrs);
    }

    public ButtonPlus(Context context) {
        super(context);
        //	init(context, null);
    }

/*	void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
			return;
		mContext = context;
		if (fontnormal == null) {
			fontnormal = Typeface.createFromAsset(getResources().getAssets(),
					"fonts/century-gothic-bold.ttf");
			this.setTypeface(fontnormal);
		}

	}*/

    @Override
    public void setTypeface(Typeface tf) {

        fontnormal = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/century-gothic-bold.ttf");

        super.setTypeface(fontnormal);
    }
}
