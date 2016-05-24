package com.sn.core;

import android.animation.ValueAnimator;
import android.widget.ProgressBar;

import com.sn.main.SNElement;

/**
 * Created by xuhui on 16/5/24.
 */
public class ProgressBarManager {


    SNElement progressBar;


    ValueAnimator currentValueAnimator;

    public static ProgressBarManager instance(SNElement progressBar) {
        return new ProgressBarManager(progressBar);
    }


    ProgressBarManager(SNElement progressBar) {
        this.progressBar = progressBar;
        progressBar.toView(ProgressBar.class).setMax(100);
        progressBar.toView(ProgressBar.class).setProgress(0);
    }

    public void progress(int val) {
        this.progress(val, true);
    }

    public void progress(int val, boolean animated) {
        if (val < 0) val = 0;
        if (val > 100) val = 100;
        if (!animated) {
            progressBar.toView(ProgressBar.class).setProgress(val);
        } else {
            if (currentValueAnimator != null)
                currentValueAnimator.cancel();
            currentValueAnimator = ValueAnimator.ofInt(progressBar.toView(ProgressBar.class).getProgress(), val);
            currentValueAnimator.setDuration(500);
            currentValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int v = Integer.parseInt(animation.getAnimatedValue().toString());
                    progressBar.toView(ProgressBar.class).setProgress(v);
                }
            });
            currentValueAnimator.start();
        }
    }
}
