package com.vssnake.intervaltraining.shared.interval;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;

import com.vssnake.intervaltraining.shared.interval.TrainingServiceConnectors;
import com.vssnake.intervaltraining.shared.interval.TrainingServiceInterface;
import com.vssnake.intervaltraining.shared.utils.StacData;


public abstract class TrainingBase_Service extends Service implements TrainingServiceInterface {

    private static final  String TAG = "TrainingBase Service";

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

    protected Notification mNotification;

    //Is the Training Running
    boolean mTrainingStart = false;

    //The mBackground of the application
    protected boolean mBackground = true;

    protected static final int NOTIFICATION_ID = 1024;

    protected NotificationManager mNotificationManager;

    protected SoundPool mSoundPool;
    protected SparseArray<Integer> mSoundsMap;

    protected TrainingServiceConnectors.TrainingInterface mTrainingInterface;

    protected boolean mVibrationEnabled = true;
    protected boolean mSoundEnabled = true;

    protected SharedPreferences mSharedPreference;

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

        mSharedPreference =  getSharedPreferences(StacData.BASIC_CONFIG_PREFS, 0);

        mVibrationEnabled = mSharedPreference.
                getBoolean(StacData.PREFS_VIBRATION_KEY,true);

        mSoundEnabled = mSharedPreference.
                getBoolean(StacData.PREFS_SOUND_KEY,true);
        mSharedPreference.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key == StacData.PREFS_VIBRATION_KEY){
                mVibrationEnabled = sharedPreferences.
                        getBoolean(StacData.PREFS_VIBRATION_KEY, true);

            }else if (key == StacData.PREFS_SOUND_KEY){
                mSoundEnabled = sharedPreferences.
                        getBoolean(StacData.PREFS_SOUND_KEY, true);
            }
            Log.d(TAG,key + " Changed");
        }
    };


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy(){
        //Set the flag to Stop
        serviceState = stateFlag.stop;
        mSharedPreference.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

    }


    /**
     * Play sound of the soundMap
     * @param sound the key of the sound in the sound Map
     * @param fSpeed the speed of the sound
     */
    public void playSound(int sound, float fSpeed) {
        if (mSoundEnabled){
            AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = streamVolumeCurrent / streamVolumeMax;
            mSoundPool.play(mSoundsMap.get(sound), volume, volume, 1, 0, fSpeed);
        }




    }

    public void vibration(long milliseconds){
        if (mVibrationEnabled){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(milliseconds);
        }

    }


    protected void setBackground(boolean background){

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

    @Override
    public boolean isTrainStarted(){
        return mTrainingStart;
    }
}
