package com.example.depiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileChannel extends AppCompatActivity {

    TextView usernameText , EmailText , noofPost;
    RecyclerView profileRecycleview;
    private ArrayList<String> list;
    private WallpaperAdapter adapter;

    ProgressBar progressBar;
    ImageView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_channel);

        usernameText=findViewById(R.id.profileusername);
        EmailText=findViewById(R.id.profileemail);
        noofPost=findViewById(R.id.noOfPost);
        profileRecycleview=findViewById(R.id.profilephotosRecycleView);
        profile=findViewById(R.id.profilepicture);
        progressBar=findViewById(R.id.progressBarUserPro);




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


        String username=getIntent().getStringExtra("username");
        username=username.replace('.','_');

        try {ImageDataFetch(username);}
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ProfileChannel.this, "Something went wrong to load Images, try again", Toast.LENGTH_LONG).show();
        }
        try{profileDataFetch(username);}
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ProfileChannel.this, "Something went wrong to load userInfo, try again", Toast.LENGTH_LONG).show();
        }





    }

    public void ImageDataFetch(String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("Users/"+username+'/'+"imageslinks");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list =new ArrayList<>();
                progressBar.setVisibility(View.GONE);

                for(DataSnapshot shot: snapshot.getChildren()){
                    String data =shot.getValue().toString();
                    list.add(data);
                }
                String counter= String.valueOf(list.size());
                noofPost.setText(counter);
                profileRecycleview.setLayoutManager(new GridLayoutManager(ProfileChannel.this, 3));
                adapter=new WallpaperAdapter(list,ProfileChannel.this);
                profileRecycleview.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }
    public void profileDataFetch(String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("Users/"+username);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String icon = snapshot.child("profileUrl").getValue().toString();

                usernameText.setText(name);
                EmailText.setText(email);
                try{Glide.with(ProfileChannel.this).load(icon).placeholder(R.drawable.depiction).error(R.drawable.depiction).into(profile);}
                catch (Exception e){
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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