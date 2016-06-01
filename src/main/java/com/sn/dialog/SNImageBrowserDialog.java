package com.sn.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.sn.interfaces.SNOnClickListener;
import com.sn.interfaces.SNOnLoadImageFinishListener;
import com.sn.interfaces.SNThreadDelayedListener;
import com.sn.lib.R;
import com.sn.main.SNElement;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xuhui on 15/12/30.
 */
public class SNImageBrowserDialog extends SNDialog {
    SNElement scroImages;
    SNElement viewMain;
    SNElement tvInfo;

    int defaultShow = 0;
    ArrayList<SNElement> elements;
    int currentPage = 0;
    ArrayList<PhotoViewAttacher> attachers = new ArrayList<PhotoViewAttacher>();

    public SNImageBrowserDialog(Context context) {
        super(context, R.style.SNDialog);

    }

    public void setBitmap(ArrayList<Bitmap> bitmaps) {
        elements = new ArrayList<SNElement>();
        for (Bitmap bitmap : bitmaps) {
            SNElement elem = $.create(new ImageView(this.getContext()));
            elem.image(bitmap);
            elem.scaleType(ImageView.ScaleType.FIT_XY);
            elem.adjustViewBounds(true);
            elements.add(elem);
        }
    }

    void dismissEvent(SNElement elem) {
        elem.clickable(true);
        SNOnClickListener onClickListener = new SNOnClickListener() {
            @Override
            public void onClick(SNElement snElement) {
                SNImageBrowserDialog.this.dismiss();
            }
        };
        elem.click(onClickListener);
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }

    int load_count = 0;
    boolean isLoadUrl = false;

    public void setUrls(final ArrayList<String> urls) {
        isLoadUrl = true;
        elements = new ArrayList<SNElement>();
        load_count = 0;
        for (final String url : urls) {
            final SNElement elem = $.layoutInflateResId(R.layout.view_dialog_loading_image_item);
            elem.find(R.id.ivImage).visible($.SN_UI_INVISIBLE);
            elem.find(R.id.pbLoading).visible($.SN_UI_VISIBLE);
            final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(elem.find(R.id.ivImage).toView(ImageView.class));
            attachers.add(photoViewAttacher);
            elem.find(R.id.ivImage).image(url, new SNOnLoadImageFinishListener() {
                @Override
                public void onFinish(Bitmap bitmap) {
                    photoViewAttacher.update();
                    $.util.logInfo(SNImageBrowserDialog.class, url);
                    load_count++;
                    if (load_count == urls.size() && scroImages != null) {
                        scroImages.bindScrollable(elements);
                        setInfo();
                        for (SNElement elem : elements) {
                            elem.find(R.id.ivImage).visible($.SN_UI_VISIBLE);
                            elem.find(R.id.pbLoading).visible($.SN_UI_INVISIBLE);
                        }
                    }
                }
            });
            elements.add(elem);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_browser);
        scroImages = $.create(R.id.scroImages);
        viewMain = $.create(R.id.viewMain);
        tvInfo = $.create(R.id.tvInfo);
        if (elements != null && elements.size() > 0) {
            scroImages.bindScrollable(elements);
            if (!isLoadUrl) {
                for (SNElement elem : elements) {
                    elem.find(R.id.ivImage).visible($.SN_UI_VISIBLE);
                    elem.find(R.id.pbLoading).visible($.SN_UI_INVISIBLE);
                }
            }
            setInfo();
        }
        scroImages.currentItem(defaultShow);

        dismissEvent(viewMain);
        scroImages.pageChange(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SNImageBrowserDialog.this.currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    setInfo();
                }
            }
        });

        $.util.threadDelayed(500, new SNThreadDelayedListener() {
            @Override
            public void onFinish() {
                scroImages.toView(ViewPager.class).requestLayout();
            }
        });
    }


    void setInfo() {
        if (elements != null)
            tvInfo.text($.util.strFormat("{0}/{1}", currentPage + 1, elements.size()));
    }


    public void setDefaultShow(int defaultShow) {
        this.defaultShow = defaultShow;
        this.currentPage = defaultShow;
    }
}
