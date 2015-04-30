package com.example.parami.myappride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteData {
    public static final String MYDATABASE_NAME = "watery";
    public static final int MYDATABASE_VERSION = 1;

    public static final String MYDATABASE_TABLE = "MY_WATER";

    public static final String KEY_ID = "_id";
    public static final String KEY_LEVEL = "Target";
    public static final String KEY_AMOUNT = "Amount";
    public static final String KEY_REQ = "Req";
    public static final String KEY_DATE = "Date";
    public static final String KEY_TIME = "Time";


    //create table MY_DATABASE (ID integer primary key, Content text not null);
    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + MYDATABASE_TABLE + " ("
                    + KEY_ID + " integer not null  , "
                    + KEY_LEVEL + " text not null, "+KEY_AMOUNT+" text ,"+KEY_REQ+" text ,"+KEY_DATE+" text ,"+KEY_TIME+" text , primary key ("+KEY_ID+","+KEY_DATE+","+KEY_TIME+"));";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public SQLiteData(Context c){
        context = c;
    }

    public SQLiteData openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteData openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insert(String x,String level,String amount,String req,String date,String time){

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, x);
        contentValues.put(KEY_LEVEL, level);
        contentValues.put(KEY_AMOUNT, amount);
        contentValues.put(KEY_REQ, req);
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_TIME, time);


        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }
    public String gettarget(String id) {
        Cursor cursor = sqLiteDatabase.query("MY_WATER", null, " _id=?", new String[]{id}, null, null, null);
        if (cursor.getCount() < 1) // id Not Exist
        {
            cursor.close();

        }
        cursor.moveToFirst();
        String content1 = cursor.getString(cursor.getColumnIndex("Target"));


        cursor.close();
        return  content1;
    }
    public String getamount(String id,String date) {
        Cursor cursor = sqLiteDatabase.query("MY_WATER", null, " _id=? and Date=? ", new String[]{id, date}, null, null, null);
        if (cursor.getCount() < 1) // id Not Exist
        {
            cursor.close();

        }
        cursor.moveToFirst();
        String content2 = cursor.getString(cursor.getColumnIndex("Amount"));


        cursor.close();
        return  content2;
    }


    public void updateamount(String id,String date,String amount,String pamount) {
        int z=Integer.parseInt(pamount);
        int y=Integer.parseInt(amount);
        int x=y*200;
        int q=z+x;

        String p=String.valueOf(q);

        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("Amount", p);

        sqLiteDatabase.update("MY_WATER", updatedValues,"_id=? and date=?", new String[] { id,date  });

    }
    public Cursor Queue(String id){
        String[] columns=new String[]{KEY_ID,KEY_LEVEL,KEY_AMOUNT};
        Cursor cursor=sqLiteDatabase.query(MYDATABASE_TABLE,columns,"_id=? ", new String[]{id},null,null,null);

        return  cursor;


    }



    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub

            db.execSQL(SCRIPT_CREATE_DATABASE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



        }

    }

}

