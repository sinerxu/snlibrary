package com.sn.controlers;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.sn.interfaces.SNAnimationListener;
import com.sn.main.SNElement;
import com.sn.main.SNManager;
import com.sn.util.SNUtility;

/**
 * @author Siner QQ348078707
 */
public class SNSlipNavigation extends ViewGroup {
    SNManager $;
    SNElement $this;
    SNElement menuView;
    SNElement coverView;
    SNElement contentView;
    private VelocityTracker mVelocityTracker;

    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    boolean mIsMenuSliding = false;
    boolean mAlloyClickClose = false;
    int mSlideWidth;
    int mLastMotionX = 0;
    int mMaxSpeed = 2000;
    int mMinSpeed = 150;
    float mLastCoverOpacity = 0;
    float mCoverOpacity = 0.7f;
    int mStartLeft = 0;
    public int mDefaultSpeed = 300;
    public int mTouchState = TOUCH_STATE_REST;
    boolean isMenuShow = false;

    public SNSlipNavigation(Context context) {
        this(context, null, 0);
    }

    public SNSlipNavigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SNSlipNavigation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        $ = new SNManager(context);
        $this = $.create(this);
        mSlideWidth = $.px(200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureViews(widthMeasureSpec, heightMeasureSpec);
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
        // TODO Auto-generated method stub
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
        contentView.layout(0, 0, contentView.width(), contentView.height());
        coverView.layout(0, 0, coverView.width(), coverView.height());

        coverView.visible(SNManager.SN_UI_NONE);
        mSlideWidth = menuView.width();
        menuView.layout(-mSlideWidth, 0, 0, menuView.height());

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
                mAlloyClickClose = mLastMotionX > menuView.right();
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

    // 关闭
    public void closeMenu(int speed) {
        if (mIsMenuSliding) {
            return;
        }
        mIsMenuSliding = true;
        menuView.slideLeft(-mSlideWidth, speed, new SNAnimationListener() {
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
                menuView.layout(-mSlideWidth, 0, 0, menuView.height());
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
        if (mIsMenuSliding) {
            return;
        }
        mIsMenuSliding = true;
        coverView.fadeIn(mCoverOpacity, mDefaultSpeed, null);
        menuView.slideLeft(mSlideWidth, mDefaultSpeed,
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
                        menuView.layout(0, 0, menuView.width(),
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
        menuView.slideLeft(left, speed, new SNAnimationListener() {
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
                menuView.layout(0, 0, menuView.width(), menuView.height());
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
                    Log.e("$$$$$$$$$$$$$$$$", "" + Math.abs(currX - mLastMotionX));
                    if (Math.abs(currX - mLastMotionX) > 10) {
                        Log.e("$$$$$$$$$$$$$$$$", "false");
                        mAlloyClickClose = false;
                        mTouchState = TOUCH_STATE_SCROLLING;
                    }
                }
                if (!mIsMenuSliding) {
                    int currentLeft = menuView.left();
                    int currentRight = menuView.right();
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.addMovement(ev);
                    int left = currentLeft + currX - mLastMotionX;
                    int right = currentRight + currX - mLastMotionX;
                    float opacity = mCoverOpacity;
                    if (currentLeft != 0) {
                        opacity = calcOpacity(left);
                    }
                    mLastMotionX = currX;
                    if (left < mStartLeft && left > -mSlideWidth
                            && currX <= currentRight) {
                        mLastCoverOpacity = opacity;

                        coverView.opacity(opacity);
                        menuView.layout(left, 0, right, menuView.height());
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
                        SNUtility.format("velocityX={0},speed={1},left={2},1000/Math.abs(velocityX)={3}",
                                velocityX, speed,
                                Math.abs(menuView.left()),
                                1000 / (float) Math.abs(velocityX)));
                if (speed < mMinSpeed) {
                    speed = mMinSpeed;
                }
                if (speed > mDefaultSpeed) {
                    speed = mDefaultSpeed;
                }
                if ((float) velocityX / 1000 * mDefaultSpeed <= -mSlideWidth) {
                    closeMenu(speed);
                } else if (Math.abs(menuView.left() + mSlideWidth) <= mSlideWidth / 2) {
                    closeMenu(speed);
                } else {
                    restoreMenu(speed, Math.abs(menuView.left()));
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public float calcOpacity(int left) {
        float opacity = mCoverOpacity;
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
        return opacity;
    }
}
