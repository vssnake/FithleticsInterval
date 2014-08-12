package vssnake.intervaltraining.interval;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.services.GoogleApiService;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabataTrainingBase_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabataTrainingBase_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TabataTraining_Fragment extends TabataTrainingBase_Fragment implements
        TrainingServiceConnectors.IntervalInterface {

    private static final String TAG = "TabataFragment";


    Interval_Service.TabataServiceBinder binder;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().bindService(intent,mConnection, Context.BIND_ABOVE_CLIENT);
        getActivity().startService(intent);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View view =   super.onCreateView(inflater,container,savedInstanceState);

        mSecondFrame.setOnClickListener(botonClick);



        return view;

    }




    private Button.OnClickListener botonClick = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            binder.runTabata();



        }
    };



    @Override
    public void onDestroy(){
        getActivity().unbindService(mConnection);
        super.onDestroy();
    }



    @Override
    public void onPause(){
        super.onPause();
        if (binder != null) {
            binder.runInbackground(true);
            Log.i("Background","true");
        }


    }
    @Override
    public void onResume(){
        super.onResume();
        if (mBound) {
            binder.runInbackground(false);
            Log.i("Background", "false");

        }else {
            getActivity().bindService(intent,mConnection, Context.BIND_ABOVE_CLIENT);
            getActivity().startService(intent);
        }
        GoogleApiService.startNotification(GoogleApiService.TypeNotifications.PREPARE_NOTIFICATION);


    }

    boolean mBound = false;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onServiceDisconnected " + arg0);
            mBound = false;
            binder = null;
            mService = null;


        }

        Interval_Service mService;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected " + name + " " + service);
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            binder = (Interval_Service.TabataServiceBinder) service;
            mService = binder.getService();
            binder.setListener(TabataTraining_Fragment.this);
            mBound = true;

            binder.runInbackground(false);



        };

    };


    @Override
    public void changeIntervalMode(final String mode) {
        main_Activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfoIntervalFragment.changeMode(mode);
            }
        });

    }

    @Override
    public void changeTime(final int secondsTotal,final int secondsInterval) {
        main_Activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChronometerFragment.changeTime(secondsTotal,secondsInterval);
            }
        });

    }

    @Override
    public void changeInterval(final int numberInterval,final int totalInterval) {

        main_Activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfoIntervalFragment.changeRound(numberInterval, totalInterval);
            }
        });

    }





    @Override
    public void specialEvent(final TrainingServiceConnectors.specialUICommands commands) {
        if (isAdded()){
            main_Activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (commands) {
                        case REST:
                            mIntervalClickView.setBackgroundColor(getResources().getColor(R.color.startInterval));
                            mShadowFrame2.setBackground(getResources().getDrawable(R.drawable.shaw_frag_interval));
                            mChronometerFragment.changeIntervalColor(getResources().getColor(R.color.numbers_interval_goo));
                            break;
                        case RUN:
                            mIntervalClickView.setBackgroundColor(getResources().getColor(R.color.rest_Interval));
                            mShadowFrame2.setBackground(getResources().getDrawable(R.drawable.shaw_frag_interval_inv));
                            mChronometerFragment.changeIntervalColor(getResources().getColor(R.color.numbers_interval_rest));
                            break;
                        case END_TRAINING:
                            break;
                    }
                }
            });

        }

    }


    @Override
    public void statusTrain(boolean status) {
         /* if(isAdded()){
                    if (status){
                        mStartCountDownButton.setText(getResources().getString(R.string.stopCountDown));
                    }else{
                        mStartCountDownButton.setText( getResources().getString(R.string.startCountDown));
                    }
                }*/
    }
}
