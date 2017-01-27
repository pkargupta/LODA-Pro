package com.locatedamini.loda;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Priyanka Kargupta on 7/18/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactManager",
    TABLE_PHONES = "phonelist",
    TABLE_PHONE = "numberlist",
    TABLE_CONTACTS = "contacts",
    KEY_ID = "id",
    KEY_PHONE= "phone",
    KEY_NAME= "name";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PHONES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PHONE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PHONE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PHONE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT)");
    }

@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONE);
    onCreate(db);
}

    public void createContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        db.insert(TABLE_CONTACTS, null, values);
        ContentValues phone = new ContentValues();
        phone.put(KEY_PHONE, contact.getPhone());
        db.insert(TABLE_PHONE, null, phone);

       // ArrayList<String> phonenum = new ArrayList<String>();
       // phonenum.add(contact.getPhone().toString());
        db.close();
    }
    public ArrayList<String> listofPhones(Contact contact){
    ArrayList<String> phonenum = new ArrayList<String>();
    phonenum.add(contact.getPhone().toString());
    return phonenum;
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {KEY_ID, KEY_NAME, KEY_PHONE}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
if (cursor != null)
    cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        db.close();
        cursor.close();
        return contact;
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String [] { String.valueOf(contact.getId())});
        db.delete(TABLE_PHONE, KEY_ID + "=?", new String[] {String.valueOf(contact.getPhone())});
        ContactsActivity.Phones.remove(String.valueOf(contact.getId()));




        db.close();
    }
    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
        int count = cursor.getCount();

        db.close();
        cursor.close();

        return count;

    }
    public int getPhoneCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHONE, null);
        int count = cursor.getCount();
        return count;
    }



    public int updateContact(Contact contact) {

       //update TABLE_CONTACTS set phone_number='' where id=''

//DatabaseHandler dbHandler = new DatabaseHandler();
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, "");
        //values.put(KEY_PHONE, contact.getPhone());
        int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=" +1 , null);
        //int rowsAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
db.close();
        return rowsAffected;

    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do{

                contacts.add(new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
return contacts;
    }


}
