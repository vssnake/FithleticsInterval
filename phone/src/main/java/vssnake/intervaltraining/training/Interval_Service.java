package vssnake.intervaltraining.training;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashMap;

import vssnake.intervaltraining.IntervalNotification;
import vssnake.intervaltraining.R;
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

    boolean background = false;

    IntervalBehaviour intervalBehaviour;

    Notification mNotification;


    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundsMap;
    int SOUND1=1;
    int SOUND2=2;

    public interface IntervalServiceListener {

        public void changeIntervalMode(String mode);
        public void changeTime(long secondsTotal,long secondInterval);
        public void changeInterval(int numberInterval,int totalInterval);

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



        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(SOUND1, soundPool.load(this, R.raw.sfx, 1));
        soundsMap.put(SOUND2, soundPool.load(this, R.raw.sfx2, 1));


    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        //Get the binder for activity class


        return START_STICKY;


    }

    @Override
    public void onDestroy(){
        Log.i("Tabata_Service", "Destroy service");
        handler.removeCallbacks(runnable);
        super.onDestroy();

    }



    long timeInMilliseconds = 0L;
    long startTime = 0L;

    void startTabata(){
        intervalBehaviour = ImplIntervalBehaviour.newInstance(8,10000,20000,this);

        notificationTitle = getResources().getString(R.string.tabata);
        notification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                        getResources().getString(R.string.tabata),0,0,"nothing","00:00","00:00");

        runnable = new Runnable() {
            @Override
            public void run() {


                    timeInMilliseconds = SystemClock.uptimeMillis() -  startTime;



                    handler.postDelayed(this,500);
                    intervalBehaviour.executeTime(timeInMilliseconds);



            }
        };
       // Log.i("Tabata_Service", "Start Tabata");
        if (startTime == 0){
            startTime = SystemClock.uptimeMillis();
        }

        handler.postDelayed(runnable,500);
    }

    public void playSound(int sound, float fSpeed) {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;


        soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, fSpeed);
    }


    void setBackground(boolean background){
        if (background){
            startForeground(1024, notification);
        }else{
            stopForeground(true);
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

            if (background){
                notification = IntervalNotification.createNotification(getApplicationContext(),getApplicationContext().
                                getResources().getString(R.string.tabata),
                        intervalData.getNumberInterval(),
                        intervalData.getTotalIntervals(),
                        intervalData.getIntervalState().name(),
                        Utils.formatIntervalTime(intervalData.getIntervalTime()/1000),
                        Utils.formatTotalIntervalTime(intervalData.getTotalIntervalTime()/1000));

                mNotificationManager.notify(1024,notification);
            }



           /*mListener.showTabataData(intervalData.getTotalIntervalTime(),
                    intervalData.getIntervalTime(),intervalData.getNumberInterval(),
                    intervalData.getTotalIntervals(),"hello");*/
            /*Log.i("ServiceNotification",intervalData.getIntervalTime() /1000 + " IntervalTime");
            Log.i("ServiceNotification",intervalData.getTotalIntervalTime() /1000 + " TotalIntervalTime");
            Log.i("ServiceNotification",intervalData.getNumberInterval() /1000 + " NumberInterval");
            Log.i("ServiceNotification",intervalData.getTotalIntervals() /1000 + " TotalIntervals");*/


        }
    }

    @Override
    public void endTrain() {
        handler.removeCallbacks(runnable);
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
}
