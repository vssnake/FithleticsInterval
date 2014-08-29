package vssnake.intervaltraining.behaviours;

/**
 * Created by unai on 04/07/2014.
 */
public interface IntervalBehaviour {
    void executeTime(long time);
    boolean stopInterval();
    boolean resetInterval();
    boolean changeTraining(long id);
}
