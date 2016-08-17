package com.sn.activity.listeners;

import android.content.Intent;

/**
 * Created by xuhui on 15/12/21.
 */
public interface SNOnActivityResult {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
