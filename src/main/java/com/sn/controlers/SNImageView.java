package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.main.SNManager;
import com.sn.models.SNSize;

/**
 * Created by xuhui on 15/12/31.
 */
public class SNImageView extends ImageView {

    public SNManager $;
    public SNElement $this;
    boolean isInit = false;
    int imgResid = 0;
    Bitmap bitmap;
    Boolean adjustWidth;
    Boolean circle;
    int circleBorder;


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
            adjustWidth = array.getBoolean(R.styleable.SNImageView_adjustWidth, false);
            circle = array.getBoolean(R.styleable.SNImageView_circle, false);
            circleBorder = array.getDimensionPixelOffset(R.styleable.SNImageView_circleBorder, 0);
            array.recycle();
        }
        if (imgResid != 0)
            imageResource(imgResid);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    boolean isInitSize = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    protected void onInit() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitSize) {
            updateImage();
            if (bitmap != null) {
                $this.scaleType(ScaleType.FIT_XY);
                //获取宽度
                int w = $this.width();
                int h = $this.height();
                $.util.logInfo(SNImageView.class, $.util.strFormat("adjustWidth===width:{0},height:{1}", w, h));
                int ih = bitmap.getHeight();
                int iw = bitmap.getWidth();
                if (adjustWidth) {
                    int result_width = 0;
                    result_width = (int) ((float) h / (float) ih * (float) iw);
                    // $this.width(result_width);
                    $this.size(new SNSize(result_width, h));
                    $.util.logInfo(SNImageView.class, $.util.strFormat("adjustWidth===width:{0},height:{1}====bwidth:{2},bheight:{3}", result_width, h, iw, ih));
                } else {
                    int result_height = 0;
                    result_height = (int) ((float) w / (float) iw * (float) ih);
                    // $this.height(result_height);
                    $this.size(new SNSize(w, result_height));
                    $.util.logInfo(SNImageView.class, $.util.strFormat("adjustHeight===width:{0},height:{1}====bwidth:{2},bheight:{3}", w, result_height, iw, ih));
                }
            } else {
                $.util.logInfo(SNImageView.class, "bitmap = null");
            }
            isInitSize = true;
        }
        if (!isInit) {
            isInit = true;
            onInit();
        }
    }

    public void imageResource(int resId) {
        bitmap = $.readBitMap(resId);
        updateImage();
    }


    public void imageBitmap(Bitmap bm) {
        bitmap = bm;
        updateImage();
    }

    void updateImage() {
        if (bitmap != null) {
            if (circle && $this.width() != 0) {
                bitmap = $.util.imgCircleBorder(bitmap, bitmap.getWidth() / $this.width() * circleBorder, Color.BLACK);
                setImageBitmap(bitmap);
            }
        }
    }
}
