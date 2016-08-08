package kr.co.mujogun.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mujogun on 2016-07-18.
 */
public class MemoActivity extends AppCompatActivity {
    DBHelper helper;
    Intent theIntent;
    EditText Memo;
    Calendar mainCalendar;
    public static int isCalendarSet;
    static Calendar myCalendar = Calendar.getInstance();
    String colorset;
    String colorindividual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative);
        helper = new DBHelper(getApplicationContext(), "memo.db", null, 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        String color = theIntent.getStringExtra("color");
        if (targetdate != null)
            DatePick.setText(targetdate);
        if (curmemo != null)
            Memo.setText(curmemo);


        ColorPick.setOnClickListener(new colorListener());

        Button btn1 = (Button) findViewById(R.id.save);
        btn1.setOnClickListener(new MemoInsertListener());



        Button btn2 = (Button) findViewById(R.id.delete);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.open();
                String sid = theIntent.getStringExtra("id");
                helper.delete(sid);
                helper.close();
                Toast.makeText(getApplicationContext(), "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                Intent prevactivity2 = new Intent(MemoActivity.this, ConfigActivity.class);
                startActivity(prevactivity2);
            }
        });







    }


    @Override
    protected void onResume() {
        super.onResume();

        TextView datepick = (TextView) findViewById(R.id.datepick);


        Button btn1 = (Button) findViewById(R.id.save);
        Button btn2 = (Button) findViewById(R.id.delete);

        datepick.setOnClickListener(new datepickListener());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String backgroundcolor = prefs.getString("Background", "Set8");

        GradientDrawable bgShape1 = (GradientDrawable)btn1.getBackground();
        GradientDrawable bgShape2 = (GradientDrawable)btn2.getBackground();

        Log.i("memobutton", backgroundcolor);
        if (backgroundcolor.compareTo("Set1")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet1Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet1Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));

        }
        else if (prefs.getString("Background", "Set8").compareTo("Set2")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet2Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet2Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set3")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet3Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet3Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set4")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet4Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet4Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set5")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet5Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet5Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set6")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet6Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet6Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set7")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet7Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet7Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set8")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet8Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet8Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }
        else if (prefs.getString("Background", "Set8").compareTo("Set9")==0) {
            bgShape1.setColor(getResources().getColor(R.color.colorSet9Background));
            bgShape1.setStroke(0, Color.parseColor("#0d1825"));
            bgShape2.setColor(getResources().getColor(R.color.colorSet9Background));
            bgShape2.setStroke(0, Color.parseColor("#0d1825"));
        }




    }


    public class datepickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DialogFragment newFragment = new DatePickerFragment();

            newFragment.show(getSupportFragmentManager(), "dateSpinner");
        }

    }
    public class colorListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String sid = theIntent.getStringExtra("id");
            SelectMemoColor selectMemoColor = new SelectMemoColor();
            selectMemoColor.setSid(sid);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            selectMemoColor.show(fm, "ss");
            // 변경할 때
            if (sid != null) {
                Log.i("change", "Change color with exist memo");
                helper.open();
                Cursor cursor = helper.select(Integer.parseInt(sid));
                cursor.moveToFirst();
                colorindividual = cursor.getString(3);
                helper.close();
                // 검->흰은 되는데 흰->검은 안됨?
            }

                        /*

           */

            // 추가할 때
            if (sid == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                colorindividual = prefs.getString("MemoColor", "NotSet");
            }

        }

    }
    public class MemoInsertListener implements View.OnClickListener {

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

            if (from.compareTo("일정설정") == 0)
                ddate = "일정설정";


            helper.open();

            if (sid == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // color 선택한 경우 먼저 체크
                colorindividual = prefs.getString("MemoColor", "NotSet");

                if (colorindividual.compareTo("NotSet") == 0) {
                    colorindividual = prefs.getString("FontColor", "Set1");
                    Log.i("Individual", colorindividual);
                }
                // 이렇게 되면 기존의 메모세트 컬러 고른 거가 디폴트로 안됨;;
                helper.insert(curmemo, ddate, colorindividual);
            }
            else {
                Cursor cursor = helper.select(Integer.parseInt(sid));
                cursor.moveToFirst();
                colorindividual = cursor.getString(3);
                helper.update(sid, curmemo, ddate, colorindividual);
            }
            helper.close();
            Toast.makeText(getApplicationContext(), "메모가 추가되었습니다.", Toast.LENGTH_SHORT).show();



            //db에 저장
            Intent prevactivity = new Intent(MemoActivity.this, ConfigActivity.class);

            startActivity(prevactivity);



        }

    }



    private  void updateLabel() {

        String myFormat = "yyyy년 M월 d일 H시 mm분"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TextView datepick = (TextView) findViewById(R.id.datepick);
        datepick.setText(sdf.format(myCalendar.getTime()));
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
                            textView.setText("일정설정");
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
                            textView.setText("일정설정");
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