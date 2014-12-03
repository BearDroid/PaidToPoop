package com.beardroid.paid2poop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Max on 11/13/2014.
 */
public class DataHandler extends SQLiteOpenHelper {
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    //fields
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_RATING = "rating";
    public static final String KEY_AMOUNT_STR = "amountStr";
    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_AMOUNT, KEY_DATE, KEY_TIME, KEY_RATING, KEY_AMOUNT_STR};
    public static final String[] AMOUNT_STRING_ARRAY = new String[]{KEY_AMOUNT_STR};
    //field numbers
    public static final int COL_AMOUNT = 1;
    public static final int COL_DATE = 2;
    public static final int COL_TIME = 3;
    public static final int COL_RATING = 4;
    public static final int COL_RATING_STR = 5;
    public static final String TABLE_NAME = "mainTable";
    public static final String TABLE_CREATE =
            "create table if not exists " + TABLE_NAME + " ("
                    + KEY_ROWID + " integer primary key autoincrement,"
                    + KEY_AMOUNT + " real not null,"
                    + KEY_DATE + " text not null,"
                    + KEY_TIME + " text not null,"
                    + KEY_RATING + " text not null,"
                    + KEY_AMOUNT_STR + " text not null);";
    public static final String DATABASE_NAME = "PoopDB";
    public static final int DATABASE_VERSION = 17;
    //order by:
    public static final String ORDER_BY = "_id DESC";
    // For logging:
    private static final String TAG = "DBAdapter";
    private SQLiteDatabase db;

    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public List<PoopCard> getPoopCardInfo() {
        List<PoopCard> poopCardList = new ArrayList<PoopCard>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PoopCard poopCard = new PoopCard();
                poopCard.setId(cursor.getLong(0));
                poopCard.setAmount(cursor.getString(1));
                poopCard.setDate(cursor.getString(2));
                poopCard.setTime(cursor.getString(3));
                poopCard.setRating(cursor.getString(4));
                poopCardList.add(poopCard);
            } while (cursor.moveToNext());
        }
        Collections.reverse(poopCardList);
        return poopCardList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

        db.execSQL("DROP TABLE IF EXISTS PoopTable ");
        onCreate(db);
    }

    public DataHandler open() {
        db = getWritableDatabase();
        return this;
    }

    public void close() {
        close();
    }

    public long insertData(Double amount, String date, String time, String rating, String amountStr) {
        ContentValues content = new ContentValues();
        content.put(KEY_AMOUNT, amount);
        content.put(KEY_DATE, date);
        content.put(KEY_TIME, time);
        content.put(KEY_RATING, rating);
        content.put(KEY_AMOUNT_STR, amountStr);
        return db.insert(TABLE_NAME, null, content);
    }

    public boolean deleteRow(long rowId) {
        return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
    }

    public Cursor getAllRows() {
        Cursor c = db.query(true, TABLE_NAME, ALL_KEYS, null, null, null, null, ORDER_BY, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor totalAmount() {
        String where = null;
        Cursor c = db.rawQuery("SELECT Sum(amount) AS myTotal FROM " + TABLE_NAME, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateRow(long rowId, Double amount, String date, String time, String rating, String amountStr) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues content = new ContentValues();
        content.put(KEY_AMOUNT, amount);
        content.put(KEY_DATE, date);
        content.put(KEY_TIME, time);
        content.put(KEY_RATING, rating);
        content.put(KEY_AMOUNT_STR, amountStr);

        return db.update(TABLE_NAME, content, where, null) != 0;
    }

}