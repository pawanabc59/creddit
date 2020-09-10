package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.creddit.Adapter.FollowingListAdapter;
import com.example.creddit.Model.FollowingListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MySubRedditListActivity extends AppCompatActivity {

    RecyclerView subRedditRecyclerView;
    SharedPref sharedPref;
    List<FollowingListModel> subRedditListModel;
    FollowingListAdapter subRedditListAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseUser user;
    String userId;
    ArrayList<String> subIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_my_sub_reddit_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        subRedditRecyclerView = findViewById(R.id.subRedditRecyclerView);

        subRedditListModel = new ArrayList<>();
        subIDs = new ArrayList<>();
        subRedditListAdapter = new FollowingListAdapter(MySubRedditListActivity.this, subRedditListModel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MySubRedditListActivity.this);

        loadSubRedditsIds();

        mRef.child("subreddits").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subRedditListModel.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        if (subIDs.contains(dataSnapshot1.getKey())){
                            subRedditListModel.add(new FollowingListModel(dataSnapshot1.child("subPicture").getValue(String.class),
                                    dataSnapshot1.child("subName").getValue(String.class), dataSnapshot1.getKey(), "sub", "mySubRedditList"));
                            subRedditListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        subRedditRecyclerView.setLayoutManager(linearLayoutManager);
        subRedditRecyclerView.setAdapter(subRedditListAdapter);
    }

    private void loadSubRedditsIds() {

        mRef.child("users").child(userId).child("createdSubreddits").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        subIDs.add(dataSnapshot1.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}