package com.mujogun.hyk.lockscreenchanger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mujogun.hyk.lockscreenchanger.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hyk on 2016-07-18.
 */
public class MemoActivity extends AppCompatActivity {
    DBHelper helper;
    Intent theIntent;
    EditText Memo;
    Calendar mainCalendar;
    public static int isCalendarSet;
    static Calendar myCalendar = Calendar.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative);
        helper = new DBHelper(getApplicationContext(), "memo.db", null, 1);
        helper.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevactivity = new Intent(MemoActivity.this, ConfigActivity.class);

                startActivity(prevactivity);
            }
        });


        Memo = (EditText) findViewById(R.id.editText);
        TextView DatePick = (TextView) findViewById(R.id.datepick);
        TextView ColorPick = (TextView) findViewById(R.id.colorpick);
        theIntent = getIntent();
        String curmemo = theIntent.getStringExtra("memos");
        String targetdate = theIntent.getStringExtra("time");

        if (targetdate != null)
            DatePick.setText(targetdate);
        if (curmemo != null)
            Memo.setText(curmemo);


        ColorPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SelectMemoColor selectMemoColor = new SelectMemoColor();

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
               selectMemoColor.show(fm, "ss");
            }
        });

        Button btn1 = (Button) findViewById(R.id.save);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Memo = (EditText) findViewById(R.id.editText);

                TextView realdate = (TextView) findViewById(R.id.datepick);
                String from = (String) realdate.getText();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");
                Date to = Calendar.getInstance().getTime();
                try {
                    to = transFormat.parse(from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String sid = theIntent.getStringExtra("id");
                String curmemo = Memo.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");

                String currentdate = sdf.format(Calendar.getInstance().getTime());

                Date realcurrentDate = Calendar.getInstance().getTime();
                /*
                String ddate = formatedDate;
                */
                String ddate = sdf.format(to);

                if (from.compareTo("") == 0)
                    ddate = "";


                if (sid == null) {
                    helper.insert(curmemo, ddate);
                }
                else {
                    helper.update(sid, curmemo, ddate);
                }
                helper.close();


                //db에 저장
                Intent prevactivity = new Intent(MemoActivity.this, ConfigActivity.class);

                startActivity(prevactivity);
            }
        });
        Button btn2 = (Button) findViewById(R.id.delete);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sid = theIntent.getStringExtra("id");
                helper.delete(sid);
                helper.close();
                Intent prevactivity2 = new Intent(MemoActivity.this, ConfigActivity.class);
                startActivity(prevactivity2);
            }
        });







    }


    @Override
    protected void onResume() {
        super.onResume();

        TextView datepick = (TextView) findViewById(R.id.datepick);



        datepick.setOnClickListener(new datepickListener());

    }

    public class cancellistener implements DialogInterface.OnCancelListener {


        @Override
        public void onCancel(DialogInterface dialogInterface) {

            Toast.makeText(getApplicationContext(), "cancel detected", Toast.LENGTH_SHORT).show();

        }
    }
    public class datepickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DialogFragment newFragment = new DatePickerFragment();

            newFragment.show(getSupportFragmentManager(), "dateSpinner");



        }

    }



    private  void updateLabel() {

        String myFormat = "yyyy년 M월 d일 H시 mm분"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TextView datepick = (TextView) findViewById(R.id.datepick);
        datepick.setText(sdf.format(myCalendar.getTime()));
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

                TextView realdate = (TextView) findViewById(R.id.datepick);
                String from = (String) realdate.getText();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");
                Date to = Calendar.getInstance().getTime();
                try {
                    to = transFormat.parse(from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String sid = theIntent.getStringExtra("id");
                String curmemo = Memo.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");

                String currentdate = sdf.format(Calendar.getInstance().getTime());

                Date realcurrentDate = Calendar.getInstance().getTime();
                /*
                String ddate = formatedDate;
                */
                String ddate = sdf.format(to);

                if (from.compareTo("") == 0)
                    ddate = "";


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


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        TextView textView;

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.textView = (TextView) activity.findViewById(R.id.datepick);
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog x = new DatePickerDialog(getActivity(),  AlertDialog.THEME_HOLO_LIGHT ,this, year, month, day);
            x.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do stuff
                            textView.setText("");
                        }
                    });


            return x;
        }




        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);

            TimePickerFragment timepick = new TimePickerFragment();
            timepick.show(getFragmentManager(), "timeSpinner");
        }
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        TextView textView;

        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.textView = (TextView) activity.findViewById(R.id.datepick);
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog x = new TimePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            x.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do stuff
                            textView.setText("");
                        }
                    });

            // Create a new instance of TimePickerDialog and return it
            return x;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
            String myFormat = "yyyy년 M월 d일 H시 mm분"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

            textView.setText(sdf.format(myCalendar.getTime()));
        }

    }


}
