package com.example.creddit.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.creddit.Fragments.AnotherUserUploadedImageFragment;
import com.example.creddit.Fragments.CustomFeedFragment;
import com.example.creddit.Fragments.SubscriptionFragment;
import com.example.creddit.Fragments.UploadedImageFragment;

public class AnotherUserProfileTabAdapter extends FragmentStatePagerAdapter {
    int counttab;
    String anotherUserId, subType;

    public AnotherUserProfileTabAdapter(FragmentManager fm, int counttab, String anotherUserId, String subType) {
        super(fm);
        this.counttab = counttab;
        this.anotherUserId = anotherUserId;
        this.subType = subType;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                AnotherUserUploadedImageFragment anotherUserUploadedImageFragment = new AnotherUserUploadedImageFragment(anotherUserId, subType);
                return anotherUserUploadedImageFragment;
            case 1:
                CustomFeedFragment customFeedFragment = new CustomFeedFragment();
                return customFeedFragment;
            case 2:
                SubscriptionFragment subscriptionFragment1 = new SubscriptionFragment();
                return subscriptionFragment1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }
}
