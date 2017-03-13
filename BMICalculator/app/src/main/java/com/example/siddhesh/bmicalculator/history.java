package com.example.siddhesh.bmicalculator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class history extends AppCompatActivity {

    TextView tvHistory;
    Button btnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistory= (TextView) findViewById(R.id.tvHistory);
        btnClear= (Button) findViewById(R.id.btnClear);

        Intent i = getIntent();
        String data = i.getStringExtra("d");
        tvHistory.setText(data);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
                final SQLiteDatabase dbH = dbHandler.getWritableDatabase();
                String data=dbHandler.clearHistory(dbH);

                tvHistory.setText(data);
            }
        });
    }
}
