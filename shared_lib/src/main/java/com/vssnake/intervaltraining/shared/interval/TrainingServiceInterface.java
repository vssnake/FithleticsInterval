package com.vssnake.intervaltraining.shared.interval;

import com.vssnake.intervaltraining.shared.model.IntervalData;

/**
 * Created by unai on 04/07/2014.
 */
public interface TrainingServiceInterface {
    //Specials Commands for service
    public enum specialsCommands {
        SOUND, VIBRATION, END_INTERVAL, RUN, REST
    }
    void newNotification(IntervalData intervalData);
    void endTrain();
    void specialCommand(specialsCommands commands,Object adicionalData);
    boolean isTrainStarted();
}
