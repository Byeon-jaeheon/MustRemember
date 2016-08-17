package kr.co.mujogun.app;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by mujogun on 2016-07-14.
 */
public class BootReiceiver extends BroadcastReceiver {

    private KeyguardManager km = null;
    private KeyguardManager.KeyguardLock keyLock = null;
    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;
    private Activity activity;
    public static int abovepatterndetecter= 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals((Intent.ACTION_BOOT_COMPLETED))) {



            Intent i = new Intent(context, ConfigActivity.class);
            i.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(i);
        }
        if (km == null)
            km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyLock == null)
            keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
/*
        if (km.isKeyguardSecure()) {
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                if (km == null)
                    km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                if (keyLock == null)
                    keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

                if(telephonyManager == null){
                    telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                }
                if (isPhoneIdle) {
                    disableKeyguard();

                    Intent i = new Intent(context, ConfigActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    context.startActivity(i);
                }

            }

        }
        else {
*/

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (km == null)
                    km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

                if (keyLock == null)
                    keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

                if (telephonyManager == null) {
                    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                }
                if (isPhoneIdle) {
                    disableKeyguard();

                    Intent i = new Intent(context, ConfigActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    context.startActivity(i);
                }



            }

        if (km.isKeyguardSecure()) {


            if (isPhoneIdle) {

                Intent i = new Intent(context, ConfigActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


                context.startActivity(i);
            }

        }



    }
    public void reenableKeyguard() {
        keyLock.reenableKeyguard();
    }

    public void disableKeyguard() {
        keyLock.disableKeyguard();
    }

    private PhoneStateListener phoneListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE :
                    isPhoneIdle = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING :
                    isPhoneIdle = false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK :
                    isPhoneIdle = false;
                    break;
            }
        }
    };

}
