package com.sn.interfaces;

import android.graphics.Bitmap;

/**
 * Created by xuhui on 15/11/6.
 */
public interface SNOnSetImageListenter {

    Bitmap onCreateDefaultBitmap(Bitmap bitmap);

    Bitmap onCreateBitmap(String url, Bitmap bitmap);

    Bitmap onCreateErrorBitmap(Bitmap bitmap);
}
