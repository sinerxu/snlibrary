package com.sn.models;

public class SNSize {
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	int height;
	 int width;

	public SNSize(int w, int h) {
		height = h;
		width = w;
	}

	public SNSize() {
		height = 0;
		width = 0;
	}
}
