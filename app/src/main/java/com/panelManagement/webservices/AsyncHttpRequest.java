package com.panelManagement.webservices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SignUpActivity;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Helps to make Http GET and Http POST request with server
 *
 * @author Manoj Prasad
 */
public class AsyncHttpRequest extends AsyncTask<Void, Void, Void> {

    static String TAG = "AsyncHttpRequest";
    DefaultHttpClient mHttpClient = null;
    HttpGet mGet = null;
    HttpPost mPost = null;
    String mResponse = null;
    Exception mError = null;
    String mUrl = "";
    Context mContext = null;
    Bundle mParams = null;
    int mRequestCode = -1;
    Type mType = Type.GET;
    boolean mRunning = false;
    RequestListener mRequestListener = null;
    String jsonObject;

    public AsyncHttpRequest() {
    }

    public AsyncHttpRequest(Context context, String url, String jsonobject, int requestCode, Type type) {
        this.mContext = context;
        this.mRequestCode = requestCode;
        this.mType = type;
        this.mUrl = url;
        this.jsonObject = jsonobject;
    }

    public AsyncHttpRequest(Context context, String url, int requestCode, Bundle bundle, Type type) {
        this.mContext = context;
        this.mRequestCode = requestCode;
        this.mType = type;
        this.mUrl = url;
        this.mParams = bundle;
    }

    public static boolean isConnected(final Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public void setRequestListener(RequestListener listener) {
        if (listener != null)
            this.mRequestListener = listener;
    }

    @Override
    protected void onCancelled() {
        if (mType == Type.GET && mGet != null)
            mGet.abort();
        else if (mType == Type.POST && mPost != null)
            mPost.abort();
        mParams = null;
        mPost = null;
        mGet = null;
        mError = null;
        if (mRequestListener != null)
            mRequestListener.onRequestError(new Exception(mContext.getString(R.string.async_task_error)), mRequestCode);
        mRequestListener = null;
        mRunning = false;
        super.onCancelled();
    }

    public boolean isRunning() {
        return mRunning;
    }

    @Override
    protected Void doInBackground(Void... p) {
        if (AsyncHttpRequest.isConnected(mContext)) {
            mRunning = true;
            try {
                if (mType == Type.GET) {
                    if (!TextUtils.isEmpty(mUrl) && !mUrl.contains("LanguageCulture"))
                        if (mUrl.contains("?")) {
                            this.mUrl = new StringBuffer(mUrl).append("&LanguageCulture=" + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE)).toString();
                        } else {
                            this.mUrl = new StringBuffer(mUrl).append("?LanguageCulture=" + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE)).toString();
                        }
                    mResponse = doHttpGetRequest(mUrl, mParams);
                } else if (mType == Type.POST)
                    mResponse = doHttpPostRequest(mUrl, mParams);
                System.out.println(mResponse);
            } catch (UnknownHostException e) {
                mError = new Exception("UnknownHostException");
            } catch (Exception e) {
                e.printStackTrace();
                mError = e;
            }
        } else {
            mError = new Exception(mContext.getString(R.string.async_task_error));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mHandler.sendEmptyMessage(0);
        mRunning = false;
        if (mError != null && mRequestListener != null) {
            mRequestListener.onRequestError(mError, mRequestCode);
        } else if (mResponse != null && mRequestListener != null) {
            mRequestListener.onRequestCompleted(mResponse, mRequestCode);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @SuppressWarnings("deprecation")
    private String doHttpGetRequest(String url, Bundle params) throws Exception {
        String response = null;
        mHttpClient = new DefaultHttpClient();
        mHttpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
        if (params != null) {
            String paramsString = "";
            Set<String> parameters = params.keySet();
            Iterator<String> it = parameters.iterator();
            while (it.hasNext()) {
                String key = it.next().trim();
                if (params.containsKey(key)) {
                    if (key.startsWith("<") && key.endsWith(">"))
                        paramsString = paramsString + "&" + key.replace("<", "").replace(">", "") + "=" + params.getString(key);
                    else
                        paramsString = paramsString + "&" + key + "=" + URLEncoder.encode(params.getString(key));
                }
            }
            url = url + "?" + paramsString;
        }
        mGet = new HttpGet(url);
        mGet.setHeader("Content-Type", "application/json");
        mGet.setHeader("LanguageCulture", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE));

        Log.d("languageculture:1","languageculture:1"+url);
        Log.d("languageculture:11","languageculture:11"+InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE));

        HttpParams httpParams = mHttpClient.getParams();
        httpParams.setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
        int timeoutConnection = 80000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 120000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        Log.d(TAG, Arrays.toString(mGet.getAllHeaders()));
        HttpResponse resp = mHttpClient.execute(mGet);
        if (resp.getStatusLine().getStatusCode() == 204) {
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.alert).setMessage("R")
                    .setPositiveButton("Exit", (dialog, which) -> {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(mContext, SignUpActivity.class);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();

                    })
                    .setCancelable(false)
                    .show();

        }
        if (resp.getStatusLine().getStatusCode() != 200)
            throw new Exception(String.format((mContext.getString(R.string.async_task_not_responding)), resp.getStatusLine().getStatusCode()));
        InputStream inStream = resp.getEntity().getContent();
        response = convertStreamToString(inStream);
        inStream.close();
        return response;
    }

    @Override
    protected void onPreExecute() {
        if (mRequestListener != null)
            mRequestListener.onRequestStarted(mRequestCode);
        super.onPreExecute();
    }


    @SuppressLint("StringFormatInvalid")
    @SuppressWarnings("deprecation")
    private String doHttpPostRequest(String url, Bundle params) throws Exception {
        String response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null) {
            Set<String> parameters = params.keySet();
            Iterator<String> it = parameters.iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (params.containsKey(key)) {
                    nameValuePairs.add(new BasicNameValuePair(key, params.getString(key)));
                }
            }
        }
        mHttpClient = new DefaultHttpClient();
        mHttpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));
        HttpParams httpParams = mHttpClient.getParams();
        int timeoutConnection = 80000;
        HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
        int timeoutSocket = 60000;
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
        mPost = new HttpPost(url);
        StringEntity s;
        if (jsonObject != null && !jsonObject.contains("LanguageCulture")) {
            JSONObject jsonObjectNew = new JSONObject(jsonObject);
            jsonObjectNew.put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE));
            s = new StringEntity(jsonObjectNew.toString(), HTTP.UTF_8);
        } else if (jsonObject == null) {
            JSONObject jsonObjectNew = new JSONObject();
            jsonObjectNew.put("LanguageCulture", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE));
            s = new StringEntity(jsonObjectNew.toString(), HTTP.UTF_8);
        } else {
            s = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
        }

        mPost.setHeader("Content-Type", "application/json; charset=utf-8");

        mPost.setHeader("UserAgent", "	");
        mPost.setHeader("LanguageCulture", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_LOCALECODE));
        mPost.setEntity(s);
        Log.e("TPS_HTTP", "request ");
        HttpResponse resp = mHttpClient.execute(mPost);
        Log.d(TAG, Arrays.toString(mPost.getAllHeaders()));
        if (resp.getStatusLine().getStatusCode() != 200)
            throw new Exception(String.format((mContext.getString(R.string.async_task_not_responding)), resp.getStatusLine().getStatusCode()));
        InputStream inStream = resp.getEntity().getContent();
        response = convertStreamToString(inStream);
        inStream.close();

        return response;
    }

    private void callUnScribe() {

        new AlertDialog.Builder(mContext)
                .setTitle(R.string.alert).setMessage("R")
                .setPositiveButton("Exit", (dialog, which) -> {
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(mContext, SignUpActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();

                })
                .setCancelable(false)
                .show();
    }


    private String convertStreamToString(InputStream instream) {
        String response = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response = sb.toString();
        return response.trim();
    }

    public enum Type {
        POST, GET, POST_WITH_FILE
    }

    public interface RequestListener {
        void onRequestCompleted(String response, int requestCode);

        void onRequestError(Exception e, int requestCode);

        void onRequestStarted(int requestCode);
    }
}
