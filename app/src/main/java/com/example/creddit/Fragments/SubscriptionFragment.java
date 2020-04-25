package com.example.creddit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.creddit.Adapter.FollowingListAdapter;
import com.example.creddit.Model.FollowingListModel;
import com.example.creddit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionFragment extends Fragment {

    RecyclerView sub_recyclerview, sub_fav_recyclerview;
    List<FollowingListModel> followingListModels, favouriteFollowingListModels;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    String userId;
    ValueEventListener joinedListValueEventListener, searchAllUserValueEventListener, favouriteListValueEventListener;
    androidx.appcompat.widget.SearchView searchAllUsers;
    FollowingListAdapter followingListAdapter, favouriteFollowingListAdapter;
    TextView favouriteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        sub_recyclerview = view.findViewById(R.id.sub_recyclerview);
        searchAllUsers = view.findViewById(R.id.searchAllUsers);
        favouriteList = view.findViewById(R.id.favouriteList);
        sub_fav_recyclerview = view.findViewById(R.id.sub_fav_recyclerview);

        searchAllUsers.setIconified(false);

        followingListModels = new ArrayList<>();
        favouriteFollowingListModels = new ArrayList<>();
//        followingListModals.add(new FollowingListModel(R.drawable.zoro, "Zoro Fan Club"));

        followingListAdapter = new FollowingListAdapter(getContext(), followingListModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        favouriteFollowingListAdapter = new FollowingListAdapter(getContext(), followingListModels);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());

        favouriteListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteFollowingListModels.clear();
                if (dataSnapshot.exists()){
                    favouriteList.setVisibility(View.VISIBLE);
                    sub_fav_recyclerview.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        try {
                            if (dataSnapshot1.child("favourite").getValue(Integer.class) == 1) {
                                mRef.child("users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                        if (!userId.equals(dataSnapshot2.getKey())) {
                                            favouriteFollowingListModels.add(new FollowingListModel(dataSnapshot2.child("profileImage").getValue(String.class), dataSnapshot2.child("optionalName").getValue(String.class), dataSnapshot2.getKey()));
                                            favouriteFollowingListAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    favouriteList.setVisibility(View.GONE);
                    sub_fav_recyclerview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.child("users").child(userId).child("followingList").orderByChild("favourite").equalTo(1).addValueEventListener(favouriteListValueEventListener);

        showJoinedUsers();

        searchAllUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s.trim())) {
                    searchAllUser(s);
                } else {
                    showJoinedUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s.trim())) {
                    searchAllUser(s);
                } else {
                    showJoinedUsers();
                }
                return true;
            }
        });

        sub_fav_recyclerview.setLayoutManager(linearLayoutManager2);
        sub_fav_recyclerview.setAdapter(favouriteFollowingListAdapter);

        sub_recyclerview.setLayoutManager(linearLayoutManager);
        sub_recyclerview.setAdapter(followingListAdapter);

        return view;
    }

    public void searchAllUser(final String username){
        searchAllUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingListModels.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        if (!userId.equals(dataSnapshot1.getKey())){
                            if (dataSnapshot1.child("optionalName").getValue(String.class).toLowerCase().contains(username.toLowerCase()) ||
                                    dataSnapshot1.child("email").getValue(String.class).toLowerCase().contains(username.toLowerCase())) {
                                followingListModels.add(new FollowingListModel(dataSnapshot1.child("profileImage").getValue(String.class), dataSnapshot1.child("optionalName").getValue(String.class), dataSnapshot1.getKey()));
                            }
                        }
                        followingListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.child("users").addListenerForSingleValueEvent(searchAllUserValueEventListener);
    }

    public void showJoinedUsers(){
        joinedListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingListModels.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        mRef.child("users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if (!userId.equals(dataSnapshot2.getKey())) {
                                    followingListModels.add(new FollowingListModel(dataSnapshot2.child("profileImage").getValue(String.class), dataSnapshot2.child("optionalName").getValue(String.class), dataSnapshot2.getKey()));
                                    followingListAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("users").child(userId).child("followingList").addListenerForSingleValueEvent(joinedListValueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRef.child("users").child(userId).child("followingList").removeEventListener(favouriteListValueEventListener);

//        mRef.child("users").child(userId).child("followingList").removeEventListener(joinedListValueEventListener);

//        try {
//            mRef.child("users").removeEventListener(searchAllUserValueEventListener);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
