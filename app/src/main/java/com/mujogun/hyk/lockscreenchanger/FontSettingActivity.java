package com.mujogun.hyk.lockscreenchanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by leehk on 2016-07-27.
 */
public class FontSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.font_setting);

        LinearLayout fontsetting1 = (LinearLayout) findViewById(R.id.FONT_SETTING1);
        fontsetting1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                startActivity(intent);
            }
        });




    }
}
