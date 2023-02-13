package com.example.depiction;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DeleteImage extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<String> profilelist;
    private ArrayList<String> imageName;
    private deleteAdapter adapter;
    public StorageReference storageReference;

    ProgressBar progressBar;



    TextView usernamelabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_image);


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

        recyclerView=findViewById(R.id.deleteRecycleView);
        usernamelabel=findViewById(R.id.usernamelabel);
        progressBar=findViewById(R.id.progressBarDelete);

        String username=getIntent().getStringExtra("username");
        if(username.equals("Noname")){usernamelabel.setText("Username : Please login with your google account to delete your uploaded photos.");}
        else{usernamelabel.setText("Username: "+username);}

        String newusername=username.replace('.','_');



        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("Users").child(newusername).child("imageslinks");

        myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               progressBar.setVisibility(View.GONE);
               profilelist =new ArrayList<>();
               imageName = new ArrayList<>();
               for(DataSnapshot shot: snapshot.getChildren()){
                       String profilephoto=shot.getValue().toString();
                       String keyname=shot.getKey();
                       profilelist.add(profilephoto);
                   imageName.add(keyname);
               }
               if(profilelist.size()!=0) {
                   recyclerView.setLayoutManager(new GridLayoutManager(DeleteImage.this, 3));
                   adapter = new deleteAdapter(profilelist,newusername,imageName,DeleteImage.this);
                   recyclerView.setAdapter(adapter);
                   progressBar.setVisibility(View.GONE);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               progressBar.setVisibility(View.GONE);

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