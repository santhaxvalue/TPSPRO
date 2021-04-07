package com.panelManagement.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;

import org.json.JSONObject;

public abstract class BaseDialogFragment extends DialogFragment implements RequestListener {
    ProgressDialog mProgressDialog;
    private AsyncHttpRequest mAppRequest;

    public BaseDialogFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());
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

    @Override
    public void onResume() {
        super.onResume();
        // TODO Auto-generated method stub
        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {

                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    dismiss();
                    return true;
                } else
                    return false;
            }
        });
    }

    protected void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: "
                + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    protected synchronized void requestTypeGET(String url, int requestCode) {
        if (Utility.isConnectedToInternet(getActivity())) {
            mAppRequest = new AsyncHttpRequest(getActivity(), url, null,
                    requestCode, AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(this);
            mAppRequest.execute();
        } else {
            dismissDialog();
            showErrorAlert(" ", getString(R.string.msg_low_conn));

        }

    }

    protected synchronized void requestTypePost(String url, JSONObject jsonobject, int requestCode) {
        if (Utility.isConnectedToInternet(getActivity())) {
            mAppRequest = new AsyncHttpRequest(getActivity(), url, jsonobject.toString(), requestCode, AsyncHttpRequest.Type.POST);
            mAppRequest.setRequestListener(this);
            mAppRequest.execute();
        } else {
            dismissDialog();
            showErrorAlert(" ", getString(R.string.msg_low_conn));
        }
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        mProgressDialog.dismiss();
        vLayout(response, requestCode);
    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        mProgressDialog.dismiss();
        Log.d("reqeusterror:1111","requesterror:"+e);
        showErrorAlert(" ", getString(R.string.msg_serverdown));
    }

    @Override
    public void onRequestStarted(int requestCode) {
    }

    @Override
    public void onDestroyView() {
        if (mAppRequest != null)
            if (mAppRequest.getStatus() != AsyncTask.Status.FINISHED)
                mAppRequest.cancel(true);
        super.onDestroyView();

    }

    protected void showErrorAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        if (!TextUtils.isEmpty(title))
            alertDialog.setTitle(title);
        if (!TextUtils.isEmpty(message))
            alertDialog.setMessage(message);
        alertDialog.setButton(Activity.RESULT_OK, getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {

            }
        });
        alertDialog.show();
        TextView titleView = alertDialog.findViewById(getActivity().getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }

        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setGravity(Gravity.CENTER);
        }
    }

    public abstract void vLayout(String res, int requestcode);

    public abstract void rError(int requestcode);
}
