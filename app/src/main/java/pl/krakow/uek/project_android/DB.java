package pl.krakow.uek.project_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Yarikk on 28.05.2017.
 */

public class DB extends SQLiteOpenHelper {
    private Context con;
    public DB(Context context) {
        super(context, "MyDB4", null, 1);
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table if not exists currencyInfo(id integer primary key,rate DOUBLE,currency TEXT,direction TEXT,day TEXT)";
        db.execSQL(createTable);
        Toast.makeText(con,"currencyInfo created",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS currencyInfo");
        onCreate(sqLiteDatabase);
    }

    public void insertData(Double rate,String currency,String direction,String day){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("rate",rate);
        contentValues.put("currency",currency);
        contentValues.put("direction",direction);
        contentValues.put("day",day);


        db.insert("currencyInfo",null,contentValues);
        Toast.makeText(con,"currencyInfo inserted",Toast.LENGTH_LONG).show();
    }
}