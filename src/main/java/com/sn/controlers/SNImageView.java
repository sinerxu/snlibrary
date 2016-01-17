package com.sn.controlers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/12/31.
 */
public class SNImageView extends ImageView {

    public SNManager $;
    public SNElement $this;
    boolean isInit = false;


    public SNImageView(Context context) {
        super(context);
    }

    public SNImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SNImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
