package com.sn.sdk.adsdk;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.sn.sdk.interfaces.SNAdInterface;

/**
 * Created by xuhui on 15/8/6.
 */
public class SNAd implements SNAdInterface {
    Context context;
    Activity activity;
    public static final int ADTYPE_BAIDUMOB = 0;
    public static final int ADTYPE_BAIDUAPPX = 1;

    SNAd(Activity activity) {
        this.activity = activity;
        this.context = activity.getBaseContext();
    }

    @Override
    public void banner(ViewGroup _parent) {

    }
}
