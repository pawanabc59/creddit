package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creddit.Adapter.FollowingListAdapter;
import com.example.creddit.Adapter.JoinedCommunityAdapter;
import com.example.creddit.Model.FollowingListModel;
import com.example.creddit.Model.JoinedCommunityModel;
import com.example.creddit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinedCommunitiesFragment extends Fragment {

    RecyclerView joinedCommunityRecyclerView;
    ArrayList<JoinedCommunityModel> joinedCommunityModel;
    JoinedCommunityAdapter joindedCommunityAdapter;
    DatabaseReference mRef;
    FirebaseUser user;
    String userId, feedName;
    ValueEventListener joinedCommunityValueEventListener;

    public JoinedCommunitiesFragment(String feedName) {
        this.feedName = feedName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joined_communities, container, false);

        mRef = FirebaseDatabase.getInstance().getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        joinedCommunityRecyclerView = view.findViewById(R.id.joined_communities_recycler);
        joinedCommunityModel = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        joindedCommunityAdapter = new JoinedCommunityAdapter(getContext(), joinedCommunityModel);

        joinedCommunityValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                joinedCommunityModel.clear();
                if (dataSnapshot.exists()){
                    for (final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                        if (!dataSnapshot1.getKey().equals("1")) {
                            mRef.child("subreddits").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    String membersCount = String.valueOf(dataSnapshot3.child("members").getChildrenCount());
                                    joinedCommunityModel.add(new JoinedCommunityModel(dataSnapshot3.child("subPicture").getValue(String.class),
                                            dataSnapshot3.child("subName").getValue(String.class), dataSnapshot3.getKey(), "sub", "joinedCommunity",
                                            feedName, membersCount));
                                    joindedCommunityAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.child("users").child(userId).child("customFeed").child(feedName).addValueEventListener(joinedCommunityValueEventListener);

        joinedCommunityRecyclerView.setLayoutManager(linearLayoutManager);
        joinedCommunityRecyclerView.setAdapter(joindedCommunityAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRef.child("users").child(userId).child("customFeed").child(feedName).removeEventListener(joinedCommunityValueEventListener);
    }
}