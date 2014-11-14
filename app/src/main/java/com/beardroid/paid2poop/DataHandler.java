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
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    //fields
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_RATING = "rating";

    //field numbers
    public static final int COL_AMOUNT = 1;
    public static final int COL_DATE = 2;
    public static final int COL_TIME = 3;
    public static final int COL_RATING = 4;

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_AMOUNT, KEY_DATE, KEY_TIME, KEY_RATING};


    public static final String TABLE_NAME = "mainTable";
    public static final String DATABASE_NAME = "PoopDB";
    public static final int DATABASE_VERSION = 5;
    public static final String TABLE_CREATE =
            "create table if not exists " + TABLE_NAME + " ("
                    + KEY_ROWID + " integer primary key autoincrement,"
                    + KEY_AMOUNT + " real not null,"
                    + KEY_DATE + " text not null,"
                    + KEY_TIME + " text not null,"
                    + KEY_RATING + " text not null);";


    private DataBaseHelper myDBHelper;
    private final Context context;
    private SQLiteDatabase db;

    public DataHandler(Context context) {
        this.context = context;
        myDBHelper = new DataBaseHelper(context);

    }

    public DataHandler open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    public long insertData(Double amount, String date, String time, String rating) {
        ContentValues content = new ContentValues();
        content.put(KEY_AMOUNT, amount);
        content.put(KEY_DATE, date);
        content.put(KEY_TIME, time);
        content.put(KEY_RATING, rating);
        return db.insert(TABLE_NAME, null, content);
    }

    public boolean deleterRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(TABLE_NAME, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleterRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
    }

    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, TABLE_NAME, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean updateRow(long rowId, Double amount, String date, String time, String rating) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues content = new ContentValues();
        content.put(KEY_AMOUNT, amount);
        content.put(KEY_DATE, date);
        content.put(KEY_TIME, time);
        content.put(KEY_RATING, rating);

        return db.update(TABLE_NAME, content, where, null) != 0;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}