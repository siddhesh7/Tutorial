package com.example.siddhesh.bmicalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button btnRegister;
    EditText etName,etAge,etPhonenumber;
    SharedPreferences sp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName= (EditText) findViewById(R.id.etName);
        etAge= (EditText) findViewById(R.id.etAge);
        etPhonenumber= (EditText) findViewById(R.id.etPhoneNumber);
        btnRegister= (Button) findViewById(R.id.btnRegister);
        sp1=getSharedPreferences("UserData",MODE_PRIVATE);

        if(sp1.getBoolean("ne",false)==false)
        {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name=etName.getText().toString();
                    String age=etAge.getText().toString();
                    String phoneNumber=etPhonenumber.getText().toString();

                    if(name.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Name",Toast.LENGTH_LONG).show();
                        etName.requestFocus();
                        return;
                    }

                    if(phoneNumber.length()!=10)
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Phone Number",Toast.LENGTH_LONG).show();
                        etPhonenumber.requestFocus();
                        return;
                    }

                    SharedPreferences.Editor editor=sp1.edit();
                    editor.putString("name",name);
                    editor.putString("age",age);
                    editor.putString("phonenumber",phoneNumber);
                    editor.putBoolean("ne",true);
                    editor.commit();


                    Intent i=new Intent(getApplicationContext(),BmiData.class);
                    startActivity(i);
                    finish();

                }
            });

        }
        else
        {

            Intent i=new Intent(getApplicationContext(),BmiData.class);
            startActivity(i);
            finish();

        }

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
