package com.sn.util;

import com.sn.interfaces.SNXListListener;
import com.sn.main.SNElement;

import java.util.ArrayList;

import me.maxwin.view.XListView;

/**
 * Created by xuhui on 15/10/25.
 */
public class SNXListManager<T> {
    public SNElement listView;
    public int page;
    public int pageSize;
    public boolean isDone;
    public ArrayList<T> data;
    public SNXListListener<T> listener;

    public static void create(SNElement _element, int _pageSize, SNXListListener listener) {
        new SNXListManager(_element, _pageSize, listener);
    }

    public static void create(SNElement _element, SNXListListener listener) {
        new SNXListManager(_element, listener);
    }

    SNXListManager(SNElement _element, int _pageSize, SNXListListener<T> listener) {
        this.listView = _element;
        this.pageSize = _pageSize;
        this.listener = listener;
        listView.pullRefreshEnable(true);
        listView.pullLoadEnable(true);
        listView.pullListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        data = new ArrayList<T>();
        if (listener != null) listener.onCreate(this);
    }

    SNXListManager(SNElement _element, SNXListListener<T> listener) {
        this.listView = _element;
        this.listener = listener;
        listView.pullRefreshEnable(true);
        listView.pullLoadEnable(false);
        listView.pullListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        data = new ArrayList<T>();

        if (listener != null) listener.onCreate(this);
    }

    /**
     * 刷新
     */
    public void refresh() {
        this.page = 1;
        this.isDone = false;
        data.clear();
        if (listener != null)
            listener.onRefresh(this);
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if (this.isDone && listView != null) {
            this.done();
            return;
        }
        if (listener != null)
            listener.onLoadMore(this);
    }

    /**
     * 设置监听事件
     *
     * @param _listener
     */
    public void setListener(SNXListListener<T> _listener) {
        this.listener = _listener;
    }

    /**
     * 加载成功后调用
     */
    public void success() {
        this.listView.pullStop();
        this.page++;
        this.isDone = false;
    }

    /**
     * 加载成功后调用
     *
     * @param msgResId msg string id
     */
    public void success(int msgResId) {
        success();
        msg(msgResId);
    }

    /**
     * 加载成功后调用
     *
     * @param msg msg string
     */
    public void success(String msg) {
        success();
        msg(msg);
    }

    /**
     * 数据全部加载完成后调用
     */
    public void done() {
        this.listView.pullLoadFinish();
        this.isDone = true;
    }

    /**
     * 数据全部加载完成后调用
     *
     * @param msgResId msg string id
     */
    public void done(int msgResId) {
        done();
        msg(msgResId);
    }

    /**
     * 数据全部加载完成后调用
     *
     * @param msg msg string
     */
    public void done(String msg) {
        done();
        this.listView.pullHintMessage(msg);
    }

    /**
     * 数据加载失败后调用
     *
     * @param msg msg string
     */
    public void error(String msg) {
        error();
        msg(msg);
    }

    /**
     * 数据加载失败后调用
     *
     * @param msgResId msg string id
     */
    public void error(int msgResId) {
        error();
        msg(msgResId);
    }

    /**
     * 数据加载失败后调用
     */
    public void error() {
        this.listView.pullLoadError();
    }

    /**
     * 可以设置底部提示信息
     *
     * @param msgResId msg string id
     */
    public void msg(int msgResId) {
        this.listView.pullHintMessage(msgResId);
    }

    /**
     * 可以设置底部提示信息 msg string
     *
     * @param msg
     */
    public void msg(String msg) {
        this.listView.pullHintMessage(msg);
    }
}
