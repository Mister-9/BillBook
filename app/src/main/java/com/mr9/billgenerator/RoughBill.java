package com.mr9.billgenerator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RoughBill extends AppCompatActivity {
    //region declration
    protected TextView rb_bill_no;
    protected TextView rb_bill_total;
    protected int rb_bill_item_count;
    protected static String rbl_quantity[];
    protected static String rbl_rate[];
    protected static String rbl_total[]=new String[10];
    protected int rb_bill_noCount=0;
    protected String rb_i_quant;
    protected String rb_i_rat;
    protected TableLayout rb_q_col ;
    protected TableLayout rb_r_col ;
    protected TableLayout rb_t_col ;
    protected TableRow rb_q_tbrow;
    protected TableRow rb_r_tbrow;
    protected TableRow rb_t_tbrow;
    protected TextView rb_quantity;
    protected TextView rb_rate;
    protected TextView rb_total;
    protected int rb_bill_numb;
    protected Intent rb_intent;
    protected int i,j;
    protected float total=0;
    protected MainActivity.BillGenDbHelper billGenDbHelper;
    protected SQLiteDatabase db;
    protected String rate_array[];
    protected String quat_array[];
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rough_bill);
        //region dbhelper
        billGenDbHelper = new MainActivity.BillGenDbHelper(getBaseContext());
        db = billGenDbHelper.getWritableDatabase();
        //endregion
        //region intent call
        rb_intent = getIntent();
        rb_bill_numb = rb_intent.getIntExtra("bill_no",1);
        //endregion
        rb_bill_item_count = 0;
        //region widget linking
        rb_bill_no = (TextView) findViewById(R.id.text_numBillNum);
        rb_bill_total = (TextView) findViewById(R.id.text_rNumFinalTotal);
        //endregion
        //set billNO
        rb_bill_no.setText(String.valueOf(rb_bill_numb));

        //region bill_noCount
        String[] bill_projection = {
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate
        };
        String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " = ?";
        String[] selectionArgs = {String.valueOf(rb_bill_numb)};
        String sortOrder =
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " ASC";
        Cursor cursor = db.query(
                DatabaseContract.InitialBillTable.TABLE_NAME,// The table to query
                bill_projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        //endregion

        //region getListToArray
        List<String> b_quantity = new ArrayList<>();
        List<String> b_rate = new ArrayList<>();
        while (cursor.moveToNext()) {
            rb_bill_noCount++;
            rb_i_quant = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant));
            rb_i_rat = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billRate));
            b_quantity.add(rb_i_quant);
            b_rate.add(rb_i_rat);
        }
        cursor.close();
        rbl_quantity = b_quantity.toArray(new String[0]);
        rbl_rate = b_rate.toArray(new String[0]);
        //Toast.makeText(RoughBill.this,String.valueOf(rb_bill_noCount),Toast.LENGTH_SHORT).show();
        int temp=rb_bill_noCount;
        rate_array=new String[rbl_rate.length];
        quat_array=new String[rbl_quantity.length];
        int j=0;
        for(i=0;i<rb_bill_noCount;)
        {
            quat_array[j] = String.valueOf(Integer.valueOf(rbl_quantity[i]));
            rate_array[j] = String.valueOf(Float.valueOf(rbl_rate[i]));
            i++;
          //Toast.makeText(RoughBill.this,String.valueOf(rbl_rate[i])+String.valueOf(rbl_rate[j]),Toast.LENGTH_SHORT).show();
          while(i<rb_bill_noCount && (rate_array[j]).equals(rbl_rate[i])) {
              quat_array[j] = String.valueOf(Integer.valueOf(quat_array[j]) + Integer.valueOf(rbl_quantity[i++]));
              //Toast.makeText(RoughBill.this, String.valueOf("here we go"), Toast.LENGTH_SHORT).show();
          }
          j++;
        }
        //Toast.makeText(RoughBill.this,String.valueOf(j),Toast.LENGTH_SHORT).show();
        //endregion
        for(i=0;i<j;i++) {//show inserted row
            total=total+(Integer.valueOf(quat_array[i])*Float.valueOf(rate_array[i]));
            rbl_total[i]=String.valueOf(Integer.valueOf(quat_array[i])*Float.valueOf(rate_array[i]));
            show_bill_item();
        }
        rb_bill_total.setBackgroundResource(R.drawable.divider_table);
        rb_bill_total.setText(String.valueOf(total));
    }
    private void show_bill_item() {
        //region Initialization
        rb_q_col = (TableLayout) findViewById(R.id.item_rQuntity_column);
        rb_r_col = (TableLayout) findViewById(R.id.item_rRate_column);
        rb_t_col = (TableLayout) findViewById(R.id.item_rTotal_column);
        rb_q_tbrow = new TableRow(this);
        rb_quantity = new TextView(this);
        rb_r_tbrow = new TableRow(this);
        rb_rate = new TextView(this);
        rb_t_tbrow = new TableRow(this);
        rb_total = new TextView(this);
        //endregion
        //rb_q_col.removeAllViews();
        //region quantity_tableInsertion
        rb_q_tbrow.setBackgroundResource(R.drawable.divider_table);
        rb_r_tbrow.setBackgroundResource(R.drawable.divider_table);
        rb_t_tbrow.setBackgroundResource(R.drawable.divider_table);
        rb_q_col.setGravity(Gravity.CENTER);
        rb_q_tbrow.setGravity(Gravity.CENTER);
        rb_quantity.setText(quat_array[i]);
        //quantity.setTextColor(Color.BLACK);
        rb_quantity.setTextSize(25);
        rb_q_tbrow.addView(rb_quantity);
        rb_q_col.addView(rb_q_tbrow);
        //endregion

        //region rate_tableInsertion
        rb_r_col.setGravity(Gravity.CENTER);
        rb_r_tbrow.setGravity(Gravity.CENTER);
        rb_rate.setText(rate_array[i]);
        //rate.setTextColor(Color.BLACK);
        rb_rate.setTextSize(25);
        rb_r_tbrow.addView(rb_rate);
        rb_r_col.addView(rb_r_tbrow);
        //endregion

        //region total_tableInsertion
        rb_t_col.setGravity(Gravity.CENTER);
        rb_t_tbrow.setGravity(Gravity.CENTER);
        rb_total.setText(rbl_total[i]);
        //rate.setTextColor(Color.BLACK);
        rb_total.setTextSize(25);
        rb_t_tbrow.addView(rb_total);
        rb_t_col.addView(rb_t_tbrow);
        //endregion
    }
}
