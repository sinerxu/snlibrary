package com.sn.interfaces;

public interface SNOnHttpResultListener {
    void onSuccess(int statusCode, String result);
    void onFailure(int statusCode, String result);
}
