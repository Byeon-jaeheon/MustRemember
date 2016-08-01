package com.mujogun.hyk.lockscreenchanger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by leehk on 2016-07-25.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    //메세지를 받았을 때 동작하는 함수입니다.
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("nickname");
        String message = data.getString("msg");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        sendNotification("무조건 기억", message);
    }

    //메세지를 알림으로 보여주는 함수입니다.
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, ConfigActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
