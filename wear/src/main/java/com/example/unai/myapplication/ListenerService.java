package com.example.unai.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.unai.myapplication.model.IntervalData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListenerService extends WearableListenerService{

private static final String TAG = "WereableServiceWear";



public static final String PREPARATIVE_INTERVAL = "/prepare/interval";
public static final String SEND_INTERVAL_DATA = "/send";



    GoogleApiClient mGoogleApiClient;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();
        }

        if (Log.isLoggable(TAG, Log.DEBUG)){
            Log.d(TAG,"onDataChanged :" + dataEvents);
        }

        Log.d(TAG,"MEssage Received");
        final List events = FreezableUtils.freezeIterable(dataEvents);

        ConnectionResult connectionResult =
                mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()){
            Log.e(TAG,"Failed to connect to GoogleApiClient");
        }
           for (DataEvent event: dataEvents){
               switch (event.getType()){
                   case DataEvent.TYPE_CHANGED:
                      if (event.getDataItem().getUri().getPath().equals(SEND_INTERVAL_DATA)){
                          DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                          DataMap dataMap = dataMapItem.getDataMap();

                             IntervalData data = new IntervalData();
                                  data.setIntervalData(
                                  dataMap.getInt(IntervalData.intervalDataKey.NUMBER_INTERVAL
                                          .name()),
                                  dataMap.getInt(IntervalData.intervalDataKey.TOTAL_INTERVALS
                                          .name()),
                                  IntervalData.eIntervalState.RUNNING,
                                  dataMap.getLong(IntervalData.intervalDataKey.TOTAL_INTERVAL_TIME
                                          .name()),
                                  dataMap.getLong(IntervalData.intervalDataKey.INTERVAL_TIME
                                          .name()),
                                  0);

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

        if (messageEvent.getPath().equals(PREPARATIVE_INTERVAL)) {
            long token = Binder.clearCallingIdentity();
            try {
                Intent startIntent = new Intent(this, MyActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
            } finally {
                Binder.restoreCallingIdentity(token);
            }

        }
    }


        }
