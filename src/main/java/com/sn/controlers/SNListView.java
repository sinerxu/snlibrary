package com.sn.controlers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by xuhui on 16/1/30.
 */
public class SNListView extends ListView {


    boolean scrollEnable = true;

    public SNListView(Context context) {
        super(context);
    }

    public SNListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SNListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (scrollEnable) return super.isEnabled();
        else return false;
    }

    public boolean isScrollEnable() {
        return scrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        //this.scrollEnable = scrollEnable;
    }
}
