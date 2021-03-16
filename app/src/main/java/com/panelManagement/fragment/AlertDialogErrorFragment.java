package com.panelManagement.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnClickAction;

import java.util.ArrayList;

public class AlertDialogErrorFragment extends DialogFragment implements DialogInterface.OnDismissListener{

    private static final String KEYTITLE = "title";
    private static final String KEYMESSAGE = "message";
    TextView dialogtxt;
    String errorMessage;
    private String title;
    private OnClickAction onClickAction;
    private Dialog rootView;
    ImageView iv_close;

    public AlertDialogErrorFragment() {
        super();
    }

    public static AlertDialogErrorFragment newInstance(String title, String message) {
        AlertDialogErrorFragment frag = new AlertDialogErrorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEYTITLE, title);
        bundle.putString(KEYMESSAGE, message);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        if (budle != null) {
            title = budle.getString(KEYTITLE);
            errorMessage = budle.getString(KEYMESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        rootView = new Dialog(getActivity(), R.style.Theme_Dialog);
        rootView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView.setContentView(R.layout.dialog_custom_alert);

        Window window = rootView.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        TextView dialogtxtTitle = rootView.findViewById(R.id.dialog_txt_title);
        if (!TextUtils.isEmpty(title))
            dialogtxtTitle.setText(title);
        else
            dialogtxtTitle.setText("");

        iv_close = rootView.findViewById(R.id.iv_close_error);
        dialogtxt = rootView.findViewById(R.id.dialog_txt);
        if(!errorMessage.contains("<li>"))
        {
            dialogtxt.setText(errorMessage);
            dialogtxt.setGravity(Gravity.CENTER);
        }
        else
        {
            String first = errorMessage.replaceAll("<li>","* ");
            String second = first.replaceAll("</li>","\n");
            dialogtxt.setText(second);
        }
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    public void setListener(OnClickAction onClickAction) {
        this.onClickAction = onClickAction;
    }
}