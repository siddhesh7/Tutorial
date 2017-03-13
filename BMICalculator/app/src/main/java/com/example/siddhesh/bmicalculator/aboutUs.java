package com.example.siddhesh.bmicalculator;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class aboutUs extends AppCompatActivity {

    TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        tvAbout= (TextView) findViewById(R.id.tvAbout);

        Intent i = getIntent();
        String file="aboutUs.txt";
        try {
            InputStreamReader isr=new InputStreamReader(getAssets().open(file));

            BufferedReader br=new BufferedReader(isr);
            StringBuffer sb=new StringBuffer();

            String msg;

            while((msg=br.readLine())!=null)
            {
                sb.append(msg + "\n");
            }

            br.close();
            String text = sb.toString();
            tvAbout.setText(text);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

