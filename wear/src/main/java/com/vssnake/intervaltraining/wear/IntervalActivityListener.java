package com.vssnake.intervaltraining.wear;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.vssnake.intervaltraining.shared.model.IntervalData;
import com.vssnake.intervaltraining.shared.utils.Utils;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by unai on 22/07/2014.
 */
public class IntervalActivityListener extends IntervalActivity_Base implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener{

    public static final String TAG = "MainActivityWearable";
    public static final String SEND_INTERVAL_DATA = "/send";

    private Messenger mMessenger_GoogleApiService = null;

    Intent intent;

    GoogleApiClient mGoogleApiClient;


    @Override
    public void onConnected(Bundle bundle) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Connected to Google Api Service");
        }
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        GoogleApiService.startNotification(GoogleApiService.TypeNotifications.SEND_REQUEST_DEFAULT_DATA);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();

        }

        if (Log.isLoggable(TAG, Log.DEBUG)){
            Log.d(TAG,"onDataChanged :" + dataEvents);
        }

        Log.d(TAG,"Message Received");
        final List events = FreezableUtils.freezeIterable(dataEvents);

        ConnectionResult connectionResult =
                mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()){
            Log.e(TAG,"Failed to connect to GoogleApiClient");
        }
        for (DataEvent event: dataEvents){
            switch (event.getType()){
                case DataEvent.TYPE_CHANGED:
                    if (event.getDataItem().getUri().getPath().equals(ListenerService.
                            SEND_INTERVAL_DATA)){
                        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                        DataMap dataMap = dataMapItem.getDataMap();

                        final IntervalData data = new IntervalData();
                        data.setIntervalData(
                                dataMap.getInt(IntervalData.intervalDataKey.NUMBER_INTERVAL
                                        .name()),
                                dataMap.getInt(IntervalData.intervalDataKey.TOTAL_INTERVALS
                                        .name()),

                                IntervalData.eIntervalState.valueOf(
                                        dataMap.getString(IntervalData.intervalDataKey.INTERVAL_STATE
                                        .name())),
                                dataMap.getLong(IntervalData.intervalDataKey.TOTAL_INTERVAL_TIME
                                        .name()),
                                dataMap.getLong(IntervalData.intervalDataKey.INTERVAL_TIME
                                        .name()),
                                0);
                        data.setName(dataMap.getString(IntervalData.intervalDataKey.INTERVAL_NAME
                                .name()));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIntervalRound.setText(data.getNumberInterval() + " of " + data
                                        .getTotalIntervals());
                                mIntervalTime.setText(Utils.formatTime(data.
                                        getIntervalTimeSeconds()));
                                mIntervalTotalTime.setText(Utils.formatTime(data
                                        .getTotalIntervalTimeSeconds()));
                                mIntervalState.setText(data.getIntervalState().name());
                                mIntervalName.setText(data.getName());
                            }
                        });

                        Log.d(TAG,data.getNumberInterval() + " " +data.getTotalIntervalTimeSeconds() + " " +
                                data.getBpm() + " " + data.getTotalIntervals() + " " +
                                data.getTotalIntervals() + " " + data.getIntervalState());

                    }
                    break;
                case DataEvent.TYPE_DELETED:
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG,"onMessageReceived " + messageEvent);
        if (messageEvent.getPath().equals(ListenerService.INTERVAL_VIBRATION)) {
            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(Utils.arrayBytesToInt(messageEvent.getData()));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        intent = new Intent(getApplicationContext(),GoogleApiService.class);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        Log.d(TAG,"StartService");
        startService(intent);
        bindService(intent,mGoolgleApiConnection,Context.BIND_AUTO_CREATE);


        mWatchViewStub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if (mMessenger_GoogleApiService != null){
                    //Second parameter is the code
                    Message msg = Message.obtain(null, 0, 0, 0);

                        GoogleApiService.startNotification(GoogleApiService.TypeNotifications.SEND_INTERVAL_ACTION);

              //  }
            }
        });
    }

    @Override
    public void finished() { }


    @Override
    public void onDestroy(){
        super.onDestroy();
        unbindService(mGoolgleApiConnection);
        stopService(intent);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient,this);

            mGoogleApiClient.disconnect();

        }
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

    private ServiceConnection mGoolgleApiConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"Service Connected");
            mMessenger_GoogleApiService = new Messenger(service);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"Service Disconnected");
            mMessenger_GoogleApiService = null;

        }
    };


}
