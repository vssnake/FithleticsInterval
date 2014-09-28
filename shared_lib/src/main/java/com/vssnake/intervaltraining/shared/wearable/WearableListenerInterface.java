package com.vssnake.intervaltraining.shared.wearable;

import android.os.Bundle;

/**
 * Created by unai on 06/08/2014.
 */
public interface WearableListenerInterface {
    public enum typeMessage{
        action,
        request_data;
    }

    void onMessageReceived(typeMessage message, Bundle data);
}
