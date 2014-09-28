package com.vssnake.intervaltraining.wear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.unai.intervaltraining.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService{

private static final String TAG = "WereableServiceWear";



public static final String PREPARATIVE_INTERVAL = "/prepare/interval";
public static final String SEND_INTERVAL_DATA = "/send";
public static final String INTERVAL_VIBRATION = "/interval/vibration";



    GoogleApiClient mGoogleApiClient;



    @Override
    public void onDataChanged(DataEventBuffer dataEvents){

    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {



        if (messageEvent.getPath().equals(PREPARATIVE_INTERVAL)) {

            NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
            bigStyle.bigPicture(BitmapFactory.decodeResource(getResources(),
                    R.drawable.hiit));


            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,
                    new Intent(this, IntervalActivityListener.class),0);
            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat
                    .WearableExtender();
            wearableExtender.setHintHideIcon(true);
            wearableExtender.setBackground((BitmapFactory.decodeResource(getResources(),
                    R.drawable.a_track)));


            Notification notif = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(getResources().getString(R.string.interval_ready))
                    .setContentText(getResources().getString(R.string.interval_ready_context))
                    .setSmallIcon(R.drawable.hiit)
                    .extend(wearableExtender)
                    //.setStyle(bigStyle)
                    .addAction(R.drawable.hiit,
                            "Run Interval", pendingIntent)
                   // .setContentIntent(pendingIntent)
                    .build();

            NotificationManagerCompat  notifManager = NotificationManagerCompat.from
                    (getApplicationContext());

            notifManager.notify(1,notif);

        }
    }


        }
