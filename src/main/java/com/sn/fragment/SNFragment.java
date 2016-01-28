package com.sn.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.sn.main.SNManager;


public class SNFragment extends Fragment {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public SNManager $;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ($ == null)
            $ = SNManager.instence(SNFragment.this.getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ($ == null)
            $ = SNManager.instence(SNFragment.this.getActivity());
    }

    @Override
    public void onResume() {
        if ($ == null)
            $ = SNManager.instence(SNFragment.this.getActivity());
        super.onResume();

    }


    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        if ($ == null)
            $ = SNManager.instence(activity);
        super.onInflate(activity, attrs, savedInstanceState);
    }


}
