package com.example.depiction;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference reference ,childreference;
    private ArrayList<String> list ,linklist;
    private WallpaperAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        reference= FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("ViewPort");


        recyclerView=view.findViewById(R.id.recycleerview);
        progressBar=view.findViewById(R.id.progressBar);

        getData();

        return view;
    }

    private void getData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list =new ArrayList<>();
                for(DataSnapshot shot: snapshot.getChildren()){
                    String page=shot.getValue().toString();
                    String substring="@gmail_com";
//                    Toast.makeText(getContext(), page, Toast.LENGTH_SHORT).show();
                  childreference= FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child(page);
//                    childreference= FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users/"+page+'/'+"imageslinks");

                    childreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            linklist =new ArrayList<>();
                            progressBar.setVisibility(View.GONE);
                            for(DataSnapshot shot:snapshot.getChildren()){
                                String link=shot.getValue().toString();
                                linklist.add(link);
                            }

                            list.addAll(linklist);
                            Collections.shuffle(list);
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                Log.d(TAG, "onDataChange: "+list);

                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                adapter=new WallpaperAdapter(list,getContext());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error while Fetching...", Toast.LENGTH_LONG).show();
            }
        });

    }
}