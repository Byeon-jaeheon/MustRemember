package kr.co.mujogun.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * Created by mujogun on 2016-07-12.
 */
public class ScreenService extends Service {

    private BootReiceiver mReceiver = null;
    private Notification.Builder builder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mReceiver = new BootReiceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        Resources res = getResources();
        Intent pendingintent = new Intent(this, ConfigActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, pendingintent, 0);


       updateFirstMemo();
       Notification notification = builder.getNotification();
        notification.priority = Notification.PRIORITY_MIN;
        startForeground(1, notification);
        if(intent != null){
            if(intent.getAction()==null){
                if(mReceiver==null){
                    mReceiver = new BootReiceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }
        }

        return START_REDELIVER_INTENT;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }




    public String ShowFirstMemo() {

        DBHelper helper = new DBHelper(getApplicationContext(), "memo.db", null, 1);
        helper.open();
        Cursor cursor = helper.selectAll();
        cursor.moveToFirst();
        String thememo = "";
        if (cursor.getCount() < 1)
            thememo = "메모를 등록해주세요";
        else
            thememo = cursor.getString(1);
        helper.close();
        /*
       final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {


                ShowFirstMemo();
                handler.postDelayed(this, 3000);
            }
        });
*/




        return thememo;

    }

    public void updateFirstMemo() {
        Intent pendingintent = new Intent(this, ConfigActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, pendingintent, 0);
        builder = new Notification.Builder(getApplicationContext()).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(R.drawable.small_icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(ShowFirstMemo())
                .setContentIntent(contentIntent);

    }

}
