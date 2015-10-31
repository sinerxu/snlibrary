package com.sn.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sn.controlers.SNNavTitleBar;
import com.sn.controlers.SNScrollable;
import com.sn.controlers.SNSlipNavigation;
import com.sn.interfaces.SNAdapterListener;
import com.sn.interfaces.SNAdapterOnItemClickListener;
import com.sn.interfaces.SNAnimationListener;
import com.sn.interfaces.SNOnClickListener;
import com.sn.interfaces.SNOnLongClickListener;
import com.sn.interfaces.SNOnTouchListener;
import com.sn.models.SNMargins;
import com.sn.models.SNSize;
import com.sn.models.SNViewHolder;
import com.sn.override.SNAdapter;
import com.sn.util.SNUtility;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

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
    SNAdapter listViewAdapter;
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
        if (elem != null && elem instanceof ListView) {
            ListView view = (ListView) elem;
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (onItemClickListener != null) {
                        SNViewHolder holder = (SNViewHolder) view.getTag();
                        holder.id = id;
                        holder.pos = position;
                        holder.parent = parent;
                        onItemClickListener.onItemClick(holder);
                    }
                }
            });
        }
        return this;
    }

    /**
     * 主动触发itemClick
     *
     * @param holder
     * @return
     */
    public SNElement itemClick(SNViewHolder holder) {
        if (elem != null && elem instanceof ListView) {
            ListView view = (ListView) elem;
            view.performItemClick(holder.view.toView(), holder.pos, holder.id);
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

    // region others

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
     * 获取SNAdapter适配器
     *
     * @return
     */
    public SNAdapter getListViewAdapter() {
        return listViewAdapter;
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

    // region style

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
                Context context = getContext();

                if (n > 0 && dip) {
                    n = px(n);
                }

                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }

                this.elem.setLayoutParams(lp);
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
     * 获取TextView文本
     *
     * @return
     */
    public String text() {
        if (elem instanceof TextView) {
            TextView textView = (TextView) elem;
            return (String) textView.getText();
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
            } else {
                elem.setBackgroundDrawable(null);
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
            elem.setBackgroundDrawable(drawable);
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
        maxHeight(size.height);
        maxWidth(size.width);
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
        if (elem instanceof ImageView) {
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
        if (elem instanceof ImageView) {
            ImageView iv = (ImageView) elem;
            iv.setImageBitmap(bm);
        }
        return this;
    }

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
            slideMenu.closeMenu(slideMenu.mDefaultSpeed);
        } else {
            errorNullOrNotInstance("SNSlipNavigation");
        }
        return this;
    }

    // endregion

    // region listView

    /**
     * ListView数据绑定
     *
     * @param dataSource ArrayList dataSource
     * @param onLoadView SNAdapterListener onLoadView call back
     */
    public SNElement bindListAdapter(ArrayList dataSource, SNAdapterListener onLoadView) {
        SNAdapter adapter = new SNAdapter(dataSource, getContext());
        adapter.onLoadView = onLoadView;
        return bindListAdapter(dataSource, adapter);
    }

    /**
     * ListView数据绑定
     *
     * @param dataSource ArrayList dataSource
     * @param adapter    SNAdapter adapter
     * @return
     */
    public SNElement bindListAdapter(ArrayList dataSource, SNAdapter adapter) {
        if (elem != null) {
            if (elem instanceof ListView) {
                ListView temp = (ListView) elem;
                this.listViewAdapter = adapter;
                temp.setAdapter(this.listViewAdapter);
            } else {
                errorNullOrNotInstance("ListView");
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
            } else {
                errorNullOrNotInstance("XListView");
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
            } else {
                errorNullOrNotInstance("XListView");
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
            } else {
                errorNullOrNotInstance("XListView");
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
            } else {
                errorNullOrNotInstance("XListView");
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
        if (!SNUtility.isNullOrEmpty(time)) {
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
            } else {
                errorNullOrNotInstance("XListView");
            }
        }
        return this;
    }



    /**
     * set pull hint message
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
        showNavLogo(drawableResId(resId));
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

    //region Scrollable

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
    //endregion
}
