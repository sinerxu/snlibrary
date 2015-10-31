package com.sn.controlers.slidingtab.homebottomtab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.sn.lib.R;
import com.sn.controlers.slidingtab.SNSlidingTabItemHover;

/**
 * @style   style="?attr/theme_home_bottomtabitem_hover_style"    需要添加的的样式
 */
public class SNHomeBottomTabItemHover extends SNSlidingTabItemHover {
    public SNHomeBottomTabItemHover(Context context, AttributeSet attrs) {
        super(context, attrs);

        $this = $.layoutInflateResId(R.layout.controler_home_bottomtabitemhover, (ViewGroup) $this.toView());
    }
}
