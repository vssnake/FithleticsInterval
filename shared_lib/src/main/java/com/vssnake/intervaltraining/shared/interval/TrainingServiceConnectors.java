package com.vssnake.intervaltraining.shared.interval;

/**
 * Created by unai on 17/07/2014.
 */
public class TrainingServiceConnectors {

    public enum specialUICommands {REST,RUN, END_TRAINING}

    public interface TrainingInterface {
        public void statusTrain(boolean status);
        public void specialEvent(specialUICommands commands);
    }

    public interface IntervalInterface extends TrainingInterface {

        public void changeIntervalMode(String mode);
        public void changeTime(int secondsTotal,int secondsInterval);
        public void changeInterval(int numberInterval,int totalInterval);


    }
}
