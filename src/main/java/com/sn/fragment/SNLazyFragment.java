package com.sn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.util.SNUtility;

/**
 * Created by xuhui on 15/8/16.
 */
public class SNLazyFragment extends SNFragment {
    public SNElement $box;
    public SNElement $main;
    int speed = 500;
    private boolean isPrepared;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        $box = $.create(inflater.inflate(R.layout.fragment_lazy, container, false));
        return $box.toView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;


    @Override
    public void onResume() {
        //SNUtility.logDebug(SNLazyFragment.class, "onResume");
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
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
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        //SNUtility.logDebug(SNLazyFragment.class, "initPrepare");
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
        //SNUtility.logDebug(SNLazyFragment.class, "onFirstUserVisible");
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        //SNUtility.logDebug(SNLazyFragment.class, "onUserVisible");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
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

    public void setMainElement(SNElement _main, boolean animated) {
        $main = _main;
        $box.id(R.id.mainBox).add($main, new ViewGroup.LayoutParams($.SN_UI_FILL, $.SN_UI_SIZE));
        if (animated) {
            $box.id(R.id.loadingBox).fadeOut(1, speed, null);
        } else {
            $box.id(R.id.loadingBox).opacity(0);
        }
    }

    public void setMainElement(int resId, boolean animated) {
        SNElement v = $.layoutInflateResId(resId);
        setMainElement(v, animated);
    }

    public void setMainElement(int resId) {
        SNElement v = $.layoutInflateResId(resId);
        setMainElement(v, true);
    }

    public SNElement getMainElement() {
        return $main;
    }
}
