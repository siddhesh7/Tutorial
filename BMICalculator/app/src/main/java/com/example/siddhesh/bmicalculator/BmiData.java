package com.example.siddhesh.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;

public class BmiData extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,View.OnClickListener {
    Button btnCalculate,btnHistory;
    TextView tvWelcome,tvLocation;
    Spinner spnFeet, spnInch;
    EditText etWeight;
    SharedPreferences sp1;
    GoogleApiClient mLocationClient;
    Location mLastLocation;

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.items,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.aboutUs:
                Intent i1 = new Intent(getApplicationContext(),aboutUs.class);
                startActivity(i1);
                break;
            case R.id.callUs:
                Intent i2 = new Intent(Intent.ACTION_DIAL);
                i2.setData(Uri.parse("tel:9833277742"));
                startActivity(i2);
                break;
            case R.id.contactUs:
                Toast.makeText(getApplicationContext(),"Click on call Us to call and Email to mail",Toast.LENGTH_LONG).show();
                break;
            case R.id.email:
                Intent i3=new Intent(Intent.ACTION_SENDTO);
                i3.setData(Uri.parse("mailto:"+Uri.encode("siddhesh.d.hindalekar@gmail.com")));
                //i3.putExtra(Intent.EXTRA_EMAIL,"siddhesh.d.hindalekar@gmail.com");
                startActivity(i3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_data);

        GoogleApiClient.Builder builder= new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        mLocationClient=builder.build();

        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
        final SQLiteDatabase dbH = dbHandler.getWritableDatabase();

        sp1 = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = sp1.getString("name", "");
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnHistory= (Button) findViewById(R.id.btnHistory);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvLocation= (TextView) findViewById(R.id.tvLocation);
        etWeight = (EditText) findViewById(R.id.etWeight);
        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInch = (Spinner) findViewById(R.id.spnInch);

        tvWelcome.setText("Welcome  " + name);

        String[] feet = {"1", "2", "3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, feet);
        spnFeet.setAdapter(ad1);

        String[] inch = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        ArrayAdapter<String> ad2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inch);
        spnInch.setAdapter(ad2);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ft = (String) spnFeet.getSelectedItem();
                String in = (String) spnInch.getSelectedItem();
                String wt =  etWeight.getText().toString();

                if (wt.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Weight", Toast.LENGTH_LONG).show();
                    etWeight.requestFocus();
                    return;
                }

                int foot = Integer.parseInt(ft);
                int inches = Integer.parseInt(in);
                double weight = Double.parseDouble(wt);

                while (foot > 0) {
                    inches = inches + 12;
                    foot--;
                }

                double height = inches * 2.54;
                double bmi = weight / (height * height);
                bmi = bmi * 10000;

                String msg;
                if (bmi < 18.5) {
                    msg = "You are underweight";
                } else if (bmi >= 18.5 & bmi < 25) {
                    msg = "You are normal";
                } else if (bmi >= 25 & bmi < 30) {
                    msg = "You are overweight";
                } else {
                    msg = "You are Obese";
                }
                Date date = new Date();
                String send=date.toString();
                /*
                Date date = new Date();
                SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy-MM-dd'\ntime-'HH:mm:ss\n", Locale.getDefault());
                String currentDate = dateFormatWithZone.format(date);
                StringBuffer sb = new StringBuffer(currentDate);
                int day= date.getDay();
                sb.append(day);
                String send=sb.toString();
                */
                //

                //Toast.makeText(getApplicationContext(),send,Toast.LENGTH_LONG).show();

                dbHandler.addhistory(dbH, send,bmi,weight);
                Intent i = new Intent(BmiData.this, BmiResult.class);
                i.putExtra("bmi", String.valueOf(bmi));
                i.putExtra("msg", msg);
                startActivity(i);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data=dbHandler.getHistory(dbH);
                Log.d("New row: ", data);
                Intent i=new Intent(getApplicationContext(),history.class);
                i.putExtra("d",data);
                startActivity(i);
            }
        });
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //super.onBackPressed;
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(mLocationClient!=null)
        {
            mLocationClient.connect();
            //Toast.makeText(getApplicationContext(),"connected and inside onstart()",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        if(mLastLocation!=null)
        {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            //String countryName = addresses.get(0).getAddressLine(2);

            //Toast.makeText(getApplicationContext(),"connected and not null",Toast.LENGTH_LONG).show();
            //String loc= String.valueOf(mLastLocation);
            String f="LOCATION : "+cityName+", "+stateName;
           tvLocation.setText(f);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(),"Connection Suspended",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_LONG).show();
    }
}