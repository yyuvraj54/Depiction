package com.example.depiction.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.depiction.R;

import java.io.IOException;

public class FullImageActivity extends AppCompatActivity {
    private ImageView fullImage;
    private Button apply , lock ,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullImage=findViewById(R.id.fullImage);
        apply=findViewById(R.id.apply);
        lock=findViewById(R.id.lockscreen);
        home=findViewById(R.id.homescreen);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        Glide.with(this).load(getIntent().getStringExtra("image")).into(fullImage);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundforboth();

            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundforlock();
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundforhome();
            }
        });
    }

    private void setBackgroundforboth() {

        Bitmap bitmap=((BitmapDrawable)fullImage.getDrawable()).getBitmap() ;
        WallpaperManager manager=WallpaperManager.getInstance(getApplicationContext());
        try {

            manager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error DisplayImage", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    private void setBackgroundforlock(){
        Bitmap bitmap=((BitmapDrawable)fullImage.getDrawable()).getBitmap() ;
        WallpaperManager manager=WallpaperManager.getInstance(getApplicationContext());
        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error DisplayImage", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }

    }

    private void setBackgroundforhome(){
        Bitmap bitmap=((BitmapDrawable)fullImage.getDrawable()).getBitmap() ;
        WallpaperManager manager=WallpaperManager.getInstance(getApplicationContext());
        try {

            manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            Toast.makeText(this, "Wallpaper Applied", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error DisplayImage", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}