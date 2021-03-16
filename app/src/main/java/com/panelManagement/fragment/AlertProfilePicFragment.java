package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnprofileCaptureTypeListener;

public class AlertProfilePicFragment extends DialogFragment implements OnClickListener {
    public static final int PICK_CAMERA = 1;
    public static final int PICK_GALLERY = 2;
    public static final int PICTURE_CROP = 3;
    public static final int RESULT_OK = 4;
    OnprofileCaptureTypeListener listener = null;

    public AlertProfilePicFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public AlertProfilePicFragment(OnprofileCaptureTypeListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog rootView = new Dialog(getActivity(), R.style.slideBottomTopTheme);
        rootView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView.setContentView(R.layout.picture_alert);
        // rootView.getWindow().setBackgroundDrawable(
        // getResources().getDrawable(R.drawable.transparentbg));
        rootView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.getWindow().setGravity(Gravity.CENTER);
        rootView.findViewById(R.id.camera).setOnClickListener(this);
        rootView.findViewById(R.id.gallery).setOnClickListener(this);
        rootView.findViewById(R.id.done).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.done:
                dismiss();
                break;
            case R.id.camera:
                if (listener != null)
                    listener.onPickerSelection(PICK_CAMERA);
                dismiss();
                break;
            case R.id.gallery:
                if (listener != null)
                    listener.onPickerSelection(PICK_GALLERY);
                dismiss();
                break;
        }
    }
}