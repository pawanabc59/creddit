package com.example.creddit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPageAdapter extends FragmentStatePagerAdapter {
    int counttab;

    public TabPageAdapter(FragmentManager fm, int counttab) {
        super(fm);
        this.counttab = counttab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1 :
                PopularFragment popularFragment = new PopularFragment();
                return popularFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return counttab;
    }
}
