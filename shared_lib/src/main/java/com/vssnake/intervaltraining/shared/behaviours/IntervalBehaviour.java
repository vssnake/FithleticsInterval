package com.vssnake.intervaltraining.shared.behaviours;

import android.content.Context;

/**
 * Created by unai on 04/07/2014.
 */
public interface IntervalBehaviour {
    void executeTime(long time);
    boolean stopInterval();
    boolean resetInterval();
    boolean changeTraining(long id,Context context);
}
