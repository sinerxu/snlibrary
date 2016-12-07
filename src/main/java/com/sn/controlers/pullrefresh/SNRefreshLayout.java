package com.sn.controlers.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.andview.refreshview.XRefreshView;
import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 16/9/2.
 */
public class SNRefreshLayout extends XRefreshView {


    public SNManager $;
    public SNElement $this;

    public SNRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public SNRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        $ = new SNManager(context);
        $this = $.create(this);
    }
}
