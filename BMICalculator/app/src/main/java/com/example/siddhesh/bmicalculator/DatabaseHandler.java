package com.example.siddhesh.bmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.Date;

import static android.R.attr.id;

/**
 * Created by Siddhesh on 06-03-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public DatabaseHandler(Context context) {
        super(context, "WeightDb", null , 1);
        Log.d("DB456","DB CREATED/OPENED");
        //db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS history(id integer primary key autoincrement, date TEXT, bmi REAL, weight REAL)");
        Log.d("DB456","Table CREATED");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists history");
        onCreate(db);
        Log.d("DB456","TABLE DROPPED");
    }

    public  void addhistory(SQLiteDatabase db, String date,double bmi,double weight)
    {
        ContentValues values=new ContentValues();
        values.put("date", String.valueOf(date));
        values.put("bmi",bmi);
        values.put("weight",weight);

        long rid=db.insert("history",null,values);
       // /*String[] rid=*/db.execSQL("INSERT into history(date,bmi,weight) values ('"+date+"','"+bmi+"','"+weight+"')");

        if(rid <= 0)
            Log.d("DB456","INSERT ISSUE");
        else
            Log.d("DB456","INSERTED");
    }

    public String getHistory(SQLiteDatabase db)
    {
        Cursor cursor=db.query("history",new String[]{"id","date","bmi","weight"},null,null,null,null,"id DESC");
        cursor.moveToFirst();
        String data = "";
        if(cursor!=null && cursor.getCount()>0) {
            do {
                int no1 = cursor.getInt(0);
                String date1 = cursor.getString(1);
                Double bmi1 = cursor.getDouble(2);
                Double weight1 = cursor.getDouble(3);

                data = data +"Record No :"+ no1 +"\n" + "Date and Time:" + date1 + "\n" + "BMI: " + bmi1 + "\n" + "Weight: " + weight1 + "\n\n\n";

                //Log.d("DB456","history");
            } while (cursor.moveToNext());
        }
        else
        {
            Log.d("DB456","NO RESULT TO SHOW");
        }
        return data;
    }


    public String clearHistory(SQLiteDatabase db)
    {
        db.execSQL("delete from history");
        String data=getHistory(db);
        /*
        SQLite keeps track of the largest ROWID that a table has ever held using
        the special SQLITE_SEQUENCE table. The  SQLITE_SEQUENCE table is created and
        initialized automatically whenever a normal table that contains an AUTOINCREMENT column is created.
        The content of the SQLITE_SEQUENCE table can be modified using ordinary UPDATE, INSERT, and DELETE statements.*/

        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='history'");
        return data;
    }
}