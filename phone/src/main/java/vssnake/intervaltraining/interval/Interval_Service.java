package vssnake.intervaltraining.interval;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import vssnake.intervaltraining.customNotifications.IntervalNotification;
import vssnake.intervaltraining.R;
import vssnake.intervaltraining.behaviours.ImplIntervalBehaviour;
import vssnake.intervaltraining.main.MainBase_Activity;
import vssnake.intervaltraining.main.Main_Activity;
import vssnake.intervaltraining.utils.Utils;

/**
 * Created by unai on 27/06/2014.
 */
public class Interval_Service extends TrainingBase_Service {

    private static final String TAG = "IntervalService";
    //Timer mTimerHandler
    Handler mTimerHandler = new Handler();
    //The runnable of TabataTraining
    Runnable runnable;

    IntervalBehaviour intervalBehaviour;
    int SOUND1=1;
    int SOUND2=2;

    //The Interface to TabataTraining_Fragment
    TrainingServicesConnectors.IntervalInterface mIntervalInterface;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TabataServiceBinder extends Binder {
        public Interval_Service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Interval_Service.this;
        }

        public void setListener(TrainingServicesConnectors.IntervalInterface listener) {
            mIntervalInterface = listener;
            mTrainingInterface = mIntervalInterface;


        }

        public void runTabata(){startTabata();}

        public void runBackground(boolean background){
            setBackground(background);}
    }

    TabataServiceBinder mBinder = new TabataServiceBinder();



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return true;
    }



    @Override
    public void onCreate(){
        super.onCreate();

        //Start the sounds for interval
        mSoundsMap.put(SOUND1, mSoundPool.load(this, R.raw.sfx, 1));
        mSoundsMap.put(SOUND2, mSoundPool.load(this, R.raw.sfx2, 1));
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

    @Override
    public void onDestroy(){
        Log.i("Tabata_Service", "Destroy service");
        mTrainingStart = false;
        mTimerHandler.removeCallbacks(runnable);
        mIntervalInterface = null;
        mTrainingInterface = null;
        super.onDestroy();

    }

    void startTabata(){


        if (!mTrainingStart){
           startTrain();
        }else{
            endTrain();
        }

    }

    @Override
    public void newNotification(IntervalData_Base intervalData) {
        long secondsTotal = (long) Math.ceil( intervalData.getTotalIntervalTime()/1000d);

        long secondsInterval = (long) Math.ceil( intervalData.getIntervalTime()/1000d);

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

            mNotification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                            getResources().getString(R.string.tabata),
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
        mTimerHandler.removeCallbacks(runnable);
        IntervalData_Base intervalData = new IntervalData_Base();
        intervalData.setIntervalData(0,0, IntervalData_Base.eIntervalState.NOTHING,0,0,0);
        mTrainingStart = false;
        newNotification(intervalData);
        setBackground(false);

        if (mTrainingInterface != null){
            mTrainingInterface.specialEvent(TrainingServicesConnectors.specialCommands.END_TRAINING);
        }
    }

    long timeInMilliseconds = 0L;
    long startTime = 0L;

    void startTrain(){


        if (intervalBehaviour == null)
            intervalBehaviour = ImplIntervalBehaviour.newInstance(8, 10000, 20000, this, new int[]{3000,
                    2000, 1000});

        intervalBehaviour.resetInterval();
        mNotification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                getResources().getString(R.string.tabata),0,0,"nothing","00:00","00:00",null,null);

        runnable = new Runnable() {
            @Override
            public void run() {
                timeInMilliseconds = SystemClock.uptimeMillis() -  startTime;

                mTimerHandler.postDelayed(this, 500);
                intervalBehaviour.executeTime(timeInMilliseconds);

            }
        };
        startTime = SystemClock.uptimeMillis();

        mTrainingStart = true;
        if (mTrainingInterface != null){
            mTrainingInterface.statusTrain(mTrainingStart);
        }
        intervalBehaviour.executeTime(0);
        mTimerHandler.postDelayed(runnable, 500);
    }



    @Override
    public void specialCommand(specialsCommands commands, Object adicionalData) {
            switch (commands){

                case SOUND:
                    playSound((Integer)adicionalData,1.0f);
                    break;
                case VIBRATION:
                    break;
                case RUN:
                    mIntervalInterface.specialEvent(TrainingServicesConnectors.specialCommands.RUN);
                    break;
                case REST:
                    mIntervalInterface.specialEvent(TrainingServicesConnectors.specialCommands.REST);
                    break;
            }
    }
}
