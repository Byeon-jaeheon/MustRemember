package com.mujogun.hyk.lockscreenchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mujogun.hyk.lockscreenchanger.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

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
        ViewHolder holder = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memo_list_item, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(holder);

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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            int fontsize = Integer.parseInt(prefs.getString("memo_font_size", "17"));
            text1.setTextSize(fontsize);
            text2.setTextSize(fontsize);
            text3.setTextSize(16);
            setColor(context, convertView);




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

        holder = (ViewHolder) convertView.getTag();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int fontsize = Integer.parseInt(prefs.getString("memo_font_size", "17"));
        text1.setTextSize(fontsize);
        text2.setTextSize(fontsize);
        text3.setTextSize(16);
        setColor(context, convertView);

        return convertView;
    }
    public void setMemoColor(Context context, View convertView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        TextView text2 = (TextView) convertView.findViewById(R.id.textView2);


    }


    public void setColor(Context context, View convertView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        DBHelper helper = new DBHelper(context, "memo.db", null, 1);
        helper.open();

        TextView text1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView text2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView text3 = (TextView) convertView.findViewById(R.id.textView3);
        // 그 텍스트만 되야함
        Cursor cursor = helper.select(Integer.parseInt((String)text1.getText()));
        cursor.moveToFirst();
        Log.d("color", cursor.getString(3));
        if (cursor.getString(0).compareTo((String)text1.getText())== 0)
        {
            if (cursor.getString(3).compareTo("Set1") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet1Memo));
            else if (cursor.getString(3).compareTo("Set2") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet2Memo));
            else if (cursor.getString(3).compareTo("Set3") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet3Memo));
            else if (cursor.getString(3).compareTo("Set4") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet4Memo));
            else if (cursor.getString(3).compareTo("Set5") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet5Memo));
            else if (cursor.getString(3).compareTo("Set6") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet6Memo));
            else if (cursor.getString(3).compareTo("Set7") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet7Memo));
            else if (cursor.getString(3).compareTo("Set8") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet8Memo));
            else if (cursor.getString(3).compareTo("Set9") == 0)
                text2.setTextColor(context.getResources().getColor(R.color.colorSet9Memo));
        }
        helper.close();


        if (prefs.getString("FontColor", "Set1").compareTo("Set1") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorMint));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorMint));


        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set2") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet2dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet2dday));

        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set3") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet3dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet3dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set4") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet4dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet4dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set5") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet5dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet5dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set6") == 0) {
           GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet6dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet6dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set7") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet7dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet7dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set8") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet8dday));
            bgShape.setStroke(2, context.getResources().getColor(R.color.colorSet8dday));
        }
        else if (prefs.getString("FontColor", "Set1").compareTo("Set9") == 0) {

            GradientDrawable bgShape = (GradientDrawable)text3.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.colorSet9dday));
            bgShape.setStroke(2,context.getResources().getColor(R.color.colorSet9dday));
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

    public int find(String msg) {
        int i = 0;
        for (int j = 0; j < m_list.size(); j++) {
            if (m_list.get(j).getData2().compareTo(msg) == 0)
                return j;
        }

        return -1;

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    private static class ViewHolder {
        private TextView textView1 = null;
        private TextView textView2 = null;
        private TextView textView3 = null;

    }

}
