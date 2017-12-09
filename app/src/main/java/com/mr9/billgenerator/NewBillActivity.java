package com.mr9.billgenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewBillActivity extends AppCompatActivity {
    private static EditText bill_no;
    protected MainActivity.BillGenDbHelper billGenDbHelper;
    protected SQLiteDatabase db;
    protected int bill_noCount=0;
    private static Button bill_no_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);
        bill_no=(EditText)findViewById(R.id.edit_newBillNumber);
        bill_no_submit=(Button) findViewById(R.id.button_newBillSubmit);
        bill_no_submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try{
                            billGenDbHelper=new MainActivity.BillGenDbHelper(getBaseContext());
                            db = billGenDbHelper.getWritableDatabase();
                            //region bill_noAlert
//                                bill_no.setText(String.valueOf(bill_no));
                            //region bill_noCount
                            String[] bill_projection = {
                                    DatabaseContract.InitialBillTable.COLUMN_NAME_billNo
                            };
                            String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " = ?";
                            String[] selectionArgs = {bill_no.getText().toString()};
                            String sortOrder =
                                    DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " DESC";

                            if(Integer.valueOf(bill_no.getText().toString())<=0)
                            {
                                Toast.makeText(NewBillActivity.this,"Please enter correct bill number.",Toast.LENGTH_LONG).show();
                            }
                            else {
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
                                    bill_noCount++;
                                }
                                cursor.close();
                                if(bill_noCount>0)
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NewBillActivity.this);
                                    builder.setMessage("The bill number already exist\n Do you want to overwrite bill?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " LIKE ?";
                                                    String[] selectionArgs = {bill_no.getText().toString()};
                                                    db.delete(DatabaseContract.InitialBillTable.TABLE_NAME, selection, selectionArgs);
                                                    Intent intent = new Intent(NewBillActivity.this, NewItemsActivity.class);
                                                    intent.putExtra("bill_no", Integer.valueOf(bill_no.getText().toString()));
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                else{
                                    Intent intent = new Intent(NewBillActivity.this, NewItemsActivity.class);
                                    intent.putExtra("bill_no", Integer.valueOf(bill_no.getText().toString()));
                                    startActivity(intent);
                                }
                                //endregion
                            }
                        }
                        catch (Exception e){}
                    }
                }
        );
    }
}
