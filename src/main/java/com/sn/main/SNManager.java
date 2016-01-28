package com.sn.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sn.annotation.SNIOC;
import com.sn.annotation.SNInjectElement;
import com.sn.core.SNAppEventListenerManager;
import com.sn.core.SNBindInjectManager;
import com.sn.core.SNLoadBitmapManager;
import com.sn.core.SNLoadingDialogManager;
import com.sn.core.SNUtility;
import com.sn.interfaces.SNAppEventListener;
import com.sn.interfaces.SNOnClickListener;
import com.sn.interfaces.SNOnHttpResultListener;
import com.sn.interfaces.SNOnImageLoadListener;
import com.sn.interfaces.SNOnSetImageListenter;
import com.sn.lib.R;
import com.sn.models.SNInject;
import com.sn.models.SNSize;
import com.sn.postting.alert.SNAlert;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


/**
 * @author Siner QQ348078707
 */
public class SNManager extends SNConfig {

    Object manager;
    Context context;
    public SNUtility util;

    public SNManager(Object manager) {
        this.manager = manager;
        if (manager instanceof Activity) {
            Activity _activity = (Activity) manager;
            this.context = (Context) _activity;
        } else if (manager instanceof Dialog) {
            Dialog dialog = (Dialog) manager;
            this.context = dialog.getContext();
        } else if (manager instanceof Context) {
            this.context = (Context) manager;
        }
        util = SNUtility.instance();
    }

    public SNManager(Dialog dialog, Context context) {
        this.manager = dialog;
        this.context = context;
        util = SNUtility.instance();
    }

    public static SNManager instence(Object manager) {
        return new SNManager(manager);
    }

    public static SNManager instence(Dialog dialog, Context context) {
        return new SNManager(dialog, context);
    }

    // region message


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
        SNLoadingDialogManager.instance(context).show();
    }

    /**
     * 关闭加载层，用户结束等待
     */
    public void closeLoading() {
        SNLoadingDialogManager.instance(context).close();
    }

    // endregion

    //region App
    public boolean appOnForeground() {
        Context context = getContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RecentTaskInfo> appTask = activityManager.getRecentTasks(Integer.MAX_VALUE, 1);
        if (appTask == null) {
            return false;
        }
        if (appTask.get(0).baseIntent.toString().contains(packageName)) {
            return true;
        }
        return false;
    }

    /**
     * get app version
     *
     * @return
     */
    public String appVersion() {
        try {

            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);

            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String deviceCode() {

        TelephonyManager TelephonyMgr = (TelephonyManager) getContext()
                .getSystemService(getContext().TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId();
        String m_szDevIDShort = "35"
                + // we make this look like a valid IMEI

                Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10; // 13 digits
        String m_szAndroidID = Settings.Secure.getString(getContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);

        // WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        // String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        // BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth
        // adapter
        // m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // String m_szBTMAC = m_BluetoothAdapter.getAddress();

        String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }


    /**
     * 移除
     *
     * @param key
     */

    public void removeAppEventListener(String key) {
        SNAppEventListenerManager.instance().remove(key);
    }

    /**
     * 设置
     *
     * @param key
     */
    public void setAppEventListener(String key, SNAppEventListener appEventListener) {
        SNAppEventListenerManager.instance().set(key, appEventListener);
    }

    /**
     * 执行
     *
     * @param key
     * @return
     */

    public void fireAppEventListener(String key, HashMap<String, Object> args) {
        fireAppEventListener(key, args, false);
    }

    /**
     * 执行
     *
     * @param key
     * @return
     */

    public void fireAppEventListener(String key) {
        fireAppEventListener(key, null);
    }

    /**
     * 执行
     *
     * @param key
     * @param args
     * @param isRemove @return
     */
    public void fireAppEventListener(String key, HashMap<String, Object> args, boolean isRemove) {
        SNAppEventListenerManager.instance().fire(key, args, isRemove);
    }

    //endregion

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
        if (getActivity() instanceof FragmentActivity) {
            t = (T) ((FragmentActivity) getActivity()).getSupportFragmentManager().findFragmentById(resId);
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
        if (manager instanceof Activity)
            return getActivity().findViewById(id);
        else if (manager instanceof Dialog)
            return getDialog().findViewById(id);
        return null;
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
    public void contentView(SNElement element, LayoutParams layoutParams, SNInject _holder) {
        if (manager instanceof Activity)
            getActivity().setContentView(element.toView(), layoutParams);
        else if (manager instanceof Dialog)
            getDialog().setContentView(element.toView(), layoutParams);
        inject(_holder);
    }

    /**
     * activity.setContentView(view,layoutParams) ,support view inject
     *
     * @param element SNElement
     * @param _holder view inject holder object，refer to : document->view inject
     */
    public void contentView(SNElement element, SNInject _holder) {
        if (manager instanceof Activity)
            getActivity().setContentView(element.toView());
        else if (manager instanceof Dialog)
            getDialog().setContentView(element.toView());
        inject(_holder);
    }

    /**
     * activity.setContentView(view,layoutParams)
     *
     * @param element      SNElement
     * @param layoutParams LayoutParams
     */
    public void contentView(SNElement element, LayoutParams layoutParams) {
        if (manager instanceof Activity)
            getActivity().setContentView(element.toView(), layoutParams);
        else if (manager instanceof Dialog)
            getDialog().setContentView(element.toView(), layoutParams);
    }

    /**
     * ctivity.setContentView(view)
     *
     * @param element SNElement
     */
    public void contentView(SNElement element) {
        if (manager instanceof Activity)
            getActivity().setContentView(element.toView());
        else if (manager instanceof Dialog)
            getDialog().setContentView(element.toView());
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
    public void contentView(int layoutResID, SNInject _holder) {
        if (manager instanceof Activity)
            getActivity().setContentView(layoutResID);
        else if (manager instanceof Dialog)
            getDialog().setContentView(layoutResID);
        inject(_holder);
    }

    /**
     * view inject, refer to : document->view inject
     *
     * @param _holder view inject holder object,refer to : document->view inject
     */
    public void inject(SNInject _holder) {
        if (_holder != null) {
            _holder.$ = this;
            _holder.onInjectStart();
            Field[] fields = _holder.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                util.logInfo(SNManager.class, fieldName);
                if (field.isAnnotationPresent(SNInjectElement.class)) {
                    try {
                        SNInjectElement bindId = (SNInjectElement) field.getAnnotation(SNInjectElement.class);
                        field.setAccessible(true);
                        field.set(_holder, create(bindId.id()));
                    } catch (Exception ex) {

                    }
                } else if (field.isAnnotationPresent(SNIOC.class)) {
                    SNIOC ioc = (SNIOC) field.getAnnotation(SNIOC.class);
                    Class c = field.getType();
                    Class t = SNBindInjectManager.instance().to(c);
                    if (t != null) {
                        util.logInfo(SNManager.class, "t != null");
                        try {
                            util.logInfo(SNManager.class, "getDeclaredConstructor start==" + t.getName());
                            Constructor c1 = t.getDeclaredConstructor(SNManager.class);
                            util.logInfo(SNManager.class, "getDeclaredConstructor" + c1);
                            c1.setAccessible(true);
                            util.logInfo(SNManager.class, "setAccessible success");
                            Object obj = c1.newInstance(this);
                            util.logInfo(SNManager.class, "newInstance success");
                            field.setAccessible(true);
                            util.logInfo(SNManager.class, "setAccessible success");
                            field.set(_holder, obj);
                            util.logInfo(SNManager.class, "field set");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            throw new IllegalStateException("IOC class constructor parameter must be SNManager class." + ex.getMessage());
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
            _holder.onInjectFinish();
        }

    }

    /**
     * ioc
     *
     * @param _holder
     */
    public void injectIOC(Object _holder) {
        if (_holder != null) {
            Field[] fields = _holder.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (field.isAnnotationPresent(SNIOC.class)) {
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
                            throw new IllegalStateException("IOC class constructor parameter must be SNManager class:" + ex.getMessage());
                        }
                    }
                }
            }
        }
    }

    // endregion

    // region Activity & Context

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
        Intent intent = new Intent(getContext(), c);
        this.startActivity(intent);
        activityAnimateType(animated);
    }

    /**
     * start activity
     *
     * @param intent Intent
     */
    public void startActivity(Intent intent) {
        this.getContext().startActivity(intent);
    }

    /**
     * start activity
     *
     * @param intent   Intent
     * @param animated refer to->SNConfig->animate type
     */
    public void startActivity(Intent intent, int animated) {
        this.getContext().startActivity(intent);
        activityAnimateType(animated);
    }


    public void startActivityResult(Class<?> c, int requestCode, int animated) {
        Intent intent = new Intent(getContext(), c);
        this.startActivityResult(intent, requestCode);
        activityAnimateType(animated);
    }

    /**
     * start activity
     *
     * @param intent Intent
     */
    public void startActivityResult(Intent intent, int requestCode, int animated) {
        this.startActivityResult(intent, requestCode);
        activityAnimateType(animated);
    }

    /**
     * start activity
     *
     * @param intent Intent
     */
    public void startActivityResult(Intent intent, int requestCode) {
        this.getActivity().startActivityForResult(intent, requestCode);
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

        getActivity().finish();
        activityAnimateType(animated, true);
    }


    public void activityAnimateType(int animated) {
        activityAnimateType(animated, false);
    }

    /**
     * activity animate,after "startActivity" or "finishActivity" call.
     *
     * @param animated
     */
    public void activityAnimateType(int animated, boolean isFinish) {
        if (animated == SNManager.SN_ANIMATE_ACTIVITY_NO || animated == SNManager.SN_ANIMATE_ACTIVITY_SN) {
            getActivity().overridePendingTransition(0, 0);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_FADE) {
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SLIDE) {
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_scale_one,
                        R.anim.finish_scale_two);
            else
                getActivity().overridePendingTransition(R.anim.start_scale_one,
                        R.anim.start_scale_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE_ROTATE) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_scale_rotate_one,
                        R.anim.finish_scale_rotate_two);
            else
                getActivity().overridePendingTransition(R.anim.start_scale_rotate_one,
                        R.anim.start_scale_rotate_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_SCALE_TRANSLATE) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_scale_translate_one,
                        R.anim.finish_scale_translate_two);
            else
                getActivity().overridePendingTransition(R.anim.start_scale_translate_one,
                        R.anim.start_scale_translate_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_PUSH_POP_HORIZONTAL) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_pop_horizontal_one,
                        R.anim.finish_pop_horizontal_two);
            else
                getActivity().overridePendingTransition(R.anim.start_push_horizontal_one,
                        R.anim.start_push_horizontal_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_PUSH_POP_VERTICAL) {
            if (isFinish)

                getActivity().overridePendingTransition(R.anim.finish_pop_vertical_one,
                        R.anim.finish_pop_vertical_two);
            else
                getActivity().overridePendingTransition(R.anim.start_push_vertical_one,
                        R.anim.start_push_vertical_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_ZOOM) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_zoom_one,
                        R.anim.finish_zoom_two);
            else
                getActivity().overridePendingTransition(R.anim.start_zoom_one,
                        R.anim.start_zoom_two);
        } else if (animated == SNManager.SN_ANIMATE_ACTIVITY_TEST) {
            if (isFinish)
                getActivity().overridePendingTransition(R.anim.finish_zoom_one,
                        R.anim.finish_zoom_two);
            else
                getActivity().overridePendingTransition(R.anim.start_zoom_one, R.anim.start_zoom_two);
        }
    }


    /**
     * get package name
     *
     * @return
     */
    public String packageName() {
        return getContext().getPackageName();
    }

    public Activity getActivity() {
        if (getContext() instanceof Activity) {
            return (Activity) getContext();
        } else {
            new IllegalStateException("Manager must be a activity.");
        }
        return null;
    }

    public Dialog getDialog() {
        if (manager instanceof Dialog) {
            return (Dialog) manager;
        } else {
            new IllegalStateException("Manager must be a dialog.");
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    /**
     * get activity object
     *
     * @param activityClass
     * @param <T>
     * @return
     */
    public <T> T getActivity(Class<T> activityClass) {
        if (activityClass.isInstance(getActivity()))
            return (T) getActivity();
        return null;
    }

    public <T> T prop(Class<T> _class, String key) {
        try {
            SharedPreferences sp = getContext().getSharedPreferences(SNConfig.SHAREDPREFERENCES_KEY, Context.MODE_WORLD_WRITEABLE);

            String valueBase64 = sp.getString(key, "");

            if (util.strIsNotNullOrEmpty(valueBase64)) {

                byte[] base64 = util.base64Decode(valueBase64.getBytes());
                ByteArrayInputStream bais = new ByteArrayInputStream(base64);
                ObjectInputStream bis = new ObjectInputStream(bais);
                return (T) bis.readObject();
            } else
                return null;
        } catch (Exception ex) {
            util.logDebug(SNManager.class, ex.getMessage());
            return null;
        }
    }

    public boolean propExist(String key) {
        try {
            SharedPreferences sp = getContext().getSharedPreferences(SNConfig.SHAREDPREFERENCES_KEY, Context.MODE_WORLD_WRITEABLE);
            if (sp.contains(key)) {
                String r = sp.getString(key, "");
                return util.strIsNotNullOrEmpty(r);
            } else {
                return sp.contains(key);
            }
        } catch (Exception ex) {
            return false;
        }
    }

    public void removeProp(String key) {
        try {
            SharedPreferences sp = getContext().getSharedPreferences(SNConfig.SHAREDPREFERENCES_KEY, Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor prefEditor = sp.edit();
            prefEditor.remove(key);
            prefEditor.commit();
        } catch (Exception ex) {
            util.logDebug(SNManager.class, ex.getMessage());
        }
    }

    public void prop(String key, Object value) {
        try {
            SharedPreferences sp = getContext().getSharedPreferences(SNConfig.SHAREDPREFERENCES_KEY, Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor prefEditor = sp.edit();
            if (value != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(value);
                String value_Base64 = util.base64EncodeStr(baos
                        .toByteArray());
                prefEditor.putString(key, value_Base64);
            } else {
                prefEditor.remove(key);
            }
            prefEditor.commit();
        } catch (Exception ex) {
            util.logDebug(SNManager.class, ex.getMessage());
        }
    }
    // endregion

    //region Intent
    public Intent weChatIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        return intent;
    }

    public Intent cameraIntent(Uri opUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, opUri);
        return intent;
    }
    //endregion

    //region suport fragment

    /**
     * get support fragment manager
     *
     * @return
     */
    public FragmentManager supportFragmentManager() {
        if (getActivity() instanceof FragmentActivity) {
            FragmentActivity a = (FragmentActivity) getActivity();
            return a.getSupportFragmentManager();
        }
        return null;
    }
    //endregion

    // region resource manager

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
    public TypedArray obtainStyledAttr(AttributeSet attrs, int[] resIds) {
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
    public SNElement layoutInflateResId(int resId, SNElement root, SNInject _holder) {

        return layoutInflateResId(resId, root.toViewGroup(), _holder);
    }

    /**
     * layout Inflate
     *
     * @param resId
     * @param root
     * @return
     */
    public SNElement layoutInflateResId(int resId, SNElement root) {
        return layoutInflateResId(resId, root.toViewGroup());
    }

    /**
     * layout Inflate
     *
     * @param resId layout id
     * @param root  root view
     * @return
     */
    public SNElement layoutInflateResId(int resId, SNElement root, boolean _attachToRoot) {
        return layoutInflateResId(resId, root.toViewGroup(), _attachToRoot);
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
    public SNElement layoutInflateResId(int resId, SNElement root, boolean _attachToRoot, SNInject _holder) {
        return layoutInflateResId(resId, root.toViewGroup(), _attachToRoot, _holder);
    }


    /**
     * layout Inflate
     *
     * @param resId
     * @param root
     * @return
     */
    public SNElement layoutInflateResId(int resId, ViewGroup root, SNInject _holder) {
        SNElement r = null;
        if (manager instanceof Activity)
            r = create(getActivity().getLayoutInflater().inflate(resId, root));
        else if (manager instanceof Dialog)
            r = create(getDialog().getLayoutInflater().inflate(resId, root));

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
    public SNElement layoutInflateResId(int resId, ViewGroup root, boolean _attachToRoot, SNInject _holder) {
        SNElement r = null;
        if (manager instanceof Activity)
            r = create(getActivity().getLayoutInflater().inflate(resId, root, _attachToRoot));
        else if (manager instanceof Dialog)
            r = create(getDialog().getLayoutInflater().inflate(resId, root, _attachToRoot));
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
        SNElement r = null;
        if (manager instanceof Activity)
            r = create(getActivity().getLayoutInflater().inflate(resId, null));
        else if (manager instanceof Dialog)
            r = create(getDialog().getLayoutInflater().inflate(resId, null));
        return r;
    }

    /**
     * layout Inflate
     *
     * @param resId   layout id
     * @param _holder view inject, refer to document->view inject
     * @return
     */
    public SNElement layoutInflateResId(int resId, SNInject _holder) {

        SNElement r = null;
        if (manager instanceof Activity)
            r = create(getActivity().getLayoutInflater().inflate(resId, null, false));
        else if (manager instanceof Dialog)
            r = create(getDialog().getLayoutInflater().inflate(resId, null, false));
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
    public SNElement layoutInflateName(String name, ViewGroup root, boolean _attachToRoot, SNInject _holder) {
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
    public SNElement layoutInflateName(String name, ViewGroup root, SNInject _holder) {
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
    public SNElement layoutInflateName(String name, SNInject _holder) {
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
        String packageName = getContext().getPackageName();
        return getContext().getResources().getIdentifier(name, typeName, packageName);
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


    public Bitmap readBitMap(int resId) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            InputStream is = getContext().getResources().openRawResource(resId);
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        getContext().getTheme().resolveAttribute(resid, typedValue, true);
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
        Display mDisplay = null;
        if (manager instanceof Activity)
            mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        else mDisplay = getDialog().getWindow().getWindowManager().getDefaultDisplay();
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
        return getContext().getResources().getColor(resId);
    }

    /**
     * get string by id
     *
     * @param resId string id
     * @return String
     */
    public String stringResId(int resId) {
        return getContext().getResources().getString(resId);
    }

    /**
     * get string by id
     *
     * @param resId string id
     * @return String
     */
    public String[] stringArrayResId(int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    public List<String> stringArrayListResId(int resId) {
        String[] a = stringArrayResId(resId);
        return util.arrayToList(a);
    }

    /**
     * get drawable object by id
     *
     * @param resId drawable id
     * @return
     */
    public Drawable drawableResId(int resId) {
        Bitmap bitmap = readBitMap(resId);
        if (bitmap != null)
            return util.imgParseDrawable(bitmap);
        else return null;
    }

    /**
     * get bitmap object by id
     *
     * @param resId
     * @return Bitmap
     */
    public Bitmap bitmapResId(int resId) {
        Drawable drawable = drawableResId(resId);
        return util.imgParse(drawable);
    }

    /**
     * get dimen value by id
     *
     * @param resId dimen id
     * @return
     */
    public int dimenResId(int resId) {
        return getContext().getResources().getDimensionPixelOffset(resId);
    }

    /**
     * get Resources object
     *
     * @return
     */
    public Resources resources() {

        return getContext().getResources();
    }


    public String readAssetsFile(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getContext().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
        if (getActivity() instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) getActivity();
            a.setBehindContentView(element.toView(), layoutParams);
        }
    }

    /**
     * set sliding left view
     *
     * @param element
     */
    public void slidingLeftView(SNElement element) {
        if (getActivity() instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) getActivity();
            a.setBehindContentView(element.toView());
        }
    }

    /**
     * set sliding left view
     *
     * @param layoutResID
     */
    public void slidingLeftView(int layoutResID) {
        if (getActivity() instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) getActivity();
            a.setBehindContentView(layoutResID);
        }
    }

    /**
     * set sliding right view
     *
     * @param element
     */
    public void slidingRightView(SNElement element) {
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
            SlidingActivity a = (SlidingActivity) getActivity();
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setShadowDrawable(resId);
        }
    }

    /**
     * set left shadow
     *
     * @param drawable drawable object
     */
    public void slidingLeftShadow(Drawable drawable)

    {
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
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
        if (getActivity() instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.setFadeDegree(f);
        }
    }

    /**
     * shwo left
     */
    public void showSlidingLeft() {
        if (getActivity() instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showMenu();
        }
    }

    /**
     * show right
     */
    public void showSlidingRight() {
        if (getActivity() instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showSecondaryMenu();
        }
    }

    /**
     * show main
     */
    public void showSlidingContent() {
        if (getActivity() instanceof SlidingActivity) {
            SlidingMenu menu = slidingMenu();
            menu.showContent();
        }
    }

    // endregion

    //region http

    public void loadImage(SNElement listView, int position, String imageUrl, SNOnSetImageListenter onSetImageListenter, SNOnImageLoadListener _onImageLoadListener) {
        AbsListView absListView = null;
        if (listView != null && listView.toView() instanceof AbsListView)
            absListView = listView.toView(AbsListView.class);
        SNLoadBitmapManager.instance(this).loadImageFromUrl(absListView, position, imageUrl, onSetImageListenter, _onImageLoadListener);
    }

    public void loadImage(String imageUrl, SNOnSetImageListenter onSetImageListenter, SNOnImageLoadListener _onImageLoadListener) {
        SNLoadBitmapManager.instance(this).loadImageFromUrl(imageUrl, onSetImageListenter, _onImageLoadListener);
    }

    public void loadImage(String imageUrl, SNOnImageLoadListener _onImageLoadListener) {
        SNLoadBitmapManager.instance(this).loadImageFromUrl(imageUrl, null, _onImageLoadListener);
    }

    /**
     * http post
     *
     * @param url                  url
     * @param bodys                post request body
     * @param contentType          content type, refer to SNConfig->http
     * @param requestHeader        http request header
     * @param onHttpResultListener call back
     */
    public void post(final String url, final HashMap<String, String> bodys, String contentType, final HashMap<String, String> requestHeader,
                     final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = util.httpCreateAsyncHttpClient(requestHeader, contentType);

        client.post(this.context, url, new RequestParams(bodys), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("POST", url, bodys, requestHeader, statusCode, r);
                    onHttpResultListener.onSuccess(statusCode, r);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("POST", url, bodys, requestHeader, statusCode, r);
                    onHttpResultListener.onFailure(statusCode, r);
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
        post(url, bodys, SN_HTTP_REQUEST_CONTENT_TYPE_FORM, null, onHttpResultListener);
    }

    public void post(String url, HashMap<String, String> bodys, HashMap<String, String> headers, SNOnHttpResultListener onHttpResultListener) {
        post(url, bodys, SN_HTTP_REQUEST_CONTENT_TYPE_FORM, headers, onHttpResultListener);
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
    public void get(final String url, final HashMap<String, String> requestParams, String contentType, final HashMap<String, String> requestHeader, final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = util.httpCreateAsyncHttpClient(requestHeader, contentType);
        client.get(url, new RequestParams(requestParams), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("GET", url, null, requestHeader, statusCode, r);
                    onHttpResultListener.onSuccess(statusCode, r);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("GET", url, null, requestHeader, statusCode, r);
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
    public void get(final String url, final HashMap<String, String> requestHeader, final SNOnHttpResultListener onHttpResultListener) {
        AsyncHttpClient client = util.httpCreateAsyncHttpClient(requestHeader, SNConfig.SN_HTTP_REQUEST_CONTENT_TYPE_FORM);
        client.get(this.context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("GET", url, null, requestHeader, statusCode, r);
                    onHttpResultListener.onSuccess(statusCode, r);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (onHttpResultListener != null) {
                    String r = "";
                    if (responseBody != null)
                        r = new String(responseBody);
                    httpLog("GET", url, null, requestHeader, statusCode, r);
                    onHttpResultListener.onFailure(statusCode, r);
                }
            }
        });
    }


    public void httpLog(String type, String url, HashMap<String, String> requestParams, HashMap<String, String> requestHeader, int status, String result) {
        util.logInfo(SNManager.class, "============" + type + " REQUEST START============");
        util.logInfo(SNManager.class, url);
        if (requestParams != null) {
            util.logInfo(SNManager.class, "============Request Params============");
            String r = "";
            for (String key : requestParams.keySet()) {
                String v = requestParams.get(key);
                r += util.strFormat("{0}={1}", key, v) + "&";
            }
            r.substring(0, r.length() - 1);
            util.logInfo(SNManager.class, r);
        }
        if (requestHeader != null) {
            util.logInfo(SNManager.class, "============Request Header============");
            for (String key : requestHeader.keySet()) {
                String v = requestHeader.get(key);
                util.logInfo(SNManager.class, util.strFormat("{0}:{1}", key, v));
            }
        }
        util.logInfo(SNManager.class, "============Request Status============");
        util.logInfo(SNManager.class, util.strParse(status));
        util.logInfo(SNManager.class, "============Request Result============");
        util.logInfo(SNManager.class, result);
        util.logInfo(SNManager.class, "============Request END============");
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

    //region create manager
    public LocationManager locationManager() {

        return (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    }
    //endregion

    //region input
    public InputMethodManager inputMethodManager() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm;
    }


    public void inputAwaysVisible() {
        if (manager instanceof Activity)

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        else if (manager instanceof Dialog)
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    public void inputShow(SNElement element) {
        InputMethodManager imm = inputMethodManager();
        imm.showSoftInput(element.toView(), InputMethodManager.SHOW_FORCED);
    }

    public void inputToggle(SNElement element) {
        InputMethodManager imm = inputMethodManager();
        imm.toggleSoftInputFromWindow(element.windowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void inputHide(SNElement element) {
        InputMethodManager imm = inputMethodManager();
        imm.hideSoftInputFromWindow(element.windowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //endregion
}
