package com.sn.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.sn.controlers.SNNavTitleBar;
import com.sn.lib.R;
import com.sn.main.SNConfig;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/10/24.
 */
public class SNNavigationSlidingActivity extends SlidingActivity {
    public final String LCAP = this.getClass().getName() + " Log";
    public SNElement container;
    public SNElement mainContainer;
    SNElement navTitleBarContainer;
    SNElement topAdContainer;
    public SNElement navTitleBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int id) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.setContentView(R.layout.activity_navigation_sliding);
        //首先设置主题
        initBase();
        $.layoutInflateResId(id, container.toView(ViewGroup.class));
        Log.d(LCAP, "看看到底执行多少遍？");

    }

    void initBase() {
        container = $.create(R.id.container);
        $.slidingMode(SlidingMenu.LEFT_RIGHT_NONE);
        navTitleBarContainer = $.create(R.id.navTitleBarContainer);
        mainContainer = $.create(R.id.mainContainer);
        topAdContainer = $.create(R.id.topAdContainer);
    }

    /**
     * load left view
     *
     * @param left_id            left layout id
     * @param offset_id          offset id
     * @param shadow_width_id    shadow width id
     * @param shadow_drawable_id shadow drawable id
     * @param fade               fade value
     */
    public void loadLeftResId(int left_id, int offset_id, int shadow_width_id, int shadow_drawable_id, float fade) {

        $.slidingMode(SlidingMenu.LEFT);

        $.slidingLeftView(left_id);

        //$.slidingTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 偏移宽度
        $.slidingOffsetRes(offset_id);

        // 阴影宽度
        $.slidingShadowWidthRes(shadow_width_id);

        // 阴影样式
        $.slidingLeftShadow(shadow_drawable_id);

        $.slidingFade(fade);
    }

    /**
     * load left view
     *
     * @param left_id            left layout id
     * @param offset_value       offset value
     * @param shadow_width_value shadow width value
     * @param shadow_drawable_id shadow drawable style
     * @param fade               fade value
     */
    public void loadLeft(int left_id, int offset_value, int shadow_width_value, int shadow_drawable_id, float fade) {

        $.slidingMode(SlidingMenu.LEFT);

        $.slidingLeftView(left_id);

        //$.slidingTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 偏移宽度
        $.slidingOffset(offset_value);

        // 阴影宽度
        $.slidingShadowWidth(shadow_width_value);

        // 阴影样式
        $.slidingLeftShadow(shadow_drawable_id);

        $.slidingFade(fade);
    }

    /**
     * 加载导航条
     *
     * @param height              导航条高度
     * @param background_color_id 导航条颜色资源id
     */
    public void loadNavBar(int height, int background_color_id) {
        navTitleBar = $.create(new SNNavTitleBar(this));
        navTitleBarContainer.add(navTitleBar);
        //navTitleBar.backgroundColorResId(background_color_id);
        navTitleBar.background(background_color_id);
        navTitleBar.width(SNConfig.SN_UI_FILL);
        navTitleBar.height(height);
    }

    /**
     * 加载导航条
     *
     * @param height_id     导航条高度资源id
     * @param background_id 导航条颜色资源id
     */
    public void loadNavBarResId(int height_id, int background_id) {
        int height = $.resources().getDimensionPixelSize(height_id);
        loadNavBar(height, background_id);
    }

    public SNElement getAdContainer() {
        return topAdContainer;
    }
}
