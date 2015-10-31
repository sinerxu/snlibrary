package com.sn.controlers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNViewPager extends ViewPager {
    public SNManager $;
    public SNElement $this;

    public SNViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        $ = new SNManager(context);
        $this = $.create(this);
    }

    public SNViewPager(Context context) {
        super(context);
        $ = new SNManager(context);
        $this = $.create(this);
    }
}
