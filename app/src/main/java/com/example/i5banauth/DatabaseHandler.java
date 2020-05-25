
package com.example.i5banauth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;



public class DatabaseHandler  {

    myDbHelper helper;


    public DatabaseHandler(Context context)
    {

        helper = new myDbHelper(context);

    }

    public long insertData(String name)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.BANK, name);
        long id = db.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public int deleteData(int SNO)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int id=db.delete(myDbHelper.TABLE_NAME, myDbHelper.SNO+"='"+SNO+"'",null);
        return id;
    }

    public Cursor fetchData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = new String[] { myDbHelper.SNO, myDbHelper.BANK };
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        public static final String DATABASE_NAME = "i5bankauth";
        public static final String TABLE_NAME = "Banks";
        public static final int DATABASE_Version = 1;
        public static final String SNO="_id";
        public static final String BANK= "BANKNM";

        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME +
                "( "+SNO+" INTEGER PRIMARY KEY AUTOINCREMENT ," +BANK+ " VARCHAR(225));";
        // private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
            //Message.message(context,"Started...");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {

                db.execSQL(CREATE_TABLE);
               // Message.message(context,"TABLE CREATED");
                Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "exption", Toast.LENGTH_SHORT).show();
                //Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
