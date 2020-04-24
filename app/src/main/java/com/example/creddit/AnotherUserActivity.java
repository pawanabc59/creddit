package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creddit.Adapter.AnotherUserProfileTabAdapter;
import com.example.creddit.Adapter.ProfilePageTabAdapter;
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

public class AnotherUserActivity extends AppCompatActivity {

    SharedPref sharedPref;
    TextView profile_join, profile_joined, sameUserProfile;
    ImageView profileImage, profileBannerImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    CollapsingToolbarLayout toolbarLayout;

    String userId, anotherUserId;

    ValueEventListener profileValueEventListener;

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

        setContentView(R.layout.activity_another_user);

        Intent intent = getIntent();
        anotherUserId = intent.getExtras().getString("anotherUserId");

        profileImage = findViewById(R.id.anotherUserProfileImage);
        profileBannerImage = findViewById(R.id.anotherUserProfileBannerImage);

        toolbarLayout = findViewById(R.id.anotherUserToolbarTitle);
//        toolbar = findViewById(R.id.profile_toolbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        profileValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileImagePath = dataSnapshot.child("profileImage").getValue().toString();
                    String profileBannerImagePath = dataSnapshot.child("profileBannerImage").getValue().toString();
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

                    toolbarLayout.setTitle(dataSnapshot.child("optionalName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child(anotherUserId).addValueEventListener(profileValueEventListener);

        Toolbar toolbar = findViewById(R.id.anotherUserProfileToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
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

        if (user == null){
            profile_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("You need to login first to join this!");
                }
            });
        }
        else {
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
                    mRef.child(userId).child("followingList").child(anotherUserId).setValue(anotherUserId);
                    mRef.child(userId).child("followingList").child(userId).setValue(userId);
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
                    mRef.child(userId).child("followingList").child(anotherUserId).removeValue();
                    profile_joined.setVisibility(View.GONE);
                    sameUserProfile.setVisibility(View.GONE);
                    profile_join.setVisibility(View.VISIBLE);
                    showToast("You unjoined this User!");
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
        AnotherUserProfileTabAdapter anotherUserProfileTabAdapter = new AnotherUserProfileTabAdapter(getSupportFragmentManager(),tabLayout.getTabCount(), anotherUserId);
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

    public void showToast(String toast_text){
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
