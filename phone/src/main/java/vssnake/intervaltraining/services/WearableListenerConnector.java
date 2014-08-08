package vssnake.intervaltraining.services;

import android.os.Bundle;

/**
 * Created by unai on 06/08/2014.
 */
public interface WearableListenerConnector {
    public enum typeMessage{
        action;
    }

    void onMessageReceived(typeMessage message, Bundle data);
}
