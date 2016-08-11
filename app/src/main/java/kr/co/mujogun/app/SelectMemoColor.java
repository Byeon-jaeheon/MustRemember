package kr.co.mujogun.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.WindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by leehk on 2016-08-03.
 */
public class SelectMemoColor extends DialogFragment {

    public boolean setted = false;
    public String sid;


    public void setSid(String sid) {
        this.sid = sid;
    }

    public int StringtoID (String str) {
        if (str.compareTo("R.id.color1") == 0)
            return R.id.color1;
        else if (str.compareTo("R.id.color2") == 0)
            return R.id.color2;
        else if (str.compareTo("R.id.color3") == 0)
            return R.id.color3;
        else if (str.compareTo("R.id.color4") == 0)
            return R.id.color4;
        else if (str.compareTo("R.id.color5") == 0)
            return R.id.color5;
        else if (str.compareTo("R.id.color6") == 0)
            return R.id.color6;
        else if (str.compareTo("R.id.color7") == 0)
            return R.id.color7;
        else if (str.compareTo("R.id.color8") == 0)
            return R.id.color8;
        else
            return R.id.color9;




    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

/*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("MemoColor", "NotSet");
        editor.commit();
        */


        AlertDialog.Builder builder;

//
// 사용할 Dialog를 선언
//
        AlertDialog alertDialog;

//
// Context를 가져옴.
//
        Context mContext = getActivity();


//
// Layout Inflater로 View를 가져옴.
//
        LayoutInflater inflater
                = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.set_fontcolor_memo, null);

// Layout에 있는 TextView및 ImageView에 아이콘 및 Text지정
//

        ImageView color[] = new ImageView[9];
        imageClickListener listener[] = new imageClickListener[9];

        for (int i= 0; i < 9; i++) {
            color[i] = (ImageView) layout.findViewById(StringtoID("R.id.color" + String.valueOf(i+1)));
            listener[i] = new imageClickListener();
            listener[i].set("Color" + String.valueOf(i+1));
            color[i].setOnClickListener(listener[i]);
        }

//
// Dialog Builder에 Layout View를 할당.
//
        builder = new AlertDialog.Builder(mContext);
        layout.setBackgroundColor(Color.parseColor("#f1f1f1"));
        builder.setView(layout);

        TextView title = new TextView(getContext());
// You Can Customise your Title here
        title.setText("글자색을 선택하세요");
        title.setBackgroundColor(Color.RED);
        title.setHeight(220);
        title.setPadding(20, 20, 20, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setCustomTitle(title);





//
// Custom Dialog Display
//
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        alertDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);


        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("이 세트로 바꾸시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("FontColor", fontcolor);
                        editor.commit();


                    }
                })
                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

                */
        // Create the AlertDialog object and return it
        return alertDialog;
    }


    public class imageClickListener implements View.OnClickListener {

        String putColor;

        public void set(String str) {
            this.putColor = str;
        }
        @Override
        public void onClick(View view) {
            // sid가 null이면 그냥 추가, 기존에 없으니// 아니면 수정
            if (sid == null || (sid.compareTo("") == 0)) {

            // sid가 null이니까 db 업데이트 불가능, 따라서 preference 이용
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("MemoColor", putColor);
                editor.commit();

            }
            else {
                DBHelper helper = new DBHelper(getContext(), "memo.db", null, 1);
                helper.open();
                helper.updateColor(sid, putColor);
                helper.close();
            }
            TextView colorpick = (TextView)getActivity().findViewById(R.id.colorpick);
            if (putColor.compareTo("Color1") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet1Background));
                colorpick.setText("");
            }
            else if (putColor.compareTo("Color2") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet2Background));
                colorpick.setText("");
            }
            else if (putColor.compareTo("Color3") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet3Background));
                colorpick.setText("");
            }
            else if (putColor.compareTo("Color4") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet4Background));
                colorpick.setText("");
            }
            else if (putColor.compareTo("Color5") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet5Background));
                colorpick.setText("");

            }
            else if (putColor.compareTo("Color6") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet6Background));
                colorpick.setText("");
            }
            else if (putColor.compareTo("Color7") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet7Background));
                colorpick.setText("");

            }
            else if (putColor.compareTo("Color8") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                colorpick.setText("");

            }
            else if (putColor.compareTo("Color9") == 0) {
                colorpick.setBackgroundColor(getResources().getColor(R.color.colorSet9Background));
                colorpick.setText("");
            }

            dismiss();

        }
    }
}
