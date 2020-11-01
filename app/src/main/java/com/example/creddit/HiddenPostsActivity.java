package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HiddenPostsActivity extends AppCompatActivity {

    RecyclerView historyRecyclerView;
    List<CardModel> hiddenModel;
    SimpleDateFormat sdf;
    String currentDate, postTime, cardPostTime, userId;
    Date todayDate, postedDate;
    CardAdapter cardAdapter;

    ValueEventListener hiddenPostsValueEventListener, nsfwValueEventListener;
    ArrayList<String> hiddenList = new ArrayList<>();

    DatabaseReference mRef, postRef;
    int showNSFWvalue = 0, blurNSFWvalue = 0;

    SharedPref sharedPref;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_hidden_posts);

        toolbar = findViewById(R.id.hiddenToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        historyRecyclerView = findViewById(R.id.recycler_hidden);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        mRef = FirebaseDatabase.getInstance().getReference("creddit");
        postRef = FirebaseDatabase.getInstance().getReference("creddit").child("posts").child("imagePosts");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getHiddenPostsLists();

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


        hiddenModel = new ArrayList<>();

        cardAdapter = new CardAdapter(this, hiddenModel, HiddenPostsActivity.this, "uploadedImageFragment");
        LinearLayoutManager cardManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        cardManager.setReverseLayout(true);
//        cardManager.setStackFromEnd(true);

        hiddenPostsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hiddenModel.clear();
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            if (hiddenList.contains(dataSnapshot1.getKey())) {

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
                                        hiddenModel.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
                                                dataSnapshot1.child("imagePath").getValue(String.class), dataSnapshot1.child("subName").getValue(String.class),
                                                "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class),
                                                dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime,
                                                dataSnapshot1.child("userId").getValue(String.class), dataSnapshot1.child("NSFW").getValue(Integer.class),
                                                dataSnapshot1.child("spoiler").getValue(Integer.class), dataSnapshot1.child("postType").getValue(String.class),
                                                dataSnapshot1.child("subId").getValue(String.class), dataSnapshot1.child("subType").getValue(String.class),
                                                dataSnapshot1.getKey()));
                                    }
                                } else {
                                    hiddenModel.add(new CardModel(dataSnapshot1.child("cardPostProfileImage").getValue(String.class),
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

        postRef.orderByChild("postNumber").addValueEventListener(hiddenPostsValueEventListener);

        historyRecyclerView.setLayoutManager(cardManager);
        historyRecyclerView.setAdapter(cardAdapter);
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
    protected void onDestroy() {
        super.onDestroy();
        try {
            postRef.removeEventListener(hiddenPostsValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}