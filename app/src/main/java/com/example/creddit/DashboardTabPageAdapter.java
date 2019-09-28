package com.example.creddit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DashboardTabPageAdapter extends FragmentStatePagerAdapter {

    int counttab;

    public DashboardTabPageAdapter(FragmentManager fm, int counttab) {
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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }
}
