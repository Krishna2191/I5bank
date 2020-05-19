package com.example.i5banauth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    // Table Name
    public static final String TABLE_NAME = "Banks";

    // Table columns
    public static final String SNO = "_id";
    public static final String BANK = "BANKNM";

    // Database Information
    static final String DB_NAME = "i5bankauth";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME +
            "( "+SNO+" INTEGER PRIMARY KEY AUTOINCREMENT ," +BANK+ " VARCHAR(225));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
