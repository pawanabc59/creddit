package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModal;
import com.example.creddit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recycler_home_posts;
    List<CardModal> home_posts;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef,mRef_user, mRef_post;
    FirebaseUser user;
    String userId;
    String cardProfileImage="",cardPostUserId, cardImage, cardTitle, postedBy, cardDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycler_home_posts = view.findViewById(R.id.recycler_home_posts);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        home_posts = new ArrayList<>();

        final CardAdapter cardAdapter = new CardAdapter(getContext(), home_posts);
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext());
//        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));

        if (user == null){

        }
        else {
            userId = user.getUid();
            mRef_user = mRef.child("users");
            mRef_post = mRef.child("posts");

            mRef_post.orderByChild("postNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        cardImage = dataSnapshot1.child("imagePath").getValue().toString();
                        cardTitle = dataSnapshot1.child("uploadedBy").getValue().toString();
                        postedBy = dataSnapshot1.child("uploadedBy").getValue().toString();
                        cardDescription = dataSnapshot1.child("cardTitle").getValue().toString();
                        cardPostUserId = dataSnapshot1.child("userId").getValue().toString();
                        cardProfileImage = dataSnapshot1.child("cardPostProfileImage").getValue().toString();

//                        mRef_user.child(cardPostUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshotUser) {
//                                cardProfileImage = dataSnapshotUser.child("profileImage").getValue().toString();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
                            home_posts.add(new CardModal(cardProfileImage, cardImage, cardTitle, postedBy, cardDescription));
                            cardAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            recycler_home_posts.setLayoutManager(cardManager);
            recycler_home_posts.setAdapter(cardAdapter);
        }

        return view;
    }

}
