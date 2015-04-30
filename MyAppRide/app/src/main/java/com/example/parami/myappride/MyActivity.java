package com.example.parami.myappride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;


public class MyActivity extends Activity {
    SQLiteData sqLiteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(android.R.style.Theme_Holo_Light_NoActionBar);


        setContentView(R.layout.activity_my);

        sqLiteAdapter=new SQLiteData(this);
        sqLiteAdapter=sqLiteAdapter.openToWrite();

        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent startingpoint=new Intent(MyActivity.this,Home.class);
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                    // Get the date today using Calendar object.
                    Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                    String reportDate = df.format(today);
                    StringTokenizer tk = new StringTokenizer(reportDate);
                    String date1 = tk.nextToken();  // <---  yyyy-mm-dd
                    final String time = tk.nextToken();  // <---  hh:mm:ss
                    sqLiteAdapter.insert("1","5000","0000","5000",date1,"0");

                    startActivity(startingpoint);
                }
            }
        };
        timer.start();
    }
    protected void onPause(){
        super.onPause();
        finish();
    }


}
