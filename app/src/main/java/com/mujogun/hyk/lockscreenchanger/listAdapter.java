package com.mujogun.hyk.lockscreenchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mujogun.hyk.lockscreenchanger.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hyk on 2016-07-19.
 */
public class listAdapter extends BaseAdapter{


    private ArrayList<memo_item> m_list;

    public listAdapter() {
        m_list = new ArrayList<memo_item>();
    }
    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int i) {
        return m_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_list_item, parent, false);

            // TextView에 현재 position의 문자열 추가
            TextView text1 = (TextView) convertView.findViewById(R.id.textView1);
            text1.setText(m_list.get(position).getData1());

            TextView text2 = (TextView) convertView.findViewById(R.id.textView2);
            text2.setText(m_list.get(position).getData2());

            TextView text3 = (TextView) convertView.findViewById(R.id.textView3);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");
            String currentdate = sdf.format(Calendar.getInstance().getTime());
            Date realformatedDate = null;

            if (m_list.get(position).getData3().compareTo("") == 0) {
                text3.setVisibility(View.GONE);

            }
            else {
                try {
                    realformatedDate = sdf.parse(m_list.get(position).getData3());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Date realcurrentDate = Calendar.getInstance().getTime();
                if (realcurrentDate.before(realformatedDate)) {
                    String dday = daysBetween(realcurrentDate, realformatedDate);
                    text3.setText(String.valueOf(dday) + "일\n" + "남음");
                }
                else {
                    String fday = daysBetween(realformatedDate, realcurrentDate);
                    text3.setText(String.valueOf(fday) + "일\n" + "지남");
                }
            }



        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.memo_list_item, parent, false);
        convertView.setAlpha((float) 1.0);
        // TextView에 현재 position의 문자열 추가
        TextView text1 = (TextView) convertView.findViewById(R.id.textView1);
        text1.setText(m_list.get(position).getData1());

        TextView text2 = (TextView) convertView.findViewById(R.id.textView2);
        text2.setText(m_list.get(position).getData2());

        TextView text3 = (TextView) convertView.findViewById(R.id.textView3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일 H시 mm분");
        String currentdate = sdf.format(Calendar.getInstance().getTime());
        Date realformatedDate = null;
        if (m_list.get(position).getData3().compareTo("") == 0) {
            text3.setVisibility(View.GONE);

        }
        else {
            try {
                realformatedDate = sdf.parse(m_list.get(position).getData3());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Date realcurrentDate = Calendar.getInstance().getTime();

            if (realcurrentDate.before(realformatedDate)) {
                String dday = daysBetween(realcurrentDate, realformatedDate);
                text3.setText(String.valueOf(dday) + "\n" + "남음");
            }
            else {
                String fday = daysBetween(realformatedDate, realcurrentDate);
                text3.setText(String.valueOf(fday) + "\n" + "지남");
            }
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int fontsize = Integer.parseInt(prefs.getString("memo_font_size", "17"));
        text1.setTextSize(fontsize);
        text2.setTextSize(fontsize);
        text3.setTextSize(16);
        setColor(context, convertView);
        return convertView;
    }


    public void setColor(Context context, View convertView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        TextView text2 = (TextView) convertView.findViewById(R.id.textView2);
        if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.RED") == 0) {
            text2.setTextColor(Color.RED);
        }
        else if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.BLUE") == 0) {
            text2.setTextColor(Color.BLUE);
        }
        else if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.WHITE") == 0) {
            text2.setTextColor(Color.WHITE);
        }
        else if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.GRAY") == 0) {
            text2.setTextColor(Color.GRAY);
        }
        else if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.ORANGE") == 0) {
            text2.setTextColor(Color.parseColor("#ff8800"));
        }
        else if (prefs.getString("FontColor", "Color.BLACK").compareTo("Color.BLACK") == 0) {
            text2.setTextColor(Color.BLACK);
        }
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
    public static String daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        String dday = "";


        long daysBetween = 0;
        long hoursBetween = ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60));

        long minutesBetween = ((endDate.getTime() - startDate.getTime()) / (1000 * 60)) % 60;

        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }

        dday = dday.concat(String.valueOf(daysBetween)).concat("일 ").concat(String.valueOf(hoursBetween)).concat("시간 ").concat(String.valueOf(minutesBetween)).concat("분");
        Log.i("으아아아아", dday);

        if ((hoursBetween < 24) && (hoursBetween >=1) ) {

            return String.valueOf(hoursBetween) + "시간";
        }
        if (hoursBetween < 1) {

            return String.valueOf(minutesBetween) + "분";
        }
        if (hoursBetween > 24)
            return String.valueOf(daysBetween) + "일";


        return String.valueOf("오류");
    }



    public void add(memo_item msg) {
        m_list.add(msg);
        notifyDataSetChanged();
    }
    public void remove(int _position) {
        m_list.remove(_position);
        notifyDataSetChanged();
    }
}
