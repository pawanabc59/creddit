package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModel;
import com.example.creddit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PopularFragment extends Fragment {

    RecyclerView recycler_popular_posts;
    List<CardModel> popular_posts;
    SimpleDateFormat sdf;
    String currentDate, postTime, cardPostTime, userId;
    Date todayDate, postedDate;
    CardAdapter cardAdapter;
    ArrayList<String> followingList;

    ValueEventListener postValueEventListener, followingListValueEventListener, nsfwValueEventListener;

    DatabaseReference mRef, postRef;
    FirebaseUser user;
    int showNSFWvalue=0, blurNSFWvalue=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        recycler_popular_posts = view.findViewById(R.id.recycler_popular_posts);

        popular_posts = new ArrayList<>();
        followingList = new ArrayList<String>();

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        postRef = FirebaseDatabase.getInstance().getReference("creddit").child("posts").child("imagePosts");

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();

            followingListValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            followingList.add(dataSnapshot1.child("key").getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).child("followingList").addListenerForSingleValueEvent(followingListValueEventListener);

            nsfwValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        showNSFWvalue = dataSnapshot.child("showNSFW").getValue(Integer.class);
                        blurNSFWvalue = dataSnapshot.child("blurNSFW").getValue(Integer.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).addValueEventListener(nsfwValueEventListener);
        }

        cardAdapter = new CardAdapter(getContext(), popular_posts, getActivity(), "popularFragment");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        postValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popular_posts.clear();
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

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

//                            you have to change here from taking userid to subreddit id .
                            if (!followingList.contains(dataSnapshot1.child("userId").getValue().toString())) {
                                if (showNSFWvalue == 0 && dataSnapshot1.child("NSFW").getValue(Integer.class) == 0) {
                                    popular_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("uploadedBy").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class), dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class)));
                                }
                                else {
                                    popular_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("uploadedBy").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class), dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class)));
                                }
                                cardAdapter.notifyDataSetChanged();
                                followingList.add(dataSnapshot1.child("userId").getValue().toString());
                            }
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

        postRef.orderByChild("postNumber").addValueEventListener(postValueEventListener);

        recycler_popular_posts.setLayoutManager(linearLayoutManager);
        recycler_popular_posts.setAdapter(cardAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postRef.removeEventListener(postValueEventListener);
    }
}
