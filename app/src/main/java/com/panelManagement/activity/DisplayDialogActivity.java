package com.panelManagement.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.panelManagement.utils.AlertDialogUtility;
import com.panelManagement.utils.PanelConstants;

public class DisplayDialogActivity extends Activity {
    private String message;
    private int count;
    // private HashMap<String, String> mLocaleModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* REMOVE-LOCALE */
        // mLocaleModel = LocaleDetailModel.getInstance().getLocaledetails();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            message = intent.getExtras().getString("message");
            count = intent.getExtras().getInt("count");
            showDialog();
        }
    }

    private void showDialog() {
        if (PanelConstants.IS_FOREGROUND) {
            AlertDialogUtility.showDialog(DisplayDialogActivity.this,
                    getString(R.string.app_name), message,
                    getString(R.string.Ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            DisplayDialogActivity.this.finish();
                        }
                    });
        } else {
            AlertDialogUtility.showAlertDialog(DisplayDialogActivity.this,
                    getString(R.string.app_name), message,
                    getString(R.string.Ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            DisplayDialogActivity.this.finish();
                        }
                    }, "View", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(DisplayDialogActivity.this, PushNotifierActivity.class);
                            startActivity(myIntent);
                            dialog.dismiss();
                            DisplayDialogActivity.this.finish();
                        }
                    });
        }
    }
}