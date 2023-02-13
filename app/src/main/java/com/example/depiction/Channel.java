package com.example.depiction;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Channel extends Fragment {

    private ArrayList<String> namelist;
    private ArrayList<String> profilelist;

    private ArrayList<String> emaillist ,restrictuser;
    private channelAdapter adapter;
    RecyclerView channelRecycleview;

    private ArrayList<String> animedata;

    CardView animewall ,ambientwall,Defaultcardimage ,cartoonwall , gradient;

    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_channel, container, false);

        channelRecycleview=view.findViewById(R.id.channelRV);
        animewall=view.findViewById(R.id.Animewall);
        ambientwall=view.findViewById(R.id.Ambientwall);
        Defaultcardimage=view.findViewById(R.id.coolwall);
        progressBar=view.findViewById(R.id.progressBarChannel);
        cartoonwall=view.findViewById(R.id.CartoonWall);
        gradient=view.findViewById(R.id.Gradient);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
        animewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inbuildpage=new Intent(getContext(),inbuildChannel.class);
                inbuildpage.putExtra("pageHeading","Anime");
                DatabaseReference animeWall=database.getReference().child("Anime");
                animeWall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animedata =new ArrayList<>();
                        for(DataSnapshot shot: snapshot.getChildren()){
                            String data =shot.getValue().toString();
                            animedata.add(data);
                        }
                        inbuildpage.putExtra("imagesList" , (ArrayList<String>)animedata);
                        startActivity(inbuildpage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        ambientwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inbuildpage=new Intent(getContext(),inbuildChannel.class);
                inbuildpage.putExtra("pageHeading","Amoled Wallpapers");
                DatabaseReference animeWall=database.getReference().child("Amoled");
                animeWall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animedata =new ArrayList<>();
                        for(DataSnapshot shot: snapshot.getChildren()){
                            String data =shot.getValue().toString();
                            animedata.add(data);
                        }
                        inbuildpage.putExtra("imagesList" , (ArrayList<String>)animedata);
                        startActivity(inbuildpage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        Defaultcardimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inbuildpage=new Intent(getContext(),inbuildChannel.class);
                inbuildpage.putExtra("pageHeading","Depiction Special");
                DatabaseReference animeWall=database.getReference().child("DepictionSpecial");
                animeWall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animedata =new ArrayList<>();
                        for(DataSnapshot shot: snapshot.getChildren()){
                            String data =shot.getValue().toString();
                            animedata.add(data);
                        }
                        inbuildpage.putExtra("imagesList" , (ArrayList<String>)animedata);
                        startActivity(inbuildpage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        cartoonwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inbuildpage=new Intent(getContext(),inbuildChannel.class);
                inbuildpage.putExtra("pageHeading","Cartoons");
                DatabaseReference animeWall=database.getReference().child("Cartoons");
                animeWall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animedata =new ArrayList<>();
                        for(DataSnapshot shot: snapshot.getChildren()){
                            String data =shot.getValue().toString();
                            animedata.add(data);
                        }
                        inbuildpage.putExtra("imagesList" , (ArrayList<String>)animedata);
                        startActivity(inbuildpage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        gradient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inbuildpage=new Intent(getContext(),inbuildChannel.class);
                inbuildpage.putExtra("pageHeading","Gradient/Solid Colours");
                DatabaseReference animeWall=database.getReference().child("Images");
                animeWall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        animedata =new ArrayList<>();
                        for(DataSnapshot shot: snapshot.getChildren()){
                            String data =shot.getValue().toString();
                            animedata.add(data);
                        }
                        inbuildpage.putExtra("imagesList" , (ArrayList<String>)animedata);
                        startActivity(inbuildpage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        DatabaseReference myRef = database.getReference().child("Users");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                namelist =new ArrayList<>();
                emaillist =new ArrayList<>();
                profilelist =new ArrayList<>();

                restrictuser= new ArrayList<>();
                for(DataSnapshot shot: snapshot.getChildren()){

//                    Toast.makeText(getContext(), shot.child("RestrictUser").getValue().toString(), Toast.LENGTH_SHORT).show();
//                    if(shot.getValue().toString()=="RestrictUser"){
//                        for(DataSnapshot bannedUser: shot.getChildren()){
//                            restrictuser.add(bannedUser.toString());
//                            Toast.makeText(getContext(), bannedUser.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//


                    if(shot.child("imageslinks").getValue()!=null) {
                        String name = shot.child("name").getValue().toString();
                        String email = shot.child("email").getValue().toString();



                        String profilephoto=shot.child("profileUrl").getValue().toString();

                        namelist.add(name);
                        emaillist.add(email);
                        profilelist.add(profilephoto);
                    }
                }


//                channelRecycleview.setLayoutManager(new GridLayoutManager(getContext(),1));
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL ,false);
                adapter=new channelAdapter(profilelist,namelist,emaillist,getContext());
                channelRecycleview.setLayoutManager(linearLayoutManager);
                channelRecycleview.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
//



//        channelProfileIntent.putExtra("profiledata",username);




        return view;
    }
}