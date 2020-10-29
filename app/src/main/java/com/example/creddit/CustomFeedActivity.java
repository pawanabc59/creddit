package com.example.creddit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.creddit.Adapter.CustomFeedAdapter;
import com.example.creddit.Adapter.CustomFeedTabAdapter;
import com.example.creddit.Adapter.TabPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class CustomFeedActivity extends AppCompatActivity {

    String feedName;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_custom_feed);

        Intent intent = getIntent();
        feedName = intent.getExtras().getString("FeedName");

        TabLayout tabLayout = findViewById(R.id.custom_feed_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("Communities"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.custom_feed_content_pager);
        CustomFeedTabAdapter customFeedTabAdapter = new CustomFeedTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), feedName);
        viewPager.setAdapter(customFeedTabAdapter);
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