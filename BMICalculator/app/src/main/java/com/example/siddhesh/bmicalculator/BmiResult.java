package com.example.siddhesh.bmicalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BmiResult extends AppCompatActivity {

    TextView tvResult,tvOv,tvOb,tvNo,tvUn;
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_result);

        tvResult= (TextView) findViewById(R.id.tvResult);
        tvNo= (TextView) findViewById(R.id.tvNo);
        tvOb= (TextView) findViewById(R.id.tvOb);
        tvOv= (TextView) findViewById(R.id.tvOv);
        tvUn= (TextView) findViewById(R.id.tvUn);
        btnShare= (Button) findViewById(R.id.btnShare);

        Intent i=getIntent();
        String bmi=i.getStringExtra("bmi");
        final String msg=i.getStringExtra("msg");
        String txt="Your BMI is "  +  bmi  + " and "  +  msg;
        tvResult.setText(txt);

        final double bmid = Double.parseDouble(bmi);
        if(bmid < 18.5) {
            tvUn.setTextColor(Color.parseColor("#ff0000"));
        }
        else if(bmid>=18.5 & bmid <25) {
            tvNo.setTextColor(Color.parseColor("#ff0000"));
        }
        else if(bmid>=25 & bmid <30) {
            tvOv.setTextColor(Color.parseColor("#ff0000"));

        }
        else
        {
            tvOb.setTextColor(Color.parseColor("#ff0000"));
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp1=getSharedPreferences("UserData",MODE_PRIVATE);
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String name = sp1.getString("name","");
                String age=sp1.getString("age","");
                String phone=sp1.getString("phonenumber","");
                String details="Name = " + name + "\n" +"Age " + age + "\n" +
                        "Phone " + phone + "\n" + "BMI " + bmid +"\n" + msg + "\n";
                i.putExtra(Intent.EXTRA_SUBJECT,"My BMI");
                i.putExtra(Intent.EXTRA_TEXT,details);
                startActivity(Intent.createChooser(i,getResources().getString(R.string.share_using)));
            }
        });
    }

    }





