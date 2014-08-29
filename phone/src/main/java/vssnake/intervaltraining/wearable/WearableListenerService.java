package vssnake.intervaltraining.wearable;

import android.os.Bundle;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by unai on 21/07/2014.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {

    private static final String TAG = "WereableServicePhone";


    private static List<WeakReference<WearableListenerInterface>> mConnectors =
            new ArrayList<WeakReference<WearableListenerInterface>>();




    private static final String INTERVAL_ACTION = "interval/action";
    private static final String INTERVAL_REQUEST_DEFAULT = "interval/request/default";

    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    public static final String DATA_ITEM_INTERVAL_RECEIVED_PATH = "/data-item-interval-received";


    public static int  addListener(WearableListenerInterface connector){
        if (mConnectors.contains(connector)){
            return -1;
        }
        mConnectors.add(new WeakReference<WearableListenerInterface>(connector));
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
            sendHandler(WearableListenerInterface.typeMessage.action,null);
        }else if (messageEvent.getPath().equals(INTERVAL_REQUEST_DEFAULT)){
            sendHandler(WearableListenerInterface.typeMessage.request_data,null);
        }

    }

    void sendHandler(WearableListenerInterface.typeMessage message, Bundle data){
        int connectorsSize = mConnectors.size();
       for (int i=0;connectorsSize> i; i++){
           WearableListenerInterface connector = mConnectors.get(i).get();
           if (connector!= null){
               connector.onMessageReceived(message,data);
           }
       }
    }
}
