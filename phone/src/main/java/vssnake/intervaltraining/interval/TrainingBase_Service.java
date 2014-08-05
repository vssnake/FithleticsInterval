package vssnake.intervaltraining.interval;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.SparseArray;

public abstract class TrainingBase_Service extends Service implements TrainingServiceInterface{


    public enum stateFlag {
        running,
        stop,
        paused
    }
    private NotificationManager nNM;

    private static stateFlag serviceState = stateFlag.stop;

    public static stateFlag getStateService(){
        return serviceState;
    }

    Notification mNotification;

    //Is the Training Running
    boolean mTrainingStart = false;

    //The mBackground of the application
    boolean mBackground = false;

    static final int NOTIFICATION_ID = 1024;

    NotificationManager mNotificationManager;

    SoundPool mSoundPool;
    SparseArray<Integer> mSoundsMap;

    TrainingServiceConnectors.TrainingInterface mTrainingInterface;

    public TrainingBase_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        //Set the flag to running
        serviceState = stateFlag.running;
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mSoundsMap = new SparseArray<Integer>();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy(){
        //Set the flag to Stop
        serviceState = stateFlag.stop;


    }


    /**
     * Play sound of the soundMap
     * @param sound the key of the sound in the sound Map
     * @param fSpeed the speed of the sound
     */
    public void playSound(int sound, float fSpeed) {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;


        mSoundPool.play(mSoundsMap.get(sound), volume, volume, 1, 0, fSpeed);
    }


    void setBackground(boolean background){

        if (mTrainingStart){
            if (background){
                startForeground(NOTIFICATION_ID, mNotification);
            }
        }
        if (!background){
            stopForeground(true);
        }
        if (mTrainingInterface != null){
            mTrainingInterface.statusTrain(mTrainingStart);
        }
        this.mBackground = background;


    }
}
