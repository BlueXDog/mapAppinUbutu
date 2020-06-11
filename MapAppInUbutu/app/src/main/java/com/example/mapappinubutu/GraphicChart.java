package com.example.mapappinubutu;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphicChart extends AppCompatActivity {

    TextView txtTime,txtNhietDo,txtDoam,txtDoBui;
    private LineChart doamChart;
    private LineChart nhietdoChart;
    private LineChart dobuiChart;

    private DatabaseReference mDatabase;
    String DATABASE_NAME="weatherinfo.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase weatherInfoDatabase=null;

    private static final int  doamIndex = 1;
    private static final int nhietdoIndex = 3;
    private static final int dobuiIndex = 2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_chart);
        addControls();
        addEvents();






    }

    private void addEvents() {

        List<Entry> doamEntries=new ArrayList<Entry>();
        List<Entry> nhietdoEntries=new ArrayList<Entry>();
        List<Entry> dobuiEntries=new ArrayList<Entry>();

       /* int nhietdo=20;
        int thoigian=1;
        for (int i=0;i<12;i++)
        {
            entries.add(new Entry(thoigian,nhietdo));
            nhietdo++;
            thoigian++;
        }
        LineDataSet dataSet=new LineDataSet(entries,"nhiet do theo time");
        dataSet.setColor(Color.BLUE);
        LineData lineData=new LineData(dataSet);
        nhietdoChart.setData(lineData);
        nhietdoChart.invalidate();
       */
       try {

           weatherInfoDatabase = openOrCreateDatabase("weatherinfo.sqlite", MODE_PRIVATE, null);
           SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - EEE, dd MMM yyyy");
           Cursor cursor = weatherInfoDatabase.rawQuery("select * from hvnnInfo", null);
           Log.i("rowNumber", "the number of row =" + cursor.getCount());
           while (cursor.moveToNext()) {
               long epocTime = (long) cursor.getInt(0)*1000l;

               Log.i("readDatachart", "updateTime :" +epocTime);
              // Log.i("readDatachart", "doam :" + cursor.getFloat(1));

               Date date = new Date(epocTime);

               int hour = date.getHours();
               Log.i("readDatachart", "the hour :" + hour);
               Log.i("readDatachart","time format :"+sdf.format(date));
               doamEntries.add(new Entry(hour,cursor.getFloat(doamIndex)));
               dobuiEntries.add(new Entry(hour,cursor.getFloat(dobuiIndex)));
               nhietdoEntries.add(new Entry(hour,cursor.getFloat(nhietdoIndex)));

           }


           LineDataSet doamDataSet=new LineDataSet(doamEntries,"do am theo time ");
           LineDataSet nhietdoDataSet=new LineDataSet(nhietdoEntries,"nhiet do theo time ");
           LineDataSet dobuiDataSet=new LineDataSet(dobuiEntries," do bui theo time");

           doamDataSet.setColor(Color.GREEN);
           nhietdoDataSet.setColor(Color.RED);
           dobuiDataSet.setColor(Color.GRAY);

           doamChart.setData(new LineData(doamDataSet));
           nhietdoChart.setData(new LineData(nhietdoDataSet));
           dobuiChart.setData(new LineData(dobuiDataSet));

           Cursor cursorLatest=weatherInfoDatabase.rawQuery( "select *from hvnnInfo order by updateTime DESC",null);
           if (cursorLatest.moveToNext())
           {
               long lastestTime=(long) cursorLatest.getInt(0)*1000l;
               Date date=new Date(lastestTime);
               txtTime.setText(sdf.format(date));
               txtDoBui.setText(""+cursorLatest.getFloat(dobuiIndex));
               txtDoam.setText(""+cursorLatest.getFloat(doamIndex));
               txtNhietDo.setText(""+cursorLatest.getFloat(nhietdoIndex));
           }

           weatherInfoDatabase.close();
       }
       catch (Exception e)
       {
           Log.i("databaseError","loi doc du lieu") ;
       }

    }

    private void addControls() {
        doamChart=findViewById(R.id.doamChart);
        nhietdoChart=findViewById(R.id.nhietdoChart);
        dobuiChart=findViewById(R.id.dobuiChart);

        txtTime=findViewById(R.id.txtTime);
        txtNhietDo=findViewById(R.id.txtNhietDo);
        txtDoam=findViewById(R.id.txtDoAm);
        txtDoBui=findViewById(R.id.txtDoBui);
    }
}
