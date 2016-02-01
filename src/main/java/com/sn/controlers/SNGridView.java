package com.sn.controlers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by xuhui on 16/1/30.
 */
public class SNGridView extends GridView {
    boolean scrollEnable = true;

    public SNGridView(Context context) {
        super(context);
    }

    public SNGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SNGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        this.scrollEnable = scrollEnable;
    }
}
