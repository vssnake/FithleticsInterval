package vssnake.intervaltraining.interval;

/**
 * Created by unai on 04/07/2014.
 */
public interface TrainingServiceInterface {
    //Specials Commands for service
    public enum specialsCommands {
        SOUND, VIBRATION, END_INTERVAL, RUN, REST
    }
    void newNotification(IntervalData_Base intervalData);
    void endTrain();
    void specialCommand(specialsCommands commands,Object adicionalData);
}
