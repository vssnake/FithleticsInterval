package com.example.unai.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

/**
 * Created by unai on 06/08/2014.
 */
public class GoogleApiService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "WereableService";
    private static final String INTERVAL_ACTION = "interval/action";


    public static GoogleApiClient mGoogleApiClient;
    static ArrayList<String> nodes; // the connected device to send the message to

    public enum TypeNotifications {
        SEND_INTERVAL_ACTION;
    }

    public GoogleApiService() {

    }

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startNotification(TypeNotifications.SEND_INTERVAL_ACTION);
                    break;
                case 2:

                default:
                    super.handleMessage(msg);
            }
        }

    }


    final Messenger mMessenger = new Messenger(new ServiceHandler());



    @Override
    public void onCreate(){
        Log.d(TAG,"StartService");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  mMessenger.getBinder();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected " + connectionHint);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed " + connectionResult);
    }

    public static void startNotification(final TypeNotifications typeNotification){
        new Thread(){
            public void run() {
                MessageApi.SendMessageResult result = null;
                nodes = getNodes();
                switch (typeNotification){
                    case SEND_INTERVAL_ACTION:
                        result = Wearable.MessageApi.sendMessage(mGoogleApiClient,
                                nodes.get(0),INTERVAL_ACTION,null).await();
                        break;
                }



                if (result != null && !result.getStatus().isSuccess()){
                    Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                }
            }
        }.start();


    }


    private static ArrayList<String> getNodes() {
        ArrayList<String> results= new ArrayList<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {

            results.add(node.getId());
        }
        return results;
    }



}
