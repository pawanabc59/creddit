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

public class AnotherUserUploadedImageFragment extends Fragment {
    RecyclerView uploadedImageRecyclerView;
    List<CardModel> uploadedImage;
    SimpleDateFormat sdf;
    String currentDate, postTime, cardPostTime, userId;
    Date todayDate, postedDate;
    CardAdapter cardAdapter;

    ValueEventListener postValueEventListener, nsfwValueEventListener;

    DatabaseReference mRef, postRef;
    String anotherUserId, subType, subIdSelection;
    FirebaseUser user;
    ArrayList<String> hiddenList = new ArrayList<>();
    int showNSFWvalue = 0, blurNSFWvalue = 0;

    public AnotherUserUploadedImageFragment(String anotherUserId, String subType) {
        this.anotherUserId = anotherUserId;
        this.subType = subType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_uploaded_image, container, false);

//        Toast.makeText(getContext(), anotherUserId, Toast.LENGTH_LONG).show();

        uploadedImageRecyclerView = view.findViewById(R.id.uploadedImageRecyclerView);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        mRef = FirebaseDatabase.getInstance().getReference("creddit");
        postRef = FirebaseDatabase.getInstance().getReference("creddit").child("posts").child("imagePosts");

        user = FirebaseAuth.getInstance().getCurrentUser();
        hiddenList.add("RandomStringSoAppDoesNotCrashWhenNoOneIsLoggedIn");

        if (user != null) {
            userId = user.getUid();

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

            getHiddenPostsLists();
        }

        uploadedImage = new ArrayList<>();

        cardAdapter = new CardAdapter(getContext(), uploadedImage, getActivity(), "anotherUserPopularFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);


        postValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadedImage.clear();
                if (subType.equals("user")){
                    subIdSelection="userId";
                } else {
                    subIdSelection="subId";
                }
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.child(subIdSelection).getValue(String.class).equals(anotherUserId)) {

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

                                if (!hiddenList.contains(dataSnapshot1.getKey())) {
                                    if (showNSFWvalue == 0) {
                                        if (dataSnapshot1.child("NSFW").getValue(Integer.class) == 0) {
                                            uploadedImage.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
                                                    dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class),
                                                    "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class),
                                                    dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime,
                                                    dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class),
                                                    dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class),
                                                    dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class), dataSnapshot1.getKey()));
                                        }
                                    } else {
                                        uploadedImage.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
                                                dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class),
                                                "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class),
                                                dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime,
                                                dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class),
                                                dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class),
                                                dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class),dataSnapshot1.getKey()));
                                    }
                                    cardAdapter.notifyDataSetChanged();
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

        postRef.orderByChild("postNumber").addValueEventListener(postValueEventListener);

        uploadedImageRecyclerView.setLayoutManager(cardManager);
        uploadedImageRecyclerView.setAdapter(cardAdapter);

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
        postRef.removeEventListener(postValueEventListener);
    }
}
