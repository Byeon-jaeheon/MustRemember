package kr.co.mujogun.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by leehk on 2016-07-27.
 */
public class FontSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.font_setting);

        FontColorListener test [] = new FontColorListener[6];
        ButtonColorListener buttonListener [] = new ButtonColorListener[6];
        for (int i=0; i < 6; i++) {
            test[i] = new FontColorListener();
            buttonListener[i] = new ButtonColorListener();
        }
        ImageView font[] = new ImageView[6];
        ImageView button[] = new ImageView[6];
        font[0] = (ImageView) findViewById(R.id.fontblack);
        font[1] = (ImageView) findViewById(R.id.fontwhite);
        font[2] = (ImageView) findViewById(R.id.fontblue);
        font[3] = (ImageView) findViewById(R.id.fontgray);
        font[4] = (ImageView) findViewById(R.id.fontorange);
        font[5] = (ImageView) findViewById(R.id.fontred);
        button[0] = (ImageView) findViewById(R.id.buttongray);
        button[1] = (ImageView) findViewById(R.id.buttonwhite);
        button[2] = (ImageView) findViewById(R.id.buttonblue);
        button[3] = (ImageView) findViewById(R.id.buttongreen);
        button[4] = (ImageView) findViewById(R.id.buttonorange);
        button[5] = (ImageView) findViewById(R.id.buttonpurple);
        test[0].set("Color.BLACK");
        buttonListener[0].set("Color.GRAY");
        test[1].set("Color.WHITE");
        buttonListener[1].set("Color.WHITE");
        test[2].set("Color.BLUE");
        buttonListener[2].set("Color.BLUE");
        test[3].set("Color.GRAY");
        buttonListener[3].set("Color.GREEN");
        test[4].set("Color.ORANGE");
        buttonListener[4].set("Color.ORANGE");
        test[5].set("Color.RED");
        buttonListener[5].set("Color.PURPLE");
        for (int i = 0; i < 6; i++) {

            font[i].setOnClickListener(test[i]);
            button[i].setOnClickListener(buttonListener[i]);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevactivity = new Intent(FontSettingActivity.this, SettingActivity.class);

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

    public class ButtonColorListener implements View.OnClickListener {

        String buttonColor;

        public void set(String FontColor) {
            this.buttonColor = FontColor;
        }


        @Override
        public void onClick(View view) {

            ButtonSelectColor buttonSelectColor = new ButtonSelectColor();
            buttonSelectColor.set(buttonColor);

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            buttonSelectColor.show(fm, "Button");


        }
    }
}
