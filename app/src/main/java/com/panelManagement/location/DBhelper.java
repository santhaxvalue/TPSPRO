package com.panelManagement.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Centura User1 on 29-05-2017.
 */

public class DBhelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Location.db";
    public static final String LOCATION_TABLE_NAME = "Location";
    public static final String LOCATION_COLUMN_TIME = "time";
    public static final String LOCATION_COLUMN_LATITUDE = "latitude";
    public static final String LOCATION_COLUMN_LONGITUDE = "longitude";
    private HashMap hp;


    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtable = "create table IF NOT EXISTS "
                + LOCATION_TABLE_NAME + " ( " +
                LOCATION_COLUMN_TIME + " TEXT, " +
                LOCATION_COLUMN_LATITUDE + " TEXT, " +
                LOCATION_COLUMN_LONGITUDE + " TEXT " + " )";
        db.execSQL(createtable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
        onCreate(db);
    }

    public void cleardb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATION_TABLE_NAME, null, null);
        db.close();
    }

    public void addlocation(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCATION_COLUMN_TIME, locationModel.getDate());
        values.put(LOCATION_COLUMN_LATITUDE, locationModel.getLatitude());
        values.put(LOCATION_COLUMN_LONGITUDE, locationModel.getLongitude());

        db.insert(LOCATION_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<LocationModel> getAllLocation() {
        ArrayList<LocationModel> Location_list = new ArrayList<LocationModel>();

        //hp = new HashMap();
        String selectQuery = "SELECT * From " + LOCATION_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            LocationModel model = new LocationModel();
            model.setDate(res.getString(0));
            model.setLatitude(res.getString(1));
            model.setLongitude(res.getString(2));
            Location_list.add(model);
            // array_list.add(""+(res.getString(res.getColumnIndex(LOCATION_COLUMN_TIME)))+","+(res.getString(res.getColumnIndex(LOCATION_COLUMN_LATITUDE)))+","+(res.getString(res.getColumnIndex(LOCATION_COLUMN_LONGITUDE)))+"\n");
            res.moveToNext();
        }
        return Location_list;
    }

    public void Clearlocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LOCATION_TABLE_NAME, null, null);
        db.close();

    }
}
