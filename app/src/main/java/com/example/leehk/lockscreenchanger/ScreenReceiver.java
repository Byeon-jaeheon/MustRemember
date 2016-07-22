package com.example.leehk.lockscreenchanger;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by leehk on 2016-07-12.
 */
public class ScreenReceiver extends BroadcastReceiver{


    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (km == null)
                km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if (keyLock == null)
                keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

            disableKeyguard();

            Intent i = new Intent(context, ConfigActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            context.startActivity(i);

        }


    }

    public void reenableKeyguard() {
        keyLock.reenableKeyguard();
    }

    public void disableKeyguard() {
        keyLock.disableKeyguard();
    }


}
