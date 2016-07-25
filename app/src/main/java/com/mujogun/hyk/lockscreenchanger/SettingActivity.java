package com.mujogun.hyk.lockscreenchanger;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mujogun.hyk.lockscreenchanger.R;

/**
 * Created by hyk on 2016-07-20.
 */
public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);


    }



}
