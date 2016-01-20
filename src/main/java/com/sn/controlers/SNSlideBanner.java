package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sn.core.SNInterval;
import com.sn.interfaces.SNIntervalListener;
import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.models.SNInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/11/22.
 */
public class SNSlideBanner extends SNLinearLayout {
    final int SCROLL_STATE_LOAD = -1;
    final int SCROLL_STATE_NORMAL = 0;
    final int SCROLL_STATE_SCROLLING = 1;
    final float MIN_OPACITY = 0.3f;
    final int DOT_SIZE = $.px(8);
    boolean auto_switch;
    int auto_switch_duration;
    SNInterval interval;
    int scroll_state = SCROLL_STATE_LOAD;

    class SNSlideBannerInject extends SNInject {
        SNElement saSlides;
        SNElement viewDotBox;
        SNElement ivShape;
    }

    int selectedPage;
    int currentPosition;
    SNSlideBannerInject inject = new SNSlideBannerInject();
    List<SNElement> banners;
    List<SNElement> dataBanners;
    List<SNElement> dots;
    SNElement $main;

    public SNSlideBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SNSlideBanner(Context context) {
        super(context);
    }

    void init(AttributeSet attrs) {
        TypedArray ta = $.obtainStyledAttr(attrs, R.styleable.SNSlideBanner);
        auto_switch = ta.getBoolean(R.styleable.SNSlideBanner_auto_switch, false);
        auto_switch_duration = ta.getInt(R.styleable.SNSlideBanner_auto_switch_duration, 10000);
        ta.recycle();
    }

    @Override
    protected void onInit() {
        super.onInit();
        dataBanners = new ArrayList<SNElement>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View item = getChildAt(i);
            dataBanners.add($.create(item));
        }
        removeAllViews();
        init();
    }

    void init() {
        if (dataBanners != null) {
            dots = new ArrayList<SNElement>();
            $main = $.layoutInflateResId(R.layout.controler_slide_banner, $this.toViewGroup());
            $main.inject(inject);
            setSelectedPage(0);
            for (int i = 0; i < dataBanners.size(); i++) {
                SNElement banner = dataBanners.get(i);
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

            inject.saSlides.pageChange(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    currentPosition = position;
                    if (positionOffsetPixels != 0)
                        animateDot(positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 0) {
                        scroll_state = SCROLL_STATE_NORMAL;
                        setSelectedPage(positionToPage(currentPosition));
                        setDotIndex(selectedPage);
                    } else if (state == 1) {
                        scroll_state = SCROLL_STATE_SCROLLING;
                    } else if (state == 2) {
                        scroll_state = SCROLL_STATE_SCROLLING;
                    }
                }
            });

        }

    }


    public void setSelectedPage(int page) {
        this.selectedPage = page;
        banners = new ArrayList<SNElement>();
        if (dataBanners.size() <= 2) {
            for (SNElement elem : dataBanners) {
                banners.add(elem);
            }
            inject.saSlides.bindScrollable(banners);
            inject.saSlides.currentItem(page);
            currentPosition = page;
        } else if (dataBanners.size() > 2) {
            if (selectedPage == 0) {
                banners.add(dataBanners.get(dataBanners.size() - 1));
                banners.add(dataBanners.get(0));
                banners.add(dataBanners.get(1));
            } else if (selectedPage == dataBanners.size() - 1) {
                banners.add(dataBanners.get(dataBanners.size() - 2));
                banners.add(dataBanners.get(dataBanners.size() - 1));
                banners.add(dataBanners.get(0));
            } else {
                banners.add(dataBanners.get(selectedPage - 1));
                banners.add(dataBanners.get(selectedPage));
                banners.add(dataBanners.get(selectedPage + 1));
            }
            inject.saSlides.bindScrollable(banners);
            inject.saSlides.currentItem(1);
            currentPosition = 1;
        }


    }

    int positionToPage(int viewPage) {
        if (dataBanners.size() > 2) {
            if (viewPage == 1) {
                return selectedPage;
            } else if (viewPage == 0) {
                return getSafePage(selectedPage - 1);
            } else {
                return getSafePage(selectedPage + 1);
            }
        } else return viewPage;
    }

    /**
     * 获取方向
     *
     * @param positionOffsetPixels
     * @return -1向左，1向右
     */
    int getDirect(int positionOffsetPixels) {
        int toPage = positionToPage(currentPosition);
        int c = positionOffsetPixels + inject.saSlides.width() * toPage;
        int s = inject.saSlides.width() * this.selectedPage;
        if (c > s) {
            if (selectedPage == 0 && toPage == dataBanners.size() - 1)
                return -1;
            else return 1;
        } else if (c < s) {
            return -1;
        } else {
            return 0;
        }

    }

    //0.3/1    positionOffsetPixels   /   inject.saSlides.width()
    float getOpacity(int positionOffsetPixels) {
        int r = positionToPage(currentPosition);
        if (this.selectedPage == 0 && r == dataBanners.size() - 1)
            r = -1;
        //停留位置
        int s = inject.saSlides.width() * this.selectedPage;
        //当前位置
        int c = positionOffsetPixels + inject.saSlides.width() * r;
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
            dots.get(getSafePage(selectedPage + 1)).opacity(o);
        } else if (direct == -1) {

            dots.get(getSafePage(selectedPage - 1)).opacity(o);
            dots.get(selectedPage).opacity(t);
        }
    }

    int getSafePage(int page) {
        if (page >= dataBanners.size()) page = 0;
        if (page < 0) page = dataBanners.size() - 1;
        return page;
    }

    void setDotIndex(int index) {
        for (SNElement item : dots) {
            item.opacity(MIN_OPACITY);
        }
        dots.get(index).opacity(1);
    }

    public void setBanners(List<SNElement> _banners) {
        this.dataBanners = _banners;
        init();
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

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        $.util.logDebug(SNScrollable.class, "onWindowFocusChanged＝＝hasWindowFocus＝" + hasWindowFocus);
        if (auto_switch) {
            if (hasWindowFocus) {
                if (interval != null) {
                    interval.stop();
                    interval = null;
                }
                interval = $.util.interval();
                interval.start(auto_switch_duration, new SNIntervalListener() {
                    @Override
                    public void onInterval(SNInterval interval) {
                        if (interval.equals(SNSlideBanner.this.interval)) {
                            if (scroll_state == SCROLL_STATE_SCROLLING)
                                return;
                            if (dataBanners.size() <= 2) {
                                int p = currentPosition + 1;
                                if (p >= dataBanners.size()) p = 0;
                                inject.saSlides.currentItem(p);
                            } else {
                                inject.saSlides.currentItem(currentPosition + 1);
                            }

                        }
                    }
                });
            } else {
                if (interval != null)
                    interval.stop();
                interval = null;
            }
        }


    }
}
