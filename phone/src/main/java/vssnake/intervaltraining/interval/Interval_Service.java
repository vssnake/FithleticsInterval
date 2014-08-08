package vssnake.intervaltraining.interval;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import vssnake.intervaltraining.behaviours.IntervalBehaviour;
import vssnake.intervaltraining.customNotifications.IntervalNotification;
import vssnake.intervaltraining.R;
import vssnake.intervaltraining.behaviours.ImplIntervalBehaviour;
import vssnake.intervaltraining.main.MainBase_Activity;
import vssnake.intervaltraining.main.Main_Activity;
import vssnake.intervaltraining.services.GoogleApiService;
import vssnake.intervaltraining.services.WearableListenerConnector;
import vssnake.intervaltraining.services.WearableListenerService;
import vssnake.intervaltraining.utils.Utils;

/**
 * Created by unai on 27/06/2014.
 */
public class Interval_Service extends TrainingBase_Service implements WearableListenerConnector{

    private static final String TAG = "IntervalService";
    //Timer mTimerHandler
    Handler mTimerHandler = new Handler();



    //The runnable of TabataTraining
    Runnable runnable;

    IntervalBehaviour intervalBehaviour;
    int SOUND1=1;
    int SOUND2=2;

    ScheduledExecutorService scheduler;
    ScheduledFuture beeperHandle;
    protected PowerManager.WakeLock mWakeLock;

    //The Interface to TabataTraining_Fragment
    TrainingServiceConnectors.IntervalInterface mIntervalInterface;

    @Override
    public void onMessageReceived(typeMessage message, Bundle data) {
        switch (message) {
            case action:
                startTabata();
                break;
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TabataServiceBinder extends Binder {
        public Interval_Service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Interval_Service.this;
        }

        public void setListener(TrainingServiceConnectors.IntervalInterface listener) {
            mIntervalInterface = listener;
            mTrainingInterface = mIntervalInterface;


        }

        public void runTabata(){startTabata();}

        public void runInbackground(boolean background){
            setBackground(background);}
    }

    TabataServiceBinder mBinder = new TabataServiceBinder();

    DataMap mIntervalDataMap = new DataMap();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return true;
    }

    int mWearableListenerConnectorID = -1;

    @Override
    public void onCreate(){
        super.onCreate();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        //Start the sounds for interval
        mSoundsMap.put(SOUND1, mSoundPool.load(this, R.raw.sfx, 1));
        mSoundsMap.put(SOUND2, mSoundPool.load(this, R.raw.sfx2, 1));

        scheduler = Executors.newSingleThreadScheduledExecutor();

        mWearableListenerConnectorID =  WearableListenerService.addListener(this);

      //  GoogleApiService.setDataMap(GoogleApiService.SEND_INTERVAL_DATA, mIntervalDataMap);
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
        //Destroy mWearableListener
        WearableListenerService.removeListener(mWearableListenerConnectorID);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        //Get the binder for activity class

        if (intent != null){
            String action = intent.getAction();
            if (intent.getAction() == "STOP"){
                endTrain();

            }
        }
        return START_NOT_STICKY;


    }



    void startTabata(){


        if (!mTrainingStart){

           startTrain();
        }else{
            endTrain();

        }

    }
    static int count = 0; //Test
    @Override
    public void newNotification(IntervalData intervalData) {

        long secondsTotal = (long) Math.ceil( intervalData.getTotalIntervalTime()/1000d);

        long secondsInterval = (long) Math.ceil( intervalData.getIntervalTime()/1000d);


        mIntervalDataMap.putInt(IntervalData.intervalDataKey.NUMBER_INTERVAL.name(),
                intervalData.getNumberInterval());
        mIntervalDataMap.putInt(IntervalData.intervalDataKey.TOTAL_INTERVALS.name(),
                intervalData.getTotalIntervals());
        mIntervalDataMap.putString(IntervalData.intervalDataKey.INTERVAL_STATE.name(),
                intervalData.getIntervalState().name());
        mIntervalDataMap.putLong(IntervalData.intervalDataKey.INTERVAL_TIME.name(),
                secondsInterval);
        mIntervalDataMap.putLong(IntervalData.intervalDataKey.TOTAL_INTERVAL_TIME.name(),
                secondsTotal);

        GoogleApiService.setDataMap(GoogleApiService.SEND_INTERVAL_DATA, mIntervalDataMap);




        if (mBackground){

            PendingIntent showFragmentIntent ;
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Main_Activity.class);

            //Callback to show the intervalFragment
            intent.putExtra(MainBase_Activity.FRAGMENT_KEY,MainBase_Activity.TABATA_FRAGMENT);
            showFragmentIntent = PendingIntent.getActivity(getApplicationContext(),0,
                    intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //Callback to cancel the current Training
            PendingIntent cancelIntervalIntent ;
            Intent intervalIntent = new Intent(this,Interval_Service.class);
            intervalIntent.setAction("STOP");
            cancelIntervalIntent = PendingIntent.getService(getBaseContext(),0,
                    intervalIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            mNotification = IntervalNotification.createNotification(getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.tabata),
                    intervalData.getNumberInterval(),
                    intervalData.getTotalIntervals(),
                    intervalData.getIntervalState().name(),
                    Utils.formatIntervalTime(secondsInterval),
                    Utils.formatTotalIntervalTime(secondsTotal),
                    showFragmentIntent,cancelIntervalIntent);

            mNotificationManager.notify(NOTIFICATION_ID,mNotification);


        }else{
            if (mIntervalInterface != null){
                mIntervalInterface.changeInterval(intervalData.getNumberInterval(),
                        intervalData.getTotalIntervals());

                mIntervalInterface.changeIntervalMode(intervalData.getIntervalState().name());

                mIntervalInterface.changeTime(secondsTotal,secondsInterval);



            }else{
                Log.e("IntervalService","Listener is null");
            }
        }
    }

    @Override
    public void endTrain() {

        mWakeLock.release();

       // mTimerHandler.removeCallbacks(runnable);
        beeperHandle.cancel(true);
        IntervalData intervalData = new IntervalData();
        intervalData.setIntervalData(0,0, IntervalData.eIntervalState.NOTHING,0,0,0);
        mTrainingStart = false;
        newNotification(intervalData);
        setBackground(false);

        if (mTrainingInterface != null){
            mTrainingInterface.specialEvent(TrainingServiceConnectors.specialUICommands.END_TRAINING);
        }
    }

    long timeInMilliseconds = 0L;
    long startTime = 0L;

    void startTrain(){



        mWakeLock.acquire();

        GoogleApiService.setDataMap(GoogleApiService.SEND_INTERVAL_DATA, mIntervalDataMap);

        if (intervalBehaviour == null)
            intervalBehaviour = ImplIntervalBehaviour.newInstance(8, 10000, 20000, this, new int[]{3000,
                    2000, 1000});

        intervalBehaviour.resetInterval();
        mNotification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                getResources().getString(R.string.tabata),0,0,"nothing","00:00","00:00",null,null);




        runnable = new Runnable() {
            @Override
            public void run() {
                //mTimerHandler.postDelayed(this, 1000);
                timeInMilliseconds = SystemClock.uptimeMillis() -  startTime;


                Log.d(TAG,timeInMilliseconds + "");
               intervalBehaviour.executeTime(timeInMilliseconds);

            }
        };
        startTime = SystemClock.uptimeMillis();

        mTrainingStart = true;
        if (mTrainingInterface != null){
            mTrainingInterface.statusTrain(mTrainingStart);
        }
        intervalBehaviour.executeTime(0);

        beeperHandle = scheduler.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS);
       //mTimerHandler.postDelayed(runnable, 1000);
    }



    @Override
    public void specialCommand(specialsCommands commands, Object adicionalData) {
            switch (commands){

                case SOUND:
                    playSound((Integer)adicionalData,1.0f);
                    break;
                case VIBRATION:
                    GoogleApiService.startNotification(GoogleApiService.TypeNotifications.
                            VIBRATION_NOTIFICATION,(Integer)adicionalData);
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
