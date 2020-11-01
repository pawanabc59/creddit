package com.example.creddit.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Adapter.UsersAdapter;
import com.example.creddit.Model.UsersModel;
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

public class DirectsFragment extends Fragment {

    RecyclerView ShowUsersRecyclerView;
    List<UsersModel> usersModels;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String userId;
    ValueEventListener UsersListValueEventListener, searchUserValueEventListener;
    UsersAdapter usersAdapter;
    SearchView searchUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directs, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        ShowUsersRecyclerView = view.findViewById(R.id.ShowUsersRecyclerView);
        searchUsers = view.findViewById(R.id.searchUsers);

        usersModels = new ArrayList<>();

        usersAdapter = new UsersAdapter(getContext(), usersModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

//        UsersListValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersModels.clear();
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
////                        if (!userId.equals(dataSnapshot1.getKey())) {
////                            usersModels.add(new UsersModel(dataSnapshot1.child("optionalName").getValue(String.class), dataSnapshot1.child("profileImage").getValue(String.class), dataSnapshot1.getKey().toString() ,dataSnapshot1.child("userNumber").getValue(Integer.class)));
////                        }
////                        usersAdapter.notifyDataSetChanged();
//                        mRef.child("users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
//                                if (dataSnapshot3.exists()) {
//                                    usersModels.add(new UsersModel(dataSnapshot3.child("optionalName").getValue(String.class), dataSnapshot3.child("profileImage").getValue(String.class), dataSnapshot3.getKey().toString(), dataSnapshot3.child("userNumber").getValue(Integer.class)));
//                                    usersAdapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };

//        mRef.child("users").addValueEventListener(UsersListValueEventListener);

//        try {
//        mRef.child("users").child(userId).child("friends").addValueEventListener(UsersListValueEventListener);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        showFriends();

        ShowUsersRecyclerView.setLayoutManager(linearLayoutManager);
        ShowUsersRecyclerView.setAdapter(usersAdapter);

        searchUsers.setIconified(false);
//        searchUsers.setFocusable(true);
        searchUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchUser(query);
                } else {
                    showFriends();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                if (!TextUtils.isEmpty(newText.trim())) {
                    searchUser(newText);
                } else {
                    showFriends();
                }
                return true;
            }
        });

        return view;
    }

    private void showFriends() {
        UsersListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersModels.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        if (!userId.equals(dataSnapshot1.getKey())) {
//                            usersModels.add(new UsersModel(dataSnapshot1.child("optionalName").getValue(String.class), dataSnapshot1.child("profileImage").getValue(String.class), dataSnapshot1.getKey().toString() ,dataSnapshot1.child("userNumber").getValue(Integer.class)));
//                        }
//                        usersAdapter.notifyDataSetChanged();
                        mRef.child("users").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                if (dataSnapshot3.exists()) {
                                    usersModels.add(new UsersModel(dataSnapshot3.child("optionalName").getValue(String.class), dataSnapshot3.child("profileImage").getValue(String.class), dataSnapshot3.getKey().toString(), dataSnapshot3.child("userNumber").getValue(Integer.class)));
                                    usersAdapter.notifyDataSetChanged();
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

        mRef.child("users").child(userId).child("friends").addValueEventListener(UsersListValueEventListener);
    }

    private void searchUser(final String newText) {
        searchUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersModels.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (!userId.equals(dataSnapshot1.getKey())) {
                            try {
                                if (dataSnapshot1.child("optionalName").getValue(String.class).toLowerCase().contains(newText.toLowerCase()) ||
                                        dataSnapshot1.child("email").getValue(String.class).toLowerCase().contains(newText.toLowerCase())) {
                                    usersModels.add(new UsersModel(dataSnapshot1.child("optionalName").getValue(String.class),
                                            dataSnapshot1.child("profileImage").getValue(String.class),
                                            dataSnapshot1.getKey().toString(),
                                            dataSnapshot1.child("userNumber").getValue(Integer.class)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("users").addValueEventListener(searchUserValueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRef.child("users").child(userId).child("friends").removeEventListener(UsersListValueEventListener);
        try {
            mRef.child("users").removeEventListener(searchUserValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
