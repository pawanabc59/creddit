package com.example.creddit.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creddit.Adapter.FollowingListAdapter;
import com.example.creddit.Adapter.UsersAdapter;
import com.example.creddit.Model.FollowingListModel;
import com.example.creddit.Model.UsersModel;
import com.example.creddit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DirectsFragment extends Fragment {

    RecyclerView ShowUsersRecyclerView;
    List<UsersModel> usersModels;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userId;
    ValueEventListener UsersListValueEventListener;
    UsersAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directs, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        ShowUsersRecyclerView = view.findViewById(R.id.ShowUsersRecyclerView);

        usersModels = new ArrayList<>();

        usersAdapter = new UsersAdapter(getContext(), usersModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        UsersListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersModels.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        if (!userId.equals(dataSnapshot1.getKey())) {
                            usersModels.add(new UsersModel(dataSnapshot1.child("optionalName").getValue(String.class), dataSnapshot1.child("profileImage").getValue(String.class), dataSnapshot1.getKey().toString()));
                        }
                        usersAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("users").addValueEventListener(UsersListValueEventListener);

        ShowUsersRecyclerView.setLayoutManager(linearLayoutManager);
        ShowUsersRecyclerView.setAdapter(usersAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRef.child("users").removeEventListener(UsersListValueEventListener);
    }

}
