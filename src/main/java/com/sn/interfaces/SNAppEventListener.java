package com.sn.interfaces;

import java.util.HashMap;

/**
 * Created by xuhui on 16/1/17.
 */
public interface SNAppEventListener {
    void onEvent(HashMap<String, Object> args);
}
