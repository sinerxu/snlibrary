package com.sn.controlers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNLinearLayout extends LinearLayout {
    public SNManager $;
    public SNElement $this;

    public SNLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        $ = new SNManager(context);
        $this = $.create(this);
    }

    public SNLinearLayout(Context context) {
        super(context);
        $ = new SNManager(context);
        $this = $.create(this);
    }


}
