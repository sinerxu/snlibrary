package com.sn.controlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.models.SNInject;

import java.util.ArrayList;

/**
 * Created by xuhui on 15/11/22.
 */
public class SNSlideBanner extends SNLinearLayout {

    final float MIN_OPACITY = 0.3f;
    final int DOT_SIZE = $.px(8);

    class SNSlideBannerInject extends SNInject {
        SNElement saSlides;
        SNElement viewDotBox;
        SNElement ivShape;
    }

    int selectedPage;
    int currentPage;
    SNSlideBannerInject inject = new SNSlideBannerInject();
    ArrayList<SNElement> banners;
    ArrayList<SNElement> dots;
    SNElement $main;

    public SNSlideBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SNSlideBanner(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (banners == null && getChildCount() > 0) {
            banners = new ArrayList<SNElement>();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View item = getChildAt(i);
                banners.add($.create(item));
            }
            removeAllViews();
            init();
        }
    }


    void init() {
        if (banners != null) {
            dots = new ArrayList<SNElement>();
            $main = $.layoutInflateResId(R.layout.controler_slide_banner, $this.toViewGroup());
            $main.inject(inject);
            inject.saSlides.bindScrollable(banners);
            for (int i = 0; i < banners.size(); i++) {
                SNElement banner = banners.get(i);
                banner.adjustViewBounds(true);
                banner.scaleType(ImageView.ScaleType.FIT_XY);
                SNElement dot = $.create(new LinearLayout($.getActivity()));
                inject.viewDotBox.add(dot);
                dot.marginLeft(DOT_SIZE);
                dot.width(DOT_SIZE);
                dot.height(DOT_SIZE);
                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadius(DOT_SIZE / 2);
                gd.setColor(Color.rgb(255, 255, 255));
                dot.background(gd);
                dot.opacity(MIN_OPACITY);
                if (i == 0) {
                    dot.opacity(1);
                }
                dots.add(dot);
            }

            inject.saSlides.toView(SNScrollable.class).setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    SNSlideBanner.this.currentPage = position;
                    animateDot(positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    selectedPage = currentPage;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 0) {
                        selectedPage = currentPage;
                        setDotIndex(selectedPage);
                    }
                }
            });

        }

    }

    public void setBanners(ArrayList<SNElement> _banners) {
        this.banners = _banners;
        init();
    }

    /**
     * 获取方向
     *
     * @param positionOffsetPixels
     * @return -1向左，1向右
     */
    int getDirect(int positionOffsetPixels) {
        int c = positionOffsetPixels + inject.saSlides.width() * this.currentPage;
        int s = inject.saSlides.width() * this.selectedPage;
        if (c > s) {
            return 1;
        } else if (c < s) {
            return -1;
        } else {
            return 0;
        }

    }

    //0.3/1    positionOffsetPixels   /   inject.saSlides.width()
    float getOpacity(int positionOffsetPixels) {
        //停留位置
        int s = inject.saSlides.width() * this.selectedPage;
        //当前位置
        int c = positionOffsetPixels + inject.saSlides.width() * this.currentPage;
        //偏移
        int o = c - s;
        //偏移量
        float op = (float) o / inject.saSlides.width();
        //最小值
        float p = 1 - MIN_OPACITY;
        return Math.abs(op) * p + 0.3f;
    }

    void animateDot(int positionOffsetPixels) {
        int direct = getDirect(positionOffsetPixels);
        float o = getOpacity(positionOffsetPixels);
        float t = 1 + 0.3f - o;
        if (direct == 1) {
            dots.get(selectedPage).opacity(t);
            dots.get(selectedPage + 1).opacity(o);
        } else if (direct == -1) {
            dots.get(selectedPage - 1).opacity(o);
            dots.get(selectedPage).opacity(t);
        }
    }


    void setDotIndex(int index) {
        for (SNElement item : dots) {
            item.opacity(MIN_OPACITY);
        }
        dots.get(index).opacity(1);
    }

    public SNElement getDotBox() {
        return inject.viewDotBox;
    }

    public void setShape(Bitmap _shape) {
        int size = $.px(20);
        setShape(_shape, size);
    }

    public void setShape(Bitmap _shape, int size) {
        dotMarginUpdate(size);
        inject.ivShape.image($.util.imgCurve(_shape, size));
    }

    public void setCurveShape(int size) {

        dotMarginUpdate(size);
        Bitmap _shape = $.util.imgCreate($.displaySize().getWidth(), size);
        inject.ivShape.image($.util.imgCurve(_shape, size));
    }

    public void setCurveShape() {
        int size = $.px(20);
        setCurveShape(size);
    }


    void dotMarginUpdate(int size) {
        inject.viewDotBox.marginBottom(size + $.px(10));
    }
}
