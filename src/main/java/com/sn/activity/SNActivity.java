
/**
 * @author xuhui
 */
package com.sn.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.sn.activity.listeners.SNOnActivityRequestPermissionsResult;
import com.sn.activity.listeners.SNOnActivityResult;
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

    SNOnActivityResult activityResult;
    SNOnActivityRequestPermissionsResult activityRequestPermissionsResult;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (this.activityRequestPermissionsResult != null) {
            this.activityRequestPermissionsResult.onActivityRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResult != null) activityResult.onActivityResult(requestCode, resultCode, data);
    }


    public void setActivityResult(SNOnActivityResult activityResult) {
        this.activityResult = activityResult;
    }


    public void setActivityRequestPermissionsResult(SNOnActivityRequestPermissionsResult activityRequestPermissionsResult) {
        this.activityRequestPermissionsResult = activityRequestPermissionsResult;
    }
}