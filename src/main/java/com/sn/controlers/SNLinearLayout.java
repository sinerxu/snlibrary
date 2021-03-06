package com.sn.controlers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.widget.LinearLayout;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNLinearLayout extends LinearLayout {
    public SNManager $;
    public SNElement $this;
    boolean isInit = false;

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

    protected void onInit() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            isInit = true;
            onInit();
        }
    }
}
