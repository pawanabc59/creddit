package com.example.creddit.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.creddit.Fragments.DirectsFragment;
import com.example.creddit.Fragments.HomeFragment;
import com.example.creddit.Fragments.PopularFragment;
import com.example.creddit.Fragments.SavedPostsFragment;

public class ChatTabAdapter extends FragmentStatePagerAdapter {

    int counttab;

    public ChatTabAdapter(FragmentManager fm, int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                DirectsFragment directsFragment = new DirectsFragment();
                return directsFragment;

            case 1 :
                PopularFragment popularFragment = new PopularFragment();
                return popularFragment;
            case 2 :
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }

}
