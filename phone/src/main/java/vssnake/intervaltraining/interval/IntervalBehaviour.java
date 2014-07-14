package vssnake.intervaltraining.interval;

/**
 * Created by unai on 04/07/2014.
 */
public interface IntervalBehaviour {
    void executeTime(long time);
    boolean stopInterval();
    boolean resetInterval();
}
