package com.panelManagement.utils;

import android.Manifest;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.panelManagement.location.DBhelper;
import com.panelManagement.location.GenericData;
import com.panelManagement.location.LocationModel;
import com.panelManagement.webservices.AsyncHttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyJobService extends JobService {
    private static final String TAG = MyJobService.class.getSimpleName();
    private DBhelper db;
    private LocationManager locationManager;
    private Location mLocation;
    private Context mContext;

    @Override
    public boolean onStartJob(JobParameters job) {
        mContext = this;
        db = new DBhelper(mContext);

        //Toast.makeText(mContext, "Job started", Toast.LENGTH_LONG).show();
        setLocation();
        Log.wtf(TAG, job.getTag());
        sendLocationUpdates();
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

    private void setLocation() {
        //   Toast.makeText(mContext, "Location refreshed", Toast.LENGTH_SHORT).show();
        DecimalFormat df = new DecimalFormat("##.###");
        LocationModel locationModel = new LocationModel();
        if (locationManager == null) {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        }
        Location location30 = getLocation(LocationManager.NETWORK_PROVIDER);
        if (location30 != null) {
            double latitude = location30.getLatitude();
            double longitude = location30.getLongitude();
            locationModel.setDate(GenericData.getCurrentTime());
            //locationModel.setTime(CurrentTime[1]);
            locationModel.setLatitude(df.format(latitude));
            locationModel.setLongitude(df.format(longitude));
            Constants.locationcurrent = locationModel;
            db.addlocation(locationModel);
        } else {
            Location location = getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                locationModel.setDate(GenericData.getCurrentTime());
                locationModel.setLatitude(df.format(latitude));
                locationModel.setLongitude(df.format(longitude));
                Constants.locationcurrent = locationModel;
                db.addlocation(locationModel);
            }
        }
    }

    public Location getLocation(String provider) {
        if (locationManager != null) {
            if (locationManager.isProviderEnabled(provider)) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is  granted
                    mLocation = locationManager.getLastKnownLocation(provider);
                } else {
                    Log.wtf(TAG, "Permission is not granted - location");
                }
                return mLocation;
            }
        }
        return null;
    }

    private void sendLocationUpdates() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(mContext, Constants.PREF_ID));
            ArrayList<LocationModel> locationModels;
            locationModels = db.getAllLocation();
            JSONArray jsonArray = new JSONArray(new Gson().toJson(locationModels));
            json.put("Locations", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(mContext,
                Constants.API_GEOLOCATION, json.toString(), Constants.REQUESTCODE_GEOLOCATIONSAVE, AsyncHttpRequest.Type.POST);
        mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("Status"))
                        db.cleardb();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestError(Exception e, int requestCode) {
                Log.wtf(TAG, "error with requestCode: " + requestCode, e);
            }

            @Override
            public void onRequestStarted(int requestCode) {

            }
        });
        mAppRequest.execute();
    }
}