package com.example.depiction.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.depiction.Ai.Ai_fragment;
import com.example.depiction.Fragments.Channel;
import com.example.depiction.Fragments.HomeFragment;
import com.example.depiction.R;
import com.example.depiction.Fragments.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnView=findViewById(R.id.bnView);


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


        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id =item.getItemId();

                switch (id){
                    case R.id.nav_home:
                        loadFragment(new HomeFragment(),false);
                        break;
                    case R.id.nav_channel:
                        loadFragment(new Channel(),false);
                        break;
                    case R.id.nav_AI:
                            loadFragment(new Ai_fragment(),false);
                            break;
                    case R.id.nav_profile:
                        loadFragment(new profile(),false);
                        break;
                    default:
                        loadFragment(new HomeFragment(),true);
                        break;
                }

                return true;
            }
        });


        bnView.setSelectedItemId(R.id.nav_home);



    }


    public void loadFragment(Fragment fragment,boolean flag){
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        if(flag){ft.add(R.id.container,fragment);}
        else{ft.replace(R.id.container,fragment);}
        ft.commit();

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