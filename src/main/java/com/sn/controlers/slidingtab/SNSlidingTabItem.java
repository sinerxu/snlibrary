package com.sn.controlers.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.sn.controlers.SNRelativeLayout;
import com.sn.lib.R;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNSlidingTabItem extends SNRelativeLayout {

    String fragmentName;


    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }


    public SNSlidingTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = $.loadStyle(attrs, R.styleable.SNSlidingTabItem);
        fragmentName = array.getString(R.styleable.SNSlidingTabItem_fragment);
        array.recycle();
    }
}
