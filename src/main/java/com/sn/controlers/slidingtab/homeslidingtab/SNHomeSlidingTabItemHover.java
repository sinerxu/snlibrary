package com.sn.controlers.slidingtab.homeslidingtab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;


import com.sn.controlers.slidingtab.SNSlidingTabItemHover;
import com.sn.lib.R;

/**
 * Created by xuhui on 15/8/12.
 */
public class SNHomeSlidingTabItemHover extends SNSlidingTabItemHover {
    public SNHomeSlidingTabItemHover(Context context, AttributeSet attrs) {
        super(context, attrs);

        $this = $.layoutInflateResId(R.layout.controler_home_slidetabitemhover, (ViewGroup) $this.toView());
    }
}
