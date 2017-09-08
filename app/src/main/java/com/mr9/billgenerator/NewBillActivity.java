package com.mr9.billgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewBillActivity extends AppCompatActivity {
    private static EditText bill_no;
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
                            if(Integer.valueOf(bill_no.getText().toString())<=0)
                            {
                                Toast.makeText(NewBillActivity.this,"Please enter correct bill number.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Intent intent = new Intent(NewBillActivity.this, NewItemsActivity.class);
                                intent.putExtra("bill_no", Integer.valueOf(bill_no.getText().toString()));
                                startActivity(intent);
                            }
                        }
                        catch (Exception e){}
                    }
                }
        );
    }
}
