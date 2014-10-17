package vssnake.intervaltraining.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.wearable.WearableService;


public class Main_Activity extends MainBase_Activity {

    private Messenger mMessenger_GoogleApiService = null;

    SharedPreferences mSharedPreferences;

    Intent googleServiceIntent;
    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialize();
        super.onCreate(savedInstanceState);
        googleServiceIntent  = new Intent(this,WearableService.class);
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


    void initialize(){
       mSharedPreferences = getSharedPreferences(StacData.BASIC_CONFIG_PREFS, 0);
       IntervalStaticData.getIntervalData(this);
       int idTraining =  mSharedPreferences.getInt(StacData.PREFS_TRAIN_KEY,-1);
        if (idTraining== -1 || idTraining >= IntervalStaticData.intervalData.size()){
            mSharedPreferences.edit().putInt(StacData.PREFS_TRAIN_KEY,0).commit();

        }
    }
}
