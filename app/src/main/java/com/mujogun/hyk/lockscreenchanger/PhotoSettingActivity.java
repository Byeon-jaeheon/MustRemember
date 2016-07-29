package com.mujogun.hyk.lockscreenchanger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by leehk on 2016-07-29.
 */
public class PhotoSettingActivity extends AppCompatActivity implements select.TestDialogFragmentListener {
    public static final int select_photo = 120;
    Intent PhotoPickerIntent;

    Bitmap b;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.photo_setting);
        ImageView default1 = (ImageView) findViewById(R.id.default1);
        ImageView default2 = (ImageView) findViewById(R.id.default2);
        ImageView default3 = (ImageView) findViewById(R.id.default3);
        ImageView default4 = (ImageView) findViewById(R.id.default4);
        ImageView default5 = (ImageView) findViewById(R.id.default5);
        ImageView select_image = (ImageView) findViewById(R.id.select_image);

        default1.setOnClickListener(new setBackgroundlistener());
        default2.setOnClickListener(new setBackgroundlistener());
        default3.setOnClickListener(new setBackgroundlistener());
        default4.setOnClickListener(new setBackgroundlistener());
        default5.setOnClickListener(new setBackgroundlistener());
        select_image.setOnClickListener(new setBackgroundlistener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevactivity = new Intent(PhotoSettingActivity.this, ConfigActivity.class);

                startActivity(prevactivity);
            }
        });


    }

    public class setBackgroundlistener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            select select = new select();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            switch(view.getId()) {
                case R.id.default1:
                    select.set(R.id.default1);
                    select.show(fm, "select_image");
                    break;
                case R.id.default2:
                    select.set(R.id.default2);
                    select.show(fm, "select_image");
                    break;
                case R.id.default3:
                    select.set(R.id.default3);
                    select.show(fm, "select_image");
                    break;
                case R.id.default4:
                    select.set(R.id.default4);
                    select.show(fm, "select_image");
                    break;
                case R.id.default5:
                    select.set(R.id.default5);
                    select.show(fm, "select_image");
                    break;
                case R.id.select_image:

                    PhotoPickerIntent = new Intent(Intent.ACTION_PICK);
                    PhotoPickerIntent.setType("image/*");
                    startActivityForResult(PhotoPickerIntent, select_photo);

                    break;

            }


        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("으아아아", "불림");

        switch(requestCode) {
            case select_photo:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filename = "imagepath.txt";
                    String filepath = getRealImagePath(selectedImage);
                    File k = new File(filepath);
                    FileOutputStream output;
                    Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();


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

                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    if (yourSelectedImage == null)
                        Toast.makeText(this, "null image", Toast.LENGTH_SHORT).show();


                    ImageView x = (ImageView) findViewById(R.id.select_image);
                    x.setImageBitmap(yourSelectedImage);
                    select select = new select();
                    select.set(yourSelectedImage);
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    select.show(fm, "select_image");
                }
        }

                /*
        switch(requestCode) {
            case 120:
                ImageView select_image = (ImageView) findViewById(R.id.select_image);
                BitmapFactory.decodeResource(getResources(), R.id.select_image);
                select select = new select();
                select.set(R.id.select_image);
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                select.show(fm, "select_image");
        }
        */
    }

    public void onTestDialogClick(DialogFragment dialog, Bitmap b) {
        Intent intent = new Intent(this, ConfigActivity.class);

        ImageView default1 = (ImageView) findViewById(R.id.default1);
        intent.setAction("set_background");
        intent.putExtra("img", b);
        intent.putExtra("str", String.valueOf(default1.getSolidColor()));

        startActivity(intent);

    }

    @Override
    public void pickbackground(DialogFragment dialog, int id) {
        Intent intent = new Intent(this, ConfigActivity.class);

        ImageView default1 = (ImageView) findViewById(R.id.default1);
        intent.setAction("set_background");
        intent.putExtra("id", id);

        startActivity(intent);

    }


}
