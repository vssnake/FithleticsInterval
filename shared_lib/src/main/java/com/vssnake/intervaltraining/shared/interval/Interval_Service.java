package com.vssnake.intervaltraining.shared.interval;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;


import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.model.IntervalData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vssnake.intervaltraining.shared.behaviours.IntervalBehaviour;

import com.vssnake.intervaltraining.shared.behaviours.ImplIntervalBehaviour;

import com.vssnake.intervaltraining.shared.wearable.WearableService;



/**
 * Created by unai on 27/06/2014.
 */
public abstract class Interval_Service extends TrainingBase_Service {

    private static final String TAG = "IntervalService";
    //Timer mTimerHandler
    Handler mTimerHandler = new Handler();



    //The runnable of TabataTraining
    Runnable runnable;

    protected IntervalBehaviour mIntervalBehaviour;


    ScheduledExecutorService scheduler;
    ScheduledFuture beeperHandle;
    protected PowerManager.WakeLock mWakeLock;

    protected IntervalData mIntervalData;







    //The Interface to TabataTraining_Fragment
    protected TrainingServiceConnectors.IntervalInterface mIntervalInterface;



    int IDTrain;

    public boolean onUnbind(Intent intent){
        return true;
    }



    @Override
    protected void setBackground(boolean background){

        super.setBackground(background);
        if (!background && mIntervalData!= null){
            sendDataToActivity(); //If the app go to foreground send the last info of interval
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");


        scheduler = Executors.newSingleThreadScheduledExecutor();

        IDTrain = mSharedPreference.getInt(StacData.PREFS_TRAIN_KEY,0);
        if (mIntervalBehaviour == null)
            mIntervalBehaviour = ImplIntervalBehaviour.newInstance(IDTrain, this,
                    new int[]{3000,
                    2000, 1000});



    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Destroy service");
        mTrainingStart = false;
        mTimerHandler.removeCallbacks(runnable);
        mIntervalInterface = null;
        mTrainingInterface = null;

        try{
            beeperHandle.cancel(true); //Cancel the timer
        }catch (Exception e){

        }


        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        //Get the binder for activity class


        return START_NOT_STICKY;


    }



    protected void startTabata(){


        if (!mTrainingStart){
           startTrain();
        }else{
            endTrain();

        }

    }
    static int count = 0; //Test
    @Override
    public void newNotification(IntervalData intervalData) {

        mIntervalData = intervalData;



        if (!mBackground){
            sendDataToActivity();
        }
    }


   protected void getDefaultIntervalData(){
        if (mIntervalData== null){
            mIntervalData = new IntervalData();
        }
        int keyData = mSharedPreference.getInt(StacData.PREFS_TRAIN_KEY,0);
        IntervalStaticData.IntervalData intervalData = IntervalStaticData.intervalData.get(keyData);
        mIntervalData.setIntervalData(1,intervalData.getmTotalIntervals(),
                IntervalData.eIntervalState.NOTHING,0,0,0);

        mIntervalData.setName(intervalData.getmName());
    }

    @Override
    public void endTrain() {


        if (mWakeLock.isHeld()){
            mWakeLock.release();
        }


       // mTimerHandler.removeCallbacks(runnable);
        if (beeperHandle != null){
            beeperHandle.cancel(true);
        }



        getDefaultIntervalData();




        mTrainingStart = false;
        newNotification(mIntervalData);

        stopForeground(true);

        if (mTrainingInterface != null){
            mTrainingInterface.specialEvent(TrainingServiceConnectors.specialUICommands.END_TRAINING);
        }
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    long timeInMilliseconds = 0L;
    long startTime = 0L;

    protected void startTrain(){

        mWakeLock.acquire();



        if (mIntervalBehaviour == null)
            mIntervalBehaviour = ImplIntervalBehaviour.newInstance(IDTrain, this, new int[]{3000,
                    2000, 1000});

        mIntervalBehaviour.resetInterval();

        runnable = new Runnable() {
            @Override
            public void run() {
                //mTimerHandler.postDelayed(this, 1000);
                timeInMilliseconds = SystemClock.uptimeMillis() -  startTime;


                Log.d(TAG,timeInMilliseconds + "");
               mIntervalBehaviour.executeTime(timeInMilliseconds);

            }
        };
        startTime = SystemClock.uptimeMillis();

        mTrainingStart = true;
        setBackground(mBackground);

        if (mTrainingInterface != null){
            mTrainingInterface.statusTrain(mTrainingStart);
        }
        mIntervalBehaviour.executeTime(0);

        beeperHandle = scheduler.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
       //mTimerHandler.postDelayed(runnable, 1000);
    }


    private void sendDataToActivity(){
        if (mIntervalInterface != null){
            mIntervalInterface.changeInterval(mIntervalData.getNumberInterval(),
                    mIntervalData.getTotalIntervals());

            mIntervalInterface.changeIntervalMode(mIntervalData.getIntervalState().name());

            mIntervalInterface.changeTime(
                    mIntervalData.getTotalIntervalTimeSeconds(),
                    mIntervalData.getIntervalTimeSeconds());
        }else{
            Log.e("IntervalService","Listener is null");
        }
    }


    //TODO move to phone





    @Override
    public void specialCommand(TrainingServiceInterface.specialsCommands commands, Object adicionalData) {
            switch (commands){

                case SOUND:
                    //playSound((Integer)adicionalData,1.0f);
                    break;
                case VIBRATION:
                    vibration((Integer)adicionalData);
                    WearableService.startNotification(WearableService.TypeNotifications.
                            VIBRATION_NOTIFICATION, (Integer) adicionalData);
                    break;
                case RUN:
                    mIntervalInterface.specialEvent(TrainingServiceConnectors.specialUICommands.RUN);
                    break;
                case REST:
                    mIntervalInterface.specialEvent(TrainingServiceConnectors.specialUICommands.REST);
                    break;
            }
    }
}
