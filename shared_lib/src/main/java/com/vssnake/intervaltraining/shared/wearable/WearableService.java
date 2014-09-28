package com.vssnake.intervaltraining.shared.wearable;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
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
import com.vssnake.intervaltraining.shared.utils.Utils;

import java.util.ArrayList;



import static com.vssnake.intervaltraining.shared.wearable.WearableService.TypeNotifications.PREPARE_NOTIFICATION;

public class WearableService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "WereableService";
    public static final String PREPARATIVE_INTERVAL = "/prepare/interval";
    public static final String INTERVAL_VIBRATION = "/interval/vibration";
    public static final String SEND_INTERVAL_DATA = "/send";

    public static GoogleApiClient mGoogleApiClient;
    static ArrayList<String> nodes; // the connected device to send the message to

    public enum TypeNotifications {
        PREPARE_NOTIFICATION,
        VIBRATION_NOTIFICATION,
    }

    public WearableService() {

    }

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startNotification(PREPARE_NOTIFICATION);


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
                         case PREPARE_NOTIFICATION:
                             if (nodes.size() >= 1){
                                 result = Wearable.MessageApi.sendMessage(mGoogleApiClient,
                                         nodes.get(0),PREPARATIVE_INTERVAL,null).await();
                                 break;
                             }

                     }



                     if (result != null && !result.getStatus().isSuccess()){
                         Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                     }
                }
            }.start();


    }

    public static void startNotification(final TypeNotifications typeNotification, final int intValue){
        new Thread(){
            public void run() {
                MessageApi.SendMessageResult result = null;
                nodes = getNodes();
                switch (typeNotification){
                    case VIBRATION_NOTIFICATION:
                        if (nodes.size() >= 1) {
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient,
                                    nodes.get(0), INTERVAL_VIBRATION, Utils.intToByte(intValue)).await();
                        }

                        break;
                }



                if (result != null && !result.getStatus().isSuccess()){
                    Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                }
            }
        }.start();
    }

    public static void setDataMap(String patch, DataMap dataMap){

        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(patch);


        dataMapRequest.getDataMap().putAll(dataMap);


        PutDataRequest request = dataMapRequest.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient,request);

               /* if (!pendingResult.await().getStatus().isSuccess()){
                    Log.e(TAG, "ERROR: failed to send Message: " + pendingResult.await().getStatus().getStatus());
                }*/




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
