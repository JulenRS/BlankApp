package com.jross.blankapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jross.blankapp.fragments.ClassicFragment;
import com.jross.blankapp.fragments.SwipeFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ClassicFragment();
            case 1:
                return new SwipeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
