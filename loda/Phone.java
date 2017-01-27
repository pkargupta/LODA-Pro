package com.locatedamini.loda;

/**
 * Created by Priyanka Kargupta on 11/26/2014.
 */
public class Phone{

    private String _phone;
    private int _id;
    public Phone (int id,String phone){
        _id = id;
        _phone = phone;
    }

    public int getId (){
        return _id;
    }
    public String getPhone(){
        return _phone;
    }
}
