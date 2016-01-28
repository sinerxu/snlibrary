package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.main.SNManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuhui on 15/12/31.
 */
public class SNImageView extends ImageView {

    public SNManager $;
    public SNElement $this;
    boolean isInit = false;
    int imgResid = 0;

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
            $.util.logInfo(SNImageView.class, "imgResid=" + imgResid);
            array.recycle();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (imgResid != 0)
            $this.image(readBitMap(imgResid));
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
        $this.image(readBitMap(resId));
    }

    public Bitmap readBitMap(int resId) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            InputStream is = $.getContext().getResources().openRawResource(resId);
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
            is.close();
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
