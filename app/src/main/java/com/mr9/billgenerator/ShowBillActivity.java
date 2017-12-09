package com.mr9.billgenerator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ShowBillActivity extends AppCompatActivity {

    protected static Spinner bills;
    protected MainActivity.BillGenDbHelper billGenDbHelper;
    protected SQLiteDatabase db;
    protected static String[] bill_nos;
    protected Button showBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill);
        bills = (Spinner) findViewById(R.id.spinner_bills);
        showBill=(Button)findViewById(R.id.button_showBill);
        String[] bill_projection = {
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo
        };
        String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo;
        String group = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo;
        String sortOrder =
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " ASC";

        billGenDbHelper=new MainActivity.BillGenDbHelper(getBaseContext());
        db = billGenDbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.InitialBillTable.TABLE_NAME,// The table to query
                bill_projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                group,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        List<String> b_no = new ArrayList<>();
        while(cursor.moveToNext()) {
            b_no.add(cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billNo)));
        }
        cursor.close();
        bill_nos=b_no.toArray(new String[0]);
        ArrayAdapter billsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bill_nos);
        bills.setAdapter(billsAdapter);
        showBill.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(ShowBillActivity.this,bills.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ShowBillActivity.this,RoughBill.class);
                        intent.putExtra("bill_no",Integer.valueOf(bills.getSelectedItem().toString()));
                        startActivity(intent);
                    }
                }
        );

    }
}
