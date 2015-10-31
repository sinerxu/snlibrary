package com.sn.activity;

import android.os.Bundle;
import android.view.KeyEvent;


import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 *
 * <activity android:name="com.sn.activity.SNLoadingActivity"  android:theme="@android:style/Theme.Translucent.NoTitleBar"></activity>
 * 
 * @author xuhui
 * 
 */
public class SNLoadingActivity extends SNActivity {

	public static SNLoadingActivity currentLoadingActivity;
	SNElement loadingProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentLoadingActivity = this;
		// TODO Auto-generated method stub
		$.contentView($.resourceLayout("sn_loading"));
		loadingProgressBar = $.create($.resourceId("loadingProgressBar"));
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
		currentLoadingActivity = null;
		$.finishActivity(SNManager.SN_ANIMATE_ACTIVITY_NO);
	}
}
