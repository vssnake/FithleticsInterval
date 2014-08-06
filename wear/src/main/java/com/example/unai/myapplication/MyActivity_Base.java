package com.example.unai.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.example.unai.myapplication.utils.Utils;

public class MyActivity_Base extends Activity {

    TextView mIntervalTime;
    TextView mIntervalTotalTime;
    TextView mIntervalState;
    TextView mIntervalRound;
    TextView mIntervalHeartRate; //Not implemented yet


    protected PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mIntervalTime = (TextView) stub.findViewById(R.id.interval_time);
                mIntervalTotalTime = (TextView) stub.findViewById(R.id.interval_totalTime);
                mIntervalState = (TextView) stub.findViewById(R.id.interval_State);
                mIntervalRound = (TextView) stub.findViewById(R.id.interval_round);

                mIntervalTime.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalTotalTime.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalState.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalRound.setTypeface(Utils.getFontRoboto_black(getAssets()));

            }
        });

        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

       // Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
      //  v.vibrate(500);
    }

    public void onDestroy(){
        super.onDestroy();
        this.mWakeLock.release();
    }
}
