package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.sn.lib.R;
import com.sn.util.SNUtility;

/**
 * 扩展百分比布局
 * @attr app:percent_width="80%"            宽度百分比
 * @attr app:percent_height="80%"           高度百分比
 * Created by xuhui on 15/8/7.
 */
public class SNPercentRelativeLayout extends SNRelativeLayout {
    String LCAT = "SNPercentRelativeLayout Log";

    double rWidth;
    double rHeight;


    public SNPercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = $.loadStyle(attrs, R.styleable.SNPercentLayout);

        String pWidth = typedArray.getString(R.styleable.SNPercentLayout_percent_width);

        if (!$.util.strIsNullOrEmpty(pWidth)) {
            pWidth = pWidth.replace("%", "");
            try {
                rWidth = Double.parseDouble(pWidth) * 0.01;
            } catch (Exception ex) {
                throw new IllegalArgumentException("percent width must be a percent,such 20.3%");
            }

        }

        String pHeight = typedArray.getString(R.styleable.SNPercentLayout_percent_height);
        if (!$.util.strIsNullOrEmpty(pHeight)) {
            pHeight = pHeight.replace("%", "");
            try {
                rHeight = Double.parseDouble(pHeight) * 0.01;
            } catch (Exception ex) {
                throw new IllegalArgumentException("percent height must be a percent,such 20.3%");
            }
        }

        typedArray.recycle();
        Log.e(LCAT, $.util.strFormat("width:{0},height:{1}", rHeight, rWidth));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (rWidth > 0)
            $this.width((int) ($this.parent().width() * rWidth));
        if (rHeight > 0)
            $this.height((int) ($this.parent().height() * rHeight));
    }
}
