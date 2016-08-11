package kr.co.mujogun.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        ImageView default6 = (ImageView) findViewById(R.id.default6);
        ImageView default7 = (ImageView) findViewById(R.id.default7);
        ImageView default8 = (ImageView) findViewById(R.id.default8);
        ImageView default9 = (ImageView) findViewById(R.id.default9);
        Button select_image = (Button) findViewById(R.id.select_button);

        default1.setOnClickListener(new setBackgroundlistener());
        default2.setOnClickListener(new setBackgroundlistener());
        default3.setOnClickListener(new setBackgroundlistener());
        default4.setOnClickListener(new setBackgroundlistener());
        default5.setOnClickListener(new setBackgroundlistener());
        default6.setOnClickListener(new setBackgroundlistener());
        default7.setOnClickListener(new setBackgroundlistener());
        default8.setOnClickListener(new setBackgroundlistener());
        default9.setOnClickListener(new setBackgroundlistener());

        select_image.setOnClickListener(new setBackgroundlistener());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        */
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
                case R.id.default6:
                    select.set(R.id.default6);
                    select.show(fm, "select_image");
                    break;
                case R.id.default7:
                    select.set(R.id.default7);
                    select.show(fm, "select_image");
                    break;
                case R.id.default8:
                    select.set(R.id.default8);
                    select.show(fm, "select_image");
                    break;
                case R.id.default9:
                    select.set(R.id.default9);
                    select.show(fm, "select_image");
                    break;
                case R.id.select_button:

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


                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

                    ExifInterface exif = null;
                    try {
                        File pictureFile = new File(picturePath);
                        exif = new ExifInterface(pictureFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int orientation = ExifInterface.ORIENTATION_NORMAL;
                    if (exif != null)
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            loadedBitmap = rotateBitmap(loadedBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            loadedBitmap = rotateBitmap(loadedBitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            loadedBitmap = rotateBitmap(loadedBitmap, 270);
                            break;
                        case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                            loadedBitmap = flip(loadedBitmap, true, false);
                            break;
                        case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                            loadedBitmap = flip(loadedBitmap, false, true);
                            break;
                    }
                    Bitmap bMapScaled = Bitmap.createScaledBitmap(loadedBitmap, 480, 800, true);


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

                    /*
                    ImageView x = (ImageView) findViewById(R.id.select_image);
                    x.setImageBitmap(yourSelectedImage);
                    */
                    select select = new select();
                    select.set(bMapScaled);
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

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
