package com.example.creddit.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creddit.Adapter.CustomFeedAdapter;
import com.example.creddit.Model.CustomFeedModel;
import com.example.creddit.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomFeedFragment extends Fragment {
    RecyclerView customFeedRecyclerView;
    TextView creatCustomFeed;
    DatabaseReference mRef;
    FirebaseUser user;
    String userId, feedName;
    ArrayList<CustomFeedModel> customFeedModels;
    CustomFeedAdapter customFeedAdapter;
    ValueEventListener customFeedValueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_feed, container, false);

        mRef = FirebaseDatabase.getInstance().getReference("creddit");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        customFeedRecyclerView = view.findViewById(R.id.customFeedRecyclerView);
        creatCustomFeed = view.findViewById(R.id.createCustomFeed);

        customFeedModels = new ArrayList<>();

        customFeedAdapter = new CustomFeedAdapter(getContext(), customFeedModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        customFeedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customFeedModels.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        customFeedModels.add(new CustomFeedModel(dataSnapshot1.getKey()));
                        customFeedAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.child("users").child(userId).child("customFeed").addValueEventListener(customFeedValueEventListener);

        customFeedRecyclerView.setLayoutManager(linearLayoutManager);
        customFeedRecyclerView.setAdapter(customFeedAdapter);

        creatCustomFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edittext = new EditText(getContext());
                edittext.setTextColor(R.attr.buttoncolor);
                alert.setMessage("Create New Custom Feed")
                        .setTitle("Custom Feed")
                        .setView(edittext)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                feedName = edittext.getText().toString();
                                if (feedName.isEmpty()) {
                                    edittext.setError("Please enter name");
                                    dialog.dismiss();
                                } else {
                                    mRef.child("users").child(userId).child("customFeed").child(feedName).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Snackbar.make(view, "Custom Feed Already Exists.", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                mRef.child("users").child(userId).child("customFeed").child(feedName).child("1").setValue("1");
                                                Toast.makeText(getContext(), "Feed Created", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRef.child("users").child(userId).child("customFeed").removeEventListener(customFeedValueEventListener);
    }
}
