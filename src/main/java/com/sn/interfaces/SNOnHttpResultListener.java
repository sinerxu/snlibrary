package com.sn.interfaces;

import com.sn.models.SNHeader;

import java.util.ArrayList;

public interface SNOnHttpResultListener {
    void onSuccess(int statusCode, ArrayList<SNHeader> headers, String result);

    void onFailure(int statusCode, ArrayList<SNHeader> headers, String result);
}
