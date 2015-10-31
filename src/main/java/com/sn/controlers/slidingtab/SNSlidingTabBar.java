package com.sn.controlers.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;


import com.sn.controlers.SNLinearLayout;
import com.sn.controlers.slidingtab.homebottomtab.SNHomeBottomTabItem;
import com.sn.controlers.slidingtab.homeslidingtab.SNHomeSlidingTabItem;
import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.util.SNUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/8/23.
 */
public class SNSlidingTabBar extends SNLinearLayout {
    SNElement $tab;

    class ViewHolder {
        SNElement tabItemHover;
        SNElement tabItemBox;
        SNElement tabContainer;
    }

    ViewHolder holder = new ViewHolder();
    FragmentManager fragmentManager;
    List<SNElement> items;
    ArrayList<Fragment> fragments;
    int style = 0;

    public SNSlidingTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        int childCount = getChildCount();

        TypedArray a = $.loadStyle(attrs, R.styleable.SNSlidingTabBar);

        style = a.getInt(R.styleable.SNSlidingTabBar_style, 0);
        a.recycle();
        fragmentManager = $.supportFragmentManager();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (items == null) {
            if (fragmentManager == null)
                new IllegalStateException("Must be set fragment manager.");
            items = new ArrayList<SNElement>();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View item = getChildAt(i);
                if (item instanceof SNHomeBottomTabItem) style = 0;
                else if (item instanceof SNHomeSlidingTabItem) style = 1;
                items.add($.create(item));
            }
            //移除所有的view
            removeAllViews();
            int tab_layout = R.layout.controler_home_bottomtabbar;
            if (style == 1) {
                tab_layout = R.layout.controler_home_slidingtabbar;
            }
            $tab = $.layoutInflateResId(tab_layout, this, holder);

            //移除
            holder.tabItemBox.remove(holder.tabItemHover);
            //添加子项
            fragments = new ArrayList<Fragment>();
            for (SNElement item : items) {
                holder.tabItemBox.add(item);
                String fragmentName = item.toView(SNSlidingTabItem.class).getFragmentName();
                String all_f_name = $.packageName() + ".fragments." + fragmentName;
                Fragment fragment = null;
                try {
                    fragment = SNUtility.instanceObject(Fragment.class, all_f_name);
                } catch (Exception ex) {
                    new IllegalStateException(SNUtility.format("The {0} instance is error.", all_f_name));
                }
                fragments.add(fragment);
            }
            holder.tabContainer.toView(SNSlidingTabContainer.class).bindData(fragmentManager, fragments);
            holder.tabItemBox.add(holder.tabItemHover);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setFragmentManager(FragmentManager _fragmentManager) {
        this.fragmentManager = _fragmentManager;
    }
}
