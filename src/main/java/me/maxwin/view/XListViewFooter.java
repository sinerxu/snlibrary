/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package me.maxwin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sn.main.SNManager;

public class XListViewFooter extends LinearLayout {
    SNManager $;
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    public final static int STATE_FINISH = 3;
    public final static int STATE_ERROR = 4;
    private int mState;
    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;


    private String mHintError;
    private String mHintReady;
    private String mHintFinish;
    private String mHintNormal;


    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        mState = state;
        mHintView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(mHintReady);
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else if (state == STATE_FINISH) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(mHintFinish);

        } else if (state == STATE_ERROR) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(mHintError);
        } else {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(mHintNormal);
        }
    }

    public int getState() {
        return mState;
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show hint message
     *
     * @param resid resid
     */
    public void showHintMessage(int resid) {
        if (resid > 0) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(resid);
        }

    }

    /**
     * show hint message
     *
     * @param msg string msg
     */
    public void showHintMessage(String msg) {
        if ($.util.strIsNotNullOrEmpty(msg)) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(msg);
        }

    }

    private void initView(Context context) {

        $ = new SNManager(context);
        mHintError = $.stringResId($.resourceString("xlistview_footer_hint_error"));
        mHintReady = $.stringResId($.resourceString("xlistview_footer_hint_ready"));
        mHintFinish = $.stringResId($.resourceString("xlistview_footer_hint_finish"));
        mHintNormal = $.stringResId($.resourceString("xlistview_footer_hint_normal"));
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate($.resourceLayout("xlistview_footer"), null);

        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById($
                .resourceId("xlistview_footer_content"));
        mProgressBar = moreView.findViewById($
                .resourceId("xlistview_footer_progressbar"));
        mHintView = (TextView) moreView.findViewById($
                .resourceId("xlistview_footer_hint_textview"));

    }

}
