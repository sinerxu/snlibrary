package com.wheel.base;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wheel.utils.SNWindowUtil;

/**
 * 自定义视图的基类
 *
 * @author wangzengyang@gmail.com
 * @since 2013-8-28
 */
abstract public class WeBaseView extends View {

    /**
     * 缩放比例:水平
     */
    public static float SCALE_RATIO_HORIZONTAL;
    /**
     * 缩放比例:垂直
     */
    public static float SCALE_RATIO_VERTICAL;
    /**
     * 缩放比例
     */
    public static float SCALE_RATIO;


    public WeBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public WeBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public WeBaseView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        resize();
        onInit(context);
    }

    protected void onInit(Context context) {
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void invisible() {
        setVisibility(View.INVISIBLE);
    }

    /**
     * 重新计算当前View的宽高、边距
     */
    public void resize() {

        SNWindowUtil.resize(this);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public void onDestroy() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void clear() {
    }

    public void reset() {
    }

    public void onShow() {

    }


}
