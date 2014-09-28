package com.vssnake.intervaltraining.wear;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.example.unai.intervaltraining.R;
import com.vssnake.intervaltraining.shared.utils.Utils;


public abstract class IntervalActivity_Base extends Activity {

    WatchViewStub mWatchViewStub;
    TextView mIntervalTime;
    TextView mIntervalTotalTime;
    TextView mIntervalState;
    TextView mIntervalRound;
    TextView mIntervalHeartRate; //Not implemented yet
    TextView mIntervalName;


    protected PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mWatchViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        mWatchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mIntervalTime = (TextView) stub.findViewById(R.id.interval_time);
                mIntervalTotalTime = (TextView) stub.findViewById(R.id.interval_totalTime);
                mIntervalState = (TextView) stub.findViewById(R.id.interval_State);
                mIntervalRound = (TextView) stub.findViewById(R.id.interval_round);
                mIntervalName = (TextView) stub.findViewById(R.id.interval_Name);
                mIntervalTime.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalTotalTime.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalState.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalRound.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mIntervalName.setTypeface(Utils.getFontRoboto_black(getAssets()));
                finished();

            }
        });



        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();


    }
    public abstract void finished();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mIntervalTime", mIntervalTime.getText().toString());
        outState.putString("mIntervalTotalTime", mIntervalTotalTime.getText().toString());
        outState.putString("mIntervalState", mIntervalState.getText().toString());
        outState.putString("mIntervalRound", mIntervalRound.getText().toString());
        outState.putString("mIntervalName", mIntervalName.getText().toString());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIntervalTime.setText(savedInstanceState.getString("mIntervalTime"));
        mIntervalTotalTime.setText(savedInstanceState.getString("mIntervalTotalTime"));
        mIntervalState.setText(savedInstanceState.getString("mIntervalState"));
        mIntervalRound.setText(savedInstanceState.getString("mIntervalRound"));
        mIntervalName.setText(savedInstanceState.getString("mIntervalName"));


    }

    public void onDestroy(){
        super.onDestroy();
        this.mWakeLock.release();
    }
}
