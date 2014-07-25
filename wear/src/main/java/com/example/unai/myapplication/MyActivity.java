package com.example.unai.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.example.unai.myapplication.model.IntervalData;
import com.example.unai.myapplication.utils.Utils;
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

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by unai on 22/07/2014.
 */
public class MyActivity extends MyActivity_Base  implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener{

    public static final String TAG = "MainActivityWearable";




    GoogleApiClient mGoogleApiClient;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)


                .addOnConnectionFailedListener(this)
                .build();

    }
    @Override
    public void onConnected(Bundle bundle) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Connected to Google Api Service");
        }
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient,this);


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
                                dataMap.getString(IntervalData.intervalDataKey.INTERVAL_STATE
                                        .name()),
                                dataMap.getLong(IntervalData.intervalDataKey.TOTAL_INTERVAL_TIME
                                        .name()),
                                dataMap.getLong(IntervalData.intervalDataKey.INTERVAL_TIME
                                        .name()),
                                0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mIntervalRound.setText(data.getNumberInterval() + " of " + data
                                        .getTotalIntervals());
                                mIntervalTime.setText(Utils.formatIntervalTime(data.
                                        getIntervalTime()));
                                mIntervalTotalTime.setText(Utils.formatIntervalTime(data
                                        .getTotalIntervalTime()));
                                mIntervalState.setText(data.getIntervalState().name());
                            }
                        });

                        Log.d(TAG,data.getNumberInterval() + " " +data.getIntervalTime() + " " +
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


}
