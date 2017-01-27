package com.locatedamini.loda;

/**
 * Created by Priyanka Kargupta on 7/17/2014.
 */
public class Contact {
//contact values that are taken from ContactsActivity and inputted both into the ContactsListActivity (displayed in the list with these values)
    //and the DatabaseHandler class so that the contacts can be stored
    private String _name, _phone;
    private int _id;
    public Contact (int id, String name, String phone){
        _id = id;
        _name = name;
        _phone = phone;
    }

    public int getId (){
        return _id;
    }
    public String getName(){
        return _name;
    }
    public String getPhone(){
        return _phone;
    }
}
