package com.example.leehk.lockscreenchanger;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leehk on 2016-07-18.
 */
public class MemoActivity extends AppCompatActivity {
    DBHelper helper;
    Intent theIntent;
    EditText Memo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative);
        helper = new DBHelper(getApplicationContext(), "memo.db", null, 1);
        helper.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Memo = (EditText) findViewById(R.id.editText);
        theIntent = getIntent();
        String curmemo = theIntent.getStringExtra("memos");
        if (curmemo != null)
            Memo.setText(curmemo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.menu_add:


                EditText Memo = (EditText) findViewById(R.id.editText);
                DatePicker date = (DatePicker) findViewById(R.id.datePicker) ;
                String sid = theIntent.getStringExtra("id");
                String curmemo = Memo.getText().toString().trim();
                int   day  = date.getDayOfMonth();
                int   month= date.getMonth();
                int   year = date.getYear() - 1900;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
                String formatedDate = sdf.format(new Date(year, month, day));
                String currentdate = sdf.format(Calendar.getInstance().getTime());
                Date realformatedDate = new Date(year, month, day);
                Date realcurrentDate = Calendar.getInstance().getTime();
                long dday = daysBetween(realcurrentDate, realformatedDate);
                String ddate = formatedDate;

                if (sid == null) {
                    helper.insert(curmemo, ddate);
                }
                else {
                    helper.update(sid, curmemo, ddate);
                }
                helper.close();


                //db에 저장
                Intent prevactivity = new Intent(this, ConfigActivity.class);

                startActivity(prevactivity);
                return true;
            case R.id.menu_add2:
                sid = theIntent.getStringExtra("id");
                helper.delete(sid);
                helper.close();
                Intent prevactivity2 = new Intent(this, ConfigActivity.class);
                startActivity(prevactivity2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }
    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
