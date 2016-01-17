package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sn.interfaces.SNAnimationListener;
import com.sn.interfaces.SNOnClickListener;
import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.main.SNManager;

import java.util.IllegalFormatCodePointException;

/**
 * @author Siner QQ348078707
 */
public class SNSlipNavigation extends RelativeLayout {
    SNManager $;
    SNElement $this;
    SNElement menuView;
    SNElement coverView;
    SNElement contentView;
    private VelocityTracker mVelocityTracker;
    private final static int POP_MODE_LEFT = 0;
    private final static int POP_MODE_RIGHT = 1;
    /**
     * 停止滑动
     */
    public final static int TOUCH_STATE_REST = 0;
    /**
     * 正在滑动
     */
    public final static int TOUCH_STATE_SCROLLING = 1;
    boolean mIsMenuSliding = false;
    boolean mAlloyClickClose = false;
    int mPopMode = POP_MODE_RIGHT;
    int mSlideWidth;
    int mLastMotionX = 0;
    int mMaxSpeed = 2000;
    int mMinSpeed = 150;
    float mLastCoverOpacity = 0;
    float mCoverOpacity = 0.7f;
    int mStartLeft = 0;
    private int mDefaultSpeed = 300;
    private int mTouchState = TOUCH_STATE_REST;
    boolean isMenuShow = false;

    boolean isInit = false;
    int contentOffset = -1;


    boolean needOffset() {
        return contentOffset != -1;
    }

    public SNSlipNavigation(Context context) {
        super(context);
        init(context, null);
    }

    public SNSlipNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    void init(Context context, AttributeSet attrs) {
        $ = new SNManager(context);
        $this = $.create(this);
        mVelocityTracker = VelocityTracker.obtain();
        if (attrs != null) {
            TypedArray array = $.obtainStyledAttr(attrs, R.styleable.SNSlipNavigation);
            if (array.hasValue(R.styleable.SNSlipNavigation_content_offset)) {
                contentOffset = array.getDimensionPixelOffset(R.styleable.SNSlipNavigation_content_offset, -1);
            }
            array.recycle();
        }
    }

    public SNSlipNavigation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //measureViews(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureViews(int widthMeasureSpec, int heightMeasureSpec) {
        View coverView = getChildAt(1);
        coverView.measure(widthMeasureSpec, heightMeasureSpec);
        View menuView = getChildAt(2);
        menuView.measure(menuView.getLayoutParams().width + menuView.getLeft()
                + menuView.getRight(), heightMeasureSpec);
        View contentView = getChildAt(0);
        contentView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        super.onLayout(arg0, arg1, arg2, arg3, arg4);
        if (!isInit) {
            isInit = true;
            int childCount = getChildCount();
            if (childCount != 3) {
                throw new IllegalStateException(
                        "The childCount of SlidingMenu must be 3,and the first view is empty relativeLayout.");
            }
            if (coverView == null) {
                coverView = $.create(getChildAt(1));
                int b_color = coverView.backgroundColor();
                if (b_color == 0) {
                    coverView.backgroundColor(Color.BLACK);
                }
            }
            if (menuView == null) {
                menuView = $.create(getChildAt(2));
            }
            if (contentView == null) {
                contentView = $.create(getChildAt(0));
            }
            coverView.visible(SNManager.SN_UI_NONE);
            coverView.clickable(true);
            contentView.layout(0, 0, contentView.width(), contentView.height());
            coverView.layout(0, 0, coverView.width(), coverView.height());
            if (needOffset()) {
                int width = $.displaySize().getWidth() - contentOffset;
                menuView.width(width);
                mSlideWidth = width;
            } else {
                mSlideWidth = menuView.width();
            }
        }
        if (isMenuShow) {
            if (mPopMode == POP_MODE_LEFT)
                menuView.layout(0, 0, mSlideWidth, menuView.height());
            else if (mPopMode == POP_MODE_RIGHT)
                menuView.layout($.displaySize().getWidth() - mSlideWidth, 0, $.displaySize().getWidth(), menuView.height());
        } else {
            if (mPopMode == POP_MODE_LEFT)
                menuView.layout(-mSlideWidth, 0, 0, menuView.height());
            else if (mPopMode == POP_MODE_RIGHT)
                menuView.layout($.displaySize().getWidth(), 0, $.displaySize().getWidth() + mSlideWidth, menuView.height());
        }

        // TODO Auto-generated method stub

    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (!isMenuShow) {
            return false;
        }
        if (mTouchState == TOUCH_STATE_SCROLLING) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = (int) ev.getX();
                mStartLeft = menuView.left();
                if (mPopMode == POP_MODE_LEFT)
                    mAlloyClickClose = mLastMotionX > menuView.right();
                else if (mPopMode == POP_MODE_RIGHT)
                    mAlloyClickClose = mLastMotionX >= 0 && mLastMotionX <= ($.displaySize().getWidth() - mSlideWidth);

                break;
            case MotionEvent.ACTION_MOVE:
                if (isMenuShow) {
                    int currX = (int) ev.getX();
                    if (Math.abs(currX - mLastMotionX) > 10) {
                        mAlloyClickClose = false;
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                if (mAlloyClickClose) {
                    closeMenu(mDefaultSpeed);
                    mAlloyClickClose = false;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }

    public void closeMenu() {
        closeMenu(mDefaultSpeed);
    }

    // 关闭
    public void closeMenu(int speed) {
        if (mIsMenuSliding) {
            return;
        }
        mIsMenuSliding = true;
        int x = 0, left = 0;
        if (mPopMode == POP_MODE_LEFT) {
            x = -mSlideWidth;
            left = x;
        } else if (mPopMode == POP_MODE_RIGHT) {
            x = mSlideWidth;
            left = $.displaySize().getWidth();
        }
        final int f_left = left;
        menuView.slideLeft(x, speed, new SNAnimationListener() {
            @Override
            public void onAnimationStart(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                menuView.layout(f_left, 0, f_left + mSlideWidth, menuView.height());
                isMenuShow = false;
                mIsMenuSliding = false;
                mTouchState = TOUCH_STATE_REST;
                mLastCoverOpacity = 0;
            }
        }, true);

        coverView.fade(mLastCoverOpacity, 0, speed, new SNAnimationListener() {

            @Override
            public void onAnimationStart(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                view.visible(SNManager.SN_UI_NONE);
            }
        }, true);
    }

    // 显示
    public void openMenu() {
        if (isMenuShow) return;
        if (mIsMenuSliding) {
            return;
        }
        mIsMenuSliding = true;
        coverView.fadeIn(mCoverOpacity, mDefaultSpeed, null);
        int from = 0, to = 0, left = 0;
        if (mPopMode == POP_MODE_LEFT) {
            from = 0;
            to = mSlideWidth;
            left = 0;
        } else if (mPopMode == POP_MODE_RIGHT) {
            from = 0;// $.displaySize().getWidth() + mSlideWidth;
            to = -mSlideWidth;//$.displaySize().getWidth();
            left = $.displaySize().getWidth() - mSlideWidth;
        }

        final int f_left = left;
        menuView.slide(from, to, 0, 0, mDefaultSpeed,
                new SNAnimationListener() {
                    @Override
                    public void onAnimationStart(SNElement view,
                                                 Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(SNElement view,
                                                  Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(SNElement view,
                                               Animation animation) {
                        // TODO Auto-generated method stub
                        isMenuShow = true;
                        mIsMenuSliding = false;
                        menuView.layout(f_left, 0, f_left + menuView.width(),
                                menuView.height());
                        mTouchState = TOUCH_STATE_REST;
                        mLastCoverOpacity = mCoverOpacity;
                    }
                }, true);
    }

    // 返回
    public void restoreMenu(int speed, int left) {
        if (mIsMenuSliding) {
            return;
        }
        mIsMenuSliding = true;
        int to = 0, le = 0;
        if (mPopMode == POP_MODE_LEFT) {
            to = left;
            le = 0;
        } else if (mPopMode == POP_MODE_RIGHT) {
            to = -left;//$.displaySize().getWidth();
            le = $.displaySize().getWidth() - mSlideWidth;
        }
        final int f_left = le;
        menuView.slideLeft(to, speed, new SNAnimationListener() {
            @Override
            public void onAnimationStart(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(SNElement view, Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                isMenuShow = true;
                menuView.layout(f_left, 0, f_left + menuView.width(), menuView.height());
                mTouchState = TOUCH_STATE_REST;
                mIsMenuSliding = false;
                mLastCoverOpacity = mCoverOpacity;
            }
        }, true);
        coverView.fade(mLastCoverOpacity, mCoverOpacity, speed, null, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!isMenuShow) {
            return true;
        }
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int currX = (int) ev.getX();
                if (isMenuShow) {
                    if (Math.abs(currX - mLastMotionX) > 10) {
                        mAlloyClickClose = false;
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                }
                if (!mIsMenuSliding) {
                    int currentLeft = menuView.left();
                    int currentRight = menuView.right();

                    mVelocityTracker.addMovement(ev);
                    int left = currentLeft + currX - mLastMotionX;
                    int right = currentRight + currX - mLastMotionX;
                    float opacity = mCoverOpacity;
                    if (currentLeft != 0) {
                        opacity = calcOpacity(left);
                    }
                    mLastMotionX = currX;

                    if (mPopMode == POP_MODE_LEFT) {
                        if (left < mStartLeft && left > -mSlideWidth
                                && currX <= currentRight) {
                            mLastCoverOpacity = opacity;

                            coverView.opacity(opacity);
                            menuView.layout(left, 0, right, menuView.height());
                        }
                    } else if (mPopMode == POP_MODE_RIGHT) {
                        if (left > mStartLeft && left > $.displaySize().getWidth() - mSlideWidth
                                && currX >= currentLeft) {
                            mLastCoverOpacity = opacity;

                            coverView.opacity(opacity);
                            menuView.layout(left, 0, right, menuView.height());
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("$$$$$$$$$$$$$$$$", String.valueOf(mAlloyClickClose));
                if (mAlloyClickClose) {
                    closeMenu(mDefaultSpeed);
                    mAlloyClickClose = false;
                    return true;
                }
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaxSpeed);
                int velocityX = (int) velocityTracker.getXVelocity();
                int speed = mDefaultSpeed;
                if (velocityX != 0) {
                    speed = (int) Math.abs((1000 / (float) Math.abs(velocityX))
                            * Math.abs(menuView.left()));
                }
                Log.e("@@@@@@@@@@@@@@",
                        $.util.strFormat("velocityX={0},speed={1},left={2},1000/Math.abs(velocityX)={3}",
                                velocityX, speed,
                                Math.abs(menuView.left()),
                                1000 / (float) Math.abs(velocityX)));
                if (speed < mMinSpeed) {
                    speed = mMinSpeed;
                }
                if (speed > mDefaultSpeed) {
                    speed = mDefaultSpeed;
                }

                if (mPopMode == POP_MODE_LEFT) {
                    if ((float) velocityX / 1000 * mDefaultSpeed <= -mSlideWidth / 2) {
                        closeMenu(speed);
                    } else if (Math.abs(menuView.left() + mSlideWidth) <= mSlideWidth / 2) {
                        closeMenu(speed);
                    } else {
                        restoreMenu(speed, Math.abs(menuView.left()));
                    }
                } else if (mPopMode == POP_MODE_RIGHT) {
                    if ((float) velocityX / 1000 * mDefaultSpeed >= $.displaySize().getWidth() + mSlideWidth) {
                        closeMenu(speed);
                    } else if (Math.abs($.displaySize().getWidth() - menuView.left()) <= mSlideWidth / 2) {
                        closeMenu(speed);
                    } else {
                        int curr = menuView.left() - ($.displaySize().getWidth() - mSlideWidth);
                        restoreMenu(speed, curr);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public float calcOpacity(int left) {
        float opacity = mCoverOpacity;
        if (mPopMode == POP_MODE_LEFT) {
            if (left >= 0) {
                opacity = mCoverOpacity;
            } else if (left <= -mSlideWidth) {
                opacity = 0;
            } else {
                // opacity=a*left+b
                // 当left=0时，opacit=mCoverOpacity,可求b
                float b = mCoverOpacity;
                // 当opacit=0时,left=-mSlideWidth,可求a
                float a = (0 - b) / -mSlideWidth;
                opacity = a * left + b;
            }
        } else if (mPopMode == POP_MODE_RIGHT) {
            if (left <= ($.displaySize().getWidth() - mSlideWidth)) {
                opacity = mCoverOpacity;
            } else if (left >= $.displaySize().getWidth()) {
                opacity = 0;
            } else {

                int c = $.displaySize().getWidth() - mSlideWidth;

                // opacity=a*left+b
                // 当left=min时，opacit=mCoverOpacity,可求b
                float b = mCoverOpacity;
                // 当opacit=0时,left=max,可求a
                float a = (b) / ($.displaySize().getWidth() - c);
                opacity = b - a * (left - c);
            }
        }

        return opacity;
    }

    /**
     * 获取滑动状态
     * TOUCH_STATE_REST
     * TOUCH_STATE_SCROLLING
     *
     * @return
     */
    public int getTouchState() {
        return mTouchState;
    }

    /**
     * 是否显示
     *
     * @return
     */
    public boolean isShow() {
        return isMenuShow;
    }
}
