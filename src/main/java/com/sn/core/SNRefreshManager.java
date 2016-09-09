package com.sn.core;

import com.sn.interfaces.SNPullRefreshManagerListener;

import java.util.List;

/**
 * Created by xuhui on 16/9/2.
 */
public interface SNRefreshManager<T> {

    /**
     * 获取当前的页数
     */
    public int getPage();

    /**
     * 设置页数
     *
     * @param page
     */
    public void setPage(int page);

    /**
     * 获取分页显示的大小
     *
     * @return
     */
    public int getPageSize();

    /**
     * 获取分页显示的大小
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize);

    /**
     * 获取是否加载完成
     *
     * @return
     */
    public boolean isDone();

    /**
     * 设置完成
     *
     * @param isDone
     */
    public void setIsDone(boolean isDone);

    /**
     * 获取数据
     *
     * @return
     */
    public List<T> getData();

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(List<T> data);

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<T> data);

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(T data);

    /**
     * 清理数据
     */
    public void clearData();


    /**
     * 刷新
     */
    public void refresh();

    /**
     * 加载更多
     */
    public void loadMore();

    /**
     * 设置监听事件
     *
     * @param _listener
     */
    void setListener(SNPullRefreshManagerListener<T> _listener);

    /**
     * 加载成功后调用
     */
    public void success();

    /**
     * 数据全部加载完成后调用
     */
    public void done();

    /**
     * 数据全部加载完成后调用
     *
     * @param msgResId msg string id
     */
    public void done(int msgResId);

    /**
     * 数据全部加载完成后调用
     *
     * @param msg msg string
     */
    public void done(String msg);

    /**
     * 数据加载失败后调用
     *
     * @param msg msg string
     */
    public void error(String msg);

    /**
     * 数据加载失败后调用
     *
     * @param msgResId msg string id
     */
    public void error(int msgResId);

    /**
     * 数据加载失败后调用
     */
    public void error();
}
