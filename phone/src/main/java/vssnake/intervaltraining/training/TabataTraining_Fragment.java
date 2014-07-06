package vssnake.intervaltraining.training;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        Interval_Service.IntervalServiceListener{


    Interval_Service.TabataServiceBinder binder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       View view =   super.onCreateView(inflater,container,savedInstanceState);
        mStartCountDownButton.setOnClickListener(botonClick);
        return view;

    }



    private Button.OnClickListener botonClick = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {


            //Intent intent = new Intent(main_Activity,Tabata_Service.class);

            binder.runTabata();




        }
    };

    @Override
    public void onPause(){
        if (binder != null) {
            binder.runBackground(true);
        }
        getActivity().unbindService(mConnection);
        super.onPause();
    }
    @Override
    public void onResume(){
        getActivity().bindService(intent,mConnection, Context.BIND_ABOVE_CLIENT);
        getActivity().startService(intent);


        super.onResume();
    }

    boolean mBound = false;
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }

        Interval_Service mService;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            binder = (Interval_Service.TabataServiceBinder) service;
            mService = binder.getService();
            binder.setListener(TabataTraining_Fragment.this);
            mBound = true;
            binder.runBackground(false);

        };

    };


    @Override
    public void changeIntervalMode(String mode) {
        mInfoIntervalFragment.changeMode(mode);
    }

    @Override
    public void changeTime(long secondsTotal, long secondInterval) {
        mChronometerFragment.changeTime(secondsTotal,secondInterval);
    }

    @Override
    public void changeInterval(int numberInterval, int totalInterval) {
        mInfoIntervalFragment.changeRound(numberInterval,totalInterval);
    }
}
