package com.sn.interfaces;

import com.sn.main.SNElement;

import java.util.Calendar;

/**
 * Created by xuhui on 15/11/14.
 */
public interface SNCalendarListener {
    void onSelectDate(SNElement element, Calendar calendar);
    void onDate(SNElement element, Calendar calendar);
}
