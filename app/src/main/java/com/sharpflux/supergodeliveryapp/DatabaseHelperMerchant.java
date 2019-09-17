package com.sharpflux.supergodeliveryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar Hatikat on 10 September 2019
 */
public class DatabaseHelperMerchant extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SuperGoDeliveryMerchnat.db";
    public static final String TABLE_NAME = "MerchantsOrder";
    public static final String ID = "ID";
    public static final String ITEMID = "ITEMID";
    public static final String ITEMNAME = "ITEMNAME";
    public static final String QUANTITY = "QUANTITY";
    public static final String PRICE = "PRICE";
    public static final String IMAGEURL = "IMAGEURL";

    public DatabaseHelperMerchant(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,ITEMID INTEGER,ITEMNAME TEXT,QUANTITY TEXT," +
                " PRICE TEXT,IMAGEURL TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean OrderInsert(Integer itemId,String ItemName, String quantity,Double price,String ImageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMID,itemId);
        contentValues.put(ITEMNAME,ItemName);
        contentValues.put(QUANTITY,quantity);
        contentValues.put(PRICE,price);
        contentValues.put(IMAGEURL,ImageUrl);
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

    public boolean UpdateOrder(Integer itemId,String ItemName, String quantity,Double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMID,itemId);
        contentValues.put(ITEMNAME,ItemName);
        contentValues.put(QUANTITY,quantity);
        contentValues.put(PRICE,price);
        db.update(TABLE_NAME, contentValues, "ITEMID = ?",new String[] { String.valueOf( itemId) });
        return true;
    }

    public boolean UpdateQty(Integer itemId, String quantity ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMID,itemId);
        contentValues.put(QUANTITY,quantity);
        db.update(TABLE_NAME, contentValues, "ITEMID = ?",new String[] { String.valueOf( itemId) });
        return true;
    }

    public boolean CheckItemIsExists(String ItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + ITEMID + " = " + "" + ItemId + "";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }

    public Cursor GetCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME  ,null);
        return res;
    }

    public Integer DeleteRecord (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ITEMID = ?",new String[] {id});
    }

    public String GETExist(String itemId){
        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME +"WHERE ITEMID", null);
        String query = "SELECT ITEMID FROM " + TABLE_NAME + " WHERE ITEMID = ?";
        Cursor cursor =  db.rawQuery(query, new String[] {itemId});
        String Id="0";
        if(cursor.moveToFirst()){
            Id = cursor.getString(0);
        }
        return  Id;
    }
}