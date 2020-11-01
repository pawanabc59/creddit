package com.example.creddit.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SavedPostsFragment extends Fragment {

    RecyclerView savedPostsRecycler;
    List<CardModel> saved_posts;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef, mRef_user, mRef_post, mRef2;
    FirebaseUser user;
    String userId;
    String cardPostTime;

    String currentDate, postTime;
    SimpleDateFormat sdf;
    CardAdapter cardAdapter;
    Date todayDate, postedDate;
    ArrayList<String> hiddenList = new ArrayList<>();
    ValueEventListener postValueEventListener, savedPostValueEventListener, nsfwValueEventListener;
    int showNSFWvalue = 0, blurNSFWvalue = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_posts, container, false);

        savedPostsRecycler = view.findViewById(R.id.savedPostsRecycler);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        saved_posts = new ArrayList<>();

        getHiddenPostsLists();

        cardAdapter = new CardAdapter(getContext(), saved_posts, getActivity(), "savedPostsFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRef_user = mRef.child("users").child(userId).child("savedImages");
        mRef_post = mRef.child("posts").child("imagePosts");

        nsfwValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showNSFWvalue = dataSnapshot.child("showNSFW").getValue(Integer.class);
                    blurNSFWvalue = dataSnapshot.child("blurNSFW").getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).addValueEventListener(nsfwValueEventListener);

//        postValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                saved_posts.clear();
//                if (dataSnapshot.exists()) {
//                    try {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            String postKey = dataSnapshot1.getKey();
//                            postTime = dataSnapshot1.child("postTime").getValue(String.class);
//
//                            todayDate = sdf.parse(currentDate);
//                            postedDate = sdf.parse(postTime);
//
//                            long diff = todayDate.getTime() - postedDate.getTime();
//                            long seconds = diff / 1000;
//                            long minutes = seconds / 60;
//                            long hours = minutes / 60;
//                            int days = (int) (hours / 24);
//
//                            if ((days / 365) > 0) {
//                                int year = days / 365;
//                                int leftMonths = days % 365;
//                                int month = leftMonths / 30;
//                                int leftDays = month % 30;
//                                cardPostTime = (year + "y " + month + "m ago");
//                            } else if ((days / 30) > 0) {
//                                int month = days / 30;
//                                int leftDays = days % 30;
//                                cardPostTime = (month + "m ago");
//                            } else if ((hours / 24) > 0) {
//                                cardPostTime = (days + "d ago");
//                            } else if ((minutes / 60) > 0) {
//                                cardPostTime = (hours + "h ago");
//                            } else {
//                                cardPostTime = (minutes + "min ago");
//                            }
//                            saved_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime));
//                            cardAdapter.notifyDataSetChanged();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };

        savedPostValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                saved_posts.clear();
                if (dataSnapshot2.exists()) {
                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                        postValueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    try {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
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

//                                            we can see here blocked users posts because user has previously choose to save those posts and then afterwards he blocked the user.

                                            if (!hiddenList.contains(dataSnapshot1.getKey())) {
                                                if (showNSFWvalue == 0) {
                                                    if (dataSnapshot1.child("NSFW").getValue(Integer.class) == 0) {
                                                        saved_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
                                                                dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class),
                                                                "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class),
                                                                dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime,
                                                                dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class),
                                                                dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class),
                                                                dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class),
                                                                dataSnapshot1.getKey()));
                                                    }
                                                } else {
                                                    saved_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
                                                            dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class),
                                                            "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class),
                                                            dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime,
                                                            dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class),
                                                            dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class),
                                                            dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class),
                                                            dataSnapshot1.getKey()));
                                                }
                                                cardAdapter.notifyDataSetChanged();
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

                        mRef_post.orderByChild("imagePath").equalTo(dataSnapshot3.child("imagePath").getValue(String.class)).addValueEventListener(postValueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef_user.orderByChild("postNumber").addListenerForSingleValueEvent(savedPostValueEventListener);
//        mRef_post.orderByChild("postNumber").addValueEventListener(postValueEventListener);

        savedPostsRecycler.setLayoutManager(cardManager);
        savedPostsRecycler.setAdapter(cardAdapter);
//        }


        return view;
    }

    private void getHiddenPostsLists() {
        mRef.child("users").child(userId).child("hiddenPosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot5 : dataSnapshot.getChildren()) {
                        hiddenList.add(dataSnapshot5.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        mRef_user.removeEventListener(savedPostValueEventListener);
        mRef_post.removeEventListener(postValueEventListener);
    }

}
