package com.example.parami.myappride;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class Home extends Activity {
    SQLiteData sqLiteAdapter;
    TextView t5, t2, t4;
    static int[] bus1 = {5000, 5000, 5000, 5000, 5000, 5000, 5000};
    static int p = 0;
    static int[] taxi1 = new int[100];
    Button b;
    EditText e;
    private PendingIntent pendingIntent;
    static String date1;
    String[] mMonth = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(android.R.style.Theme_Holo_Light_DarkActionBar);
        setContentView(R.layout.home);
        b = (Button) findViewById(R.id.button);
        e = (EditText) findViewById(R.id.editText);
        t2 = (TextView) findViewById(R.id.textView2);
        t5 = (TextView) findViewById(R.id.textView5);
        t4 = (TextView) findViewById(R.id.textView4);
        sqLiteAdapter = new SQLiteData(this);
        sqLiteAdapter = sqLiteAdapter.openToWrite();
        sqLiteAdapter = sqLiteAdapter.openToRead();
        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec BUDGET1 = tabs.newTabSpec("ADD_Water");
        BUDGET1.setContent(R.id.ADD_Water);

        BUDGET1.setIndicator("ADD_Water");
        tabs.addTab(BUDGET1);

        TabHost.TabSpec FOOD1 = tabs.newTabSpec("Alarm");
        FOOD1.setContent(R.id.Alarm);
        FOOD1.setIndicator("Alarm");
        tabs.addTab(FOOD1);

        TabHost.TabSpec ACTIVITY1 = tabs.newTabSpec("Statistic");
        ACTIVITY1.setContent(R.id.Statistic);
        ACTIVITY1.setIndicator("Statistic");
        tabs.addTab(ACTIVITY1);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        String reportDate = df.format(today);
        StringTokenizer tk = new StringTokenizer(reportDate);
        date1 = tk.nextToken();  // <---  yyyy-mm-dd
        final String time = tk.nextToken();  // <---  hh:mm:ss
        t5.setText(date1);
        t2.setText(sqLiteAdapter.gettarget("1"));
        t4.setText(sqLiteAdapter.getamount("1", date1));
   /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(Home.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(Home.this, 0, alarmIntent, 0);

        findViewById(R.id.startAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        findViewById(R.id.stopAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.stopAlarmAt10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAt10();
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = e.getText().toString();
                String pamount = sqLiteAdapter.getamount("1", date1);
                sqLiteAdapter.updateamount("1", date1, amount, pamount);
                t4.setText(sqLiteAdapter.getamount("1", date1));
            }
        });
        Button b3 = (Button) findViewById(R.id.button1);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Openchart();
            }
        });





    }

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;


        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt10() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
    }


    protected void Openchart(){


        int[] x={1,2,3,4,5,6,7,};
        sqLiteAdapter=new SQLiteData(this);
        sqLiteAdapter=sqLiteAdapter.openToWrite();
      Cursor cursor=sqLiteAdapter.Queue("1");
        String[] array = new String[cursor.getCount()];
        int k = 0;
        while(cursor.moveToNext()){
            String uname = cursor.getString(cursor.getColumnIndex(SQLiteData.KEY_AMOUNT));
            array[k] = uname;
            k++;
        }

        for(int j=0;j<array.length-1;j++){
            taxi1[j]=Integer.parseInt(array[j]);

        }







        XYSeries series=new XYSeries("Daily Target");
        XYSeries series1=new XYSeries("Daily consumption");


        for(int i=0;i<x.length;i++) {

            series.add(x[i], taxi1[i]);
            series1.add(x[i], bus1[i]);
        }
            XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
            Dataset.addSeries(series);
            Dataset.addSeries(series1);


            XYSeriesRenderer renderer2 = new XYSeriesRenderer();
            renderer2.setColor(Color.RED);
            renderer2.setPointStyle(PointStyle.CIRCLE);
            renderer2.setFillPoints(true);
            renderer2.setLineWidth(4);
            renderer2.setDisplayChartValues(true);

            XYSeriesRenderer renderer3 = new XYSeriesRenderer();
            renderer3.setColor(Color.GREEN);
            renderer3.setPointStyle(PointStyle.CIRCLE);
            renderer3.setFillPoints(true);
            renderer3.setLineWidth(4);
            renderer3.setDisplayChartValues(true);

            XYMultipleSeriesRenderer multirender = new XYMultipleSeriesRenderer();
            multirender.setXLabels(0);
            multirender.setChartTitle("User Statistic");
            multirender.setXTitle("Time");
            multirender.setYTitle("Calories");
            multirender.setZoomButtonsVisible(true);

            for(int i=0;i<x.length;i++){
                multirender.addXTextLabel(i+1,mMonth[i]);
            }


            multirender.addSeriesRenderer(renderer2);
            multirender.addSeriesRenderer(renderer3);
            Intent intent= ChartFactory.getLineChartIntent(getBaseContext(), Dataset, multirender);
            startActivity(intent);

        }




    private int[] convert(String[] string) { //Note the [] after the String.
        int number[] = new int[string.length];

        for (int i = 0; i < string.length; i++) {
            number[i] = Integer.parseInt(string[i]);
        }
        return number;
    }

}
