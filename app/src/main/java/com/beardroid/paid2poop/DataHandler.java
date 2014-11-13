package com.beardroid.paid2poop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Max on 11/13/2014.
 */
public class DataHandler {
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String RATING = "rating";
    public static final String COLUMN_ID = "_id";
    public static final String TABLE_NAME = "PoopTable";
    public static final String DATABASE_NAME = "PoopDataBase";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_CREATE = "create table if not exists " + TABLE_NAME + " ("
            + COLUMN_ID + " integer primary key autoincrement,"
            + AMOUNT + " real not null,"
            + DATE + " text not null,"
            + TIME + " text not null,"
            + RATING + " text not null);";


    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;

    public DataHandler(Context ctx) {
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);

    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
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
    }

    public DataHandler open() {
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
    }

    public long insertData(Double amount, String date, String time, String rating) {
        ContentValues content = new ContentValues();
        content.put(AMOUNT, amount);
        content.put(DATE, date);
        content.put(TIME, time);
        content.put(RATING, rating);
        return db.insertOrThrow(TABLE_NAME, null, content);
    }

    public Cursor returnData() {
        return db.query(TABLE_NAME, new String[]{AMOUNT, DATE, TIME, RATING}, null, null, null, null, null);
    }
}