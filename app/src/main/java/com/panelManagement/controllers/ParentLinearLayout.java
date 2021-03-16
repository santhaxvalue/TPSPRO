package com.panelManagement.controllers;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.panelManagement.model.ProfilerSurveyModels;

/**
 * @author manojp Insert row and all childview init , default orientation is set
 *         to vertical , android its default width and height is matchparent and
 *         wrapcontent. default bg is R.drawable.edtbox_rounded_corner , any one
 *         can change background by using setParentBackground(int) method.
 */
public class ParentLinearLayout extends LinearLayout {

    public ParentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefalut();
    }

    public ParentLinearLayout(Context context) {
        super(context);
        initDefalut();
    }

    private void initDefalut() {
        setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        // layoutParams.setMargins(5, 20, 5, 0);
        setPadding(20, 25, 20, 20);
        setLayoutParams(layoutParams);
        setBackgroundColor(Color.WHITE);
    }

    public void setParentBackground(int resource) {
        super.setBackgroundResource(resource);
    }

    public ProfilerSurveyModels getChilAttributes() {
        return null;
    }

}
