package vssnake.intervaltraining.services;

import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by unai on 21/07/2014.
 */
public class WearableListenerService extends com.google.android.gms.wearable.WearableListenerService {

    private static final String TAG = "WereableServicePhone";

    private GoogleApiClient mGoogleApiClient;

    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    public static final String DATA_ITEM_INTERVAL_RECEIVED_PATH = "/data-item-interval-received";

    @Override
    public void onCreate(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){


    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {


    }
}
