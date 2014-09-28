package vssnake.intervaltraining.interval;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.wearable.DataMap;
import com.vssnake.intervaltraining.shared.interval.Interval_Service;
import com.vssnake.intervaltraining.shared.interval.TrainingServiceConnectors;
import com.vssnake.intervaltraining.shared.model.IntervalData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.utils.Utils;
import com.vssnake.intervaltraining.shared.wearable.WearableListenerInterface;
import vssnake.intervaltraining.wearable.WearableListenerService;
import com.vssnake.intervaltraining.shared.wearable.WearableService;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.customNotifications.IntervalNotification;
import vssnake.intervaltraining.main.MainBase_Activity;
import vssnake.intervaltraining.main.Main_Activity;

/**
 * Created by unai on 05/09/2014.
 */
public class IntervalPhoneService extends Interval_Service implements
        WearableListenerInterface {

    int SOUND1=1;
    int SOUND2=2;

    Intent mMainActivityIntent;
    Intent mIntervalServiceIntent;
    PendingIntent mCancelIntervalIntent;
    PendingIntent mShowTabataFragmentIntent;


    DataMap mIntervalDataMap = new DataMap();

    int mWearableListenerConnectorID = -1;

    protected TabataServiceBinder mBinder;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class TabataServiceBinder extends Binder {


        public  IntervalPhoneService getService() {
            // Return this instance of LocalService so clients can call public methods
            return IntervalPhoneService.this ;
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
    public void onCreate() {
        super.onCreate();

        mWearableListenerConnectorID =  WearableListenerService.addListener(this);

        mIntervalServiceIntent = new Intent(this,IntervalPhoneService.class);
        mIntervalServiceIntent.setAction("STOP"); //Set Action to stop the Interval

        mMainActivityIntent = new Intent(getApplicationContext(),Main_Activity.class);

        //Callback to show the intervalFragment
        mMainActivityIntent.putExtra(MainBase_Activity.FRAGMENT_KEY,
                MainBase_Activity.TABATA_FRAGMENT);

        PendingIntent.getActivity(getApplicationContext(),0,
                mMainActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mShowTabataFragmentIntent= PendingIntent.getActivity(getApplicationContext(),0,
                mMainActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mCancelIntervalIntent = PendingIntent.getService(getBaseContext(),0,
                mIntervalServiceIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Start the sounds for interval
         mSoundsMap.put(SOUND1, mSoundPool.load(this, R.raw.sfx, 1));
         mSoundsMap.put(SOUND2, mSoundPool.load(this, R.raw.sfx2, 1));

        mBinder = new  TabataServiceBinder();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
        //Destroy mWearableListener
        WearableListenerService.removeListener(mWearableListenerConnectorID);

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        //Get the binder for activity class
        if (intent != null){
            String action = intent.getAction();
            if (intent.getAction() == "STOP"){
                endTrain();

            }
        }
        return super.onStartCommand(intent,flags,startId);
    }



    @Override
    public void newNotification(IntervalData intervalData) {
        super.newNotification(intervalData);

        sendDataToWearables();
        if (mBackground){


            mNotification = IntervalNotification.createNotification(getApplicationContext(),
                    intervalData.getName(),
                    intervalData.getNumberInterval(),
                    intervalData.getTotalIntervals(),
                    intervalData.getIntervalState().name(),
                    Utils.formatTime(intervalData.getIntervalTimeSeconds()),
                    Utils.formatTime(intervalData.getTotalIntervalTimeSeconds()),
                    mShowTabataFragmentIntent, mCancelIntervalIntent);

            mNotificationManager.notify(NOTIFICATION_ID,mNotification);

        }
    }

    @Override
    protected void startTrain() {
        super.startTrain();

         WearableService.setDataMap(WearableService.SEND_INTERVAL_DATA, mIntervalDataMap);

        mNotification = IntervalNotification.createNotification(getApplicationContext(),
                getApplicationContext().
                        getResources().getString(R.string.tabata),0,0,"nothing","00:00","00:00",null,
                null);
    }

    @Override
    public void onMessageReceived(typeMessage message, Bundle data) {
        switch (message) {
            case action:
                startTabata();

            case request_data:

                getDefaultIntervalData();
                mIntervalDataMap.clear();
                WearableService.setDataMap(WearableService.SEND_INTERVAL_DATA, mIntervalDataMap);
                sendDataToWearables();
                break;
        }
    }



    private void sendDataToWearables(){

       mIntervalDataMap.putString(IntervalData.intervalDataKey.INTERVAL_NAME.name(),
                mIntervalData.getName());
        mIntervalDataMap.putInt(IntervalData.intervalDataKey.NUMBER_INTERVAL.name(),
                mIntervalData.getNumberInterval());
        mIntervalDataMap.putInt(IntervalData.intervalDataKey.TOTAL_INTERVALS.name(),
                mIntervalData.getTotalIntervals());
        mIntervalDataMap.putString(IntervalData.intervalDataKey.INTERVAL_STATE.name(),
                mIntervalData.getIntervalState().name());
        mIntervalDataMap.putLong(IntervalData.intervalDataKey.INTERVAL_TIME.name(),
                mIntervalData.getIntervalTimeMilliseconds());
        mIntervalDataMap.putLong(IntervalData.intervalDataKey.TOTAL_INTERVAL_TIME.name(),
                mIntervalData.getTotalIntervalTimeMilliseconds());

        WearableService.setDataMap(WearableService.SEND_INTERVAL_DATA, mIntervalDataMap);
    }


}
