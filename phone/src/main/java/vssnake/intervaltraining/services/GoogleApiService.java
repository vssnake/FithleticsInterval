package vssnake.intervaltraining.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import vssnake.intervaltraining.interval.TrainingServicesConnectors;

public class GoogleApiService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "WereableService";
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";

    private GoogleApiClient mGoogleApiClient;
    ArrayList<String> nodes; // the connected device to send the message to


    public GoogleApiService() {
    }

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity();
                    break;
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

    public void startActivity(){
        new Thread(){
                 public void run() {
                     nodes = getNodes();

                     MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient,
                             nodes.get(0),START_ACTIVITY_PATH,null).await();
                     if (!result.getStatus().isSuccess()){
                         Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                     }
                }
            }.start();


    }

    private ArrayList<String> getNodes() {
        ArrayList<String> results= new ArrayList<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {

            results.add(node.getId());
        }
        return results;
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
}
