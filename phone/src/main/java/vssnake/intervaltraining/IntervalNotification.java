package vssnake.intervaltraining;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


/**
 * Helper class for showing and canceling interval
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class IntervalNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Interval";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of interval notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static Notification notify(final Context context,final String intervalName,
            final int numberInterval, final int totalIntervals,final String stateInterval,
            final String intervalTime, final String totalTime,PendingIntent showIntent,
            PendingIntent closeIntent) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.example_picture);




       Notification notification = createNotification(context,intervalName,numberInterval,totalIntervals,
               stateInterval,intervalTime,totalTime,showIntent,closeIntent);
        notify(context, notification);
        return notification;
    }

    public static Notification createNotification(final Context context,final String intervalName,
                                                  final int numberInterval, final int totalIntervals,
                                                  final String stateInterval,final String intervalTime,
                                                  final String totalTime,PendingIntent showIntent,
                                                  PendingIntent closeIntent){



        RemoteViews contentView = new RemoteViews(context.getPackageName(),
                R.layout.notification_interval);
        contentView.setTextViewText(R.id.notifIntervalName_TextView,intervalName);
        contentView.setTextViewText(R.id.notifIntervalRound_TextView,numberInterval+ "/" + totalIntervals);
        contentView.setTextViewText(R.id.notifIntervalState_TextView,stateInterval);
        contentView.setTextViewText(R.id.notifIntervalTime_TextView,intervalTime);
        contentView.setTextViewText(R.id.notifIntervalTotalTime_TextView,totalTime);
        contentView.setOnClickPendingIntent(R.id.notifIntervalClose_Button,closeIntent);




        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, SOUND,
                // and VIBRATION.
                .setDefaults(Notification.DEFAULT_LIGHTS)

                        // Set required fields, including the small icon, the
                        // notification title, and text.
                .setSmallIcon(R.drawable.hiit)
                        //.setContentTitle(title)
                        //.setContentText(text)

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                .setContentIntent(showIntent)


                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_LIGHTS;

        //   notification.contentView = contentView;
        notification.bigContentView = contentView;

        return notification;


    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 1024, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link (Context, String, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}