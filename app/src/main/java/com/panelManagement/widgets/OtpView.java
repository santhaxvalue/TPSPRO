package com.panelManagement.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.panelManagement.activity.R;

/**
 * Created by Centura Technology on 2/24/2018.
 */

public class OtpView extends LinearLayout {
    private EditText mOtpOneField;
    private EditText mOtpTwoField;
    private EditText mOtpThreeField;
    private EditText mOtpFourField;
    private EditText mOtpFiveField;
    private EditText mOtpSixField;
    private EditText mCurrentlyFocusedEditText;

    public OtpView(Context context) {
        super(context);
        this.init(null);
    }

    public OtpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public OtpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styles = this.getContext().obtainStyledAttributes(attrs, com.mukesh.R.styleable.OtpView);
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.otp_view_layout, this);
        this.mOtpOneField = this.findViewById(R.id.otp_one_edit_text);
        this.mOtpTwoField = this.findViewById(R.id.otp_two_edit_text);
        this.mOtpThreeField = this.findViewById(R.id.otp_three_edit_text);
        this.mOtpFourField = this.findViewById(R.id.otp_four_edit_text);
        this.mOtpFiveField = this.findViewById(R.id.otp_five_edit_text);
        this.mOtpSixField = this.findViewById(R.id.otp_six_edit_text);
        this.styleEditTexts(styles);
        styles.recycle();
        this.mOtpOneField.requestFocus();
    }

    private String makeOTP() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mOtpOneField.getText().toString());
        stringBuilder.append(this.mOtpTwoField.getText().toString());
        stringBuilder.append(this.mOtpThreeField.getText().toString());
        stringBuilder.append(this.mOtpFourField.getText().toString());
        stringBuilder.append(this.mOtpFiveField.getText().toString());
        stringBuilder.append(this.mOtpSixField.getText().toString());
        return stringBuilder.toString();
    }

    public boolean hasValidOTP() {
        return this.makeOTP().length() == 6;
    }

    public String getOTP() {
        return this.makeOTP();
    }

    public void setOTP(String otp) {
        if (otp.length() != 6) {
            Log.e("OTPView", "Invalid otp param");
        } else if (this.mOtpOneField.getInputType() == 2 && !otp.matches("[0-9]+")) {
            Log.e("OTPView", "OTP doesn't match INPUT TYPE");
        } else {
            this.mOtpOneField.setText(String.valueOf(otp.charAt(0)));
            this.mOtpTwoField.setText(String.valueOf(otp.charAt(1)));
            this.mOtpThreeField.setText(String.valueOf(otp.charAt(2)));
            this.mOtpFourField.setText(String.valueOf(otp.charAt(3)));
            this.mOtpFiveField.setText(String.valueOf(otp.charAt(4)));
            this.mOtpSixField.setText(String.valueOf(otp.charAt(5)));
        }
    }

    private void styleEditTexts(TypedArray styles) {
        int textColor = styles.getColor(R.styleable.OtpView_android_textColor, -16777216);
        int backgroundColor = styles.getColor(R.styleable.OtpView_text_background_color, 0);
        if (styles.getColor(R.styleable.OtpView_text_background_color, 0) != 0) {
            this.mOtpOneField.setBackgroundColor(backgroundColor);
            this.mOtpTwoField.setBackgroundColor(backgroundColor);
            this.mOtpThreeField.setBackgroundColor(backgroundColor);
            this.mOtpFourField.setBackgroundColor(backgroundColor);
            this.mOtpFiveField.setBackgroundColor(backgroundColor);
            this.mOtpSixField.setBackgroundColor(backgroundColor);
        } else {
            this.mOtpOneField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            this.mOtpTwoField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            this.mOtpThreeField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            this.mOtpFourField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            this.mOtpFiveField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
            this.mOtpSixField.getBackground().mutate().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
        }

        this.mOtpOneField.setTextColor(textColor);
        this.mOtpTwoField.setTextColor(textColor);
        this.mOtpThreeField.setTextColor(textColor);
        this.mOtpFourField.setTextColor(textColor);
        this.mOtpFiveField.setTextColor(textColor);
        this.mOtpSixField.setTextColor(textColor);
        this.setEditTextInputStyle(styles);
    }

    private void setEditTextInputStyle(TypedArray styles) {
        int inputType = styles.getInt(R.styleable.OtpView_android_inputType, 0);
        this.mOtpOneField.setInputType(inputType);
        this.mOtpTwoField.setInputType(inputType);
        this.mOtpThreeField.setInputType(inputType);
        this.mOtpFourField.setInputType(inputType);
        this.mOtpFiveField.setInputType(inputType);
        this.mOtpSixField.setInputType(inputType);
        String text = styles.getString(R.styleable.OtpView_otp);
        if (!TextUtils.isEmpty(text) && text.length() == 6) {
            this.mOtpOneField.setText(String.valueOf(text.charAt(0)));
            this.mOtpTwoField.setText(String.valueOf(text.charAt(1)));
            this.mOtpThreeField.setText(String.valueOf(text.charAt(2)));
            this.mOtpFourField.setText(String.valueOf(text.charAt(3)));
            this.mOtpFiveField.setText(String.valueOf(text.charAt(4)));
            this.mOtpSixField.setText(String.valueOf(text.charAt(5)));
        }

        this.setFocusListener();
        this.setOnTextChangeListener();
    }

    private void setFocusListener() {
        OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                OtpView.this.mCurrentlyFocusedEditText = (EditText) v;
                OtpView.this.mCurrentlyFocusedEditText.setSelection(OtpView.this.mCurrentlyFocusedEditText.getText().length());
            }
        };
        this.mOtpOneField.setOnFocusChangeListener(onFocusChangeListener);
        this.mOtpTwoField.setOnFocusChangeListener(onFocusChangeListener);
        this.mOtpThreeField.setOnFocusChangeListener(onFocusChangeListener);
        this.mOtpFourField.setOnFocusChangeListener(onFocusChangeListener);
        this.mOtpFiveField.setOnFocusChangeListener(onFocusChangeListener);
        this.mOtpSixField.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void disableKeypad() {
        OnTouchListener touchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService("input_method");
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                return true;
            }
        };
        this.mOtpOneField.setOnTouchListener(touchListener);
        this.mOtpTwoField.setOnTouchListener(touchListener);
        this.mOtpThreeField.setOnTouchListener(touchListener);
        this.mOtpFourField.setOnTouchListener(touchListener);
        this.mOtpFiveField.setOnTouchListener(touchListener);
        this.mOtpSixField.setOnTouchListener(touchListener);
    }

    public void enableKeypad() {
        OnTouchListener touchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        };
        this.mOtpOneField.setOnTouchListener(touchListener);
        this.mOtpTwoField.setOnTouchListener(touchListener);
        this.mOtpThreeField.setOnTouchListener(touchListener);
        this.mOtpFourField.setOnTouchListener(touchListener);
        this.mOtpFiveField.setOnTouchListener(touchListener);
        this.mOtpSixField.setOnTouchListener(touchListener);
    }

    public EditText getCurrentFoucusedEditText() {
        return this.mCurrentlyFocusedEditText;
    }

    private void setOnTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (OtpView.this.mCurrentlyFocusedEditText.getText().length() >= 1 && OtpView.this.mCurrentlyFocusedEditText != OtpView.this.mOtpSixField) {
                    OtpView.this.mCurrentlyFocusedEditText.focusSearch(View.FOCUS_RIGHT).requestFocus();
                } else if (OtpView.this.mCurrentlyFocusedEditText.getText().length() >= 1 && OtpView.this.mCurrentlyFocusedEditText == OtpView.this.mOtpSixField) {
                    InputMethodManager imm = (InputMethodManager) OtpView.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(OtpView.this.getWindowToken(), 0);
                    }
                } else {
                    String currentValue = OtpView.this.mCurrentlyFocusedEditText.getText().toString();
                    if (currentValue.length() <= 0 && OtpView.this.mCurrentlyFocusedEditText.getSelectionStart() <= 0) {
                        OtpView.this.mCurrentlyFocusedEditText.focusSearch(View.FOCUS_LEFT).requestFocus();
                    }
                }

            }
        };
        this.mOtpOneField.addTextChangedListener(textWatcher);
        this.mOtpTwoField.addTextChangedListener(textWatcher);
        this.mOtpThreeField.addTextChangedListener(textWatcher);
        this.mOtpFourField.addTextChangedListener(textWatcher);
        this.mOtpFiveField.addTextChangedListener(textWatcher);
        this.mOtpSixField.addTextChangedListener(textWatcher);
    }

    public void simulateDeletePress() {
        this.mCurrentlyFocusedEditText.setText("");
    }
}
