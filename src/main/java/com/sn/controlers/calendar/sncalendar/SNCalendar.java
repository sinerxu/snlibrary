package com.sn.controlers.calendar.sncalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.sn.controlers.SNLinearLayout;
import com.sn.controlers.SNScrollable;
import com.sn.interfaces.SNCalendarListener;
import com.sn.lib.R;
import com.sn.main.SNElement;
import com.sn.models.SNInject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xuhui on 15/11/14.
 */
public class SNCalendar extends SNLinearLayout {
    class SNCalendarInject extends SNInject {
        SNElement viewHeaderDate;
        SNElement viewHeaderWeek;
        SNElement tvDate;
        SNElement calendarDayBox;
    }

    Calendar mCurrentCalendar;

    SNCalendarInject inject = new SNCalendarInject();
    SNElement $main;
    ArrayList<String> mWeekCodes;
    ArrayList<String> mMonthCodes;
    ArrayList<SNElement> mCalendarDays;
    int mDirect;

    public SNCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWeekCodes();
    }

    public SNCalendar(Context context, Calendar _calendar) {
        super(context);
        this.mCurrentCalendar = _calendar;
        initWeekCodes();
        initMonthCodes();
    }

    void updateShowCurentDate() {
        inject.tvDate.text($.util.strFormat("{0}  {1}  {2}  周{3}", $.util.dateToString(mCurrentCalendar, "yyyy"), mMonthCodes.get(mCurrentCalendar.get(Calendar.MONTH)), $.util.dateToString(mCurrentCalendar, "dd"), mWeekCodes.get($.util.dateWeek(mCurrentCalendar))));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if ($this.childCount() == 0) {
            $main = $.layoutInflateResId(R.layout.view_calendar, $this.toViewGroup());
            $main.inject(inject);
            updateShowCurentDate();
            for (int i = 0; i < mWeekCodes.size(); i++) {
                String item = mWeekCodes.get(i);
                int w = $this.width() / mWeekCodes.size();
                SNElement tvDay = $.create(new TextView($.getActivity()));
                tvDay.text(item);
                tvDay.textSize(12);
                tvDay.textColor(Color.WHITE);
                tvDay.backgroundColor(Color.TRANSPARENT);
                inject.viewHeaderWeek.add(tvDay);
                tvDay.gravity(Gravity.CENTER);
                tvDay.width(w);
            }

            mCalendarDays = new ArrayList<SNElement>();
            for (int i = 0; i < 3; i++) {
                int add_c;
                Calendar calendar = (Calendar) mCurrentCalendar.clone();
                calendar.add(Calendar.MONTH, i - 1);
                SNCalendarDayLayout calendarDay = new SNCalendarDayLayout($.getActivity(), calendar);
                calendarDay.setCalendarListener(new SNCalendarListener() {
                    @Override
                    public void onSelectDate(SNElement element, Calendar calendar) {
                        mCurrentCalendar = calendar;
                        updateShowCurentDate();
                        updateOther();
                    }

                    @Override
                    public void onDate(SNElement element, Calendar calendar) {

                    }
                });
                mCalendarDays.add($.create(calendarDay));
            }
            allowLoad();
            inject.calendarDayBox.bindScrollable(mCalendarDays);
            inject.calendarDayBox.toView(SNScrollable.class).setCurrentItem(1, false);
            inject.calendarDayBox.toView(SNScrollable.class).setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    mDirect = position - 1;
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == 0) {
                        $.util.logDebug(SNCalendar.class, "direct=" + mDirect);
                        Calendar currCalendar = (Calendar) mCurrentCalendar.clone();
                        currCalendar.add(Calendar.MONTH, mDirect);
                        mCalendarDays.get(1).toView(SNCalendarDayLayout.class).setSelectedDatetime(currCalendar);
                        $.util.logDebug(SNCalendar.class, $.util.dateToString(currCalendar, "yyyy/MM/dd"));
                        Calendar currCalendar_0 = (Calendar) currCalendar.clone();
                        currCalendar_0.add(Calendar.MONTH, -1);
                        Calendar currCalendar_2 = (Calendar) currCalendar.clone();
                        currCalendar_2.add(Calendar.MONTH, 1);
                        mCalendarDays.get(0).toView(SNCalendarDayLayout.class).setSelectedDatetime(currCalendar_0);
                        mCalendarDays.get(2).toView(SNCalendarDayLayout.class).setSelectedDatetime(currCalendar_2);
                        //update day
                        allowLoad();
                        updateAll();
                        inject.calendarDayBox.toView(SNScrollable.class).setCurrentItem(1, false);
                        mCurrentCalendar = currCalendar;
                        updateShowCurentDate();
                    }

                }
            });
        }
    }

    void updateAll() {
        for (SNElement item : mCalendarDays) {
            item.toView(SNCalendarDayLayout.class).showCalendarDays();
        }
    }

    void updateOther() {
        int index = 0;
        for (SNElement item : mCalendarDays) {
            if (index != 1) {
                Calendar calendar = item.toView(SNCalendarDayLayout.class).getSelectedDatetime();
                calendar.set(Calendar.DATE, mCurrentCalendar.get(Calendar.DATE));
                item.toView(SNCalendarDayLayout.class).showCalendarDays();
            }
            index++;
        }
    }

    void allowLoad() {
        mCalendarDays.get(1).toView(SNCalendarDayLayout.class).setAllowLoadDate(true);
    }

    void initWeekCodes() {
        if (mWeekCodes == null || mWeekCodes.size() == 0) {
            mWeekCodes = new ArrayList<String>();
//            mWeekCodes.add("Sun");
//            mWeekCodes.add("Mon");
//            mWeekCodes.add("Tue");
//            mWeekCodes.add("Wed");
//            mWeekCodes.add("Thu");
//            mWeekCodes.add("Fri");
//            mWeekCodes.add("Sat");
            mWeekCodes.add("日");
            mWeekCodes.add("一");
            mWeekCodes.add("二");
            mWeekCodes.add("三");
            mWeekCodes.add("四");
            mWeekCodes.add("五");
            mWeekCodes.add("六");
        }
    }

    void initMonthCodes() {
        if (mMonthCodes == null || mMonthCodes.size() == 0) {
            mMonthCodes = new ArrayList<String>();
//            mMonthCodes.add("Jan");
//            mMonthCodes.add("Feb");
//            mMonthCodes.add("Mar");
//            mMonthCodes.add("Apr");
//            mMonthCodes.add("May");
//            mMonthCodes.add("Jun");
//            mMonthCodes.add("Jul");
//            mMonthCodes.add("Aug");
//            mMonthCodes.add("Sep");
//            mMonthCodes.add("Oct");
//            mMonthCodes.add("Nov");
//            mMonthCodes.add("Dec");

            mMonthCodes.add("一月");
            mMonthCodes.add("二月");
            mMonthCodes.add("三月");
            mMonthCodes.add("四月");
            mMonthCodes.add("五月");
            mMonthCodes.add("六月");
            mMonthCodes.add("七月");
            mMonthCodes.add("八月");
            mMonthCodes.add("九月");
            mMonthCodes.add("十月");
            mMonthCodes.add("十一月");
            mMonthCodes.add("十二月");
        }
    }
}
