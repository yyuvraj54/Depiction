package com.example.depiction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Channel extends Fragment {

    TextView authorname , authoremail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_channel, container, false);
//
//    authorname=view.findViewById(R.id.AuthroName);
//    authoremail=view.findViewById(R.id.AuthorEmail);
//
//







        return view;
    }
}