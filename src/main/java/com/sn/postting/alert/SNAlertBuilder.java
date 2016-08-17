package com.sn.postting.alert;

import android.content.Context;

import com.sn.main.SNConfig;


public class SNAlertBuilder {
    static Class<SNAlert> customerAlertClass;
    Context context;

    SNAlertBuilder(Context context) {
        this.context = context;
    }

    public static void resetCustomerAlert() {
        setCustomerAlert(null);
    }

    public static void setIOSAlert() {
        resetCustomerAlert();
        SNConfig.SN_UI_ALERT_STYLE = SNAlert.ALERT_TYPE_IOS;
    }

    public static void setCustomerAlert(Class _customerAlertClass) {
        if (_customerAlertClass != null)
            SNConfig.SN_UI_ALERT_STYLE = SNAlert.ALERT_TYPE_CUSTOMER;
        else SNConfig.SN_UI_ALERT_STYLE = SNAlert.ALERT_TYPE_DEFAULT;
        customerAlertClass = _customerAlertClass;
    }

    public static SNAlertBuilder instance(Context context) {
        SNAlertBuilder alertBuilder = new SNAlertBuilder(context);
        return alertBuilder;
    }

    public SNAlert create() {
        if (SNConfig.SN_UI_ALERT_STYLE == SNAlert.ALERT_TYPE_CUSTOMER) {
            if (customerAlertClass != null) {
                try {
                    SNAlert alert = customerAlertClass.newInstance();
                    alert.context = context;
                    return alert;
                } catch (Exception e) {
                    e.printStackTrace();
                    return SNAlert.instance(context, SNAlert.ALERT_TYPE_DEFAULT);
                }
            } else {
                return SNAlert.instance(context, SNAlert.ALERT_TYPE_DEFAULT);
            }
        } else {
            return SNAlert.instance(context, SNConfig.SN_UI_ALERT_STYLE);
        }
    }
}
