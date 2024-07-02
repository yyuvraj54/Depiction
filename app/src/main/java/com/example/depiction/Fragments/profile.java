package com.example.depiction.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.depiction.Activities.AboutApp;
import com.example.depiction.Adapters.WallpaperAdapter;
import com.example.depiction.Activities.DeleteImage;
import com.example.depiction.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class profile extends Fragment {


    GoogleSignInOptions gso;
    String username;
    GoogleSignInClient gsc;
    SignInButton googleBtn ;
    Button signOut;
    public String proiflephoto;
    TextView nametext;
    TextView emailtext , recycleEmptytext;

    ImageView proimage;
    ScrollView scrollFrame;
    LinearLayout logFrame;
    Button Addimages;

    RecyclerView recyclerView;
    private ArrayList<String> list ,UploadRestrictction;
    StorageReference storageReference;
    private WallpaperAdapter adapter;

    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        recycleEmptytext=view.findViewById(R.id.Emptytext);
        googleBtn=view.findViewById(R.id.google_btn);
        proimage=view.findViewById(R.id.profileimage);
        nametext=view.findViewById(R.id.userName);
        emailtext=view.findViewById(R.id.userEmail);
        signOut=view.findViewById(R.id.signout);
        scrollFrame=view.findViewById(R.id.scrollframe);
        logFrame=view.findViewById(R.id.logframe);
        Addimages=view.findViewById(R.id.addImages);
        recyclerView=view.findViewById(R.id.profileuploadedpic);
        progressBar=view.findViewById(R.id.progressBarProfile);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account !=null) {

            uiElementUpdate(true);
            UIupdate();
        }
        else{
            uiElementUpdate(false);
        }


        try {
            username = account.getEmail();
            username = username.replace('.', '_');
        }
        catch(Exception e){
            username="";
            e.printStackTrace();
        }
        //Downloading data from firebase to profile recycleView
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference().child("Users/"+username+'/'+"imageslinks");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                list =new ArrayList<>();
                for(DataSnapshot shot: snapshot.getChildren()){
                    String data =shot.getValue().toString();
                    list.add(data);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                adapter=new WallpaperAdapter(list,getContext());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

                if(list.isEmpty()){
                    recycleEmptytext.setVisibility(View.VISIBLE);
                }
                else{
                    recycleEmptytext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIN();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uiElementUpdate(false);
                        deleteAllItem();
                        Toast.makeText(getContext(), "Sign Out Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        FloatingActionButton fab2 = view.findViewById(R.id.deletefab);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), DeleteImage.class);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
                try{
                    String usname=account.getEmail();
                    intent.putExtra("username",usname);
                    startActivity(intent);}
                catch (Exception e){
                    String usname="Noname";
                    intent.putExtra("username",usname);
                    startActivity(intent);
                    e.printStackTrace();
                }

            }
        });


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent=new Intent(getContext(), AboutApp.class);
                        startActivity(intent);
            }
        });

        Addimages.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


        DatabaseReference myRef = database.getReference().child("UploadRestriction");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UploadRestrictction =new ArrayList<>();
                for(DataSnapshot shot: snapshot.getChildren()){
                    UploadRestrictction.add(shot.getValue().toString());
                }

                username = username.replace('_', '.');
                if( UploadRestrictction.contains("ALLUSERS")){
                    Toast.makeText(getContext(), "Service is paused ,Cloud storage is under maintenance!", Toast.LENGTH_LONG).show();
                }
                else if(UploadRestrictction.contains(username)){
                    Toast.makeText(getContext(), "Your Account is blocked by depiction!", Toast.LENGTH_LONG).show();
                }
                else{
                    dataAddingStart();
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Someting went wrong ,try again!", Toast.LENGTH_SHORT).show();

            }
        });


    }
});

        return view;
    }




















    public void dataAddingStart(){
        Intent  iGallery=new Intent(Intent.ACTION_PICK);
        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallery, 110);
    }
    //TO upload the image in Runtime Database
    public void uploadDataToFirebaseRealTimeStorage(String username ,Uri imageuri,String filename) {
        //xyz@gmail.com -> xyz@gmail_com
        username=username.replace('.','_');
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        //Log.d(TAG,  "Users/"+username+"/imagesLinks");
        DatabaseReference myRef = database.getReference().child("Users/"+username+'/'+"imageslinks").child(filename);
        myRef.setValue(imageuri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "File Uploaded Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //TO upload the image in Storage Database
    public void  uploadDataToFirebaseStorage(String username , Uri imagefile){

        String filename= new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
        storageReference= FirebaseStorage.getInstance().getReference().child(username).child(filename);
        storageReference.putFile(imagefile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                //Downloading Image url(token) to Runtime DataBase
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uploading url Data to Runtime DataBase
                        uploadDataToFirebaseRealTimeStorage(username,uri,filename);

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error Uploading Image Please,Try again", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else if (requestCode==110) {
            try {
                Uri uridata =data.getData();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                String personEmail = acct.getEmail();
                uploadDataToFirebaseStorage(personEmail,uridata);
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public void signIN(){
        Intent signIntent =gsc.getSignInIntent();
        startActivityForResult(signIntent, 100);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            uiElementUpdate(true);
            UIupdate();
            username=account.getEmail();
            username=username.replace('.','_');

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference().child("Users/"+username).child("name");
            myRef.setValue(account.getDisplayName());

            database.getReference().child("Users/"+username).child("email").setValue(account.getEmail());

            try{String proiflephoto=account.getPhotoUrl().toString();
                database.getReference().child("Users/"+username).child("profileUrl").setValue(proiflephoto);
            }catch (Exception e){proiflephoto="nullImage";e.printStackTrace();
                database.getReference().child("Users/"+username).child("profileUrl").setValue(proiflephoto);}




            DatabaseReference myRefnew = database.getReference().child("Users/"+username+'/'+"imageslinks");
            myRefnew.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list =new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    for(DataSnapshot shot: snapshot.getChildren()){
                        String data =shot.getValue().toString();
                        list.add(data);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    adapter=new WallpaperAdapter(list,getContext());
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                    if(list.isEmpty()){
                        recycleEmptytext.setVisibility(View.VISIBLE);
                    }
                    else{
                        recycleEmptytext.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);

                }
            });

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getContext(), "Something wrong ,Try Again", Toast.LENGTH_LONG).show();
        }
    }

    void UIupdate(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        String personname = acct.getDisplayName();
        String personEmail = acct.getEmail();
        String personPhoto = String.valueOf(acct.getPhotoUrl());
        if(personPhoto =="nullImage"){proimage.setImageResource(R.drawable.depiction);}
        else{
        try {
            Glide.with(getContext()).load(personPhoto).placeholder(R.drawable.depiction).error(R.drawable.depiction).into(proimage);
        }catch (Exception e){
            proimage.setImageResource(R.drawable.depiction);
            e.printStackTrace();
            Log.d(TAG, "UIupdate: error from here");

        }
        }



        nametext.setText(personname);
        emailtext.setText(personEmail);
    }
    void uiElementUpdate(boolean flag){

        if(flag){
            proimage.getLayoutParams().height = 120;
            proimage.getLayoutParams().width = 120;
            proimage.requestLayout();
            logFrame.setVisibility(View.GONE);
            scrollFrame.setVisibility(View.VISIBLE);
        }
        else{

            proimage.getLayoutParams().height = 450;
            proimage.getLayoutParams().width = 450;
            proimage.requestLayout();


            nametext.setText("");
            emailtext.setText("");
            proimage.setImageResource(R.drawable.depiction);


            scrollFrame.setVisibility(View.INVISIBLE);
            logFrame.setVisibility(View.VISIBLE);
        }


    }


    private void deleteAllItem() {
        list =new ArrayList<>();
        list.clear();
        adapter=new WallpaperAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
    }



}