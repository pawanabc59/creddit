package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    SharedPref sharedPref;
    TextView profile_edit;

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

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        profile_edit = findViewById(R.id.profile_edit);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Edit is clicked", Toast.LENGTH_SHORT).show();
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
}
