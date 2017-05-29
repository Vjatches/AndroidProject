package pl.krakow.uek.project_android;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Graph extends AppCompatActivity {
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    DB db;
    SQLiteDatabase sqLiteDatabase;
    Intent intent;
    String input_currency,input_direction;
    Double input_rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graph = (GraphView)findViewById(R.id.graph);
        db = new DB(this);
        sqLiteDatabase = db.getWritableDatabase();

        intent = getIntent();
        input_currency = intent.getStringExtra("currency");
        input_rate = Double.parseDouble(intent.getStringExtra("rate"));
        input_direction = intent.getStringExtra("direction");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = sdf.format(new Date());

        db.insertData(input_rate,input_currency,input_direction,day);
        try {
            series = new LineGraphSeries<DataPoint>(getData());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setNumVerticalLabels(5);

        //graph.getViewport().setMinX(d1.getTime());
        //graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    private DataPoint[] getData() throws ParseException {;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT day,rate,currency,direction FROM currencyInfo WHERE (datetime(day)>=datetime('now','-5 days') AND datetime(day)<=datetime('now')) AND currency==" + "'" + input_currency + "'" + " AND direction==" + "'" + input_direction + "'" + " ORDER BY day",null);
        DataPoint[] dp = new DataPoint[cursor.getCount()];
        for(int i = 0; i<cursor.getCount(); i++){
            cursor.moveToNext();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(cursor.getString(0));
            dp[i] = new DataPoint(date,cursor.getDouble(1));
        }
        return dp;

    }

}
