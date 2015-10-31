package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sn.lib.R;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhui on 15/8/8.
 */
public class SNScrollable extends SNViewPager {

    private AwesomePagerAdapter awesomeAdapter;
    String LCAT = "SNScrollable Log";

    private List<View> mListViews = new ArrayList<View>();
    boolean isAutoHeight = false;
    boolean isShowDot = false;
    public List<SNElement> $contentList;

    public SNScrollable(Context context, AttributeSet attrs) {
        super(context, attrs);
        $contentList = new ArrayList<SNElement>();
        //DEFAULT_OFFSCREEN_PAGES
        awesomeAdapter = new AwesomePagerAdapter();
        TypedArray ta = $.loadStyle(attrs, R.styleable.SNScrollable);
        isAutoHeight = ta.getBoolean(R.styleable.SNScrollable_layout_auto_height, false);
        ta.recycle();

    }


    public void bindContent() {
        ArrayList<SNElement> _contentList = new ArrayList<SNElement>();
        int childCount = getChildCount();
        //Log.d(LCAT, SNUtility.format("childCount {0}", childCount));
        if (mListViews.size() == 0 && getChildCount() > 0) {
            for (int i = 0; i < childCount; i++) {
                _contentList.add($.create(getChildAt(i)));
            }
            bindContent(_contentList);
        }
    }


    public void bindContent(List<SNElement> elements) {
        if ($contentList.size() == 0 && mListViews.size() == 0) {
            for (SNElement item : elements) {
                $contentList.add(item);
                mListViews.add(item.toView());
            }
            SNScrollable.this.removeAllViews();
            SNScrollable.this.setAdapter(awesomeAdapter);
        } else
            throw new IllegalStateException(
                    "The SNScrollable already bind adapter.");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isAutoHeight) {
            int height = 0;
            //下面遍历所有child的高度
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec,
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                int w = child.getMeasuredWidth();
                //Log.e(LCAT, SNUtility.format("child height:{0},child width:{0}", h, w));
                if (h > height) //采用最大的view的高度。
                    height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.EXACTLY);
            //Log.e(LCAT, SNUtility.format("widthMeasureSpec:{1},heightMeasureSpec:{0}", heightMeasureSpec, widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        bindContent();
    }

    private class AwesomePagerAdapter extends PagerAdapter {


        public AwesomePagerAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Log.d(LCAT, "~instantiateItem");
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }
    }
}
