package com.panelManagement.utils;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Centura User1 on 01-06-2017.
 */

public class LocationService extends Service implements LocationListener {
    public static final long MIN_DISTANCE_FOR_UPDATE = 10;
    public static final long MIN_TIME_FOR_UPDATE = 3000 /** 30*/
            ;
    protected LocationManager mlocationManager;
    Location mlocation;
    LocationListener locationListener;
    Context mContext;

    public LocationService(Context context) {
        this.mContext = context;
        mlocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        addListenerLocation(context);
    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public Location getLocation(String provider) {
//       Toast.makeText(mContext, "Location Update", Toast.LENGTH_SHORT).show();
        if (mlocationManager.isProviderEnabled(provider)) {
            // mlocationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (mlocationManager != null) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is  granted
                    mlocation = mlocationManager.getLastKnownLocation(provider);
                }
                return mlocation;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {


        // getLocation(mlocationManager.NETWORK_PROVIDER);

        //   mlocationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        getLocation(s);
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void addListenerLocation(Context mContext) {
        // mlocationManager = (LocationManager) getSystemService(mContext.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               /* currentLat = location.getLatitude();
                currentLon = location.getLongitude();

                Toast.makeText(getBaseContext(), currentLat + "-" + currentLon, Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

                /*Location lastKnownLocation = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(lastKnownLocation!=null){
                    currentLat = lastKnownLocation.getLatitude();
                    currentLon = lastKnownLocation.getLongitude();
                }*/

                getLocation(provider);

            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, locationListener);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationManager.removeUpdates(locationListener);
    }
}
