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
import com.google.firebase.database.Query;
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
    DatabaseReference mRef,mRef_user, mRef_post,mRef2;
    FirebaseUser user;
    String userId;
    String cardProfileImage="",cardPostUserId, cardImage, cardTitle, postedBy, cardDescription, cardPostTime;

    String currentDate, postTime;
    SimpleDateFormat sdf;
    Date todayDate, postedDate;
    ValueEventListener postValueEventListener;

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

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        home_posts = new ArrayList<>();

        final CardAdapter cardAdapter = new CardAdapter(getContext(), home_posts, getActivity());
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        home_posts.add(new CardModal(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));

//        if (user == null){
//
//        }
//        else {
//            userId = user.getUid();
            mRef_user = mRef.child("users");
            mRef_post = mRef.child("posts").child("imagePosts");

//            Query query = FirebaseDatabase.getInstance().getReference().child("creddit").child("posts").orderByChild("postNumber");

            postValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    home_posts.clear();
                    if (dataSnapshot.exists()) {
                        try {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        cardImage = dataSnapshot1.child("imagePath").getValue().toString();
//                        cardTitle = dataSnapshot1.child("uploadedBy").getValue().toString();
//                        postedBy = dataSnapshot1.child("uploadedBy").getValue().toString();
//                        cardDescription = dataSnapshot1.child("cardTitle").getValue().toString();
//                        cardPostUserId = dataSnapshot1.child("userId").getValue().toString();
//                        cardProfileImage = dataSnapshot1.child("cardPostProfileImage").getValue().toString();
                                String postKey = dataSnapshot1.getKey();
                                postTime = dataSnapshot1.child("postTime").getValue(String.class);

                                todayDate = sdf.parse(currentDate);
                                postedDate = sdf.parse(postTime);

                                long diff = todayDate.getTime() - postedDate.getTime();
                                long seconds = diff / 1000;
                                long minutes = seconds / 60;
                                long hours = minutes / 60;
                                int days = (int) (hours / 24);

                                if ((days / 365) > 0) {
                                    int year = days / 365;
                                    int leftMonths = days % 365;
                                    int month = leftMonths / 30;
                                    int leftDays = month % 30;
                                    cardPostTime = (year + "y " + month + "m ago");
                                } else if ((days / 30) > 0) {
                                    int month = days / 30;
                                    int leftDays = days % 30;
                                    cardPostTime = (month + "m ago");
                                } else if ((hours / 24) > 0) {
                                    cardPostTime = (days + "d ago");
                                } else if ((minutes / 60) > 0) {
                                    cardPostTime = (hours + "h ago");
                                } else {
                                    cardPostTime = (minutes + "min ago");
                                }

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
                                home_posts.add(new CardModal(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime));
                                cardAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef_post.orderByChild("postNumber").addValueEventListener(postValueEventListener);

            recycler_home_posts.setLayoutManager(cardManager);
            recycler_home_posts.setAdapter(cardAdapter);
//        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRef_post.removeEventListener(postValueEventListener);
    }
}
