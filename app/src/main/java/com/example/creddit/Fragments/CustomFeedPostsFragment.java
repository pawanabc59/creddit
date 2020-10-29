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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomFeedPostsFragment extends Fragment {

    String feedName;
    RecyclerView recycler_custom_feed;
    List<CardModel> customFeedPosts;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef, mRef_post;
    FirebaseUser user;
    String userId, cardPostTime;
    CardAdapter cardAdapter;
    SharedPref sharedPref;

    String currentDate, postTime;
    SimpleDateFormat sdf;
    Date todayDate, postedDate;
    ValueEventListener postValueEventListener, followedUserslistValueEventListener, userFollowedValueEventListener;

    ArrayList<String> followedUsersId = new ArrayList<>();
    ArrayList<String> blockedList = new ArrayList<>();
    ArrayList<String> hiddenList = new ArrayList<>();
    int showNSFWvalue = 0, blurNSFWvalue = 0;

    public CustomFeedPostsFragment(String feedName) {
        this.feedName = feedName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_feed_posts, container, false);

        recycler_custom_feed = view.findViewById(R.id.custom_feed_posts_recyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        sharedPref = new SharedPref(getContext());

        customFeedPosts = new ArrayList<>();

        cardAdapter = new CardAdapter(getContext(), customFeedPosts, getActivity(), "customFeedFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        userId = user.getUid();

        try {
            showNSFWvalue = sharedPref.get_showNSFW();
            blurNSFWvalue = sharedPref.get_blurNSFW();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getBlockedUsersLists();
        getHiddenPostsLists();


        /*here followedUsersListValueEventListener is similar to followedSubsValueEventListener*/
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
                            customFeedPosts.clear();
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

                                        if (followedUsersId.contains(dataSnapshot3.child("subId").getValue(String.class))) {
                                            if (!blockedList.contains(dataSnapshot3.child("userId").getValue(String.class))) {
                                                if (!hiddenList.contains(postKey)) {
                                                    if (showNSFWvalue == 0) {
                                                        if (dataSnapshot3.child("NSFW").getValue(Integer.class) == 0) {
                                                            customFeedPosts.add(new CardModel(dataSnapshot3.child("cardPostProfileImage").getValue(String.class),
                                                                    dataSnapshot3.child("imagePath").getValue(String.class),
                                                                    dataSnapshot3.child("subName").getValue(String.class),
                                                                    "Posted by " + dataSnapshot3.child("uploadedBy").getValue(String.class),
                                                                    dataSnapshot3.child("cardTitle").getValue(String.class), cardPostTime,
                                                                    dataSnapshot3.child("userId").getValue(String.class),
                                                                    dataSnapshot3.child("NSFW").getValue(Integer.class),
                                                                    dataSnapshot3.child("spoiler").getValue(Integer.class),
                                                                    dataSnapshot3.child("postType").getValue(String.class),
                                                                    dataSnapshot3.child("subId").getValue(String.class),
                                                                    dataSnapshot3.child("subType").getValue(String.class)));
                                                        }
                                                    } else {
                                                        customFeedPosts.add(new CardModel(dataSnapshot3.child("cardPostProfileImage").getValue(String.class),
                                                                dataSnapshot3.child("imagePath").getValue(String.class),
                                                                dataSnapshot3.child("subName").getValue(String.class),
                                                                "Posted by " + dataSnapshot3.child("uploadedBy").getValue(String.class),
                                                                dataSnapshot3.child("cardTitle").getValue(String.class), cardPostTime,
                                                                dataSnapshot3.child("userId").getValue(String.class),
                                                                dataSnapshot3.child("NSFW").getValue(Integer.class),
                                                                dataSnapshot3.child("spoiler").getValue(Integer.class),
                                                                dataSnapshot3.child("postType").getValue(String.class),
                                                                dataSnapshot3.child("subId").getValue(String.class),
                                                                dataSnapshot3.child("subType").getValue(String.class)));
                                                    }
                                                    cardAdapter.notifyDataSetChanged();
                                                }
                                            }
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
                    mRef.child("posts").child("imagePosts").orderByChild("postNumber").addValueEventListener(userFollowedValueEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("users").child(userId).child("customFeed").child(feedName).addValueEventListener(followedUserslistValueEventListener);

        recycler_custom_feed.setLayoutManager(cardManager);
        recycler_custom_feed.setAdapter(cardAdapter);


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

        try {
            if (user == null) {
                mRef_post.removeEventListener(postValueEventListener);
            } else {
                mRef.child("users").child(userId).child("customFeed").child(feedName).removeEventListener(followedUserslistValueEventListener);
                mRef.child("posts").child("imagePosts").removeEventListener(userFollowedValueEventListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}