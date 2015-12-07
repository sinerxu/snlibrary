package com.sn.controlers.slidingtab.underlinetab;

import android.content.Context;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTabItemBox;

/**
 * @style style="?attr/theme_home_slidingtabitembox_style"  需要添加的的样式
 * Created by xuhui on 15/8/12.
 */
public class SNUnderLineTabItemBox extends SNSlidingTabItemBox {
    public SNUnderLineTabItemBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onItemLoadFinish() {
        super.onItemLoadFinish();

    }

}
