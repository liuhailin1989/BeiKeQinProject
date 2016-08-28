package com.android.backchina.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public final static int DB_VERSION = 1;
    public static final String DATABASE_NAME = "backchina";

    public static final String TABLE_NAME_CHANNEL = "channel";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "
                + TABLE_NAME_CHANNEL
                + " (_id integer primary key autoincrement,"
                + " typeid integer, name varchar(10), url varchar(50), urlapi varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}