package vssnake.intervaltraining.training;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;

import java.util.HashMap;

import vssnake.intervaltraining.IntervalNotification;
import vssnake.intervaltraining.R;
import vssnake.intervaltraining.main.MainBase_Activity;
import vssnake.intervaltraining.main.Main_Activity;
import vssnake.intervaltraining.model.Exercise;
import vssnake.intervaltraining.utils.Utils;

/**
 * Created by unai on 27/06/2014.
 */
public class Interval_Service extends TrainingBase_Service implements IntervalServiceConnector{


    static int sNOTIFID = 1325;

    Handler handler = new Handler();

    NotificationManager mNotificationManager;
    Notification notification;
    String notificationTitle;
    String nSeconds;

    //The runnable of TabataTraining
    Runnable runnable;

    //The background of the application
    private boolean background = false;

    //Is the Interval Running
    boolean isIntervalStart = false; //

    IntervalBehaviour intervalBehaviour;

    Notification mNotification;


    private SoundPool soundPool;
    private SparseArray<Integer> soundsMap;
    int SOUND1=1;
    int SOUND2=2;


    long timeInMilliseconds = 0L;
    long startTime = 0L;

    public interface IntervalServiceListener {

        public void changeIntervalMode(String mode);
        public void changeTime(long secondsTotal,long secondInterval);
        public void changeInterval(int numberInterval,int totalInterval);
        public void statusInterval(boolean status);

    }

    IntervalServiceListener mListener;
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TabataServiceBinder extends Binder {
        public Interval_Service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Interval_Service.this;
        }

        public void setListener(IntervalServiceListener listener) {
            mListener = listener;
        }

        public void runTabata(){startTabata();}

        public void runBackground(boolean background){setBackground(background);}
    }

    TabataServiceBinder mBinder = new TabataServiceBinder();



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean onUnbind(Intent intent){
        return false;
    }



    @Override
    public void onCreate(){
        super.onCreate();
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        nSeconds = getResources().getString(R.string.seconds);



        //Start the sounds for interval
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new SparseArray<Integer>();
        soundsMap.put(SOUND1, soundPool.load(this, R.raw.sfx, 1));
        soundsMap.put(SOUND2, soundPool.load(this, R.raw.sfx2, 1));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("TEST"));

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
        isIntervalStart = false;
        handler.removeCallbacks(runnable);
        super.onDestroy();

    }





    void startTabata(){


        if (!isIntervalStart){
           startTrain();
        }else{
            endTrain();
        }

    }

    public void playSound(int sound, float fSpeed) {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;


        soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, fSpeed);
    }


    void setBackground(boolean background){

        if (isIntervalStart){
            if (background){
                startForeground(1024, notification);
            }else{
                stopForeground(true);
            }

        }
        if (!background){
            stopForeground(true);
        }
        if (mListener != null){
            mListener.statusInterval(isIntervalStart);
        }
        this.background = background;


    }

    @Override
    public void newNotification(IntervalData_Base intervalData) {
        if (mListener != null){
            mListener.changeInterval(intervalData.getNumberInterval(),
                    intervalData.getTotalIntervals());

            mListener.changeIntervalMode(intervalData.getIntervalState().name());

            mListener.changeTime(intervalData.getTotalIntervalTime(),
                    intervalData.getIntervalTime());



        }
        if (background){

            PendingIntent showFragmentIntent ;
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Main_Activity.class);

            intent.putExtra(MainBase_Activity.FRAGMENT_KEY,MainBase_Activity.TABATA_FRAGMENT);
            showFragmentIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent cancelIntervalIntent ;
            Intent intervalIntent = new Intent(this,Interval_Service.class);
            intervalIntent.setAction("STOP");
            cancelIntervalIntent = PendingIntent.getService(getBaseContext(),0,intervalIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            notification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                            getResources().getString(R.string.tabata),
                    intervalData.getNumberInterval(),
                    intervalData.getTotalIntervals(),
                    intervalData.getIntervalState().name(),
                    Utils.formatIntervalTime(intervalData.getIntervalTime()/1000),
                    Utils.formatTotalIntervalTime(intervalData.getTotalIntervalTime()/1000),
                    showFragmentIntent,cancelIntervalIntent);

            mNotificationManager.notify(1024,notification);
        }
    }

    @Override
    public void endTrain() {
        handler.removeCallbacks(runnable);
        IntervalData_Base intervalData = new IntervalData_Base();
        intervalData.setIntervalData(0,0, IntervalData_Base.eIntervalState.NOTHING,0,0,0);
        isIntervalStart = false;
        newNotification(intervalData);
        setBackground(false);

        if (mListener != null){
            mListener.statusInterval(isIntervalStart);
        }
    }

    void startTrain(){


        if (intervalBehaviour == null)
            intervalBehaviour = ImplIntervalBehaviour.newInstance(2,10000,20000,this, new int[]{3000,
            2000,1000});

        intervalBehaviour.resetInterval();

        notificationTitle = getResources().getString(R.string.tabata);
        notification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                getResources().getString(R.string.tabata),0,0,"nothing","00:00","00:00",null,null);

        runnable = new Runnable() {
            @Override
            public void run() {


                timeInMilliseconds = SystemClock.uptimeMillis() -  startTime;



                handler.postDelayed(this,500);
                intervalBehaviour.executeTime(timeInMilliseconds);



            }
        };
        // Log.i("Tabata_Service", "Start Tabata");

        startTime = SystemClock.uptimeMillis();

        isIntervalStart = true;
        if (mListener != null){
            mListener.statusInterval(isIntervalStart);
        }
        handler.postDelayed(runnable,500);
    }



    @Override
    public void specialCommand(specialsCommands commands, Object adicionalData) {
            switch (commands){

                case sound:
                    playSound((Integer)adicionalData,1.0f);
                    break;
                case vibration:
                    break;
            }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}
