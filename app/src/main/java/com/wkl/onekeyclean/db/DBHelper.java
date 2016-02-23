package com.wkl.onekeyclean.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "clean.db";
    public static final String TABLE_CALL = "call";
    public static final String PHONE_NUM = "phone_num";
    public static final String TIME = "time";
    public static final String READ = "read";

    public static final String SQL = "create table " + TABLE_CALL +
            "(_id integer primary key autoincrement, "+PHONE_NUM+" text, "+TIME+" text, "+READ+" integer)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
