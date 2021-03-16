package com.panelManagement.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.panelManagement.activity.R;

public class AlertDialogUtility extends AlertDialog.Builder {

    private static ProgressDialog mDialog;
    private static AlertDialog alertDialog_yes, alertDialog_both;
    public AlertDialogUtility(Context arg0) {
        super(arg0);

    }

    public static void showAlertDialog(Context mActivity, String title, String message, String positiveBtnText,
                                       DialogInterface.OnClickListener ACCEPT, String negativeBtnText, DialogInterface.OnClickListener DECLINE) {
        if (alertDialog_yes != null && alertDialog_yes.isShowing())
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mActivity);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message

        alertDialogBuilder.setMessage(message)
                .setPositiveButton(positiveBtnText, ACCEPT)
                .setNegativeButton(negativeBtnText, DECLINE);
        alertDialog_yes = alertDialogBuilder.create();
        alertDialog_yes.setCancelable(false);
        alertDialog_yes.show();

    }

    public static void showDialog(Context context, String title, String message, String positiveBtnText,
                                  DialogInterface.OnClickListener ACCEPT) {
        if (alertDialog_both != null && alertDialog_both.isShowing())
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(message).setPositiveButton(positiveBtnText, ACCEPT);
        alertDialog_both = alertDialogBuilder.create();
        alertDialog_both.setCancelable(false);
        alertDialog_both.show();

    }

    public static void showProgressDialog(Context context) {

        mDialog = ProgressDialog.show(context, context.getString(R.string.app_name), context.getString(R.string.dialog_login), true, false);

    }

    public static void dismisDialog() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }


}
