package com.sn.controlers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNRelativeLayout extends RelativeLayout {
    public SNManager $;
    public SNElement $this;

    public SNRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        $ = new SNManager(context);
        $this = $.create(this);
    }
}
