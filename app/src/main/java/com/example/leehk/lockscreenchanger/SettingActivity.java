package com.example.leehk.lockscreenchanger;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by leehk on 2016-07-20.
 */
public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);


    }



}
