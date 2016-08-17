package com.sn.postting.alert;

import android.content.Context;

import com.sn.interfaces.SNOnClickListener;
import com.sn.lib.R;

/**
 * Created by xuhui on 15/8/6.
 */
public class SNAlert {
    public static final int ALERT_TYPE_DEFAULT = 0;
    public static final int ALERT_TYPE_IOS = 1;
    public static final int ALERT_TYPE_CUSTOMER = 100;
    public Context context;
    static SNDefaultAlert defaultAlert;
    static SNUIAlert uiAlert;


    public static SNAlert instance(Context _context, int uiType) {
        SNAlert _result;
        switch (uiType) {
            case 0:
                _result = instanceDefault(_context);
                break;
            case 1:
                _result = instanceIOS(_context);
                break;
            default:
                _result = instanceDefault(_context);
                break;
        }
        return _result;
    }


    static SNDefaultAlert instanceDefault(Context _context) {
        if (defaultAlert == null) {
            defaultAlert = new SNDefaultAlert(_context);
        }
        defaultAlert.context = _context;
        return defaultAlert;
    }

    static SNUIAlert instanceIOS(Context _context) {
        if (uiAlert == null) {
            uiAlert = new SNUIAlert(_context);
        }
        uiAlert.context = _context;
        return uiAlert;
    }

    public SNAlert() {

    }

    public SNAlert(Context _context) {
        this.context = _context;
    }

    public void alert(String title, String msg, String buttonTitle, SNOnClickListener onClickListener) {

    }

    public void alert(String title, String msg) {
        alert(title, msg, context.getString(R.string.alert_ok), null);

    }


    public void alert(String msg, SNOnClickListener onClickListener) {
        alert(context.getString(R.string.alert_title), msg, context.getString(R.string.alert_ok), onClickListener);
    }


    public void alert(String msg) {
        alert(context.getString(R.string.alert_title), msg, context.getString(R.string.alert_ok), null);
    }


    public void confirm(String title, String msg, String btnOkTitle, String btnCancelTitle,
                        SNOnClickListener okClick, SNOnClickListener cancelClick) {

    }


    public void confirm(String msg, SNOnClickListener okClick,
                        SNOnClickListener cancelClick) {
        confirm(context.getString(R.string.alert_title), msg, context.getString(R.string.alert_ok), context.getString(R.string.alert_cancel), okClick, cancelClick);
    }
}
