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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FinalBill extends AppCompatActivity {
    //region declration
    protected TextView iTotal,discount,disTotal,SGST,CGST,finalTotal;
    protected int  bill_numb;
    protected int  bill_item_count;
    protected static String rbl_sr[];
    protected static String rbl_billNum[];
    protected static String rbl_quantity[];
    protected static String rbl_rate[];
    protected static String rbl_total[]=new String[15];
    protected int  bill_noCount=0;
    protected String  i_sr;
    protected String  i_billNum;
    protected String  i_quant;
    protected String  i_rat;
    protected TableLayout  s_col ;
    protected TableLayout  b_col ;
    protected TableLayout  q_col ;
    protected TableLayout  r_col ;
    protected TableLayout  t_col ;
    protected TableRow  s_tbrow;
    protected TableRow  b_tbrow;
    protected TableRow  q_tbrow;
    protected TableRow  r_tbrow;
    protected TableRow  t_tbrow;
    protected TextView  sr;
    protected TextView  billNum;
    protected TextView  quantity;
    protected TextView  rate;
    protected TextView  rtotal;
    protected Intent  intent;
    protected int i,j;
    protected float total=0;
    protected MainActivity.BillGenDbHelper billGenDbHelper;
    protected SQLiteDatabase db;
    protected String sr_array[];
    protected String billNum_array[];
    protected String rate_array[];
    protected String quat_array[];
    DecimalFormat df=new DecimalFormat("0.00");

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);
        //region dbhelper
        billGenDbHelper = new MainActivity.BillGenDbHelper(getBaseContext());
        db = billGenDbHelper.getWritableDatabase();
        //endregion
        //region intent call
         intent = getIntent();
        int f_bill = intent.getIntExtra("first_bill", 1);
        int l_bill = intent.getIntExtra("last_bill", 1);
        //endregion
         bill_item_count = 0;
        //region widget linking
        iTotal = (TextView) findViewById(R.id.iTotal);
        discount = (TextView) findViewById(R.id.discount);
        disTotal = (TextView) findViewById(R.id.disTotal);
        SGST = (TextView) findViewById(R.id.SGST);
        CGST = (TextView) findViewById(R.id.CGST);
        finalTotal = (TextView) findViewById(R.id.finalTotal);
        //endregion
        //set billNO
        //region bill_noCount
        String[] bill_projection = {
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant,
                DatabaseContract.InitialBillTable.COLUMN_NAME_billRate
        };
        String selection = DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " BETWEEN " + f_bill + " AND " + l_bill;
        String sortOrder =
                DatabaseContract.InitialBillTable.COLUMN_NAME_billNo + " ASC ," + DatabaseContract.InitialBillTable.COLUMN_NAME_billRate + " ASC " ;
        Cursor cursor = db.query(
                DatabaseContract.InitialBillTable.TABLE_NAME,// The table to query
                bill_projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        //endregion

        //region getListToArray
        List<String> b_sr = new ArrayList<>();
        List<String> b_billNum = new ArrayList<>();
        List<String> b_quantity = new ArrayList<>();
        List<String> b_rate = new ArrayList<>();
        while (cursor.moveToNext()) {
            i_sr=String.valueOf(bill_noCount++);
            i_billNum = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billNo));
            i_quant = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billQuant));
             i_rat = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseContract.InitialBillTable.COLUMN_NAME_billRate));
            b_sr.add( i_sr);
            b_billNum.add( i_billNum);
            b_quantity.add( i_quant);
            b_rate.add( i_rat);
        }
        cursor.close();
        rbl_sr = b_sr.toArray(new String[0]);
        rbl_billNum = b_billNum.toArray(new String[0]);
        rbl_quantity = b_quantity.toArray(new String[0]);
        rbl_rate = b_rate.toArray(new String[0]);
        //Toast.makeText(RoughBill.this,String.valueOf( bill_noCount),Toast.LENGTH_SHORT).show();
        int temp= bill_noCount;
        sr_array=new String[rbl_rate.length];
        billNum_array=new String[rbl_rate.length];
        rate_array=new String[rbl_rate.length];
        quat_array=new String[rbl_quantity.length];
        int j=0;
        for(i=0;i< bill_noCount;)
        {
            sr_array[j] = String.valueOf(j+1);
            billNum_array[j] = String.valueOf(Integer.valueOf(rbl_billNum[i]));
            quat_array[j] = String.valueOf(Integer.valueOf(rbl_quantity[i]));
            rate_array[j] = String.valueOf(Float.valueOf(rbl_rate[i]));
            i++;
            //Toast.makeText(RoughBill.this,String.valueOf(rbl_rate[i])+String.valueOf(rbl_rate[j]),Toast.LENGTH_SHORT).show();
            while(i< bill_noCount && (rate_array[j]).equals(rbl_rate[i]) && (billNum_array[j]).equals(rbl_billNum[i])) {
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
        try {
            iTotal.setBackgroundResource(R.drawable.divider_table);
            iTotal.setText(String.valueOf(df.format(total)));
            discount.setBackgroundResource(R.drawable.divider_table);
            discount.setText(String.valueOf(df.format((total * 3) / 100)));
            disTotal.setBackgroundResource(R.drawable.divider_table);
            disTotal.setText(String.valueOf(df.format(total - ((total*3)/100))));
            SGST.setBackgroundResource(R.drawable.divider_table);
            SGST.setText(String.valueOf(df.format(((total - ((total*3)/100)) * 2.5) / 100)));
            CGST.setBackgroundResource(R.drawable.divider_table);
            CGST.setText(String.valueOf(df.format(((total - ((total*3)/100)) * 2.5) / 100)));
            finalTotal.setBackgroundResource(R.drawable.divider_table);
            finalTotal.setText(String.valueOf(df.format((total - ((total*3)/100))+(((total - ((total*3)/100)) * 5) / 100))));
        }
        catch (Exception e){}
    }
    private void show_bill_item() {
        //region Initialization
         s_col = (TableLayout) findViewById(R.id.bill_fSrNum_col);
         b_col = (TableLayout) findViewById(R.id.bill_fBillNum_col);
         q_col = (TableLayout) findViewById(R.id.bill_fQuntity_col);
         r_col = (TableLayout) findViewById(R.id.bill_fRate_col);
         t_col = (TableLayout) findViewById(R.id.bill_fTotal_col);
         s_tbrow = new TableRow(this);
         sr = new TextView(this);
         b_tbrow = new TableRow(this);
         billNum = new TextView(this);
         q_tbrow = new TableRow(this);
         quantity = new TextView(this);
         r_tbrow = new TableRow(this);
         rate = new TextView(this);
         t_tbrow = new TableRow(this);
         rtotal = new TextView(this);
        //endregion
        // q_col.removeAllViews();
        //region quantity_tableInsertion
         s_tbrow.setBackgroundResource(R.drawable.divider_table);
         b_tbrow.setBackgroundResource(R.drawable.divider_table);
         q_tbrow.setBackgroundResource(R.drawable.divider_table);
         r_tbrow.setBackgroundResource(R.drawable.divider_table);
         t_tbrow.setBackgroundResource(R.drawable.divider_table);
        s_col.setGravity(Gravity.CENTER);
        s_tbrow.setGravity(Gravity.CENTER);
        sr.setText(sr_array[i]);
        //quantity.setTextColor(Color.BLACK);
        sr.setTextSize(20);
        s_tbrow.addView(sr);
        s_col.addView( s_tbrow);
        //endregion
        b_col.setGravity(Gravity.CENTER);
        b_tbrow.setGravity(Gravity.CENTER);
        billNum.setText(billNum_array[i]);
        //quantity.setTextColor(Color.BLACK);
        billNum.setTextSize(20);
        b_tbrow.addView(billNum);
        b_col.addView( b_tbrow);
        //endregion
        q_col.setGravity(Gravity.CENTER);
         q_tbrow.setGravity(Gravity.CENTER);
         quantity.setText(quat_array[i]);
        //quantity.setTextColor(Color.BLACK);
         quantity.setTextSize(20);
         q_tbrow.addView( quantity);
         q_col.addView( q_tbrow);
        //endregion

        //region rate_tableInsertion
         r_col.setGravity(Gravity.CENTER);
         r_tbrow.setGravity(Gravity.CENTER);
         rate.setText(rate_array[i]);
        //rate.setTextColor(Color.BLACK);
         rate.setTextSize(20);
         r_tbrow.addView( rate);
         r_col.addView( r_tbrow);
        //endregion

        //region total_tableInsertion
         t_col.setGravity(Gravity.CENTER);
         t_tbrow.setGravity(Gravity.CENTER);
         rtotal.setText(rbl_total[i]);
        //rate.setTextColor(Color.BLACK);
         rtotal.setTextSize(20);
         t_tbrow.addView( rtotal);
         t_col.addView( t_tbrow);
        //endregion
    }
}
