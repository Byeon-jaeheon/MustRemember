package kr.co.mujogun.app;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by leehk on 2016-07-25.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // GCM을 위한 Instance ID를 가져옵니다.
        InstanceID instanceID = InstanceID.getInstance(RegistrationIntentService.this);
        String token = null;
        try {
            //json파일에 들어있는 데이터를 가져와서 토큰을 조회합니다.
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            Intent registrationComplete = new Intent("registrationComplete");
            registrationComplete.putExtra("token", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
