package com.sn.core;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.andview.refreshview.XRefreshViewHeader;
import com.sn.interfaces.SNPullRefreshManagerListener;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 16/9/2.
 */
public class SNXPullRefreshManager<T> implements SNRefreshManager<T> {


    SNElement pullRefreshLayout;
    int page;

    int pageSize;
    boolean isDone;
    List<T> data;


    SNPullRefreshManagerListener<T> listener;

    SNXPullRefreshManager(SNElement _element, int _pageSize, SNPullRefreshManagerListener<T> listener) {
        this.pullRefreshLayout = _element;
        this.page = 1;
        this.pageSize = _pageSize;
        this.listener = listener;
        init();
        getPullRefreshLayout().loadMoreEnable(true);
        getPullRefreshLayout().toView(XRefreshView.class).setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadMore();
            }
        });
        data = new ArrayList<T>();
        if (listener != null) listener.onCreate(this);
    }

    SNXPullRefreshManager(SNElement _element, SNPullRefreshManagerListener<T> listener) {
        this.page = 1;
        this.pullRefreshLayout = _element;
        this.listener = listener;
        init();
        getPullRefreshLayout().loadMoreEnable(false);
        getPullRefreshLayout().toView(XRefreshView.class).setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadMore();
            }
        });
        data = new ArrayList<T>();

        if (listener != null) listener.onCreate(this);

    }


    void init() {
        XRefreshView refreshView = getPullRefreshLayout().toView(XRefreshView.class);
        refreshView.setPinnedContent(true);
        refreshView.setPinnedTime(200);
        refreshView.setHideFooterWhenComplete(false);
    }

    public SNElement getPullRefreshLayout() {
        return pullRefreshLayout;
    }

    public static void create(SNElement _element, int _pageSize, SNPullRefreshManagerListener listener) {
        new SNXPullRefreshManager(_element, _pageSize, listener);
    }

    public static void create(SNElement _element, SNPullRefreshManagerListener listener) {
        new SNPullRefreshManager(_element, listener);
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
    public void setListener(SNPullRefreshManagerListener<T> _listener) {
        this.listener = _listener;
    }

    /**
     * 加载成功后调用
     */
    public void success() {
        this.getPullRefreshLayout().loadStop();
        this.getPullRefreshLayout().refreshStop();
        this.page++;
        this.isDone = false;
        getPullRefreshLayout().toView(XRefreshView.class).notifyDataSetChanged();
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
        setMessage(msg);
        this.getPullRefreshLayout().loadDone();
        notifyDataSetChanged();
    }

    /**
     * 数据加载失败后调用
     *
     * @param msg msg string
     */
    public void error(String msg) {
        setMessage(msg);
        this.getPullRefreshLayout().loadStop();
        notifyDataSetChanged();
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


    void setFooterMessage(String msg) {
        this.getPullRefreshLayout().toView(XRefreshView.class).getCustomFooterView(XRefreshViewFooter.class).setMessage(msg);
    }

    void setHeaderMessage(String msg) {
        this.getPullRefreshLayout().toView(XRefreshView.class).getCustomHeaderView(XRefreshViewHeader.class).setMessage(msg);
    }

    void setMessage(String msg) {
        setHeaderMessage(msg);
        setFooterMessage(msg);
    }

    void notifyDataSetChanged() {
        getPullRefreshLayout().toView(XRefreshView.class).notifyDataSetChanged();
    }
}
