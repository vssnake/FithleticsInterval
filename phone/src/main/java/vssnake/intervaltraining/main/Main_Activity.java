package vssnake.intervaltraining.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

import vssnake.intervaltraining.services.GoogleApiService;


public class Main_Activity extends MainBase_Activity {

    private Messenger mMessenger_GoogleApiService = null;


    Intent googleServiceIntent;
    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleServiceIntent  = new Intent(this,GoogleApiService.class);
        startService(googleServiceIntent);
        bindService(googleServiceIntent, mGoolgleApiConnection, Context.BIND_ABOVE_CLIENT);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(mGoolgleApiConnection);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };



    private ServiceConnection mGoolgleApiConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger_GoogleApiService = new Messenger(service);
            onNavigationDrawerItemSelected(2); //HardCode

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
