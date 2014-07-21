package vssnake.intervaltraining.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import vssnake.intervaltraining.services.GoogleApiService;


public class Main_Activity extends MainBase_Activity {

    private Messenger mMessenger_GoogleApiService = null;


    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(this,GoogleApiService.class);
        Thread t = new Thread(){
            public void run(){
                startService(intent);
                bindService(intent,mConnection, Context.BIND_ABOVE_CLIENT);
            }
        };
        t.start();
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    private ServiceConnection mConnection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger_GoogleApiService = new Messenger(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger_GoogleApiService = null;
        }
    };


    public Messenger getMessengerGoogApiService() {
        return mMessenger_GoogleApiService;
    }


}
