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

public class BlockedUsersListActivity extends AppCompatActivity {

    SharedPref sharedPref;
    RecyclerView blockedUserRecyclerView;
    List<FollowingListModel> blockUserListModel;
    FollowingListAdapter blockedListAdapter;

    DatabaseReference mRef;
    FirebaseUser user;
    String userId;
    ValueEventListener blockedUserListValueEventListener, usersListValueEventListener;
    ArrayList<String> blockArrayList;

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

        setContentView(R.layout.activity_blocked_users_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        mRef = FirebaseDatabase.getInstance().getReference("creddit");

        blockedUserRecyclerView = findViewById(R.id.blockedUserRecyclerView);

        blockUserListModel = new ArrayList<>();
        blockArrayList = new ArrayList<String>();

        blockedListAdapter = new FollowingListAdapter(this, blockUserListModel);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        blockedUserRecyclerView.setLayoutManager(linearLayoutManager);
        blockedUserRecyclerView.setAdapter(blockedListAdapter);

        blockedUserListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        blockArrayList.add(dataSnapshot1.child("key").getValue(String.class));
                    }

                    usersListValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            blockUserListModel.clear();
                            if (dataSnapshot.exists()){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (blockArrayList.contains(dataSnapshot1.getKey())){
                                        blockUserListModel.add(new FollowingListModel(dataSnapshot1.child("profileImage").getValue(String.class), dataSnapshot1.child("optionalName").getValue(String.class), dataSnapshot1.getKey(),"self", "blocked"));
                                        blockedListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };

                    mRef.child("users").addListenerForSingleValueEvent(usersListValueEventListener);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("users").child(userId).child("blockedUsers").addValueEventListener(blockedUserListValueEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mRef.child("users").child(userId).child("blockedUsers").removeEventListener(blockedUserListValueEventListener);
            mRef.child("users").removeEventListener(usersListValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}