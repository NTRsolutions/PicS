package com.justdoit.pics.adapater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户页面的viewpager adapter
 * Created by mengwen on 2015/11/2.
 */
public class UserInfoViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles; // 标题

    public UserInfoViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String>();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.isEmpty()) {
            return null;
        } else {
            return fragments.get(position);
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles.isEmpty()) {
            return null;
        } else {
            return titles.get(position);
        }
    }
}
