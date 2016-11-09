package com.sn.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.sn.controlers.SNFragmentScrollable;
import com.sn.controlers.SNGridView;
import com.sn.controlers.SNImageView;
import com.sn.controlers.SNListView;
import com.sn.controlers.SNNavTitleBar;
import com.sn.controlers.SNScrollable;
import com.sn.controlers.SNSlipNavigation;
import com.sn.controlers.SNWebView;
import com.sn.controlers.pullrefresh.SNPullRefreshLayout;
import com.sn.controlers.slidingtab.SNSlidingTabBar;
import com.sn.controlers.slidingtab.homebottomtab.SNHomeBottomTabItem;
import com.sn.controlers.slidingtab.listeners.SNSlidingTabListener;
import com.sn.controlers.wheel.adapters.WheelViewAdapter;
import com.sn.controlers.wheel.views.OnWheelChangedListener;
import com.sn.controlers.wheel.views.OnWheelScrollListener;
import com.sn.controlers.wheel.views.WheelView;
import com.sn.core.ProgressBarManager;
import com.sn.core.SNLoadBitmapManager;
import com.sn.core.SNPullRefreshManager;
import com.sn.core.SNRefreshManager;
import com.sn.core.SNXListManager;
import com.sn.interfaces.SNAdapterListener;
import com.sn.interfaces.SNAdapterOnItemClickListener;
import com.sn.interfaces.SNAnimationListener;
import com.sn.interfaces.SNOnClickListener;
import com.sn.interfaces.SNOnGetImageUrlListener;
import com.sn.interfaces.SNOnImageLoadListener;
import com.sn.interfaces.SNOnLoadImageFinishListener;
import com.sn.interfaces.SNOnLongClickListener;
import com.sn.interfaces.SNOnSetImageListenter;
import com.sn.interfaces.SNOnTouchListener;
import com.sn.models.SNAdapterViewInject;
import com.sn.models.SNMargins;
import com.sn.models.SNSize;
import com.sn.override.SNAdapter;

import java.util.AbstractSequentialList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListViewFooter;

/**
 * @author Siner QQ348078707
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SNElement extends SNManager {

    public SNElement(View view) {
        super(view.getContext());
        this.elem = view;
    }

    void errorNullOrNotInstance(String instanceName) {
        throw new IllegalStateException("This elem is null or not instance of " + instanceName + ".");
    }

    void errorNull() {
        throw new IllegalStateException("This elem is null.");
    }

    // region member

    View elem;

    // endregion

    // region base function

    /**
     * find element by id, equal view.findViewById
     *
     * @param id layout‘s id
     * @return
     */
    @Override
    public SNElement create(int id) {
        return find(id);
    }

    /**
     * find element by id, equal view.findViewById
     *
     * @param id layout‘s id
     * @return
     */
    @Override
    public View findView(int id) {
        return find(id).toView();
    }

    /**
     * find element by id, equal view.findViewById
     *
     * @param viewClass view’s class
     * @param id        layout‘s id
     * @param <T>
     * @return
     */
    @Override
    public <T> T findView(Class<T> viewClass, int id) {
        return (T) findView(id);
    }

    /**
     * find element by id, equal view.findViewById
     *
     * @param resId layout‘s id
     * @return
     */
    public SNElement find(int resId) {
        return create(toView().findViewById(resId));
    }

    /**
     * find element by id, equal view.findViewById
     *
     * @param resId layout‘s id
     * @return
     */
    public SNElement id(int resId) {
        return find(resId);
    }

    /**
     * get parent's elem
     *
     * @return
     */
    public SNElement parent() {
        View node = elem;
        View result = null;
        ViewParent p = node.getParent();
        if (p != null)
            result = (View) p;
        return create(result);
    }

    /**
     * remove a child elem.
     *
     * @param _elem a child elem.
     * @return
     */
    public SNElement remove(SNElement _elem) {
        ViewGroup viewGroup = (ViewGroup) toView();
        viewGroup.removeView(_elem.toView());
        return this;
    }


    /**
     * remove all child elem.
     *
     * @param _elem a child elem.
     * @return
     */
    public SNElement removeAllChild() {
        ViewGroup viewGroup = (ViewGroup) toView();
        viewGroup.removeAllViews();
        return this;
    }


    /**
     * add a elem
     *
     * @param element      elem
     * @param layoutParams layout params
     * @return
     */
    public SNElement add(SNElement element, ViewGroup.LayoutParams layoutParams) {
        if (elem != null && elem instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) elem;
            viewGroup.addView(element.elem, layoutParams);
        } else {
            errorNullOrNotInstance("ViewGroup");
        }
        return this;
    }

    /**
     * add a elem
     *
     * @param element elem
     * @return
     */
    public SNElement add(SNElement element) {
        if (elem != null && elem instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) elem;
            viewGroup.addView(element.elem);
        } else {
            errorNullOrNotInstance("ViewGroup");
        }
        return this;
    }

    /**
     * add a elem
     *
     * @param element elem
     * @return
     */
    public SNElement add(SNElement element, int index) {
        if (elem != null && elem instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) elem;
            viewGroup.addView(element.elem, index);
        } else {
            errorNullOrNotInstance("ViewGroup");
        }
        return this;
    }

    /**
     * add a view in self
     *
     * @param view         view
     * @param layoutParams layout params
     * @return
     */
    public SNElement add(View view, ViewGroup.LayoutParams layoutParams) {
        add(create(view), layoutParams);
        return this;
    }

    /**
     * add a view in self
     *
     * @param view
     * @return
     */
    public SNElement add(View view) {
        add(create(view));
        return this;
    }

    public int childCount() {
        try {
            return toViewGroup().getChildCount();
        } catch (Exception ex) {
            return 0;
        }
    }

    public SNElement childAt(int index) {
        try {
            return create(toViewGroup().getChildAt(index));
        } catch (Exception ex) {
            return null;
        }
    }


    /**
     * 转成view
     *
     * @return
     */
    public View toView() {
        return elem;
    }

    /**
     * 转成view
     *
     * @param _c  view对象的类型
     * @param <T> view对象的类型
     * @return
     */
    public <T> T toView(Class<T> _c) {
        return (T) elem;
    }

    public ViewGroup toViewGroup() {
        ViewGroup viewGroup = null;
        if (elem != null) {
            viewGroup = toView(ViewGroup.class);
        } else {
            errorNull();
        }
        return viewGroup;
    }

    public SNElement clickable(boolean clickable) {
        if (elem != null) {
            elem.setClickable(clickable);
        }
        return this;
    }

    public boolean clickable() {
        if (elem != null) {
            return elem.isClickable();
        }
        return false;
    }
    // endregion

    // region listener

    /**
     * click's listener
     *
     * @param onClickListener
     */
    public SNElement click(final SNOnClickListener onClickListener) {
        if (elem != null) {
            elem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    if (onClickListener != null) {
                        onClickListener.onClick(SNElement.this);
                    }
                }
            });
        }
        return this;
    }

    /**
     * perform click's listener
     *
     * @return
     */
    public SNElement click() {
        if (elem != null) {
            elem.performClick();
        }
        return this;
    }

    /**
     * list view item click's listener
     *
     * @param onItemClickListener
     * @return
     */
    public SNElement itemClick(final SNAdapterOnItemClickListener onItemClickListener) {
        if (elem != null && elem instanceof AbsListView) {
            AbsListView view = (AbsListView) elem;
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (onItemClickListener != null) {
                        SNAdapterViewInject holder = (SNAdapterViewInject) view.getTag();
                        holder.setParent(parent);
                        onItemClickListener.onItemClick(holder);
                    }
                }
            });
        }
        return this;
    }

    public SNElement scrollEnable(boolean enable) {
        if (elem != null && elem instanceof SNListView) {
            SNListView view = (SNListView) elem;
            view.setScrollEnable(enable);
        } else if (elem != null && elem instanceof SNGridView) {
            SNGridView view = (SNGridView) elem;
            view.setScrollEnable(enable);
        }
        return this;
    }

    /**
     * 主动触发itemClick
     *
     * @param holder
     * @return
     */
    public SNElement itemClick(SNAdapterViewInject holder) {
        if (elem != null && elem instanceof AbsListView) {
            AbsListView view = (AbsListView) elem;
            view.performItemClick(holder.getView().toView(), holder.getPos(), 0);
        }
        return this;
    }

    /**
     * 长按事件
     *
     * @param listener 回调
     * @return
     */
    public SNElement longClicked(final SNOnLongClickListener listener) {

        if (elem != null) {
            elem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // TODO Auto-generated method stub
                    if (listener != null) {
                        return listener.onLonbgClick(SNElement.this);
                    }
                    return false;
                }
            });
        }
        return this;
    }

    /**
     * 主动触发长按事件
     *
     * @return
     */
    public SNElement longClicked() {
        if (elem != null) {
            elem.performLongClick();
        }
        return this;
    }

    /**
     * 滑动事件
     *
     * @param listener
     * @return
     */
    public SNElement touch(final SNOnTouchListener listener) {
        if (elem != null) {
            elem.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    // TODO Auto-generated method stub
                    if (listener != null) {
                        boolean b = listener.onLoad(SNElement.this, event);
                        if (b) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    Log.i("@@@onTouch", "ACTION_DOWN");
                                    b = listener.onTouchDown(SNElement.this, event);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    Log.i("@@@onTouch", "ACTION_UP");
                                    b = listener.onTouchUp(SNElement.this, event);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    b = listener.onTouchMove(SNElement.this, event);
                                    Log.i("@@@onTouch", "ACTION_MOVE");
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                    b = listener.onTouchCancel(SNElement.this, event);
                                    Log.i("@@@onTouch", "ACTION_CANCEL");
                                    break;
                            }
                            return b;
                        } else {
                            return false;
                        }

                    }
                    return false;
                }
            });
        }
        return this;
    }

    // endregion

    // region position

    /**
     * 设置左侧外边距
     *
     * @param left
     */
    public SNElement marginLeft(int left) {
        SNMargins margins = margins();
        margins.setLeft(left);
        margins(margins);
        return this;
    }

    /**
     * 设置右侧外边距
     *
     * @param right
     */
    public SNElement marginRight(int right) {
        SNMargins margins = margins();
        margins.setRight(right);
        margins(margins);
        return this;
    }

    /**
     * 设置顶部外边距
     *
     * @param top
     */
    public SNElement marginTop(int top) {
        SNMargins margins = margins();
        margins.setTop(top);
        margins(margins);
        return this;
    }

    /**
     * 设置底部外边距
     *
     * @param bottom
     */
    public SNElement marginBottom(int bottom) {
        SNMargins margins = margins();
        margins.setBottom(bottom);
        margins(margins);
        return this;
    }

    /**
     * 移动
     *
     * @param x
     * @param y
     */
    public SNElement move(int x, int y) {
        SNMargins margins = margins();
        margins.setLeft(margins.getLeft() + x);
        margins.setTop(margins.getTop() + y);
        margins(margins);
        return this;
    }

    /**
     * 设置外边距
     *
     * @param margins
     */
    public SNElement margins(SNMargins margins) {
        if (margins == null) {
            margins = new SNMargins();
        }
        LayoutParams _layoutParams = elem.getLayoutParams();
        if (_layoutParams == null) {
            return this;
        }
        if (_layoutParams instanceof MarginLayoutParams) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) _layoutParams;
            layoutParams.setMargins(margins.getLeft(), margins.getTop(), margins.getRight(), margins.getBottom());
            setLayoutParams(layoutParams);
        }
        return this;
    }

    /**
     * 获取边距
     *
     * @return
     */
    public SNMargins margins() {
        SNMargins margins = new SNMargins();
        LayoutParams _layoutParams = elem.getLayoutParams();
        if (_layoutParams == null) {
            return margins;
        }

        if (_layoutParams instanceof MarginLayoutParams) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) _layoutParams;
            margins = new SNMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin,
                    layoutParams.bottomMargin);

            setLayoutParams(layoutParams);
        }
        return margins;
    }

    // endregion

    // region common

    /**
     * getLayoutParams
     *
     * @return
     */
    public LayoutParams getLayoutParams() {
        return elem.getLayoutParams();
    }

    /**
     * setLayoutParams
     *
     * @param layoutParams
     * @return
     */
    public SNElement setLayoutParams(LayoutParams layoutParams) {
        elem.setLayoutParams(layoutParams);
        return this;
    }


    /**
     * getContext
     *
     * @return
     */
    public Context getContext() {
        if (elem != null) {
            return elem.getContext();
        } else {
            return null;
        }
    }

    public SNElement inputToggle() {
        inputToggle(this);
        return this;
    }

    public SNElement inputShow() {
        inputShow(this);
        return this;
    }

    public SNElement inputHide() {
        inputHide(this);
        return this;
    }

    public IBinder windowToken() {
        return elem.getWindowToken();
    }

    /**
     * set visible
     *
     * @param visible SNConfig->visibility value
     * @return
     */
    public SNElement visible(int visible) {
        this.elem.setVisibility(visible);

        return this;
    }

    /**
     * get visibility
     *
     * @return
     */
    public int visible() {
        return this.elem.getVisibility();
    }

    /**
     * 设置尺寸
     *
     * @param width 是否设置宽度
     * @param n     值
     * @param dip   是否是dip
     * @return
     */
    public SNElement size(boolean width, int n, boolean dip) {
        if (this.elem != null) {
            LayoutParams lp = this.elem.getLayoutParams();
            if (lp != null) {
                if (n > 0 && dip) {
                    n = px(n);
                }
                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }
                this.elem.setLayoutParams(lp);
                this.elem.requestLayout();
            }
        }
        return this;
    }

    public SNElement size(SNSize size) {
        if (this.elem != null) {
            LayoutParams lp = this.elem.getLayoutParams();
            if (lp != null) {
                lp.width = size.getWidth();
                lp.height = size.getHeight();
                this.elem.setLayoutParams(lp);
                this.elem.requestLayout();
            }
        }
        return this;
    }

    /**
     * get elem measured width
     *
     * @return
     */
    public int width() {
        return this.elem.getMeasuredWidth();
    }

    public SNSize size() {
        SNSize size = new SNSize();
        if (this.elem != null) {
            LayoutParams lp = this.elem.getLayoutParams();
            if (lp != null) {
                size.setWidth(lp.width);
                size.setHeight(lp.height);
            }
        }
        return size;
    }


    /**
     * Set elem measured width
     *
     * @param px 像素值
     * @return
     */
    public SNElement width(int px) {
        size(true, px, false);
        return this;
    }

    /**
     * 获取高度
     *
     * @return
     */
    public int height() {
        return this.elem.getMeasuredHeight();
    }

    /**
     * 设置高度
     *
     * @param px
     * @return
     */
    public SNElement height(int px) {
        return size(false, px, false);

    }


    /**
     * 设置背景样式
     *
     * @param id
     * @return Drawable id
     */
    public SNElement background(int id) {
        if (elem != null) {
            if (id != 0) {
                elem.setBackgroundResource(id);
            }
        }
        return this;
    }

    /**
     * 设置背景样式
     *
     * @param drawable Drawable
     * @return
     */
    public SNElement background(Drawable drawable) {
        if (elem != null) {
            elem.setBackground(drawable);
        }
        return this;
    }

    /**
     * 背景颜色
     *
     * @param color color
     * @return
     */
    public SNElement backgroundColor(int color) {
        if (elem != null) {
            elem.setBackgroundColor(color);
        }
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param colorResId color resid
     * @return
     */
    public SNElement backgroundColorResId(int colorResId) {
        if (elem != null) {
            elem.setBackgroundColor(colorResId((colorResId)));
        }
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @return
     */
    public int backgroundColor() {
        if (elem != null) {
            ColorDrawable colorDrawable = (ColorDrawable) elem.getBackground();
            if (colorDrawable == null) {
                return 0;
            }
            return colorDrawable.getColor();
        } else {
            return 0;
        }

    }

    /**
     * 设置tag
     *
     * @param tag object tag
     * @return
     */
    public SNElement tag(Object tag) {
        if (elem != null) {
            elem.setTag(tag);
        }
        return this;
    }

    /**
     * 获取tag
     *
     * @return
     */
    public Object tag() {
        if (elem != null) {
            return elem.getTag();
        }
        return null;
    }

    /**
     * layout
     *
     * @param left   left
     * @param top    top
     * @param right  right
     * @param bottom bottom
     * @return SNElement
     */
    public SNElement layout(int left, int top, int right, int bottom) {
        if (elem != null) {
            elem.layout(left, top, right, bottom);
        }
        return this;
    }

    /**
     * measure
     *
     * @param w width
     * @param h height
     * @return SNElement
     */
    public SNElement measure(int w, int h) {
        if (elem != null) {
            elem.measure(w, h);
        }
        return this;
    }

    /**
     * get left
     *
     * @return left
     */
    public int left() {
        if (elem != null) {
            return elem.getLeft();
        }
        return 0;
    }

    /**
     * set left
     *
     * @param _left left
     * @return SNElement
     */
    public SNElement left(int _left) {
        if (elem != null) {
            elem.setLeft(_left);
        }
        return this;
    }

    /**
     * get right
     *
     * @return right
     */
    public int right() {
        if (elem != null) {
            return elem.getRight();
        }
        return 0;
    }

    /**
     * set right
     *
     * @param _right right
     * @return SNElement
     */
    public SNElement right(int _right) {
        if (elem != null) {
            elem.setLeft(_right);
        }
        return this;
    }

    /**
     * get top
     *
     * @return top
     */
    public int top() {
        if (elem != null) {
            return elem.getTop();
        }
        return 0;
    }

    /**
     * set top
     *
     * @param _top
     * @return SNElement
     */
    public SNElement top(int _top) {
        if (elem != null) {
            elem.setTop(_top);
        }
        return this;
    }

    /**
     * get bottom
     *
     * @return int
     */
    public int bottom() {
        if (elem != null) {
            return elem.getBottom();
        }
        return 0;
    }

    /**
     * set bottom
     *
     * @param _bottom bottom
     * @return SNElement
     */
    public SNElement bottom(int _bottom) {
        if (elem != null) {
            elem.setBottom(_bottom);
        }
        return this;
    }

    //endregion　

    //region webview

    public SNElement webChromeClient(WebChromeClient webChromeClient) {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.setWebChromeClient(webChromeClient);
        }
        return this;
    }

    public SNElement webViewClient(WebViewClient webViewClient) {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.setWebViewClient(webViewClient);
        }
        return this;
    }

    public SNElement webAllowOpenUrlInApp() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
        }
        return this;
    }

    public WebSettings webSettings() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            return webView.getSettings();
        }
        return null;
    }

    public SNElement webCookie(String url, String cookie) {
        if (elem instanceof WebView) {
            CookieSyncManager.createInstance(getActivity());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
            CookieSyncManager.getInstance().sync();
        }
        return this;
    }

    public SNElement webResponsive() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.getSettings().setDefaultTextEncodingName(SNConfig.DEFAULT_ENCODING);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);

        }
        return this;
    }

    public SNElement webGoForward() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.goForward();
        }
        return this;
    }

    public boolean webCanGoForward() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            return webView.canGoForward();
        }
        return false;
    }

    public SNElement webGoBack() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.goBack();
        }
        return this;
    }

    public boolean webCanGoBack() {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            return webView.canGoBack();
        }
        return false;
    }

    public SNElement jsInterface(Object object, String objectName) {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.addJavascriptInterface(object, objectName);
        }
        return this;
    }

    public SNElement loadUrl(String url) {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.loadUrl(url);
        } else if (elem instanceof SNWebView) {
            SNWebView webView = (SNWebView) elem;
            webView.loadUrl(url);
        }
        return this;
    }

    public SNElement loadHtml(String html) {
        return loadHtml("about:blank", html, "text/html", SNConfig.DEFAULT_ENCODING, null);
    }

    public SNElement loadHtml(String baseUrl, String data,
                              String mimeType, String encoding, String historyUrl) {
        if (elem instanceof WebView) {
            WebView webView = (WebView) elem;
            webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        } else if (elem instanceof SNWebView) {
            SNWebView webView = (SNWebView) elem;
            webView.loadHtml(baseUrl, data, mimeType, encoding, historyUrl);
        }
        return this;
    }

    public SNElement getWebView() {
        if (elem instanceof SNWebView) {
            SNWebView webView = (SNWebView) elem;
            return webView.getWebView();
        } else {
            return null;
        }
    }

    public SNElement getProgress(String html) {
        if (elem instanceof SNWebView) {
            SNWebView webView = (SNWebView) elem;
            return webView.getProgress();
        } else {
            return null;
        }
    }
    //endregion

    //region wheelview
    public int wheelCurrentItem() {
        if (elem instanceof WebView) {
            WheelView wheelView = (WheelView) elem;
            return wheelView.getCurrentItem();
        }
        return 0;
    }

    public SNElement wheelCurrentItem(int index) {
        if (elem instanceof WebView) {
            WheelView wheelView = (WheelView) elem;
            wheelView.setCurrentItem(index);
        }
        return this;
    }

    public SNElement wheelAdapter(WheelViewAdapter viewAdapter) {
        if (elem instanceof WebView) {
            WheelView wheelView = (WheelView) elem;
            wheelView.setViewAdapter(viewAdapter);
        }
        return this;
    }

    public SNElement wheelScrolling(OnWheelScrollListener onWheelScrollListener) {
        if (elem instanceof WebView) {
            WheelView wheelView = (WheelView) elem;
            wheelView.addScrollingListener(onWheelScrollListener);
        }
        return this;
    }

    public SNElement wheelChanging(OnWheelChangedListener onWheelChangedListener) {
        if (elem instanceof WebView) {
            WheelView wheelView = (WheelView) elem;
            wheelView.addChangingListener(onWheelChangedListener);
        }
        return this;
    }
    //endregion

    //region button and textview

    public SNElement textChanged(TextWatcher watcher) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.addTextChangedListener(watcher);
        }
        return this;
    }

    /**
     * Gravity
     *
     * @return
     */
    public int gravity() {

        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            return textView.getGravity();
        }
        return 0;
    }

    /**
     * @param gravity Gravity.CENTER
     * @return
     */
    public SNElement gravity(int gravity) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setGravity(gravity);
        }
        return this;
    }

    /**
     * 获取TextView文本
     *
     * @return
     */
    public String text() {

        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            return textView.getText().toString();
        }
        return "";
    }

    /**
     * 设置TextView文本
     *
     * @param text
     * @return
     */
    public SNElement text(CharSequence text) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setText(text);
        }
        return this;
    }

    /**
     * 设置TextView文本
     *
     * @param text
     * @return
     */
    public SNElement text(String text) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setText(text);
        }
        return this;
    }

    /**
     * 设置TextView文本
     *
     * @param resid res id
     * @return
     */
    public SNElement text(int resid) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setText(resid);
        }
        return this;
    }

    /**
     * 设置TextView文本颜色
     *
     * @param resId res id
     * @return
     */
    public SNElement textColorResId(int resId) {
        return textColor(colorResId(resId));
    }

    /**
     * 设置TextView文本颜色
     *
     * @param color 颜色值
     * @return
     */
    public SNElement textColor(int color) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setTextColor(color);
        }
        return this;
    }


    public SNElement textSize(float size) {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.setTextSize(size);
        }
        return this;
    }

    public float textSize() {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            textView.getTextSize();
        }
        return 0;
    }


    //endregion

    //region image view

    /**
     * get ImageView max width
     *
     * @return max width
     */
    public int maxWidth() {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            return imageView.getMaxWidth();
        }
        return 0;
    }

    /**
     * get ImageView max height
     *
     * @return max height
     */
    public int maxHeight() {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            return imageView.getMaxHeight();
        }
        return 0;
    }

    /**
     * set ImageView max width
     *
     * @param val max width
     * @return SNElement
     */
    public SNElement maxWidth(int val) {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            imageView.setMaxWidth(val);
        }
        return this;
    }

    /**
     * set ImageView max height
     *
     * @param val max height
     * @return SNElement
     */
    public SNElement maxHeight(int val) {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            imageView.setMaxHeight(val);
        }
        return this;
    }

    /**
     * set ImageView max size
     *
     * @param size SNSize
     * @return SNElement
     */
    public SNElement maxSize(SNSize size) {
        maxHeight(size.getHeight());
        maxWidth(size.getWidth());
        return this;
    }

    /**
     * 设置ImageView的大小(自适应)
     *
     * @param maxSize SNSize
     * @return
     */
    public SNElement autoSize(SNSize maxSize) {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            imageView.setAdjustViewBounds(true);
            maxSize(maxSize);
        }
        return this;
    }

    /**
     * Set the image of an ImageView.
     *
     * @param resid
     * @return
     */
    public SNElement image(int resid) {

        if (elem instanceof SNImageView) {
            SNImageView iv = (SNImageView) elem;
            if (resid == 0) {
                iv.setImageBitmap(null);
            } else {
                iv.imageResource(resid);
            }
        } else if (elem instanceof ImageView) {
            ImageView iv = (ImageView) elem;
            if (resid == 0) {
                iv.setImageBitmap(null);
            } else {
                iv.setImageResource(resid);
            }
        }
        return this;
    }

    /**
     * Set the image of an ImageView.
     *
     * @param drawable
     * @return
     */
    public SNElement image(Drawable drawable) {
        if (elem instanceof ImageView) {
            ImageView iv = (ImageView) elem;
            if (drawable != null)
                iv.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * Set the image of an ImageView.
     *
     * @param bm
     * @return
     */
    public SNElement image(Bitmap bm) {
        if (elem instanceof SNImageView) {
            SNImageView iv = (SNImageView) elem;
            if (bm != null)
                iv.imageBitmap(bm);
        } else if (elem instanceof ImageView) {
            ImageView iv = (ImageView) elem;
            if (bm != null)
                iv.setImageBitmap(bm);
        }
        return this;
    }

    public SNElement image(final String url, int def_resource, final int err_resource, SNOnSetImageListenter onSetImageListenter, final SNOnGetImageUrlListener onGetImageUrlListener, final SNOnLoadImageFinishListener onLoadImageFinishListener) {
        if (def_resource != 0)
            image(def_resource);
        if (util.strIsNullOrEmpty(url)) {
            if (def_resource != 0)
                image(def_resource);
        } else {
            loadImage(url, onSetImageListenter, new SNOnImageLoadListener() {
                @Override
                public void onSuccess(Bitmap map) {
                    if (onGetImageUrlListener != null) {
                        String u = onGetImageUrlListener.onGetRealUrl();
                        if (u != null && u.equals(url)) image(map);
                    } else {
                        image(map);
                    }
                    if (onLoadImageFinishListener != null) onLoadImageFinishListener.onFinish(map);
                }

                @Override
                public void onFailure() {
                    if (err_resource != 0) image(err_resource);
                    if (onLoadImageFinishListener != null) onLoadImageFinishListener.onFinish(null);
                }
            });
        }
        return this;
    }


    public SNElement image(String url, SNOnLoadImageFinishListener onLoadImageFinishListener) {
        image(url, 0, 0, null, null, onLoadImageFinishListener);
        return this;
    }

    public SNElement image(String url) {
        image(url, 0, 0, null, null, null);
        return this;
    }

    public SNElement image(String url, int def_redId) {
        image(url, def_redId, 0, null, null, null);
        return this;
    }

    public SNElement image(String url, int def_redId, final SNOnSetImageListenter onSetImageListenter) {
        image(url, def_redId, 0, onSetImageListenter, null, null);
        return this;
    }

    public SNElement image(String url, int def_redId, final SNOnSetImageListenter onSetImageListenter, final SNOnGetImageUrlListener onGetImageUrlListener) {
        image(url, def_redId, 0, onSetImageListenter, onGetImageUrlListener, null);
        return this;
    }

    public SNElement image(String url, int def_redId, final SNOnSetImageListenter onSetImageListenter, SNOnLoadImageFinishListener onLoadImageFinishListener) {
        image(url, def_redId, 0, onSetImageListenter, null, onLoadImageFinishListener);
        return this;
    }

    public SNElement image(String url, int def_redId, final int err_resId) {
        image(url, def_redId, err_resId, null, null, null);
        return this;
    }


    public SNElement image(String url, int def_redId, final int err_resId, final SNOnSetImageListenter onSetImageListenter) {
        image(url, def_redId, err_resId, onSetImageListenter, null, null);
        return this;
    }

    public SNElement adjustViewBounds(boolean val) {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            imageView.setAdjustViewBounds(val);
        }
        return this;
    }

    public boolean adjustViewBounds() {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            return imageView.getAdjustViewBounds();
        }
        return false;
    }

    public SNElement scaleType(ImageView.ScaleType scaleType) {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            imageView.setScaleType(scaleType);
        }
        return this;
    }

    public ImageView.ScaleType scaleType() {
        if (elem != null && elem instanceof ImageView) {
            ImageView imageView = (ImageView) elem;
            return imageView.getScaleType();
        }
        return null;
    }
    //endregion

    // region SNSlipNavigation

    /**
     * 打开SNSlipNavigation
     *
     * @return
     */
    public SNElement openSlipMenu() {
        if (elem != null && elem instanceof SNSlipNavigation) {
            SNSlipNavigation slideMenu = (SNSlipNavigation) elem;
            slideMenu.openMenu();
        } else {
            errorNullOrNotInstance("ViewGroup");
        }
        return this;
    }

    /**
     * 关闭SNSlipNavigation
     *
     * @return
     */
    public SNElement closeSlipMenu() {
        if (elem != null && elem instanceof SNSlipNavigation) {
            SNSlipNavigation slideMenu = (SNSlipNavigation) elem;
            slideMenu.closeMenu();
        } else {
            errorNullOrNotInstance("SNSlipNavigation");
        }
        return this;
    }

    // endregion

    // region listView
    public SNElement lazyLoadImage() {
        if (elem != null) {
            if (elem instanceof AbsListView) {
                AbsListView listView = (AbsListView) elem;
                SNLoadBitmapManager.instance(SNElement.this).unlock();
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        util.logDebug(SNElement.class, "scrollstate==" + scrollState);
                        switch (scrollState) {
                            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                                SNLoadBitmapManager.instance(SNElement.this).lock();
                                break;
                            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                SNLoadBitmapManager.instance(SNElement.this).unlock();
                                break;
                            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                                SNLoadBitmapManager.instance(SNElement.this).lock();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });
            } else {
                errorNullOrNotInstance("ListView");
            }
        }
        return this;
    }

    public SNElement bindListAdapter(final SNManager $, SNXListManager listManager, final int layout_id, final Class injectClass) {
        return bindListAdapter($, listManager.getData(), layout_id, injectClass);
    }

    public SNElement bindListAdapter(SNRefreshManager listManager, final int layout_id, final Class injectClass) {
        return bindListAdapter(this, listManager.getData(), layout_id, injectClass);
    }

    public SNElement bindListAdapter(SNXListManager listManager, final int layout_id, final Class injectClass) {
        return bindListAdapter(listManager.getData(), layout_id, injectClass);
    }

    public SNElement bindListAdapter(List dataSource, final int layout_id, final Class injectClass) {
        return bindListAdapter(this, dataSource, layout_id, injectClass);
    }

    public SNElement bindListAdapter(final SNManager $, List dataSource, final int layout_id, final Class injectClass) {
        bindListAdapter(dataSource, new SNAdapterListener() {
            @Override
            public SNAdapterViewInject onCreateInject(int pos) {
                try {
                    return (SNAdapterViewInject) injectClass.getConstructor(SNElement.class).newInstance($.layoutInflateResId(layout_id));
                } catch (Exception ex) {
                    throw new IllegalStateException("Inject class is must be SNAdapterViewInject.");
                }
            }
        });
        return this;
    }


    public ListAdapter listAdapter() {
        if (elem != null) {
            if (elem instanceof AbsListView) {
                AbsListView temp = (AbsListView) elem;
                ListAdapter adapter = temp.getAdapter();
                return adapter;
            } else {
                errorNullOrNotInstance("AbsListView");
            }
        }
        return null;
    }

    public SNElement bindListAdapter(SNXListManager listManager, SNAdapterListener onLoadView) {
        return bindListAdapter(listManager.getData(), onLoadView);
    }

    /**
     * ListView数据绑定
     *
     * @param dataSource ArrayList dataSource
     * @param onLoadView SNAdapterListener onLoadView call back
     */
    public SNElement bindListAdapter(List dataSource, SNAdapterListener onLoadView) {
        SNAdapter adapter = new SNAdapter(this, dataSource, getContext());
        adapter.onLoadView = onLoadView;
        return bindListAdapter(adapter);
    }

    /**
     * ListView数据绑定
     *
     * @param adapter SNAdapter adapter
     * @return
     */
    public SNElement bindListAdapter(SNAdapter adapter) {
        if (elem != null) {
            if (elem instanceof AbsListView) {
                AbsListView temp = (AbsListView) elem;

                temp.setAdapter(adapter);
            } else {
                errorNullOrNotInstance("AbsListView");
            }
        }
        return this;
    }

    /**
     * 控件的listenter
     *
     * @param listener
     * @return
     */
    public SNElement pullListener(IXListViewListener listener) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.setXListViewListener(listener);
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }

    public SNElement pullListener(SNPullRefreshLayout.SNPullRefreshListener listener) {
        if (elem != null) {
            if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setPullRefreshListener(listener);
            } else {
                errorNullOrNotInstance("SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 开始加载更多
     *
     * @param enable
     * @return
     */
    public SNElement pullLoadEnable(boolean enable) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.setPullLoadEnable(enable);
            }
            if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setLoadMoreEnable(enable);
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 开启下啦刷新
     *
     * @param enable
     * @return
     */
    public SNElement pullRefreshEnable(boolean enable) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.setPullRefreshEnable(enable);
            }
            if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setRefreshEnable(enable);
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 停止刷新
     *
     * @return
     */
    public SNElement pullStopRefresh() {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.stopRefresh();
            } else if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setRefreshState(SNPullRefreshLayout.REFRESH_STATE_NORMAL);
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 停止加载更多
     *
     * @return
     */
    public SNElement pullStopLoadMore() {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.stopLoadMore();
            } else if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setLoadState(SNPullRefreshLayout.LOAD_STATE_NORMAL);
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 设置最后的刷新时间
     *
     * @param time
     * @return
     */
    public SNElement pullRefreshTime(String time) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.setRefreshTime(time);
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }

    /**
     * 停止加载，并显示最后更新时间
     *
     * @param time
     * @return
     */
    public SNElement pullStop(String time) {
        pullStopRefresh();
        pullStopLoadMore();
        if (!util.strIsNullOrEmpty(time)) {
            pullRefreshTime(time);
        }
        return this;
    }

    /**
     * 停止加载
     *
     * @return
     */
    public SNElement pullStop() {
        return pullStop(null);
    }

    /**
     * 加载完成
     *
     * @return
     */
    public SNElement pullLoadFinish() {
        pullStop();
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.loadFinish();
            } else if (elem instanceof SNPullRefreshLayout) {
                pullStopRefresh();
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * 复原listview的状态
     *
     * @return
     */
    public SNElement pullReset() {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.getFooterView().setState(XListViewFooter.STATE_NORMAL);
            } else if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setLoadMoreDone(false);
            } else {
                errorNullOrNotInstance("XListView or SNPullRefreshLayout");
            }
        }
        return this;
    }

    /**
     * set pull hint message
     *
     * @param message
     * @return
     */
    public SNElement pullHintMessage(String message) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.getFooterView().showHintMessage(message);
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }

    public SNElement pullHintMessage(int messageResId) {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.getFooterView().showHintMessage(messageResId);
            } else if (elem instanceof SNPullRefreshLayout) {
                SNPullRefreshLayout temp = (SNPullRefreshLayout) elem;
                temp.setLoadState(SNPullRefreshLayout.LOAD_STATE_ERROR, stringResId(messageResId));
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }

    /**
     * load error
     *
     * @return SNElement
     */
    public SNElement pullLoadError() {
        if (elem != null) {
            if (elem instanceof XListView) {
                XListView temp = (XListView) elem;
                temp.loadError();
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }

    // endregion

    //region NavBar

    /**
     * 显示左侧按钮
     *
     * @param onClickListener
     * @return
     */
    public SNElement showNavLeftButton(int imgResId, SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showLeftButton(imgResId, onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示左侧按钮
     *
     * @return
     */
    public SNElement showNavLeftView(SNElement view) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setLeftView(view);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }


    /**
     * 显示左侧按钮
     *
     * @return
     */
    public SNElement showNavRightView(SNElement view) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setRightView(view);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }


    /**
     * 显示后退
     *
     * @param onClickListener
     * @return
     */
    public SNElement showNavBack(SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showBack(onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    public SNElement showNavBack() {
        showNavBack(new SNOnClickListener() {
            @Override
            public void onClick(SNElement view) {
                getActivity().setResult(getActivity().RESULT_CANCELED);
                getActivity().finish();
            }
        });
        return this;
    }

    /**
     * 显示菜单
     *
     * @param onClickListener
     * @return
     */
    public SNElement showNavMenu(SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showMenu(onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }


    /**
     * 显示右侧图片按钮
     *
     * @param resId
     * @param onClickListener
     * @return
     */
    public SNElement showNavRightImage(int resId, SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showRightImage(resId, onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示发布文章按钮
     *
     * @param onClickListener
     * @return
     */
    public SNElement showNavWrite(SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showRightWriteImage(onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示右侧文字按钮
     *
     * @param title
     * @param onClickListener
     * @return
     */
    public SNElement showNavRightText(String title, SNOnClickListener onClickListener) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).showRightText(title, onClickListener);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示导航logo
     *
     * @param resId
     * @return
     */
    public SNElement showNavLogo(int resId) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setLogo(resId);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示导航logo
     *
     * @param drawable
     * @return
     */
    public SNElement showNavLogo(Drawable drawable) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setLogo(drawable);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 显示标题
     *
     * @param string
     * @return
     */
    public SNElement showNavTitle(String string) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setTitle(string);
            } else {
                Log.e("showNavRightText", "对象必须是SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 设置导航左侧按钮的background
     *
     * @param drawable drawable
     * @return SNElement
     */
    public SNElement navLeftButtonBackground(Drawable drawable) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setLeftButtonBakcground(drawable);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 设置导航左侧按钮的background
     *
     * @param resId resId
     * @return SNElement
     */
    public SNElement navLeftButtonBackground(int resId) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setLeftButtonBakcground(resId);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 设置导航标题的颜色
     *
     * @param resId color res id
     * @return
     */
    public SNElement navTitleColor(int resId) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setTitleColor(resId);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }

    /**
     * 设置导航右侧文字的颜色
     *
     * @param resId color res id
     * @return
     */
    public SNElement navRightTextColor(int resId) {
        if (elem != null) {
            if (elem instanceof SNNavTitleBar) {
                ((SNNavTitleBar) elem).setRightTextColor(resId);
            } else {
                errorNullOrNotInstance("SNNavTitleBar");
            }
        }
        return this;
    }
    //endregion

    //region ViewPager SNSlidingTabBar SNFragmentScrollable SNScrollable SNSlidingTabItem


    public SNElement bedge(int badge) {
        if (elem != null) {
            if (elem instanceof SNHomeBottomTabItem) {
                ((SNHomeBottomTabItem) elem).setBedge(badge);
            } else {
                errorNullOrNotInstance("SNHomeBottomTabItem");
            }
        }
        return this;
    }

    public SNElement pageChange(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (elem != null) {
            if (elem instanceof ViewPager) {
                ((ViewPager) elem).setOnPageChangeListener(onPageChangeListener);
            } else {
                errorNullOrNotInstance("ViewPager");
            }
        }
        return this;
    }


    public SNElement tabListener(SNSlidingTabListener tabListener) {
        if (elem != null) {
            if (elem instanceof SNSlidingTabBar) {
                ((SNSlidingTabBar) elem).setTabListener(tabListener);
            } else {
                errorNullOrNotInstance("ViewPager SNSlidingTabBar");
            }
        }
        return this;
    }


    /**
     * 绑定SNScrollable的内容
     *
     * @param elements SNElement集合
     * @return SNElement
     */


    public SNElement bindScrollable(List<SNElement> elements) {
        if (elem != null) {
            if (elem instanceof SNScrollable) {
                ((SNScrollable) elem).bindContent(elements);
            } else {
                errorNullOrNotInstance("SNScrollable");
            }
        }
        return this;
    }

    public int pageSize() {
        if (elem != null) {
            if (elem instanceof ViewPager) {
                return ((ViewPager) elem).getAdapter().getCount();
            } else {
                errorNullOrNotInstance("ViewPager");
            }
        }
        return 0;
    }


    public Fragment contentItem(int i) {
        if (elem != null) {
            if (elem instanceof SNSlidingTabBar) {
                return ((SNSlidingTabBar) elem).getContentItem(i);
            } else {
                errorNullOrNotInstance("SNSlidingTabBar");
            }
        }
        return null;
    }

    public <T> T contentItem(Class<T> _class, int i) {
        if (elem != null) {
            if (elem instanceof SNSlidingTabBar) {
                return ((SNSlidingTabBar) elem).getContentItem(_class, i);
            } else {
                errorNullOrNotInstance("SNSlidingTabBar");
            }
        }
        return null;
    }

    public int currentItem() {
        if (elem != null) {
            if (elem instanceof ViewPager) {
                return ((ViewPager) elem).getCurrentItem();
            } else if (elem instanceof SNSlidingTabBar) {
                return ((SNSlidingTabBar) elem).getCurrentItem();
            } else {
                errorNullOrNotInstance("ViewPager");
            }
        }
        return 0;
    }


    public SNElement currentItem(int item) {
        if (elem != null) {
            if (elem instanceof ViewPager) {
                ((ViewPager) elem).setCurrentItem(item);
            } else if (elem instanceof SNSlidingTabBar) {
                ((SNSlidingTabBar) elem).setCurrentItem(item);
            } else {
                errorNullOrNotInstance("ViewPager SNSlidingTabBar");
            }
        }
        return this;
    }

    public SNElement currentItem(int item, boolean animated) {
        if (elem != null) {
            if (elem instanceof ViewPager) {
                ((ViewPager) elem).setCurrentItem(item, animated);
            } else if (elem instanceof SNSlidingTabBar) {
                ((SNSlidingTabBar) elem).setCurrentItem(item, animated);
            } else {
                errorNullOrNotInstance("ViewPager SNSlidingTabBar");
            }
        }
        return this;
    }

    public SNElement bindData(FragmentManager manager, List<Fragment> list, int selectItem) {
        if (elem != null) {
            if (elem instanceof SNFragmentScrollable) {
                ((SNFragmentScrollable) elem).bindData(manager, list, selectItem);
            } else {
                errorNullOrNotInstance("SNFragmentScrollable");
            }
        }
        return this;
    }

    //ednregion
    // endregion

    // region animate

    /**
     * 执行动画
     *
     * @param animation
     * @param duration
     * @param animationListener
     */
    public SNElement animate(Animation animation, long duration, final SNAnimationListener animationListener,
                             final boolean isClearAnimate) {
        if (animationListener != null) {
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub
                    animationListener.onAnimationStart(SNElement.this, arg0);
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub
                    animationListener.onAnimationRepeat(SNElement.this, arg0);
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    // TODO Auto-generated method stub
                    if (isClearAnimate) {
                        SNElement.this.clearAnimate();
                    }
                    animationListener.onAnimationEnd(SNElement.this, arg0);
                }
            });
        }
        animation.setFillAfter(true);
        animation.setDuration(duration);
        this.elem.startAnimation(animation);
        return this;
    }

    /**
     * 清除所有动画
     */
    public SNElement clearAnimate() {
        elem.clearAnimation();
        return this;
    }

    /**
     * 滑动
     *
     * @param fromX
     * @param toX
     * @param fromY
     * @param toY
     * @param duration
     * @param animationListener
     */
    public SNElement slide(int fromX, int toX, int fromY, int toY, long duration,
                           final SNAnimationListener animationListener, final boolean isClearAnimate) {
        Animation animation = new TranslateAnimation(fromX, toX, fromY, toY);
        animate(animation, duration, animationListener, isClearAnimate);
        return this;
    }

    /**
     * 左右滑动
     *
     * @param toX
     * @param duration
     * @param animationListener
     */
    public SNElement slideLeft(int toX, long duration, final SNAnimationListener animationListener,
                               final boolean isClearAnimate) {

        Animation animation = new TranslateAnimation(0, toX, 0, 0);
        animate(animation, duration, animationListener, isClearAnimate);
        return this;
    }

    /**
     * 上下滑动
     *
     * @param toY
     * @param duration
     * @param animationListener
     */
    public SNElement slideDown(int toY, long duration, final SNAnimationListener animationListener,
                               final boolean isClearAnimate) {
        Animation animation = new TranslateAnimation(0, 0, 0, toY);
        animate(animation, duration, animationListener, isClearAnimate);
        return this;
    }

    /**
     * 渐变
     *
     * @param fromOpacity
     * @param toOpacity
     * @param duration
     * @param animationListener
     */
    public SNElement fade(float fromOpacity, float toOpacity, long duration,
                          final SNAnimationListener animationListener, Boolean isClearAnimation) {
        Animation animation = new AlphaAnimation(fromOpacity, toOpacity);
        animate(animation, duration, animationListener, isClearAnimation);
        return this;
    }

    public SNElement opacity(float opacity) {
        Animation animation = new AlphaAnimation(0, opacity);
        animate(animation, 0, null, false);
        return this;
    }

    /**
     * 淡入
     *
     * @param duration
     * @param animationListener
     */
    public SNElement fadeIn(float toOpacity, long duration, final SNAnimationListener animationListener) {
        Animation animation = new AlphaAnimation(0, toOpacity);
        animate(animation, duration, new SNAnimationListener() {
            @Override
            public void onAnimationStart(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                view.visible(SNManager.SN_UI_VISIBLE);
                if (animationListener != null) {
                    animationListener.onAnimationStart(view, animation);
                }

            }

            @Override
            public void onAnimationRepeat(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                if (animationListener != null) {
                    animationListener.onAnimationRepeat(view, animation);
                }
            }

            @Override
            public void onAnimationEnd(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                if (animationListener != null) {
                    animationListener.onAnimationEnd(view, animation);
                }
            }
        }, false);
        return this;
    }

    /**
     * 淡出
     *
     * @param opacity           当前的opacity
     * @param duration
     * @param animationListener
     * @return
     */
    public SNElement fadeOut(float opacity, long duration, final SNAnimationListener animationListener) {
        Animation animation = new AlphaAnimation(opacity, 0);
        animate(animation, duration, new SNAnimationListener() {
            @Override
            public void onAnimationStart(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                if (animationListener != null) {
                    animationListener.onAnimationStart(view, animation);
                }
            }

            @Override
            public void onAnimationRepeat(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                if (animationListener != null) {
                    animationListener.onAnimationRepeat(view, animation);
                }
            }

            @Override
            public void onAnimationEnd(SNElement view, Animation animation) {
                // TODO Auto-generated method stub
                view.visible(SNManager.SN_UI_NONE);
                if (animationListener != null) {
                    animationListener.onAnimationEnd(view, animation);
                }
            }
        }, true);
        return this;
    }

    // endregion

    //region progressbar
    public SNElement progressMax(int max) {
        if (elem != null && elem instanceof ProgressBar) {
            toView(ProgressBar.class).setMax(max);
        }
        return this;
    }

    public SNElement progress(int val) {
        return progress(val, true);
    }

    public SNElement progress(int val, boolean animated) {
        if (elem != null && elem instanceof ProgressBar) {
            ProgressBarManager.instance(this).progress(val, animated);
        }
        return this;
    }
    //endregion


    //region refresh
    public SNElement refreshStop() {
        if (elem != null) {
            if (elem instanceof XRefreshView) {
                XRefreshView temp = (XRefreshView) elem;

                temp.stopRefresh();
            }
        }
        return this;
    }

    public SNElement loadStop() {
        if (elem != null) {
            if (elem instanceof XRefreshView) {
                XRefreshView temp = (XRefreshView) elem;
                temp.stopLoadMore();
            }
        }
        return this;
    }

    public SNElement loadDone() {
        if (elem != null) {
            if (elem instanceof XRefreshView) {
                XRefreshView temp = (XRefreshView) elem;
                temp.setLoadComplete(true);
            }
        }
        return this;
    }


    public SNElement loadMoreEnable(boolean enable) {
        if (elem != null) {
            if (elem instanceof XRefreshView) {
                XRefreshView temp = (XRefreshView) elem;
                temp.setPullLoadEnable(enable);
            }
        }
        return this;
    }
    //endregion

}
