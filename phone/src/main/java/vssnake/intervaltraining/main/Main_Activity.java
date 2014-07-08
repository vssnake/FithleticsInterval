package vssnake.intervaltraining.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import vssnake.intervaltraining.model.TrainingBase;


public class Main_Activity extends MainBase_Activity {


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


}
