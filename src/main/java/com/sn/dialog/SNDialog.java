package com.sn.dialog;

import android.app.Dialog;
import android.content.Context;

import com.sn.main.SNManager;

public class SNDialog extends Dialog {
    public SNManager $;

    public SNDialog(SNManager $) {
        super($.getActivity());
        this.$ = $;
        // TODO Auto-generated constructor stub
    }

    public SNDialog(SNManager $, int theme) {
        super($.getActivity(), theme);
        this.$ = $;
        // TODO Auto-generated constructor stub
    }

    public SNDialog(Context context) {
        super(context);
        $ = SNManager.instence(context);
        // TODO Auto-generated constructor stub
    }

    public SNDialog(Context context, int theme) {
        super(context, theme);
        $ = SNManager.instence(context);
        // TODO Auto-generated constructor stub
    }
}
