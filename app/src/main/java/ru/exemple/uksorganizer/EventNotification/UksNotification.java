package ru.exemple.uksorganizer.EventNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class UksNotification {

    private final static String manasia_notification_channel = "Manasia Event Reminder";
    private static final String NOTIFICATION_CHANNEL_ID_AND_NAME = "By the way";
    private static final int NOTIFICATION_TAG = 0;

    private Context context;
    private Event event;
    private PendingIntentProvider intentProvider;

    public UksNotification(Context context, Event event){
        this.context = context;
        this.event = event;
    }

    public void createNotification() {
        String notificationTitle = "Event: " + event.getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, hh:mm", Locale.getDefault());
        String time = simpleDateFormat.format(System.currentTimeMillis());
        String notificationText = time + "; " + event.getDescription();
        intentProvider = new PendingIntentProvider(context, event);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_AND_NAME)
                        .setSmallIcon(R.drawable.ic_notification_important_24px)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(intentProvider.getPendingIntent())
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //trigger the notification
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        //we give each notification the ID of the event it's describing,
        //to ensure they all show up and there are no duplicates
        notificationManager.notify(NOTIFICATION_TAG, notificationBuilder.build());
    }

    public void createNotificationChannel() {

        String description = "By the way";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID_AND_NAME,
                            NOTIFICATION_CHANNEL_ID_AND_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);

            NotificationManager notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
