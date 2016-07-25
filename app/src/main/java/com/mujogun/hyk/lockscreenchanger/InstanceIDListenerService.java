package com.mujogun.hyk.lockscreenchanger;

import android.content.Intent;

/**
 * Created by leehk on 2016-07-25.
 */
public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
