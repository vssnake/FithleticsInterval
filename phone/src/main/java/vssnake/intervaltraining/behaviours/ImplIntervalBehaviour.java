package vssnake.intervaltraining.behaviours;

import vssnake.intervaltraining.interval.IntervalBehaviour;
import vssnake.intervaltraining.interval.IntervalData_Base;
import vssnake.intervaltraining.interval.IntervalServiceInterface;

/**
 * Created by unai on 04/07/2014.
 */
public class ImplIntervalBehaviour implements IntervalBehaviour {

    int mTotalIntervals = 0;
    long mTimeToRest = 0;
    long mTimeToExercise = 0;

    int mCurrentInterval = 1;
    long mTotalIntervalsTime = 0; //Current Time in the Interval
    long mLastIntervalsTime = 0;
    long mCurrentIntervalTime = 0;

    long mCurrentIntervalSpace = 0;//The wide of the current part of interval in milliseconds

    IntervalData_Base.eIntervalState intervalState = IntervalData_Base.eIntervalState.RUNNING;

    IntervalData_Base mIntervalData;

    IntervalServiceInterface mIntervalServiceInterface;

    boolean mFinish = false;

    int[] mSoundTimeArray;
    int [] mTemporalSoundArray;

    long mMillisecondsTotal;

    private ImplIntervalBehaviour(int totalIntervals, long timeToRest,
                                  long timeToExercise,IntervalServiceInterface iIntervalService,
                                  int[] soundsTimeArray){
        this.mTotalIntervals = totalIntervals;
        this.mTimeToRest = timeToRest;
        this.mTimeToExercise = timeToExercise;
        mCurrentIntervalSpace = timeToExercise;
        this.mIntervalServiceInterface = iIntervalService;

        mMillisecondsTotal = (totalIntervals * timeToExercise) + ((totalIntervals-1) * timeToRest);



        this.mSoundTimeArray = soundsTimeArray;

        this.mTemporalSoundArray = soundsTimeArray.clone();

        mIntervalData = new IntervalData_Base();
    }

    public static ImplIntervalBehaviour
                    newInstance (int totalIntervals, int timeToRest,
                    int timeToExercise,IntervalServiceInterface iIntervalService,
                    int[] soundsTimeArray){
        return new ImplIntervalBehaviour(totalIntervals,
                timeToRest,timeToExercise,iIntervalService,soundsTimeArray);
    }

    /***
     * @Override
     * @param time In milliseconds
     */
    public void executeTime(long time) {


        if (mIntervalServiceInterface == null){
            return;
        }
        mTotalIntervalsTime = time;
        mCurrentIntervalTime = mTotalIntervalsTime - mLastIntervalsTime;


        if (mCurrentIntervalSpace <= mCurrentIntervalTime){

            //Set again the SOUND array
            mTemporalSoundArray = mSoundTimeArray.clone();

            //Reset the new intervalTime + the time overhead

            mCurrentIntervalTime -= mCurrentIntervalSpace;
            mLastIntervalsTime = mTotalIntervalsTime - mCurrentIntervalTime;

            mIntervalServiceInterface.specialCommand(IntervalServiceInterface.specialsCommands.SOUND, 2);


            if (mCurrentIntervalSpace == mTimeToRest){
                //The user is going to REST
                intervalState = IntervalData_Base.eIntervalState.RUNNING;
                mCurrentIntervalSpace = mTimeToExercise;
                mCurrentInterval++;

                //Send command to service to inform what the interval has changes the state to REST
                mIntervalServiceInterface.specialCommand(IntervalServiceInterface.specialsCommands.REST, null);


            }else{
                //Send command to service to inform what the interval has changes the state to RUN
                mIntervalServiceInterface.specialCommand(IntervalServiceInterface.specialsCommands.RUN, null);

                if (mCurrentInterval >= mTotalIntervals){
                    mFinish = true;
                    mIntervalData.intervalDone();
                    mIntervalServiceInterface.endTrain();
                    return;
                }
                //The user is going to make exercise
                intervalState = IntervalData_Base.eIntervalState.RESTING;
                mCurrentIntervalSpace = mTimeToRest;
            }
        }else{
            if (mTemporalSoundArray.length != 0) {
                int intervalTime =  (int)(mCurrentIntervalSpace - mCurrentIntervalTime);
                for (int i = 0; i < mTemporalSoundArray.length; i++) {
                    if (intervalTime <= mTemporalSoundArray[i]){
                        mTemporalSoundArray[i]=-1;
                        mIntervalServiceInterface.specialCommand(
                                IntervalServiceInterface.specialsCommands.SOUND, 1);
                    }
                }

            }

        }
        mIntervalData.setIntervalData(mCurrentInterval, mTotalIntervals,
                intervalState, mMillisecondsTotal - mTotalIntervalsTime, mCurrentIntervalSpace - mCurrentIntervalTime,0);
        //TODO make notifications and show data to the interface
        mIntervalServiceInterface.newNotification(mIntervalData);
    }

    @Override
    public boolean stopInterval() {
        return false;
    }

    @Override
    public boolean resetInterval() {
        mCurrentInterval = 1;
        mCurrentIntervalTime = 0;
        mTotalIntervalsTime = 0;
        mLastIntervalsTime = 0;
        mCurrentIntervalSpace = mTimeToExercise;
        mFinish = false;
        intervalState = IntervalData_Base.eIntervalState.RUNNING;
        return true;
    }
}
