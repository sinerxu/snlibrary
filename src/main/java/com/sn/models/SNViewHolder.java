package com.sn.models;

import android.widget.AdapterView;

import com.sn.main.SNElement;

public class SNViewHolder {
	public int pos;
	public AdapterView<?> parent;
	public SNElement view;
	public SNElement viewGroup;
	public long id;
	public SNViewHolder(SNElement _v) {
		this.view = _v;
	}

}
