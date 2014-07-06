package vssnake.intervaltraining.utils;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import vssnake.intervaltraining.training.TabataTrainingBase_Fragment;

/**
 * Created by unai on 27/06/2014.
 */
public class Utils {

    public static String BINDER_KEY = "messenger";


    public static String composeIntervalNotificationTitle(String typeInterval,String mode){
        return typeInterval + " (" + mode + ")";
    }
    public static String composeIntervalNotificationSubtitle(int interval, int TotalIntervals){
        //return typeInterval + " (" + mode + ")";
        return null;
    }

    public static String formatIntervalTime(long secondsInterval){
       return String.format("%02d",secondsInterval/60) + ":"
               +  String.format("%02d",secondsInterval%60);
    }
    public static String formatTotalIntervalTime(long secondsTotal){
        return String.format("%02d",secondsTotal/60) + ":"
                +  String.format("%02d",secondsTotal%60);
    }
}
