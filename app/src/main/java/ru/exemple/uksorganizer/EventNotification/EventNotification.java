package ru.exemple.uksorganizer.EventNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class EventNotification {

    private static final String NOTIFICATION_CHANNEL_ID_AND_NAME = "By the way";
    private static final int NOTIFICATION_TAG = 0;
    private static final String WORK_TAG = "notificationWork";
    private static final String EVENT_NAME_KEY = "name";
    private static final String EVENT_CATEGORY_KEY = "category";
    private static final String EVENT_DESCRIPTION_KEY = "description";
    private static final String EVENT_TIME_KEY = "time";
    private static final String EVENT_PRIORITY_KEY = "priority";

    private Context context;
    private Event event;

    public EventNotification(Context context, Event event){
        this.context = context;
        this.event = event;
    }

    public void createNotification() {
        String notificationTitle = event.getCategory() + " EVENT: " + event.getName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, hh:mm", Locale.getDefault());
        String time = simpleDateFormat.format(System.currentTimeMillis());
        String notificationText = time + "; " + event.getDescription();
        PendingIntentProvider intentProvider = new PendingIntentProvider(context, event);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_AND_NAME)
                        .setSmallIcon(R.drawable.ic_notification_important_24px)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationText)
                        .setContentIntent(intentProvider.getPendingIntent())
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (event.getPriority() > 0) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        Notification notification = notificationBuilder.build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_TAG, notification);
    }

    public void createNotificationChannel() {

        String description = "By the way notification channel";

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

    public void createWorkNotification() {

        Data.Builder builder = new Data.Builder()
                .putString(EVENT_NAME_KEY, event.getName())
                .putString(EVENT_CATEGORY_KEY, event.getCategory().toString())
                .putString(EVENT_DESCRIPTION_KEY, event.getDescription())
                .putLong(EVENT_TIME_KEY, event.getTime())
                .putInt(EVENT_PRIORITY_KEY, event.getPriority());

        Data inputData = builder.build();
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(calculateDelay(event.getTime()), TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(WORK_TAG)
                .build();

        WorkManager.getInstance(context).enqueue(notificationWork);
    }

    private long calculateDelay(long targetTime) {
        return targetTime - System.currentTimeMillis();
    }
}
