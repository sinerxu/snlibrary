package com.sn.controlers;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.fragment.SNFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/8/16.
 */
public class SNFragmentScrollable extends SNViewPager {
    private MyFragmentPagerAdapter awesomeAdapter;
    public ArrayList<Fragment> fragments;

    public SNFragmentScrollable(Context context, FragmentManager manager, ArrayList<Fragment> list, int selectItem) {
        super(context);
        bindData(manager, list, selectItem);
    }

    public SNFragmentScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindData(FragmentManager manager, ArrayList<Fragment> list, int selectItem) {
        fragments = list;
        //DEFAULT_OFFSCREEN_PAGES
        awesomeAdapter = new MyFragmentPagerAdapter(manager, fragments);
        setAdapter(awesomeAdapter);
        setOffscreenPageLimit(list.size());
        setCurrentItem(selectItem);

    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        FragmentManager fm;
        List<Boolean> fragmentsUpdateFlag;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            fragmentsUpdateFlag = new ArrayList<Boolean>();
            for (Fragment f : list) {
                SNFragment fragment = (SNFragment) f;
                $.util.logInfo(SNFragmentScrollable.class, "fragment name=" + fragment.getName());
                fragmentsUpdateFlag.add(false);
            }
            this.fm = fm;
            this.fragments = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            SNFragment fragment = (SNFragment) super.instantiateItem(container,
//                    position);
//            String fragmentTag = fragment.getTag();
//            if (fragmentsUpdateFlag.get(position % fragmentsUpdateFlag.size())) {
//                //如果这个fragment需要更新
//                FragmentTransaction ft = fm.beginTransaction();
//                //移除旧的fragment
//                ft.remove(fragment);
//                //换成新的fragment
//                fragment = (SNFragment) fragments.get(position % fragments.size());
//                ft.add(container.getId(), fragment, fragmentTag);
//                ft.attach(fragment);
//                ft.commit();
//                //复位更新标志
//                fragmentsUpdateFlag.set(position % fragmentsUpdateFlag.size(), false);
//            }
//            fragment.setName(((SNFragment) getItem(position)).getName());
//            $.util.logInfo(SNFragmentScrollable.class, "fragment name=" + fragment.getName());
//            return fragment;
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
