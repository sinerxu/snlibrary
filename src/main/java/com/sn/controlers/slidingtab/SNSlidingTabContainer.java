package com.sn.controlers.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sn.controlers.SNFragmentScrollable;
import com.sn.controlers.slidingtab.listeners.SNSlidingTabListener;
import com.sn.lib.R;

import java.util.ArrayList;

/**
 * @attr android:isScrollContainer  是否支持左右滑动
 * <p/>
 * Created by xuhui on 15/8/11.
 */
public class SNSlidingTabContainer extends SNFragmentScrollable {
    String LCAP = "SNSlidingTabContainer Log";
    boolean isScrollContainer;


    public SNSlidingTabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = $.obtainStyledAttr(attrs, R.styleable.SNSlidingTabContainer);
        isScrollContainer = a.getBoolean(R.styleable.SNSlidingTabContainer_android_isScrollContainer, true);
        a.recycle();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isScrollContainer)
            return false;
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isScrollContainer)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    public void setIsScrollContainer(boolean isScrollContainer) {
        this.isScrollContainer = isScrollContainer;
    }
}
