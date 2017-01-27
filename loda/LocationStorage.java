package com.locatedamini.loda;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by Priyanka Kargupta on 8/15/2014.
 */
public class LocationStorage extends SQLiteOpenHelper {
//*IMPORTANT* you can ignore this class too. This was just added during the development of the remembered location feature.

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationStorage",
            TABLE_LOCATION = "locationManager",
            KEY_ID = "id",
            KEY_ADDRESS= "address",
            KEY_LATITUDE= "latitude",
            KEY_LONGITUDE= "longitude";


    public LocationStorage(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_LOCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ADDRESS + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }
    public int getLocationCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATION, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;

    }
    public void storeRem(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_LOCATION,null,null);
        ContentValues values = new ContentValues();

       // values.put(KEY_ADDRESS,);
        //values.put(KEY_LATITUDE, contact.getPhone());
        //values.put(KEY_LONGITUDE, contact.getPhone());

        db.close();

    }
}
