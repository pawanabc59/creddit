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
import com.example.creddit.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recycler_home_posts;
    List<CardModel> home_posts;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef, mRef_user, mRef_post, mRef2;
    FirebaseUser user;
    String userId;
    String cardProfileImage = "", cardPostUserId, cardImage, cardTitle, postedBy, cardDescription, cardPostTime;
    CardAdapter cardAdapter;
    SharedPref sharedPref;

    String currentDate, postTime;
    SimpleDateFormat sdf;
    Date todayDate, postedDate;
    ValueEventListener postValueEventListener, followedUserslistValueEventListener, userFollowedValueEventListener, nsfwValueEventListener;

    ArrayList<String> followedUsersId = new ArrayList<>();
    ArrayList<String> blockedList = new ArrayList<>();
    int showNSFWvalue = 0, blurNSFWvalue = 0;

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

        sharedPref = new SharedPref(getContext());

        home_posts = new ArrayList<>();
//        withourThisStringAppWouldNotShowAnythingBecauseArrayWillBecomeNULLOtherwise
//        blockedList.add("randomString");

        cardAdapter = new CardAdapter(getContext(), home_posts, getActivity(), "homeFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        home_posts.add(new CardModel(R.drawable.zoro,R.drawable.zoro, "Zoro Fan Club", "Pawan Kumar Maurya", "Check my awesome zoro wallpaper" ));

        if (user == null) {

            showPosts();

        } else {
            userId = user.getUid();

//            nsfwValueEventListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        showNSFWvalue = dataSnapshot.child("showNSFW").getValue(Integer.class);
//                        blurNSFWvalue = dataSnapshot.child("blurNSFW").getValue(Integer.class);

            try {
                showNSFWvalue = sharedPref.get_showNSFW();
                blurNSFWvalue = sharedPref.get_blurNSFW();
            } catch (Exception e) {
                e.printStackTrace();
            }

            getBlockedUsersLists();

            followedUserslistValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            followedUsersId.add(dataSnapshot1.getKey());
                        }
                        userFollowedValueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                                home_posts.clear();
                                if (dataSnapshot2.exists()) {
                                    try {

                                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                            String postKey = dataSnapshot3.getKey();
                                            postTime = dataSnapshot3.child("postTime").getValue(String.class);

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

                                            if (followedUsersId.contains(dataSnapshot3.child("userId").getValue(String.class))) {
                                                if (!blockedList.contains(dataSnapshot3.child("userId").getValue(String.class))) {
                                                    if (showNSFWvalue == 0) {
                                                        if (dataSnapshot3.child("NSFW").getValue(Integer.class) == 0) {
                                                            home_posts.add(new CardModel(dataSnapshot3.child("cardPostProfileImage").getValue(String.class), dataSnapshot3.child("imagePath").getValue(String.class), dataSnapshot3.child("subName").getValue(String.class), "Posted by " + dataSnapshot3.child("uploadedBy").getValue(String.class), dataSnapshot3.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot3.child("userId").getValue(String.class), dataSnapshot3.child("NSFW").getValue(Integer.class), dataSnapshot3.child("spoiler").getValue(Integer.class), dataSnapshot3.child("postType").getValue(String.class), dataSnapshot3.child("subId").getValue(String.class), dataSnapshot3.child("subType").getValue(String.class)));
                                                        }
                                                    } else {
                                                        home_posts.add(new CardModel(dataSnapshot3.child("cardPostProfileImage").getValue(String.class), dataSnapshot3.child("imagePath").getValue(String.class), dataSnapshot3.child("subName").getValue(String.class), "Posted by " + dataSnapshot3.child("uploadedBy").getValue(String.class), dataSnapshot3.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot3.child("userId").getValue(String.class), dataSnapshot3.child("NSFW").getValue(Integer.class), dataSnapshot3.child("spoiler").getValue(Integer.class), dataSnapshot3.child("postType").getValue(String.class), dataSnapshot3.child("subId").getValue(String.class), dataSnapshot3.child("subType").getValue(String.class)));
                                                    }
                                                    cardAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        mRef.child("posts").child("imagePosts").orderByChild("postNumber").addValueEventListener(userFollowedValueEventListener);
                    } else {
                        showPosts();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef.child("users").child(userId).child("followingList").addValueEventListener(followedUserslistValueEventListener);

//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            };
//            FirebaseDatabase.getInstance().getReference("creddit").child("users").child(userId).addValueEventListener(nsfwValueEventListener);

        }

        recycler_home_posts.setLayoutManager(cardManager);
        recycler_home_posts.setAdapter(cardAdapter);

        return view;
    }

    private void getBlockedUsersLists() {
        mRef.child("users").child(userId).child("blockedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot4 : dataSnapshot.getChildren()) {
                        blockedList.add(dataSnapshot4.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showPosts() {
        mRef_user = mRef.child("users");
        mRef_post = mRef.child("posts").child("imagePosts");

        postValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                home_posts.clear();
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
                            if (showNSFWvalue == 0) {
                                if (dataSnapshot1.child("NSFW").getValue(Integer.class) == 0) {
                                    home_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class), dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class), dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class)));
                                }
                            } else {
                                home_posts.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime, dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class), dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class), dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class)));
                            }
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (user == null) {
                mRef_post.removeEventListener(postValueEventListener);
            } else {
                mRef.child("users").child(userId).child("followingList").removeEventListener(followedUserslistValueEventListener);
                mRef.child("posts").child("imagePosts").removeEventListener(userFollowedValueEventListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
