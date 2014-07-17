package com.example.unai.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService{

private static final String TAG = "WereableService";

private GoogleApiClient mGoogleApiClient;

public static final String START_ACTIVITY_PATH = "/start/MainActivity";

public ListenerService() {
        }




    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
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
