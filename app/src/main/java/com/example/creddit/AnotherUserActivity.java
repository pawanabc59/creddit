package com.example.creddit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.creddit.Adapter.AnotherUserProfileTabAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class AnotherUserActivity extends AppCompatActivity {

    SharedPref sharedPref;
    TextView profile_join, profile_joined, sameUserProfile;
    SearchableSpinner addToCustomFeed;
    ImageView profileImage, profileBannerImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef, mRef2;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ArrayList<String> customFeedNameList;
    ArrayAdapter<String> adapter;

    CollapsingToolbarLayout toolbarLayout;

    String userId, anotherUserId, subType, profileImagePath, profileBannerImagePath, subName, customFeedName;

    ValueEventListener profileValueEventListener, subRedditValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_another_user);

        Intent intent = getIntent();
        anotherUserId = intent.getExtras().getString("anotherUserId");
        subType = intent.getExtras().getString("subType");

        profileImage = findViewById(R.id.anotherUserProfileImage);
        profileBannerImage = findViewById(R.id.anotherUserProfileBannerImage);
        addToCustomFeed = findViewById(R.id.addToCustomFeed);

        toolbarLayout = findViewById(R.id.anotherUserToolbarTitle);
//        toolbar = findViewById(R.id.profile_toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");
        mRef2 = firebaseDatabase.getReference("creddit");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (subType.equals("user")) {
            profileValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        profileImagePath = dataSnapshot.child("profileImage").getValue().toString();
                        profileBannerImagePath = dataSnapshot.child("profileBannerImage").getValue().toString();
                        if (profileImagePath.equals("null")) {
                            Picasso.get().load(R.drawable.reddit_logo_hd).into(profileImage);
                        } else {
                            Picasso.get().load(profileImagePath).error(R.drawable.reddit_logo_hd).into(profileImage);
                        }

                        if (profileBannerImagePath.equals("null")) {
                            Picasso.get().load(R.drawable.reddit_logo_hd).into(profileBannerImage);
                        } else {
                            Picasso.get().load(profileBannerImagePath).error(R.drawable.reddit_logo_hd).into(profileBannerImage);
                        }
                        subName = dataSnapshot.child("optionalName").getValue(String.class);
                        toolbarLayout.setTitle(dataSnapshot.child("optionalName").getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef.child(anotherUserId).addValueEventListener(profileValueEventListener);

        } else {

            subRedditValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        profileImagePath = dataSnapshot.child("subPicture").getValue().toString();
                        profileBannerImagePath = dataSnapshot.child("subPictureBanner").getValue().toString();
                        if (profileImagePath.equals("null")) {
                            Picasso.get().load(R.drawable.reddit_logo_hd).into(profileImage);
                        } else {
                            Picasso.get().load(profileImagePath).error(R.drawable.reddit_logo_hd).into(profileImage);
                        }

                        if (profileBannerImagePath.equals("null")) {
                            Picasso.get().load(R.drawable.reddit_logo_hd).into(profileBannerImage);
                        } else {
                            Picasso.get().load(profileBannerImagePath).error(R.drawable.reddit_logo_hd).into(profileBannerImage);
                        }
                        subName = dataSnapshot.child("subName").getValue(String.class);
                        toolbarLayout.setTitle(dataSnapshot.child("subName").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef2.child("subreddits").child(anotherUserId).addValueEventListener(subRedditValueEventListener);

            if (user != null){
                userId = user.getUid();
                customFeedNameList = new ArrayList<>();
                customFeedNameList.add("Add to Custom Feed");

                getCustomFeedNames(customFeedNameList);

                addToCustomFeed.setVisibility(View.VISIBLE);
                addToCustomFeed.setTitle("Add to Custom Feed");
                addToCustomFeed.setPrompt("Add to Custom Feed");
                addToCustomFeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i != 0) {
                            customFeedName = customFeedNameList.get(i);
                            mRef.child(userId).child("customFeed").child(customFeedName).child(anotherUserId).setValue(1);
                            Toast.makeText(getApplicationContext(), "Added to Custom Feed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, customFeedNameList);
                addToCustomFeed.setAdapter(adapter);
            }
        }

        Toolbar toolbar = findViewById(R.id.anotherUserProfileToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profile_join = findViewById(R.id.anotherUserProfileJoin);
        profile_joined = findViewById(R.id.anotherUserProfileJoined);
        sameUserProfile = findViewById(R.id.sameUserProfile);

        if (user == null) {
            profile_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("You need to login first to join this!");
                }
            });
        } else {
            userId = user.getUid();

            if (userId.equals(anotherUserId)) {
                profile_join.setVisibility(View.GONE);
                profile_joined.setVisibility(View.GONE);
                sameUserProfile.setVisibility(View.VISIBLE);
                showToast("This is Your Profile!");
            }

            mRef.child(userId).child("followingList").child(anotherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (!userId.equals(anotherUserId)) {
                            profile_join.setVisibility(View.GONE);
                            sameUserProfile.setVisibility(View.GONE);
                            profile_joined.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            profile_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRef.child(userId).child("followingList").child(anotherUserId).child("key").setValue(anotherUserId);
                    mRef.child(userId).child("followingList").child(anotherUserId).child("favourite").setValue(0);
                    mRef.child(userId).child("followingList").child(anotherUserId).child("profilePicture").setValue(profileImagePath);
                    mRef.child(userId).child("followingList").child(anotherUserId).child("name").setValue(subName);
                    mRef.child(userId).child("followingList").child(anotherUserId).child("type").setValue(subType);
                    mRef.child(userId).child("followingList").child(userId).child("key").setValue(userId);
                    mRef.child(userId).child("followingList").child(userId).child("favourite").setValue(0);
                    mRef.child(userId).child("followingList").child(userId).child("type").setValue(subType);
                    mRef.child(userId).child("followingList").child(userId).child("profilePicture").setValue("selfPicture");
                    mRef.child(userId).child("followingList").child(userId).child("name").setValue("self");
                    if (subType.equals("sub")){
                        mRef2.child("subreddits").child(anotherUserId).child("members").child(userId).setValue(1);
                    }
                    profile_join.setVisibility(View.GONE);
                    sameUserProfile.setVisibility(View.GONE);
                    profile_joined.setVisibility(View.VISIBLE);
                    showToast("You joined this User!");
                }
            });

            profile_joined.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                mRef.child(userId).child("followingList").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()){
//                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                                if (dataSnapshot1.getKey().equals(anotherUserId)){
                    mRef.child(userId).child("followingList").child(anotherUserId).child("key").removeValue();
                    mRef.child(userId).child("followingList").child(anotherUserId).child("favourite").removeValue();
                    mRef.child(userId).child("followingList").child(anotherUserId).child("type").removeValue();
                    mRef.child(userId).child("followingList").child(anotherUserId).child("profilePicture").removeValue();
                    mRef.child(userId).child("followingList").child(anotherUserId).child("name").removeValue();
                    if (subType.equals("sub")){
                        mRef2.child("subreddits").child(anotherUserId).child("members").child(userId).removeValue();
                    }
                    profile_joined.setVisibility(View.GONE);
                    sameUserProfile.setVisibility(View.GONE);
                    profile_join.setVisibility(View.VISIBLE);
                    showToast("You left this User!");
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                }
            });
        }

        TabLayout tabLayout = findViewById(R.id.anotherUserProfileTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.anotherUserProfileContentPager);
        AnotherUserProfileTabAdapter anotherUserProfileTabAdapter = new AnotherUserProfileTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), anotherUserId, subType);
        viewPager.setAdapter(anotherUserProfileTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getCustomFeedNames(final ArrayList<String> customFeedNameList) {
        mRef.child(userId).child("customFeed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        customFeedNameList.add(dataSnapshot1.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showToast(String toast_text) {
        LayoutInflater inflater = LayoutInflater.from(AnotherUserActivity.this);
        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(toast_text);

        Toast toast = new Toast(AnotherUserActivity.this);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            mRef.child(userId).removeEventListener(profileValueEventListener);
            mRef.child(anotherUserId).removeEventListener(subRedditValueEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
