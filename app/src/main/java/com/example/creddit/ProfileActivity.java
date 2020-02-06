package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity {

    SharedPref sharedPref;
    TextView profile_edit;
    ImageView profileImage, profileBannerImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    CollapsingToolbarLayout toolbarLayout;

    String userId;

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

        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        profileBannerImage = findViewById(R.id.profileBannerImage);

        toolbarLayout = findViewById(R.id.toolbarTitle);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        userId = user.getUid();

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

        mRef.child(userId).addValueEventListener(profileValueEventListener);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        profile_edit = findViewById(R.id.profile_edit);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Edit is clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        TabLayout tabLayout = findViewById(R.id.profile_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.profile_content_pager);
        ProfilePageTabAdapter profilePageTabAdapter = new ProfilePageTabAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(profilePageTabAdapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRef.child(userId).removeEventListener(profileValueEventListener);
    }
}
