package com.sn.interfaces;

import android.view.MotionEvent;

import com.sn.main.SNElement;

public interface SNOnTouchListener {
	public boolean onLoad(SNElement view, MotionEvent event);
	public boolean onTouchUp(SNElement view, MotionEvent event);
	public boolean onTouchMove(SNElement view, MotionEvent event);
	public boolean onTouchDown(SNElement view, MotionEvent event);
	public boolean onTouchCancel(SNElement view, MotionEvent event);
	
}
