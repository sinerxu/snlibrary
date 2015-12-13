package com.sn.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by xuhui on 15/11/29.
 */
public class SNApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    public static Context getAppContext() {
        return mContext;
    }

}
