package com.sn.controlers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by xuhui on 16/9/2.
 */
public class SNExWebView extends WebView {

    public interface SNOnScrollChangeListener {
        void onScrollChanged(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    SNOnScrollChangeListener onScrollChangeListener;


    public void setOnScrollChangeListener(SNOnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public SNExWebView(Context context) {
        super(context);
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public SNExWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public SNExWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null)
            onScrollChangeListener.onScrollChanged(this, l, t, oldl, oldt);
    }


    public boolean isScrollBottom() {
        return getScrollY() + getHeight() == (getContentHeight() * getScale());
    }

    public boolean isScrollTop() {
        return getScrollY() == 0;
    }
}
