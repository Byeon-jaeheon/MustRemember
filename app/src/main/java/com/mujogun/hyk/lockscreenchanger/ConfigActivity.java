package com.mujogun.hyk.lockscreenchanger;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mujogun.hyk.lockscreenchanger.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hyk on 2016-07-12.
 */
public class ConfigActivity extends FragmentActivity {

    private Button onBtn, memoBtn, fontBtn, helpBtn;
    private DigitalClock watch;
    private static final int SELECT_PHOTO = 100;
    private static int COUNTER_FOR_SCREEN = 0;

    private static final String DB_NAME = "memo.db";
    private static final String DB_TABLE = "memo";

    private static Bitmap yourSelectedImage;
    private static String ret;
    public static int unlocked = 0;
    DBHelper helper;
    phpDown task;
    register rl;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static String regId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String[] perms = {"android.permission. WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        helper = new DBHelper(getApplicationContext(), "memo.db", null, 1);
        helper.open();
        int permsRequestCode = 200;
/*
        if (Build.VERSION.SDK_INT >= 23)
            if (this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_DENIED)
                requestPermissions(perms, permsRequestCode);
*/

        if (ContextCompat.checkSelfPermission((this), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(perms, permsRequestCode);
            }
            else {
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(perms, permsRequestCode);
            }

        }else {

        }


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        TextView line = (TextView) findViewById(R.id.line);
        Drawable alpha = line.getBackground();
        alpha.setAlpha(100);

        TextView touchspace = (TextView) findViewById(R.id.touchspace);
        touchspace.setTextColor(Color.WHITE);

        LinearLayout relate1 = (LinearLayout) findViewById(R.id.relative1);
        relate1.setBackgroundColor(Color.BLACK);
        Drawable alpha3 = relate1.getBackground();
        alpha3.setAlpha(150);
        ImageView lock = (ImageView) findViewById(R.id.lock) ;
        lock.getBackground().setAlpha(5);
        Toast.makeText(this, String.valueOf(lock.getWidth()), Toast.LENGTH_SHORT ).show();

        onBtn= (Button)findViewById(R.id.btn1);
        memoBtn = (Button)findViewById(R.id.btn2);
        fontBtn = (Button)findViewById(R.id.btn3);
        helpBtn = (Button)findViewById(R.id.btn4);
        onBtn.setScaleX((float)0.8);
        onBtn.setScaleY((float)0.8);
        memoBtn.setScaleX((float)0.8);
        memoBtn.setScaleY((float)0.8);
        fontBtn.setScaleX((float)0.8);
        fontBtn.setScaleY((float)0.8);
        helpBtn.setScaleX((float)0.8);
        helpBtn.setScaleY((float)0.8);




        watch = (DigitalClock)findViewById(R.id.clock);

        Intent intent = new Intent(this, ScreenService.class);
        startService(intent);


        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COUNTER_FOR_SCREEN +=1;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent memoIntent = new Intent(ConfigActivity.this, MemoActivity.class);
                startActivity(memoIntent);
            }
        });
        fontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(ConfigActivity.this, SettingActivity.class);
                startActivity(settingIntent);
            }
        });
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Help help = new Help();

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                help.show(fm, "aa");
            }
        });
        registBroadcastReceiver();

        if (checkPlayServices())
            getInstanceIdToken();





    }

    public void getInstanceIdToken() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals("registrationComplete")){

                    String token = intent.getStringExtra("token");
                    regId = token;
                    rl = new register();
                    rl.execute();



                }
            }
        };
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE


                            |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
/*
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
*/
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
/*
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
*/
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                            );
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (COUNTER_FOR_SCREEN == 0) {
           if (event.getY() > 1300)
                moveTaskToBack(true);



            unlocked = 1;

            return true;
        }
        else
            return false;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ( event.getAction() == KeyEvent.ACTION_DOWN )
        {
            if ( keyCode == KeyEvent.KEYCODE_BACK )
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            if ( keyCode == KeyEvent.KEYCODE_HOME )
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String filename = "imagepath.txt";
                    String filepath = getRealImagePath(selectedImage);
                    File k = new File(filepath);
                    FileOutputStream output;
                    Toast.makeText(getApplicationContext(),"Image Selected", Toast.LENGTH_SHORT).show();


                    try {
                        output = openFileOutput(filename, Context.MODE_WORLD_READABLE);
                        output.write(filepath.getBytes());
                        output.close();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    if (yourSelectedImage == null)
                        Toast.makeText(this, "null image", Toast.LENGTH_SHORT).show();


                    ImageView x = (ImageView) findViewById(R.id.imageView);
                    x.setImageBitmap(yourSelectedImage);

                    Drawable alpha =  ((ImageView) findViewById(R.id.imageView)).getDrawable();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, ScreenService.class);
        stopService(intent);
    }

    public String getRealImagePath (Uri uriPath)

    {
        if (uriPath == null)
            Toast.makeText(this, "null" , Toast.LENGTH_SHORT).show();


        String []proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getApplicationContext().getContentResolver().query(uriPath, proj, null, null, null);


        cursor.moveToFirst();


        int columnIndex = cursor.getColumnIndex(proj[0]);

        String path = cursor.getString(columnIndex);


        return path;

    }



    @Override
    protected void onResume() {
        super.onResume();
        COUNTER_FOR_SCREEN = 0;
/*
            ImageView x = (ImageView) findViewById(R.id.imageView);
            x.setImageBitmap(yourSelectedImage);


        Drawable alpha = ((ImageView) findViewById(R.id.imageView)).getDrawable();
        alpha.setAlpha(0);
        }
        */

        //Read text from file


        try {
            FileInputStream inputStream = openFileInput("imagepath.txt");

            if ( inputStream != null ) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }


        yourSelectedImage = BitmapFactory.decodeFile(ret);

        if (yourSelectedImage == null) {


        }

        if (yourSelectedImage != null) {
            ImageView x = (ImageView) findViewById(R.id.imageView);
            x.setImageBitmap(yourSelectedImage);
        }

        onBtn= (Button)findViewById(R.id.btn1);
        onBtn.bringToFront();

        memoBtn= (Button)findViewById(R.id.btn2);
        memoBtn.bringToFront();

        fontBtn= (Button)findViewById(R.id.btn3);
        fontBtn.bringToFront();

        helpBtn= (Button)findViewById(R.id.btn4);
        helpBtn.bringToFront();

        watch = (DigitalClock)findViewById(R.id.clock);
        watch.bringToFront();

        TextView date = (TextView)findViewById(R.id.date);
        date.bringToFront();

        TextView line = (TextView) findViewById(R.id.line);
        line.bringToFront();


        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일");
        String currentdate = sdf.format(Calendar.getInstance().getTime());
        date.setText(currentdate);

        unlocked = 1;



        Cursor cursor = helper.selectAll();
        cursor.moveToFirst();
        ListView listView = (ListView) findViewById(R.id.listView);
        listAdapter customadapter = new listAdapter();
        listView.setAdapter(customadapter);

        listView.bringToFront();


       while(!cursor.isAfterLast()) {

          customadapter.add(new memo_item(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
           cursor.moveToNext();
       }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor mycursor = helper.selectAll();
                mycursor.move(i+1);
                Intent memoIntent = new Intent(ConfigActivity.this, MemoActivity.class);
                memoIntent.putExtra("id", mycursor.getString(0));
                memoIntent.putExtra("memos", mycursor.getString(1));
                startActivity(memoIntent);

            }
        });



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("memo_visible", Boolean.parseBoolean(null))) {
           listView.setVisibility(View.VISIBLE);
        }
        else {
            listView.setVisibility(View.GONE);

        }
        if (prefs.getBoolean("clock_visible", Boolean.parseBoolean(null))) {
            watch.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
        }
        else {
            watch.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
        }
        if (prefs.getBoolean("line_visible", Boolean.parseBoolean(null))) {
            line.setVisibility(View.VISIBLE);

        }
        else {
            line.setVisibility(View.GONE);

        }

        task = new phpDown();
        task.execute("http://app.mujogun.co.kr/?action=lockscreen");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("registrationComplete"));



    }



    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){


        switch(permsRequestCode){

            case 200:
                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                yourSelectedImage = BitmapFactory.decodeFile(ret);
                ImageView x = (ImageView) findViewById(R.id.imageView);
                x.setImageBitmap(yourSelectedImage);
                onBtn= (Button)findViewById(R.id.btn1);
                onBtn.bringToFront();

                watch = (DigitalClock)findViewById(R.id.clock);
                watch.bringToFront();
                return;

        }

    }

    public String makeJsonData() throws JSONException {
        TelephonyManager telephony = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("action", "login");
        jsonObject1.put("actionType", null);
        jsonObject1.put("country", "KR");
        jsonObject1.put("deviceId", telephony.getDeviceId());
        jsonObject1.put("carrier", telephony.getNetworkOperatorName());
        jsonObject1.put("mobile", telephony.getLine1Number());
        jsonObject1.put("device", telephony.getPhoneType());
        jsonObject1.put("version", telephony.getDeviceSoftwareVersion());
        jsonObject1.put("registId", regId );
        jsonObject1.put("deleteYN", "N");

        String str = jsonObject1.toString();
        System.out.println(str);


        return str;
    }



    private class register extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
           String postMsg = "";


            try {
                postMsg = makeJsonData();
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            OutputStream os = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://app.mujogun.co.kr/");
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(5 * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setChunkedStreamingMode(0);


                System.out.println(postMsg);




                os = conn.getOutputStream();
               os.write(postMsg.getBytes());
                os.flush();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                }


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postMsg;
        }

        @Override
        protected void onPostExecute(String str) {


        }
    }




    private class phpDown extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }

            } catch(Exception ex){
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
        @Override
        protected void onPostExecute(String str) {

            try {
                JSONObject root = new JSONObject(str);
                String connection = root.getString("result");
                if (connection.compareTo("success") == 0) {
                    String jline = root.getString("content");
                   final String jlink = root.getString("link");
                   TextView x = (TextView)findViewById(R.id.line);
                    x.setText(jline);
                   if (jlink.compareTo("") != 0 && jlink.compareTo("null") != 0) {

                       x.setText(Html.fromHtml("<u>" + jline + "</u" ));
                       x.setTextColor(Color.BLUE);
                       x.setClickable(true);
                       x.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jlink));
                               startActivity(browserIntent);
                           }
                       });
                   }

                    else {
                       x.setText(jline);
                       x.setTextColor(Color.BLACK);
                       x.setClickable(false);
                   }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
