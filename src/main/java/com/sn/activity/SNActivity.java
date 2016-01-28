
/**
 * @author xuhui
 */
package com.sn.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sn.main.SNManager;

public class SNActivity extends FragmentActivity {


    public SNManager $;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        $ = SNManager.instence(SNActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}