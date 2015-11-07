package com.sn.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.sn.lib.R;
import com.sn.main.SNElement;


public class SNLoadingDialog extends SNDialog {

    public static SNLoadingDialog currentLoadingDialog;

    SNElement loadingProgressBar;

    public SNLoadingDialog(Context context) {
        super(context, R.style.mystyle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        $.contentView($.resourceLayout("sn_loading"));
        loadingProgressBar = $.create($.resourceId("loadingProgressBar"));
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //close();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void close() {
        // TODO Auto-generated method stub
        if (currentLoadingDialog != null)
            currentLoadingDialog.dismiss();
    }
}
