package com.sn.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sn.lib.R;
import com.sn.main.SNElement;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xuhui on 15/11/14.
 */
public class SNCalendarDaysFragment extends SNFragment {
    SNElement daysBox;

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    Date currentDate;

    public static SNCalendarDaysFragment instance(Date _date) {
        SNCalendarDaysFragment fragment = new SNCalendarDaysFragment();
        fragment.setCurrentDate(_date);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        daysBox = $.create(inflater.inflate(R.layout.view_calendar_days, container, false));
        return daysBox.toView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //updateDays();
    }

    /**
     *

    void updateDays() {
        ArrayList<String> days = new ArrayList<String>();
        int day = $.util.dateDayOfMonth(currentDate);
        int cDay = 1;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 7; x++) {
                if (cDay > day) {
                    days.add("");
                } else {
                    Date d = new Date(currentDate.getDate(), currentDate.getMonth() + 1, cDay);
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

        int boxSize = daysBox.width() / 7;
        daysBox.backgroundColor(Color.BLUE);
        daysBox.height(boxSize * 6);
        int size = $.px(24);
        if (daysBox.childCount() == 0) {
            for (int i = 0; i < days.size(); i++) {
                int row = ((int) (i / 7));
                int y = row * boxSize;
                int x = (i - row * 7) * boxSize;
                SNElement box = $.create(new RelativeLayout(getActivity()));
                int cx = (boxSize - size) / 2;
                SNElement tvDay = $.create(new TextView(getActivity()));
                tvDay.textSize(14);
                tvDay.text("5");
                int cirSize = 8;
                SNElement cir = $.create(new LinearLayout(getActivity()));
                cir.backgroundColor(Color.rgb(255, 124, 0));
                box.add(tvDay);
                box.add(cir);
                daysBox.add(box);
                box.width(boxSize);
                box.height(boxSize);
                cir.width(cirSize);
                cir.height(cirSize);
                cir.left(cx + size / 3 * 2);
                cir.right(cx - cirSize / 4);

            }
        }
    }
     */
}
