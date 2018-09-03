package com.jross.blankapp.adapters.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jross.blankapp.fragments.ClassicFragment;
import com.jross.blankapp.fragments.SwipeFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public MainPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ClassicFragment.newInstance();
            case 1:
                return SwipeFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
