package com.vssnake.intervaltraining.wear.interval;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.vssnake.intervaltraining.shared.behaviours.ImplIntervalBehaviour;
import com.vssnake.intervaltraining.shared.interval.Interval_Service;
import com.vssnake.intervaltraining.shared.interval.TrainingServiceConnectors;
import com.vssnake.intervaltraining.shared.interval.TrainingServiceInterface;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.wearable.WearableService;

import java.util.concurrent.Executors;

/**
 * Created by unai on 05/09/2014.
 */
public class IntervalWearService extends Interval_Service{

    protected TabataServiceBinder mBinder;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TabataServiceBinder extends Binder {


        public  IntervalWearService getService() {
            // Return this instance of LocalService so clients can call public methods
            return IntervalWearService.this ;
        }

        public void setListener(TrainingServiceConnectors.IntervalInterface listener) {

            mIntervalInterface = listener;
            mTrainingInterface = mIntervalInterface;




        }

        public void runTabata(){
            startTabata();
        }

        public void runInbackground(boolean background) {
            setBackground(background);
        }

        public void changeTrain(long IDTraining){
            mSharedPreference.edit().putInt(StacData.PREFS_TRAIN_KEY,(int)IDTraining).commit();
            endTrain();
            mIntervalBehaviour.changeTraining(IDTraining);
        }

    }

    @Override
    public void onCreate(){
        super.onCreate();

        mBinder = new TabataServiceBinder();
    }

    @Override
    protected void startTabata(){
            setBackground(false);
         super.startTabata();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void specialCommand(TrainingServiceInterface.specialsCommands commands, Object adicionalData) {
        switch (commands){
            case VIBRATION:
                vibration((Integer)adicionalData);

                break;
        }
    }
}
