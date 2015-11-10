package com.sn.core;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sn.lib.R;


public class SNLoadingDialogManager {

    public static Dialog currentLoadingDialog;
    public static Thread closeThread;

    public static SNLoadingDialogManager loadingDialogManager;
    Context context;

    SNLoadingDialogManager() {

    }

    public SNLoadingDialogManager(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static SNLoadingDialogManager instance(Context context) {
        if (loadingDialogManager == null)
            loadingDialogManager = new SNLoadingDialogManager();
        loadingDialogManager.setContext(context);
        return loadingDialogManager;
    }


    public void show() {
        if (currentLoadingDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.sn_loading, null);// 得到加载view
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.loadingBox);// 加载布局
            // main.xml中的ImageView
            currentLoadingDialog = new Dialog(this.context, R.style.LoadingDialog);// 创建自定义样式dialog
            currentLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            currentLoadingDialog.setCancelable(false);// 不可以用“返回键”取消
            if (!currentLoadingDialog.isShowing()) {
                currentLoadingDialog.show();
            }
        }
    }

    public void close() {
        if (closeThread == null) {
            closeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (currentLoadingDialog != null) {
                            currentLoadingDialog.cancel();
                            currentLoadingDialog = null;
                            closeThread = null;
                            break;
                        }
                    }
                }
            });
            closeThread.start();
        }
    }
}
