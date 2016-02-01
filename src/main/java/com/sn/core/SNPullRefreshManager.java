package com.sn.core;

import com.sn.controlers.pullrefresh.SNPullRefreshLayout;
import com.sn.interfaces.SNPullRefreshManagerListener;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by xuhui on 15/10/25.
 */
public class SNPullRefreshManager<T> {
    SNElement pullRefreshLayout;
    int page;

    public SNPullRefreshLayout getPullRefreshLayout() {
        return pullRefreshLayout.toView(SNPullRefreshLayout.class);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        if (data == null) data = new ArrayList<T>();
        if (this.data != null) {
            this.data.clear();
            for (T t : data) {
                this.data.add(t);
            }
        }
    }

    public void addData(List<T> data) {
        if (data == null) data = new ArrayList<T>();
        if (this.data != null) {
            for (T t : data) {
                this.data.add(t);
            }
        }
    }

    public void addData(T data) {
        if (this.data != null) {
            this.data.add(data);
        }
    }

    public void clearData() {
        if (this.data != null) {
            this.data.clear();
        }
    }

    int pageSize;
    boolean isDone;
    List<T> data;
    SNPullRefreshManagerListener<T> listener;

    public static void create(SNElement _element, int _pageSize, SNPullRefreshManagerListener listener) {
        new SNPullRefreshManager(_element, _pageSize, listener);
    }

    public static void create(SNElement _element, SNPullRefreshManagerListener listener) {
        new SNPullRefreshManager(_element, listener);
    }

    SNPullRefreshManager(SNElement _element, int _pageSize, SNPullRefreshManagerListener<T> listener) {
        this.pullRefreshLayout = _element;
        this.page = 1;
        this.pageSize = _pageSize;
        this.listener = listener;
        getPullRefreshLayout().setRefreshEnable(true);
        getPullRefreshLayout().setLoadMoreEnable(true);
        getPullRefreshLayout().setPullRefreshListener(new SNPullRefreshLayout.SNPullRefreshListener() {
            @Override
            public void onRefresh(SNPullRefreshLayout pullRefreshLayout) {
                refresh();
            }

            @Override
            public void onLoadMore(SNPullRefreshLayout pullRefreshLayout) {
                loadMore();
            }
        });
        data = new ArrayList<T>();
        if (listener != null) listener.onCreate(this);
    }

    SNPullRefreshManager(SNElement _element, SNPullRefreshManagerListener<T> listener) {
        this.page = 1;
        this.pullRefreshLayout = _element;
        this.listener = listener;
        getPullRefreshLayout().setRefreshEnable(true);
        getPullRefreshLayout().setLoadMoreEnable(false);
        getPullRefreshLayout().setPullRefreshListener(new SNPullRefreshLayout.SNPullRefreshListener() {
            @Override
            public void onRefresh(SNPullRefreshLayout pullRefreshLayout) {
                refresh();
            }

            @Override
            public void onLoadMore(SNPullRefreshLayout pullRefreshLayout) {
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
        if (listener != null)
            listener.onRefresh(this);
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if (this.isDone) {
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
    void setListener(SNPullRefreshManagerListener<T> _listener) {
        this.listener = _listener;
    }

    /**
     * 加载成功后调用
     */
    public void success() {
        this.getPullRefreshLayout().setRefreshState(SNPullRefreshLayout.REFRESH_STATE_NORMAL);
        this.getPullRefreshLayout().setLoadState(SNPullRefreshLayout.LOAD_STATE_NORMAL);
        this.page++;
        this.isDone = false;
        getPullRefreshLayout().notifyDataSetChanged();
    }


    /**
     * 数据全部加载完成后调用
     */
    public void done() {
        done(null);
    }

    /**
     * 数据全部加载完成后调用
     *
     * @param msgResId msg string id
     */
    public void done(int msgResId) {
        done(pullRefreshLayout.stringResId(msgResId));
    }

    /**
     * 数据全部加载完成后调用
     *
     * @param msg msg string
     */
    public void done(String msg) {
        this.isDone = true;
        this.getPullRefreshLayout().setRefreshState(SNPullRefreshLayout.REFRESH_STATE_NORMAL);
        this.getPullRefreshLayout().setLoadState(SNPullRefreshLayout.LOAD_STATE_DONE, msg);
        getPullRefreshLayout().notifyDataSetChanged();
    }

    /**
     * 数据加载失败后调用
     *
     * @param msg msg string
     */
    public void error(String msg) {
        this.getPullRefreshLayout().setRefreshState(SNPullRefreshLayout.REFRESH_STATE_NORMAL);
        this.getPullRefreshLayout().setLoadState(SNPullRefreshLayout.LOAD_STATE_ERROR, msg);
        getPullRefreshLayout().notifyDataSetChanged();
    }

    /**
     * 数据加载失败后调用
     *
     * @param msgResId msg string id
     */
    public void error(int msgResId) {
        error(pullRefreshLayout.stringResId(msgResId));

    }

    /**
     * 数据加载失败后调用
     */
    public void error() {
        error(null);
    }

}
