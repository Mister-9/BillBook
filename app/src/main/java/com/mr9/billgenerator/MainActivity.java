package com.mr9.billgenerator;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            DatabaseContract.InitialBillTable.TABLE_NAME + " (" +
            DatabaseContract.InitialBillTable._ID + " INTEGER PRIMARY KEY," +
            DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " INTEGER," +
            DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant + " INTEGER," +
            DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " FLOAT)";
    static String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.InitialBillTable.TABLE_NAME;
    static class BillGenDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "BillGen.db";

        public BillGenDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Intent intent=new Intent(MainActivity.this,NewBillActivity.class);

        startActivity(intent);

    }

}
