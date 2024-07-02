package com.example.depiction.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.depiction.Adapters.WallpaperAdapter;
import com.example.depiction.R;

import java.util.ArrayList;

public class inbuildChannel extends AppCompatActivity {

    RecyclerView recyclerView;
    WallpaperAdapter adapter;
    TextView Heading;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbuild_channel);


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


        recyclerView=findViewById(R.id.inbuildvhannelRecycleView);
        Heading=findViewById(R.id.pageheading);
        progressBar=findViewById(R.id.progressBarinbuilt);

        Intent intent = getIntent();
        String heading=intent.getStringExtra("pageHeading");
        ArrayList<String> imagelist=intent.getStringArrayListExtra("imagesList");

        Heading.setText(heading);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter=new WallpaperAdapter(imagelist,this);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);



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