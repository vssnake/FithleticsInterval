package vssnake.intervaltraining.interval;

import vssnake.intervaltraining.utils.Utils;

/**
 * Created by unai on 04/07/2014.
 */
public class IntervalData {

    public enum intervalDataKey{
        INTERVAL_TIME,
        TOTAL_INTERVAL_TIME,
        NUMBER_INTERVAL,
        TOTAL_INTERVALS,
        INTERVAL_STATE,

    }
    public int getTotalIntervalTimeSeconds() {
        return totalIntervalTimeSeconds;
    }

    public int getIntervalTimeSeconds() {
        return intervalTimeSeconds;
    }


    public int getNumberInterval() {
        return numberIntervals;
    }

    public int getTotalIntervals() {
        return totalIntervals;
    }

    public eIntervalState getIntervalState() {
        return intervalState;
    }

    public int getBpm() {
        return bpm;
    }

    public long getTotalIntervalTimeMilliseconds() {
        return totalIntervalTimeMilliseconds;
    }

    public long getIntervalTimeMilliseconds() {
        return intervalTimeMilliseconds;
    }

    public boolean isIntervalDone() {
        return intervalDone;
    }

    public enum eIntervalState{
        RUNNING,
        RESTING,
        NOTHING
    }

    private int numberIntervals;
    private int totalIntervals;
    private eIntervalState intervalState;
    private long totalIntervalTimeMilliseconds;
    private long intervalTimeMilliseconds;

    private int totalIntervalTimeSeconds;
    private int intervalTimeSeconds;
    //Beats per minute
    private int bpm;

    private boolean intervalDone;

    public IntervalData(){   }

    public void setIntervalData(int numberInterval, int totalIntervals,
                           eIntervalState intervalState,
                           long totalIntervalTime,
                           long intervalTime,
                           int bpm){
        this.numberIntervals = numberInterval;
        this.totalIntervals = totalIntervals;
        this.intervalState = intervalState;
        this.totalIntervalTimeMilliseconds = totalIntervalTime;
        this.intervalTimeMilliseconds = intervalTime;
        this.totalIntervalTimeSeconds = Utils.convertMillisecondsToSeconds(totalIntervalTime);
        this.intervalTimeSeconds = Utils.convertMillisecondsToSeconds(intervalTime);
        this.bpm = bpm;
    }
    public void intervalDone(){
        intervalDone = true;
    }
}
