package com.sn.controlers.calendar.sncalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sn.controlers.SNRelativeLayout;
import com.sn.interfaces.SNCalendarListener;
import com.sn.interfaces.SNOnClickListener;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xuhui on 15/11/14.
 */
public class SNCalendarDayLayout extends SNRelativeLayout {

    public SNCalendarDayLayout(Context context, Calendar showDate) {
        super(context);
        this.mSelectedDatetime = showDate;

        $this.backgroundColor(Color.rgb(245, 233, 255));
    }

    /**
     * 是否需要加载date
     *
     * @param mIsLoadDate
     */
    public void setAllowLoadDate(boolean mIsLoadDate) {
        this.mAllowLoadDate = mIsLoadDate;
    }

    boolean mAllowLoadDate = false;

    public void setCalendarListener(SNCalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    SNCalendarListener calendarListener;

    public Calendar getSelectedDatetime() {
        return mSelectedDatetime;
    }

    public void setSelectedDatetime(Calendar mSelectedDatetime) {
        this.mSelectedDatetime = mSelectedDatetime;
    }

    Calendar mSelectedDatetime;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ($this.childCount() == 0) {
            showCalendarDays();
        }
    }

    public void showCalendarDays() {
        ArrayList<String> days = new ArrayList<String>();
        int day = $.util.dateDayOfMonth(mSelectedDatetime);
        int cDay = 1;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 7; x++) {
                if (cDay > day) {
                    days.add("");
                } else {
                    Calendar d = $.util.dateInstance(mSelectedDatetime.get(Calendar.YEAR), mSelectedDatetime.get(Calendar.MONTH), cDay);
                    int week = $.util.dateWeek(d);
                    if (week != x) {
                        days.add("");
                    } else {
                        days.add($.util.strParse(cDay));
                        cDay++;
                    }
                }
            }
        }
        int boxSize = $this.width() / 7;
        $this.height(boxSize * 6);
        int size = $.px(24);
        if ($this.childCount() == 0) {
            for (int i = 0; i < days.size(); i++) {
                int row = ((int) (i / 7));
                int y = row * boxSize;
                int x = (i - row * 7) * boxSize;
                SNElement box = $.create(new RelativeLayout($this.getActivity()));
                int cx = (boxSize - size) / 2;
                SNElement tvDay = $.create(new TextView($this.getActivity()));
                tvDay.textSize(14);
                tvDay.text("");
                int cirSize = size / 3;
                SNElement cir = $.create(new LinearLayout($this.getActivity()));
                //cir.backgroundColor(Color.rgb(255, 124, 0));
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.rgb(255, 124, 0));
                gd.setCornerRadius(cirSize / 2);
                cir.background(gd);
                box.add(tvDay);
                box.add(cir);
                $this.add(box);
                box.width(boxSize);
                box.height(boxSize);
                box.marginLeft(x);
                box.marginTop(y);
                tvDay.marginTop(cx);
                tvDay.marginLeft(cx);
                tvDay.height(size);
                tvDay.width(size);
                tvDay.gravity(Gravity.CENTER);
                cir.width(cirSize);
                cir.height(cirSize);
                cir.marginLeft(cx + size / 3 * 2);
                cir.marginTop(cx - cirSize / 4);
                cir.visible($.SN_UI_NONE);
                tvDay.click(new SNOnClickListener() {
                    @Override
                    public void onClick(SNElement view) {
                        String v = view.text();
                        if ($.util.strIsNotNullOrEmpty(v)) {
                            int d = Integer.parseInt(v);
                            mSelectedDatetime = $.util.dateInstance(mSelectedDatetime.get(Calendar.YEAR), mSelectedDatetime.get(Calendar.MONTH), d);// new DateTime(datetime.Year, datetime.Month, d);
                            mAllowLoadDate = true;
                            showCalendarDays();
                            if (calendarListener != null)
                                calendarListener.onSelectDate(view.parent(), mSelectedDatetime);
                        }
                    }
                });
            }
        }
        for (int i = 0; i < days.size(); i++) {
            String item = days.get(i);
            SNElement box = $this.childAt(i);
            SNElement tvDay = box.childAt(0);
            SNElement cir = box.childAt(1);
            tvDay.text(item);
            tvDay.textColor(Color.BLACK);
            tvDay.backgroundColor(Color.TRANSPARENT);
            cir.visible($.SN_UI_NONE);
            if ($.util.strIsNotNullOrEmpty(item)) {
                int dd = Integer.parseInt(item);
                Calendar curr_datetime = $.util.dateInstance(mSelectedDatetime.get(Calendar.YEAR), mSelectedDatetime.get(Calendar.MONTH), dd);
                if (mAllowLoadDate == true && calendarListener != null) {
                    calendarListener.onDate(box, curr_datetime);
                    if (dd == 5) {
                        GradientDrawable gd = new GradientDrawable();
                        gd.setCornerRadius(size / 2);
                        gd.setColor(Color.rgb(217, 217, 217));
                        tvDay.background(gd);
                        cir.visible($.SN_UI_VISIBLE);
                    }
                }
                if ($.util.dateToString(curr_datetime, "yyyy/MM/dd").equals($.util.dateToString(Calendar.getInstance(), "yyyy/MM/dd"))) {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setCornerRadius(size / 2);
                    gd.setColor(Color.rgb(224, 30, 53));
                    tvDay.background(gd);
                    tvDay.textColor(Color.WHITE);
                }
                if (dd == mSelectedDatetime.get(Calendar.DATE)) {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setCornerRadius(size / 2);
                    gd.setColor(Color.BLACK);
                    tvDay.background(gd);
                    tvDay.textColor(Color.WHITE);
                }
            }
        }
        mAllowLoadDate = false;
    }
}
