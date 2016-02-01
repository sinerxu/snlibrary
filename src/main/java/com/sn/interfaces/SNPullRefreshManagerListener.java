package com.sn.interfaces;

import com.sn.core.SNPullRefreshManager;
import com.sn.core.SNXListManager;

/**
 * Created by xuhui on 16/1/30.
 */
public interface SNPullRefreshManagerListener<T> {
    /**
     * 当下拉刷新时会调用此方法
     *
     * @param manager SNXListManager
     */
    public void onRefresh(SNPullRefreshManager<T> manager);

    /**
     * 当加载更多时会调用此方法
     *
     * @param manager SNXListManager
     */
    public void onLoadMore(SNPullRefreshManager<T> manager);

    /**
     * 初始化完成后会调用此方法，这里可以处理bind adapter逻辑
     *
     * @param manager SNXListManager
     */
    public void onCreate(SNPullRefreshManager<T> manager);
}
