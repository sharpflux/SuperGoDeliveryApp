package com.sharpflux.supergodeliveryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbAddress extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Address.db";
    public static final String TABLE_NAME = "Address";
    public static final String NAME = "Name";
    public static final String MOBNO = "MobileNo";
    public static final String ADDRESS = "Address";
    public static final String AREA = "Area";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String COUNTRY = "Country";
    public static final String PINCODE = "Pincode";
    public static final String ADDRESS_TYPE = "AddressType";
    public static final String LAT = "Lat";
    public static final String LONG = "Long";


    public dbAddress(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (Name text, Mobileno text , address text,area text, city text, state text, " +
                "country text, pincode text, addresstype text,Lat Text,Long Text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean AddressInsert(String fullname, String mobNo, String address, String area,
                                 String city, String state, String country, String pincode, String addresstype, String Lat, String Long) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,fullname);
        contentValues.put(MOBNO,mobNo);
        contentValues.put(ADDRESS,address);
        contentValues.put(AREA,area);
        contentValues.put(CITY,city);
        contentValues.put(STATE,state);
        contentValues.put(COUNTRY,country);
        contentValues.put(PINCODE,pincode);
        contentValues.put(ADDRESS_TYPE,addresstype);
        contentValues.put(LAT,Lat);
        contentValues.put(LONG,Long);
        long result = db.insertWithOnConflict(TABLE_NAME,null ,contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean CheckAddressIsExit() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query ="Select * from " + TABLE_NAME;
        // String Query = "Select * from " + TABLE_NAME + " where " + ADDRESS_TYPE + " = " + "" + addresstype + "";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }

    public Cursor GetAddress() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
}
