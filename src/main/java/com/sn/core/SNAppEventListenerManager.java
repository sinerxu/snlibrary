package com.sn.core;

import android.content.Intent;

import com.sn.interfaces.SNAppEventListener;

import java.util.HashMap;

/**
 * Created by xuhui on 16/1/17.
 */
public class SNAppEventListenerManager {


    public static SNAppEventListenerManager instance() {
        return new SNAppEventListenerManager();
    }

    SNAppEventListenerManager() {
    }


    static HashMap<String, SNAppEventListener> appEventListeners;

    /**
     * 移除
     *
     * @param key
     */

    public void remove(String key) {
        if (appEventListeners != null && appEventListeners.containsKey(key))
            appEventListeners.remove(key);
    }

    /**
     * 设置
     *
     * @param key
     */
    public void set(String key, SNAppEventListener appEventListener) {
        if (appEventListeners == null)
            appEventListeners = new HashMap<String, SNAppEventListener>();
        if (appEventListeners.containsKey(key)) appEventListeners.remove(key);
        appEventListeners.put(key, appEventListener);
    }

    /**
     * 执行
     *
     * @param key
     * @return
     */
    public void fire(String key, Intent intent) {
        fire(key, intent, false);
    }

    /**
     * 执行
     *
     * @param key
     * @return
     */

    public void fire(String key) {
        fire(key, null);
    }

    /**
     * 执行
     *
     * @param key
     * @param isRemove @return
     */
    public void fire(String key, Intent intent, boolean isRemove) {
        if (appEventListeners != null && appEventListeners.containsKey(key)) {
            appEventListeners.get(key).onEvent(intent);
            if (isRemove) appEventListeners.remove(key);
        }
    }
}
