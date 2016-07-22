package com.example.leehk.lockscreenchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by leehk on 2016-07-19.
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
            String currentdate = sdf.format(Calendar.getInstance().getTime());
            Date realformatedDate = null;
            try {
                realformatedDate = sdf.parse(m_list.get(position).getData3());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date realcurrentDate = Calendar.getInstance().getTime();
            long dday = daysBetween(realcurrentDate, realformatedDate);
            text3.setText(String.valueOf(dday) + "일 남음");



        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.memo_list_item, parent, false);
        convertView.setAlpha((float) 0.5);
        // TextView에 현재 position의 문자열 추가
        TextView text1 = (TextView) convertView.findViewById(R.id.textView1);
        text1.setText(m_list.get(position).getData1());

        TextView text2 = (TextView) convertView.findViewById(R.id.textView2);
        text2.setText(m_list.get(position).getData2());

        TextView text3 = (TextView) convertView.findViewById(R.id.textView3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String currentdate = sdf.format(Calendar.getInstance().getTime());
        Date realformatedDate = null;
        try {
            realformatedDate = sdf.parse(m_list.get(position).getData3());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date realcurrentDate = Calendar.getInstance().getTime();
        long dday = daysBetween(realcurrentDate, realformatedDate);
        text3.setText(String.valueOf(dday) + "일 남음");
        if (dday == 0) {
            long fday = daysBetween(realformatedDate, realcurrentDate);
            text3.setText(String.valueOf(fday) + "일 지남");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int fontsize = Integer.parseInt(prefs.getString("memo_font_size", null));
        text1.setTextSize(fontsize);
        text2.setTextSize(fontsize);
        text3.setTextSize(fontsize);

        return convertView;
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

    public void add(memo_item msg) {
        m_list.add(msg);
        notifyDataSetChanged();
    }
    public void remove(int _position) {
        m_list.remove(_position);
        notifyDataSetChanged();
    }
}
