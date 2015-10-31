package com.sn.interfaces;

import android.view.animation.Animation;

import com.sn.main.SNElement;

public interface SNAnimationListener {
	public void onAnimationStart(SNElement view, Animation animation);

	public void onAnimationRepeat(SNElement view, Animation animation);

	public void onAnimationEnd(SNElement view, Animation animation);
}
