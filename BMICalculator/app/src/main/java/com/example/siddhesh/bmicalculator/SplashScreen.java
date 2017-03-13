package com.example.siddhesh.bmicalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation animation;
    TextView tvBmi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvBmi1= (TextView) findViewById(R.id.tvBmi1);
        animation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.custom);

        tvBmi1.startAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        }).start();
    }
}
