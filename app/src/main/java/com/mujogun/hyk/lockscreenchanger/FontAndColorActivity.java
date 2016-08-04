package com.mujogun.hyk.lockscreenchanger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by leehk on 2016-08-03.
 */
public class FontAndColorActivity extends AppCompatActivity {

    public int StringtoID (String str) {
        if (str.compareTo("R.id.set1") == 0)
            return R.id.set1;
        else if (str.compareTo("R.id.set2") == 0)
            return R.id.set2;
        else if (str.compareTo("R.id.set3") == 0)
            return R.id.set3;
        else if (str.compareTo("R.id.set4") == 0)
            return R.id.set4;
        else if (str.compareTo("R.id.set5") == 0)
            return R.id.set5;
        else if (str.compareTo("R.id.set6") == 0)
            return R.id.set6;
        else if (str.compareTo("R.id.set7") == 0)
            return R.id.set7;
        else if (str.compareTo("R.id.set8") == 0)
            return R.id.set8;
        else
            return R.id.set9;

    }


    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.color_setting);

        FontColorListener test [] = new FontColorListener[9];
       ImageView FontAndColorSet [] = new ImageView[9];

        for(int i = 0; i < 9; i++) {
            test[i] = new FontColorListener();
            FontAndColorSet[i] = (ImageView) findViewById(StringtoID("R.id.set" + String.valueOf(i+1)));
            test[i].set("Set" + String.valueOf(i+1));
            FontAndColorSet[i].setOnClickListener(test[i]);

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevactivity = new Intent(FontAndColorActivity.this, ConfigActivity.class);

                startActivity(prevactivity);
            }
        });







    }

    public class FontColorListener implements View.OnClickListener {

        String FontColor;

        public void set(String FontColor) {
            this.FontColor = FontColor;
        }


        @Override
        public void onClick(View view) {

            FontSelectColor fontSelectColor = new FontSelectColor();
            fontSelectColor.set(FontColor);

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fontSelectColor.show(fm, "font");


        }
    }


}
