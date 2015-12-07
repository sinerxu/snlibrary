package com.sn.controlers.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;


import com.sn.controlers.SNLinearLayout;
import com.sn.controlers.slidingtab.homebottomtab.SNHomeBottomTabItem;
import com.sn.controlers.slidingtab.homeslidingtab.SNHomeSlidingTabItem;
import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.models.SNInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/8/23.
 */
public class SNSlidingTabBar extends SNLinearLayout {
    SNElement $tab;

    class SNSlidingTabBarInject extends SNInject {
        SNElement tabItemHover;
        SNElement tabItemBox;
        SNElement tabContainer;
    }

    SNSlidingTabBarInject inject = new SNSlidingTabBarInject();
    FragmentManager fragmentManager;
    List<SNElement> items;
    ArrayList<Fragment> fragments;
    int style = 0;



    int underLineColor;
    public int getUnderLineColor() {
        return underLineColor;
    }
    public SNSlidingTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        int childCount = getChildCount();

        TypedArray a = $.obtainStyledAttr(attrs, R.styleable.SNSlidingTabBar);

        style = a.getInt(R.styleable.SNSlidingTabBar_style, 0);
        underLineColor = a.getColor(R.styleable.SNSlidingTabBar_underline_color, 0);
        a.recycle();
        fragmentManager = $.supportFragmentManager();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (items == null) {
            if (fragmentManager == null)
                throw new IllegalStateException("Must be set fragment manager.");
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
            } else if (style == 2) {
                tab_layout = R.layout.controler_underline_slidingtabbar;
            }else if (style == 3) {
                tab_layout = R.layout.controler_block_slidingtabbar;
            }
            $tab = $.layoutInflateResId(tab_layout, this, false, inject);

            $this.add($tab);
            //移除
            inject.tabItemBox.remove(inject.tabItemHover);
            //添加子项
            fragments = new ArrayList<Fragment>();
            for (SNElement item : items) {
                inject.tabItemBox.add(item);
                String fragmentName = item.toView(SNSlidingTabItem.class).getFragmentName();
                String all_f_name = "";
                if (fragmentName.contains($.packageName())) {
                    all_f_name = fragmentName;
                } else {
                    if (fragmentName.contains(".")) {
                        all_f_name = $.packageName() + fragmentName;
                    } else {
                        all_f_name = $.packageName() + ".app.controllers.fragments." + fragmentName;
                    }
                }
                Fragment fragment = null;
                try {
                    fragment = $.util.refInstanceObject(Fragment.class, all_f_name);
                    if (fragment == null)
                        throw new IllegalStateException($.util.strFormat("The {0} instance is error.", all_f_name));
                } catch (Exception ex) {
                    throw new IllegalStateException($.util.strFormat("The {0} instance is error.", all_f_name));
                }
                fragments.add(fragment);
            }
            inject.tabContainer.toView(SNSlidingTabContainer.class).bindData(fragmentManager, fragments);
            inject.tabItemBox.add(inject.tabItemHover);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setFragmentManager(FragmentManager _fragmentManager) {
        this.fragmentManager = _fragmentManager;
    }
}
