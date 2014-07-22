package com.example.unai.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {



        if (messageEvent.getPath().equals(PREPARATIVE_INTERVAL)) {

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,
                    new Intent(this, MyActivity.class),0);
            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat
                    .WearableExtender();


            Notification notif = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Interval Ready")
                    .setContentText("Interval Ready to start, push to run the app")
                    .setSmallIcon(R.drawable.hiit)
                    .extend(wearableExtender)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManagerCompat  notifManager = NotificationManagerCompat.from
                    (getApplicationContext());

            notifManager.notify(1,notif);

        }
    }


        }