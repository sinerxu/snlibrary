package com.sn.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sn.activity.SNLoadingActivity;

import com.sn.annotation.SNIOC;
import com.sn.annotation.SNInjectView;
import com.sn.interfaces.SNOnClickListener;
import com.sn.interfaces.SNOnHttpResultListener;
import com.sn.lib.R;
import com.sn.models.SNSize;
import com.sn.postting.alert.SNAlert;
import com.sn.util.SNBindInjectManager;
import com.sn.util.SNUtility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * @author Siner QQ348078707
 */
public class SNManager extends SNConfig {

    Activity activity;
    Context context;

    public SNManager(Context context) {
        this.activity = (Activity) context;
        this.context = context;
    }

    public static SNManager instence(Context context) {
        return new SNManager(context);
    }

    // region message alert

    /**
     * show alert
     *
     * @param title           title
     * @param msg             message
     * @param buttonTitle     title of button
     * @param onClickListener listener of after click
     */
    public void alert(String title, String msg, String buttonTitle, SNOnClickListener onClickListener) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).alert(title, msg, buttonTitle, onClickListener);
    }

    /**
     * 弹出提示
     *
     * @param title 标题
     * @param msg   消息
     */
    public void alert(String title, String msg) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).alert(title, msg);
    }

    /**
     * 弹出提示
     *
     * @param msg             消息
     * @param onClickListener 点击按钮后的事件
     */
    public void alert(String msg, SNOnClickListener onClickListener) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).alert(msg, onClickListener);
    }

    /**
     * 弹出提示
     *
     * @param msg 消息
     */
    public void alert(String msg) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).alert(msg);
    }

    /**
     * 弹出确认提示
     *
     * @param title          标题
     * @param msg            消息
     * @param btnOkTitle     第一个按钮标题
     * @param btnCancelTitle 第二个按钮标题
     * @param okClick        点击第一个按钮事件
     * @param cancelClick    点击第二个按钮事件
     */
    public void confirm(String title, String msg, String btnOkTitle, String btnCancelTitle,
                        SNOnClickListener okClick, SNOnClickListener cancelClick) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).confirm(title, msg, btnOkTitle, btnCancelTitle, okClick, cancelClick);
    }

    /**
     * 弹出确认提示
     *
     * @param msg         消息
     * @param okClick     点击第一个按钮事件
     * @param cancelClick 点击第二个按钮事件
     */
    public void confirm(String msg, SNOnClickListener okClick,
                        SNOnClickListener cancelClick) {
        SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE).confirm(msg, okClick, cancelClick);
    }

    /**
     * 现实信息提示
     *
     * @param text     内容
     * @param duration 几秒后消失
     */
    public void toast(String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    /**
     * 弹出加载层，用户等待
     */
    public void openLoading() {
        startActivity(SNLoadingActivity.class, SNManager.SN_ANIMATE_ACTIVITY_NO);
    }

    /**
     * 关闭加载层，用户结束等待
     */
    public void closeLoading() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                SNLoadingActivity.currentLoadingActivity.close();
            }
        };
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    if (SNLoadingActivity.currentLoadingActivity != null) {
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }
        }.start();

    }

    // endregion

    // region View

    /**
     * create SNElement by View
     *
     * @param view view object
     * @return SNElement
     */
    public SNElement create(View view) {
        SNElement $view = new SNElement(view);
        return $view;
    }

    /**
     * findFragmentById
     *
     * @param c     class type
     * @param resId layout id
     * @param <T>   Fragment class type
     * @return Fragment
     */
    public <T> T createFragment(Class<T> c, int resId) {
        T t = null;
        if (activity instanceof FragmentActivity) {
            t = (T) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentById(resId);
        }
        return t;
    }

    /**
     * create SNElement by View id
     *
     * @param id view id
     * @return
     */
    public SNElement create(int id) {
        return create(findView(id));
    }

    /**
     * activity.findViewById
     *
     * @param id view id
     * @return
     */
    public View findView(int id) {
        return activity.findViewById(id);
    }

    /**
     * activity.findViewById
     *
     * @param viewClass view class
     * @param id        view id
     * @param <T>
     * @return
     */
    public <T> T findView(Class<T> viewClass, int id) {
        return (T) findView(id);
    }


    /**
     * activity.setContentView(view,layoutParams) ,support view inject
     *
     * @param element      SNElement
     * @param layoutParams LayoutParams
     * @param _holder      view inject holder object，refer to : document->view inject
     */
    public void contentView(SNElement element, LayoutParams layoutParams, Object _holder) {
        activity.setContentView(element.toView(), layoutParams);
        inject(_holder);
    }

    /**
     * activity.setContentView(view,layoutParams) ,support view inject
     *
     * @param element SNElement
     * @param _holder view inject holder object，refer to : document->view inject
     */
    public void contentView(SNElement element, Object _holder) {
        activity.setContentView(element.toView());
        inject(_holder);
    }

    /**
     * activity.setContentView(view,layoutParams)
     *
     * @param element      SNElement
     * @param layoutParams LayoutParams
     */
    public void contentView(SNElement element, LayoutParams layoutParams) {
        activity.setContentView(element.toView(), layoutParams);
    }

    /**
     * ctivity.setContentView(view)
     *
     * @param element SNElement
     */
    public void contentView(SNElement element) {
        activity.setContentView(element.toView());
    }

    /**
     * activity.setContentView(resId)
     *
     * @param layoutResID layout id
     */
    public void contentView(int layoutResID) {
        contentView(layoutResID, null);
    }

    /**
     * activity ContentView,support view inject
     *
     * @param layoutResID layout id
     * @param _holder     view inject holder object，refer to : document->view inject
     */
    public void contentView(int layoutResID, Object _holder) {
        activity.setContentView(layoutResID);
        inject(_holder);
    }

    /**
     * view inject, refer to : document->view inject
     *
     * @param _holder view inject holder object,refer to : document->view inject
     */
    public void inject(Object _holder) {
        if (_holder != null) {
            Field[] fields = _holder.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (field.isAnnotationPresent(SNInjectView.class)) {
                    try {
                        SNInjectView bindId = (SNInjectView) field.getAnnotation(SNInjectView.class);
                        field.setAccessible(true);
                        field.set(_holder, create(bindId.id()));
                    } catch (Exception ex) {

                    }
                } else if (field.isAnnotationPresent(SNIOC.class)) {

                    SNIOC ioc = (SNIOC) field.getAnnotation(SNIOC.class);
                    Class c = field.getType();
                    Class t = SNBindInjectManager.instance().to(c);
                    if (t != null) {
                        try {
                            Constructor c1 = t.getDeclaredConstructor(SNManager.class);
                            c1.setAccessible(true);
                            Object obj = c1.newInstance(this);
                            field.setAccessible(true);
                            field.set(_holder, obj);
                        } catch (Exception ex) {
                            throw new IllegalStateException("IOC class constructor parameter must be SNManager class.");
                        }
                    }
                } else {
                    int id = resourceId(fieldName);
                    if (id != 0) {
                        field.setAccessible(true);
                        try {
                            field.set(_holder, create(id));
                        } catch (Exception ex2) {

                        }
                    }
                }
            }
        }

    }
    // endregion

    // region Activity

    /**
     * start activity
     *
     * @param c activity class
     */
    public void startActivity(Class<?> c) {
        startActivity(c, SNManager.SN_ANIMATE_ACTIVITY_YES);
    }

    /**
     * start activity
     *
     * @param c        activity class
     * @param animated refer to->SNConfig->animate type
     */
    public void startActivity(Class<?> c, int animated) {
        Intent intent = new Intent(context, c);

        this.startActivity(intent);
        activityAnimateType(animated);
    }

    /**
     * start activity
     *
     * @param intent Intent
     */
    public void startActivity(Intent intent) {
        this.activity.startActivity(intent);
    }

    /**
     * start activity
     *
     * @param intent   Intent
     * @param animated refer to->SNConfig->animate type
     */
    public void startActivity(Intent intent, int animated) {
        this.activity.startActivity(intent);
        activityAnimateType(animated);
    }

    /**
     * start activity
     */
    public void finish() {
        finishActivity(SNManager.SN_ANIMATE_ACTIVITY_YES);
    }

    /**
     * finish activity
     *
     * @param animated refer to->SNConfig->animate type
     */
    public void finishActivity(int animated) {

        activity.finish();
        activityAnimateType(animated, true);
    }


    void activityAnimateType(int animated) {
        activityAnimateType(animated, false);
    }

    /**
     * activity animate,after "startActivity" or "finishActivity" call.
     *
     * @param animated
     */
    void activityAnimateType(int animated, boolean isFinish) {
        if (animated == SNManager.SN_ANIMATE_ACTIVITY_NO || animated == SNManager.SN_ANIMATE_ACTIVITY_SN) {
            activity.overridePendingTransition(0, 0);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_FADE) {
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SLIDE) {
            activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_scale_one,
                        R.anim.finish_scale_two);
            else
                activity.overridePendingTransition(R.anim.start_scale_one,
                        R.anim.start_scale_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE_ROTATE) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_scale_rotate_one,
                        R.anim.finish_scale_rotate_two);
            else
                activity.overridePendingTransition(R.anim.start_scale_rotate_one,
                        R.anim.start_scale_rotate_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE_TRANSLATE) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_scale_translate_one,
                        R.anim.finish_scale_translate_two);
            else
                activity.overridePendingTransition(R.anim.start_scale_translate_one,
                        R.anim.start_scale_translate_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_PUSH_POP_HORIZONTAL) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_pop_horizontal_one,
                        R.anim.finish_pop_horizontal_two);
            else
                activity.overridePendingTransition(R.anim.start_push_horizontal_one,
                        R.anim.start_push_horizontal_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_PUSH_POP_VERTICAL) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_push_vertical_one,
                        R.anim.finish_push_vertical_two);
            else
                activity.overridePendingTransition(R.anim.start_push_vertical_one,
                        R.anim.start_push_vertical_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_ZOOM) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_zoom_one,
                        R.anim.finish_zoom_two);
            else
                activity.overridePendingTransition(R.anim.start_zoom_one,
                        R.anim.start_zoom_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_TEST) {
            if (isFinish)
                activity.overridePendingTransition(R.anim.finish_zoom_one,
                        R.anim.finish_zoom_two);
            else
                activity.overridePendingTransition(R.anim.start_zoom_one, R.anim.start_zoom_two);
        }
    }


    /**
     * get package name
     *
     * @return
     */
    public String packageName() {
        return activity.getPackageName();
    }

    public Activity getActivity() {

        return activity;
    }

    /**
     * get activity object
     *
     * @param activityClass
     * @param <T>
     * @return
     */
    public <T> T getActivity(Class<T> activityClass) {
        if (activityClass.isInstance(activity))
            return (T) activity;
        return null;
    }
    // endregion

    //region suport fragment

    /**
     * get support fragment manager
     *
     * @return
     */
    public FragmentManager supportFragmentManager() {
        if (activity instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) activity;
            return a.getSupportFragmentManager();
        }
        return null;
    }
    //endregion

    // region 资源操作

    /**
     * dp to px
     *
     * @param dpValue
     * @return
     */
    public int px(float dpValue) {
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.getResources().getDisplayMetrics());
        return value;
    }

    /**
     * px to dp
     *
     * @param pxValue
     * @return
     */
    public float dip(float pxValue) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = pxValue / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
     * get TypedArray
     *
     * @param attrs  AttributeSet
     * @param resIds styleable
     * @return
     */
    public TypedArray loadStyle(AttributeSet attrs, int[] resIds) {
        TypedArray a = context.obtainStyledAttributes(attrs, resIds);
        return a;
    }

    /**
     * layout Inflate
     *
     * @param resId
     * @param root
     * @return
     */
    public SNElement layoutInflateResId(int resId, ViewGroup root, Object _holder) {
        SNElement r = create(activity.getLayoutInflater().inflate(resId, root));
        r.inject(_holder);
        return r;
    }

    /**
     * layout Inflate
     *
     * @param resId
     * @param root
     * @return
     */
    public SNElement layoutInflateResId(int resId, ViewGroup root) {
        return layoutInflateResId(resId, root, null);
    }

    /**
     * layout Inflate
     *
     * @param resId layout id
     * @param root  root view
     * @return
     */
    public SNElement layoutInflateResId(int resId, ViewGroup root, boolean _attachToRoot) {
        return layoutInflateResId(resId, root, _attachToRoot, null);
    }

    /**
     * layout Inflate
     *
     * @param resId         layout id
     * @param root          root view
     * @param _attachToRoot attach root
     * @param _holder       view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateResId(int resId, ViewGroup root, boolean _attachToRoot, Object _holder) {
        SNElement r = create(activity.getLayoutInflater().inflate(resId, root, _attachToRoot));
        r.inject(_holder);
        return r;
    }

    /**
     * layout Inflate
     *
     * @param resId layout id
     * @return
     */
    public SNElement layoutInflateResId(int resId) {
        return layoutInflateResId(resId, null);
    }

    /**
     * layout Inflate
     *
     * @param resId   layout id
     * @param _holder view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateResId(int resId, Object _holder) {
        SNElement r = create(activity.getLayoutInflater().inflate(resId, null, false));
        r.inject(_holder);
        return r;
    }

    /**
     * layout Inflate
     *
     * @param name layout file name
     * @param root root view
     * @return
     */
    public SNElement layoutInflateName(String name, ViewGroup root) {
        return layoutInflateName(name, root, null);
    }

    /**
     * layout Inflate
     *
     * @param name          layout file name
     * @param root          root view
     * @param _attachToRoot attach to root
     * @return
     */
    public SNElement layoutInflateName(String name, ViewGroup root, boolean _attachToRoot) {
        return layoutInflateName(name, root, _attachToRoot, null);
    }

    /**
     * layout Inflate
     *
     * @param name          layout file name
     * @param root          root view
     * @param _attachToRoot attach to root
     * @param _holder       view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateName(String name, ViewGroup root, boolean _attachToRoot, Object _holder) {
        int resId = resource(name, "layout");
        return layoutInflateResId(resId, root, _attachToRoot, _holder);
    }

    /**
     * layout Inflate
     *
     * @param name    layout file name
     * @param root    root view
     * @param _holder view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateName(String name, ViewGroup root, Object _holder) {
        int resId = resource(name, "layout");
        return layoutInflateResId(resId, root, _holder);
    }

    /**
     * layout Inflate
     *
     * @param name    layout file name
     * @param _holder view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateName(String name, Object _holder) {
        return layoutInflateName(name, null, _holder);
    }

    /**
     * layout Inflate
     *
     * @param name layout file name
     * @return
     */
    public SNElement layoutInflateName(String name) {
        return layoutInflateName(name, null, null);
    }

    /**
     * get resource id
     *
     * @param name     resource file name
     * @param typeName resource type
     * @return
     */
    public int resource(String name, String typeName) {
        String packageName = activity.getPackageName();
        return activity.getResources().getIdentifier(name, typeName, packageName);
    }

    /**
     * get dimen id
     *
     * @param name dimen name
     * @return
     */
    public int resourceDimen(String name) {
        return resource(name, "dimen");
    }

    /**
     * get attr id
     *
     * @param name attr name
     * @return
     */
    public int resourceAttr(String name) {

        return resource(name, "attr");
    }

    /**
     * get color id
     *
     * @param name color name
     * @return
     */
    public int resourceColor(String name) {

        return resource(name, "color");
    }

    /**
     * get id's id
     *
     * @param name id name
     * @return
     */
    public int resourceId(String name) {
        return resource(name, "id");
    }

    /**
     * get string id
     *
     * @param name string name
     * @return
     */
    public int resourceString(String name) {
        return resource(name, "string");
    }

    /**
     * get drawable id
     *
     * @param name drawable name
     * @return
     */
    public int resourceDrawable(String name) {
        return resource(name, "drawable");
    }

    /**
     * get layout id
     *
     * @param name layout file name
     * @return
     */
    public int resourceLayout(String name) {
        return resource(name, "layout");
    }

    /**
     * get current theme's TypeValue
     *
     * @param resid attr'id
     * @return
     */
    public TypedValue themeTypeValue(int resid) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(resid, typedValue, true);
        return typedValue;
    }

    /**
     * get current theme typevalue's id
     *
     * @param resid attr'id
     * @return
     */
    public int themeResourceId(int resid) {
        TypedValue typedValue = themeTypeValue(resid);
        return typedValue.resourceId;
    }

    /**
     * get display size (px)
     *
     * @return
     */
    public SNSize displaySize() {
        Display mDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mDisplay.getMetrics(displayMetrics);
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        return new SNSize(w, h);
    }


    /**
     * get color value by id
     *
     * @param resId color id
     * @return
     */
    public int colorResId(int resId) {
        return activity.getResources().getColor(resId);
    }

    /**
     * get string by id
     *
     * @param resId string id
     * @return String
     */
    public String stringResId(int resId) {
        return activity.getResources().getString(resId);
    }

    /**
     * get drawable object by id
     *
     * @param resId drawable id
     * @return
     */
    public Drawable drawableResId(int resId) {
        return activity.getResources().getDrawable(resId);
    }

    /**
     * get dimen value by id
     *
     * @param resId dimen id
     * @return
     */
    public int dimenResId(int resId) {
        return activity.getResources().getDimensionPixelOffset(resId);
    }

    /**
     * get Resources object
     *
     * @return
     */
    public Resources resources() {
        return activity.getResources();
    }
    // endregion

    // region Sliding Menu

    /**
     * set sliding left view
     *
     * @param element
     * @param layoutParams
     */
    public void slidingLeftView(SNElement element, LayoutParams layoutParams) {
        if (activity instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) activity;
            a.setBehindContentView(element.toView(), layoutParams);
        }
    }

    /**
     * set sliding left view
     *
     * @param element
     */
    public void slidingLeftView(SNElement element) {
        if (activity instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) activity;
            a.setBehindContentView(element.toView());
        }
    }

    /**
     * set sliding left view
     *
     * @param layoutResID
     */
    public void slidingLeftView(int layoutResID) {
        if (activity instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) activity;
            a.setBehindContentView(layoutResID);
        }
    }

    /**
     * set sliding right view
     *
     * @param element
     */
    public void slidingRightView(SNElement element) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setSecondaryMenu(element.toView());
        }
    }

    /**
     * set sliding right view
     *
     * @param layoutResID
     */
    public void slidingRightView(int layoutResID) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setSecondaryMenu(layoutResID);
        }
    }

    /**
     * get SlidingMenu
     *
     * @return SlidingMenu
     */
    public SlidingMenu slidingMenu() {
        if (activity instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) activity;
            return a.getSlidingMenu();
        }
        return null;
    }

    /**
     * set sliding mode, SlidingMenu.LEFT,SlidingMenu.RIGHT,SlidingMenu.LEFT_RIGHT
     *
     * @param mode ：左侧：SlidingMenu.LEFT；右侧：SlidingMenu.RIGHT；左右侧：SlidingMenu.
     *             LEFT_RIGHT；
     */
    public void slidingMode(int mode) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setMode(mode);
        }
    }

    /**
     * touch mode,SlidingMenu.TOUCHMODE_FULLSCREEN,SlidingMenu.TOUCHMODE_MARGIN,SlidingMenu.TOUCHMODE_NONE
     *
     * @param mode ：full touch：SlidingMenu.TOUCHMODE_FULLSCREEN；only margin：SlidingMenu.
     *             TOUCHMODE_MARGIN；3、not allow touch：SlidingMenu.TOUCHMODE_NONE。
     */
    public void slidingTouchModeAbove(int mode) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setTouchModeAbove(mode);
        }
    }

    /**
     * set left shadow
     *
     * @param resId drawable id
     */
    public void slidingLeftShadow(int resId) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setShadowDrawable(resId);
        }
    }

    /**
     * set left shadow
     *
     * @param drawable drawable object
     */
    public void slidingLeftShadow(Drawable drawable) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setShadowDrawable(drawable);
        }
    }

    /**
     * set right shadow
     *
     * @param resId drawable id
     */
    public void slidingRightShadow(int resId) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setSecondaryShadowDrawable(resId);
        }
    }

    /**
     * set right shadow
     *
     * @param drawable drawable object
     */
    public void slidingRightShadow(Drawable drawable) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setSecondaryShadowDrawable(drawable);
        }
    }

    /**
     * set shadow width
     *
     * @param px px value
     */
    public void slidingShadowWidth(int px) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setShadowWidth(px);
        }
    }

    /**
     * set shadow width
     *
     * @param resId dimen id
     */
    public void slidingShadowWidthRes(int resId) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setShadowWidthRes(resId);
        }
    }

    /**
     * set main view width when show slding left or right.
     *
     * @param px px value
     */
    public void slidingOffset(int px) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setBehindOffset(px);
        }
    }

    /**
     * set main view width when show slding left or right.
     *
     * @param resId dimen id
     */
    public void slidingOffsetRes(int resId) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setBehindOffsetRes(resId);
        }
    }

    /**
     * 滑动视图的宽度
     *
     * @param px px value
     */
    public void slidingWidth(int px) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setBehindWidth(px);
        }
    }

    /**
     * 滑动视图的宽度
     *
     * @param resId
     */
    public void slidingWidthRes(int resId) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setBehindWidthRes(resId);
        }
    }

    /**
     * set fade animate value
     *
     * @param f
     */
    public void slidingFade(float f) {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setFadeDegree(f);
        }
    }

    /**
     * shwo left
     */
    public void showSlidingLeft() {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showMenu();
        }
    }

    /**
     * show right
     */
    public void showSlidingRight() {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showSecondaryMenu();
        }
    }

    /**
     * show main
     */
    public void showSlidingContent() {
        if (activity instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showContent();
        }
    }

    // endregion

    //region http request


    /**
     * http post
     *
     * @param url                  url
     * @param bodys                post request body
     * @param contentType          content type, refer to SNConfig->http
     * @param requestHeader        http request header
     * @param onHttpResultListener call back
     */
    public void post(String url, HashMap<String, String> bodys, String contentType, HashMap<String, String> requestHeader,
                     final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = SNUtility.makeAsyncHttpClient(requestHeader, contentType);

        client.post(this.context, url, new RequestParams(bodys), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    onHttpResultListener.onSuccess(statusCode, new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    onHttpResultListener.onFailure(statusCode, new String(responseBody));
                }
            }
        });
    }

    /**
     * http post
     *
     * @param url                  url
     * @param bodys                post request body
     * @param onHttpResultListener call back
     */
    public void post(String url, HashMap<String, String> bodys, SNOnHttpResultListener onHttpResultListener) {
        post(url, bodys, SN_HTTP_REQUEST_CONTENT_TYPE_TEXT, null, onHttpResultListener);
    }

    /**
     * http get
     *
     * @param url                  url
     * @param requestParams        request params
     * @param contentType          content type
     * @param requestHeader        request headers
     * @param onHttpResultListener call back
     */
    public void get(String url, HashMap<String, String> requestParams, String contentType, HashMap<String, String> requestHeader, final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = SNUtility.makeAsyncHttpClient(requestHeader, contentType);
        client.get(url, new RequestParams(requestParams), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    String r = null;
                    try {
                        r = new String(responseBody);
                    } catch (Exception e) {
                        r = null;
                    }
                    onHttpResultListener.onSuccess(statusCode, r);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    String r = null;
                    try {
                        r = new String(responseBody);
                    } catch (Exception e) {
                        r = null;
                    }
                    onHttpResultListener.onFailure(statusCode, r);
                }
            }
        });
    }

    /**
     * http get
     *
     * @param url                  url
     * @param requestHeader        request headers
     * @param onHttpResultListener call back
     */
    public void get(String url, HashMap<String, String> requestHeader, final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = SNUtility.makeAsyncHttpClient(requestHeader, SNConfig.SN_HTTP_REQUEST_CONTENT_TYPE_TEXT);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    String r = null;
                    try {
                        r = new String(responseBody);
                    } catch (Exception e) {
                        r = null;
                    }
                    onHttpResultListener.onSuccess(statusCode, r);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    String r = null;
                    try {
                        r = new String(responseBody);
                    } catch (Exception e) {
                        r = null;
                    }
                    onHttpResultListener.onFailure(statusCode, r);
                }
            }
        });
    }

    /**
     * http get
     *
     * @param url                  url
     * @param onHttpResultListener call back
     */
    public void get(String url, final SNOnHttpResultListener onHttpResultListener) {
        get(url, null, onHttpResultListener);
    }
    //endregion


}
