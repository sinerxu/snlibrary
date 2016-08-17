package com.sn.core;

import android.app.Dialog;
import android.content.Context;

import com.sn.main.SNConfig;
import com.sn.postting.alert.SNAlert;

import java.lang.reflect.Constructor;

/**
 * Created by xuhui on 16/6/8.
 */
public class SNLoadingBuilder {


    static Class customerLoadingDialogClass;
    Context context;

    SNLoadingBuilder(Context context) {
        this.context = context;
    }

    public static void resetCustomerLoadingDialog() {
        setCustomerLoadingDialog(null);
    }

    public static void setCustomerLoadingDialog(Class _customerLoadingDialogClass) {
        if (_customerLoadingDialogClass != null)
            SNConfig.SN_UI_LOADING_STYLE = SNLoadingDialogManager.LOADING_TYPE_CUSTOMER;
        else SNConfig.SN_UI_LOADING_STYLE = SNLoadingDialogManager.LOADING_TYPE_DEFAULT;
        customerLoadingDialogClass = _customerLoadingDialogClass;
    }

    public static Class getCustomerLoadingDialogClass() {
        return customerLoadingDialogClass;
    }

    public static SNLoadingBuilder instance(Context context) {
        SNLoadingBuilder alertBuilder = new SNLoadingBuilder(context);
        return alertBuilder;
    }

    public SNLoadingDialogManager create() {
        if (SNConfig.SN_UI_LOADING_STYLE == SNLoadingDialogManager.LOADING_TYPE_CUSTOMER) {
            try {
                Constructor c1 = customerLoadingDialogClass.getDeclaredConstructor(Context.class);
                c1.setAccessible(true);
                Dialog obj = (Dialog) c1.newInstance(context);
                SNLoadingDialogManager.setLoadingDialog(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SNLoadingDialogManager.instance(context);
    }
}
