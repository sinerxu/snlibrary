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
import com.sn.controlers.slidingtab.listeners.SNSlidingTabListener;
import com.sn.fragment.SNFragment;
import com.sn.lib.R;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/8/23.
 */
public class SNSlidingTabBar extends SNLinearLayout {
    SNElement $tab;
    SNElement tabItemHover;
    SNElement tabItemBox;
    SNElement tabContainer;


    SNSlidingTabListener slidingTabBarListener;
    FragmentManager fragmentManager;
    List<SNElement> items;
    List<Fragment> fragments;
    int style = 0;
    int underLineColor;
    int selectedItem = 0;
    String parentFragment;

    public int getUnderLineColor() {
        return underLineColor;
    }

    public SNSlidingTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        int childCount = getChildCount();
        TypedArray a = $.obtainStyledAttr(attrs, R.styleable.SNSlidingTabBar);
        style = a.getInt(R.styleable.SNSlidingTabBar_style, 0);
        underLineColor = a.getColor(R.styleable.SNSlidingTabBar_underline_color, 0);
        selectedItem = a.getInt(R.styleable.SNSlidingTabBar_selected_index, 0);
        parentFragment = a.getString(R.styleable.SNSlidingTabBar_parent_fragment);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    void initFm() {
        boolean isLoadActivityFM = true;
        if (parentFragment != null && $.supportFragmentManager().getFragments() != null) {
            List<Fragment> fragments = $.supportFragmentManager().getFragments();
            for (Fragment item : fragments) {
                if (item instanceof SNFragment) {
                    SNFragment sf = (SNFragment) item;
                    if (sf.getName() != null) {
                        $.util.logInfo(SNSlidingTabBar.class, "supportFragmentManager activity == " + sf.getName());
                        if (sf.getName().equals(parentFragment)) {
                            setFragmentManager(sf.getChildFragmentManager());
                            isLoadActivityFM = false;
                        }
                    }
                }
            }
        }
        if (isLoadActivityFM)
            setFragmentManager($.supportFragmentManager());
    }

    public void initView() {
        if (items == null) {
            initFm();
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
            } else if (style == 3) {
                tab_layout = R.layout.controler_block_slidingtabbar;
            } else if (style == 1000) {
                tab_layout = onTabLayout();
            }
            $tab = $.layoutInflateResId(tab_layout, this, false);

            tabItemHover = $tab.create(R.id.tabItemHover);

            tabItemBox = $tab.create(R.id.tabItemBox);

            tabContainer = $tab.create(R.id.tabContainer);

            $this.add($tab);
            if (this.slidingTabBarListener != null) setTabListener(this.slidingTabBarListener);
            //移除
            tabItemBox.remove(tabItemHover);

            //添加子项
            fragments = new ArrayList<Fragment>();
            for (SNElement item : items) {
                tabItemBox.add(item);
                String fragmentName = item.toView(SNSlidingTabItem.class).getFragmentName();
                String all_f_name = "";
                if (fragmentName.contains($.packageName())) {
                    all_f_name = fragmentName;
                } else {
                    if (fragmentName.contains(".")) {
                        all_f_name = $.packageName() + fragmentName;
                    } else {
                        all_f_name = $.packageName() + ".controllers.fragments." + fragmentName;
                    }
                }
                SNFragment fragment = null;
                try {
                    fragment = $.util.refInstanceObject(SNFragment.class, all_f_name);
                    if (fragment == null)
                        throw new IllegalStateException($.util.strFormat("The {0} instance is error.", all_f_name));
                } catch (Exception ex) {
                    throw new IllegalStateException($.util.strFormat("The {0} instance is error.", all_f_name));
                }
                fragment.setName(fragmentName);
                fragments.add(fragment);
            }
            $.util.logInfo(SNSlidingTabBar.class, "selected_index=" + selectedItem);
            tabContainer.toView(SNSlidingTabContainer.class).bindData(fragmentManager, fragments, selectedItem);
            tabItemBox.add(tabItemHover);
        }
        tabItemBox.toView(SNSlidingTabItemBox.class).initChild();
        $tab.toView(SNSlidingTab.class).initChild();
        if (slidingTabBarListener != null)
            slidingTabBarListener.onInitFinish();
    }

    public int onTabLayout() {
        return 0;
    }

    public Fragment getContentItem(int i) {
        if (fragments != null && i < fragments.size()) {
            return fragments.get(i);
        } else {
            return null;
        }
    }

    public <T> T getContentItem(Class<T> _class, int i) {
        Fragment fragment = getContentItem(i);
        if (fragment != null)
            return (T) getContentItem(i);
        else return null;
    }

    public void setCurrentItem(int selectedItem) {
        this.selectedItem = selectedItem;
        $tab.toView(SNSlidingTab.class).setCurrentPage(selectedItem);
    }

    public void setCurrentItem(int selectedItem, boolean animated) {
        this.selectedItem = selectedItem;
        $tab.toView(SNSlidingTab.class).setCurrentPage(selectedItem, animated);
    }

    public void setFragmentManager(FragmentManager _fragmentManager) {
        this.fragmentManager = _fragmentManager;
    }

    public void updateTabItemSize() {
        tabItemBox.toView(SNSlidingTabItemBox.class).updateSize();
    }

    public void setTabListener(SNSlidingTabListener slidingTabBarListener) {
        this.slidingTabBarListener = slidingTabBarListener;
        if ($tab != null)
            $tab.toView(SNSlidingTab.class).setTabListener(slidingTabBarListener);
    }
}
