package com.sn.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.sn.main.SNManager;

public class SNFragment extends Fragment {
    public SNManager $;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ($ == null)
            $ = SNManager.instence(SNFragment.this.getActivity());
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        if ($ == null)
            $ = SNManager.instence(activity);
        super.onInflate(activity, attrs, savedInstanceState);
    }





}
