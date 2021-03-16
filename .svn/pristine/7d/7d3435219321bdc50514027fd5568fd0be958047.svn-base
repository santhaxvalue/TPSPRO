package com.panelManagement.location;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.panelManagement.utils.Constants;
import com.panelManagement.utils.LocationService;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("Registered")
public class AppLocationService extends Service {
    public static final int notify = 60 * 30 * 1000;  //interval between two services(Here Service run every 30 Minute)
    protected LocationManager locationManager;
    DBhelper db;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;//timer handling
    private boolean isRunning;

    @Override
    public IBinder onBind(Intent arg0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ServiceLog", "Service Started");
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        db = new DBhelper(this);
        Log.d("ServiceEntered", "Service Done");
     //   Toast.makeText(this, "Location Called", Toast.LENGTH_SHORT).show();
        setLocation();
    }

  /*  @Override
    public void onDestroy() {
        super.onDestroy();
//        stopSelf();
        //mTimer.cancel();    //For Cancel Timer
        //Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }*/

    private void setLocation() {

      //   Toast.makeText(AppLocationService.this, "Location refreshed", Toast.LENGTH_SHORT).show();

        DecimalFormat df = new DecimalFormat("##.#######");
        LocationModel locationModel = new LocationModel();
        if (locationManager == null) {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        }
        LocationService locationService = new LocationService(getBaseContext());
        Location location30 = locationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (location30 != null) {
            double latitude = location30.getLatitude();
            double longitude = location30.getLongitude();
            locationModel.setDate(GenericData.getCurrentTime());
            //locationModel.setTime(CurrentTime[1]);
            locationModel.setLatitude(df.format(latitude));
            locationModel.setLongitude(df.format(longitude));
            Constants.locationcurrent = locationModel;
            db.addlocation(locationModel);
                           /* Toast.makeText(
                                    getApplicationContext(),
                                    "Mobile Location (GPS): \nLatitude: " + latitude
                                            + "\nLongitude: " + longitude,
                                    Toast.LENGTH_LONG).show();*/
        } else {
            Location location = locationService.getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //locationModel.setTime(CurrentTime[1]);
                locationModel.setDate(GenericData.getCurrentTime());
                locationModel.setLatitude(df.format(latitude));
                locationModel.setLongitude(df.format(longitude));
                Constants.locationcurrent = locationModel;
                db.addlocation(locationModel);
                           /* Toast.makeText(
                                    getApplicationContext(),
                                    "Mobile Location1 (GPS): \nLatitude1: " + latitude
                                            + "\nLongitude1: " + longitude,
                                    Toast.LENGTH_LONG).show();*/
            }

        }

//        stopSelf();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(AppLocationService.this, "Location refreshed", Toast.LENGTH_SHORT).show();


                    DecimalFormat df = new DecimalFormat("##.###");
                    LocationModel locationModel = new LocationModel();
                    if (locationManager == null) {
                        locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
                    }
                    LocationService locationService = new LocationService(getBaseContext());
                    Location location30 = locationService.getLocation(LocationManager.NETWORK_PROVIDER);
                    if (location30 != null) {
                        double latitude = location30.getLatitude();
                        double longitude = location30.getLongitude();
                        locationModel.setDate(GenericData.getCurrentTime());
                        //locationModel.setTime(CurrentTime[1]);
                        locationModel.setLatitude(df.format(latitude));
                        locationModel.setLongitude(df.format(longitude));
                        Constants.locationcurrent = locationModel;
                        db.addlocation(locationModel);
                            /*Toast.makeText(
                                    getApplicationContext(),
                                    "Mobile Location (GPS): \nLatitude: " + latitude
                                            + "\nLongitude: " + longitude,
                                    Toast.LENGTH_LONG).show();*/
                    } else {
                        Location location = locationService.getLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            //locationModel.setTime(CurrentTime[1]);
                            locationModel.setDate(GenericData.getCurrentTime());
                            locationModel.setLatitude(df.format(latitude));
                            locationModel.setLongitude(df.format(longitude));
                            Constants.locationcurrent = locationModel;
                            db.addlocation(locationModel);
                            /*Toast.makeText(
                                    getApplicationContext(),
                                    "Mobile Location (GPS): \nLatitude: " + latitude
                                            + "\nLongitude: " + longitude,
                                    Toast.LENGTH_LONG).show();*/
                        }
                    }


                    //locationManager = (LocationManager) getBaseContext().getSystemService(LOCATION_SERVICE);
                    /*AppLocationService locationService = new AppLocationService(getBaseContext());
                    Location location30 = locationService.getLocation(LocationManager.NETWORK_PROVIDER);
                    if (location30 != null) {
                        double latitude = location30.getLatitude();
                        double longitude = location30.getLongitude();
                        db.addlocation(getCurrentTime(), latitude + "", longitude + "");
                        Toast.makeText(
                                getApplicationContext(),
                                "Mobile Location (GPS): \nLatitude: " + latitude
                                        + "\nLongitude: " + longitude,
                                Toast.LENGTH_LONG).show();
                    }*/
                    // onLocationChanged(location);
                    // display toast
                    //Toast.makeText(AppLocationService.this, "Service is running", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
