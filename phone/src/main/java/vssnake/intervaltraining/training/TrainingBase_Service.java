package vssnake.intervaltraining.training;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class TrainingBase_Service extends Service {


    public enum stateFlag {
        running,
        stop,
        paused
    }
    private NotificationManager nNM;

    private static stateFlag serviceState = stateFlag.stop;

    public static stateFlag getStateService(){
        return serviceState;
    }

    public TrainingBase_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        //Set the flag to running
        serviceState = stateFlag.running;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy(){
        //Set the flag to Stop
        serviceState = stateFlag.stop;


    }
}
