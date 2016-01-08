package com.sn.dialog;

import android.app.Dialog;
import android.content.Context;

import com.sn.main.SNManager;

public class SNDialog extends Dialog {
    public SNManager $;

    public SNDialog(SNManager $) {
        super($.getActivity());
    }

    public SNDialog(SNManager $, int theme) {
        super($.getActivity(), theme);
    }

    public SNDialog(Context context) {
        super(context);
        $ = SNManager.instence(this, context);
        // TODO Auto-generated constructor stub
    }

    public SNDialog(Context context, int theme) {
        super(context, theme);
        $ = SNManager.instence(this, context);
        // TODO Auto-generated constructor stub
    }
}
