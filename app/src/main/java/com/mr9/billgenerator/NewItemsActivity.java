package com.mr9.billgenerator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NewItemsActivity extends AppCompatActivity {
    //region Declration
    protected EditText i_quantity,i_rate;
    protected TextView bill_no;
    protected Button add_item;
    protected Button submit_bill;
    protected int bill_item_count;
    protected static String l_quantity[];
    protected static String l_rate[];
    protected int bill_noCount=0;
    protected String i_quant;
    protected String i_rat;
    protected TableLayout q_col ;
    protected TableLayout r_col ;
    protected TableRow q_tbrow;
    protected TableRow r_tbrow;
    protected TextView quantity;
    protected TextView rate;
    protected int bill_numb;
    protected Intent intent;
    protected MainActivity.BillGenDbHelper billGenDbHelper;
    protected SQLiteDatabase db;
    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_items);
        billGenDbHelper=new MainActivity.BillGenDbHelper(getBaseContext());
        db = billGenDbHelper.getWritableDatabase();
        intent=getIntent();
        bill_numb=intent.getIntExtra("bill_no",1);
        submit_bill=(Button) findViewById(R.id.button_listSubmit);
        bill_item_count=0;
        //region widget linking
        i_quantity=(EditText)findViewById(R.id.edit_quantity);
        i_rate=(EditText)findViewById(R.id.edit_rate);
        bill_no=(TextView) findViewById(R.id.text_billNumber);
        add_item=(Button)findViewById(R.id.button_addItem);
        //endregion

        //set billNO
        bill_no.setText(String.valueOf(bill_numb));

        //region bill_noCount
        String[] bill_projection = {
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo
        };
        String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " = ?";
        String[] selectionArgs = {bill_no.getText().toString()};
        String sortOrder =
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " DESC";
        Cursor cursor = db.query(
                DatabaseContract.InitialBillTable.TABLE_NAME,// The table to query
                bill_projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        while(cursor.moveToNext()) {
            Toast.makeText(NewItemsActivity.this,String.valueOf(bill_item_count),Toast.LENGTH_SHORT).show();
            bill_noCount++;
        }
        cursor.close();
        //endregion

        //region add_itemListner
        add_item.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(i_quantity.getText().length()<1 || i_rate.getText().length()<1) {
                            if(i_quantity.getText().length()<1 && i_rate.getText().length()<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter Quantity and Rate!!",Toast.LENGTH_SHORT).show();
                            else if(i_quantity.getText().length()<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter Quantity!!",Toast.LENGTH_SHORT).show();
                            else if(i_rate.getText().length()<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter Rate!!",Toast.LENGTH_SHORT).show();
                        }
                        else if(i_rate.getText().toString().charAt(0)=='.') {
                            Toast.makeText(NewItemsActivity.this, "Please valid rate!!", Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.valueOf(i_quantity.getText().toString())<1 || Float.valueOf(i_rate.getText().toString())<1) {
                            if(Integer.valueOf(i_quantity.getText().toString())<1 && Float.valueOf(i_rate.getText().toString())<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter valid Quantity and Rate!!",Toast.LENGTH_SHORT).show();
                            else if(Integer.valueOf(i_quantity.getText().toString())<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter valid Quantity!!",Toast.LENGTH_SHORT).show();
                            else if(Float.valueOf(i_rate.getText().toString())<1)
                                Toast.makeText(NewItemsActivity.this,"Please enter valid Rate!!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            billDataEntry();
                            i_quantity.setText("");
                            i_rate.setText("");
                            i_quantity.requestFocus();
                        }
                    }
                }

        );
        //endregion`

        //region submit_billListner
        submit_bill.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(NewItemsActivity.this,"Bill Submitted",Toast.LENGTH_SHORT).show();
                        intent=new Intent(NewItemsActivity.this,RoughBill.class);
                        intent.putExtra("bill_no",bill_numb);
                        startActivity(intent);
                    }
                }
        );

        //endregion
    }
    private void billDataEntry() {
        db = billGenDbHelper.getWritableDatabase();

        //region insertRowItem and get row_id
        DecimalFormat df=new DecimalFormat("#.00");
        ContentValues billRowValues = new ContentValues();
            billRowValues.put(DatabaseContract.InitialBillTable.COLUMN_NAME_billNo,Integer.valueOf(bill_no.getText().toString()));
            billRowValues.put(DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant,Integer.valueOf(i_quantity.getText().toString()));
            billRowValues.put(DatabaseContract.InitialBillTable.COLUMN_NAME_billRate,df.format(Float.valueOf(i_rate.getText().toString())));
        // Insert the new row, returning the primary key value of the new row
        long itemRowId = db.insert(DatabaseContract.InitialBillTable.TABLE_NAME, null, billRowValues);
        //endregion

        //region getRowList
        String[] projection = {
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate,
        };
        // Filter results WHERE "ID" = 'itemRowID'
        String selection = DatabaseContract.InitialBillTable._ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemRowId)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " DESC";

        Cursor cursor = db.query(
                DatabaseContract.InitialBillTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        //endregion

        //region getListToArray
        List<String> b_quantity = new ArrayList<String>();
        List<String> b_rate = new ArrayList<String>();
        while(cursor.moveToNext()) {
            i_quant= cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant));
            i_rat= cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billRate));
            b_quantity.add(i_quant);
            b_rate.add(i_rat);
        }
        cursor.close();
        l_quantity=b_quantity.toArray(new String[0]);
        l_rate=b_rate.toArray(new String[0]);
        //endregion

        //show inserted row
        show_bill_item();

        //increment items_count
        bill_item_count++;
    }
    private void show_bill_item() {
        //region Initialization
            q_col = (TableLayout) findViewById(R.id.item_quntity_column);
            r_col = (TableLayout) findViewById(R.id.item_rate_column);
            q_tbrow = new TableRow(this);
            quantity = new TextView(this);
            r_tbrow = new TableRow(this);
            rate = new TextView(this);
        //endregion
        //region quantity_tableInsertion
        if(bill_item_count%2==1)
        {
            q_tbrow.setBackgroundResource(R.drawable.divider_table);
            r_tbrow.setBackgroundResource(R.drawable.divider_table);
        }
        else
        {
            q_tbrow.setBackgroundResource(R.drawable.divider_table);
            r_tbrow.setBackgroundResource(R.drawable.divider_table);
        }
        q_col.setGravity(Gravity.CENTER);
            q_tbrow.setGravity(Gravity.CENTER);
            quantity.setText(l_quantity[0]);
        //quantity.setTextColor(Color.BLACK);
            quantity.setTextSize(25);
            q_tbrow.addView(quantity);
            q_col.addView(q_tbrow);
        //endregion

        //region rate_tableInsertion
        r_col.setGravity(Gravity.CENTER);
            r_tbrow.setGravity(Gravity.CENTER);
            rate.setText(l_rate[0]);
            //rate.setTextColor(Color.BLACK);
            rate.setTextSize(25);
            r_tbrow.addView(rate);
            r_col.addView(r_tbrow);
        //endregion
    }
}
