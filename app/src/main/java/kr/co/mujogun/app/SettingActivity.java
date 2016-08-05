package kr.co.mujogun.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by mujogun on 2016-07-20.
 */
public class SettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);


    }



}
