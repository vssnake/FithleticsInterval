package vssnake.intervaltraining.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by unai on 27/06/2014.
 */
public class Utils {

    public static String BINDER_KEY = "messenger";

    public  static Typeface roboto_black;
    private static Typeface roboto_regular;

    public static Typeface getFontRoboto_black(AssetManager asset){
        if (roboto_black == null){
            roboto_black = Typeface.createFromAsset(asset,"roboto_black.ttf");
        }
        return roboto_black;
    }

    public static Typeface getFontRoboto_regular(AssetManager asset){
        if (roboto_regular == null){
            roboto_regular = Typeface.createFromAsset(asset,"roboto_regular.ttf");
        }
        return roboto_regular;
    }

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
