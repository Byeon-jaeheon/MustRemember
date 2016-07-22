package com.example.leehk.lockscreenchanger;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by leehk on 2016-07-12.
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

        onBtn= (Button)findViewById(R.id.btn1);
        memoBtn = (Button)findViewById(R.id.btn2);
        fontBtn = (Button)findViewById(R.id.btn3);
        helpBtn = (Button)findViewById(R.id.btn4);

       ;

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

        TextView line = (TextView) findViewById(R.id.line);
        line.bringToFront();
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
        }
        else {
            watch.setVisibility(View.INVISIBLE);
        }
        if (prefs.getBoolean("line_visible", Boolean.parseBoolean(null))) {
            line.setVisibility(View.VISIBLE);
        }
        else {
            line.setVisibility(View.INVISIBLE);
        }

        task = new phpDown();
        task.execute("http://app.mujogun.co.kr/?action=lockscreen");
    }



    protected void onPause() {
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
                    String  jname = root.getString("name");
                    String jline = root.getString("content");
                   String jlink = root.getString("link");
                   TextView x = (TextView)findViewById(R.id.line);
                    x.setText(jline + "\n" + jname);
                   if (jlink.compareTo("") != 0)
                       x.append("\n" + jlink);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
