package com.vssnake.intervaltraining.shared.behaviours;

import android.content.Context;
import android.util.Log;

import com.vssnake.intervaltraining.shared.interval.TrainingServiceInterface;
import com.vssnake.intervaltraining.shared.model.IntervalData;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;


/**
 * Created by unai on 04/07/2014.
 */
public class ImplIntervalBehaviour implements IntervalBehaviour {

    static final String TAG ="Interval Behaviour";
    int mTotalIntervals = 0;
    long mTimeToRest = 0;
    long mTimeToExercise = 0;

    int mCurrentInterval = 1;
    long mTotalIntervalsTime = 0; //Current Time in the Interval
    long mLastIntervalsTime = 0;
    long mCurrentIntervalTime = 0;

    long mCurrentIntervalSpace = 0;//The wide of the current part of interval in milliseconds

    IntervalData.eIntervalState intervalState = IntervalData.eIntervalState.RUNNING;

    IntervalData mIntervalData;

    TrainingServiceInterface mTrainingServiceInterface;

    boolean mFinish = false;

    int[] mSoundTimeArray;
    int [] mTemporalSoundArray;

    long mMillisecondsTotal;

    Context mContext;

    private ImplIntervalBehaviour(int IDTraining,TrainingServiceInterface iIntervalService,
                                  int[] soundsTimeArray,Context context){
        mIntervalData = new IntervalData();

        this.mContext = context;
        changeTraining(IDTraining,mContext);

        this.mTrainingServiceInterface = iIntervalService;





        this.mSoundTimeArray = soundsTimeArray;

        this.mTemporalSoundArray = soundsTimeArray.clone();


    }

    public static ImplIntervalBehaviour
                    newInstance (int IDTraining,
                                 TrainingServiceInterface iIntervalService,
                    int[] soundsTimeArray,Context context){
        return new ImplIntervalBehaviour(IDTraining,iIntervalService,soundsTimeArray,context);
    }

    /***
     * @Override
     * @param time In milliseconds
     */
    public void executeTime(long time) {


        if (mTrainingServiceInterface == null){
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
            Log.d(TAG, "Final Beep");

           mTrainingServiceInterface.specialCommand(TrainingServiceInterface.specialsCommands.SOUND, 2);
           mTrainingServiceInterface.specialCommand(TrainingServiceInterface.specialsCommands.
                           VIBRATION,Integer.valueOf(StacData.Intervaldata.finalStateVibration));


            if (mCurrentIntervalSpace == mTimeToRest){
                //The user is going to REST
                intervalState = IntervalData.eIntervalState.RUNNING;
                mCurrentIntervalSpace = mTimeToExercise;
                mCurrentInterval++;

            }else{

                if (mCurrentInterval >= mTotalIntervals){
                    mFinish = true;
                    mIntervalData.intervalDone();
                    mTrainingServiceInterface.endTrain();
                    return;
                }
                //The user is going to make exercise
                intervalState = IntervalData.eIntervalState.RESTING;
                mCurrentIntervalSpace = mTimeToRest;
            }
        }else{
            if (mTemporalSoundArray.length != 0) {

                int intervalTime = (int)(mCurrentIntervalSpace - (int)Math.round(mCurrentIntervalTime/1000f)*1000);
                Log.d(TAG,mCurrentIntervalSpace +"");
                Log.d(TAG,(int)Math.round(mCurrentIntervalTime/1000f)*1000 +"");
                Log.d(TAG,intervalTime +"");
                for (int i = 0; i < mTemporalSoundArray.length; i++) {
                    if (intervalTime <= mTemporalSoundArray[i]){
                        mTemporalSoundArray[i]=-1;
                        mTrainingServiceInterface.specialCommand(
                                TrainingServiceInterface.specialsCommands.SOUND, 1);
                        mTrainingServiceInterface.specialCommand(TrainingServiceInterface.
                                specialsCommands.VIBRATION,
                                Integer.valueOf(StacData.Intervaldata.upComingStateVibration));
                        Log.d(TAG, "Beep");
                    }
                }

            }

        }
        mIntervalData.setIntervalData(mCurrentInterval, mTotalIntervals,
                intervalState, mMillisecondsTotal - mTotalIntervalsTime, mCurrentIntervalSpace - mCurrentIntervalTime,0);
        //TODO make notifications and show data to the interface
        mTrainingServiceInterface.newNotification(mIntervalData);
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
        intervalState = IntervalData.eIntervalState.RUNNING;
        return true;
    }

    @Override
    public boolean changeTraining(long id,Context context) {
        IntervalStaticData.IntervalData intervalData = IntervalStaticData.initIntervalData(context)
                .get((int) id);

        if (intervalData!=null){
            this.mTotalIntervals = intervalData.getmTotalIntervals();
            this.mTimeToRest = intervalData.getmTimeResting()*1000;
            this.mTimeToExercise = intervalData.getmTimeDoing()*1000;
            mCurrentIntervalSpace = intervalData.getmTimeDoing()*1000;
            mMillisecondsTotal = (mTotalIntervals * mTimeToExercise) + ((mTotalIntervals-1) * mTimeToRest);

            mIntervalData.setName(intervalData.getmName());
        }

        return false;
    }
}
