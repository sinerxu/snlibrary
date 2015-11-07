package com.sn.interfaces;

import android.graphics.Bitmap;

/**
 * Created by xuhui on 15/11/6.
 */
public interface SNOnImageLoadListener {
    void onSuccess(Bitmap map);
    void onFailure();
}
