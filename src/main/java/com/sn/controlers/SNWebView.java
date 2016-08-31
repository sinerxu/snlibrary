package com.sn.controlers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sn.lib.R;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 16/8/17.
 */

public class SNWebView extends SNRelativeLayout {

    SNElement $box;
    SNElement $wv;
    SNElement $progress;
    ValueAnimator animation;
    OnLoadFinishListener onLoadFinishListener;

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

    /**
     * Constructs a new WebView with layout parameters and a default style.
     *
     * @param context      a Context object used to access application assets
     * @param attrs        an AttributeSet passed to our parent
     * @param defStyleAttr an attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     */
    public SNWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
}
