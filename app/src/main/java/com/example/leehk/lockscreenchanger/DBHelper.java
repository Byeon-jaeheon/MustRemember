package com.example.leehk.lockscreenchanger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leehk on 2016-07-19.
 */
public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase myDb;
    String table = "memo";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =
                "CREATE TABLE memo (_id INTEGER PRIMARY KEY AUTOINCREMENT, memos TEXT, time TEXT); ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void open() {
        myDb = this.getWritableDatabase();
    }
    public void insert(String memo, String time) {

        String sql = "INSERT INTO memo (memos, time) values (?, ?);";
        Object[] bindArgs = {memo, time };
        myDb.execSQL(sql, bindArgs);


    }
    public Cursor select(int no) {
        Cursor cursor = myDb.query("memo", null, "_id=?", new String[]{String.valueOf(no)}, null, null, null);
        return cursor;
    }
    public Cursor selectAll() {
        Cursor cursor = myDb.query("memo", null, null, null, null, null, null);
        return cursor;
    }
    public void update(String id, String newMemo, String time) {
        ContentValues values = new ContentValues();
        values.put("memos", newMemo);
        values.put("time", time);
        String[] whereArgs={id};
        int update = myDb.update("memo", values, "_id=?", whereArgs);

    }

    public void delete(String id) {
        String sql = "DELETE FROM memo where _id=?";
        myDb.execSQL(sql, new String[]{id});
    }
}
