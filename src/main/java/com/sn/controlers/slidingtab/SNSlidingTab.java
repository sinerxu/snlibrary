package com.sn.controlers.slidingtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.sn.controlers.SNRelativeLayout;
import com.sn.interfaces.SNOnClickListener;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNSlidingTab extends SNRelativeLayout {
    String LCAP = "SNSlidingTab Log";
    SNElement $itemBox;

    public SNElement get$itemBox() {
        return $itemBox;
    }


    public SNElement get$content() {
        return $content;
    }


    public SNSlidingTabItemBox getItemBox() {
        return itemBox;
    }


    public SNSlidingTabContainer getContent() {
        return content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getSlidingState() {
        return slidingState;
    }


    SNElement $content;
    SNSlidingTabItemBox itemBox;
    SNSlidingTabContainer content;
    int direct = 0;
    int x = 0;


    /**
     * 实时页
     */
    int currentPage = 0;

    /**
     * 0休息
     * 1手滑动中
     * 2自动滑动中
     */
    int slidingState = 0;

    public SNSlidingTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);
        initChild();
    }

    void initChild() {
        if ($itemBox == null) {
            int childCount = getChildCount();
            if (childCount != 2)
                throw new IllegalStateException(
                        "The childCount of SNSlidingTab must be 3.");

            View firstView = getChildAt(0);
            View lastView = getChildAt(1);
            if (!(firstView instanceof SNSlidingTabContainer) && !(firstView instanceof SNSlidingTabItemBox))
                throw new IllegalStateException(
                        "The child of SNSlidingTab must be SNSlidingTabContent or SNSlidingTabItemBox.");
            if (!(lastView instanceof SNSlidingTabContainer) && !(lastView instanceof SNSlidingTabItemBox))
                throw new IllegalStateException(
                        "The child of SNSlidingTab must be SNSlidingTabContent or SNSlidingTabItemBox.");
            if (firstView instanceof SNSlidingTabContainer) {
                $content = $.create(firstView);
                $itemBox = $.create(lastView);
            } else {
                $content = $.create(lastView);
                $itemBox = $.create(firstView);
            }
            itemBox = $itemBox.toView(SNSlidingTabItemBox.class);
            content = $content.toView(SNSlidingTabContainer.class);

            content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    SNSlidingTab.this.currentPage = position;
                    //Log.d(LCAP, SNUtility.format("onPageScrolled position = {2} positionOffsetPixels = {0} , positionOffsetPixels = {1}", positionOffset, positionOffsetPixels, position));
                    if (positionOffsetPixels != 0) {
                        itemBox.$hover.marginLeft(getX(positionOffsetPixels));
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    //Log.d(LCAP, "onPageSelected position==" + position + "===" + itemBox.itemWidth);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    //Log.d(LCAP, "onPageScrollStateChanged state==" + state);
                    slidingState = state;
                    if (slidingState == 0) {
                        itemBox.$hover.marginLeft(getX(0));
                        onPage(currentPage, itemBox.$itemList.get(currentPage), content.fragments.get(currentPage));
                    }
                }
            });
            this.currentPage = content.getCurrentItem();
            onPage(currentPage, itemBox.$itemList.get(currentPage), content.fragments.get(currentPage));

            if (itemBox.$itemList != null && itemBox.$itemList.size() > 0) {
                for (int i = 0; i < itemBox.$itemList.size(); i++) {
                    SNElement item = itemBox.$itemList.get(i);
                    item.tag(i);
                    //Log.d(LCAP, "set item.click");
                    item.click(new SNOnClickListener() {
                        @Override
                        public void onClick(SNElement view) {
                            int tag = Integer.parseInt(view.tag().toString());
                            setCurrentPage(tag, false);
                        }
                    });
                }
            }
            //Log.d(LCAP, "itemBox.$itemList===" + itemBox.$itemList.size());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setCurrentPage(int currentPage) {
        setCurrentPage(currentPage, false);
    }

    public void setCurrentPage(int currentPage, boolean isScroll) {
        this.currentPage = currentPage;
        content.setCurrentItem(currentPage, isScroll);
        if (!isScroll)
            itemBox.$hover.marginLeft(getX(0));
        onPage(currentPage, itemBox.$itemList.get(currentPage), content.fragments.get(currentPage));
    }

    /**
     * 当滑动到指定页面时，会调用这个方法
     * @param _page
     * @param _item
     * @param _content
     */
    public void onPage(int _page, SNElement _item, Fragment _content) {

    }

    int getX(int p) {
        if ($itemBox != null && $content != null) {
            float _p = (float) p + ($content.width() * currentPage);
            int result = (int) (_p / $content.width() * itemBox.itemWidth);
            //Log.d(LCAP, SNUtility.format("{0}/{1}*{2}={3}", _p, $content.width(), itemBox.itemWidth, result));
            return result;
        } else return 0;
    }
}
