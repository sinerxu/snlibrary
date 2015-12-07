package com.sn.controlers.slidingtab.underlinetab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.controlers.slidingtab.SNSlidingTabBar;
import com.sn.controlers.slidingtab.SNSlidingTabItemHover;
import com.sn.lib.R;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/8/12.
 */
public class SNUnderLineTabItemHover extends SNSlidingTabItemHover {
    SNElement $hover;
    boolean isLoad;

    public SNUnderLineTabItemHover(Context context, AttributeSet attrs) {
        super(context, attrs);
        $hover = $.layoutInflateResId(R.layout.controler_underline_slidetabitemhover, $this.toViewGroup(), false);
        $this.add($hover);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isLoad)
        {
            int uc = $this.parent().parent().parent().toView(SNSlidingTabBar.class).getUnderLineColor();
            if (uc != 0)
                $hover.backgroundColor(uc);
            isLoad = true;
        }
    }
}
