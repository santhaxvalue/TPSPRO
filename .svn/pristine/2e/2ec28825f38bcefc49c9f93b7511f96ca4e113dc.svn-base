package com.panelManagement.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.panelManagement.fragment.AlertDialogErrorFragment;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;

import org.json.JSONObject;

public abstract class BaseActivity extends FragmentActivity implements RequestListener {

    ProgressDialog mProgressDialog;
    private AsyncHttpRequest mAppRequest;

    public BaseActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

    }

    protected void showDialog(boolean isShow, String message) {
        if (isShow) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }


    protected synchronized void requestTypeGET(String url, int requestCode) {
        if (Utility.isConnectedToInternet(this)) {
            mAppRequest = new AsyncHttpRequest(this, url, null, requestCode, AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(this);
            mAppRequest.execute();
        } else {
            dismissDialog();
            showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));

        }

    }

    protected synchronized void requestTypePost(String url, JSONObject jsonobject, int requestCode) {
        mAppRequest = new AsyncHttpRequest(this, url, jsonobject.toString(), requestCode, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        mProgressDialog.dismiss();
        vLayout(response, requestCode);

    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        try {
            mProgressDialog.dismiss();
            rError(requestCode);
        }
        catch (Exception E){

        }
        //showErrorAlert("Error", getString(R.string.async_task_error_timeout));
    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mAppRequest != null)
            if (mAppRequest.getStatus() != AsyncTask.Status.FINISHED)
                mAppRequest.cancel(true);

        super.onDestroy();
    }

    protected void showErrorAlert(String title, String message) {
        AlertDialogErrorFragment fragment = AlertDialogErrorFragment.newInstance(title, message);
        fragment.show(getSupportFragmentManager(), "");

    }


    @Override
    public void onStop() {
        super.onStop();

    }


    public abstract void vLayout(String res, int requestcode);

    public abstract void rError(int requestcode);
}
