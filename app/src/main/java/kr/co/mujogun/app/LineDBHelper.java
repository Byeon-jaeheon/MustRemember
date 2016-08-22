package kr.co.mujogun.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by leehk on 2016-08-19.
 */
public class LineDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase myDb;
    String table = "Lines";

    public LineDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =
                "CREATE TABLE Lines (_id INTEGER PRIMARY KEY AUTOINCREMENT, line TEXT, category TEXT); ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
       /*
        switch (oldVersion) {
            case 1:
                try {
                    myDb.beginTransaction();
                    myDb.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + KEY_IMPORTANT + " Integer DEFAULT 0");
                    myDb.setTransactionSuccessful();
                } catch (IllegalStateException e) {
                    Log.e(TAG, CLASS, e);
                } finally {
                    myDb.endTransaction();
                }
                ;
                break;
        }
        */
    }
    public void open() {
        myDb = this.getWritableDatabase();
    }
    public void insert(String line, String category) {

        String sql = "INSERT INTO Lines (line, category) values (?, ?);";
        Object[] bindArgs = {line, category};
        myDb.execSQL(sql, bindArgs);


    }
    public Cursor select(int no) {
        Cursor cursor = myDb.query("Lines", null, "_id=?", new String[]{String.valueOf(no)}, null, null, null);
        return cursor;
    }
    public Cursor selectAll() {
        Cursor cursor = myDb.query("Lines", null, null, null, null, null, null);
        return cursor;
    }

    public void delete(String id) {
        String sql = "DELETE FROM Lines where _id=?";
        myDb.execSQL(sql, new String[]{id});
    }
}