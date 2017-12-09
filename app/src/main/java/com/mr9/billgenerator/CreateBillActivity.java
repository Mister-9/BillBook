package com.mr9.billgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBillActivity extends AppCompatActivity {
    protected Button button_createBill;
    protected EditText first_bill,last_bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_final_bill);
        first_bill=(EditText)findViewById(R.id.first_bill);
        last_bill=(EditText)findViewById(R.id.last_bill);
        button_createBill=(Button)findViewById(R.id.button_createBill);
        button_createBill.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(first_bill.getText().length()<1 || last_bill.getText().length()<1) {
                            if(first_bill.getText().length()<1 && last_bill.getText().length()<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter Starting and Ending bills!!",Toast.LENGTH_SHORT).show();
                            else if(first_bill.getText().length()<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter Starting bill!!",Toast.LENGTH_SHORT).show();
                            else if(last_bill.getText().length()<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter Ending bill!!",Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.valueOf(first_bill.getText().toString())<1 || Float.valueOf(last_bill.getText().toString())<1) {
                            if(Integer.valueOf(first_bill.getText().toString())<1 && Float.valueOf(last_bill.getText().toString())<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter valid Bill numbers!!",Toast.LENGTH_SHORT).show();
                            else if(Integer.valueOf(first_bill.getText().toString())<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter valid Starting bill!!",Toast.LENGTH_SHORT).show();
                            else if(Float.valueOf(last_bill.getText().toString())<1)
                                Toast.makeText(CreateBillActivity.this,"Please enter valid Ending bill!!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //  Toast.makeText(ShowBillActivity.this,bills.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(CreateBillActivity.this,FinalBill.class);
                            intent.putExtra("first_bill",Integer.valueOf(first_bill.getText().toString()));
                            intent.putExtra("last_bill",Integer.valueOf(last_bill.getText().toString()));
                            startActivity(intent);
                        }
                    }
                }
        );
    }
}
