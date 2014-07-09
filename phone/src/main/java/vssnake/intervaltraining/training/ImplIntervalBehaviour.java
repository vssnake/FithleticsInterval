package vssnake.intervaltraining.training;

/**
 * Created by unai on 04/07/2014.
 */
public class ImplIntervalBehaviour implements IntervalBehaviour {

    int totalIntervals = 0;
    long timeToRest = 0;
    long timeToExercise = 0;

    int currentInterval = 1;
    long totalIntervalsTime = 0; //Current Time in the Interval
    long lastIntervalsTime = 0;
    long currentIntervalTime = 0;

    long currentIntervalSpace = 0;//The wide of the current part of interval in milliseconds

    IntervalData_Base.eIntervalState intervalState = IntervalData_Base.eIntervalState.RUNNING;

    IntervalData_Base mIntervalData;

    IntervalServiceConnector iIntervalService;

    boolean finish= false;

    int[] soundTimeArray;
    int [] temporalSoundArray;

    private ImplIntervalBehaviour(int totalIntervals, long timeToRest,
                                  long timeToExercise,IntervalServiceConnector iIntervalService,
                                  int[] soundsTimeArray){
        this.totalIntervals = totalIntervals;
        this.timeToRest = timeToRest;
        this.timeToExercise = timeToExercise;
        currentIntervalSpace = timeToExercise;
        this.iIntervalService = iIntervalService;



        this.soundTimeArray = soundsTimeArray;

        this.temporalSoundArray = soundsTimeArray.clone();

        mIntervalData = new IntervalData_Base();
    }

    public static ImplIntervalBehaviour
                    newInstance (int totalIntervals, int timeToRest,
                    int timeToExercise,IntervalServiceConnector iIntervalService,
                    int[] soundsTimeArray){
        return new ImplIntervalBehaviour(totalIntervals,
                timeToRest,timeToExercise,iIntervalService,soundsTimeArray);
    }

    /***
     * @Override
     * @param time In milliseconds
     */
    public void executeTime(long time) {

        totalIntervalsTime = time;
        currentIntervalTime = totalIntervalsTime - lastIntervalsTime;


        if (currentIntervalSpace <= currentIntervalTime){

            //Set again the sound array
            temporalSoundArray = soundTimeArray.clone();

            //Reset the new intervalTime + the time overhead

            currentIntervalTime -=currentIntervalSpace;
            lastIntervalsTime = totalIntervalsTime - currentIntervalTime;

            iIntervalService.specialCommand(IntervalServiceConnector.specialsCommands.sound,2);

            if (currentIntervalSpace == timeToRest){
                //The user is going to rest
                intervalState = IntervalData_Base.eIntervalState.RUNNING;
                currentIntervalSpace = timeToExercise;
                currentInterval++;

                if (currentInterval > totalIntervals){
                    finish = true;
                    mIntervalData.intervalDone();
                    iIntervalService.endTrain();
                    return;
                }
            }else{
                //The user is going to make exercise
                intervalState = IntervalData_Base.eIntervalState.RESTING;
                currentIntervalSpace = timeToRest;
            }
        }else{
            if (temporalSoundArray.length != 0) {
                int intervalTime =  (int)(currentIntervalSpace - currentIntervalTime);
                for (int i = 0; i < temporalSoundArray.length; i++) {
                    if (intervalTime <= temporalSoundArray[i]){
                        temporalSoundArray[i]=-1;
                        iIntervalService.specialCommand(
                                IntervalServiceConnector.specialsCommands.sound, 1);
                    }
                }

            }



        }
        mIntervalData.setIntervalData(currentInterval,totalIntervals,
                intervalState,totalIntervalsTime,currentIntervalTime,0);
        //TODO make notifications and show data to the interface
        iIntervalService.newNotification(mIntervalData);
    }

    @Override
    public boolean stopInterval() {
        return false;
    }

    @Override
    public boolean resetInterval() {
        currentInterval = 1;
        currentIntervalTime = 0;
        totalIntervalsTime = 0;
        lastIntervalsTime = 0;
        currentIntervalSpace = timeToExercise;
        finish = false;
        intervalState = IntervalData_Base.eIntervalState.RUNNING;
        return true;
    }
}
