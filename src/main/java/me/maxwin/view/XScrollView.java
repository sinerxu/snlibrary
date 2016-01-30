package me.maxwin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.sn.main.SNElement;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 16/1/29.
 */
public class XScrollView extends ScrollView {
    ScrollView self;
    SNElement $this;
    private final int MAX_HEADER_HEIGHT = 50;
    private int mBeginMarginTop = 0;
    private int mBeginY = 0;
    SNManager $;


    public XScrollView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        $.util.logInfo(XScrollView.class, $.util.strFormat("{0},{1},{2},{3}", l, t, oldl, oldt));
    }

    private void initWithContext(Context context) {
        self = this;
        $ = new SNManager(context);
        $this = $.create(this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (self.getScrollY() == 0) {
                            int interval = ((int) motionEvent.getY() - mBeginY) / 5;
                            int maxheader = $.px(MAX_HEADER_HEIGHT);
                            int currTop = $this.margins().getTop();
                            int offsetTop = currTop - mBeginMarginTop;
                            int finalTop = currTop + interval;
                            if (finalTop <= mBeginMarginTop) finalTop = mBeginMarginTop;
                            if (offsetTop <= maxheader) {
                                $.util.logInfo(XScrollView.class, "animate:" + offsetTop);
                            }
                            $this.marginTop(finalTop);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mBeginY = (int) motionEvent.getY();
                        mBeginMarginTop = $this.margins().getTop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        $this.marginTop(mBeginMarginTop);
                        break;
                }
                return true;
            }
        });
    }
}
