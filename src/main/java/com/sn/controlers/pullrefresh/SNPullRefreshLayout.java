package com.sn.controlers.pullrefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.main.SNManager;


public class SNPullRefreshLayout extends LinearLayout {

    public interface SNPullRefreshListener {
        void onRefresh(SNPullRefreshLayout pullRefreshLayout);

        void onLoadMore(SNPullRefreshLayout pullRefreshLayout);
    }

    SNPullRefreshListener pullRefreshListener;

    public void setPullRefreshListener(SNPullRefreshListener pullRefreshListener) {
        this.pullRefreshListener = pullRefreshListener;
    }

    SNPullRefreshLayout self;
    SNElement $this;
    SNElement plHeader, plFooter;
    SNElement content;
    SNElement plArrow;
    SNElement plStateName, plFooterStateName;
    SNElement plProgressbar, plFooterProgressbar;
    private static final int BACK_SPEED = 500;
    //拉动阻力
    private static final int PULL_OBSTRUCTION = 3;
    private static final int ROTATE_ANIM_DURATION = 180;
    private static final int MAX_HEADER_HEIGHT = 60;
    private static final int MAX_FOOTER_HEIGHT = 50;
    private static final int SCROLL_STATE_NORMAL = 0;
    private static final int SCROLL_STATE_BACK_SCROLLING = 1;
    private static final int SCROLL_STATE_PULL_SCROLLING = 2;

    public static final int REFRESH_STATE_NORMAL = 0;
    public static final int REFRESH_STATE_READY = 1;
    public static final int REFRESH_STATE_LOADING = 2;


    public static final int LOAD_STATE_NORMAL = 0;
    public static final int LOAD_STATE_READY = 1;
    public static final int LOAD_STATE_LOADING = 2;
    public static final int LOAD_STATE_ERROR = 3;
    public static final int LOAD_STATE_DONE = 4;


    private int mBeginHeaderMarginTop = 0;
    private int mBeginFooterMarginTop = 0;
    private int mBeginContentMarginBottom = 0;
    private int mBeginContentMarginTop = 0;
    private int mBeginY = 0;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    boolean refreshEnable = false;
    boolean loadMoreEnable = false;
    boolean loadMoreDone = false;
    SNManager $;

    private int mScrollState = SCROLL_STATE_NORMAL;

    private int mRefreshState = REFRESH_STATE_NORMAL;

    private int mLoadState = LOAD_STATE_NORMAL;

    public SNPullRefreshLayout(Context context) {
        super(context);
        initWithContext(context);
    }

    public SNPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public SNPullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        plHeader = $.layoutInflateResId(R.layout.pull_refresh_header, self, false);
        $this.add(plHeader, 0);
        plFooter = $.layoutInflateResId(R.layout.pull_refresh_footer, self, false);
        $this.add(plFooter, 2);
        plHeader.marginTop(-$.px(MAX_HEADER_HEIGHT));
        content = $.create(getChildAt(1));
        plArrow = plHeader.find(R.id.plArrow);
        plStateName = plHeader.find(R.id.plStateName);
        plProgressbar = plHeader.find(R.id.plProgressbar);
        plFooterProgressbar = plFooter.find(R.id.plFooterProgressbar);
        plFooterStateName = plFooter.find(R.id.plFooterStateName);
        setLoadMoreEnable(loadMoreEnable);

        content.toView().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (mRefreshState == REFRESH_STATE_LOADING || mLoadState == LOAD_STATE_LOADING) {
                    $this.toViewGroup().setEnabled(false);
                    return true;
                }
                $this.toViewGroup().setEnabled(true);


                if (refreshEnable && mRefreshState == REFRESH_STATE_LOADING) {
                    if (content.toView() instanceof AbsListView)
                        content.toView(AbsListView.class).smoothScrollToPosition(0);
                    return false;
                }
                if (loadMoreEnable && mLoadState == LOAD_STATE_LOADING) {
                    if (content.toView() instanceof AbsListView)
                        content.toView(AbsListView.class).smoothScrollToPosition(content.toView(AbsListView.class).getLastVisiblePosition());
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        int interval = ((int) motionEvent.getY() - mBeginY) / PULL_OBSTRUCTION;
                        if (refreshEnable && getRealScrollY() == 0) {
                            if (interval > 0 || mScrollState == SCROLL_STATE_PULL_SCROLLING) {
                                int maxheader = $.px(MAX_HEADER_HEIGHT);
                                int currTop = plHeader.margins().getTop();
                                int offsetTop = currTop - mBeginHeaderMarginTop;
                                int finalTop = currTop + interval;
                                if (finalTop <= mBeginHeaderMarginTop)
                                    finalTop = mBeginHeaderMarginTop;
                                if (offsetTop <= maxheader) {
                                    setRefreshState(REFRESH_STATE_NORMAL);
                                } else {
                                    setRefreshState(REFRESH_STATE_READY);
                                }
                                mScrollState = SCROLL_STATE_PULL_SCROLLING;
                                plHeader.marginTop(finalTop);
                                if (content.toView() instanceof AbsListView)
                                    content.toView(AbsListView.class).smoothScrollToPosition(0);
                                return false;
                            }
                        } else if (loadMoreEnable) {
                            if (content.toView() instanceof AbsListView) {
                                AbsListView listView = content.toView(AbsListView.class);
                                if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1) {
                                    if (interval < 0 || mScrollState == SCROLL_STATE_PULL_SCROLLING) {
                                        int currTop = plFooter.margins().getTop();
                                        int finalv = currTop + interval;
                                        int maxfooter = $.px(MAX_FOOTER_HEIGHT);
                                        int offsetTop = Math.abs(currTop - mBeginFooterMarginTop);
                                        $.util.logInfo(SNPullRefreshLayout.class, $.util.strFormat("currTop={0},mBeginFooterMarginTop={1},offsetTop={2}", currTop, mBeginFooterMarginTop, offsetTop));
                                        if (!loadMoreDone) {
                                            if (offsetTop <= maxfooter) {
                                                setLoadState(LOAD_STATE_NORMAL);
                                            } else {
                                                setLoadState(LOAD_STATE_READY);
                                            }
                                        }
                                        content.marginTop(finalv);
                                        content.marginBottom(-finalv);
                                        plFooter.marginTop(finalv);
                                        mScrollState = SCROLL_STATE_PULL_SCROLLING;
                                        if (content.toView() instanceof AbsListView)
                                            content.toView(AbsListView.class).smoothScrollToPosition(content.toView(AbsListView.class).getLastVisiblePosition());
                                        return false;
                                    }
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mBeginY = (int) motionEvent.getY();
                        if (mBeginHeaderMarginTop == 0)
                            mBeginHeaderMarginTop = plHeader.margins().getTop();
                        if (mBeginFooterMarginTop == 0)
                            mBeginFooterMarginTop = plFooter.margins().getTop();
                        if (mBeginContentMarginBottom == 0)
                            mBeginContentMarginBottom = content.margins().getBottom();
                        if (mBeginContentMarginTop == 0)
                            mBeginContentMarginTop = content.margins().getTop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (refreshEnable && getRealScrollY() == 0) {
                            int currTop = plHeader.margins().getTop();
                            if (currTop != mBeginHeaderMarginTop)
                                mScrollState = SCROLL_STATE_BACK_SCROLLING;
                            else {
                                return false;
                            }
                            //进入刷新状态
                            setRefreshState(REFRESH_STATE_LOADING);
                            int bt = mBeginHeaderMarginTop;
                            if (mRefreshState == REFRESH_STATE_LOADING)
                                bt = mBeginHeaderMarginTop + $.px(MAX_HEADER_HEIGHT);
                            final int fbt = bt;
                            ValueAnimator valueAnimator = ValueAnimator.ofInt(currTop, bt);
                            valueAnimator.setDuration(BACK_SPEED);
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    if (mScrollState != SCROLL_STATE_PULL_SCROLLING && !((mRefreshState == REFRESH_STATE_NORMAL) && fbt != mBeginHeaderMarginTop)) {
                                        if (content.toView() instanceof AbsListView)
                                            content.toView(AbsListView.class).smoothScrollToPosition(0);
                                        int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                                        plHeader.marginTop(v);
                                        mScrollState = SCROLL_STATE_BACK_SCROLLING;
                                        if (v == mBeginHeaderMarginTop) {
                                            mScrollState = SCROLL_STATE_NORMAL;
                                        }
                                    } else {
                                        valueAnimator.cancel();
                                        plArrow.toView().clearAnimation();
                                    }
                                }
                            });
                            valueAnimator.start();
                        } else {
                            if (loadMoreEnable) {
                                //region ContentTop
                                int currContentTop = content.margins().getTop();
                                if (currContentTop != mBeginContentMarginTop)
                                    mScrollState = SCROLL_STATE_BACK_SCROLLING;
                                else {

                                    return false;
                                }

                                int bct = mBeginContentMarginTop;
                                setLoadState(LOAD_STATE_LOADING);
                                if (mLoadState == LOAD_STATE_LOADING)
                                    bct = -(mBeginContentMarginTop + $.px(MAX_FOOTER_HEIGHT));
                                ValueAnimator valueContentTopAnimator = ValueAnimator.ofInt(currContentTop, bct);
                                valueContentTopAnimator.setDuration(BACK_SPEED);
                                valueContentTopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        if (mScrollState != SCROLL_STATE_PULL_SCROLLING) {
                                            if (content.toView() instanceof AbsListView)
                                                content.toView(AbsListView.class).smoothScrollToPosition(content.toView(AbsListView.class).getLastVisiblePosition());
                                            int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                                            content.marginTop(v);
                                            if (v == mBeginHeaderMarginTop) {

                                                content.scrollEnable(true);
                                                mScrollState = SCROLL_STATE_NORMAL;
                                            }
                                        } else {
                                            valueAnimator.cancel();
                                            plArrow.toView().clearAnimation();
                                        }
                                    }
                                });
                                valueContentTopAnimator.start();
                                //endregion
                                //region ContentBottom
                                int currContentBottom = content.margins().getBottom();
                                int bcb = mBeginContentMarginBottom;
                                if (mLoadState == LOAD_STATE_LOADING)
                                    bcb = mBeginContentMarginBottom + $.px(MAX_FOOTER_HEIGHT);
                                ValueAnimator valueContentBottomAnimator = ValueAnimator.ofInt(currContentBottom, bcb);
                                valueContentBottomAnimator.setDuration(BACK_SPEED);
                                valueContentBottomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        if (mScrollState != SCROLL_STATE_PULL_SCROLLING) {
                                            int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                                            content.marginBottom(v);
                                            if (v == mBeginHeaderMarginTop) {
                                                mScrollState = SCROLL_STATE_NORMAL;
                                                content.scrollEnable(true);
                                            }

                                        } else {
                                            valueAnimator.cancel();
                                            plArrow.toView().clearAnimation();
                                        }
                                    }
                                });
                                valueContentBottomAnimator.start();
                                //endregion
                                //region FooterTop
                                int currFooterTop = content.margins().getTop();

                                int bft = mBeginFooterMarginTop;
                                if (mLoadState == LOAD_STATE_LOADING)
                                    bft = -(mBeginFooterMarginTop + $.px(MAX_FOOTER_HEIGHT));

                                ValueAnimator valueFooterTopAnimator = ValueAnimator.ofInt(currFooterTop, bft);
                                valueFooterTopAnimator.setDuration(BACK_SPEED);
                                valueFooterTopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        if (mScrollState != SCROLL_STATE_PULL_SCROLLING) {
                                            int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                                            plFooter.marginTop(v);
                                            if (v == mBeginHeaderMarginTop)
                                                mScrollState = SCROLL_STATE_NORMAL;
                                        } else {
                                            valueAnimator.cancel();
                                            plArrow.toView().clearAnimation();
                                        }
                                    }
                                });
                                valueFooterTopAnimator.start();
                                //endregion
                            }

                        }

                        break;
                }

                return false;
            }
        });


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        $.util.logInfo(SNPullRefreshLayout.class, $.util.strFormat("{0},{1},{2},{3}", l, t, oldl, oldt));
    }

    private void initWithContext(Context context) {
        self = this;
        $ = new SNManager(context);
        $this = $.create(this);
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setRefreshState(int state) {
        if (mRefreshState == state)
            return;
        switch (state) {
            case REFRESH_STATE_READY:
                if (mRefreshState == REFRESH_STATE_NORMAL) {
                    //进入准备状态
                    plStateName.text("松开刷新数据");
                    plArrow.toView().startAnimation(mRotateUpAnim);
                } else return;
                break;
            case REFRESH_STATE_NORMAL:
                if (mRefreshState == REFRESH_STATE_READY) {
                    plStateName.text("下啦刷新");
                    //还原状态
                    plArrow.toView().clearAnimation();
                    plArrow.toView().startAnimation(mRotateDownAnim);
                } else if (mRefreshState == REFRESH_STATE_LOADING) {
                    int currTop = plHeader.margins().getTop();
                    int bt = mBeginHeaderMarginTop;
                    plStateName.text("数据加载完成");
                    plProgressbar.visible($.SN_UI_NONE);
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(currTop, bt);
                    valueAnimator.setDuration(BACK_SPEED);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                            plHeader.marginTop(v);
                            mScrollState = SCROLL_STATE_BACK_SCROLLING;
                            if (v == mBeginHeaderMarginTop) {
                                mScrollState = SCROLL_STATE_NORMAL;
                                plStateName.text("下啦刷新");
                                mRefreshState = REFRESH_STATE_NORMAL;
                                plArrow.visible($.SN_UI_VISIBLE);
                                content.scrollEnable(true);
                            }
                        }
                    });
                    valueAnimator.start();
                    return;
                } else return;
                break;
            case REFRESH_STATE_LOADING:
                if (mRefreshState == REFRESH_STATE_READY) {
                    //开始进入刷新状态
                    setLoadMoreDone(false);
                    plStateName.text("正在刷新数据");
                    if (pullRefreshListener != null)
                        pullRefreshListener.onRefresh(self);
                } else return;
                break;
        }
        mRefreshState = state;
        if (state == REFRESH_STATE_LOADING) {
            plArrow.toView().clearAnimation();
            plArrow.visible($.SN_UI_NONE);
            plProgressbar.visible($.SN_UI_VISIBLE);
        } else {
            plArrow.visible($.SN_UI_VISIBLE);
            plProgressbar.visible($.SN_UI_NONE);
        }
    }

    public void setLoadState(int state) {
        setLoadState(state, "");
    }

    public void setLoadState(int state, String msg) {
        if (mLoadState == state)
            return;
        switch (state) {
            case LOAD_STATE_ERROR:
            case LOAD_STATE_DONE:
                restoreFooter(state, msg);
                return;

            case LOAD_STATE_NORMAL:
                //正常状态
                if (mLoadState == LOAD_STATE_READY) {
                    //进入准备状态
                    plFooterStateName.text("加载更多");
                } else if (mLoadState == LOAD_STATE_LOADING) {
                    //状态是loading的时候，设置成normal
                    restoreFooter(state, msg);
                    return;
                } else return;
                break;
            case LOAD_STATE_READY:
                if (mLoadState == LOAD_STATE_NORMAL) {
                    //进入准备状态
                    plFooterStateName.text("松开加载更多");
                } else return;
                break;
            case LOAD_STATE_LOADING:
                if (mLoadState == LOAD_STATE_READY) {
                    //进入准备状态
                    plFooterStateName.text("正在加载数据");
                    if (pullRefreshListener != null)
                        pullRefreshListener.onLoadMore(self);
                } else return;
                break;
        }
        mLoadState = state;
    }


    private int getRealScrollY() {
        ViewGroup listView = content.toViewGroup();
        if (listView instanceof AbsListView) {
            View c = listView.getChildAt(0);
            if (c == null) {
                return 0;
            }
            int firstVisiblePosition = ((AbsListView) listView).getFirstVisiblePosition();
            int result = firstVisiblePosition * c.getHeight();
            $.util.logInfo(SNPullRefreshLayout.class, "firstVisiblePosition=" + firstVisiblePosition + ",result=" + result);
            return result;
        } else {
            return listView.getScrollY();
        }
    }


    private void restoreFooter(final int state, String msg) {
        int speed = BACK_SPEED;
        if (state == LOAD_STATE_NORMAL) {
            if ($.util.strIsNullOrEmpty(msg)) msg = "数据加载成功";
            plFooterStateName.text(msg);
        } else if (state == LOAD_STATE_ERROR) {
            speed = 1000;
            if ($.util.strIsNullOrEmpty(msg)) msg = "数据加载失败";
            plFooterStateName.text(msg);
        } else if (state == LOAD_STATE_DONE) {
            speed = 1000;
            if ($.util.strIsNullOrEmpty(msg)) msg = "数据已全部加载完成";
            plFooterStateName.text(msg);
        }

        //region ContentTop
        int currContentTop = content.margins().getTop();
        int bct = mBeginContentMarginTop;
        ValueAnimator valueContentTopAnimator = ValueAnimator.ofInt(currContentTop, bct);
        valueContentTopAnimator.setDuration(speed);
        valueContentTopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                content.marginTop(v);
            }
        });
        valueContentTopAnimator.start();
        //endregion
        //region ContentBottom
        int currContentBottom = content.margins().getBottom();
        int bcb = mBeginContentMarginBottom;
        ValueAnimator valueContentBottomAnimator = ValueAnimator.ofInt(currContentBottom, bcb);
        valueContentBottomAnimator.setDuration(speed);
        valueContentBottomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                content.marginBottom(v);
            }
        });
        valueContentBottomAnimator.start();
        //endregion
        //region FooterTop
        int currFooterTop = content.margins().getTop();

        final int bft = mBeginFooterMarginTop;
        ValueAnimator valueFooterTopAnimator = ValueAnimator.ofInt(currFooterTop, bft);
        valueFooterTopAnimator.setDuration(speed);
        valueFooterTopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int v = Integer.parseInt(valueAnimator.getAnimatedValue().toString());
                mScrollState = SCROLL_STATE_BACK_SCROLLING;
                plFooter.marginTop(v);
                if (v == bft) {
                    mScrollState = SCROLL_STATE_NORMAL;
                    mLoadState = LOAD_STATE_NORMAL;
                    setLoadMoreDone(state == LOAD_STATE_DONE);
                    content.scrollEnable(true);
                }
            }
        });
        valueFooterTopAnimator.start();
        //endregion
    }

    public boolean isLoadMoreDone() {
        return loadMoreDone;
    }

    public void setLoadMoreDone(boolean loadMoreDone) {
        if (!loadMoreDone) plFooterStateName.text("加载更多");
        this.loadMoreDone = loadMoreDone;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        if (plFooter != null)
            plFooter.visible(loadMoreEnable ? $.SN_UI_VISIBLE : $.SN_UI_NONE);

    }

    public boolean isLoadMoreEnable() {
        return loadMoreEnable;
    }


    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    public void notifyDataSetChanged() {
        if (content.toView() instanceof AbsListView) {
            if (content.listAdapter() != null)
                content.listAdapter().notifyDataSetChanged();
        }
    }
}
