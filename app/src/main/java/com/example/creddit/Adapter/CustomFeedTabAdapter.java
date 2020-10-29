package com.example.creddit.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.creddit.Fragments.CustomFeedPostsFragment;
import com.example.creddit.Fragments.HomeFragment;
import com.example.creddit.Fragments.JoinedCommunitiesFragment;
import com.example.creddit.Fragments.PopularFragment;

public class CustomFeedTabAdapter extends FragmentStatePagerAdapter {

    int countTab;
    String feedName;

    public CustomFeedTabAdapter(FragmentManager fm, int countTab, String feedName) {
        super(fm);
        this.countTab = countTab;
        this.feedName = feedName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                CustomFeedPostsFragment customFeedPostsFragment = new CustomFeedPostsFragment(feedName);
                return customFeedPostsFragment;
            case 1 :
                JoinedCommunitiesFragment joinedCommunitiesFragment = new JoinedCommunitiesFragment(feedName);
                return joinedCommunitiesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
