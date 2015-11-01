package com.sn.controlers;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by xuhui on 15/8/16.
 */
public class SNFragmentScrollable extends SNViewPager {
    private MyFragmentPagerAdapter awesomeAdapter;
    public ArrayList<Fragment> fragments;

    public SNFragmentScrollable(Context context, FragmentManager manager, ArrayList<Fragment> list) {
        super(context);
        bindData(manager, list);
    }

    public SNFragmentScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindData(FragmentManager manager, ArrayList<Fragment> list) {
        fragments = list;
        //DEFAULT_OFFSCREEN_PAGES
        awesomeAdapter = new MyFragmentPagerAdapter(manager, fragments);
        setAdapter(new MyFragmentPagerAdapter(manager, list));
        setOffscreenPageLimit(list.size());
        setCurrentItem(0);
    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

}
