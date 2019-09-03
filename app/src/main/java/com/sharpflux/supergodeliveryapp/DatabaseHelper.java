package com.sharpflux.supergodeliveryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar Hatikat on 25/072019
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SuperGoDelivery.db";
    public static final String TABLE_NAME = "delivery_request";
    public static final String ID = "ID";
    public static final String PICKUPADDRESS = "PICKUPADDRESS";
    public static final String FROMLAT = "FROMLAT";
    public static final String FROMLONG = "FROMLONG";

    public static final String DELIVERYADDRESS = "DELIVERYADDRESS";
    public static final String TOLAT = "TOLAT";
    public static final String TOLONG = "TOLONG";

    public static final String VEHICLETYPE = "VEHICLETYPE";
    public static final String ORDERDETAILS = "ORDERDETAILS";
    public static final String IMAGESTRING = "IMAGESTRING";

    public static final String PICKUPDATE = "PICKUPDATE";
    public static final String PICKUPTIME = "PICKUPTIME";
    public static final String CONTACTPERSON = "CONTACTPERSON";
    public static final String RECIEVERNUMBER = "RECIEVERNUMBER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,PICKUPADDRESS TEXT,FROMLAT TEXT, FROMLONG TEXT,DELIVERYADDRESS TEXT,TOLAT TEXT,TOLONG TEXT,VEHICLETYPE TEXT,ORDERDETAILS TEXT,IMAGESTRING TEXT,PICKUPDATE TEXT,PICKUPTIME TEXT,CONTACTPERSON TEXT,RECIEVERNUMBER TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean InsertDelivery(String pickupaddress, String fromlat, String fromlong, String DeliveryAddress, String toLat, String toLong,
                                  String Vehicletype,String OrderDetails,String Images,String Pickupdate,String PickupTIme,String ContactPerson,String ReciverNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PICKUPADDRESS,pickupaddress);
        contentValues.put(FROMLAT,fromlat);
        contentValues.put(FROMLONG,fromlong);

        contentValues.put(DELIVERYADDRESS,DeliveryAddress);
        contentValues.put(TOLAT,toLat);
        contentValues.put(TOLONG,toLong);

        contentValues.put(VEHICLETYPE,Vehicletype);
        contentValues.put(ORDERDETAILS,OrderDetails);
        contentValues.put(IMAGESTRING,Images);

        contentValues.put(PICKUPDATE,Pickupdate);
        contentValues.put(PICKUPTIME,PickupTIme);
        contentValues.put(CONTACTPERSON,ContactPerson);
        contentValues.put(RECIEVERNUMBER,ReciverNo);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor DeliveryGETById(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+ " where " + ID + "='" + Id + "'" ,null);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }


    public boolean UpdatePickupAddress(String id,String pickupaddress, String fromlat, String fromlong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(PICKUPADDRESS,pickupaddress);
        contentValues.put(FROMLAT,fromlat);
        contentValues.put(FROMLONG,fromlong);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public boolean UpdateDeliveryAddress(String id,String DeliveryAddress, String toLat, String toLong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(DELIVERYADDRESS,DeliveryAddress);
        contentValues.put(TOLAT,toLat);
        contentValues.put(TOLONG,toLong);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }
    public boolean UpdateOtherInfo(String id,String Vehicletype,String OrderDetails,String Images,String Pickupdate,String PickupTIme,String ContactPerson,String ReciverNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(VEHICLETYPE,Vehicletype);
        contentValues.put(ORDERDETAILS,OrderDetails);
        contentValues.put(IMAGESTRING,Images);
        contentValues.put(PICKUPDATE,Pickupdate);
        contentValues.put(PICKUPTIME,PickupTIme);
        contentValues.put(CONTACTPERSON,ContactPerson);
        contentValues.put(RECIEVERNUMBER,ReciverNo);

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public boolean UpdateDelivery(String id,String pickupaddress, String fromlat, String fromlong, String DeliveryAddress, String toLat, String toLong,
                              String Vehicletype,String OrderDetails,String Images,String Pickupdate,String PickupTIme,String ContactPerson,String ReciverNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(PICKUPADDRESS,pickupaddress);
        contentValues.put(FROMLAT,fromlat);
        contentValues.put(FROMLONG,fromlong);

        contentValues.put(DELIVERYADDRESS,DeliveryAddress);
        contentValues.put(TOLAT,toLat);
        contentValues.put(TOLONG,toLong);

        contentValues.put(VEHICLETYPE,Vehicletype);
        contentValues.put(ORDERDETAILS,OrderDetails);
        contentValues.put(IMAGESTRING,Images);
        contentValues.put(PICKUPDATE,Pickupdate);
        contentValues.put(PICKUPTIME,PickupTIme);
        contentValues.put(CONTACTPERSON,ContactPerson);
        contentValues.put(RECIEVERNUMBER,ReciverNo);

        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer DeleteRecord (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public String GetLastId(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);
        String Id="0";
        if(cursor.moveToLast()){
            Id = cursor.getString(0);
            //--get other cols values
        }
        return  Id;
    }
}