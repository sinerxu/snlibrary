package com.sn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.sn.interfaces.SNAnimationListener;
import com.sn.interfaces.SNLazyFragmentSetElementListener;
import com.sn.interfaces.SNThreadListener;
import com.sn.lib.R;
import com.sn.main.SNElement;


public class SNLazyFragment extends SNFragment {
    public SNElement $box;
    public SNElement $main;
    int speed = 500;
    private boolean isPrepared;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if ($box == null)
            $box = $.create(inflater.inflate(R.layout.fragment_lazy, container, false));
        return $box.toView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
        if (getUserVisibleHint()) initPrepare();
    }


    private boolean alreadyFirstVisible = false;
    private boolean alreadyFirstInvisible = false;

    private boolean isLoadElement = false;
    /**
     * 是否需要执行user visible
     */
    private boolean isCallUserVisible = false;

    @Override
    public void onResume() {
        //SNUtility.logDebug(SNLazyFragment.class, "onResume");
        super.onResume();

        if (alreadyFirstVisible && getUserVisibleHint()) {
            isCallUserVisible = true;
            if (isLoadElement)
                onUserVisible();
        }
    }

    @Override
    public void onPause() {
        //SNUtility.logDebug(SNLazyFragment.class, "onPause");
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //SNUtility.logDebug(SNLazyFragment.class, "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!alreadyFirstVisible) {
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (!alreadyFirstInvisible) {
                alreadyFirstInvisible = true;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        //SNUtility.logDebug(SNLazyFragment.class, "initPrepare");
        if (isPrepared && !alreadyFirstVisible) {
            alreadyFirstVisible = true;
            onCreateElement();
        } else {
            isPrepared = true;
        }
    }


    public void setMainElement(SNElement _main, boolean animated) {
        $main = _main;
        $box.id(R.id.mainBox).add($main, new ViewGroup.LayoutParams($.SN_UI_FILL, $.SN_UI_SIZE));
        if (animated) {
            $box.id(R.id.loadingBox).fadeOut(1, speed, new SNAnimationListener() {
                @Override
                public void onAnimationStart(SNElement view, Animation animation) {

                }

                @Override
                public void onAnimationRepeat(SNElement view, Animation animation) {

                }

                @Override
                public void onAnimationEnd(SNElement view, Animation animation) {
                    $box.id(R.id.loadingBox).visible($.SN_UI_NONE);
                }
            });
        } else {
            $box.id(R.id.loadingBox).visible($.SN_UI_NONE);
        }
        onCreateElementFinish(_main);
    }

    public void setMainElement(final int resId, final boolean animated, final SNLazyFragmentSetElementListener setElementListener) {
        $.util.threadRun(new SNThreadListener() {
            @Override
            public Object run() {


                try {
                    Thread.sleep(10);
                } catch (Exception ex) {

                }

                return null;//null;//$.layoutInflateResId(resId);
            }

            @Override
            public void onFinish(Object object) {
                SNElement element = $.layoutInflateResId(resId);//$.layoutInflateResId(resId);//(SNElement) object;
                setMainElement(element, animated);
                if (setElementListener != null)
                    setElementListener.onFinish(element);
            }
        });
    }

    public void setMainElement(int resId, final SNLazyFragmentSetElementListener setElementListener) {
        setMainElement(resId, true, setElementListener);
    }


    //region 可以重写

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        onVisible();
        //SNUtility.logDebug(SNLazyFragment.class, "onUserVisible");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件)
     */
    public void onFirstUserInvisible() {

        //SNUtility.logDebug(SNLazyFragment.class, "onFirstUserInvisible");
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
        //SNUtility.logDebug(SNLazyFragment.class, "onUserInvisible");
    }

    /**
     * fragment每次可见的时候调用（注意：并不能保证控件全部加载完成）
     */
    public void onVisible() {
        //SNUtility.logDebug(SNLazyFragment.class, "onUserInvisible");
    }

    /**
     * 在这里进行初始化工作 setElement
     */
    public void onCreateElement() {
        onVisible();
        //SNUtility.logDebug(SNLazyFragment.class, "onFirstUserVisible");
    }

    /**
     * set element 完成后执行
     *
     * @param element
     */
    public void onCreateElementFinish(SNElement element) {
        isLoadElement = true;
    }

    //endregion

}
