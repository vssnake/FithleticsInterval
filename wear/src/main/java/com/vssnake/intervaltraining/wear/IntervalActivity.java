package com.vssnake.intervaltraining.wear;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.vssnake.intervaltraining.shared.interval.Interval_Service;
import com.vssnake.intervaltraining.shared.interval.TrainingServiceConnectors;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.utils.Utils;
import com.vssnake.intervaltraining.wear.interval.IntervalWearService;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created by unai on 22/07/2014.
 */
public class IntervalActivity extends IntervalActivity_Base  implements
        TrainingServiceConnectors.IntervalInterface{

    public static final String TAG = "MainActivityWearable";
    public static final String SEND_INTERVAL_DATA = "/send";

    IntervalWearService.TabataServiceBinder binder;
    Intent mServiceIntent;



    long mIDTrain;



    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        mServiceIntent = new Intent(this,IntervalWearService.class);



        Log.d(TAG,"StartService");
        startService(mServiceIntent);
        bindService(mServiceIntent,mIntervalServiceConnection,Context.BIND_AUTO_CREATE);


        mIDTrain = getIntent().getLongExtra(StacData.TRAIN_ID_KEY,0);

        mWatchViewStub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (binder != null){
                  binder.runTabata();
              }

            }
        });



    }

    @Override
    public void finished() {
        mIntervalName.setText(IntervalStaticData.intervalData.get((int)mIDTrain).getmName());
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(mIntervalServiceConnection);
        stopService(mServiceIntent);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private ServiceConnection mIntervalServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"Service Connected");
            binder = (IntervalWearService.TabataServiceBinder) service;
            binder.setListener(IntervalActivity.this);
            binder.changeTrain(IntervalActivity.this.mIDTrain);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"Service Disconnected");
            binder  = null;

        }
    };


    @Override
    public void changeIntervalMode(final String mode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIntervalState.setText(mode);
            }
        });

    }

    @Override
    public void changeTime(final int secondsTotal,final int secondsInterval) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIntervalTime.setText(Utils.formatTime(secondsInterval));
                mIntervalTotalTime.setText(Utils.formatTime(secondsTotal));
            }
        });

    }

    @Override
    public void changeInterval(final int numberInterval,final int totalInterval) {
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                mIntervalRound.setText(numberInterval + " of " + totalInterval);
            }
        });

    }

    @Override
    public void statusTrain(final boolean status) {


    }

    @Override
    public void specialEvent(TrainingServiceConnectors.specialUICommands commands) {

    }
}
