package com.mujogun.hyk.lockscreenchanger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by leehk on 2016-08-03.
 */
public class SelectMemoColor extends DialogFragment {


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
            listener[i].set("Set" + String.valueOf(i));
            color[i].setOnClickListener(listener[i]);
        }


//
// Dialog Builder에 Layout View를 할당.
//
        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        builder.setTitle("글자색을 선택하세요");

//
// Custom Dialog Display
//
        alertDialog = builder.create();





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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("MemoColor", putColor);
            editor.commit();
            dismiss();
        }
    }
}
