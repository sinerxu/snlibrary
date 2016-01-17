package com.sn.controlers.slidingtab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.sn.controlers.SNRelativeLayout;
import com.sn.main.SNElement;

import java.util.ArrayList;

/**
 * Created by xuhui on 15/8/11.
 */
public class SNSlidingTabItemBox extends SNRelativeLayout {


    String LCAP = "SNSlidingTabItemBox Log";

    public ArrayList<SNElement> $itemList;
    public int itemWidth = 0;
    public int itemHeight = 0;
    public SNElement $hover;

    public SNSlidingTabItemBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        $itemList = new ArrayList<SNElement>();
    }


    public void initChild() {
        if ($itemList.size() == 0) {
            int childCount = getChildCount();
            if (childCount == 0) return;
            if (childCount <= 1)
                throw new IllegalStateException(
                        "The childCount of SNSlidingTabItemBox must be >=1.");
            $hover = $.create(getChildAt(childCount - 1));
            if (!($hover.toView() instanceof SNSlidingTabItemHover))
                throw new IllegalStateException(
                        "The last item must be SNSlidingTabItemHover.");
            for (int i = 0; i < childCount - 1; i++) {
                SNElement item = $.create(getChildAt(i));
                $itemList.add(item);
            }
            updateSize();
        }
    }

    public void updateSize() {
        isLoadSize = true;
        int childCount = getChildCount();
        itemWidth = $this.width() / (childCount - 1);
        itemHeight = $this.height();
        $hover.width(itemWidth);
        $hover.height(itemHeight);
        for (int i = 0; i < childCount - 1; i++) {
            SNElement item = $.create(getChildAt(i));
            item.marginLeft(itemWidth * i);
            item.width(itemWidth);
            item.height(itemHeight);
        }
        onItemLoadFinish();
    }

    boolean isLoadSize = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initChild();
        updateSize();
    }

    public void onItemLoadFinish() {
        Log.d(LCAP, "onItemLoadFinish!~");
    }
}
