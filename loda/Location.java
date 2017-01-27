package com.locatedamini.loda;

/**
 * Created by Priyanka Kargupta on 8/15/2014.
 */
public class Location {
//*IMPORTANT* ignore this class
    private String _address, _latitude, _longitude;
    private int _id;
    public Location (int id, String address, String latitude, String longitude){
        _id = id;
        _address = address;
        _latitude = latitude;
        _longitude = longitude;
    }
    public int getId (){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getLatitude(){
        return _latitude;
    }
    public String getLongitude(){
        return _longitude;
    }
}
