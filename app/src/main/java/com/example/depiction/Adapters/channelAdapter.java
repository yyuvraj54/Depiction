package com.example.depiction.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.depiction.Fragments.ProfileChannel;
import com.example.depiction.R;

import java.util.ArrayList;
public class channelAdapter extends RecyclerView.Adapter<channelAdapter.channelViewHolder>{

    public ArrayList<String> namelist;
    public ArrayList<String> profilephotolist;
    public ArrayList<String> emaillist;
    private Context context;

    public channelAdapter(ArrayList<String> profilephotolist,ArrayList<String> namelist, ArrayList<String> emaillist, Context context) {
        this.namelist = namelist;
        this.emaillist = emaillist;
        this.context = context;
        this.profilephotolist=profilephotolist;
    }

    @NonNull
    @Override
    public channelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.channelcard,parent,false);
        return new channelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull channelViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nameview.setText(namelist.get(position));
        holder.emailview.setText(emaillist.get(position));




        try{Glide.with(context).load(profilephotolist.get(position)).placeholder(R.drawable.depiction).error(R.drawable.depiction).into(holder.photoarea);}catch (Exception e){e.printStackTrace();}




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent channelProfileIntent =new Intent(context, ProfileChannel.class);
                channelProfileIntent.putExtra("username",emaillist.get(position));
                context.startActivity(channelProfileIntent);
            }
        });

    }

    @Override
    public int getItemCount() { return  namelist.size();}

    public class channelViewHolder extends RecyclerView.ViewHolder {

        TextView nameview,emailview;
        ImageView photoarea;


        public channelViewHolder(@NonNull View itemView) {
            super(itemView);


            nameview=itemView.findViewById(R.id.AuthroName);
            emailview=itemView.findViewById(R.id.AuthorEmail);
            photoarea=itemView.findViewById(R.id.Channelprofile);


        }

    }





}
