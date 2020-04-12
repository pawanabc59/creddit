package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.creddit.Adapter.CardAdapter;
import com.example.creddit.Model.CardModal;
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

public class SavePostActivity extends AppCompatActivity {

    SharedPref sharedPref;
    RecyclerView savedPostsRecyclerView;
    List<CardModal> savedPostsList;
    SimpleDateFormat sdf;
    String currentDate, postTime, cardPostTime, userId;
    Date todayDate, postedDate;
    CardAdapter cardAdapter;

    ValueEventListener postValueEventListener;

    DatabaseReference rootRef, postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_save_post);

        savedPostsRecyclerView = findViewById(R.id.savedPostsRecyclerView);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        currentDate = sdf.format(new Date());

        postRef = FirebaseDatabase.getInstance().getReference("creddit").child("posts").child("imagePosts");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        savedPostsList = new ArrayList<>();

        cardAdapter = new CardAdapter(getApplicationContext(), savedPostsList, this);
        LinearLayoutManager cardManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);


        postValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedPostsList.clear();
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

                            savedPostsList.add(new CardModal(dataSnapshot1.child("cardPostProfileImage").getValue(String.class), dataSnapshot1.child("imagePath").getValue(String.class), "Posted by " + dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("uploadedBy").getValue(String.class), dataSnapshot1.child("cardTitle").getValue(String.class), cardPostTime));
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

        postRef.orderByChild("userId").equalTo(userId).addValueEventListener(postValueEventListener);

        savedPostsRecyclerView.setLayoutManager(cardManager);
        savedPostsRecyclerView.setAdapter(cardAdapter);


    }
}
