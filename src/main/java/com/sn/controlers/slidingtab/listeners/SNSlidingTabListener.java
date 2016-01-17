package com.sn.controlers.slidingtab.listeners;

import android.support.v4.app.Fragment;

import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/12/19.
 */
public interface SNSlidingTabListener {
    void onPage(int _page, SNElement _item, Fragment _content);

    void onInitFinish();
}
