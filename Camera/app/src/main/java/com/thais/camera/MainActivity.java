package com.thais.camera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static {
        System.loadLibrary("NativeImageProcessor");
    }


    ImageView mainimg, filter1, filter2, filter3, filter4;
    FloatingActionButton fab;
    Bitmap originalbitmap;
    Button savebtn, refreshbtn;
    OutputStream mOutputStream;
    Bitmap resetbitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData(); // pegando o dado da foto
        mainimg.setImageURI(uri);
        filter1.setImageURI(uri);
        filter2.setImageURI(uri);
        filter3.setImageURI(uri);
        filter4.setImageURI(uri);
        try {
            resetbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri); //um reset geral
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainimg = findViewById(R.id.mainimg);
        fab = findViewById(R.id.floatingbtn);
        filter1 = findViewById(R.id.f1);
        filter2 = findViewById(R.id.f2);
        filter3 = findViewById(R.id.f3);
        filter4 = findViewById(R.id.f4);
        savebtn = findViewById(R.id.saveimgbtn);
        refreshbtn = findViewById(R.id.refreshimgbtn);

        filter1.setOnClickListener(this); //referenciando a main, o view já esta la
        filter2.setOnClickListener(this);
        filter3.setOnClickListener(this);
        filter4.setOnClickListener(this);


        BitmapDrawable drawable = (BitmapDrawable) mainimg.getDrawable();
        originalbitmap = drawable.getBitmap();
        resetbitmap = originalbitmap;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MainActivity.this)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable1 = (BitmapDrawable) mainimg.getDrawable();
                Bitmap bitmap = drawable1.getBitmap();

                originalbitmap = bitmap;
                File filepath = Environment.getExternalStorageDirectory();
                File dir = new File(filepath.getAbsolutePath() + "/Pictures/");

                File file = new File(dir, System.currentTimeMillis() + ".jpg");
                try {
                    mOutputStream = new FileOutputStream(file);
                    originalbitmap.compress(Bitmap.CompressFormat.JPEG, 100, mOutputStream);
                    mOutputStream.close();
                    mOutputStream.flush();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Image Saved To Gallery", Toast.LENGTH_SHORT).show();

            }
        });

        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainimg.setImageBitmap(resetbitmap);
            }
        });
    }

    @Override
    public void onClick(View view) {

        savebtn.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.f1:
                com.zomato.photofilters.imageprocessors.Filter myFilter = new com.zomato.photofilters.imageprocessors.Filter();
                myFilter.addSubFilter(new BrightnessSubFilter(30));
                myFilter.addSubFilter(new ContrastSubFilter(1.2f));


                BitmapDrawable bitdraw1 = (BitmapDrawable) mainimg.getDrawable();
                Bitmap mbitmap = bitdraw1.getBitmap();
                Bitmap inputImage = mbitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap outputImage = myFilter.processFilter(inputImage);

                mainimg.setImageBitmap(outputImage);
                break;

            case R.id.f2:
                com.zomato.photofilters.imageprocessors.Filter fooFilter = SampleFilters.getBlueMessFilter();

                BitmapDrawable bitdraw2 = (BitmapDrawable) mainimg.getDrawable();
                Bitmap mbitmap2 = bitdraw2.getBitmap();
                Bitmap inputImage2 = mbitmap2.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap outputImage2 = fooFilter.processFilter(inputImage2);
                mainimg.setImageBitmap(outputImage2);
                break;

            case R.id.f3:
                com.zomato.photofilters.imageprocessors.Filter fooFilter3 = SampleFilters.getLimeStutterFilter();
                BitmapDrawable bitdraw3 = (BitmapDrawable) mainimg.getDrawable();
                Bitmap mbitmap3 = bitdraw3.getBitmap();
                Bitmap inputImage3 = mbitmap3.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap outputImage3 = fooFilter3.processFilter(inputImage3);
                mainimg.setImageBitmap(outputImage3);
                break;

            case R.id.f4:
                Filter fooFilter4 = SampleFilters.getNightWhisperFilter();
                Bitmap inputImage4 = resetbitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap outputImage4 = fooFilter4.processFilter(inputImage4);
                mainimg.setImageBitmap(outputImage4);
                break;
                
        }

    }
}