package ru.exemple.uksorganizer.eventNotification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ru.exemple.uksorganizer.model.Event;

public class NotifyWorker extends Worker {

    public static final String EVENT_NAME_KEY = "name";
    public static final String EVENT_CATEGORY_KEY = "category";
    public static final String EVENT_DESCRIPTION_KEY = "description";
    public static final String EVENT_TIME_KEY = "time";
    public static final String EVENT_PRIORITY_KEY = "priority";

    public NotifyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString(EVENT_NAME_KEY);
        String categoryString = getInputData().getString(EVENT_CATEGORY_KEY);
        Event.Category category;
        try {
            category = Event.Category.valueOf(categoryString);
        } catch (IllegalArgumentException e) {
            category = Event.Category.CATEGORY;
        } catch (NullPointerException e) {
            category = Event.Category.CATEGORY;
        }
        String description = getInputData().getString(EVENT_DESCRIPTION_KEY);
        long time = getInputData().getLong(EVENT_TIME_KEY, System.currentTimeMillis());
        int priority = getInputData().getInt(EVENT_PRIORITY_KEY, 0);
        Event event = new Event(name, category, description, time, priority);

        EventNotification eventNotification = new EventNotification(getApplicationContext(), event);

        eventNotification.createNotification();

        return Result.success();
    }
}
