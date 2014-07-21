package vssnake.intervaltraining.interval;

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

    public long getTotalIntervalTime() {
        return totalIntervalTime;
    }

    public long getIntervalTime() {
        return intervalTime;
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
    private long totalIntervalTime;
    private long intervalTime;
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
        this.totalIntervalTime = totalIntervalTime;
        this.intervalTime = intervalTime;
        this.bpm = bpm;
    }
    public void intervalDone(){
        intervalDone = true;
    }
}
