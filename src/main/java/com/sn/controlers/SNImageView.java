package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/12/31.
 */
public class SNImageView extends ImageView {

    public SNManager $;
    public SNElement $this;
    boolean isInit = false;
    int imgResid = 0;
    Bitmap bitmap;

    public SNImageView(Context context) {
        super(context);
        init$(context, null);
    }

    public SNImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init$(context, attrs);
    }

    public SNImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init$(context, attrs);

    }


    public void init$(Context context, AttributeSet attrs) {
        $ = SNManager.instence(context);
        $this = $.create(this);
        if (attrs != null) {
            TypedArray array = $.obtainStyledAttr(attrs, R.styleable.SNImageView);
            imgResid = array.getResourceId(R.styleable.SNImageView_image, 0);
            array.recycle();
            if (imgResid != 0)
                imageResource(imgResid);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    boolean isInitSize = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInitSize) {
            if (bitmap != null && $this.adjustViewBounds()) {
                //获取宽度
                int w = $this.width();
                int ih = bitmap.getHeight();
                int iw = bitmap.getWidth();
                int h = (int) ((float) w / (float) iw * (float) ih);
                $.util.logInfo(SNImageView.class, $.util.strFormat("h={3},w={0},ih={1},iw={2}", w, ih, iw, h));
                $this.height(h);
            } else {
                $.util.logInfo(SNImageView.class, "bitmap = null");
            }
            isInitSize = true;
        }
    }

    protected void onInit() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            isInit = true;
            onInit();
        }
    }

    public void imageResource(int resId) {
        bitmap = $.readBitMap(resId);
        $this.image(bitmap);
    }

}
