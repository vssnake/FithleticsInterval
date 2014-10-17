package com.vssnake.intervaltraining.shared.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.hardware.Camera;

import java.nio.ByteBuffer;

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

    public static String formatTime(int seconds){
        String result ="";
        int hours = seconds /60/60;
        if (hours != 0){
           result += String.format("%02d",hours) + ":";
            seconds = seconds - hours*3600;
        }
      result += String.format("%02d",seconds/60) + ":"
              +  String.format("%02d",seconds%60);
       return result;
    }
    public static int convertMillisecondsToSeconds(long milliseconds){
        return (int)Math.ceil(milliseconds/1000d);
    }





    public static byte[] intToByte(int data){
       return ByteBuffer.allocate(32).putInt(data).array();
    }
    public static int arrayBytesToInt(byte[] data){
       return  ByteBuffer.wrap(data).getInt();
    }


    public static int getPixels(Context context,int dps){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
}
