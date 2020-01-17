package com.example.creddit.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.creddit.Fragments.CustomFeedFragment;
import com.example.creddit.Fragments.SubscriptionFragment;

public class ProfilePageTabAdapter extends FragmentStatePagerAdapter {

    int counttab;

    public ProfilePageTabAdapter(FragmentManager fm, int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
                return subscriptionFragment;
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
