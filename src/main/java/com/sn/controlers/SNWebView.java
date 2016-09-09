package com.sn.controlers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.andview.refreshview.XRefreshView;
import com.sn.controlers.pullrefresh.SNRefreshLayout;
import com.sn.lib.R;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 16/8/17.
 */

public class SNWebView extends SNRefreshLayout {

    SNElement $box;
    SNElement $wv;
    SNElement $progress;
    ValueAnimator animation;
    OnLoadFinishListener onLoadFinishListener;
    boolean mAllowRefresh = true;
    boolean mRefreshEnable = true;
    float mCurrY = 0;
    SNOnPullRefreshListener onPullRefreshListener;

    public void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener) {
        this.onLoadFinishListener = onLoadFinishListener;
    }

    /**
     * Constructs a new WebView with a Context object.
     *
     * @param context a Context object used to access application assets
     */
    public SNWebView(Context context) {
        super(context);
        initView();
    }

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public SNWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    void initView() {
        $box = $.layoutInflateName("controler_sn_webview");
        $this.add($box);
        $wv = $box.find(R.id.wvMain);
        $progress = $box.find(R.id.viewProgress);
        $wv.webAllowOpenUrlInApp();
        $wv.webResponsive();
        $wv.webChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (animation != null) animation.cancel();
                if (newProgress == 0) {
                    $progress.width($.px(0));
                    newProgress = 5;
                }
                float percent = (float) newProgress / 100.00f;
                int w = (int) ($.displaySize().getWidth() * percent);
                animation = ValueAnimator.ofInt($progress.width(), w);
                animation.setDuration(500);
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int v = (int) animation.getAnimatedValue();
                        $progress.width(v);
                        if (v == $.displaySize().getWidth()) {
                            $progress.width($.px(0));
                        }
                    }
                });
                animation.start();
                if (newProgress == 100) {
                    if (onLoadFinishListener != null)
                        onLoadFinishListener.onLoadFinish($wv);
                }
            }
        });
        $wv.toView(SNExWebView.class).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mRefreshEnable) return false;
                SNExWebView mWebView = (SNExWebView) $wv.toView(SNExWebView.class);
                if (mWebView.isScrollTop())
                    $.util.logDebug(SNWebView.class, "mWebView isScrollTop");
                else if (mWebView.isScrollBottom())
                    $.util.logDebug(SNWebView.class, "mWebView isScrollBottom");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mCurrY = event.getY();
                    $.util.logDebug(SNWebView.class, "OnTouch ACTION_DOWN");
                    //无论如何都允许下啦
                    disallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //屏蔽
                    $.util.logDebug(SNWebView.class, "OnTouch ACTION_MOVE==" + (event.getY() - mCurrY));
                    if (event.getY() - mCurrY > 0) {
                        //向上
                        if (mWebView.isScrollTop()) {
                            setAllowRefresh(true);
                        }

                    } else if (event.getY() - mCurrY < 0) {

                        //向下
                        if (mWebView.isScrollBottom()) {
                            setAllowRefresh(true);
                        }
                    }


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //屏蔽
                    $.util.logDebug(SNWebView.class, "OnTouch ACTION_UP");
                    //可以下啦
                    if (mWebView.isScrollTop() || mWebView.isScrollBottom()) {
                        //允许下啦
                        setAllowRefresh(true);
                    } else {
                        setAllowRefresh(false);
                    }

                }
                return false;
            }
        });


    }

    public SNElement getWebView() {
        return $wv;
    }

    public SNElement getProgress() {
        return $progress;
    }

    public void loadUrl(String url) {
        $progress.width(0);
        $wv.loadUrl(url);
    }

    public void loadHtml(String html) {
        $progress.width(0);
        $wv.loadHtml(html);
    }

    public void loadHtml(String baseUrl, String data,
                         String mimeType, String encoding, String historyUrl) {
        $progress.width(0);
        $wv.loadHtml(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public interface OnLoadFinishListener {
        void onLoadFinish(SNElement wv);
    }


    public void setRefreshEnable(boolean refreshEnable) {
        this.mRefreshEnable = refreshEnable;
        disallowInterceptTouchEvent(!refreshEnable);
        if (refreshEnable) {
            final SNWebView outView = this;
            outView.setPullLoadEnable(false);
            outView.setPinnedTime(200);
            outView.setOnLoadFinishListener(new SNWebView.OnLoadFinishListener() {
                @Override
                public void onLoadFinish(SNElement wv) {
                    outView.stopRefresh();
                }
            });
            outView.setPinnedContent(true);
            outView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                @Override
                public void onRefresh() {
                    if (onPullRefreshListener != null)
                        onPullRefreshListener.onRefresh();
                }

                @Override
                public void onLoadMore(boolean isSilence) {

                }
            });
        }
    }

    public void setAllowRefresh(boolean enable) {
        mAllowRefresh = enable;
        disallowInterceptTouchEvent(!enable);
    }

    public void setOnPullRefreshListener(SNOnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
    }

    public interface SNOnPullRefreshListener {
        void onRefresh();
    }
}
