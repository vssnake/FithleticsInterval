package vssnake.intervaltraining.training;

import android.util.Log;

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

    private ImplIntervalBehaviour(int totalIntervals, long timeToRest,
                                  long timeToExercise,IntervalServiceConnector iIntervalService){
        this.totalIntervals = totalIntervals;
        this.timeToRest = timeToRest;
        this.timeToExercise = timeToExercise;
        currentIntervalSpace = timeToExercise;
        this.iIntervalService = iIntervalService;
        mIntervalData = new IntervalData_Base();
    }

    public static ImplIntervalBehaviour
                    newInstance (int totalIntervals, int timeToRest,
                    int timeToExercise,IntervalServiceConnector iIntervalService){
        return new ImplIntervalBehaviour(totalIntervals,
                timeToRest,timeToExercise,iIntervalService);
    }

    boolean tryone = false;
    boolean trytwo = false;
    boolean trythree = false;
    /***
     * @Override
     * @param time In milliseconds
     */
    public void executeTime(long time) {
        totalIntervalsTime = time;
        currentIntervalTime = totalIntervalsTime - lastIntervalsTime;
        /*Log.i("IntervalBehaviour", totalIntervalsTime + " Total Time |" +
                currentIntervalTime + " Current IntervalTime |"  +
            intervalState.name().toString() + " Interval State |" +
            currentInterval + " Current Interval");*/


        if (currentIntervalSpace <= currentIntervalTime){
            //Reset the new intervalTime + the time overhead

            currentIntervalTime -=currentIntervalSpace;
            lastIntervalsTime = totalIntervalsTime - currentIntervalTime;

            iIntervalService.specialCommand(IntervalServiceConnector.specialsCommands.sound,
                    new Integer(2));
            tryone = false; trytwo = false; trythree = false;

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
            if (!trythree && (currentIntervalSpace - currentIntervalTime)<=3000){
                trythree = true;
                iIntervalService.specialCommand(IntervalServiceConnector.specialsCommands.sound,
                        new Integer(1));
            }
            if (!trytwo && (currentIntervalSpace - currentIntervalTime)<=2000){
                trytwo = true;
                iIntervalService.specialCommand(IntervalServiceConnector.specialsCommands.sound,
                        new Integer(1));
            }
            if (!tryone && (currentIntervalSpace - currentIntervalTime)<=1000){
                tryone = true;
                iIntervalService.specialCommand(IntervalServiceConnector.specialsCommands.sound,
                        new Integer(1));
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
        finish = false;
        intervalState = IntervalData_Base.eIntervalState.RUNNING;
        return true;
    }
}
