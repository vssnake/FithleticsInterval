package vssnake.intervaltraining.services;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import vssnake.intervaltraining.main.Main_Activity;

/**
 * Created by unai on 21/07/2014.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {

    private static final String TAG = "WereableServicePhone";


    private static List<WeakReference<WearableListenerConnector>> mConnectors =
            new ArrayList<WeakReference<WearableListenerConnector>>();




    private static final String INTERVAL_ACTION = "interval/action";

    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    public static final String DATA_ITEM_INTERVAL_RECEIVED_PATH = "/data-item-interval-received";


    public static int  addListener(WearableListenerConnector connector){
        if (mConnectors.contains(connector)){
            return -1;
        }
        mConnectors.add(new WeakReference<WearableListenerConnector>(connector));
        return mConnectors.size();
    }
    public static boolean removeListener(int handlerID){
        try {
            mConnectors.remove(handlerID);
            return true;

        }catch(IndexOutOfBoundsException e){
            return false;
        }

    }


    @Override
    public void onDataChanged(DataEventBuffer dataEvents){


    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(INTERVAL_ACTION)) {
            sendHandler(WearableListenerConnector.typeMessage.action,null);
        }

    }

    void sendHandler(WearableListenerConnector.typeMessage message, Bundle data){
        int connectorsSize = mConnectors.size();
       for (int i=0;connectorsSize> i; i++){
           WearableListenerConnector connector = mConnectors.get(i).get();
           if (connector!= null){
               connector.onMessageReceived(message,data);
           }
       }
    }
}
