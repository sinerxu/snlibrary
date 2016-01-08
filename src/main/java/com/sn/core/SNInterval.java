package com.sn.core;

import com.sn.interfaces.SNIntervalListener;
import com.sn.interfaces.SNThreadListener;
import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/12/26.
 */
public class SNInterval {

    boolean isRunning;
    int runCount = 0;
    int runFinishCount = 0;
    int duration;


    public SNInterval() {

    }


    public void start(int duration, SNIntervalListener intervalListener) {
        if (isRunning) return;
        this.duration = duration;
        isRunning = true;

        run(intervalListener);
    }

    public void stop() {
        isRunning = false;
        runCount = 0;
        runFinishCount = 0;

    }

    void run(final SNIntervalListener intervalListener) {
        if (isRunning) {
            runCount++;
            SNUtility.instance().threadRun(new SNThreadListener() {
                @Override
                public Object run() {
                    try {
                        Thread.sleep(duration);
                    } catch (Exception ex) {
                    }
                    return null;
                }

                @Override
                public void onFinish(Object object) {
                    intervalListener.onInterval(SNInterval.this);
                    runFinishCount++;
                    SNInterval.this.run(intervalListener);
                }
            });
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getRunCount() {
        return runCount;
    }

    public int getRunFinishCount() {
        return runFinishCount;
    }

    public int getDuration() {
        return duration;
    }


}
